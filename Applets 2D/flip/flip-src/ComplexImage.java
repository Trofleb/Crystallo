import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JLabel;

/* EscherFFT - ComplexImage.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 29 mai 2006
 * 
 * nicolas.schoeni@epfl.ch
 */

public class ComplexImage {
	public  double dataRe[][], dataIm[][];
	public  BufferedImage image; 
	public  int imageInMode;
	public  final int size;
	public  double maxValue=1;
	public  float contrast=1;
	private boolean rot180;
	private ComplexImageSystem complexImageSystem;
	public Random random = new Random();
	private double[] tmpDelta, tmpWeak; 

	public ComplexImage(int size, ComplexImageSystem complexImageSystem) {
		this(size, complexImageSystem, OutputSystem.createImage(size, Color.white));
	}
	public ComplexImage(int size, ComplexImageSystem complexImageSystem, BufferedImage image) {
		this.image=image;
		this.complexImageSystem=complexImageSystem;
		this.size=size;
		dataRe = new double[size][size];
		dataIm = new double[size][size];
		tmpDelta = new double[size*size];	
		tmpWeak = new double[size*size];	
	}
	
	public void reset() {
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				dataRe[i][j] = 0;
				dataIm[i][j] = 0;
			}
		}
	}

	public Object clone() {
		ComplexImage c = new ComplexImage(size, complexImageSystem);
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				c.dataRe[i][j] = dataRe[i][j];
				c.dataIm[i][j] = dataIm[i][j];
			}
		}
		c.maxValue = maxValue;
		c.contrast = contrast;
		c.imageInMode = imageInMode;
		c.image.getGraphics().drawImage(image, 0, 0, null);
		return c;
	}

	public void load(ComplexImage other, int mode) {
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				if (mode==ComplexColor.MODE_COMPLEX) {
					dataRe[i][j] = other.dataRe[i][j];
					dataIm[i][j] = other.dataIm[i][j];
				}
				else if (mode==ComplexColor.MODE_REAL_ONLY) {
					dataRe[i][j] = other.dataRe[i][j];
					dataIm[i][j] = 0;
				}
				else if (mode==ComplexColor.MODE_IM_ONLY) {
					dataRe[i][j] = 0;
					dataIm[i][j] = other.dataIm[i][j];
				}
				else if (mode==ComplexColor.MODE_MAGNITUDE) {
					dataRe[i][j] = other.dataRe[i][j]*other.dataRe[i][j]+other.dataIm[i][j]*other.dataIm[i][j];
					dataIm[i][j] = 0;
				}
				else if (mode==ComplexColor.MODE_PHASE) {
					double phi = Math.atan2(other.dataIm[i][j], other.dataRe[i][j]);
					dataRe[i][j] = (float)Math.cos(phi);
					dataIm[i][j] = (float)Math.sin(phi);
				}
			}
		}
	}
	
	
	public void load(BufferedImage image) {
		//long t0 = System.currentTimeMillis();
		int w = image.getWidth();
		int h = image.getHeight();
		int off = size/2;
		float[] reIm = new float[2];
		for (int x=0; x<size; x++) {
			for (int y=0; y<size; y++) {
				int i=(x+w/2)%size, j=(y+h/2)%size;
				int rgb = image.getRGB(x, y);
				ComplexColor.RGBToComplex(rgb, reIm);
				dataRe[i][j] = reIm[0];
				dataIm[i][j] = reIm[1];
			}
		}
		//long t1 = System.currentTimeMillis();
		//System.out.println("load "+(t1-t0)+" ms");
	}
	
	private void setPhase(int i, int j, double phi) {
		if (i<0) i=256+i;
		if (j<0) j=256+j;
		double r = Math.sqrt(dataRe[i][j]*dataRe[i][j]+dataIm[i][j]*dataIm[i][j]);
		dataRe[i][j] = (r*Math.cos(phi));
		dataIm[i][j] = (r*Math.sin(phi));
	}
	
	private double getPhase(int i, int j) {
		if (i<0) i=256+i;
		if (j<0) j=256+j;
		return Math.atan2(dataIm[i][j], dataRe[i][j]);
	}

	private void checkPhase(int i, int j, double phi) {
		if (i<0) i=256+i;
		if (j<0) j=256+j;
		double p = Math.atan2(dataIm[i][j], dataRe[i][j]);
		if (p<0) p=Math.PI-p;
		if (phi<0) phi=Math.PI-phi;
		
		if (Math.abs(p-phi)>0.001) {
			System.out.println(i+" "+j+" "+(((int)(p*10000))/10000d)+" "+(((int)(phi*10000))/10000d));
		}
	}
	
	
