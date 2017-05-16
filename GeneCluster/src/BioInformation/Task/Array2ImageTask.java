package BioInformation.Task;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import BioInformation.DataTransfer.Square;

public final class Array2ImageTask implements Callable<BufferedImage>{
    private Square data[][];
    private int y;
    private int x;
    private BufferedImage br;
	public Array2ImageTask(Square[][] data, BufferedImage br,int y,int x) {
		super();
		this.data = data;
		this.br = br;
		this.y = y;
		this.x = x;
	}
	@Override
	public BufferedImage call() throws Exception {	
        int val =(int)data[y][x].getPointIds().size();
            	//System.out.println(val);
        int rgb = ((val*256+val)*256+val);
        
        //需要同步
        //synchronized(br){
            br.setRGB(x, y,rgb );//设置像素       
        //}
        return br;
	}
	
}
/*
public final class Array2ImageTaskManager extends TaskManager{
	private Array2ImageTask array2Imagetask;
	private static BufferedImage br;
	public Array2ImageTaskManager(int poolSize) {
		super(poolSize);
	}
	public void submit(Square data[][],BufferedImage br,int y,int x){
		array2Imagetask = new Array2ImageTask(data,br,y,x);
		try {
			br =executor.submit(array2Imagetask).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	@Override
	public Object getResult() {
		return br;
	}
	@Override
	public void shutdown() {
        executor.shutdown();	
	}
}
*/