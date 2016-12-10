package NetworkInformation.DataIn;



import NetworkInformation.DataTransformation.GenerateData;
import NetworkInformation.GraphInformation.GraphforAllData;

import com.opensymphony.xwork2.Action;


public class readData implements Action {

	public String filename;
	
	public String jsondata;
	
	public String test;
	
	public GraphforAllData all = new GraphforAllData();
	
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getJsondata() {
		return jsondata;
	}

	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}

	@Override
	public String execute() throws Exception {
		return null;
	}
	
	public String loadFile() throws Exception{
		
		GenerateData gd = new GenerateData();
		all = gd.generateUIData();
		test = "testing string";
		return SUCCESS;
		
		
	}

	public String loadDialect() throws Exception{
		GenerateData gd = new GenerateData();
		all = gd.generateUIData();
		return SUCCESS;
	}
	public String loadHome() throws Exception{
		GenerateData gd = new GenerateData();
		all = gd.generateUIData();
		return SUCCESS;
	}
}

