package NetworkInformation.DataIn;

import com.opensymphony.xwork2.Action;

public class testAction implements Action {

	public String userName;
	public String PassWord;
	
	public String filename;
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return PassWord;
	}

	public void setPassWord(String passWord) {
		PassWord = passWord;
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String test(){
//		System.out.println(userName + PassWord);
		System.out.println(filename);
		if (1 > 0)
			return SUCCESS;
		else
			return ERROR;
		
	}

}
