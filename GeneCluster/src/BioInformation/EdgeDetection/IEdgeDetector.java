package BioInformation.EdgeDetection;

import java.awt.Image;
import java.awt.image.BufferedImage;

public interface IEdgeDetector {

	void setSourceImage(Image br);

	void process() throws EdgeDetectorException;

	BufferedImage getEdgeImage();

	BufferedImage toBufferedImage(Image edgeImage);
   
}
