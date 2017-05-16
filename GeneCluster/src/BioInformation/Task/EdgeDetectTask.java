package BioInformation.Task;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import BioInformation.EdgeDetection.CannyEdgeDetector;
import BioInformation.EdgeDetection.EdgeDetectorException;
import BioInformation.EdgeDetection.IEdgeDetector;

public final class EdgeDetectTask implements Callable<BufferedImage>{
	private BufferedImage br;
	public EdgeDetectTask( BufferedImage br) {
		super();
		this.br = br;
	}
	@Override
	public BufferedImage call() throws Exception {
	    IEdgeDetector edgeDetector = new CannyEdgeDetector();
        edgeDetector.setSourceImage(br);
        try{
            edgeDetector.process();
        }catch(EdgeDetectorException e) {
            System.out.println(e.getMessage());
        }
		return edgeDetector.getEdgeImage();
	}
	
}
/*
public class EdgeDetectTaskManager extends TaskManager {
    private EdgeDetectTask edgeDetectTask;
    private static BufferedImage result;
	public EdgeDetectTaskManager(int poolSize) {
		super(poolSize);
	}
    public void submit( BufferedImage br){
    	edgeDetectTask = new EdgeDetectTask(br);
    	try {
			result = executor.submit(edgeDetectTask).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.exit(-1);
		}
    }
	@Override
	public Object getResult() {
		return result;
	}

	@Override
	public void shutdown() {
		executor.shutdown();
	}

}
*/