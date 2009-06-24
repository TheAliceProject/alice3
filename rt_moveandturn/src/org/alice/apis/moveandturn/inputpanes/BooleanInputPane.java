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

package org.alice.apis.moveandturn.inputpanes;
/**
 * @author Dennis Cosgrove
 */
public class BooleanInputPane extends InputPane< Boolean > {
	private javax.swing.JRadioButton trueButton = new javax.swing.JRadioButton( "true" );
	private javax.swing.JRadioButton falseButton = new javax.swing.JRadioButton( "false" );
	public BooleanInputPane( String message ) {
		super( message );
		
		class ItemAdapter implements java.awt.event.ItemListener {
			public void itemStateChanged( java.awt.event.ItemEvent e ) {
				BooleanInputPane.this.updateOKButton();
			}
		}
		ItemAdapter itemAdapter = new ItemAdapter();
		this.trueButton.addItemListener( itemAdapter );
		this.falseButton.addItemListener( itemAdapter );
		
		javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
		group.add( trueButton );
		group.add( falseButton );
		
		javax.swing.Box box = javax.swing.Box.createVerticalBox();
		java.util.Enumeration< javax.swing.AbstractButton > enm = group.getElements();
		while( enm.hasMoreElements() ) {
			box.add( enm.nextElement() );
		}
		this.add( box, java.awt.BorderLayout.CENTER );
	}
	@Override
	public boolean isOKButtonValid() {
		return super.isOKButtonValid() && ( this.trueButton.isSelected() || this.falseButton.isSelected() );
	}
	@Override
	protected Boolean getActualInputValue() {
		return this.trueButton.isSelected();
	}
}
