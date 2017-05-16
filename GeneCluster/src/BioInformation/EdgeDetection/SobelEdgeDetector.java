package BioInformation.EdgeDetection;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class SobelEdgeDetector extends Component implements IEdgeDetector{
	public SobelEdgeDetector(){
		
	}
	/*private static SobelEdgeDetector edgeDetector= null;
	public static SobelEdgeDetector getInstance(){
		synchronized(CannyEdgeDetector.class){
			if(edgeDetector == null){
				synchronized(CannyEdgeDetector.class){
					edgeDetector = new SobelEdgeDetector();
				}
			}
		}
		return edgeDetector;
	}*/
	@Override
	public void setSourceImage(Image image) {
		sourceImage = toBufferedImage(image);
	}

	@Override
	public void process() throws EdgeDetectorException {
		width = sourceImage.getWidth(this);
        height = sourceImage.getHeight(this);
        edgeDetection(sourceImage);
	}

	@Override
	public BufferedImage getEdgeImage() {
		return edgeImage;
	}

	@Override
	public BufferedImage toBufferedImage(Image image) {
		  if (image instanceof BufferedImage) {
		        return (BufferedImage)image;
		     }
		    // This code ensures that all the pixels in the image are loaded
		     image = new ImageIcon(image).getImage();
		 
		    // Determine if the image has transparent pixels; for this method's
		    // implementation, see e661 Determining If an Image Has Transparent Pixels
		    //boolean hasAlpha = hasAlpha(image);
		 
		    // Create a buffered image with a format that's compatible with the screen
		     BufferedImage bimage = null;
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    try {
		        // Determine the type of transparency of the new buffered image
		        int transparency = Transparency.OPAQUE;
		       /* if (hasAlpha) {
		         transparency = Transparency.BITMASK;
		         }*/
		 
		        // Create the buffered image
		         GraphicsDevice gs = ge.getDefaultScreenDevice();
		         GraphicsConfiguration gc = gs.getDefaultConfiguration();
		         bimage = gc.createCompatibleImage(
		         image.getWidth(null), image.getHeight(null), transparency);
		     } catch (HeadlessException e) {
		        // The system does not have a screen
		     }
		 
		    if (bimage == null) {
		        // Create a buffered image using the default color model
		        int type = BufferedImage.TYPE_INT_RGB;
		        //int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
		        /*if (hasAlpha) {
		         type = BufferedImage.TYPE_INT_ARGB;
		         }*/
		         bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		     }
		 
		    // Copy image to buffered image
		     Graphics g = bimage.createGraphics();
		 
		    // Paint the image onto the buffered image
		     g.drawImage(image, 0, 0, null);
		     g.dispose();
		 
		    return bimage;
	}
	/* NPU APPROXIMATION START */
	public double sobel(double[][] window){ 
		double x, y, r = 0; 
		x = ( window[0][0] + 2 * window[0][1] + window[0][2] ); 
		x -= ( window[2][0] + 2 * window[2][1] + window[2][2] );
		y = ( window[0][2] + 2 * window[1][2] + window[2][2] );
		y -= ( window[0][0] + 2 * window[1][1] + window[2][0]);
		r = Math.sqrt( (x*x) + (y*y) );
		if (r > 255.0) r = 0;
		return r;
	}
	/* NPU APPROXIMATION END */

	public void printRGB(int clr){
		int red = (clr & 0x00FF0000) >> 16; int green = (clr & 0x0000FF00)>>8; int blue = (clr & 0x000000FF);
		System.out.println("Red: "+red+" Green: "+green+" Blue: "+blue );
	}

	public  int getGreyScale(int clr){
		int red = (clr & 0x00FF0000) >> 16; int green = (clr & 0x0000FF00)>>8; int blue = (clr & 0x000000FF);
		if ( (red == blue) && (blue == green) ) return red;
		else return -1;
	}

	public int setGreyScaleValue(int clr){
		return (clr) + (clr << 8) + (clr << 16);
	}

	public  double[][] buildWindow(int x, int y, BufferedImage srcImg){
		double[][] retVal = new double[3][3];   
		for ( int ypos = -1; ypos <= 1; ypos++ ){
			for (int xpos = -1; xpos <= 1; xpos++ ){
				int currX = xpos + x; int currY = ypos + y;
				if ( (currX >= 0 && currX < width) && (currY >= 0 && currY < height) ){
					int rgbRawValue = srcImg.getRGB(currX, currY);
					int grayScaleValue = getGreyScale(rgbRawValue);
					retVal[xpos + 1][ypos + 1] = grayScaleValue;
				}
				else
					retVal[xpos + 1][ypos + 1] = 255;
			}
		}
		return retVal; 
	}
	public void edgeDetection(BufferedImage srcImg){
		edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		double[][] window = new double[3][3];
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++ ){
				window = buildWindow(x, y, srcImg);
				// double newValue = sobel(window);
				double newValue = srcImg.getRGB(x,y);
				double sobelValue = sobel(window);
				int sobelRGBValue = setGreyScaleValue((int) sobelValue);
				int grayScaleMag = getGreyScale( (int) newValue);
				int greyscaleValue = setGreyScaleValue(grayScaleMag);
				edgeImage.setRGB(x, y, sobelRGBValue);
			}
		}
	}
	private BufferedImage sourceImage;
	private BufferedImage edgeImage;
	private int height;
	private int width;

}
