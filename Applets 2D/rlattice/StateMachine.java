import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;



public class StateMachine {
	State starting;
	State findingHstrip, stopHstrip, failedHstrip, foundHstrip;
	State findingVstrip, stopVstrip, failedVstrip, foundVstrip;
	State latticeCell;
	
	State currentState;
	
	ImgDisplay2 display;
	
	public StateMachine(final ImgDisplay2 display) {
		this.display = display;
		
		
		
		starting = new State(
				"<html>Here is the selected periodic pattern. You can scroll it anytime by dragging the mouse.<br><br>Please click anywhere on the pattern in order to find other points which are strictly identical.",
				null, null, null) {
			public void onEnter() {
	       display.newpts = 0;
	       if (display.quick) {
	       	display.cx = display.truecx-1;
	       } else {
	       	display.cx = 0;
	       }
			}
			public void onMouseClick(int x, int y) {
				display.ptx = x-display.mouseMotion.originX;
				display.pty = y-display.mouseMotion.originY;
				display.pxsz = new Dimension(display.offScreenSize);
       	display.panel.setupPanel("Please wait, getting image information...",null,null);
       	display.pixels = new int [display.pxsz.height*display.pxsz.width];
         PixelGrabber pg = new PixelGrabber(display.imgAll, 0, 0, display.pxsz.width, 
         		display.pxsz.height, display.pixels, 0, display.pxsz.width);
         try {
           pg.grabPixels();
         } catch (InterruptedException e) {
         }
         if ((pg.status() & ImageObserver.ABORT) != 0) {
         }
				doChangeState(findingHstrip);
			}
		};
		
		findingHstrip = new State(
				"Finding an identical point with the same environment...",
			null, "Stop", null) {
			public void onButtonClick(String buttonText) {
				if (buttonText.equals("Stop")) doChangeState(stopHstrip);
			}
			public void onEnterState() {
       	display.cx = 0;
			}
			public void paint(Graphics g) {
				super.paint(g);
				display.cx++;
        if (display.cx>1000) {
  				doChangeState(failedHstrip);
        }
        else {
        	boolean failed = false;
        	// check all lengths to the right
          for(int i=display.cx; i+display.ptx < display.pxsz.width && !failed; i++)
            if (display.pixels[display.pty*display.pxsz.width + i+display.ptx] != display.pixels[display.pty*display.pxsz.width + display.ptx+i%display.cx])
              failed = true;
          // check all lengths to the left.
          for(int i=0; i+display.ptx >= 0 && !failed; i--)
            if (display.pixels[display.pty*display.pxsz.width + i+display.ptx] != display.pixels[display.pty*display.pxsz.width + display.ptx+(i%display.cx)])
              failed = true;
          if (failed == false || display.cx == display.truecx) {
            System.out.println("strip len:  " + display.cx + " ptx: " + display.ptx + " w: " + display.pxsz.width);
    				doChangeState(foundHstrip);
          }
        }
        g.setColor(display.color);
       	g.fillRect(display.ptx+display.mouseMotion.originX,display.pty-1+display.mouseMotion.originY,display.cx,3); //TODO
        try {Thread.sleep(10);}catch(Exception e){}
        display.repaint();
			}
		};
		
		stopHstrip = new State(
				"Stopped at user request",
				"Go Back", "Continue", null) {
				public void onButtonClick(String buttonText) {
					if (buttonText.equals("Go Back")) doChangeState(starting);
					if (buttonText.equals("Continue")) doChangeState(findingHstrip);
				}
		};
		
		failedHstrip = new State(
				"Failed to find a repeating horizontal strip!",
				"Go Back", null, null) {
			public void onButtonClick(String buttonText) {
				if (buttonText.equals("Go Back")) doChangeState(starting);
			}
		};
		
		foundHstrip = new State(
				"Let us try to find an identical strip on the pattern.",
				"Go Back", "Continue", null) {
			public void onButtonClick(String buttonText) {
				if (buttonText.equals("Go Back")) doChangeState(starting);
				if (buttonText.equals("Continue")) doChangeState(findingVstrip);
			}
			public void paint(Graphics g) {
				super.paint(g);
        g.setColor(display.color);
       	g.fillRect(display.ptx+display.mouseMotion.originX,display.pty-1+display.mouseMotion.originY,display.cx,3); //TODO
        try {Thread.sleep(10);}catch(Exception e){}
       	display.repaint();
			}
		};

		findingVstrip = new State(
				"Finding this same strip somewhere below...",
			null, "Stop", null) {
			public void onButtonClick(String buttonText) {
				if (buttonText.equals("Stop")) doChangeState(stopVstrip);
			}
		};
	
		stopVstrip = new State(
				"Stopped at user request",
				"Go Back", "Continue", null){
			public void onButtonClick(String buttonText) {
				if (buttonText.equals("Go Back")) doChangeState(foundHstrip);
				if (buttonText.equals("Continue")) doChangeState(findingVstrip);
			}
		};
		
		failedVstrip = new State(
				"Failed to find the same strip below!",
				"Go Back", null, null) {
			public void onButtonClick(String buttonText) {
				if (buttonText.equals("Go Back")) doChangeState(foundHstrip);
			}
		};
		
		foundVstrip = new State(
				"The two strips relates two pairs of identical points which allows us to find a unit cell of the pattern.",
				"Go Back", "Continue", null) {
			public void onButtonClick(String buttonText) {
				if (buttonText.equals("Go Back")) doChangeState(foundHstrip);
				if (buttonText.equals("Continue")) doChangeState(latticeCell);
			}
		};
	
		latticeCell = new State(
			"This is a possible unit cell.",
			"Go Back", "Continue", null) {
			public void onButtonClick(String buttonText) {
				if (buttonText.equals("Go Back")) doChangeState(foundVstrip);
				if (buttonText.equals("Continue")) doChangeState(latticeCell);
			}
		};
	
	
		start();
	}
	
	public void start() {
		currentState = starting;
		currentState.onEnterState();
		display.panel.setupPanel(currentState.text, currentState.leftButton, currentState.rightButton);
		display.panel.showFinish(currentState.thirdButton!=null);
	}
	
	public void doChangeState(State newState) {
		currentState.onExitState();
		currentState = newState;
		currentState.onEnterState();
		display.panel.setupPanel(currentState.text, currentState.leftButton, currentState.rightButton);
		display.panel.showFinish(currentState.thirdButton!=null);
		display.invalidate();
		display.repaint();
	}
	

	
	
	
	
	class State {
		public String text;
		public String leftButton, rightButton, thirdButton;
		
		public State(String text, String leftButton, String rightButton, String thirdButton) {
			this.text=text;
			this.leftButton=leftButton;
			this.rightButton=rightButton;
			this.thirdButton=thirdButton;
		}
		public void onEnterState() {}
		public void onExitState() {}
		public void onMouseClick(int x, int y) {}
		public void onButtonClick(String buttonText) {}
		
		public void paint(Graphics g) {
			Dimension d = display.getSize();
			 if (display.drawPattern) {
			 	g.drawImage(display.imgAll, (display.mouseMotion.originX%d.width)-d.width,(display.mouseMotion.originY%d.height)-d.height,null);
			 }
			 else {
			   g.setColor(Color.white);
			   g.fillRect(0,0,d.width,d.height);
			 }
		}
	}
}
