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
package org.alice.ide.ubiquitouspane.templates;

///**
// * @author Dennis Cosgrove
// */
//class UbiquitousStatementToolTip extends javax.swing.JToolTip {
//	private java.awt.Component emptyStatementPane;
//	private java.awt.image.BufferedImage image;
//	private javax.swing.Icon icon;
//
//	UbiquitousStatementToolTip( java.awt.Component emptyStatementPane ) {
//		this.emptyStatementPane = emptyStatementPane;
////		this.setOpaque( false );
////		this.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
//	}
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		return this.emptyStatementPane.getPreferredSize();
//	}
//	@Override
//	protected void paintComponent( java.awt.Graphics g ) {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "UbiquitousStatementToolTip", g );
//		if( this.icon != null ) {
//			//pass
//		} else {
//			edu.cmu.cs.dennisc.swing.SwingUtilities.doLayout( this.emptyStatementPane );
//			this.icon = edu.cmu.cs.dennisc.swing.SwingUtilities.createIcon( this.emptyStatementPane, org.alice.ide.IDE.getSingleton().getContentPane() );
//		}
//		java.awt.Rectangle bounds = g.getClipBounds();
//		g.clearRect( bounds.x, bounds.y, bounds.width, bounds.height );
//		this.icon.paintIcon( this, g, 0, 0 );
////		if( image != null && image.getWidth() == this.getWidth() && image.getHeight() == this.getHeight() ) {
////			//pass
////		} else {
////			image = new java.awt.image.BufferedImage( getWidth(), getHeight(), java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
////		}
////
////		java.awt.Graphics gImage = image.getGraphics();
////		gImage.setColor( this.emptyStatementPane.getBackground() );
////		this.emptyStatementPane.print( gImage );
////		//this.emptyStatementPane.paint( gImage );
////		gImage.dispose();
////		g.drawImage( image, 0, 0, this );
//	}
//}
//
/**
 * @author Dennis Cosgrove
 */
public abstract class UbiquitousStatementTemplate extends org.alice.ide.templates.StatementTemplate {
	private zoot.ZLabel label = new zoot.ZLabel();
	private edu.cmu.cs.dennisc.alice.ast.Statement incompleteStatement;
	private java.awt.Component incompleteStatementPane;
	protected edu.cmu.cs.dennisc.alice.ast.Statement getIncompleteStatement() {
		return this.incompleteStatement;
	}
	protected java.awt.Component getIncompleteStatementPane() {
		return this.incompleteStatementPane;
	}
//	private UbiquitousStatementToolTip toolTip;
	private edu.cmu.cs.dennisc.swing.IconToolTip iconToolTip;
	public UbiquitousStatementTemplate( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls, edu.cmu.cs.dennisc.alice.ast.Statement incompleteStatement ) {
		super( cls );
		this.incompleteStatement = incompleteStatement;
		String text = edu.cmu.cs.dennisc.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "org.alice.ide.ubiquitouspane.Templates" );
		//text = "label: " + text;
		this.label.setText( text );
		this.add( this.label );
		this.setToolTipText( "" );
	}
	@Override
	public javax.swing.JToolTip createToolTip() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "createToolTip" );
//		if( this.toolTip != null ) {
//			//pass
//		} else {
//			this.toolTip = new UbiquitousStatementToolTip( this.getEmptyStatementPane() );
//		}
//		return this.toolTip;
//		return new UbiquitousStatementToolTip( this.getEmptyStatementPane() );
		if( this.iconToolTip != null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.swing.SwingUtilities.doLayout( this.incompleteStatementPane );
//			((javax.swing.JComponent)this.getEmptyStatementPane()).revalidate();
			javax.swing.Icon icon = edu.cmu.cs.dennisc.swing.SwingUtilities.createIcon( this.incompleteStatementPane, this );
			this.iconToolTip = new edu.cmu.cs.dennisc.swing.IconToolTip( icon );
		}
		return this.iconToolTip;
	}
	@Override
	public void addNotify() {
		if( this.incompleteStatementPane != null ) {
			//pass
		} else {
			this.incompleteStatementPane = getIDE().getTemplatesFactory().createStatementPane( this.incompleteStatement );
		}
		super.addNotify();
	}
	
//	@Override
//	public java.awt.Dimension getMinimumSize() {
//		java.awt.Dimension rv = super.getMinimumSize();
//		rv.width = 24;
//		return rv;
//	}
//		@Override
//	public java.awt.Dimension getPreferredSize() {
//		java.awt.Dimension rv = this.label.getPreferredSize();
//		javax.swing.border.Border border = this.getBorder();
//		java.awt.Insets insets = border.getBorderInsets( this );
//		rv.width += insets.left;
//		rv.width += insets.right;
//		rv.height += insets.top;
//		rv.height += insets.bottom;
//		return rv;
//	}
//	@Override
//	protected void paintChildren( java.awt.Graphics g ) {
//		javax.swing.border.Border border = this.getBorder();
//		java.awt.Insets insets = border.getBorderInsets( this );
//		g.translate( insets.left, insets.top );
//		this.label.paint( g );
//		g.translate( -insets.left, -insets.top );
//	}
	
}
