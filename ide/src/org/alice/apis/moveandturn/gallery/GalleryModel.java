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
package org.alice.apis.moveandturn.gallery;

import edu.cmu.cs.dennisc.alice.annotations.ClassTemplate;
import edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
@ClassTemplate(isFollowToSuperClassDesired = true, isConsumptionBySubClassDesired=true)
public abstract class GalleryModel extends edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel /*org.alice.apis.moveandturn.PolygonalModel*/ {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< String > MODELED_BY_CREDIT_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< String >( GalleryModel.class, "ModeledByCredit" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< String > PAINTED_BY_CREDIT_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< String >( GalleryModel.class, "PaintedByCredit" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< String > PROGRAMMED_BY_CREDIT_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< String >( GalleryModel.class, "ProgrammedByCredit" );
	private String m_modeledByCredit = null;
	private String m_paintedByCredit = null;
	private String m_programmedByCredit = null;
	
	private static java.io.File s_galleryRootDirectory;
	private static final String CHILD_NAME = "assets";
	private static final String GRANDCHILD_NAME = "org.alice.apis.moveandturn.gallery";
	static {
		s_galleryRootDirectory = GalleryRootUtilities.calculateGalleryRootDirectory( GalleryModel.class, "/Alice3Beta/gallery", "gallery", "assets", "org.alice.apis.moveandturn.gallery", "Alice Move & Turn Gallery", "Alice" );
	}
	public static java.io.File getGalleryRootDirectory() {
		return s_galleryRootDirectory;
	}
	
	protected GalleryModel() {
	}
	public GalleryModel( String path ) {
		//long t0 = System.currentTimeMillis();
		java.io.File directory = new java.io.File( new java.io.File( s_galleryRootDirectory, CHILD_NAME ), GRANDCHILD_NAME );
		java.io.File file = new java.io.File( directory, path + ".zip" );
		assert file.exists() : path;
		edu.cmu.cs.dennisc.codec.CodecUtilities.decodeZippedReferenceableBinary( this, file.getAbsolutePath(), "element.bin" );
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( System.currentTimeMillis() - t0 );
		this.realizeIfNecessary();
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
