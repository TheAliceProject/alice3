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

import org.lgna.croquet.components.BorderPanel.Constraint;

//class GalleryLabel extends edu.cmu.cs.dennisc.croquet.Label {
//	public GalleryLabel( java.net.URL url ) {
//		this.setIcon( new javax.swing.ImageIcon( url ) );
//		this.setVerticalAlignment( edu.cmu.cs.dennisc.croquet.VerticalAlignment.BOTTOM );
//	}
//	@Override
//	protected javax.swing.JLabel createAwtComponent() {
//		return new javax.swing.JLabel() {
//			@Override
//			public java.awt.Dimension getMaximumSize() {
//				return this.getPreferredSize();
//			}
//		};
//	}
//}

/**
 * @author Dennis Cosgrove
 */
public class CreateFieldFromGalleryPane extends CreateLargelyPredeterminedFieldPane {
	//private GalleryLabel galleryIcon;
	
	private CreateFieldFromGalleryPane( org.lgna.project.ast.UserType< ? > declaringType, Class<?> cls, javax.swing.Icon icon, org.lgna.project.ast.AbstractType<?,?,?> valueType ) {
		super( declaringType, cls, valueType );
		if( icon != null ) {
			this.addComponent( new org.lgna.croquet.components.Label( icon ), Constraint.LINE_END );
		}
	}
	public CreateFieldFromGalleryPane( org.lgna.project.ast.UserType< ? > declaringType, Class<?> cls ) {
		this( declaringType, cls, org.alice.stageide.gallerybrowser.ResourceManager.getLargeIconForGalleryClassName( cls.getName() ), null );
	}
	public CreateFieldFromGalleryPane( org.lgna.project.ast.UserType< ? > declaringType, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode ) {
		this( declaringType, org.alice.stageide.gallerybrowser.ResourceManager.getGalleryCls(treeNode), org.alice.stageide.gallerybrowser.ResourceManager.getLargeIcon(treeNode), null );
	}
	public CreateFieldFromGalleryPane( org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.AbstractType<?,?,?> valueType ) {
		this( declaringType, null, org.alice.stageide.gallerybrowser.ResourceManager.getLargeIconForGalleryClassName( valueType.getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getName() ), valueType );
	}

	public Object createInstanceInJava( Class<?>[] parameterClses, Object... argumentValues ) {
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( this.getValueType().getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getReification(), parameterClses, argumentValues );
	}
	public Object createInstanceInJavaForArguments( Object... argumentValues ) {
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstanceForArguments( this.getValueType().getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getReification(), argumentValues );
	}
	
	
	private static java.util.Set< String > prefixSet = new java.util.HashSet< String >();
	static {
		prefixSet = new java.util.HashSet< String >();
		prefixSet.add( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters" );
		prefixSet.add( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes" );
		prefixSet.add( "org.alice.apis.moveandturn.gallery" );
	}
	
//	private static Class<?> getClsFromGalleryFile( javax.swing.tree.TreeNode treeNode ) {
//		String path = edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file );
//		int index = -1;
//		for( String prefix : prefixSet ) {
//			int i = path.indexOf( prefix );
//			if( i >= 0 ) {
//				index = i;
//				break;
//			}
//		}
//		if( index >= 0 ) {
//			String s = path.substring( index, path.length()-4 );
//			s = s.replace( '\\', '/' );
//			s = s.replace( '/', '.' );
//			return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( s );
//		} else {
//			return null;
//		}
//	}
//	private static javax.swing.tree.TreeNode getGalleryFileFromCls( String clsName ) {
//		for( String prefix : prefixSet ) {
//			if( clsName.startsWith( prefix ) ) {
//				String post = clsName.substring( prefix.length() );
//				String path = prefix + post.replaceAll( "\\.", "/" ) + ".png";
//				java.io.File rootDirectory = new java.io.File( org.alice.ide.IDE.getActiveInstance().getGalleryRootDirectory(), "thumbnails" );
//				return new java.io.File( rootDirectory, path );
//			}
//		}
//		return null;
//	}

//	public static void main( String[] args ) {
//		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
//		org.lgna.project.ast.TypeDeclaredInJava declaringTypeDeclaredInJava = org.lgna.project.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Scene.class );
//		org.lgna.project.ast.TypeDeclaredInAlice declaringType = ide.getTypeDeclaredInAliceFor( declaringTypeDeclaredInJava );
//
//		java.io.File file = new java.io.File( "C:/Program Files/LookingGlass/0.alpha.0000/gallery/thumbnails/edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters/adults/Lunchlady.png" );
//		//org.lgna.project.ast.TypeDeclaredInJava typeDeclaredInJava = org.lgna.project.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.adults.Coach.class );
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
