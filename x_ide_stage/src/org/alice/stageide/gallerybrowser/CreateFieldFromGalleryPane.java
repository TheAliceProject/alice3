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
		this.setVerticalAlignment( BOTTOM );
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CreateFieldFromGalleryPane extends CreateLargelyPredeterminedFieldPane {
	private GalleryIcon galleryIcon;
	private java.io.File file;

	public CreateFieldFromGalleryPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, java.io.File file ) {
		super( declaringType, getTypeFromGalleryFile( file ).getCls() );
		this.file = file;
	}
	

	public Object createInstanceInJava() {
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( this.getValueType().getFirstClassEncounteredDeclaredInJava() );
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		if( this.galleryIcon != null ) {
			//pass
		} else {
			this.galleryIcon = new GalleryIcon( this.file );
			this.add( this.galleryIcon, java.awt.BorderLayout.EAST );
		}
	}
	
	private static java.util.Set< String > prefixSet = new java.util.HashSet< String >();
	static {
		prefixSet = new java.util.HashSet< String >();
		prefixSet.add( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters" );
		prefixSet.add( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes" );
		prefixSet.add( "org.alice.apis.moveandturn.gallery" );
	}
	private static edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava getTypeFromGalleryFile( java.io.File file ) {
		String path = edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( file );
		int index = -1;
		for( String prefix : prefixSet ) {
			int i = path.indexOf( prefix );
			if( i >= 0 ) {
				index = i;
				break;
			}
		}
		if( index >= 0 ) {
			String s = path.substring( index, path.length()-4 );
			s = s.replace( '\\', '/' );
			s = s.replace( '/', '.' );
			Class<?> cls = edu.cmu.cs.dennisc.lang.ClassUtilities.forName( s );
			if( cls != null ) {
				return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	
	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava declaringTypeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Scene.class );
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = ide.getTypeDeclaredInAliceFor( declaringTypeDeclaredInJava );

		java.io.File file = new java.io.File( "C:/Program Files/LookingGlass/0.alpha.0000/gallery/thumbnails/edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters/adults/Lunchlady.png" );
		//edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.adults.Coach.class );


		CreateFieldFromGalleryPane createFieldFromGalleryPane = new CreateFieldFromGalleryPane( declaringType, file );
		org.alice.ide.createdeclarationpanes.CreateFieldPane createFieldPane = new org.alice.ide.createdeclarationpanes.CreateFieldPane( declaringType );
		org.alice.ide.createdeclarationpanes.CreateLocalPane createLocalPane = new org.alice.ide.createdeclarationpanes.CreateLocalPane( null );
		org.alice.ide.createdeclarationpanes.CreateProcedurePane createProcedurePane = new org.alice.ide.createdeclarationpanes.CreateProcedurePane( declaringType );
		org.alice.ide.createdeclarationpanes.CreateFunctionPane createFunctionPane = new org.alice.ide.createdeclarationpanes.CreateFunctionPane( declaringType );
		createFieldFromGalleryPane.showInJDialog( ide );
//		createFieldPane.showInJDialog( ide );
//		createLocalPane.showInJDialog( ide );
//		createProcedurePane.showInJDialog( ide );
//		createFunctionPane.showInJDialog( ide );
		System.exit( 0 );
	}
}
