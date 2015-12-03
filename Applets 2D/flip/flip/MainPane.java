import java.awt.Dimension;
import java.awt.Insets;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.text.NumberFormatter;

import dragNdrop.CifFileOpener;


/* Charge Flip - MainPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 7 déc. 06
 * 
 * nicolas.schoeni@epfl.ch
 */

public class MainPane extends HVPanel.v implements CifFileOpener {
	public static final int IMAGE_SIZE = 256;

	private static final int DEFAULT_WEAK_LIMIT = 0;
	private static final int DEFAULT_DELTA_LIMIT = 10;
	private static final boolean DEFAULT_DELTA_DYNAMIC = true;
	private static final boolean DEFAULT_MASK_ENABLE = true;
	private static final int DEFAULT_MASK_RADIUS = IMAGE_SIZE;
	
	public JLabel posLabel;
	private FloatEditField deltaEdit;
	private IntSpinnerEditField weakEdit;
	private OutputSystem outputSystem;
	private OutputCanvas outputCanvas;
	private DrawSystem drawSystem;
	private JButton doButton, cycleButton, resetButton;
	private JToggleButton loopButton;
	private JButton helpButton;
	//private LongEditField rndSeedEdit;
	//private JToggleButton lockRndSeedButton;
	private FitnessChart fitnessChart;
	private int iter=0;
	private double delta = 0;
	private boolean dynamicDelta = DEFAULT_DELTA_DYNAMIC;
	private int weakLimit=DEFAULT_WEAK_LIMIT;
	private JCheckBox dynDeltaCheckBox;
	private boolean lockRandomSeed;
	private double f0init;
	private JLabel outputImageLabel;
	private boolean mask=DEFAULT_MASK_ENABLE;
	private int maskRadius=DEFAULT_MASK_RADIUS;
	private SliderAndValueH radiusSlider;
	private JCheckBox radiusButton;
	private HideMaskThread hideMaskThread;
	private boolean quickModeNoMask;
	private int deltaLimit = DEFAULT_DELTA_LIMIT;
	private IntSpinnerEditField deltaLimitEdit;
	private double weakValue=0;
	private JButton defValuesButton;
	private int state=0;
	private String[] buttonLabels = {"FFT", "Set random phases", "FFT-1", "Flip charges", "FFT", "Restore amplitudes"};
	private double[][] amplitudeBackup;
	private LoopThread thread;
	private boolean dontTouchDeltaDynBox;
	private StatePanel statePanel;
	private Help help;
	private ChargeFlip applet;
	
