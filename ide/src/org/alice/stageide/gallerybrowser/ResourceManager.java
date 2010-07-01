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
package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class ResourceManager {
	private static final String PACKAGE_NAME_PREFIX = ResourceManager.class.getPackage().getName();
	private ResourceManager() {
	}
	public static java.net.URL getLargeIconResource( javax.swing.tree.TreeNode treeNode ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( treeNode );
		if( treeNode instanceof edu.cmu.cs.dennisc.zip.ZipTreeNode ) {
			edu.cmu.cs.dennisc.zip.ZipTreeNode zipTreeNode = (edu.cmu.cs.dennisc.zip.ZipTreeNode)treeNode;
			String path = zipTreeNode.getValue();
			String resourceName = path.substring( PACKAGE_NAME_PREFIX.length()+1 );
			java.net.URL rv = ResourceManager.class.getResource( resourceName );
			if( rv != null ) {
				//pass
			} else {
				System.err.println( resourceName );
			}
			//assert rv != null : resourceName;
			return rv;
		} else {
			return null;
		}
	}
	public static java.net.URL getLargeIconResourceForGalleryClassName( String className ) {
		return ResourceManager.class.getResource( "images/" + className.replace( '.', '/' ) + ".png" );
	}
	public static Class<?> getGalleryCls( javax.swing.tree.TreeNode treeNode ) {
		if( treeNode instanceof edu.cmu.cs.dennisc.zip.FileZipTreeNode ) {
			final String IMAGES_TEXT = ".images.";
			edu.cmu.cs.dennisc.zip.FileZipTreeNode fileZipTreeNode = (edu.cmu.cs.dennisc.zip.FileZipTreeNode)treeNode;
			String path = fileZipTreeNode.getValue();
			path = path.replace( '/', '.' );
			assert path.startsWith( PACKAGE_NAME_PREFIX + IMAGES_TEXT ) : path;
			assert path.endsWith( ".png" ) : path;
			path = path.substring( PACKAGE_NAME_PREFIX.length()+IMAGES_TEXT.length(), path.length()-4 );
			try {
				return edu.cmu.cs.dennisc.java.lang.ClassUtilities.forName( path );
			} catch( ClassNotFoundException cnfe ) {
				throw new RuntimeException( path, cnfe );
			}
		} else {
			return null;
		}
	}
}
