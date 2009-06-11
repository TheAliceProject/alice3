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
package edu.cmu.cs.dennisc.ui.eula;

/**
 * @author Dennis Cosgrove
 */
public class EULAPane extends edu.cmu.cs.dennisc.swing.InputPane< Boolean > {
	private javax.swing.JCheckBox accept = new javax.swing.JCheckBox();
	private javax.swing.JCheckBox reject = new javax.swing.JCheckBox();

	public EULAPane( String text ) {
		super();
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );

		javax.swing.JTextArea headerTextArea = new javax.swing.JTextArea();
		headerTextArea.setText( "Please read the following license agreement carefully." );
		headerTextArea.setEditable( false );
		headerTextArea.setLineWrap( true );
		headerTextArea.setWrapStyleWord( true );
		headerTextArea.setOpaque( false );

		javax.swing.JTextArea textArea = new javax.swing.JTextArea();
		textArea.setText( text );
		textArea.setEditable( false );
		textArea.setLineWrap( true );
		textArea.setWrapStyleWord( true );
		this.accept.setText( "I accept the terms in the License Agreement" );
		this.reject.setText( "I do not accept the terms in the License Agreement" );

		javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
		this.accept.setSelected( false );
		this.reject.setSelected( true );
		group.add( this.accept );
		group.add( this.reject );

		this.addOKButtonValidator( new edu.cmu.cs.dennisc.pattern.Validator() {
			public boolean isValid() {
				return EULAPane.this.accept.isSelected();
			}
		} );

		final javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( textArea );
		scrollPane.setPreferredSize( new java.awt.Dimension( 480, 320 ) );

		headerTextArea.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		scrollPane.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		this.accept.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		this.reject.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );

		javax.swing.event.ChangeListener changeAdapter = new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				EULAPane.this.updateOKButton();
			}
		};
		this.accept.addChangeListener( changeAdapter );
		this.reject.addChangeListener( changeAdapter );

		javax.swing.BoxLayout boxLayout = new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS );
		this.setLayout( boxLayout );
		this.add( headerTextArea );
		this.add( scrollPane );
		this.add( this.accept );
		this.add( this.reject );

		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				scrollPane.getVerticalScrollBar().setValue( 0 );
			}
		} );
	}
	@Override
	protected Boolean getActualInputValue() {
		return accept.isSelected();
	}
}
