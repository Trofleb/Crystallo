/* reciprOgraph - CifFileFilter.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 19 août 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package structures;

import java.io.File;


public  class CifFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
    String extension = getExtension(f);
    if (extension==null) return false;
    if (extension.toLowerCase().equals("cif")) return true;
    return false;
	}

	public static class NoDirectories extends CifFileFilter {
		public boolean accept(File f) {
			if (f.isDirectory()) return false;
			else return super.accept(f);
		}
	}
	
	public String getDescription() {
		return "CIF file";
	}
	public String getExtension(File f) {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 &&  i < s.length() - 1) {
        ext = s.substring(i+1).toLowerCase();
    }
    return ext;
	}	
}
