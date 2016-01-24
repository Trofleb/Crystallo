/* reciprOgraph - ChartPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 25 août 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package powder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.InvalidObjectException;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.Spacer;
import org.jfree.ui.TextAnchor;

import powder.IntensitiesData.BraggsDataset;

import utils.MultiLineToolTip;


public class ChartPane {
  public ChartPanel chartPanel;
  private JFreeChart jfreechart;
  private IntensitiesData data;
  private XYPlot xyplot;
  public NumberAxis xAxis, yAxis;
  public XYAreaRenderer renderer;
  public IntensitiesData.GaussDataset dataset1;
  public IntensitiesData.BraggsDataset dataset2;
	public JCheckBox autoScale;
  
  public ChartPane(IntensitiesData data) {
  	this.data=data;
  	dataset1 = data.new GaussDataset();
  	dataset2 = data.new BraggsDataset();

  	ToolTipManager.sharedInstance().setDismissDelay(100000);

    xAxis = new NumberAxis(" 2 Theta");		//"2 \u03b8"
    yAxis = new NumberAxis("Intensity");
    
    
    //renderer = new XYLineAndShapeRenderer(true, true) {
    renderer = new XYAreaRenderer(XYAreaRenderer.AREA, dataset1, null) {
      public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
      	super.drawItem(g2, state, dataArea, info, plot, domainAxis, rangeAxis, dataset, series, item, crosshairState, pass);
      	
      	//System.out.println(dataset.);
      	
      	//if (ChartPane.this.data.Hg==0) item=item/3;

//  			if (ChartPane.this.data.Hg==0) {
//  				if (item%3==1) item=item/3;
//  				else return;
//  			}
      	
      	int k=1;
      	if (ChartPane.this.data.Hg==0) k=3;
      	
      	if(pass==0 && isSeriesItemLabelsVisible(series)) {
        	double x = dataset.getXValue(series, item);
        	double y = dataset.getYValue(series, item);
          double x1 = domainAxis.valueToJava2D(x, dataArea, plot.getDomainAxisEdge());
          double y1 = rangeAxis.valueToJava2D(y, dataArea, plot.getRangeAxisEdge());
          
          double d0 = chartPanel.translateScreenToJava2D(new Point(100, 0)).getX();
          double d = chartPanel.translateScreenToJava2D(new Point(110, 0)).getX();
          d0 = domainAxis.java2DToValue(100, dataArea, plot.getDomainAxisEdge());
          d = domainAxis.java2DToValue(110, dataArea, plot.getDomainAxisEdge());
          d = d-d0;
          d = d*ChartPane.this.data.precision/180d;
          //System.out.println(d);
          

          double y2 = ChartPane.this.data.yi[item/k];
          for (int i=0; i<d; i++) {
          	if (item/k-i>=0 && ChartPane.this.data.yi[item/k-i]>y2 && !ChartPane.this.data.labelHidden[item/k-i]) {
          		ChartPane.this.data.labelHidden[item/k]=true;
          		return;
          	}
          	if (item/k+i<ChartPane.this.data.yi.length && ChartPane.this.data.yi[item/k+i]>y2) {
          		ChartPane.this.data.labelHidden[item/k]=true;
          		return;
          	}
          }
          ChartPane.this.data.labelHidden[item/k]=false;
          
          //if (item-d/2>=0 && dataset.getYValue(series, (int)(item-d/2))>y) y1-=100;
          //if (item+d/2<dataset.getItemCount(series) && dataset.getYValue(series, (int)(item+d/2))>y) y1-=100;
          
        	//System.out.println("x:"+x+" y:"+y+" x1:"+x1+" y1:"+y1);
          drawItemLabel(g2, plot.getOrientation(), dataset, series, item, x1, y1-10, false);
          //LineAndShapeRenderer
        }
      }
      Color f0=new Color(100, 100, 100, 40), f=new Color(0, 0, 200, 20), l0=Color.black, l=new Color(0, 0, 200, 100); 
      public Paint getSeriesPaint(int series) {
      	if (series==0) return f0;
				else return f;
      }
      public Paint getSeriesOutlinePaint(int series) {
      	if (series==0) return l0;
				else return l;
      }
    };
    //xyplot = new XYPlot(dataset, xAxis, yAxis, renderer);
    xyplot = new XYPlot(dataset1, null, yAxis, renderer);

    //xyplot.setDataset(dataset);
    //xyplot.setBackgroundPaint(Color.black);
    
//    renderer.setPaint();
//    renderer.setSeriesOutlinePaint(0, Color.black);
//    renderer.setOutlinePaint(Color.blue);
    renderer.setOutline(true);
    renderer.setOutlineStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    //renderer.setSeriesPaint(0, Color.black);
    
    //renderer.setShape(new Rectangle2D.Double(0, 0, 1, 1));
    
    
    //renderer.setSeriesPaint(1, Color.green);
    //renderer.setShapesVisible(false);
    //renderer.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    //xAxis.setAutoRange(true);
		//yAxis.setRange(-1000, 100000);

    //AxisSpace aa = new AxisSpace();
    //aa.setLeft(200);
    //xyplot.setFixedRangeAxisSpace(aa);
		//xyplot.setAxisOffset(new RectangleInsets(2.0, 2.0, 2.0, 2.0));		
		
		
//    XYTextAnnotation annotation = new XYTextAnnotation("Hello!", 50.0, 10000.0);
//    annotation.setFont(new Font("SansSerif", Font.PLAIN, 9));
//    annotation.setRotationAngle(Math.PI / 4.0);
//    xyplot.addAnnotation(annotation);

		
		

		
		
		
		
    
    // create subplot 2...
    XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(false, true) {
      protected void drawSecondaryPass(java.awt.Graphics2D g2, org.jfree.chart.plot.XYPlot plot, org.jfree.data.xy.XYDataset dataset, int pass, int series, int item, org.jfree.chart.axis.ValueAxis domainAxis, 
          java.awt.geom.Rectangle2D dataArea, org.jfree.chart.axis.ValueAxis rangeAxis, org.jfree.chart.plot.CrosshairState crosshairState, org.jfree.chart.entity.EntityCollection entities) {
        java.awt.Shape entityArea = null;
        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);
        if(java.lang.Double.isNaN(y1) || java.lang.Double.isNaN(x1))
          return;
        org.jfree.ui.RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        org.jfree.ui.RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);
        if(getItemShapeVisible(series, item)) {
          java.awt.Shape shape = getItemShape(series, item);
          org.jfree.chart.plot.PlotOrientation orientation = plot.getOrientation();
          if(orientation == org.jfree.chart.plot.PlotOrientation.HORIZONTAL)
            shape = org.jfree.util.ShapeUtilities.createTranslatedShape(shape, transY1, transX1);
          else
          if(orientation == org.jfree.chart.plot.PlotOrientation.VERTICAL)
            shape = org.jfree.util.ShapeUtilities.createTranslatedShape(shape, transX1, transY1);
          entityArea = shape;
          if(shape.intersects(dataArea)) {
        		Rectangle r = shape.getBounds();
        		g2.setStroke(getItemOutlineStroke(series, item));
            g2.setPaint(getItemPaint(series, item));
          	g2.draw(new Line2D.Double(r.x+3, r.y, r.x+3, r.y+r.height));
          }
        }
        updateCrosshairValues(crosshairState, x1, y1, transX1, transY1, plot.getOrientation());
        if(entities != null)
          addEntity(entities, entityArea, dataset, series, item, transX1, transY1);
      }
    };
    
    
    renderer2.setBaseToolTipGenerator(dataset2);
    renderer2.setPaint(Color.black);
    renderer2.setShape(new Rectangle(-3, -3, 4, 6));
    renderer2.setSeriesVisibleInLegend(Boolean.FALSE);
    NumberAxis rangeAxis2 = new NumberAxis();
    rangeAxis2.setRange(-1, 1);
    rangeAxis2.setVisible(false);
    XYPlot subplot2 = new XYPlot(dataset2, null, rangeAxis2, renderer2);
    subplot2.setDomainGridlinesVisible(false);
    subplot2.setRangeGridlinesVisible(false);

    // parent plot...
    CombinedDomainXYPlot combinedPlot = new CombinedDomainXYPlot(xAxis);
    combinedPlot.setGap(3);
    
    // add the subplots...
    combinedPlot.add(xyplot, 50);
    combinedPlot.add(subplot2, 1);
    combinedPlot.setOrientation(PlotOrientation.VERTICAL);
		
		
		xAxis.setLowerMargin(0.0);
    xAxis.setUpperMargin(0.0);
    yAxis.setUpperMargin(.1);
    xAxis.setTickLabelsVisible(true);
    yAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
    jfreechart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, combinedPlot, true);
    renderer.setSeriesVisibleInLegend(new Boolean(false));
    
    chartPanel = new ChartPanel(jfreechart) {
    	public void paint(final Graphics g) {
    		if (ChartPane.this.data.needRecalculate==true) {
    			ChartPane.this.data.recalculateDone=false;
    			ChartPane.this.data.needRecalculate=false;
    			
    			new Thread() {
    				public void run() {
    					ChartPane.this.data.recalculate();
    					//ChartPane.this.dataset1.fireDatasetChanged();
    					//fireDatasetChanged();
    					try {
								ChartPane.this.dataset1.validateObject();
								ChartPane.this.dataset2.validateObject();
							} catch (InvalidObjectException e) {
								throw new RuntimeException(e);
							}
    					
    					ChartPane.this.chartPanel.repaint();
        			//superpaint(g);
    				}
    			}.start();
    		}
    		else {
    			superpaint(g);
//    			if (!recalcDone) {
//    				g.setFont(new Font("Verdana", Font.BOLD, 20));
//    				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//      			g.setColor(new Color(255, 255, 255, 200));
//      	  	FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(g.getFont());
//      			String s = "Please wait...";
//      	  	int h = getHeight();
//      			int w = getWidth();
//      	  	int dh = metrics.getHeight();
//      	  	int dw = metrics.stringWidth(s);
//      	  	g.fillRect(w/2-dw/2-10, h/2-dh/2-10, dw+20, dh+20);
//      			g.setColor(new Color(0, 0, 0, 255));
//      			g.drawString(s, w/2-dw/2, h/2+dh/2);
//    			}
    		}
    	}
    	public void superpaint(Graphics g) {
    		super.paint(g);
    	}
    	public JToolTip createToolTip() {
    		return new MultiLineToolTip();
    	}
//      public String getToolTipText(MouseEvent e) {
//      	Point2D p = translateScreenToJava2D(e.getPoint());
//      	
//      	//combinedPlot.findSubplot(combinedPlot.getRenderer()., x);
//      	
//      	double x = xAxis.java2DToValue(p.getX(), getScreenDataArea(), xyplot.getDomainAxisEdge());
//      	return ""+x;
//      }
    	
    	public void zoom(Rectangle2D selection) {
    		super.zoom(selection);
    		if (autoScale!=null) autoScale.setSelected(false);
    	}
    	public void restoreAutoBounds() {
    		super.restoreAutoBounds();
    		if (autoScale!=null) autoScale.setSelected(true);
    	}
    };
    
    
    renderer.setItemLabelGenerator(data);
    renderer.setItemLabelFont(new Font(null, 0, 10));
    renderer.setItemLabelsVisible(false);
    //ItemLabelPosition p = renderer.getPositiveItemLabelPosition();
    renderer.setPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.CENTER_LEFT, TextAnchor.CENTER_LEFT, -Math.PI/2));
    
    
    //renderer.setToolTipGenerator(((XYToolTipGenerator) (new ToolTipGenerator())));
    //renderer.setBaseToolTipGenerator(((XYToolTipGenerator) (new ToolTipGenerator())));
    //renderer.setItemLabelGenerator(new LabelGenerator());
    //renderer.setItemLabelsVisible(true);
    
    //renderer.setDrawShapes(true);
    //renderer.setItemLabelsVisible(Boolean.TRUE);
    
    
    
//    NumberAxis handAxis = new NumberAxis("Hands played");
//    xyplot.setDomainAxis(1, handAxis);
//    xyplot.setDomainAxisLocation(0, AxisLocation.TOP_OR_RIGHT);      
//    xyplot.setDomainAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);      
    
    
    
    
//    xAxis = new PeriodAxis("Time");
//    yAxis = new NumberAxis("$");
//    xyplot = new XYPlot(null, xAxis, yAxis, new DefaultXYItemRenderer());
//    renderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
//    
//    yAxis.setRange(-2, 2);
//    
//    renderer.setLinesVisible(true);
//    renderer.setShapesVisible(false);
//    //
//    
//    jfreechart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
//    chartPanel = new ChartPanel(jfreechart) {
//      public JToolTip createToolTip() {
//        return new MultiLineToolTip();
//      }
//    };

    chartPanel.setPopupMenu(null);        
    chartPanel.setMouseZoomable(true);
    
    //new DataGenerator().start();
    
  }
}
