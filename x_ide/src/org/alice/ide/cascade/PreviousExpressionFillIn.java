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
package org.alice.ide.cascade;

/**
 * @author Dennis Cosgrove
 */
public class PreviousExpressionFillIn< E extends edu.cmu.cs.dennisc.alice.ast.Expression > extends SimpleExpressionFillIn< E > {
	public PreviousExpressionFillIn( E model ) {
		super( model );
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		zoot.ZLabel label = new zoot.ZLabel( "(current value)" );
		label.setFontToDerivedFont( zoot.font.ZTextPosture.OBLIQUE, zoot.font.ZTextWeight.LIGHT );
//		label.setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
//		label.setVerticalAlignment( javax.swing.SwingConstants.CENTER );
//		label.setAlignmentX( java.awt.Component.CENTER_ALIGNMENT );
//		label.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );

		javax.swing.JComponent s = super.createMenuProxy();
		s.doLayout();
		
		javax.swing.JPanel rv = new javax.swing.JPanel();
		
		rv.setOpaque( false );
		//rv.setBackground( java.awt.Color.RED );
		
//		rv.setLayout( new java.awt.BorderLayout() );
//		rv.add( s, java.awt.BorderLayout.CENTER );
//		rv.add( label, java.awt.BorderLayout.EAST );

		rv.setLayout( new javax.swing.BoxLayout( rv, javax.swing.BoxLayout.LINE_AXIS ) );
		rv.add( s );
		rv.add( javax.swing.Box.createHorizontalStrut( 16 ) );
		rv.add( label );
		edu.cmu.cs.dennisc.swing.SwingUtilities.doLayout( rv );
		//rv.doLayout();
		return rv;
	}
//	@Override
//	public E getValue() {
//		return (E)this.getIDE().createCopy( super.getValue() );
//	}
}
