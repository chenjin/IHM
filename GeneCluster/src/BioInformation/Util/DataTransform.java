package BioInformation.Util;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mortennobel.imagescaling.ProgressListener;
import com.mortennobel.imagescaling.ResampleOp;

import BioInformation.DataTransfer.ClusteredData;
import BioInformation.DataTransfer.GridSingleton;
import BioInformation.DataTransfer.Square;
import BioInformation.EdgeDetection.CannyEdgeDetector;
import BioInformation.EdgeDetection.EdgeDetectorException;
import BioInformation.EdgeDetection.IEdgeDetector;
import BioInformation.Task.GridSearchTask;
import BioInformation.Task.Image2ArrayTask;
import BioInformation.quadtree.QuadTree;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;
import weka.filters.unsupervised.instance.Resample;

public class DataTransform {
	static Logger logger =Logger.getLogger("error");
	public static Queue<Square> squareToSplit = new ConcurrentLinkedQueue<Square>();//squares needs further spliting.
    public static Queue<List<Integer>> squareReady = new ConcurrentLinkedQueue<List<Integer>>();//squares don't need further splitting.
    static ExecutorService executor =Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static int readyCnt =0;
	/*private static IEdgeDetector edgeDetector;
	static{
		edgeDetector =CannyEdgeDetector.getInstance();
	}*/
	public static BufferedWriter bufferWritter;      
    public static Instances resampling(Instances instances,int sampleRate){
    	String[] options ={"-S","1","-Z",String.valueOf(sampleRate)};//get -Z% percent of all genes.
		Resample randomSampling =new Resample();
		try {
			randomSampling.setOptions(options);
		    randomSampling.setInputFormat(instances);
		    instances =Filter.useFilter(instances, randomSampling);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instances;
    }
    public static Instances pca(Instances instances)  {
    	String[] pc_options ={"-M","2"};
		PrincipalComponents principalComponents =new PrincipalComponents();
		
		weka.attributeSelection.PrincipalComponents pca = new weka.attributeSelection.PrincipalComponents();
		try {
			pca.buildEvaluator(instances);
			System.out.println(pca);
			//pca.getEigenValues();
			//pca.getUnsortedEigenVectors();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		Instances reducedInstances = null;
		//reduce the number of attribute to 2 
		try {
			principalComponents.setOptions(pc_options);
			principalComponents.setInputFormat(instances);
			reducedInstances = Filter.useFilter(instances,principalComponents);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return reducedInstances;
    }
  
    /**
     * Array2Image setRGB for greyscale data should be (pixel*256+pixel)*256+pixel,pixel over 255 may return a color,but makes no sense.
     * @param data
     * @param filedir
     * @return
     * @throws IOException
     */
    public static BufferedImage Array2Image(Square data[][],String filedir) throws IOException{
    	OutputStream output = new FileOutputStream(new File(filedir));
    	BufferedImage br=new BufferedImage(data[0].length, data.length, BufferedImage.TYPE_INT_RGB);
    
    	//List<Array2ImageTask> array2ImageTaskList = new ArrayList<Array2ImageTask>();
        for(int y=0;y<data.length;y++){
            for(int x=0;x<data[0].length;x++){
            	int val =(int)data[y][x].getPointIds().size();
            	int rgb = ((val*256+val)*256+val);
            	br.setRGB(x, y,rgb );//设置像素
                
            	//array2ImageTaskList.add(new Array2ImageTask(data,br,y,x));
            }
        }
        /*try {
			List<Future<BufferedImage>> results =executor.invokeAll(array2ImageTaskList);
			System.out.println("Wait for Array2Image result."+results.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
     
        ImageIO.write(br, "jpg", output);
        output.close();
        return br;
    }
    /**
     * A override method change the type of double
     * @param data
     * @param filedir
     * @return
     * @throws IOException
     */
    public static BufferedImage Array2Image(double data[][],String filedir) throws IOException{
    	//OutputStream output = new FileOutputStream(new File(filedir));
       
        //BufferedImage br=new BufferedImage(data[0].length, data.length, BufferedImage.TYPE_INT_RGB);
    	BufferedImage br=new BufferedImage(data[0].length, data.length, BufferedImage.TYPE_INT_RGB);
    	 
        for(int y=0;y<data.length;y++){
            for(int x=0;x<data[0].length;x++){	
            	int val =(int)data[y][x];
            	int rgb = ((val*256+val)*256+val);            
                br.setRGB(x, y,rgb );//设置像素
            }
        }
        //ImageIO.write(br, "jpg", output);
        return br;
    }
    
    /**
     * convert Image 2 array
     * @param edgeImage
     * @param filedir
     * @param squares
     * @param k
     * @return
     * @throws IOException 
     */
    public static int[][] Image2Array(BufferedImage bufferedEdgeImage,String filedir,Square[][] squares,int k) throws IOException{
         int [][]edgeData = new int[bufferedEdgeImage.getHeight()][bufferedEdgeImage.getWidth()];
         bufferWritter = new BufferedWriter(new FileWriter("D:\\code\\edge_data.txt"));
         List<Image2ArrayTask> image2ArrayTask = new ArrayList<Image2ArrayTask>();
         for(int y=0;y<bufferedEdgeImage.getHeight();y++){
         	for(int x=0;x<bufferedEdgeImage.getWidth();x++){
         		/*edgeData[y][x] = bufferedEdgeImage.getRGB(x, y)&0xFF;
         		int numOfPointInSquare = squares[y][x].getPointIds().size();
         		if(numOfPointInSquare>0){
	         		if(edgeData[y][x] < 255 ){
	         			squareToSplit.offer(squares[y][x]);
	         		}else{
	         			squareReady.add(squares[y][x].getPointIds());
	         			readyCnt += numOfPointInSquare;
	         		}
         		}
         		bufferWritter.write(edgeData[y][x]+" ");*/
         		//executor.submit(new Image2ArrayTask(bufferedEdgeImage,edgeData,squares,y,x));
         	    image2ArrayTask.add(new Image2ArrayTask(bufferedEdgeImage,edgeData,squares,y,x));
                //Integer val =(Integer)tm.getResult();
         	}
         	//bufferWritter.newLine();
         	//System.out.println();
         }
        try {
			List<Future<int[][]>> results =executor.invokeAll(image2ArrayTask);
			//System.out.println("Wait for image2Array result."+results.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        bufferWritter.close();
		ImageIO.write(bufferedEdgeImage,"jpg",new File(filedir));
        return edgeData;
    }
    /**
     * 
     * @param bufferedImage
     * @return
     */
    public static double[][] Image2Array(BufferedImage bufferedImage){
        double [][]edgeData = new double[bufferedImage.getHeight()][bufferedImage.getWidth()];
        for(int y=0;y<bufferedImage.getHeight();y++){
        	for(int x=0;x<bufferedImage.getWidth();x++){
        		edgeData[y][x] = bufferedImage.getRGB(x, y)&0xFF;
        	}
        }
       return edgeData;
   }
    /**  
     * 生成32位编码  
     * @return string  
     */    
    public static String getUUID(){    
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");    
        return uuid;    
    }
    /**
     * 从quadtree和hashmap中生成格子个数的矩阵，格子的个数为4^k
     * @param xMin
     * @param yMin
     * @param xMax
     * @param yMax
     * @param k
     * @return
     * @throws IOException 
     */
    public static void getNumberInGrids(int k) throws IOException{
    	QuadTree qt =GridSingleton.getInstance().getQuadTree();
    	int numOfGrid = (int)Math.pow(2, k);
    	while(!squareToSplit.isEmpty()){
    		Square squareSpliting = squareToSplit.poll();
    		Square[][] squares = new Square[numOfGrid][numOfGrid];
	    	double xMin =squareSpliting.getMinX();
	    	double yMin =squareSpliting.getMinY();
	    	double xMax =squareSpliting.getMaxX();
	    	double yMax =squareSpliting.getMaxY();
	    	double xSpace = (xMax-xMin)/numOfGrid;
	    	double ySpace = (yMax-yMin)/numOfGrid;
	    	int sum =0;
	    	List<GridSearchTask> gridSearchTaskList= new ArrayList<GridSearchTask>();
	    	for(int i=0;i<numOfGrid;i++){
	    		double yLow = yMin +i*ySpace;
	    		double yHigh = yMin + (i+1)*ySpace;
				for(int j=0;j<numOfGrid;j++){
		    		double xLow = xMin +j*xSpace;
		    		double xHigh = xMin +(j+1)*xSpace;
		    		/*squares[i][j] = qt.searchWithin(xLow, yLow, xHigh, yHigh);
		    		sum += squares[i][j].getPointIds().size();*/
		    		gridSearchTaskList.add(new GridSearchTask(i, j, xLow, yLow, xHigh, yHigh, squares, qt));
	    		}
	    	}
	    	try {
				List<Future<Integer>> results =executor.invokeAll(gridSearchTaskList);
				//System.out.println("wait for grid search result."+results.size());
				/*for(Future<Integer> f: results){
					System.out.println("isCanceled=" + f.isCancelled() + ",isDone="
							+ f.isDone());
					System.out.println("task result=" + f.get());
				}*/
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				System.exit(-1);
			}
	    	System.out.println("sum:"+sum);         
		    BufferedImage br =Array2Image(squares,"d:\\code\\original.jpg");
		    IEdgeDetector edgeDetector = new CannyEdgeDetector();
	        edgeDetector.setSourceImage(br);
	        try{
	            edgeDetector.process();
	        }catch(EdgeDetectorException e) {
	            System.out.println(e.getMessage());
	        }
	        BufferedImage edgeImage=edgeDetector.getEdgeImage();
	        int [][] edgeData =Image2Array(edgeImage,"D:/code/edgeimg.jpg",squares,k);
    	}
    }
    /**
     * assign grid according to the absolute value of x,y
     * @throws IOException 
     */
    public static void meshAndDetectEdge(Instances instances) throws IOException{
    	double[] xAxisValues = instances.attributeToDoubleArray(0);
		double[] yAxisValues = instances.attributeToDoubleArray(1);
		int len = xAxisValues.length;
		double xMin=Integer.MAX_VALUE,yMin=Integer.MAX_VALUE,xMax=0,yMax=0;
        bufferWritter = new BufferedWriter(new FileWriter("D://code//pcs.txt"));      
		for(int i=0;i<len;i++){
			if(xAxisValues[i] < xMin){
				xMin = xAxisValues[i];
			}else if(xAxisValues[i] > xMax){
				xMax = xAxisValues[i];
			}
			if(yAxisValues[i] < yMin){
				yMin = yAxisValues[i];
			}else if(yAxisValues[i] > yMax){
				yMax = yAxisValues[i];
			}
			try {
				bufferWritter.write(xAxisValues[i]+ " "+ yAxisValues[i]+"\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    bufferWritter.close();
		System.out.println("xMin:"+xMin+"xMax:"+xMax+"yMin:"+yMin+"yMax:"+yMax);
		
		long startTime = System.currentTimeMillis();
		GridSingleton.getInstance().setQuadTree(Math.floor(xMin), Math.floor(yMin), Math.ceil(xMax), Math.ceil(yMax));
		
		QuadTree qt =GridSingleton.getInstance().getQuadTree();
		for(int i=0;i<len;i++){
			qt.set(xAxisValues[i], yAxisValues[i], i);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Time to build quadTree:"+(endTime-startTime)+"ms");
		squareToSplit.offer(new Square(Math.floor(xMin), Math.floor(yMin), Math.ceil(xMax), Math.ceil(yMax),null));
		getNumberInGrids(4);
		executor.shutdown();
		/*try {
			executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(!executor.isTerminated()){
			System.out.println("Not finished");
		}*/
		System.out.println("ReadyCnt:"+readyCnt); 
        /*
		double [][] numInGrids = new double[gridSize[0]+1][gridSize[1]+1];
		
		for(int i=0;i<len;i++){
			int y = (int)((yAxisValues[i]-yMin)/(yMax-yMin)*gridSize[1]);
			int x = (int)((xAxisValues[i]-xMin)/(xMax-xMin)*gridSize[0]);
			numInGrids[x][y] += 1;
		}
		double sqrtSum =0;
		double max =0;
		int m =100;
		double ex = 0.5;
		for(int i=0;i<gridSize[0];i++){
		    for(int j=0;j<gridSize[1];j++){
		    	//System.out.print(numInGrids[i][j]+",");
		    	sqrtSum += Math.pow(numInGrids[i][j],ex);
		    }
		    //System.out.println();
		}
		System.out.println("sqrtsum ="+sqrtSum);
		for(int i =0;i<gridSize[0];i++){
			for(int j=0;j<gridSize[1];j++){
				if(numInGrids[i][j]>0){
					//qt.set(i, j, numInGrids[i][j]);
			 	    numInGrids[i][j] = numInGrids[i][j] * m/(sqrtSum * Math.pow(numInGrids[i][j],ex));
				    if(max < numInGrids[i][j]){
						max = numInGrids[i][j];
					}
				}
			}	
		}
		//System.out.println("Points number between (1,1) and (3,4) is:"+qt.searchWithin(1, 1, 3, 4));
	   
		//System.out.println("qt.getCount() is:"+qt.getCount());
		for(int i=0;i<gridSize[0];i++){
		    for(int j=0;j<gridSize[1];j++){
		    	numInGrids[i][j] = numInGrids[i][j] * 255 / max;
		    }
		   
		}
		System.out.println("max="+max);
		*/
    }
    public static int[] compressGrids(Instances numericInstances,int k){
    	int groupId =0;
    	int curId =0;
    	Instances compressedInstances = new Instances(numericInstances,0,0);
    	int []id2group =new int[numericInstances.numInstances()];
    	int []result = new int[numericInstances.numInstances()];
    	List group2compress[] = new ArrayList[squareReady.size()];
    	for(List<Integer> square: squareReady){
    		double[][] matrix = new double[square.size()][numericInstances.numAttributes()];
    		for(int i=0;i<square.size();i++){
    			matrix[i] = numericInstances.instance(square.get(i)).toDoubleArray();
    			id2group[square.get(i)] =groupId;
    		}
    		BufferedImage sourceImage=null;
    		try {
				sourceImage =Array2Image(matrix,null);
				if(sourceImage.getHeight()>10){
					ResampleOp resizeOp = new ResampleOp(10, sourceImage.getWidth());
					resizeOp.addProgressListener(new ProgressListener() {
						public void notifyProgress(float fraction) {
							//System.out.printf("Resizing %f%n",fraction);
						}
					});
					sourceImage = resizeOp.doFilter(sourceImage, sourceImage, sourceImage.getWidth(), 10);
				}
		        double [][]compressedMatrix =Image2Array(sourceImage);
		        group2compress[groupId] = new ArrayList();
		        for(int i=0;i<compressedMatrix.length;i++){
		        	DenseInstance compressedDenseInstance = new DenseInstance(1.0,compressedMatrix[i]);
		        	compressedInstances.add(compressedDenseInstance);
		        	group2compress[groupId].add(curId);
		        	curId++;
		        	/*for(int j=0;j<compressedMatrix[0].length;j++){
		        		System.out.print(compressedMatrix[i][j]+" ");
		        	}
		        	System.out.println();*/
		        }
		        
			} catch (IOException e) {
				e.printStackTrace();
			}
    		groupId++;
    	}
    	ClusterEvaluation eval = cluster(compressedInstances,k);
    	double[] cnum = eval.getClusterAssignments();//find out each data belongs to which cluster
    	/*int compressedCnt =0;
    	for(int i=0;i<group2compress.length;i++){
    		System.out.print("groupId:"+i+":");
	        for(Object o:group2compress[i]){
	        	compressedCnt ++;
	        	int id = Integer.parseInt(o.toString());
	        	double belongs = cnum[id];
	        	System.out.print(id+"belongs:"+belongs+" ");
	        }
	        System.out.println();
    	}
    	System.out.println("cnt:"+compressedCnt);
    	*/
    	try {
			bufferWritter = new BufferedWriter(new FileWriter("d:/code/cluster_result.txt"));
	    	for(int i=0;i < numericInstances.numInstances();i++){
	    		result[i] =(int)cnum[(int) group2compress[id2group[i]].get(0)];
	    		bufferWritter.write(result[i]+"\n");
	    	}
	    	bufferWritter.close();
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	return result;
    }
    public static ClusterEvaluation cluster(Instances instances,int k){
    	SimpleKMeans km =new SimpleKMeans();
    	ClusterEvaluation eval =new ClusterEvaluation();
    	try {
			km.setNumClusters(k);
			km.buildClusterer(instances);
			eval.setClusterer(km);
			eval.evaluateClusterer(instances);
			System.out.println(eval.clusterResultsToString());
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return eval;
    }
    public static JSONObject showBack(Instances data,Set<Integer>[]dataSets){
    	JSONObject jsonObject =new JSONObject();
		String []geneNames = new String[data.numInstances()];
		int attributeNum =data.instance(0).numAttributes();
		double [][]geneData =new double[data.numInstances()][attributeNum-1];
		int []dataCut =new int[dataSets.length];
		/*for(int i=0;i<data.numInstances();i++){
			geneNames[i] = "\""+data.instance(i).stringValue(0)+"\"";
			for(int j=1;j<attributeNum;j++){//real-value data is from column 1,not 0
				geneData[i][j-1] = data.instance(i).value(j);
			}
		}*/
		int cur =0;
		for(int i=0;i<dataCut.length;i++){
			if(i==0){
				dataCut[i] = dataSets[i].size();
			}else{
				dataCut[i] = dataCut[i-1] + dataSets[i].size();
			}
			for(int inx :dataSets[i]){
				geneNames[cur] = "\""+data.instance(inx).stringValue(0)+"\"";
				for(int a=1;a<attributeNum;a++){
					geneData[cur][a-1] = data.instance(inx).value(a);
				}
				cur++;
			}
		}
		//transfer the data to front view
		ClusteredData clusterData =new ClusteredData();
		clusterData.setName("cluster-name");
		clusterData.setGeneNames(geneNames);
		clusterData.setData(geneData);
		clusterData.setDataCut(dataCut);
		try {
			jsonObject.accumulate("clusterData", clusterData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
    }
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		
		double []sampleRates = {1,0.5,0.25,0.05,0.01};
		//int []sampleRates ={100,50,25,5,1};
		File upload = new File("D:\\code\\git\\IHM\\example_data.csv");
		Map<Integer,Integer> idMap = new HashMap<Integer,Integer>();//A map from from new id to original id
		long runTime[][] = new long[5][16];
		int row =0;
		File timeFile = new File("D:\\code\\git\\IHM\\timeFile.csv");
		FileOutputStream out =new FileOutputStream(timeFile,false);
		StringBuffer sb =new StringBuffer();
		//for(int s = 0;s < sampleRates.length;s++){
			//for(int k= 5; k<=80;k+=5){//each cluster number 
		    	long averageTime = 0;
		    	long squareTime = 0;
		    	long squareAverageTime =0;
		    	//logger.error("k="+k +",sampleRate:"+sampleRates[s]);
		    	//for(int t =0;t < 10;t++){//iteration for each test case
					long startMili=System.currentTimeMillis();// current time
			
					CSVLoader loader =new CSVLoader();
					Instances data =null;
					
					try {
						loader.setSource(upload);
						data =loader.getDataSet();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					//logger.error("Before resample:"+data.numInstances());
					//data = DataTransform.resampling(data, sampleRates[s]);//直接随机采样
			        //logger.error("After resample:"+data.numInstances());
					Instances numericInstances = new Instances(data);
					
					double min =Integer.MAX_VALUE,max = Integer.MIN_VALUE;
					for(int i=0;i<numericInstances.numInstances();i++){
						for(int j=1;j<numericInstances.instance(i).numAttributes();j++){
							double v = numericInstances.instance(i).value(j);
							if(v > max){
								max = v;
							}else if(v < min){
								min = v;
							}
						    if(v>4){
						    	numericInstances.instance(i).setValue(j, 4);
						    }else if(v<-4){
						    	numericInstances.instance(i).setValue(j, -4);
						    }else if(v>=-1 && v<=1){
						    	numericInstances.instance(i).setValue(j, 0);
						    }
						}
					}
					System.out.println("max="+max+",min="+min);
					
					Instances instances = new Instances(numericInstances,0,0);
					
					for(int i=0;i<numericInstances.numInstances();i++){
						if(!numericInstances.instance(i).stringValue(0).equals("no_match")){
							instances.add(numericInstances.instance(i));
						}
					}
					String geneNames[]= new String[instances.numInstances()];
					for(int i=0;i<instances.numInstances();i++){
						geneNames[i] =instances.instance(i).stringValue(0);
						//System.out.println(geneNames[i]);
					}
					CSVSaver saver = new CSVSaver();
					saver.setInstances(instances);
					saver.setDestination(new FileOutputStream(new File("D:\\code\\cleanedgenes.csv")));
					instances.deleteAttributeAt(1);
				    instances.deleteAttributeAt(0);
			
					saver.writeBatch();
					
					numericInstances.deleteAttributeAt(0);//remove gene name
					//logger.error("Before PC:"+numericInstances.numAttributes());
					//reduce the number of attribute to 2 
					System.out.println("instances size:"+instances.numInstances());
					long startTime = System.currentTimeMillis();
					ClusterEvaluation eval =cluster(numericInstances,100);
					double[] cnum = eval.getClusterAssignments();
					try {
						bufferWritter = new BufferedWriter(new FileWriter("d:/code/original_cluster_result.txt"));
				    	for(int i=0;i < numericInstances.numInstances();i++){
				    		bufferWritter.write((int)cnum[i]+"\n");
				    	}
				    	bufferWritter.close();
			    	} catch (IOException e) {
						e.printStackTrace();
					}
					Instances reducedInstances =DataTransform.pca(numericInstances);
				    //logger.error("After PC:"+numericInstances.numAttributes());
					meshAndDetectEdge(reducedInstances);
				   
					compressGrids(numericInstances,100);
					long endTime = System.currentTimeMillis();
					System.out.println("total running time:"+(endTime-startTime)+"ms"); 
					/*Instances selectedInstances = new Instances(numericInstances,0,0);
					for(int id : selectedIds){
						selectedInstances.add(numericInstances.instance(id));
					}*/
					/*int cnt=0;
					for(int i=0;i<numericInstances.numInstances();i++){
						if(!selectedIds.contains(i)){
							numericInstances.remove(i);
							continue;
						}
						idMap.put(cnt,i);
						cnt++;
						//System.out.println(numericInstances.instance(i));
					}*/
				
					//ClusterEvaluation eval =DataTransform.cluster(selectedInstances,k);
					
					//double[] cnum = eval.getClusterAssignments();//find out each data belongs to which cluster
					
					long endMili=System.currentTimeMillis();
					averageTime += (endMili-startMili);
					squareAverageTime += (endMili-startMili)*(endMili-startMili);
					
					
		    	//}
		        /*averageTime /= 10;
		    	squareTime = squareAverageTime/10 - (averageTime * averageTime);
		    	//System.out.println("Total time cost:"+averageTime+"ms"+" Square is:"+squareTime);
		    	logger.error("Total time cost:"+averageTime+"ms"+" Square is:"+squareTime);
		    	runTime[row][s] = averageTime;
		    	sb.append(String.valueOf(averageTime)+"/"+String.valueOf(squareTime)+",");*/
		    //}
		    row++;
		    sb.append("\n");
		//}
		out.write(sb.toString().getBytes("utf-8"));
	}
   
}
