package BioInformation.DataTransfer;

import java.util.Arrays;

public class ClusteredData {
    private String name;
    private double[][] data;
    private String[] geneNames;
    private int[] dataCut;
	public String getName() {
		return name;
	}
	public double[][] getData() {
		return data;
	}
	public void setData(double[][] data) {
		this.data = data;
	}
	public String[] getGeneNames() {
		return geneNames;
	}
	public void setGeneNames(String[] geneNames) {
		this.geneNames = geneNames;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int[] getDataCut() {
		return dataCut;
	}
	public void setDataCut(int[] dataCut) {
		this.dataCut = dataCut;
	}
	public static void main(String[] args) {
		
	}
	@Override
	public String toString() {
		StringBuilder geneData = new StringBuilder();
		for(int i=0;i<data.length;i++){
			geneData.append(Arrays.toString(data[i]));
			geneData.append(",");
		}
		geneData.deleteCharAt(geneData.length()-1);
		return "{\"data\":[" + geneData.toString() + "],\"geneNames\":"
				+ Arrays.toString(geneNames) +",\"dataCut\":"+Arrays.toString(dataCut)+ "}";
	}
	
}
