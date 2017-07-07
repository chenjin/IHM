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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mortennobel.imagescaling.ResampleOp;

import BioInformation.DataTransfer.ClusteredData;
import BioInformation.DataTransfer.Square;
import BioInformation.EdgeDetection.CannyEdgeDetector;
import BioInformation.EdgeDetection.EdgeDetectorException;
import BioInformation.EdgeDetection.IEdgeDetector;
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
    public static Queue<Square> squareReady = new ConcurrentLinkedQueue<Square>();//squares don't need further splitting.
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
    public static List<Integer> Image2Array(BufferedImage bufferedEdgeImage,String filedir,Square[][] squares) throws IOException{
         int [][]edgeData = new int[bufferedEdgeImage.getHeight()][bufferedEdgeImage.getWidth()];
         bufferWritter = new BufferedWriter(new FileWriter("D:\\code\\edge_data.txt"));
         //List<Image2ArrayTask> image2ArrayTask = new ArrayList<Image2ArrayTask>();
         List<Integer> pointIdsNextRound = new ArrayList<Integer>();
         for(int y=0;y<bufferedEdgeImage.getHeight();y++){
         	for(int x=0;x<bufferedEdgeImage.getWidth();x++){
         		edgeData[y][x] = bufferedEdgeImage.getRGB(x, y)&0xFF;
         		//System.out.print(edgeData[y][x]+" ");
         		int numOfPointInSquare = squares[y][x].getPointIds().size();
         		if(numOfPointInSquare>0){
	         		if(edgeData[y][x] < 255 ){//边缘网格，针对Canny来说
	         			//squareToSplit.offer(squares[y][x]);
	         			pointIdsNextRound.addAll(squares[y][x].getPointIds());
	         		}else{//非边缘网格
	         			//double w = squares[y][x].getMaxY()-squares[y][x].getMinY();
	            		//double h = squares[y][x].getMaxX()-squares[y][x].getMinX();
	            		//double density = numOfPointInSquare/(w*h);
	            		//squares[y][x].setDensity(density);
	         			squareReady.add(squares[y][x]);
	         			readyCnt += numOfPointInSquare;
	         		}
         		}
         		bufferWritter.write(edgeData[y][x]+" ");
         		//executor.submit(new Image2ArrayTask(bufferedEdgeImage,edgeData,squares,y,x));
         	    //image2ArrayTask.add(new Image2ArrayTask(bufferedEdgeImage,edgeData,squares,y,x));
                //Integer val =(Integer)tm.getResult();
         	}
         	//bufferWritter.newLine();
         	//System.out.println();
         }
        /*try {
			List<Future<int[][]>> results =executor.invokeAll(image2ArrayTask);
			//System.out.println("Wait for image2Array result."+results.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
        bufferWritter.close();
		ImageIO.write(bufferedEdgeImage,"jpg",new File(filedir));
        return pointIdsNextRound;
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
     * 从quadtree中生成格子个数的矩阵，格子的个数为4^k
     * @param xMin
     * @param yMin
     * @param xMax
     * @param yMax
     * @param k
     * @return
     * @throws IOException 
     */
    public static void getNumberInGrids(double xAxisValues[],double yAxisValues[],double xMin,double yMin,double xMax,double yMax) throws IOException{
    	int numOfGrid = 16;
    	QuadTree qt = new QuadTree(xMin, yMin, xMax, yMax);
    	for(int i=0;i<xAxisValues.length;i++){
			qt.set(xAxisValues[i], yAxisValues[i], i);
    	}
    	System.out.println("xAxisValues。length:"+xAxisValues.length);
    	int detectTime =0;
    	while(true){
    		long detectStartTime = System.currentTimeMillis();
    		Square[][] squares = new Square[numOfGrid][numOfGrid];
	    	double xSpace = (xMax-xMin)/numOfGrid;
	    	double ySpace = (yMax-yMin)/numOfGrid;
	    	//List<GridSearchTask> gridSearchTaskList= new ArrayList<GridSearchTask>();
	    	int sum =0;
	    	for(int i=0;i<numOfGrid;i++){
	    		double yLow = yMin +i*ySpace;
	    		double yHigh = yMin + (i+1)*ySpace;
				for(int j=0;j<numOfGrid;j++){
		    		double xLow = xMin +j*xSpace;
		    		double xHigh = xMin +(j+1)*xSpace;
		    		squares[i][j] = qt.searchWithin(xLow, yLow, xHigh, yHigh);
		    		sum += squares[i][j].getPointIds().size();
		    		//gridSearchTaskList.add(new GridSearchTask(i, j, xLow, yLow, xHigh, yHigh, squares, qt));
	    		}
	    	}
	    	/*try {
				List<Future<Integer>> results =executor.invokeAll(gridSearchTaskList);
				//System.out.println("wait for grid search result."+results.size());
				for(Future<Integer> f: results){
					System.out.println("isCanceled=" + f.isCancelled() + ",isDone="
							+ f.isDone());
					System.out.println("task result=" + f.get());
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				System.exit(-1);
			}*/
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
	        List<Integer>pointIdsNextRound =Image2Array(edgeImage,"D:/code/edgeimg.jpg",squares);
	        if(pointIdsNextRound.size()==0){
	        	break;
	        }
	        //System.out.println("continue:");
	        qt = new QuadTree(xMin,yMin,xMax,yMax);
	        for(int pointId : pointIdsNextRound){
	        	qt.set(xAxisValues[pointId], yAxisValues[pointId], pointId);
	        }
	        numOfGrid *= 2;
	        long detectEndTime = System.currentTimeMillis();
	        System.out.println("Epoch "+(detectTime++)+":"+(detectEndTime-detectStartTime)+"ms");
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
        for(int i=0;i<len;i++){
        	xAxisValues[i] =(xAxisValues[i]-xMin)/(xMax-xMin);
        	yAxisValues[i] =(yAxisValues[i]-yMin)/(yMax-yMin);
        }
		//squareToSplit.offer(new Square(Math.floor(xMin), Math.floor(yMin), Math.ceil(xMax), Math.ceil(yMax),null));
		//getNumberInGrids(xAxisValues,yAxisValues,Math.floor(xMin), Math.floor(yMin), Math.ceil(xMax), Math.ceil(yMax));
        squareToSplit.offer(new Square(0,0,1,1,null));
        getNumberInGrids(xAxisValues,yAxisValues,-(1e-3),-(1e-3),1+1e-3,1+1e-3);
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
    @SuppressWarnings("unchecked")
	public static int[] compressGrids(Instances numericInstances,int k, String[] geneNames){
    	long compressStartTime = System.currentTimeMillis();
    	int groupId =0;
    	int curId =0;
    	Instances compressedInstances = new Instances(numericInstances,0,0);
    	int []id2group =new int[numericInstances.numInstances()];
    	int []result = new int[numericInstances.numInstances()];
    	List group2compress[] = new ArrayList[squareReady.size()];
    	double e =0.35,ratio=0.1;
    	int m =(int) (numericInstances.numInstances()*ratio);
    	//squareReady.forEach(square->System.out.println(square.getPointIds().size()));
    	
    	double nSum =squareReady.parallelStream().mapToDouble(square->
            1/(Math.pow(square.getPointIds().size(),e-1)*Math.pow(square.getArea(), -e))
    	).sum();
        double alpha = m/nSum;
        //System.out.println("alpha:"+alpha);
        
        System.out.println("squareReady.size()"+squareReady.size());
        int addupSum =0;
    	for(Square square: squareReady){
    		int numberOfPoints = square.getPointIds().size();
    		double target = m * Math.pow(numberOfPoints,1-e)*Math.pow(square.getArea(), e)/nSum;
    		int targetSize = (int)Math.round(target);
    		//System.out.println("minX:"+square.getMinX()+"maxX:"+square.getMaxX()+"minY:"+square.getMinY()+"maxY:"+square.getMaxY()+"Area:"+square.getArea()+" OrigineSize:"+numberOfPoints+" targetSize:"+target);
    		addupSum += numberOfPoints;
    		double[][] matrix = new double[numberOfPoints][numericInstances.numAttributes()];
    		if(targetSize > 0){
	    		for(int i=0;i<numberOfPoints;i++){
	    			matrix[i] = numericInstances.instance(square.getPointIds().get(i)).toDoubleArray();
	    			id2group[square.getPointIds().get(i)] =groupId;//assign origin points to certain group
	    		}
    		}else{
    			for(int i=0;i<numberOfPoints;i++){
	    			id2group[square.getPointIds().get(i)] =-1;//outliers,belongs no group
	    		}
    			/*System.out.print("group"+groupId+"out:");
    			for(int id:square.getPointIds()){
    				System.out.print(id+ " ");
    			}
    			System.out.println();*/
    			groupId++;
    			continue;
    		}
    		BufferedImage sourceImage=null;
    		try {
    			sourceImage =Array2Image(matrix,null);
				if(targetSize >=3 && targetSize <= numberOfPoints){
					ResampleOp resizeOp = new ResampleOp(targetSize,sourceImage.getWidth());
//					resizeOp.addProgressListener(new ProgressListener() {
//						public void notifyProgress(float fraction) {
//							System.out.printf("Resizing %f%n",fraction);
//						}
//					});
					sourceImage = resizeOp.doFilter(sourceImage, sourceImage, sourceImage.getWidth(), targetSize);
				}
		        double [][]compressedMatrix =Image2Array(sourceImage);
		        group2compress[groupId] = new ArrayList();
		        for(int i=0;i<compressedMatrix.length;i++){
		        	DenseInstance compressedDenseInstance = new DenseInstance(1.0,compressedMatrix[i]);
		        	compressedInstances.add(compressedDenseInstance);
		        	group2compress[groupId].add(curId);//curId is compressed instance's index
		        	curId++;
//		        	for(int j=0;j<compressedMatrix[0].length;j++){
//		        		System.out.print(compressedMatrix[i][j]+" ");
//		        	}
//		        	System.out.println();
		        }
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    		groupId++;
    	}
    	System.out.println("addupSum:"+addupSum);
    	long compressEndTime = System.currentTimeMillis();
    	System.out.println("compress time:"+(compressEndTime-compressStartTime)+"ms");
    	ClusterEvaluation eval = cluster(compressedInstances,k);
    	double[] cnum = eval.getClusterAssignments();//find out each data belongs to which cluster
    	double apartCnt =0;
    	for(int i=0;i<group2compress.length;i++){
    		if(group2compress[i] == null){//some points in some groups are treated as outlier
    			continue;
    		}
    		//System.out.print("groupId:"+i+":");
            Set<Double> belongings = new HashSet<Double>();
            Map<Object,Integer> map = new HashMap<>();
            boolean counted=false;
	        for(Object o:group2compress[i]){
	        	int id = (int)o;
	        	double belongs = cnum[id];
	        	if(belongings.size() > 0 && !belongings.contains(belongs) && !counted){
	        		apartCnt++;
	        		counted = true;
	        	}
	        	belongings.add(belongs);
	        	//System.out.print(id+"belongs:"+belongs+" ");
	        	map.put(belongs, map.getOrDefault(belongs, 0)+1);
	        }
	        int max =0;
	        for(Map.Entry<Object, Integer>entry : map.entrySet()){
	        	if(max < entry.getValue()){
	        		max =entry.getValue();
	        		double belong =(double) entry.getKey();
	        		group2compress[i].set(0, Integer.valueOf((int)belong));
	        	}
	        }
	        //System.out.println();
    	}
    	long clusterEndTime =System.currentTimeMillis();
    	System.out.println("cluster time:"+(clusterEndTime-compressEndTime)+"ms");
    	System.out.println("Apart:"+apartCnt);
    	System.out.println("group2compress.length:"+group2compress.length);
    	System.out.println("Ratio of apart:"+apartCnt/group2compress.length);
    	System.out.println("numericInstances:"+numericInstances.numInstances());
    	//System.out.println("geneNames:"+geneNames.length);
    	try {
			bufferWritter = new BufferedWriter(new FileWriter("d:/code/cluster_result.txt"));
	    	for(int i=0;i < numericInstances.numInstances();i++){
	    		int gId = id2group[i];
	    		if(gId != -1){
	    			//System.out.println("gID:"+gId+" i:"+i);
	    			result[i] =(int)cnum[(int) (group2compress[gId].get(0))];
	    		}else{
	    			result[i] = -1;
	    		}
	    		bufferWritter.write(geneNames[i]+" "+result[i]+"\n");
	    	}
	    	bufferWritter.close();
    	} catch (IOException e1) {
			e1.printStackTrace();
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
			//System.out.println(eval.clusterResultsToString());
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
		int k = 10;
		File upload = new File("D:\\code\\git\\IHM\\attributes.txt");
		 upload = new File("D:\\code\\git\\IHM\\example_data.csv");
		// upload = new File("D:\\code\\git\\IHM\\blob_data.txt");
		CSVLoader loader =new CSVLoader();
		Instances data =null;
		try {
			loader.setSource(upload);
			data =loader.getDataSet();
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
		Instances numericInstances = new Instances(data);
		System.out.println("numInstances:"+numericInstances.numInstances());
		//Instances cleanedInstances = numericInstances;
		
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
	
		Instances cleanedInstances = new Instances(numericInstances,0,0);
		
		for(int i=0;i<numericInstances.numInstances();i++){
			if(!numericInstances.instance(i).stringValue(0).equals("no_match")){
				cleanedInstances.add(numericInstances.instance(i));
			}
		}
		String geneNames[]= new String[cleanedInstances.numInstances()];
		for(int i=0;i<cleanedInstances.numInstances();i++){
			geneNames[i] =cleanedInstances.instance(i).stringValue(0);
			//System.out.println(geneNames[i]);
		}
		CSVSaver saver = new CSVSaver();
		saver.setInstances(cleanedInstances);
		saver.setDestination(new FileOutputStream(new File("D:\\code\\cleanedgenes.csv")));
		cleanedInstances.deleteAttributeAt(1);
	    cleanedInstances.deleteAttributeAt(0);

		saver.writeBatch();
		
		numericInstances.deleteAttributeAt(0);//remove gene name
	
		System.out.println("instances size:"+cleanedInstances.numInstances());
		long originStartTime = System.currentTimeMillis();
		ClusterEvaluation eval =cluster(cleanedInstances,k);
		long originEndTime =System.currentTimeMillis();
		System.out.println("Origin running time:"+(originEndTime-originStartTime));
		double[] cnum = eval.getClusterAssignments();
		try {
			bufferWritter = new BufferedWriter(new FileWriter("d:/code/original_cluster_result.txt"));
	    	for(int i=0;i < cleanedInstances.numInstances();i++){
	    		bufferWritter.write((int)cnum[i]+"\n");
	    	}
	    	bufferWritter.close();
    	} catch (IOException e) {
			e.printStackTrace();
		}
		long gridStartTime = System.currentTimeMillis();
		Instances reducedInstances =DataTransform.pca(cleanedInstances);
		long detectStartTime = System.currentTimeMillis();
		meshAndDetectEdge(reducedInstances);
		long detectEndTime = System.currentTimeMillis();
		System.out.println("Detect time:"+(detectEndTime-detectStartTime)+"ms");
		compressGrids(cleanedInstances,k,geneNames);
		long gridEndTime = System.currentTimeMillis();
		System.out.println("Grid running time:"+(gridEndTime-gridStartTime)+"ms");
		/*bufferWritter = new BufferedWriter(new FileWriter("d:/code/random_result.txt"));
		for(int i=0;i<cleanedInstances.numInstances();i++){
			bufferWritter.write(RandomNumberUtil.rd.nextInt(k)+"\n");
		}
		bufferWritter.close();
		long pcaKmeansStartTime = System.currentTimeMillis();
		reducedInstances =DataTransform.pca(cleanedInstances);
		eval = cluster(reducedInstances,k);
		cnum = eval.getClusterAssignments();
		long pcaKmeansEndTime = System.currentTimeMillis();
		System.out.println("pca_kmeans run time"+(pcaKmeansEndTime-pcaKmeansStartTime));
		try {
			bufferWritter = new BufferedWriter(new FileWriter("d:/code/pca_cluster_result.txt"));
	    	for(int i=0;i < cleanedInstances.numInstances();i++){
	    		bufferWritter.write((int)cnum[i]+"\n");
	    	}
	    	bufferWritter.close();
    	} catch (IOException e) {
			e.printStackTrace();
		}*/      
	}
   
}