	public MainPane(ChargeFlip applet) throws IOException {
		this.applet = applet;
		posLabel = new JLabel(" ");
		posLabel.setPreferredSize(new Dimension(250, 25));
		//iterLabel = new JLabel("Iteration: 0");
		//iterLabel.setPreferredSize(new Dimension(50, 25));
		
		drawSystem = new DrawSystem(IMAGE_SIZE, this);
		drawSystem.addCanvas("image");
		
		outputSystem = new OutputSystem(IMAGE_SIZE, this);
		outputSystem.addCanvas("image");
		outputCanvas = (OutputCanvas) outputSystem.outputCanvas.get(0);
		
//		InputStream in = getClass().getResourceAsStream("/test7.png");
//		startImage = ImageIO.read(in);
//		outputCanvas.loadImage(startImage);
		
		doButton = new JButton(buttonLabels[state]);
		doButton.setPreferredSize(new Dimension(180, 26));
		cycleButton = new JButton("Do one cycle");
		loopButton = new JToggleButton("Loop");
		resetButton = new JButton("Reset");
		
		HVPanel.v pv = new HVPanel.v();
		pv.expand(false);
		pv.putExtraSpace();
		//pv.expand(true);
		HVPanel.h ph1 = new HVPanel.h();
		ph1.expand(false);

		HVPanel.v pv10 = new HVPanel.v();
		pv10.expand(true);
		pv10.putExtraSpace();
		pv10.expand(false);
		pv10.addComp(statePanel = new StatePanel());
		pv10.expand(true);
		pv10.putExtraSpace();
		pv10.expand(false);
		HVPanel.h ph101 = new HVPanel.h();
		ph101.expand(true);
		ph101.putExtraSpace();
		ph101.expand(false);
		ph101.addButton(helpButton = new JButton("Help"));
		pv10.addSubPane(ph101);
		
		HVPanel.v pv11 = new HVPanel.v();
		pv11.expand(false);
		pv11.addComp(new JLabel("Original structure"));
		//pv11.expand(true);
		pv11.addComp(drawSystem.currentCanvas);
		HVPanel.v pv12 = new HVPanel.v();
		pv12.expand(false);
		pv12.addComp(outputImageLabel=new JLabel("Reconstructed structure"));
		//pv12.expand(true);
		pv12.addComp(outputSystem.currentCanvas);
		ph1.addSubPane(pv11);
		ph1.addSubPane(pv10);
		ph1.addSubPane(pv12);
		pv.addSubPane(ph1);
		HVPanel.h ph2 = new HVPanel.h();
		fitnessChart = new FitnessChart();
		fitnessChart.setPreferredSize(new Dimension(800, 150));
		pv.expand(false);
		pv.putExtraSpace();
		pv.expand(true);
		ph2.addComp(fitnessChart);
		pv.addSubPane(ph2);

		pv.expand(false);
		pv.putExtraSpace();
		
		HVPanel.h ph = new HVPanel.h();
		ph.expand(false);
		ph.addSubPane(drawSystem.tools);
		
		HVPanel.v pv2 = new HVPanel.v("Run");
		pv2.expand(false);
		pv2.addButton(doButton);
		pv2.addButton(cycleButton);
		pv2.addButton(loopButton);
		pv2.addButton(resetButton);
		ph.addSubPane(pv2);

		HVPanel.v pv3 = new HVPanel.v("Parameters");
		//pv3.putExtraSpace();
		pv3.expand(false);

//		HVPanel.h ph31 = new HVPanel.h();
//		ph31.expand(false);
//		ph31.left();
//		ph31.addButton(shakeButton=new JButton("Shake"));
//		pv3.addSubPane(ph31);
		
		//pv3.putExtraSpace();

//		HVPanel.h ph32 = new HVPanel.h();
//		ph32.expand(true);
//		ph32.left();
//
//		NumberFormat f = NumberFormat.getIntegerInstance();
//		f.setGroupingUsed(false);
//		rndSeedEdit = new HVPanel.LongEditField("Random seed ", "", 10, 0, new NumberFormatter(f)).to(ph32);
//		lockRndSeedButton=new JToggleButton("Lock");
//		lockRndSeedButton.setMargin(new Insets(0,0,0,0));
//		ph32.addButton(lockRndSeedButton);
//		
//		pv3.addSubPane(ph32);


		HVPanel.h ph5 = new HVPanel.h();
		ph5.expand(false);
		ph5.left();
		radiusButton = new JCheckBox("Mask Radius");
		radiusButton.setSelected(mask);
		ph5.addButton(radiusButton);
		ph5.expand(true);
		radiusSlider = new HVPanel.SliderAndValueH(null, null, 4, IMAGE_SIZE, maskRadius, 0, 100).to(ph5);
		radiusSlider.slider.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				outputSystem.showMask(maskRadius);
			}
			public void mouseReleased(MouseEvent e) {
				if (state!=2) outputSystem.hideMask();
			}
		});
		pv3.addSubPane(ph5);
		
		HVPanel.h ph3 = new HVPanel.h();
		ph3.expand(false);
		ph3.left();
		HVPanel.v pv31 = new HVPanel.v();
		pv31.expand(false);
		deltaEdit = new HVPanel.FloatSpinnerEditField("Delta ", "", 4, (float)delta, "0.0000", 0.02).to(pv31);
		deltaEdit.setMinimum(0);
		ph3.addSubPane(pv31);
		ph3.putExtraSpace();
		dynDeltaCheckBox = new JCheckBox("Dynamic");
		dynDeltaCheckBox.setSelected(dynamicDelta);
		ph3.addButton(dynDeltaCheckBox);
		HVPanel.v pv32 = new HVPanel.v();
		pv32.expand(false);
		deltaLimitEdit = (IntSpinnerEditField)new HVPanel.IntSpinnerEditField(null, " %", 2, deltaLimit, 5).to(pv32);
		deltaLimitEdit.setMinimum(0);
		deltaLimitEdit.setMaximum(100);
		ph3.addSubPane(pv32);
		HVPanel.h ph4 = new HVPanel.h();
		ph4.expand(false);
		ph4.left();
		HVPanel.v pv41 = new HVPanel.v();
		pv41.expand(false);
		weakEdit = (IntSpinnerEditField)new HVPanel.IntSpinnerEditField("Weak reflections", " %", 2, weakLimit, 5).to(pv41);
		weakEdit.setMinimum(0);
		weakEdit.setMaximum(100);
		ph4.addSubPane(pv41);
		ph4.expand(true);
		ph4.putExtraSpace();
		ph4.expand(false);
		ph4.addButton(defValuesButton=new JButton("Default"));
		pv3.addSubPane(ph3);
		pv3.addSubPane(ph4);
		pv3.addComp(posLabel);
		//pv3.addComp(iterLabel);
		ph.addSubPane(pv3);
		
		ph.addSubPane(outputSystem.tools);
		pv.addSubPane(ph);
		
		addSubPane(pv);
		
		long seed = System.currentTimeMillis();
		//rndSeedEdit.setValue(seed);
		outputCanvas.complexImage.random.setSeed(seed);
	}
	
	
	public void openFile(File f) {
		try {
			BufferedImage image = ImageIO.read(f);
			drawSystem.currentCanvas.loadImage(image);
		} catch (IOException e) {
  		e.printStackTrace();
  		JOptionPane.showMessageDialog(applet.frame, "Can't read file or bad format !");
		}
	}
	
	private String toPercentage(double d) {
		return (int)Math.round(d*100)+"%";
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==deltaEdit) {
			delta = deltaEdit.getFloatValue();
			if (!dontTouchDeltaDynBox) {
				dynamicDelta = false;
				dynDeltaCheckBox.setSelected(false);
			}
		}
		else if (e.getSource()==dynDeltaCheckBox) {
			dynamicDelta = dynDeltaCheckBox.isSelected();
		}
		else if (e.getSource()==deltaLimitEdit) {
			deltaLimit = deltaLimitEdit.getIntValue();
			dynamicDelta = true;
			dynDeltaCheckBox.setSelected(true);
		}
		else if (e.getSource()==radiusButton) {
			mask = radiusButton.isSelected();
		}
		else if (e.getSource()==radiusSlider) {
			maskRadius = (int)Math.round(radiusSlider.getValue());
			mask = true;
			radiusButton.setSelected(true);
			outputSystem.showMask(maskRadius);
			scheduleHideMaskTask();
		}
		else if (e.getSource()==weakEdit) {
			weakLimit = weakEdit.getIntValue();
			if (amplitudeBackup!=null)
				weakValue = outputCanvas.complexImage.calculateWeakValue(amplitudeBackup, weakLimit);
		}
		else if (e.getSource()==defValuesButton) {
			mask=DEFAULT_MASK_ENABLE;
			maskRadius=DEFAULT_MASK_RADIUS;
			radiusButton.setSelected(mask);
			radiusSlider.setValue(maskRadius);
			dynamicDelta = DEFAULT_DELTA_DYNAMIC;
			dynDeltaCheckBox.setSelected(dynamicDelta);
			deltaLimit = DEFAULT_DELTA_LIMIT;
			deltaLimitEdit.setValue(deltaLimit);
			delta = 0;
			dontTouchDeltaDynBox = true;
			deltaEdit.setValue((float)delta);
			dontTouchDeltaDynBox = false;
			weakLimit = DEFAULT_WEAK_LIMIT; 
			weakEdit.setValue(weakLimit);
			if (amplitudeBackup!=null)
				weakValue = outputCanvas.complexImage.calculateWeakValue(amplitudeBackup, weakLimit);
		}
		else if (e.getSource()==doButton) {
			runOneStep();
			updateOutput();
			doButton.setText(buttonLabels[state]);
		}
		else if (e.getSource()==cycleButton) {
			if (mask&&!quickModeNoMask) outputSystem.hideMask();
			runOneCycle();
			updateOutput();
			doButton.setText(buttonLabels[state]);
		}
		else if (e.getSource()==resetButton) {
			if (loopButton.isSelected()) {
				thread.stop=true;
				doButton.setText(buttonLabels[state]);
				doButton.setEnabled(true);
				cycleButton.setEnabled(true);
				while (thread.running) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {}
				}
			}
			loopButton.setText("Loop");
			loopButton.setSelected(false);
			outputCanvas.clear();
			state = 0;
			doButton.setText(buttonLabels[state]);
			iter=0;
			//iterLabel.setText("Iteration: "+iter);
			fitnessChart.reset();
			amplitudeBackup=null;
			statePanel.setActiveState(0);
			if (mask&&!quickModeNoMask) outputSystem.hideMask();
			
