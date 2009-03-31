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

		java.awt.Component component = new org.alice.ide.common.TypeComponent( this.type );
		if( this.type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			zoot.ZLabel label = new zoot.ZLabel( " which extends " );
			label.setFontToDerivedFont( zoot.font.ZTextPosture.OBLIQUE, zoot.font.ZTextWeight.LIGHT );
			component = new swing.LineAxisPane( component, label, new org.alice.ide.common.TypeComponent( this.type.getSuperType() ) );
		}
		
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();;
		java.awt.Component initializer = ide.getCodeFactory().createComponent( org.alice.ide.ast.NodeUtilities.createInstanceCreation( type ) );
		
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, null ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "value class:" ), component ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "name:" ), new InstanceNameTextField() ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "initializer:" ), initializer ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, null ) );
		return rv;
	}
}

class FieldInfoPane extends swing.LineAxisPane {
	public FieldInfoPane( edu.cmu.cs.dennisc.alice.ast.AbstractType declaringType ) {
		this.add( new zoot.ZLabel( "declare " ) );
		this.add( new org.alice.ide.common.TypeComponent( declaringType ) );
		this.add( new zoot.ZLabel( " property" ) );
	}
}



/**
 * @author Dennis Cosgrove
 */
public class CreateFieldPane extends zoot.ZInputPane< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType declaringType;
	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	private java.io.File file;

	public CreateFieldPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, java.io.File file, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.declaringType = declaringType;
		this.type = type;
		this.file = file;
		FieldInfoPane fieldInfoPane = new FieldInfoPane( this.declaringType );
		GalleryIcon galleryIcon = new GalleryIcon( this.file );
		ClassInfoPane classInfoPane = new ClassInfoPane( type );
		this.setLayout( new java.awt.BorderLayout() );
		
		final int INSET = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
		this.add( galleryIcon, java.awt.BorderLayout.WEST );
		this.add( fieldInfoPane, java.awt.BorderLayout.NORTH );
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
		createFieldPane.showInJDialog( ide, "Create New Instance", true );
		System.exit( 0 );
	}
}
