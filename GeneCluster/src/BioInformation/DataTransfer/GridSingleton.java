package BioInformation.DataTransfer;

import java.util.HashMap;
import java.util.Map;

import BioInformation.quadtree.QuadTree;

public class GridSingleton {
    //private Map<String,Grid> gridMap;
    private QuadTree qt;
    private static GridSingleton single= null;
	private GridSingleton(){
		//gridMap = new HashMap<String,Grid>();
	}
    public static GridSingleton getInstance(){
    	if(single == null){
    		synchronized(GridSingleton.class){
    			if(single == null){
    				single = new GridSingleton();
    			}
    		}
    	}
    	return single;
    }
    public void setQuadTree(double xMin,double yMin,double xMax,double yMax){
    	qt =new QuadTree(xMin, yMin, xMax, yMax);
    }
    public QuadTree getQuadTree(){
    	return qt;
    }
    /*public Map<String,Grid> getGridMap(){
    	return gridMap;
    }*/
}
