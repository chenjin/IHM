package BioInformation.Task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import BioInformation.DataTransfer.Square;
import BioInformation.quadtree.QuadTree;

public final class GridSearchTask implements Callable<Integer>{
    private int y;
    private int x;
    private double xLow;
    private double yLow;
    private double xHigh;
    private double yHigh;
    private Square[][] squares;
    private QuadTree qt;
	public GridSearchTask(int y, int x, double xLow, double yLow, double xHigh, double yHigh,Square[][] squares,QuadTree qt) {
		super();
		this.y = y;
		this.x = x;
		this.xLow = xLow;
		this.yLow = yLow;
		this.xHigh = xHigh;
		this.yHigh = yHigh;
		this.squares = squares;
		this.qt = qt;
	}

	@Override
	public Integer call() {
		//synchronized(squares){
		    squares[y][x] = qt.searchWithin(xLow, yLow, xHigh, yHigh);
		//}
		return squares[y][x].getPointIds().size();
	}
	
}
/*
public class GridSearchTaskManager extends TaskManager {
	private static Integer numInGrid;
	public GridSearchTaskManager(int poolSize) {
		super(poolSize);
	}
    public void submit(int y, int x, double xLow, double yLow, double xHigh, double yHigh,Square[][] squares,QuadTree qt){
    	try {
			numInGrid =(Integer)executor.submit(new GridSearchTask(y,x,xLow,yLow,xHigh,yHigh,squares,qt)).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
    }
	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return numInGrid;
	}

	@Override
	public void shutdown() {
	    executor.shutdown();
	}

}*/