//			if (lockRandomSeed) {
//				outputCanvas.complexImage.random.setSeed(rndSeedEdit.getLongValue());
//			}
//			else {
				long seed = System.currentTimeMillis();
//				rndSeedEdit.setValue(seed);
				outputCanvas.complexImage.random.setSeed(seed);
//			}
			
		}
		else if (e.getSource()==loopButton) {
			if (loopButton.isSelected()) {
				if (mask&&!quickModeNoMask) outputSystem.hideMask();
				doButton.setEnabled(false);
				cycleButton.setEnabled(false);
				(thread = new LoopThread()).start();
				loopButton.setText("Stop");
			}
			else {
				thread.stop=true;
				while (thread.running) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {}
				}
				doButton.setText(buttonLabels[state]);
				doButton.setEnabled(true);
				cycleButton.setEnabled(true);
				loopButton.setText("Continue");
				statePanel.setActiveState(state);
			}
		}
		else if (e.getSource()==helpButton) {
			if (help==null) help = new Help(applet);
			help.show(true);
		}
	}
	
	
	private void scheduleHideMaskTask() {
		if (hideMaskThread==null || !hideMaskThread.running) {
			hideMaskThread = new HideMaskThread();
			hideMaskThread.running = true;
			hideMaskThread.start();
		}
		else {
			hideMaskThread.resetTimer();
		}
	}
	
	private class HideMaskThread extends Thread {
		private int delay;
		private boolean stop;
		public boolean running;
		
		public HideMaskThread() {
			resetTimer();
		}
		
		public void resetTimer() {
			delay = 1000;
		}
		public void quit() {
			stop = true;
		}
		
		public void run() {
			running = true;
			while(delay!=0&&!stop) {
				if (!radiusSlider.slider.getValueIsAdjusting()) delay-=100;
				try {
					sleep(100);
				} catch (InterruptedException e) {}
			}
			if (state!=2) outputSystem.hideMask();
			running = false;
		}
	}
	
	private class LoopThread extends Thread {
		public boolean stop;
		public boolean running;
		public void run() {
			running = true;
			while(!stop) {
				runOneCycle();
				updateOutput();
				try {
					sleep(10);
				} catch (InterruptedException e) {}
			}
			running = false;
		}
	}
	
	public void runOneStep() {
		switch (state) {
			case 0://fft
				statePanel.setActiveState(1);
				outputCanvas.complexImage.load(drawSystem.currentCanvas.getImage());
				outputCanvas.complexImage.doFFT();
				state++;
				break;
			case 1:// set random phases
				statePanel.setActiveState(2);
				outputCanvas.complexImage.setRandomPhases();
				amplitudeBackup = outputCanvas.complexImage.backupAmplitudes();
				weakValue = outputCanvas.complexImage.calculateWeakValue(amplitudeBackup, weakLimit);
				if (mask&&!quickModeNoMask) outputSystem.showMask(maskRadius);
				state++;
				break;
			case 2://fft-1
				statePanel.setActiveState(thread!=null&&thread.running?-1:3);
				if (mask&&!quickModeNoMask) outputSystem.hideMask();
				outputCanvas.complexImage.doFFTBack();
				if (iter==0) {
					//deltaEdit.setEnable(true);
					//dadLabel.setText("Density: "+(int)outputCanvas.complexImage.density()+" Above delta: "+toPercentage(outputCanvas.complexImage.getAeraUponDelta(delta)));
//					delta = outputCanvas.complexImage.calculateDelta();
//					System.out.println(delta);
//					System.out.println(outputCanvas.complexImage.getAeraUponDelta(delta));
				}
				state++;
				break;
			case 3://flip
				statePanel.setActiveState(thread!=null&&thread.running?-1:4);
				//deltaEdit.setEnable(false);
				//System.out.println(dynamicDelta);
				if (dynamicDelta||iter==0) {
					delta = outputCanvas.complexImage.findDelta(deltaLimit);
					dontTouchDeltaDynBox = true;
					deltaEdit.setValue((float)delta);
					dontTouchDeltaDynBox = false;
				}
				//System.out.println(delta+" "+(dynamicDelta?"dynamic":""));
				outputCanvas.complexImage.flipCharge(delta);
				state++;
				break;
			case 4://fft
				statePanel.setActiveState(thread!=null&&thread.running?-1:5);
				outputCanvas.complexImage.doFFT();
//				if (shakePending) {
//					outputCanvas.complexImage.shake();
//					shakePending = false;
//				}
				state++;
				break;
			case 5://restore amplitudes
				statePanel.setActiveState(thread!=null&&thread.running?-1:2);
				//System.out.println(weakValue);
				double Rn = outputCanvas.complexImage.restoreAmplitudes(amplitudeBackup, weakValue, maskRadius);
				
				//TODO ?
				//outputCanvas.complexImage.checkPhases();
				//outputCanvas.complexImage.recenterPhases();
				
				if (iter==0) f0init = outputCanvas.complexImage.getF0();
				fitnessChart.addValue((float)Rn, (float)(outputCanvas.complexImage.getF0()/f0init));
				iter++;
				state=2; //go back to fft-1
				if (mask&&!quickModeNoMask) outputSystem.showMask(maskRadius);
				break;
		}
	}
	
	public void runOneCycle() {
		quickModeNoMask=true;
		do {
			runOneStep();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					doButton.setText(buttonLabels[state]);
				}
			});
		} while(iter==0||state!=3);
		quickModeNoMask=false;
	}
	
	public void updateOutput() {
		outputSystem.complexImageSystem.updateOutput(outputCanvas.complexImage);
		outputSystem.currentCanvas.updateImageBuffer();
		outputSystem.currentCanvas.repaint();
	}
	
	public void stop() {
		if (help!=null) help.show(false);
	}
	public void destroy() {
	}
}
