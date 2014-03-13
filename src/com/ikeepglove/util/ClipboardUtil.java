/**
 * 
 */
package com.ikeepglove.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author Ben
 *
 */
public class ClipboardUtil {
	// 1. ��ָ���ļ��а��л�ȡ�ı�����
	public  static String getClipboardText() throws Exception {
		return getClipboardText(null);
	}
	public  static String getClipboardText(Clipboard  clip) throws Exception {
		
		if(clip==null){
			clip=Toolkit.getDefaultToolkit().getSystemClipboard();
		}
		// ��ȡ���а��е�����
		Transferable clipT = clip.getContents(null);
		if (clipT != null) {
			// ��������Ƿ����ı�����
			if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor))
				return (String) clipT.getTransferData(DataFlavor.stringFlavor);
		}
		return null;
	}
	// 2. ����а�д�ı����
	public  static void setClipboardText(String writeMe) {
		setClipboardText(writeMe,null);
	}
	public  static void setClipboardText(String writeMe,Clipboard clip) {
		
		if(clip==null){
			clip=Toolkit.getDefaultToolkit().getSystemClipboard();
		}
		Transferable tText = new StringSelection(writeMe);
		clip.setContents(tText, null);
	}
	// 3. �Ӽ��а��ȡͼ��
	public static Image getImageFromClipboard() throws Exception {
		Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable cc = sysc.getContents(null);
		if (cc == null)
			return null;
		else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor))
			return (Image) cc.getTransferData(DataFlavor.imageFlavor);
		return null;
	}

	// . дͼ�񵽼��а�

	public  static void setClipboardImage2(final Image image) {
		Transferable trans = new Transferable() {
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}

			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor))
					return image;
				throw new UnsupportedFlavorException(flavor);
			}

		};
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans,
				null);
	}


}
