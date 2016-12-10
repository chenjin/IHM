package NetworkInformation.DataTransformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class XlsUtil {

	private String filetype = null;
	private Workbook wb = null;

//	public XlsUtil() throws Exception {
//		wb = Workbook.getWorkbook(new File("D://NLPIR//felling.xls"));
//	}

	public XlsUtil(String filename) throws Exception {
		wb = Workbook.getWorkbook(new File(filename));
	}

	public Sheet ReadXls() throws Exception {
//		Sheet[] s = wb.getSheets();
		Sheet sheet = wb.getSheet(2);// 鑾峰彇鎵�湁鐨剆heet
		return sheet;
	}

	public Sheet[] ReadXlss() throws Exception{
		Sheet[] s = wb.getSheets();
//		String title = "";
//		title = s[0].getName();
		return s;
	}
	public List<String> getCol(int i) throws Exception {
		Sheet s = ReadXls();
		List<String> cols = new ArrayList<String>();
		for (int j = 1; j < s.getRows(); j++) {
			cols.add(s.getCell(i, j).getContents());
		}
		return cols;
	}

	public static void main(String[] args) throws Exception {
		 XlsUtil xu = new XlsUtil("F://data.xls");
//		 XlsUtil xu = new XlsUtil();
		 Sheet s = xu.ReadXls();
		 int size = s.getRows();
		 for (int j = 1; j < size; j++) {
				for (int k = 1; k < size; k++) {
					System.out.print(s.getCell(k, j).getContents());
				}
				System.out.println();
			}
		
//		System.out.println(1011 / 10);
		
		
	}
}
