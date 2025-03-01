package framework;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	public static BufferedImage LOADING_PAGE = getImg("LOADING_PAGE");
	
	private static BufferedImage getImg(String fileName) {
		//File must be "PNG"
		File f = new File("res/" + fileName + ".png");
		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
		} catch(Exception e) {e.printStackTrace();}
		
		return img;
	}
	
}
