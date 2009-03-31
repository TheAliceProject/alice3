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

class GalleryIcon extends javax.swing.JLabel {
	public GalleryIcon( java.io.File file ) {
		this.setIcon( new javax.swing.ImageIcon( file.getAbsolutePath() ) );
		this.setVerticalAlignment( CENTER );
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

class RowsSpringPane extends swing.Pane {
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


/**
 * @author Dennis Cosgrove
 */
public class CreateFieldPane extends zoot.ZInputPane< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType declaringType;
	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	private java.io.File file;

	private static zoot.ZLabel createLabel( String s ) {
		zoot.ZLabel rv = new zoot.ZLabel( s );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		return rv;
	}
	class MyRowsPane extends RowsSpringPane {
		@Override
		protected java.util.List< java.awt.Component[] > createComponentRows() {
			java.util.List< java.awt.Component[] > rv = super.createComponentRows();

			
			//GalleryIcon galleryIcon = new GalleryIcon( CreateFieldPane.this.file );
			
			swing.LineAxisPane declarationLine = new swing.LineAxisPane( 
					new zoot.ZLabel( "declare " ), 
					new org.alice.ide.common.TypeComponent( CreateFieldPane.this.declaringType ), 
					new zoot.ZLabel( " property:" ) 
			);
			swing.LineAxisPane valueTypeLine = new swing.LineAxisPane();
			valueTypeLine.add( new org.alice.ide.common.TypeComponent( CreateFieldPane.this.type ) );
			if( CreateFieldPane.this.type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
				valueTypeLine.add( new zoot.ZLabel( " which extends ", zoot.font.ZTextPosture.OBLIQUE, zoot.font.ZTextWeight.LIGHT ) );
				valueTypeLine.add( new org.alice.ide.common.TypeComponent( CreateFieldPane.this.type.getSuperType() ) );
//				valueTypeLine.add( new zoot.ZLabel( " ) ", zoot.font.ZTextPosture.OBLIQUE, zoot.font.ZTextWeight.LIGHT ) );
			}
			
			org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();;
			java.awt.Component initializer = ide.getCodeFactory().createExpressionPane( org.alice.ide.ast.NodeUtilities.createInstanceCreation( type ) );
			
//			rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "declare property:" ), lineAxisPane ) );
			rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( declarationLine, null ) );
			rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( javax.swing.Box.createVerticalStrut( 10 ), null ) );
			rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "value class:" ), valueTypeLine ) );
			rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "name:" ), new InstanceNameTextField() ) );
			rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "initializer:" ), initializer ) );
			rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( javax.swing.Box.createVerticalStrut( 10 ), null ) );
			return rv;
		}
	}

	public CreateFieldPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, java.io.File file, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.declaringType = declaringType;
		this.type = type;
		this.file = file;
		GalleryIcon galleryIcon = new GalleryIcon( this.file );
		MyRowsPane classInfoPane = new MyRowsPane();
		this.setLayout( new java.awt.BorderLayout() );
		final int INSET = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
		this.add( galleryIcon, java.awt.BorderLayout.EAST );
		this.add( classInfoPane, java.awt.BorderLayout.CENTER );
	}
	
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getActualInputValue() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo" );
		return null;
	}

	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava declaringTypeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Scene.class );
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = ide.getTypeDeclaredInAliceFor( declaringTypeDeclaredInJava );

		java.io.File file = new java.io.File( "C:/Program Files/LookingGlass/0.alpha.0000/gallery/thumbnails/edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters/adults/Coach.png" );
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.adults.Coach.class );
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = ide.getTypeDeclaredInAliceFor( typeDeclaredInJava );


		CreateFieldPane createFieldPane = new CreateFieldPane( declaringType, file, type );
		createFieldPane.showInJDialog( ide, "Declare Property", true );
		System.exit( 0 );
	}
}
