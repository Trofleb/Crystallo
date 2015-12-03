import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;

/* EscherFFT - ComplexImageSystem.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 29 mai 2006
 * 
 * nicolas.schoeni@epfl.ch
 */

public class ComplexImageSystem {
	private final int size, size2;
	public  float dataHSB[][][];
	public  Object lastHSB;
	public  int mode=ComplexColor.MODE_COMPLEX;
	
	public ComplexImageSystem(int size) {
		this.size=size;
		size2 = size*2;
		dataHSB = new float[size][size][3];
	}
  
	public void destroy() {
		dataHSB=null;
		lastHSB=null;
  }
		
	public void updateOutput(ComplexImage complexImage) {
		complexImage.updateReIm2HSB();
		complexImage.updateHSB2image();
	}
	
//	public void drawCell(ComplexImage complexImage, DrawCanvas.Cell cell) {
//		DrawCanvas.drawCell(complexImage.image.getGraphics(), Color.black, size/2-cell.offset, size/2-cell.h, cell.a, cell.h, cell.offset);
//	}
//	
	public void setOutputMode(ComplexImage complexImage, int mode) {
		this.mode=mode;
		complexImage.updateReIm2HSB();
		complexImage.updateHSB2image();
	}
	
	public ComplexImage getTempComplexImage(int size, BufferedImage image) {
		return new ComplexImage(size, this, image);
	}
}
