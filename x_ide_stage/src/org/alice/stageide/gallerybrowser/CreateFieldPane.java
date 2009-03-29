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
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CreateFieldPane extends zoot.ZInputPane< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava baseType;
	public CreateFieldPane( java.io.File file, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava baseType ) {
		this.baseType = baseType;
		GalleryIcon galleryIcon = new GalleryIcon( file );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( galleryIcon, java.awt.BorderLayout.WEST );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getActualInputValue() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo" );
		return null;
	}

	public static void main( String[] args ) {
		java.io.File file = new java.io.File( "C:/Program Files/LookingGlass/0.alpha.0000/gallery/thumbnails/edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters/adults/Coach.png" );
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava baseType = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.adults.Coach.class );
		final java.io.File thumbnailRoot = new java.io.File( org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory(), "thumbnails" );
		org.alice.ide.IDE ide = new org.alice.stageide.StageIDE();
		CreateFieldPane createFieldPane = new CreateFieldPane( file, baseType );
		createFieldPane.showInJDialog( ide );
	}
}
