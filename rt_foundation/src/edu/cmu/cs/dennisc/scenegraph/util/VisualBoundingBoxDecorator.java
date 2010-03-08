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
package edu.cmu.cs.dennisc.scenegraph.util;

/**
 * @author Dennis Cosgrove
 */
public class VisualBoundingBoxDecorator extends BoundingBoxDecorator implements edu.cmu.cs.dennisc.scenegraph.event.BoundListener {
	private edu.cmu.cs.dennisc.scenegraph.Visual m_sgSubject = null;

	public edu.cmu.cs.dennisc.scenegraph.Visual getSubject() {
		return m_sgSubject;
	}
	public void setSubject( edu.cmu.cs.dennisc.scenegraph.Visual sgSubject ) {
		if( m_sgSubject != null ) {
			setParent( null );
			edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = m_sgSubject.getGeometry();
			if( sgGeometry != null ) {
				sgGeometry.removeBoundObserver( this );
			}
		}
		m_sgSubject = sgSubject;
		if( m_sgSubject != null ) {
			edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = m_sgSubject.getGeometry();
			if( sgGeometry != null ) {
				setBox( sgGeometry.getAxisAlignedMinimumBoundingBox() );
				setParent( m_sgSubject.getParent() );
				sgGeometry.addBoundObserver( this );
			}
		}
	}

	public void boundChanged( edu.cmu.cs.dennisc.scenegraph.event.BoundEvent e ) {
		edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = e.getTypedSource();
		setBox( sgGeometry.getAxisAlignedMinimumBoundingBox() );
	}
}

//public class BoundingBoxDecorator implements edu.cmu.cs.dennisc.scenegraph.event.BoundListener {
//	private edu.cmu.cs.dennisc.scenegraph.Visual m_sgVisual = new edu.cmu.cs.dennisc.scenegraph.Visual();
//	private edu.cmu.cs.dennisc.scenegraph.SingleAppearance m_sgFrontAppearance = new edu.cmu.cs.dennisc.scenegraph.SingleAppearance();
//	private edu.cmu.cs.dennisc.scenegraph.SingleAppearance m_sgBackAppearance = new edu.cmu.cs.dennisc.scenegraph.SingleAppearance();
//	private edu.cmu.cs.dennisc.scenegraph.Box m_sgBox = new edu.cmu.cs.dennisc.scenegraph.Box();
//
//	private edu.cmu.cs.dennisc.scenegraph.Visual m_sgSubject = null;
//
//	public BoundingBoxDecorator() {
//		m_sgFrontAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.ColorF4.YELLOW );
//		m_sgFrontAppearance.setShadingStyle( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE );
//		m_sgFrontAppearance.setFillingStyle( edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME );
//		m_sgBackAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.ColorF4.DARK_GRAY );
//		m_sgBackAppearance.setShadingStyle( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE );
//		m_sgBackAppearance.setFillingStyle( edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME );
//		m_sgVisual.frontFacingAppearance.setValue( m_sgFrontAppearance );
//		m_sgVisual.backFacingAppearance.setValue( m_sgBackAppearance );
//		m_sgVisual.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { m_sgBox } );
//
//		m_sgVisual.setName( "BoundingBoxDecorator m_sgVisual" );
//		m_sgFrontAppearance.setName( "BoundingBoxDecorator m_sgFrontAppearance" );
//		m_sgBackAppearance.setName( "BoundingBoxDecorator m_sgBackAppearance" );
//		m_sgBox.setName( "BoundingBoxDecorator m_sgBox" );
//	}
//	
//	public edu.cmu.cs.dennisc.scenegraph.Visual getSubject() {
//		return m_sgSubject;
//	}
//	public void setSubject( edu.cmu.cs.dennisc.scenegraph.Visual sgSubject ) {
//		if( m_sgSubject != null ) {
//			m_sgVisual.setParent( null );
//			edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = m_sgSubject.getGeometry();
//			if( sgGeometry != null ) {
//				sgGeometry.removeBoundObserver( this );
//			}
//		}
//		m_sgSubject = sgSubject;
//		if( m_sgSubject != null ) {
//			edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = m_sgSubject.getGeometry();
//			if( sgGeometry != null ) {
//				m_sgBox.set( sgGeometry.getBoundingBox() );
//				m_sgVisual.setParent( m_sgSubject.getParent() );
//				sgGeometry.addBoundObserver( this );
//			}
//		}
//	}
//
//	public void boundChanged( edu.cmu.cs.dennisc.scenegraph.event.BoundEvent e ) {
//		edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = e.getGeometrySource();
//		m_sgBox.set( sgGeometry.getBoundingBox() );
//	}
//}
