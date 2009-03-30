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
package org.alice.stageide.gallerybrowser;

class TypeBorder implements javax.swing.border.Border {
	private static final int X_INSET = 10;
	private static final int Y_INSET = 4;
	private static java.awt.Insets insets = new java.awt.Insets( Y_INSET, X_INSET, Y_INSET, X_INSET );
	private static java.awt.Color FILL_COLOR = org.alice.ide.IDE.getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.TypeExpression.class );
	private static java.awt.Color FILL_BRIGHTER_COLOR = FILL_COLOR.brighter();
	private static java.awt.Color FILL_DARKER_COLOR = FILL_COLOR.darker();

	private static java.awt.Color OUTLINE_COLOR = java.awt.Color.GRAY;
	private static TypeBorder singletonForDeclaredInAlice = new TypeBorder( true );
	private static TypeBorder singletonForDeclaredInJava = new TypeBorder( false );

	public static TypeBorder getSingletonFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		if( type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			return TypeBorder.singletonForDeclaredInAlice;
		} else {
			return TypeBorder.singletonForDeclaredInJava;
		}
	}
	private boolean isDeclaredInAlice;
	private TypeBorder( boolean isDeclaredInAlice ) {
		this.isDeclaredInAlice = isDeclaredInAlice;
	}

	private int yPrevious = -1;
	private int heightPrevious = -1;
	private java.awt.GradientPaint paintPrevious = null;
	private java.awt.Paint getFillPaint( int x, int y, int width, int height ) {
		if( y==this.yPrevious && height==this.heightPrevious ) {
			//pass
		} else {
			this.yPrevious = y;
			this.heightPrevious = height;
			if( isDeclaredInAlice ) {
				this.paintPrevious = new java.awt.GradientPaint( 0, y, FILL_COLOR, 0, y + height, FILL_BRIGHTER_COLOR );
			} else {
				this.paintPrevious = new java.awt.GradientPaint( 0, y, FILL_COLOR, 0, y + height, FILL_DARKER_COLOR );
			}
		}
		return this.paintPrevious;
	}

	public java.awt.Insets getBorderInsets( java.awt.Component c ) {
		return TypeBorder.insets;
	}
	public boolean isBorderOpaque() {
		return false;
	}
	private static java.awt.Shape createShape( int x, int y, int width, int height ) {
		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
		int x0 = x;
		int x1 = x0 + width - 1;
		int xA = x0 + X_INSET;
		int xB = x1 - X_INSET;

		int y0 = y;
		int y1 = y0 + height - 1;
		int yC = (y0 + y1) / 2;

		rv.moveTo( xA, y0 );
		rv.lineTo( xB, y0 );
		rv.lineTo( x1, yC );
		rv.lineTo( xB, y1 );
		rv.lineTo( xA, y1 );
		rv.lineTo( x0, yC );
		rv.lineTo( xA, y0 );
		return rv;
	}
	public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
		java.awt.Shape shape = TypeBorder.createShape( x, y, width, height );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( getFillPaint( x, y, width, height ) );
		g2.fill( shape );
		g2.setPaint( OUTLINE_COLOR );
		g2.draw( shape );
	}
}

class TypeComponent extends org.alice.ide.common.NodeNameLabel {
	public TypeComponent( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		super( type );
		this.setBorder( TypeBorder.getSingletonFor( type ) );
	}
	@Override
	public void paint( java.awt.Graphics g ) {
		this.paintBorder( g );
		this.paintComponent( g );
	}
}

class GalleryIcon extends javax.swing.JLabel {
	public GalleryIcon( java.io.File file ) {
		this.setIcon( new javax.swing.ImageIcon( file.getAbsolutePath() ) );
//		this.setOpaque( true );
//		this.setBackground( java.awt.Color.RED );
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
}

class InstanceNameTextField extends javax.swing.JTextField {
	public InstanceNameTextField() {
		this.setFont( this.getFont().deriveFont( 18.0f ) );
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
}

class RowsPane extends swing.Pane {
	@Override
	public void addNotify() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "addNotify" );
		if( getLayout() instanceof javax.swing.SpringLayout ) {
			//pass
		} else {
			java.util.List< java.awt.Component[] > componentRows = this.createComponentRows();
			edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( this, componentRows, 12, 12 );
		}
		super.addNotify();
	}
	protected java.util.List< java.awt.Component[] > createComponentRows() {
		return new java.util.LinkedList< java.awt.Component[] >();
	}
}

class ClassInfoPane extends RowsPane {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;

	public ClassInfoPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.type = type;
//		this.setOpaque( true );
//		this.setBackground( java.awt.Color.BLUE );
	}
	private static zoot.ZLabel createLabel( String s ) {
		zoot.ZLabel rv = new zoot.ZLabel( s );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		return rv;
	}
	@Override
	protected java.util.List< java.awt.Component[] > createComponentRows() {
		java.util.List< java.awt.Component[] > rv = super.createComponentRows();

		java.awt.Component component;
		if( this.type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			zoot.ZLabel label = new zoot.ZLabel( " which extends " );
			label.setFontToDerivedFont( zoot.font.ZTextPosture.OBLIQUE, zoot.font.ZTextWeight.LIGHT );
			component = new swing.LineAxisPane( new TypeComponent( this.type ), label, new TypeComponent( this.type.getSuperType() ) );
		} else {
			component = new TypeComponent( this.type );
		}
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, null ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "create new instance of class:" ), component ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "instance name:" ), new InstanceNameTextField() ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, null ) );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CreateFieldPane extends zoot.ZInputPane< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	private java.io.File file;

	public CreateFieldPane( java.io.File file, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.type = type;
		this.file = file;
		GalleryIcon galleryIcon = new GalleryIcon( this.file );
		ClassInfoPane classInfoPane = new ClassInfoPane( type );
		this.setLayout( new java.awt.BorderLayout() );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 0, 0, 16 ) );
		this.add( galleryIcon, java.awt.BorderLayout.WEST );
		this.add( classInfoPane, java.awt.BorderLayout.CENTER );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getActualInputValue() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo" );
		return null;
	}

	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		java.io.File file = new java.io.File( "C:/Program Files/LookingGlass/0.alpha.0000/gallery/thumbnails/edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters/adults/Coach.png" );
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.adults.Coach.class );
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = ide.getTypeDeclaredInAliceFor( typeDeclaredInJava );
		CreateFieldPane createFieldPane = new CreateFieldPane( file, type );
		createFieldPane.showInJDialog( ide, "Create New Instance", true );
		System.exit( 0 );
	}
}
