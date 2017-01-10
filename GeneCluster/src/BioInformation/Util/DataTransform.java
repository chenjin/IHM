package BioInformation.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import BioInformation.DataTransfer.ClusteredData;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;
import weka.filters.unsupervised.instance.Resample;

public class DataTransform {
	static Logger logger =Logger.getLogger("error");
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
		//reduce the number of attribute to 2 
		try {
			principalComponents.setOptions(pc_options);
			principalComponents.setInputFormat(instances);
			instances = Filter.useFilter(instances,principalComponents);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return instances;
    }
    public static List<Integer>[] assignGrid(Instances instances){
    	int gridId[] =new int[instances.numInstances()];
    	int grid[] ={5,5};
    	List<Integer> []gridArray = new ArrayList[grid[0]*grid[1]+grid[0]];
    	for(int axis =0;axis <=1; axis++){
			double[] axisValues = instances.attributeToDoubleArray(axis);
			double min=Double.MAX_VALUE,max = Double.MIN_VALUE;
			for(int i=0;i<axisValues.length;i++){
				if(axisValues[i] < min){
					min = axisValues[i];
				}else if(axisValues[i] > max){
					max = axisValues[i];
				}
			}
			//System.out.println("max="+max+",min="+min);
			for(int i=0;i<axisValues.length;i++){
				//System.out.println("i="+i+"axisValues="+axisValues[i]+"axis="+axis);
				if(axis == 0){
					gridId[i] += (int)((axisValues[i]-min)*grid[axis]/(max-min));
				}else{
					gridId[i] += (int)((axisValues[i]-min)*grid[axis]*grid[0]/(max-min));
				}
				//System.out.println("gridId[i]="+gridId[i]);
			}
		}
		for(int i=0;i<instances.numInstances();i++){
			//System.out.println(gridId[i]);
			if(gridArray[gridId[i]] != null){
				gridArray[gridId[i]].add(i);
			}else{
				List<Integer> gridList =new ArrayList<Integer>();
				gridList.add(i);
				gridArray[gridId[i]]=gridList;
			}
		}
		for(int i=0;i<grid[0]*grid[1]+grid[0];i++){
			if(gridArray[i]!=null){
				System.out.print("i="+i);
				for(int g : gridArray[i]){
					System.out.print(","+g);
				}
				System.out.println();
			}
		}
    	return gridArray;
    }
    public static ClusterEvaluation cluster(Instances instances,int k){
    	SimpleKMeans km =new SimpleKMeans();
    	ClusterEvaluation eval =new ClusterEvaluation();
    	try {
			km.setNumClusters(k);
			km.buildClusterer(instances);
			eval.setClusterer(km);
			eval.evaluateClusterer(instances);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return eval;
    }
    public static JSONObject showBack(Instances data){
    	JSONObject jsonObject =new JSONObject();
		String []geneNames = new String[data.numInstances()];
		int attributeNum =data.instance(0).numAttributes();
		double [][]geneData =new double[data.numInstances()][attributeNum];
		for(int i=0;i<data.numInstances();i++){
			geneNames[i] = "\""+data.instance(i).stringValue(0)+"\"";
			for(int j=1;j<attributeNum;j++){
				geneData[i][j] = data.instance(i).value(j);
			}
		}
		//transfer the data to front view
		ClusteredData clusterData =new ClusteredData();
		clusterData.setName("cluster-name");
		clusterData.setGeneNames(geneNames);
		clusterData.setData(geneData);
		try {
			jsonObject.accumulate("clusterData", clusterData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
    }
	public static void main(String[] args) {
		int []sampleRates = {50,25,5,1};
		File upload = new File("D:\\code\\git\\IHM\\example_data.csv");
		//for(int k= 5; k<=80;k+=5){//each cluster number
		    //for(int s = 0;s< sampleRates.length;s++){
		    	//long averageTime = 0;
		    	//long squareTime = 0;
		    	//long squareAverageTime =0;
		    	//logger.error("k="+k +",sampleRate:"+sampleRates[s]);
		    	//for(int t =0;t < 10;t++){//iteration for each test case
					//long startMili=System.currentTimeMillis();// current time
			
					CSVLoader loader =new CSVLoader();
					Instances data =null;
					
					try {
						loader.setSource(upload);
						data =loader.getDataSet();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					//logger.error("Before resample:"+data.numInstances());
					//data = DataTransform.resampling(data, sampleRates[s]);
			        //logger.error("After resample:"+data.numInstances());
					Instances numericInstances = new Instances(data);
					numericInstances.deleteAttributeAt(0);//remove gene name
					//logger.error("Before PC:"+numericInstances.numAttributes());
					//reduce the number of attribute to 2 
					numericInstances =DataTransform.pca(numericInstances);
					
				    //logger.error("After PC:"+numericInstances.numAttributes());
					
					List<Integer>[] belongings =assignGrid(numericInstances);
					
					
					//for(int i=0;i<numericInstances.numInstances();i++){
					    //System.out.println(numericInstances.instance(i));
					//}
					//ClusterEvaluation eval =DataTransform.cluster(numericInstances,k);
					
					//double[] cnum = eval.getClusterAssignments();//find out each data belongs to which cluster
					
					//long endMili=System.currentTimeMillis();
					//averageTime += (endMili-startMili);
					//squareAverageTime += (endMili-startMili)*(endMili-startMili);
		    	//}
		    	//averageTime /= 10;
		    	//squareTime = squareAverageTime/10 - (averageTime * averageTime);
		    	//logger.error("Total time cost:"+averageTime+"ms"+" Square is:"+squareTime);
		    //}
		//}

	}

}
