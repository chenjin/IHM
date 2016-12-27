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
import BioInformation.Util.DataTransform;
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
	public Instances gridSampling(Instances instances){
		return instances;
	}
	@Override
	public String execute()  {
		
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/"+this.uploadFileName);
		//logger.info(savePath);
		//logger.info(FileUploadAction.class.getName());
		int []sampleRates = {100,50,25,5,1};
		for(int k= 5; k<=80;k+=5){//each cluster number
		    for(int s = 0;s< sampleRates.length;s++){
		    	long averageTime = 0;
		    	long squareTime = 0;
		    	long squareAverageTime =0;
		    	//logger.error("k="+k +",sampleRate:"+sampleRates[s]);
		    	for(int t =0;t < 10;t++){//iteration for each test case
					long startMili=System.currentTimeMillis();// current time
			
					CSVLoader loader =new CSVLoader();
					Instances data =null;
					SimpleKMeans km =new SimpleKMeans();
					try {
						loader.setSource(upload);
						data =loader.getDataSet();
					}catch(Exception e1) {
						e1.printStackTrace();
						return ERROR;
					}
					logger.error("Before resample:"+data.numInstances());
					data = DataTransform.resampling(data, sampleRates[s]);
			        logger.error("After resample:"+data.numInstances());
					Instances numericInstances = new Instances(data);
					numericInstances.deleteAttributeAt(0);//remove gene name
					logger.error("Before PC:"+numericInstances.numAttributes());
					//reduce the number of attribute to 2 
					numericInstances =DataTransform.pca(numericInstances);
				    logger.error("After PC:"+numericInstances.numAttributes());
					
					ClusterEvaluation eval =DataTransform.cluster(numericInstances,k);
					double[] cnum = eval.getClusterAssignments();//find out each data belongs to which cluster
					
					JSONObject jsonObject =DataTransform.showBack(data);
					ServletActionContext.getRequest().setAttribute("data", jsonObject);
					
					long endMili=System.currentTimeMillis();
					averageTime += (endMili-startMili);
					squareAverageTime += (endMili-startMili)*(endMili-startMili);
					
		    	}
		    	averageTime /= 10;
		    	squareTime = squareAverageTime/10 - (averageTime * averageTime);
		    	//logger.error("Total time cost:"+averageTime+"ms"+" Square is:"+squareTime);
		    }
		}
		//new GeneClusterReportClient().invokeService();
		return SUCCESS;
	}
    
}
