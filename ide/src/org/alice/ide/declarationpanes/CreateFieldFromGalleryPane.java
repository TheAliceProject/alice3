/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.declarationpanes;

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
	private CreateFieldFromGalleryPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, Class<?> cls, java.io.File file, edu.cmu.cs.dennisc.alice.ast.AbstractType valueType ) {
		super( declaringType, cls, valueType );
		if( file != null ) {
			if( file.exists() ) {
				this.galleryIcon = new GalleryIcon( file );
				this.addComponent( new edu.cmu.cs.dennisc.croquet.KSwingAdapter( this.galleryIcon ), java.awt.BorderLayout.EAST );
			}
		}
	}
	public CreateFieldFromGalleryPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, Class<?> cls ) {
		this( declaringType, cls, getGalleryFileFromCls( cls.getName() ), null );
	}
	public CreateFieldFromGalleryPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, java.io.File file ) {
		this( declaringType, getClsFromGalleryFile( file ), file, null );
	}
	public CreateFieldFromGalleryPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, edu.cmu.cs.dennisc.alice.ast.AbstractType valueType ) {
		this( declaringType, null, getGalleryFileFromCls( valueType.getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getName() ), valueType );
	}
	

	public Object createInstanceInJava() {
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( this.getValueType().getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getReification() );
	}

	private static java.util.Set< String > prefixSet = new java.util.HashSet< String >();
	static {
		prefixSet = new java.util.HashSet< String >();
		prefixSet.add( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters" );
		prefixSet.add( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes" );
		prefixSet.add( "org.alice.apis.moveandturn.gallery" );
	}
	
	private static Class<?> getClsFromGalleryFile( java.io.File file ) {
		String path = edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file );
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
			return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( s );
		} else {
			return null;
		}
	}
	private static java.io.File getGalleryFileFromCls( String clsName ) {
		for( String prefix : prefixSet ) {
			if( clsName.startsWith( prefix ) ) {
				String post = clsName.substring( prefix.length() );
				String path = prefix + post.replaceAll( "\\.", "/" ) + ".png";
				java.io.File rootDirectory = new java.io.File( org.alice.ide.IDE.getSingleton().getGalleryRootDirectory(), "thumbnails" );
				return new java.io.File( rootDirectory, path );
			}
		}
		return null;
	}

//	public static void main( String[] args ) {
//		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
//		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava declaringTypeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Scene.class );
//		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = ide.getTypeDeclaredInAliceFor( declaringTypeDeclaredInJava );
//
//		java.io.File file = new java.io.File( "C:/Program Files/LookingGlass/0.alpha.0000/gallery/thumbnails/edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters/adults/Lunchlady.png" );
//		//edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.adults.Coach.class );
//
//
//		CreateFieldFromGalleryPane createFieldFromGalleryPane = new CreateFieldFromGalleryPane( declaringType, file );
//		org.alice.ide.createdeclarationpanes.CreateFieldPane createFieldPane = new org.alice.ide.createdeclarationpanes.CreateFieldPane( declaringType );
//		org.alice.ide.createdeclarationpanes.CreateLocalPane createLocalPane = new org.alice.ide.createdeclarationpanes.CreateLocalPane( null );
//		org.alice.ide.createdeclarationpanes.CreateProcedurePane createProcedurePane = new org.alice.ide.createdeclarationpanes.CreateProcedurePane( declaringType );
//		org.alice.ide.createdeclarationpanes.CreateFunctionPane createFunctionPane = new org.alice.ide.createdeclarationpanes.CreateFunctionPane( declaringType );
//		createFieldFromGalleryPane.showInJDialog( ide );
////		createFieldPane.showInJDialog( ide );
////		createLocalPane.showInJDialog( ide );
////		createProcedurePane.showInJDialog( ide );
////		createFunctionPane.showInJDialog( ide );
//		System.exit( 0 );
//	}
}
