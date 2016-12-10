package BioInformation.DataIn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.apache.axis2.AxisFault;
import org.apache.struts2.ServletActionContext;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.opensymphony.xwork2.Action;

import david.xsd.ListRecord;
import sample.session.client.GeneClusterReportClient;
import sample.session.client.stub.DAVIDWebServiceStub;
import sample.session.service.xsd.SimpleGeneClusterRecord;

public class FileUploadAction implements Action {
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
		System.out.println(type);
		//new GeneClusterReportClient().invokeService();
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/"+this.uploadFileName);
		System.out.println(savePath);
		try {
			FileInputStream fis = new FileInputStream(upload);
			FileOutputStream fos = new FileOutputStream(savePath);
			IOUtils.copy(fis, fos);
			fos.flush();
			fos.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
    
}