//	private void rotPhase90(int i, int j) {
//		if (i<0) i=256+i;
//		if (j<0) j=256+j;
//		double t = dataRe[i][j];
//		dataRe[i][j] = -dataIm[i][j];
//		dataIm[i][j] = t;
//	}
	
	public void setRandomPhases() {
		for (int i=-size/2; i<size/2; i++) {
			for (int j=1; j<size/2; j++) {
				double phi = random.nextDouble()*2D*Math.PI;
				//phi=Math.PI/3;
				setPhase(i, j, phi);
				setPhase(-i, -j, -phi);
			}
		}
		for (int i=1; i<size/2; i++) {
			double phi = random.nextDouble()*2D*Math.PI;
			//phi=Math.PI/3;
			setPhase(i, 0, phi);
			setPhase(-i, 0, -phi);
		}
		for (int i=1; i<size/2; i++) {
			double phi = random.nextDouble()*2D*Math.PI;
			//phi=Math.PI/3;
			setPhase(i, -size/2, phi);
			setPhase(-i, -size/2, -phi);
		}
//		setPhase(0, 0, 0); 
//		setPhase(0, -size/2, 0); 
//		setPhase(-size/2, -size/2, 0); 
//		setPhase(-size/2, 0, 0); 
	}
	
	

	
	public void checkPhases() {
		for (int i=-size/2; i<size/2; i++) {
			for (int j=1; j<size/2; j++) {
				checkPhase(-i, -j, -getPhase(i, j));
			}
		}
		for (int i=1; i<size/2; i++) {
			checkPhase(-i, 0, -getPhase(i, 0));
		}
		for (int i=1; i<size/2; i++) {
			checkPhase(-i, -size/2, -getPhase(i, -size/2));
		}
		checkPhase(0, 0, 0); 
		checkPhase(0, -size/2, Math.PI); 
		checkPhase(-size/2, -size/2, Math.PI); 
		checkPhase(-size/2, 0, 0); 
	}
	
	public double getF0() {
		return dataRe[0][0];
	}
	
