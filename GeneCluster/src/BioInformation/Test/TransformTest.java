package BioInformation.Test;
import org.junit.Test;

import BioInformation.DataTransfer.GridSingleton;
import BioInformation.quadtree.QuadTree;
public class TransformTest extends junit.framework.TestCase {
    
    public QuadTree getTree(){
    	GridSingleton.getInstance().setQuadTree(-100,-100,100,100);
    	double []xAxisValues=new double[6];
		double []yAxisValues=new double[6];
		xAxisValues[0] = 1;
		xAxisValues[1] = 1;
		xAxisValues[2] = 1.1;
		xAxisValues[3] = 3;
		xAxisValues[4] = 100;
		xAxisValues[5] = 0;
		yAxisValues[0] = 1;
		yAxisValues[1] = 1;
		yAxisValues[2] = 1.1;
		yAxisValues[3] = 2;
		yAxisValues[4] = -0.01;
		yAxisValues[5] = 0;
		QuadTree qt =GridSingleton.getInstance().getQuadTree();
    	for(int i=0;i<xAxisValues.length;i++){
			qt.set(xAxisValues[i], yAxisValues[i], i);
		}
    	return qt;
    }
	@Test
	public void testGridMap() {
		/*Map<String,Grid>gridMap = GridSingleton.getInstance().getGridMap();
		Set<Integer> nodes = new HashSet<Integer>();
		int cnt =0;
		for(Entry<String,Grid> pair : gridMap.entrySet()){
			System.out.println("key:"+pair.getKey());
			Set<Integer> ids =pair.getValue().getIds();
			System.out.print("values:");
			cnt++;
			for(int id : ids){
				nodes.add(id);
				System.out.print(id+" ");
			}
			System.out.println();
		}
		System.out.println("cnt:"+cnt);*/
	}
	
    @Test
    public void testNumberOfPoints(){
    	QuadTree qt = getTree();
    	//Map<String,Grid>gridMap = GridSingleton.getInstance().getGridMap();
    	//assertEquals(5,qt.getIdsWithin(-1, -1, 4, 4).size());
		//System.out.println("Number of points within (0,0),(3,3) is:"+qt.getIdsWithin(-1, -1, 4, 4).size());
		//System.out.println("Number of points within (0,0),(3,3) is:"+qt.searchWithin(-1, -1, 4, 4).length);
    	//System.out.println("Total size:"+gridMap.get(qt.getRootNode().getGridId()).getIds().size());
    	//System.out.println("Total size:"+qt.searchWithin(-100,-100,100,100).length);
    	/*if(qt.getRootNode().getNe().getNodeType()!= NodeType.EMPTY){
    		Set<Integer> ids =gridMap.get(qt.getRootNode().getNe().getGridId()).getIds();
    		for(int id : ids){
    			System.out.print(id+" ");
    		}
    		System.out.println("Ne size:"+gridMap.get(qt.getRootNode().getNe().getGridId()).getIds().size());
    	}
    	if(qt.getRootNode().getNw().getNodeType()!= NodeType.EMPTY){
    		Set<Integer> ids =gridMap.get(qt.getRootNode().getNw().getGridId()).getIds();
    		for(int id : ids){
    			System.out.print(id+" ");
    		}
    		System.out.println("Nw size:"+gridMap.get(qt.getRootNode().getNw().getGridId()).getIds().size());
    	}
    	if(qt.getRootNode().getSe().getNodeType()!= NodeType.EMPTY){
    		Set<Integer> ids =gridMap.get(qt.getRootNode().getSe().getGridId()).getIds();
    		for(int id : ids){
    			System.out.print(id+" ");
    		}
    		System.out.println("Se size:"+gridMap.get(qt.getRootNode().getSe().getGridId()).getIds().size());
    	}
    	if(qt.getRootNode().getSw().getNodeType()!= NodeType.EMPTY){
    		Set<Integer> ids =gridMap.get(qt.getRootNode().getSw().getGridId()).getIds();
    		for(int id : ids){
    			System.out.print(id+" ");
    		}
    		System.out.println("Sw size:"+gridMap.get(qt.getRootNode().getSw().getGridId()).getIds().size());
    	}*/
    }
    
    
}
