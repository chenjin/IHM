package BioInformation.DataTransfer;

import java.util.List;

public class Square {
    double minX;
    double minY;
    double maxX;
    double maxY;
    //double numInGrid;
    List<Integer> pointIds;
	public Square(double minX, double minY, double maxX, double maxY, List<Integer> pointIds) {
		super();
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		//this.numInGrid = numInGrid;
		this.pointIds = pointIds;
	}
	
	public List<Integer> getPointIds() {
		return pointIds;
	}

	public void setPointIds(List<Integer> pointIds) {
		this.pointIds = pointIds;
	}

	public double getMinX() {
		return minX;
	}
	public void setMinX(double minX) {
		this.minX = minX;
	}
	public double getMinY() {
		return minY;
	}
	public void setMinY(double minY) {
		this.minY = minY;
	}
	public double getMaxX() {
		return maxX;
	}
	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}
	public double getMaxY() {
		return maxY;
	}
	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}
	/*public double getNumInGrid() {
		return numInGrid;
	}
	public void setNumInGrid(double numInGrid) {
		this.numInGrid = numInGrid;
	}*/
	

}