//	public void flipCharge() {
//		for (int i=0; i<size; i++) {
//			for (int j=0; j<size; j++) {
//				if (dataRe[i][j]<0) dataRe[i][j] = -dataRe[i][j];
//				if (dataIm[i][j]<0) dataIm[i][j] = -dataIm[i][j];
//				//dataIm[i][j]=0;
//			}
//		}
//		calculateMaxValue();
//	}

	public void flipCharge(double delta) {
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				if (dataRe[i][j]<delta) dataRe[i][j] = -dataRe[i][j];
				if (dataIm[i][j]<delta) dataIm[i][j] = -dataIm[i][j];
				//dataIm[i][j]=0;
			}
		}
		calculateMaxValue();
	}

	public double[][] backupAmplitudes() {
		double[][] F0 = new double[size][size];
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				F0[i][j] = Math.sqrt(dataRe[i][j]*dataRe[i][j]+dataIm[i][j]*dataIm[i][j]);
			}
		}
		return F0;
	}
	
	
	private double diff, sum;
	public double restoreAmplitudes(double[][] F0, double weakValue, int maskRadius) {
		diff=0; sum=0;
		int r2 = maskRadius*maskRadius;
		int s2 = size/2;

		for (int i=-size/2; i<size/2; i++) {
			for (int j=1; j<size/2; j++) {
				restoreAmplitude(i, j, F0, weakValue, r2, s2);
			}
		}
		for (int i=1; i<size/2; i++) {
			restoreAmplitude(i, 0, F0, weakValue, r2, s2);
		}
		for (int i=1; i<size/2; i++) {
			restoreAmplitude(i, -size/2, F0, weakValue, r2, s2);
		}

		restoreAmplitude(0, 0, F0, weakValue, r2, s2);
		restoreAmplitude(0, -size/2, F0, weakValue, r2, s2);
		restoreAmplitude(-size/2, -size/2, F0, weakValue, r2, s2);
		restoreAmplitude(-size/2, 0, F0, weakValue, r2, s2);

		calculateMaxValue();
		return diff/sum;
	}
	
	public void restoreAmplitude(int i, int j, double[][] F0, double weakValue, int maskRadius2, int size2) {
		int i2 = ((i+size2)%size)-size2; 
		int j2 = ((j+size2)%size)-size2;
		if (i<0) i=256+i;
		if (j<0) j=256+j;
		int minusi = -i;
		int minusj = -j;
		if (minusi<0) minusi=256+minusi;
		if (minusj<0) minusj=256+minusj;
		double Gn = Math.sqrt(dataRe[i][j]*dataRe[i][j]+dataIm[i][j]*dataIm[i][j]);
		if (i==0&&j==0) {
			dataRe[i][j] = Gn;
			dataIm[i][j] = 0;
			sum += F0[i][j];
			diff += Math.abs(Gn-F0[i][j]);
		}
		else if (i==128&&j==128) {
			dataRe[i][j] = -Gn;
			dataIm[i][j] = 0;
			sum += F0[i][j];
			diff += Math.abs(Gn-F0[i][j]);
		}
		else if (i==128&&j==0) {
			dataRe[i][j] = Gn;
			dataIm[i][j] = 0;
			sum += F0[i][j];
			diff += Math.abs(Gn-F0[i][j]);
		}
		else if (i==0&&j==128) {
			dataRe[i][j] = -Gn;
			dataIm[i][j] = 0;
			sum += F0[i][j];
			diff += Math.abs(Gn-F0[i][j]);
		}
		else if (i2*i2+j2*j2>maskRadius2) {
			dataRe[i][j] = 0;
			dataIm[i][j] = 0;
			dataRe[minusi][minusj] = 0;
			dataIm[minusi][minusj] = 0;
			sum += F0[i][j];
			diff += Math.abs(Gn-F0[i][j]);
			sum += F0[minusi][minusj];
			diff += Math.abs(Gn-F0[minusi][minusj]);
		}
		else if (F0[i][j]<weakValue) {
			double phi = Math.atan2(dataIm[i][j], dataRe[i][j]);
			dataRe[i][j] = -Gn*Math.sin(phi);
			dataIm[i][j] = Gn*Math.cos(phi);
			dataRe[minusi][minusj] = dataRe[i][j];
			dataIm[minusi][minusj] = -dataIm[i][j];
//			sum += F0[i][j];
//			diff += Math.abs(Gn-F0[i][j]);
//			sum += F0[minusi][minusj];
//			diff += Math.abs(Gn-F0[minusi][minusj]);
		}
		else {
			double phi = Math.atan2(dataIm[i][j], dataRe[i][j]);
			dataRe[i][j] = F0[i][j]*Math.cos(phi);
			dataIm[i][j] = F0[i][j]*Math.sin(phi);
			dataRe[minusi][minusj] = dataRe[i][j];
			dataIm[minusi][minusj] = -dataIm[i][j];
			sum += F0[i][j];
			diff += Math.abs(Gn-F0[i][j]);
			sum += F0[minusi][minusj];
			diff += Math.abs(Gn-F0[minusi][minusj]);
		}
	}
	
	public double findDelta(int deltaLimit) {
		if (deltaLimit==0) return 0;
		for (int i=0,k=0; i<size; i++) {
			for (int j=0; j<size; j++,k++) {
				tmpDelta[k] = dataRe[i][j];
			}
		}
		return NumericalRecipes.select(size*size*(100-deltaLimit)/100, tmpDelta);
	}
	
	public synchronized double calculateWeakValue(double[][] F0, int weakLimit) {
		for (int i=0,k=0; i<size; i++) {
			for (int j=0; j<size; j++,k++) {
				tmpWeak[k] = F0[i][j];
			}
		}
		return NumericalRecipes.select(size*size*weakLimit/100, tmpWeak);
	}
		
	public void doFFT() {
		fft(size, dataRe, dataIm);
		calculateMaxValue();
	}
	public void doFFTBack() {
		fft_back(size, dataRe, dataIm);
		calculateMaxValue();
	}
	
	//TODO
	public void setLabelValue(int x, int y, JLabel label, OutputSystem.ColorRef colorRef) {
		if (x<0||y<0||x>=size||y>=size) {
			label.setText(" ");
		}
		else {
			int off = size/2;

			//System.out.print(x+" "+y);
			
			x=(x+size+off)%size;
			y=(y+size+off)%size;
			
			//System.out.println(" "+x+" "+y);
			
			float re = (float)dataRe[x][y];
			float im = (float)dataIm[x][y];
			float a = (float)Math.sqrt(re*re+im*im);
			int phi = (int)Math.round(Math.atan2(im, re)*180f/Math.PI);
			re = Math.round(re*100f)/100f;
			im =  Math.round(im*100f)/100f;
			a =  Math.round(a*100f)/100f;
//			float[] hsb = new float[3];
//			ComplexColor.complexToHSB(re/maxValue, im/maxValue, hsb, mode);			
//			Color c = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
//			label.setForeground(c);
			label.setText("Re="+re+" Im="+im+" A="+a+" \u03c6="+phi+"°");
			colorRef.re=re; colorRef.im=im; 
		}
	}
	
	public void updateReIm2HSB() {
		//long t0 = System.currentTimeMillis();
		imageInMode = complexImageSystem.mode;
		complexImageSystem.lastHSB = this;
		int off = size/2;
		for (int y=0; y<size; y++) {
			for (int x=0; x<size; x++) {
				int x2=(x+off)%size, y2=(y+off)%size;
//				int x2 = (x+size+off)%size;
//				int y2 = (y+size+off)%size;
				ComplexColor.complexToHSB((float)(dataRe[x2][y2]/maxValue), (float)(dataIm[x2][y2]/maxValue), complexImageSystem.dataHSB[x][y], complexImageSystem.mode);
			}
		}
		//long t1 = System.currentTimeMillis();
		//System.out.println("ReIm->HSB "+(t1-t0)+" ms");
	}
	public void updateHSB2image() {
		//long t0 = System.currentTimeMillis();
		for (int x=0; x<size; x++) {
			for (int y=0; y<size; y++) {
				if (complexImageSystem.mode==ComplexColor.MODE_MAGNITUDE) {
					float bright = 1f-((1f-complexImageSystem.dataHSB[x][y][2])*contrast);
					if (bright>1) bright=1;
					if (bright<0) bright=0;
					int rgb = Color.HSBtoRGB(complexImageSystem.dataHSB[x][y][0], complexImageSystem.dataHSB[x][y][1], bright);
					if (rot180) {
						image.setRGB(size-1-x, size-1-y, rgb);
					}
					else {
						image.setRGB(x, y, rgb);
					}
				}
				else {
					float sat = complexImageSystem.dataHSB[x][y][1]*contrast;
					if (sat>1) sat=1;
					if (sat<0) sat=0;
					int rgb = Color.HSBtoRGB(complexImageSystem.dataHSB[x][y][0], sat, complexImageSystem.dataHSB[x][y][2]);
					if (rot180) {
						image.setRGB(size-1-x, size-1-y, rgb);
					}
					else {
						image.setRGB(x, y, rgb);
					}
				}
			}
		}
		//long t1 = System.currentTimeMillis();
		//System.out.println("HSB->image "+(t1-t0)+" ms");
	}
	
	public void setContrast(float contrast) {
		this.contrast=contrast;
		// check if hsb still valid
		if (complexImageSystem.lastHSB!=this) {
			updateReIm2HSB();
		}
		updateHSB2image();
	}
	
	public void rot180() {
		rot180=!rot180;
		if (complexImageSystem.lastHSB!=this) {
			updateReIm2HSB();
		}
		updateHSB2image();
	}

	public void calculateMaxValue() {
		maxValue=1;
		int off = size/2;
		for (int y=0; y<size; y++) {
			for (int x=0; x<size; x++) {
				int x2 = (x+size+off)%size;
				int y2 = (y+size+off)%size;
				if (dataRe[x2][y2]>maxValue) maxValue=dataRe[x2][y2];
				if (-dataRe[x2][y2]>maxValue) maxValue=-dataRe[x2][y2];
				if (dataIm[x2][y2]>maxValue) maxValue=dataIm[x2][y2];
				if (-dataIm[x2][y2]>maxValue) maxValue=-dataIm[x2][y2];
			}
		}
	}
	
	public void destroy() {
		if (image!=null) image.flush();
		image = null;
		dataRe=null;
		dataIm=null;
  }
	
	private final static double sTwoPi = 2.0 * Math.PI;
	
	private static void fft(int size, double[][] fr, double[][] fi)
	{
		double tr[]	= new double[size];
		double ti[]	= new double[size];
		
		// fft on the image rows
		for (int y=0; y<size; y++) {
			float_fft1d(fr[y], fi[y], size);
		}

		// fft on the columns, use temporary storage
		for (int x=0; x<size; x++) {
			for (int y=0; y<size; y++) {
				tr[y] = fr[y][x];
				ti[y] = fi[y][x];
			}
			float_fft1d(tr, ti, size);
			for (int y=0; y<size; y++) {
				fr[y][x] = tr[y];
				fi[y][x] = ti[y];
			}
		}
	}
	private static void fft_back(int size, double[][] fr, double[][] fi)
	{
		double tr[]	= new double[size];
		double ti[]	= new double[size];
		
		// negate im
		for (int y=0; y<size; y++) {
			for (int x=0; x<size; x++) {
				fi[y][x] = -fi[y][x];
			}
		}
		
		// fft on the image rows
		for (int y=0; y<size; y++) {
			float_fft1d(fr[y], fi[y], size);
		}
		
		// fft on the columns, use temporary storage
		for (int x=0; x<size; x++) {
			for (int y=0; y<size; y++) {
				tr[y] = fr[y][x];
				ti[y] = fi[y][x];
			}
			float_fft1d(tr, ti, size);
			for (int y=0; y<size; y++) {
				fr[y][x] = tr[y];
				fi[y][x] = ti[y];
			}
		}
		
		// negate back and rescale
		float scale = 1.0f / ((float)size * (float)size);
		for (int y=0; y<size; y++) {
			for (int x=0; x<size; x++) {
				fr[y][x] *= scale;
				fi[y][x] *= -scale;
			}
		}
	}
	  
	  private static void float_fft1d	(double xr [], double xi [], int n)
		{
//		----------------------------------------------------------------------------
	    int k, k0, k1, k2, k3, kinc, kinc2;
	    double qr, qi, rr, ri, sr, si, tr, ti, ur, ui;
	    double x1, w0r, w0i, w1r, w1i, w2r, w2i, w3r, w3i;
//		----------------------------------------------------------------------------
//	  radix 4 section
//		----------------------------------------------------------------------------
	    kinc = n;
//		----------------------------------------------------------------------------
	    while (kinc >= 4) {
	      kinc2	= kinc;
	      kinc	= kinc / 4;
//		----------------------------------------------------------------------------
	      for (k0 = 0; k0 < n; k0 += kinc2) {
	        k1 = k0 + kinc;
	        k2 = k1 + kinc;
	        k3 = k2 + kinc;
	        rr = xr[k0] + xr[k2];
	        ri = xi[k0] + xi[k2];
	        sr = xr[k0] - xr[k2];
	        si = xi[k0] - xi[k2];
	        tr = xr[k1] + xr[k3];
	        ti = xi[k1] + xi[k3];
	        ur = -xi[k1] + xi[k3];
	        ui = xr[k1] - xr[k3];
	        xr[k0] = rr + tr;
	        xi[k0] = ri + ti;
	        xr[k2] = sr + ur;
	        xi[k2] = si + ui;
	        xr[k1] = rr - tr;
	        xi[k1] = ri - ti;
	        xr[k3] = sr - ur;
	        xi[k3] = si - ui;
	      }
//		----------------------------------------------------------------------------
	      x1	= (double) sTwoPi / (double) kinc2;
	      w0r = (double) Math.cos (x1);
	      w0i = (double) Math.sin (x1);
	      w1r = 1.0f;
	      w1i = 0.0f;
//		----------------------------------------------------------------------------
	      for (int i = 1; i < kinc; i++) {
	        x1	= w0r * w1r - w0i * w1i;
	        w1i = w0r * w1i + w0i * w1r;
	        w1r = x1;
	        w2r = w1r * w1r - w1i * w1i;
	        w2i = w1r * w1i + w1i * w1r;
	        w3r = w2r * w1r - w2i * w1i;
	        w3i = w2r * w1i + w2i * w1r;
//		----------------------------------------------------------------------------
	        for (k0 = i; k0 < n; k0 += kinc2) {
	          k1			=  k0 + kinc;
	          k2			=  k1 + kinc;
	          k3			=  k2 + kinc;
	          rr			=  xr [k0] + xr [k2];
	          ri			=  xi [k0] + xi [k2];
	          sr			=  xr [k0] - xr [k2];
	          si			=  xi [k0] - xi [k2];
	          tr			=  xr [k1] + xr [k3];
	          ti			=  xi [k1] + xi [k3];
	          ur			= -xi [k1] + xi [k3];
	          ui			=  xr [k1] - xr [k3];
	          xr [k0]	=  rr + tr;
	          xi [k0] =  ri + ti;
//		----------------------------------------------------------------------------
	          qr = sr + ur;
	          qi = si + ui;
	          xr[k2] = qr * w1r - qi * w1i;
	          xi[k2] = qr * w1i + qi * w1r;
//		----------------------------------------------------------------------------
	          qr = rr - tr;
	          qi = ri - ti;
	          xr[k1] = qr * w2r - qi * w2i;
	          xi[k1] = qr * w2i + qi * w2r;
//		----------------------------------------------------------------------------
	          qr = sr - ur;
	          qi = si - ui;
	          xr[k3] = qr * w3r - qi * w3i;
	          xi[k3] = qr * w3i + qi * w3r;
	        }
	      }
	    }
//		----------------------------------------------------------------------------
//	  radix 2 section
//		----------------------------------------------------------------------------
	    while (kinc >= 2) {
	      kinc2	= kinc;
	      kinc	= kinc / 2;
	      x1		= (double) sTwoPi / (double) kinc2;
	      w0r		= (double) Math.cos (x1);
	      w0i		= (double) Math.sin (x1);
	      w1r		= 1.0f;
	      w1i		= 0.0f;
//		----------------------------------------------------------------------------
	      for (k0 = 0; k0 < n; k0 += kinc2) {
	        k1			= k0 + kinc;
	        tr			= xr [k0] - xr [k1];
	        ti			= xi [k0] - xi [k1];
	        xr [k0]	= xr [k0] + xr [k1];
	        xi [k0] = xi [k0] + xi [k1];
	        xr [k1] = tr;
	        xi [k1] = ti;
	      }
//		----------------------------------------------------------------------------
	      for (int i = 1; i < kinc; i++) {
	        x1	= w0r * w1r - w0i * w1i;
	        w1i = w0r * w1i + w0i * w1r;
	        w1r = x1;
//		----------------------------------------------------------------------------
	        for (k0 = i; k0 < n; k0 += kinc2) {
	          k1			= k0 + kinc;
	          tr			= xr [k0] - xr [k1];
	          ti			= xi [k0] - xi [k1];
	          xr [k0] = xr [k0] + xr [k1];
	          xi [k0] = xi [k0] + xi [k1];
	          xr [k1] = tr * w1r - ti * w1i;
	          xi [k1] = tr * w1i + ti * w1r;
	        }
	      }
	    }
//		----------------------------------------------------------------------------
//	  bit reverse order
//		----------------------------------------------------------------------------
	    int nv2 = n / 2;
	    int nm1 = n - 1;
	    int j = 0;
//		----------------------------------------------------------------------------
	    for (int i = 0; i < nm1; i++) {
//		----------------------------------------------------------------------------
	      if (i < j) {
	        tr			= xr [j];
	        ti			= xi [j];
	        xr [j]	= xr [i];
	        xi [j]	= xi [i];
	        xr [i]	= tr;
	        xi [i]	= ti;
	      }
	      k = nv2;
//		----------------------------------------------------------------------------
	      while (k <= j) {
	        j -= k;
	        k = k >> 1;
	      }
//		----------------------------------------------------------------------------
	      j += k;
	    }
//		----------------------------------------------------------------------------
	  }
}
