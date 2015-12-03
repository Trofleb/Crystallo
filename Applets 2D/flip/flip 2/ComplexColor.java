import java.awt.Color;

/* FFTtest - ComplexColor.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 20 févr. 2006
 * 
 * nicolas.schoeni@epfl.ch
 */

public class ComplexColor {
	public static final int MODE_COMPLEX = 0;
	public static final int MODE_REAL_ONLY = 1;
	public static final int MODE_IM_ONLY = 2;
	public static final int MODE_MAGNITUDE = 3;
	public static final int MODE_PHASE = 4;
	
//	static float RGBToComplexRe(int rgb) {
//		//int r = (rgb&0xff0000)>>16;
//		//int g = (rgb&0xff00)>>8;
//		int b = (rgb&0xff);
//		return 1f-b/255f;
//	}
	
	static void RGBToComplex(int rgb, float[] reIm) {
		if ((rgb&0xffffff)==0) {
			reIm[0] = 1;
			reIm[1] = 0;
		}
		else if ((rgb&0xffffff)==0xffffff) {
			reIm[0] = reIm[1] = 0;
		}
		else {
			int r = (rgb&0xff0000)>>16;
			int g = (rgb&0xff00)>>8;
			int b = (rgb&0xff);
			if (r==g&&r==b) {
				reIm[0] = 1f-r/255f;
				reIm[1] = 0;
			}
			else {
				float[] hsb = new float[3];
				Color.RGBtoHSB(r, g, b, hsb);
				double phi = hsb[0]*2*Math.PI-Math.PI;
				reIm[0] = -hsb[1]*(float)Math.cos(phi);
				reIm[1] = -hsb[1]*(float)Math.sin(phi);
			}
		}
	}
	
//	public static void complexToHSB52(float re, float im, float[] hsb) {
//		double r = Math.sqrt(re*re + im*im);
//    double phi = Math.atan2(-im, -re)+Math.PI;	//[0;2pi]
//
//    //hsb[0] = (float)((phi+Math.PI)/(2*Math.PI));
//    
//    phi+=(4d/20d)*Math.sin(3d*phi);
//    
//    hsb[0] = (float)(phi/(2*Math.PI));
//    hsb[1] = Math.min((float)r, 1f);
//    hsb[2] = 1.0f;
//	}
	
	
	
	public static void HSBToComplex(float h, float s, float b, float[] reIm, int mode) {
		float r = s;
		double phi = h*2*Math.PI-Math.PI;
		reIm[0] = -r*(float)Math.cos(phi);
		reIm[1] = -r*(float)Math.sin(phi);
	
	}
	
	
	public static void complexToHSB(float re, float im, float[] hsb, int mode) {
		if (mode==MODE_REAL_ONLY) im=0; 
		if (mode==MODE_IM_ONLY) re=0; 
		
		double r;
		if (mode==MODE_PHASE) r = 1;
		else  r = Math.sqrt(re*re + im*im);
		
    double phi = Math.atan2(-im, -re)+Math.PI;	//[0;2pi]
    //phi+=(4d/20d)*Math.sin(3d*phi);
    if (mode==MODE_MAGNITUDE) {
    	hsb[0] = hsb[1] = 0;
    	hsb[2] = 1f-Math.min((float)r, 1f);
    }
    else {
      hsb[0] = (float)(phi/(2*Math.PI));
      hsb[1] = Math.min((float)r, 1f);
      hsb[2] = 1.0f;
    }
	}

	public static void complexToHSB_correctBanding(float re, float im, float[] hsb, int mode) {
		if (mode==MODE_REAL_ONLY) im=0; 
		if (mode==MODE_IM_ONLY) re=0; 
		
		double r;
		if (mode==MODE_PHASE) r = 1;
		else  r = Math.sqrt(re*re + im*im);
		
    double phi = Math.atan2(-im, -re)+Math.PI;	//[0;2pi]
    phi+=(4d/20d)*Math.sin(3d*phi);
    if (mode==MODE_MAGNITUDE) {
    	hsb[0] = hsb[1] = 0;
    	hsb[2] = 1f-Math.min((float)r, 1f);
    }
    else {
      hsb[0] = (float)(phi/(2*Math.PI));
      hsb[1] = Math.min((float)r, 1f);
      hsb[2] = 1.0f;
    }
	}
	
//	public static void complexToHSB_OLD(float re, float im, float[] hsb, int mode) {
//		re=-re;
//		im=-im;
//		if (mode==MODE_REAL_ONLY) im=0; 
//		if (mode==MODE_IM_ONLY) re=0; 
//		if (re>1) re=1;
//		if (im>1) im=1;
//		if (re<-1) re=-1;
//		if (im<-1) im=-1;
//
//		double r = Math.sqrt(re*re + im*im);
//		if (mode==MODE_PHASE) r = 1;
//
//    float li = (float)Math.atan(r) * 2.0f/(float)Math.PI;
//
//    if (mode==MODE_MAGNITUDE) {
//    	hsb[0] = hsb[1] = 0;
//    	hsb[2] = 1f-li;
//    }
//    else {
//      hsb[2] = 1.0f;
//      if (li<0.5f) hsb[1] = 2f*li;
//      else hsb[1] = 2f-2f*li;
//      float phi = (float)Math.PI+(float)Math.atan2(im,re);
//      double scale = (double)4.0/20.0;
//      hsb[0] = (float)( phi+scale*Math.sin(3*phi) ) / (float)(2*Math.PI);  // Correct for the "banding" in HSB color where RGB are wider than the others
//    }
//	}
}
