package BioInformation.Task;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import BioInformation.DataTransfer.Square;
import BioInformation.Util.DataTransform;

public final class Image2ArrayTask implements Callable<int[][]>{
	private BufferedImage bufferedEdgeImage;
	private int[][] edgeData;
	private Square[][] squares;
	private int y;
	private int x;
	public Image2ArrayTask(BufferedImage bufferedEdgeImage,int[][]edgeData, Square[][] squares, int y,int x) {
		super();
		this.bufferedEdgeImage = bufferedEdgeImage;
		this.edgeData =edgeData;
		this.squares = squares;
		this.y = y;
		this.x = x;
	}


	@Override
	public int[][] call() throws Exception {
		int val = bufferedEdgeImage.getRGB(x, y)&0xFF;
		//System.out.print(val+" ");
		int numOfPointInSquare = squares[y][x].getPointIds().size();
		if(numOfPointInSquare>0){
     		if(val < 255 ){
     			DataTransform.squareToSplit.offer(squares[y][x]);
     		}else{
     			DataTransform.squareReady.add(squares[y][x].getPointIds());
     			//System.out.println("Ready grid:"+squares[y][x].getNumInGrid());
     			DataTransform.readyCnt += numOfPointInSquare;
     		}
		}
		//需要同步
		//synchronized(edgeData){
		    edgeData[y][x] =val;
		//}
        return edgeData;
	}
	
}
/*
public final class Image2ArrayTaskManager extends TaskManager{
    private Image2ArrayTask image2ArrayTask;
    private static int[][] edgeData;
	public Image2ArrayTaskManager(int poolSize) {
		super(poolSize);
	}
	public void submit(BufferedImage bufferedEdgeImage,int[][] edgeData,Square[][] squares,int y,int x){
		image2ArrayTask  =new Image2ArrayTask(bufferedEdgeImage,edgeData,squares,y,x);
	    try {
			edgeData =executor.submit(image2ArrayTask).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		//executor.shutdown();
	}
	@Override
	public Object getResult() {
		return edgeData;
	}
	@Override
	public void shutdown() {
	    executor.shutdown();
	}

}*/
