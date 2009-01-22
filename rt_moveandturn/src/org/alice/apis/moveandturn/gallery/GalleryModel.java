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
package org.alice.apis.moveandturn.gallery;

import edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public abstract class GalleryModel extends org.alice.apis.moveandturn.PolygonalModel {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< String > MODELED_BY_CREDIT_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< String >( GalleryModel.class, "ModeledByCredit" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< String > PAINTED_BY_CREDIT_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< String >( GalleryModel.class, "PaintedByCredit" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< String > PROGRAMMED_BY_CREDIT_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< String >( GalleryModel.class, "ProgrammedByCredit" );
	private String m_modeledByCredit = null;
	private String m_paintedByCredit = null;
	private String m_programmedByCredit = null;
	
	private static java.io.File s_galleryRootDirectory;
	static {
		java.util.List< String > potentialPaths = new java.util.LinkedList< String >();
		String rootPath = System.getProperty( GalleryModel.class.getName() + ".rootPath" );
		if( rootPath != null ) {
			potentialPaths.add( rootPath );
		}
		String subPath = "/Alice/3.beta.0000/gallery";
		potentialPaths.add( "/Program Files" + subPath );
		potentialPaths.add( "/Applications" + subPath );
		potentialPaths.add( "/Applications/Programming" + subPath );
		potentialPaths.add( "c:/Program Files" + subPath );
		for( String path : potentialPaths ) {
			java.io.File directory = new java.io.File( path );
			if( directory.exists() && directory.isDirectory() ) {
				s_galleryRootDirectory = directory;
				break;
			}
		}
		if( s_galleryRootDirectory != null ) {
			
		} else {
//			javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
//			fileChooser.setFileSelectionMode( javax.swing.JFileChooser.DIRECTORIES_ONLY );
		}
	}
	public static java.io.File getGalleryRootDirectory() {
		return s_galleryRootDirectory;
	}
	protected GalleryModel() {
	}
	public GalleryModel( String path ) {
		java.io.File file = new java.io.File( s_galleryRootDirectory, "assets/org.alice.apis.moveandturn.gallery/" + path + ".zip" );
		assert file.exists();
		edu.cmu.cs.dennisc.codec.CodecUtilities.decodeZippedReferenceableBinary( this, file.getAbsolutePath(), "element.bin" );	
	}
	@PropertyGetterTemplate(visibility = Visibility.TUCKED_AWAY)
	public String getModeledByCredit() {
		return m_modeledByCredit;
	}
	public void setModeledByCredit( String modeledByCredit ) {
		m_modeledByCredit = modeledByCredit;
	}
	@PropertyGetterTemplate(visibility = Visibility.TUCKED_AWAY)
	public String getPaintedByCredit() {
		return m_paintedByCredit;
	}
	public void setPaintedByCredit( String paintedByCredit ) {
		m_paintedByCredit = paintedByCredit;
	}
	@PropertyGetterTemplate(visibility = Visibility.TUCKED_AWAY)
	public String getProgrammedByCredit() {
		return m_programmedByCredit;
	}
	public void setProgrammedByCredit( String programmedByCredit ) {
		m_programmedByCredit = programmedByCredit;
	}
}
