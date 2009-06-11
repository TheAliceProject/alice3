/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.swing.transfer;

/**
 * @author Dennis Cosgrove
 */
public abstract class FileTransferHandler extends javax.swing.TransferHandler {
	protected abstract void handleFiles( java.util.List<java.io.File> files );
	@Override
	public boolean canImport( javax.swing.JComponent component, java.awt.datatransfer.DataFlavor[] dataFlavors ) {
		for( java.awt.datatransfer.DataFlavor dataFlavor : dataFlavors ) {
			if( dataFlavor.equals( java.awt.datatransfer.DataFlavor.javaFileListFlavor ) ) {
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean importData( javax.swing.JComponent comp, java.awt.datatransfer.Transferable t ) {
		for( java.awt.datatransfer.DataFlavor dataFlavor : t.getTransferDataFlavors() ) {
			if( dataFlavor.equals( java.awt.datatransfer.DataFlavor.javaFileListFlavor ) ) {
				try {
					final Object data = t.getTransferData( java.awt.datatransfer.DataFlavor.javaFileListFlavor );
					if( data instanceof java.util.List ) {
						new Thread() {
							@Override
							public void run() {
								handleFiles( (java.util.List<java.io.File>)data );
							}
						}.start();
					}
					return true;
				} catch( java.io.IOException ioe ) {
					throw new RuntimeException( ioe );
				} catch( java.awt.datatransfer.UnsupportedFlavorException ufe ) {
					throw new RuntimeException( ufe );
				}
			} else {
				//todo?
			}
		}
		return false;
	}
}
