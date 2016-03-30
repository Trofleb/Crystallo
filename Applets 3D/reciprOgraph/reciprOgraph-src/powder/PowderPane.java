/* reciprOgraph - Powder.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 25 ao�t 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package powder;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterJob;
import java.io.InvalidObjectException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.vecmath.Vector3d;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.AbstractSeriesDataset;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import panes.SingleCrystalPane;


import utils.HVPanel;
import utils.Utils3d;

import intensity.Intensity;


public class PowderPane extends HVPanel.v {
	public Intensity intensity;
	public ChartPane chartPane;
	public IntensitiesData dataset;
	public FloatSpinnerEditField peakwidth, lambda;
	public SliderAndValueH slider;
	public JCheckBox showIndex, showPeaks;
	public JButton print;
	
	public PowderPane(Intensity intensity) {
		this.intensity=intensity;
		dataset = new IntensitiesData(intensity);
		chartPane = new ChartPane(dataset);
		expand(true);
		addComp(chartPane.chartPanel);
		expand(false);
		HVPanel.h p1 = new HVPanel.h();
		p1.left();
		p1.expand(false);
		p1.putExtraSpace(5);
		lambda = (FloatSpinnerEditField)new HVPanel.FloatSpinnerEditField("Lambda  ", " Å", 4, (float)IntensitiesData.lambda, "0.000", .01).to(p1);
		lambda.setMinimum(0.5f);
		lambda.setMaximum(3f);
		p1.putExtraSpace(5);
		peakwidth = (FloatSpinnerEditField)new HVPanel.FloatSpinnerEditField("Peak width  ", " °", 3, (float)dataset.Hg, "0.0", .1).to(p1);
		peakwidth.setMinimum(0);
		//HVPanel.v p2 = new HVPanel.v();
		p1.putExtraSpace(10);
		//slider = new HVPanel.SliderAndValueH(null, null, 0, 20, 1, 1, 400).to(p2);
		//slider.slider.setMinimumSize(new Dimension(100, 60));
		//slider.slider.setPreferredSize(new Dimension(600, 60));
		//p1.addSubPane(p2);
		p1.expand(true);
		p1.putExtraSpace();
		p1.expand(false);
		p1.right();
		p1.addButton(chartPane.autoScale=new JCheckBox(" Auto scale "));
		p1.putExtraSpace(5);
		p1.addButton(showPeaks=new JCheckBox(" Show individual peaks "));
		p1.putExtraSpace(5);
		p1.addButton(showIndex=new JCheckBox(" Show indices "));
		p1.putExtraSpace(5);
		p1.addButton(print=new JButton("Print"));
		addSubPane(p1);

		chartPane.autoScale.setSelected(true);
		//slider.slider.setMajorTickSpacing(10);
		//slider.slider.setMinorTickSpacing(1);
		//slider.slider.setPaintTicks(true);
		//slider.slider.setPaintLabels(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==peakwidth) {
			if (peakwidth.getFloatValue()<2) peakwidth.model.step=.1;
			else if (peakwidth.getFloatValue()>9.9) peakwidth.model.step=1;
			else peakwidth.model.step=.5;
			
			if (peakwidth.getFloatValue()<0.1) peakwidth.setValue(0);
			
			if (dataset.needRecalculate==false && dataset.recalculateDone==true) {
				dataset.Hg=peakwidth.getFloatValue();
				dataset.recalculateHg();
				try {
					chartPane.dataset1.validateObject();
				} catch (InvalidObjectException ex) {
					throw new RuntimeException(ex);
				}
			}
		}
		if (e.getSource()==lambda) {
			IntensitiesData.lambda = lambda.getFloatValue();
			dataset.recalculate();
			SingleCrystalPane.needRecalcTheta = true;
			SingleCrystalPane.needRePrint = true;
			try {
				chartPane.dataset1.validateObject();
			} catch (InvalidObjectException ex) {
				throw new RuntimeException(ex);
			}
		}
		else if (e.getSource()==showIndex) {
			chartPane.renderer.setItemLabelsVisible(showIndex.isSelected());
		}
		else if (e.getSource()==showPeaks) {
			dataset.showPeaks=showPeaks.isSelected();
			dataset.recalculateHg();
			try {
				chartPane.dataset1.validateObject();
			} catch (InvalidObjectException ex) {
				throw new RuntimeException(ex);
			}
		}
		else if (e.getSource()==chartPane.autoScale) {
			boolean b = chartPane.autoScale.isSelected();
			chartPane.xAxis.setAutoRange(b);
			chartPane.yAxis.setAutoRange(b);
		}
		else if (e.getSource()==print) {
			chartPane.chartPanel.createChartPrintJob();
		}
	}
}

