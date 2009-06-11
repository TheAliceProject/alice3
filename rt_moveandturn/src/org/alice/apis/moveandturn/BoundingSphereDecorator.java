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

package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public class BoundingSphereDecorator {
	private Transformable m_subject;
	private edu.cmu.cs.dennisc.scenegraph.Transformable m_sgTransformable = new edu.cmu.cs.dennisc.scenegraph.Transformable();
	private edu.cmu.cs.dennisc.scenegraph.Visual m_sgVisual = new edu.cmu.cs.dennisc.scenegraph.Visual();
	private edu.cmu.cs.dennisc.scenegraph.SingleAppearance m_sgFrontAppearance = new edu.cmu.cs.dennisc.scenegraph.SingleAppearance();
	private edu.cmu.cs.dennisc.scenegraph.SingleAppearance m_sgBackAppearance = new edu.cmu.cs.dennisc.scenegraph.SingleAppearance();
	private edu.cmu.cs.dennisc.scenegraph.Sphere m_sgSphere = new edu.cmu.cs.dennisc.scenegraph.Sphere();

	public BoundingSphereDecorator( Transformable subject ) {
		m_subject = subject;
		m_sgVisual.isShowing.setValue( false );
		m_sgFrontAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.YELLOW );
		m_sgFrontAppearance.setShadingStyle( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE );
		m_sgFrontAppearance.setFillingStyle( edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME );
		m_sgBackAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.DARK_GRAY );
		m_sgBackAppearance.setShadingStyle( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE );
		m_sgBackAppearance.setFillingStyle( edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME );

		m_sgVisual.frontFacingAppearance.setValue( m_sgFrontAppearance );
		m_sgVisual.backFacingAppearance.setValue( m_sgBackAppearance );
		m_sgVisual.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { m_sgSphere } );

		m_sgVisual.setParent( m_sgTransformable );
		
		//todo
		m_sgVisual.setName( "BoundingSphereDecorator for " + m_subject );
	}
	public Boolean isShowing() {
		return m_sgVisual.isShowing.getValue();
	}
	public void setShowing( Boolean isShowing ) {
		if( isShowing ) {
			if( m_sgTransformable.getParent() != null ) {
				//pass
			} else {
				m_sgTransformable.setParent( m_subject.getSGTransformable() );
			}
			edu.cmu.cs.dennisc.math.Sphere sphere = m_subject.getBoundingSphere();
			m_sgSphere.radius.setValue( sphere.radius );
			m_sgTransformable.localTransformation.setValue( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createTranslation( sphere.center ) );
		}
		m_sgVisual.isShowing.setValue( isShowing );
	}
}
