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

package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public abstract class FileSelectionPane extends InputPane< java.io.File > {
	private javax.swing.JLabel feedback = new javax.swing.JLabel();
	private javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
	public FileSelectionPane() {
		this.fileChooser.setFileSelectionMode( this.getDesiredFileSelectionMode() );
		this.fileChooser.setControlButtonsAreShown( false );
		this.fileChooser.addPropertyChangeListener( new java.beans.PropertyChangeListener() {
			public void propertyChange( java.beans.PropertyChangeEvent e ) {
				String propertyName = e.getPropertyName();
				if( javax.swing.JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals( propertyName ) ) {
					FileSelectionPane.this.updateOKButton();
				//} else {
				//	edu.cmu.cs.dennisc.print.PrintUtilities.println( propertyName );
				}
			}
		} );
		this.updateOKButton();
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.fileChooser, java.awt.BorderLayout.CENTER );
		this.add( this.feedback, java.awt.BorderLayout.SOUTH );
	}
	protected javax.swing.JFileChooser getFileChooser() {
		return this.fileChooser;
	}
	protected abstract String getFeedbackText();
	@Override
	public void updateOKButton() {
		super.updateOKButton();
		this.feedback.setText( this.getFeedbackText() );
		java.awt.Color color;
		if( isOKButtonValid() ) {
			color = java.awt.Color.BLACK;
		} else {
			color = java.awt.Color.RED.darker();
		}
		this.feedback.setForeground( color );
	}
	protected int getDesiredFileSelectionMode() {
		return javax.swing.JFileChooser.FILES_ONLY;
	}
	@Override
	protected java.io.File getActualInputValue() {
		return this.fileChooser.getSelectedFile();
	}
}
