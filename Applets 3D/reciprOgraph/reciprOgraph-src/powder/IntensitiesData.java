/* ReciprOgraph - IntensitiesDataset.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 5 sept. 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package powder;

import java.util.Vector;

import intensity.Intensity;

import javax.vecmath.Vector3d;

import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;


public class IntensitiesData implements XYItemLabelGenerator {
	public static double lambda = 1.541;
	public double Hg = .3;
	
	public int precision = 720;
	private double ag, bg;
	private double dtheta2;
	private double[] theta2Deg = new double[precision]; 
	private double[] theta2Rad = new double[precision];
	public double[] yi = new double[precision];
	private double[] yic = new double[precision];
	private int[] braggs = new int[precision]; 
	private int braggsCount;
	private int[] mi = new int[precision];
	private String[][] hkl = new String[precision][];
	private double[] d = new double[precision];
	private int[] toolTipIndex = new int[precision];
	private Vector peaks = new Vector(precision/4, precision/4);
	private Intensity intensity;
	public boolean needRecalculate = true;
	public boolean recalculateDone = false;
	public boolean showPeaks=false;
	public boolean[] labelHidden = new boolean[precision];
	
	public IntensitiesData(Intensity intensity) {
		this.intensity=intensity;
		dtheta2 = 180d/precision;
		for (int i=0; i<precision; i++) {
			theta2Deg[i] = (double)i*180d/precision;
			theta2Rad[i] = (double)i*Math.PI/precision;
		}
	}
	
	public void recalculate() {
		for (int i=0; i<precision; i++) {
			yi[i]=0; mi[i]=0; hkl[i]=null; d[i]=0;
		}
		braggsCount=0;
		
		double l2 = 2/lambda;
		int hMax=(int)Math.ceil(l2/intensity.reciprocal.x.length());
		int kMax=(int)Math.ceil(l2/intensity.reciprocal.y.length());
		int lMax=(int)Math.ceil(l2/intensity.reciprocal.z.length());
		double bcCosAlphaStar2 = 2d*intensity.reciprocal.b*intensity.reciprocal.c*Math.cos(intensity.reciprocal.alpha*Math.PI/180d);
		double acCosBetaStar2 = 2d*intensity.reciprocal.a*intensity.reciprocal.c*Math.cos(intensity.reciprocal.beta*Math.PI/180d);
		double abCosGammaStar2 = 2d*intensity.reciprocal.a*intensity.reciprocal.b*Math.cos(intensity.reciprocal.gamma*Math.PI/180d);
		//System.out.println("hMax="+hMax+", kMax="+kMax+", lMax="+lMax);
		
		for (int h=-hMax; h<=hMax; h++) {
			double has2 = h*h*intensity.reciprocal.a*intensity.reciprocal.a;
			for (int k=-kMax; k<=kMax; k++) {
				double kbs2 = k*k*intensity.reciprocal.b*intensity.reciprocal.b;
				for (int l=-lMax; l<=lMax; l++) {
					if (h==0&&k==0&&l==0) continue;

					double lcs2 = l*l*intensity.reciprocal.c*intensity.reciprocal.c;

					double H = has2+kbs2+lcs2+k*l*bcCosAlphaStar2+h*l*acCosBetaStar2+h*k*abCosGammaStar2;
					double hh = Math.sqrt(H);
					if(hh>l2) continue;
					
					double I = intensity.I(h, k, l, H/4d);
					if (I<0.0001) continue;

					double theta2 = 2*Math.asin(lambda*hh/2);
					if (Double.isNaN(theta2)) continue;

					double lp = (1+Math.pow(Math.cos(theta2), 2))/(2*Math.pow(Math.sin(theta2/2), 2)*Math.cos(theta2/2));
					
					//System.out.println("theta="+Utils3d.posToString(theta2*180/Math.PI)+" d="+Utils3d.posToString(d_hkl)+" I="+Utils3d.posToString(I)+" lp="+Utils3d.posToString(lp));
					
					int i = (int)Math.round(theta2*precision/(Math.PI)); 
					if (yi[i]==0) braggs[braggsCount++] = i;
					yi[i] += I*lp;
					mi[i]++;
					if (hkl[i]==null) hkl[i]=new String[3];
					addHklIndices(hkl[i], h, k, l);
					d[i] = 1/hh;
					toolTipIndex[i]=i;
				}
			}
		}
		
		recalculateHg();
	}

	private void addHklIndices(String[] previous, int h, int k, int l) {
		String s2 = h+" "+k+" "+l;

		int c = 0;
		if (h<=0) c++;
		if (k<=0) c++;
		if (l<=0) c++;
		if (c>=2) {
			h=-h; k=-k; l=-l;
		}
		
		String s0 = h+" "+k+" "+l;
		h = Math.abs(h);
		k = Math.abs(k);
		l = Math.abs(l);
		if (k<h) {int t=h; h=k; k=t;} 
		if (l<h) {int t=l; l=k; k=h; h=t;}
		else if (l<k) {int t=l; l=k; k=t;}

		String s1 = h+" "+k+" "+l;
		
		//System.out.println(s);
		
		if (previous[2]==null) {
			previous[2]=s2;
		}
		else {
			previous[2]=previous[2]+", "+s2;
		}

		if (previous[0]==null) {
			previous[0]=s0;
			previous[1]=s1;
		}
		else if (previous[1].indexOf(s1)==-1) {
			previous[0]=previous[0]+", "+s0;
			previous[1]=previous[1]+", "+s1;
		}
	}
	
	
	public void recalculateHg() {
		if (Hg==0) {
			//TODO tooltips...
		}
		else {
			for (int i=0; i<precision; i++) {
				yic[i]=0; toolTipIndex[i]=-1;
			}
			peaks.clear();
			
			ag = (2.0/Hg)*Math.sqrt(Math.log(2.0)/Math.PI);
			bg = (4.0*Math.log(2.0))/(Hg*Hg);

			for (int i=0; i<precision; i++) {
				if (yi[i]<0.001) continue;
				
				Vector v = new Vector(precision/4, precision/4);
				peaks.add(v);
				
				for (int k=0; i+k<precision||i-k>=0; k++) {
					double yig = yi[i]*G(dtheta2*k);
					if (yig<0.0001) break;
					if (i+k<precision) {
						if (showPeaks) {
							v.add(new Double(theta2Deg[i+k]));
							v.add(new Double(yig));
						}
						yic[i+k]+=yig;
						toolTipIndex[i+k] = i;
					}
					if (k!=0&&i-k>=0) {
						if (showPeaks) {
							v.add(0, new Double(yig));
							v.add(0, new Double(theta2Deg[i-k]));
						}
						if (yig>=yic[i-k]) toolTipIndex[i-k] = i;
						yic[i-k]+=yig;
					}
					//System.out.println("i="+i+" k="+k+" yik="+yig+" yi="+yi[i]+" G="+G(dtheta2*k)+" 2ti-2tk="+(dtheta2*k));
				}
			}
		}
		recalculateDone=true;
	}
	
	private double G(double x) {
		return ag*Math.exp(-bg*x*x);
	}

	public double theta2RadTod(double theta2) {
		return lambda/(2*Math.sin(theta2/2));
	}
	
	
	public String generateLabel(XYDataset dataset, int series, int item) {
		if (dataset instanceof GaussDataset) {
			if (series>0) return null;
			if (Hg==0) {
				if (item%3==1) {
					if (hkl[item/3]==null) return "";
					return hkl[item/3][0];
				}
				else return null;
			}
			else {
				if (hkl[item]==null) return "";
				return hkl[item][0];
			}
		}
		else return null;
	}
		
	public class GaussDataset extends AbstractXYDataset implements XYDataset, XYToolTipGenerator {
		public int getItemCount(int series) {
			if (series>0) {
				return ((Vector)peaks.get(series-1)).size()/2;
			}
			if (Hg==0) return precision*3;
			else return precision;
		}
		public int getSeriesCount() {
			if (recalculateDone==false) return 0;
			if (!showPeaks || Hg==0) return 1;
			return 1+peaks.size();
		}
		public Comparable getSeriesKey(int series) {
			return "";
		}
		public Number getX(int series, int item) {
			if (series>0) {
				return (Double)((Vector)peaks.get(series-1)).get(item*2);
			}
			else if (Hg==0) {
				return new Double(theta2Deg[item/3]);	//TODO
				//return new Double(d[item/3]);			//TODO
			}
			else {
				return new Double(theta2Deg[item]);			//TODO
//				if (yic[item]<0.1) return new Double(0);
//				return new Double(theta2RadTod(theta2Rad[item]));			//TODO
			}
		}
		public Number getY(int series, int item) {
			if (series>0) {
				return (Double)((Vector)peaks.get(series-1)).get(item*2+1);
			}

			if (Hg==0) {
				if (item%3==1) return new Double(yi[item/3]);
				else return new Double(0);
			}
			else {
				return new Double(yic[item]);
			}
		}
		public String generateToolTip(XYDataset dataset, int series, int item) {
			if (Hg==0) {
				return (item/3)+" "+toolTipIndex[item/3];
			}
			else {
				item = toolTipIndex[item];
			}
			if (item==-1) return null;
			return hkl[item][2]+"\n2\u03b8="+theta2Deg[item]+"° d="+Math.round(d[item]*1000)/1000d+" m="+mi[item];
		}
	}
	
	public class BraggsDataset extends AbstractXYDataset implements XYDataset, XYToolTipGenerator {
		public int getItemCount(int series) {
			return braggsCount;
		}
		public int getSeriesCount() {
			if (recalculateDone==false) return 0;
			return 1;
		}
		public Comparable getSeriesKey(int series) {
			return "";
		}
		public Number getX(int series, int item) {
			item = braggs[item];
			return new Double(theta2Deg[item]);	//TODO
//			if (yic[item]<0.1) return new Double(0);
//			return new Double(theta2RadTod(theta2Rad[item]));	//TODO
		}
		public Number getY(int series, int item) {
			return new Double(0);
		}
		public String generateToolTip(XYDataset dataset, int series, int item) {
			item = braggs[item];
			return hkl[item][2]+"\n2\u03b8="+theta2Deg[item]+"° d="+Math.round(d[item]*1000)/1000d+" m="+mi[item];
		}
	}
}