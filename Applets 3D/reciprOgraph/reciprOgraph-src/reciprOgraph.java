import panes.MainApp;

/* ReciprOgraph - reciprOgraph.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 30 nov. 06
 * 
 * nicolas.schoeni@epfl.ch
 */


public class reciprOgraph {
	public static void main(String[] args) {
		MainApp.isApplet = false;
		MainApp mainApp = new MainApp();
		mainApp.init();
		mainApp.start();
	}
}
