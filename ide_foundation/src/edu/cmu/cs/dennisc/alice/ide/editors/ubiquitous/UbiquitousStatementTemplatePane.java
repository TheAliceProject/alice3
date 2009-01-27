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
package edu.cmu.cs.dennisc.alice.ide.editors.ubiquitous;

import edu.cmu.cs.dennisc.alice.ide.editors.code.AbstractStatementPane;

/**
 * @author Dennis Cosgrove
 */
class UbiquitousStatementToolTip extends javax.swing.JToolTip {
	private AbstractStatementPane emptyStatementPane;
	private java.awt.image.BufferedImage image;

	UbiquitousStatementToolTip( AbstractStatementPane emptyStatementPane ) {
		this.emptyStatementPane = emptyStatementPane;
		this.setOpaque( false );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return this.emptyStatementPane.getPreferredSize();
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		if( image != null && image.getWidth() == this.getWidth() && image.getHeight() == this.getHeight() ) {
			//pass
		} else {
			image = new java.awt.image.BufferedImage( getWidth(), getHeight(), java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
		}

		java.awt.Graphics gImage = image.getGraphics();
		this.emptyStatementPane.print( gImage );
		gImage.dispose();
		g.drawImage( image, 0, 0, this );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class UbiquitousStatementTemplatePane extends edu.cmu.cs.dennisc.alice.ide.editors.type.StatementTemplatePane {
	//private Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls;
	private edu.cmu.cs.dennisc.alice.ast.Statement emptyStatement;
	private AbstractStatementPane emptyStatementPane;
	private UbiquitousStatementToolTip toolTip;
	private javax.swing.JComponent component;

	public UbiquitousStatementTemplatePane( /*Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls,*/ edu.cmu.cs.dennisc.alice.ast.Statement emptyStatement ) {
		this.setLayout( new java.awt.FlowLayout() );
		//assert this.cls != null;
		//this.cls = cls;
		this.emptyStatement = emptyStatement;
		this.emptyStatementPane = AbstractStatementPane.createPane( this.emptyStatement, null );
		this.toolTip = new UbiquitousStatementToolTip( this.emptyStatementPane );
		this.component = this.createComponent();
		this.add( this.component );
		this.add( this.emptyStatementPane );
		this.setBackground( getIDE().getColorForASTClass( this.getStatementClass() ) );
		this.setToolTipText( "" );
		//this.label.setPreferredSize( new java.awt.Dimension( 128, 128 ) );
	}
	
	@Override
	public java.awt.Dimension getMinimumSize() {
		java.awt.Dimension rv = super.getMinimumSize();
		rv.width = 24;
		return rv;
	}
	
	protected javax.swing.JComponent createComponent() {
		String text = edu.cmu.cs.dennisc.util.ResourceBundleUtilities.getStringFromSimpleNames( this.getStatementClass(), "edu.cmu.cs.dennisc.alice.ast.UbiquitousTemplates" );
		return new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( text );
	}
	
//	@Override
//	public Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > getStatementClass() {
//		return this.cls;
//	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Dimension rv = this.component.getPreferredSize();
		javax.swing.border.Border border = this.getBorder();
		java.awt.Insets insets = border.getBorderInsets( this );
		rv.width += insets.left;
		rv.width += insets.right;
		rv.height += insets.top;
		rv.height += insets.bottom;
		return rv;
	}
	@Override
	protected void paintChildren( java.awt.Graphics g ) {
		javax.swing.border.Border border = this.getBorder();
		java.awt.Insets insets = border.getBorderInsets( this );
		g.translate( insets.left, insets.top );
		this.component.paint( g );
		g.translate( -insets.left, -insets.top );
	}
	@Override
	public int getDropWidth() {
		return this.emptyStatementPane.getPreferredSize().width;
	}
	@Override
	public int getDropHeight() {
		return this.emptyStatementPane.getPreferredSize().height;
	}
	@Override
	public void paintDrag( java.awt.Graphics2D g2, boolean isOverDragAccepter, boolean isCopyDesired ) {
		this.emptyStatementPane.print( g2 );
	}
	@Override
	public void paintDrop( java.awt.Graphics2D g2, boolean isOverDragAccepter, boolean isCopyDesired ) {
		paintDrag( g2, isOverDragAccepter, isCopyDesired );
	}
	@Override
	public javax.swing.JToolTip createToolTip() {
		return this.toolTip;
	}
}
