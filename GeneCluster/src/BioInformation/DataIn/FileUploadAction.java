package BioInformation.DataIn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.opensymphony.xwork2.Action;

import BioInformation.DataTransfer.ClusteredData;
import sun.rmi.runtime.Log;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.PrincipalComponents;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.instance.Resample;

public class FileUploadAction implements Action {
	static Logger logger =Logger.getLogger("error");
    private File upload;
    private String uploadFileName;
    private String uploadContentType;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	
	@Override
	public String execute()  {
		PrincipalComponents pc =new PrincipalComponents();
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/"+this.uploadFileName);
		//logger.info(savePath);
		//logger.info(FileUploadAction.class.getName());
		int []sampleRates = {100,50,25,5,1};
		for(int k= 5; k<=80;k+=5){//each cluster number
		    for(int s = 0;s< sampleRates.length;s++){
		    	long averageTime = 0;
		    	long squareTime = 0;
		    	long squareAverageTime =0;
		    	logger.error("k="+k +",sampleRate:"+sampleRates[s]);
		    	for(int t =0;t < 10;t++){//iteration for each test case
					long startMili=System.currentTimeMillis();// current time
			
					CSVLoader loader =new CSVLoader();
					Instances data =null;
					SimpleKMeans km =new SimpleKMeans();
					try {
						km.setNumClusters(k);
						loader.setSource(upload);
						data =loader.getDataSet();
					} catch (Exception e) {
						e.printStackTrace();
					}
					String[] options ={"-S","1","-Z",String.valueOf(sampleRates[s])};//get -Z% percent of all genes.
					Resample convert =new Resample();
					try{
						
						convert.setOptions(options);
						convert.setInputFormat(data);
						//logger.error("before sampling:"+numericInstances.numInstances());
						data =Filter.useFilter(data, convert);
						//logger.error("after sampling:"+numericInstances.numInstances());
						Instances numericInstances = new Instances(data);
					    numericInstances.deleteAttributeAt(0);//remove gene name
					    
						km.buildClusterer(numericInstances);
						ClusterEvaluation eval =new ClusterEvaluation();
						eval.setClusterer(km);	
						eval.evaluateClusterer(numericInstances);
						double[] cnum = eval.getClusterAssignments();
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
						jsonObject.accumulate("clusterData", clusterData);
						ServletActionContext.getRequest().setAttribute("data", jsonObject);
						
						long endMili=System.currentTimeMillis();
						averageTime += (endMili-startMili);
						squareAverageTime += (endMili-startMili)*(endMili-startMili);
					} catch (Exception e1) {
						e1.printStackTrace();
						return ERROR;
					}
		    	}
		    	averageTime /= 10;
		    	squareTime = squareAverageTime/10 - (averageTime * averageTime);
		    	logger.error("Total time cost:"+averageTime+"ms"+" Square is:"+squareAverageTime);
		    }
		}
		//new GeneClusterReportClient().invokeService();
		return SUCCESS;
	}
    
}
