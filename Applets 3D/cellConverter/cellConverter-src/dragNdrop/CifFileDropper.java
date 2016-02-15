/*
 * ReciprOgraph - CifFileDropper.java
 * Author : Nicolas Schoeni
 * Creation : 24 oct. 2005
 * nicolas.schoeni@epfl.ch
 */
package dragNdrop;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CifFileDropper implements DropTargetListener {
	private CifFileOpener opener;

	public CifFileDropper(CifFileOpener opener) {
		this.opener = opener;
	}

	public void drop(DropTargetDropEvent e) {
		try {
			Transferable t = e.getTransferable();
			DataFlavor[] flavors = t.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++)
				if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
					e.acceptDrop(DnDConstants.ACTION_COPY);
					if (t.getTransferData(DataFlavor.javaFileListFlavor) instanceof List) {
						List fileList = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
						for (int j = 0; j < fileList.size(); j++)
							if (fileList.get(j) instanceof File) {
								File f = (File) fileList.get(j);
								this.opener.openFile(f);
							}
					}
				}
		} catch (UnsupportedFlavorException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void dragEnter(DropTargetDragEvent e) {
	}

	public void dragExit(DropTargetEvent e) {
	}

	public void dragOver(DropTargetDragEvent e) {
	}

	public void dropActionChanged(DropTargetDragEvent e) {
	}
}
