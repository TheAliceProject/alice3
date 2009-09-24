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
package org.alice.ide.preferencesinputpane;

/**
 * @author Dennis Cosgrove
 */
public class PreferencesInputPane extends zoot.ZInputPane<Void> {
	private javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private zoot.ZList<Object> list = new zoot.ZList<Object>();
	public PreferencesInputPane() {
		this.splitPane.setLeftComponent( new javax.swing.JScrollPane( this.list ) );
		this.splitPane.setRightComponent( new javax.swing.JButton( "a" ) );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.splitPane, java.awt.BorderLayout.CENTER );
		this.setPreferredSize( new java.awt.Dimension( 320, 240 ) );
	}
	@Override
	protected Void getActualInputValue() {
		return null;
	}
	public static void main(String[] args) {
		PreferencesInputPane inputPane = new PreferencesInputPane();
		inputPane.showInJDialog(null);
	}
}
