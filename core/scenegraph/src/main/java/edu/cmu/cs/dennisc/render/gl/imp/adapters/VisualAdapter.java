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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import static javax.media.opengl.GL.GL_BACK;
import static javax.media.opengl.GL.GL_CULL_FACE;
import static javax.media.opengl.GL.GL_FRONT;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import edu.cmu.cs.dennisc.render.gl.imp.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.PickParameters;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.Appearance;

/**
 * @author Dennis Cosgrove
 */
public class VisualAdapter<E extends edu.cmu.cs.dennisc.scenegraph.Visual> extends LeafAdapter<E> {

	public enum RenderType {
		OPAQUE,
		ALPHA_BLENDED,
		GHOST,
		SILHOUETTE,
		ALL
	}

	//for tree node
	/* package-private */AppearanceAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Appearance> getFrontFacingAppearanceAdapter() {
		return m_frontFacingAppearanceAdapter;
	}

	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 inverseAbsoluteTransformationOfSource, int geometryIndex, int subElement ) {
		if( ( 0 <= geometryIndex ) && ( geometryIndex < m_geometryAdapters.length ) ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 absoluteTransformation = this.m_element.getAbsoluteTransformation();
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createMultiplication( inverseAbsoluteTransformationOfSource, absoluteTransformation );
			m_geometryAdapters[ geometryIndex ].getIntersectionInSource( rv, ray, m, subElement );
		}
		return rv;
	}

	private boolean isEthereal() {
		if( m_frontFacingAppearanceAdapter != null ) {
			if( m_frontFacingAppearanceAdapter.isEthereal() ) {
				//pass
			} else {
				return false;
			}
		}
		if( m_backFacingAppearanceAdapter != null ) {
			if( m_backFacingAppearanceAdapter.isEthereal() ) {
				//pass
			} else {
				return false;
			}
		}
		return true;
	}

	protected boolean isActuallyShowing() {
		if( m_isShowing && ( m_geometryAdapters != null ) && ( m_geometryAdapters.length > 0 ) ) {
			if( m_frontFacingAppearanceAdapter != null ) {
				if( m_frontFacingAppearanceAdapter.isActuallyShowing() ) {
					return true;
				}
			}
			if( m_backFacingAppearanceAdapter != null ) {
				if( m_backFacingAppearanceAdapter.isActuallyShowing() ) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean hasOpaque() {
		if( ( m_geometryAdapters != null ) && ( m_geometryAdapters.length > 0 ) ) {
			if( m_frontFacingAppearanceAdapter != null ) {
				if( m_frontFacingAppearanceAdapter.isAlphaBlended() ) {
					return false;
				}
			}
			if( m_backFacingAppearanceAdapter != null ) {
				if( m_backFacingAppearanceAdapter.isAlphaBlended() ) {
					return false;
				}
			}

			synchronized( m_geometryAdapters ) {
				for( GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : m_geometryAdapters ) {
					if( geometryAdapter.hasOpaque() ) {
						return true;
					}
				}
			}

		}
		return false;
	}

	protected boolean isAllAlpha() {
		if( m_frontFacingAppearanceAdapter != null ) {
			if( m_frontFacingAppearanceAdapter.isAllAlphaBlended() ) {
				return true;
			}
		}
		if( m_backFacingAppearanceAdapter != null ) {
			if( m_backFacingAppearanceAdapter.isAllAlphaBlended() ) {
				return true;
			}
		}
		return false;
	}

	protected boolean isAlphaBlended() {
		if( ( m_geometryAdapters != null ) && ( m_geometryAdapters.length > 0 ) ) {
			synchronized( m_geometryAdapters ) {
				for( GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : m_geometryAdapters ) {
					if( geometryAdapter.isAlphaBlended() ) {
						return true;
					}
				}
			}
			if( m_frontFacingAppearanceAdapter != null ) {
				if( m_frontFacingAppearanceAdapter.isAlphaBlended() ) {
					return true;
				}
			}
			if( m_backFacingAppearanceAdapter != null ) {
				if( m_backFacingAppearanceAdapter.isAlphaBlended() ) {
					return true;
				}
			}
		}
		return false;
	}

	private void updateScale( edu.cmu.cs.dennisc.math.Matrix3x3 m ) {
		m_isScaleIdentity = m.isIdentity();
		m.getAsColumnMajorArray16( m_scale );
	}

	@Override
	public void handleReleased()
	{
		synchronized( m_geometryAdapters ) {
			for( GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : m_geometryAdapters ) {
				if( geometryAdapter.m_element != null )
				{
					geometryAdapter.handleReleased();
				}
			}
			m_geometryAdapters = null;
		}
		if( ( m_frontFacingAppearanceAdapter != null ) && ( m_frontFacingAppearanceAdapter.m_element != null ) )
		{
			m_frontFacingAppearanceAdapter.handleReleased();
		}
		m_frontFacingAppearanceAdapter = null;
		if( ( m_backFacingAppearanceAdapter != null ) && ( m_backFacingAppearanceAdapter.m_element != null ) )
		{
			m_backFacingAppearanceAdapter.handleReleased();
		}
		m_backFacingAppearanceAdapter = null;
	}

	@Override
	public void setup( RenderContext rc ) {
		//pass
	}

	protected void renderGeometry( edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc, RenderType renderType ) {
		synchronized( m_geometryAdapters ) {
			for( GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : m_geometryAdapters ) {
				geometryAdapter.render( rc, renderType );
			}
		}
	}

	private void preSilhouette( RenderContext rc ) {
		rc.gl.glClearStencil( 0 );
		rc.gl.glClear( javax.media.opengl.GL.GL_STENCIL_BUFFER_BIT );
		rc.gl.glEnable( javax.media.opengl.GL.GL_STENCIL_TEST );
		rc.gl.glStencilFunc( javax.media.opengl.GL.GL_ALWAYS, 1, -1 );
		rc.gl.glStencilOp( javax.media.opengl.GL.GL_KEEP, javax.media.opengl.GL.GL_KEEP, javax.media.opengl.GL.GL_REPLACE );
	}

	private void postSilouette( RenderContext rc, int face ) {
		rc.gl.glStencilFunc( javax.media.opengl.GL2.GL_NOTEQUAL, 1, -1 );
		rc.gl.glStencilOp( javax.media.opengl.GL.GL_KEEP, javax.media.opengl.GL.GL_KEEP, javax.media.opengl.GL.GL_REPLACE );

		this.silhouetteAdapter.setup( rc, GL_FRONT );
		this.renderGeometry( rc, RenderType.SILHOUETTE );
		rc.gl.glDisable( javax.media.opengl.GL.GL_STENCIL_TEST );
	}

	protected void actuallyRender( RenderContext rc, RenderType renderType ) {
		assert ( m_frontFacingAppearanceAdapter != null ) || ( m_backFacingAppearanceAdapter != null );

		if( m_isScaleIdentity ) {
			//pass
		} else {
			rc.gl.glPushMatrix();
			rc.gl.glMultMatrixd( m_scaleBuffer );
			rc.incrementScaledCount();
		}
		if( m_frontFacingAppearanceAdapter == m_backFacingAppearanceAdapter ) {
			if( m_frontFacingAppearanceAdapter != null ) {
				m_frontFacingAppearanceAdapter.setPipelineState( rc, GL_FRONT_AND_BACK );
				rc.gl.glDisable( GL_CULL_FACE );
				if( this.silhouetteAdapter != null ) {
					this.preSilhouette( rc );
				}
				this.renderGeometry( rc, renderType );
				if( this.silhouetteAdapter != null ) {
					this.postSilouette( rc, GL_FRONT_AND_BACK );
				}
				rc.gl.glEnable( GL_CULL_FACE );
			} else {
				//should never reach here
			}
		} else {
			if( m_frontFacingAppearanceAdapter != null ) {
				rc.gl.glCullFace( GL_BACK );
				if( this.silhouetteAdapter != null ) {
					this.preSilhouette( rc );
				}
				m_frontFacingAppearanceAdapter.setPipelineState( rc, GL_FRONT );
				this.renderGeometry( rc, renderType );
				if( this.silhouetteAdapter != null ) {
					this.postSilouette( rc, GL_FRONT );
				}

			}
			if( m_backFacingAppearanceAdapter != null ) {
				rc.gl.glCullFace( GL_FRONT );
				if( this.silhouetteAdapter != null ) {
					this.preSilhouette( rc );
				}
				m_backFacingAppearanceAdapter.setPipelineState( rc, GL_BACK );
				this.renderGeometry( rc, renderType );
				if( this.silhouetteAdapter != null ) {
					this.postSilouette( rc, GL_BACK );
				}
			}
		}

		if( m_isScaleIdentity ) {
			//pass
		} else {
			rc.decrementScaledCount();
			rc.gl.glPopMatrix();
		}
	}

	public void renderAlphaBlended( RenderContext rc ) {
		//System.out.println( "renderAlphaBlended: " + this );
		if( isActuallyShowing() ) {
			rc.gl.glPushMatrix();
			rc.gl.glMultMatrixd( accessAbsoluteTransformationAsBuffer() );
			actuallyRender( rc, RenderType.ALPHA_BLENDED );
			rc.gl.glPopMatrix();
		}
	}

	public void renderAllAlphaBlended( RenderContext rc ) {
		//System.out.println( "renderAlphaBlended: " + this );
		if( isActuallyShowing() ) {
			rc.gl.glPushMatrix();
			rc.gl.glMultMatrixd( accessAbsoluteTransformationAsBuffer() );
			actuallyRender( rc, RenderType.ALL );
			rc.gl.glPopMatrix();
		}
	}

	@Override
	public void renderOpaque( RenderContext rc ) {
		if( isActuallyShowing() ) {
			if( hasOpaque() ) {
				actuallyRender( rc, RenderType.OPAQUE );
			}
		}
	}

	@Override
	public void renderGhost( RenderContext rc, GhostAdapter root ) {
		actuallyRender( rc, RenderType.GHOST );
	}

	protected void pickGeometry( edu.cmu.cs.dennisc.render.gl.imp.PickContext pc, boolean isSubElementActuallyRequired ) {
		synchronized( m_geometryAdapters ) {
			for( int i = 0; i < m_geometryAdapters.length; i++ ) {
				pc.gl.glPushName( i );
				m_geometryAdapters[ i ].pick( pc, isSubElementActuallyRequired );
				pc.gl.glPopName();
			}
		}
	}

	@Override
	public void pick( PickContext pc, PickParameters pickParameters ) {
		if( this.m_element.isPickable.getValue() && isActuallyShowing() && ( isEthereal() == false ) ) {
			boolean isSubElementActuallyRequired = pickParameters.isSubElementRequired();

			if( isSubElementActuallyRequired ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.PickDetails pickDetails = pc.getConformanceTestResultsPickDetails();
				if( pickDetails != null ) {
					isSubElementActuallyRequired = pickDetails.isPickFunctioningCorrectly() == false;
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
				}
			}

			if( m_isScaleIdentity ) {
				//pass
			} else {
				pc.gl.glPushMatrix();
				pc.incrementScaledCount();
				pc.gl.glMultMatrixd( m_scaleBuffer );
			}

			pc.gl.glPushName( pc.getPickNameForVisualAdapter( this ) );
			pc.gl.glEnable( GL_CULL_FACE );
			if( m_backFacingAppearanceAdapter != null ) {
				pc.gl.glCullFace( GL_FRONT );
				pc.gl.glPushName( 0 );
				this.pickGeometry( pc, isSubElementActuallyRequired );
				pc.gl.glPopName();
			}
			if( m_frontFacingAppearanceAdapter != null ) {
				pc.gl.glCullFace( GL_BACK );
				pc.gl.glPushName( 1 );
				this.pickGeometry( pc, isSubElementActuallyRequired );
				pc.gl.glPopName();
			}
			pc.gl.glPopName();
			if( m_isScaleIdentity ) {
				//pass
			} else {
				pc.decrementScaledCount();
				pc.gl.glPopMatrix();
			}
		}
	}

	protected void updateGeometryAdapters() {
		GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry>[] newAdapters = AdapterFactory.getAdaptersFor( m_element.geometries.getValue(), GeometryAdapter.class );
		if( m_geometryAdapters != null )
		{
			for( GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> oldAdapter : m_geometryAdapters )
			{
				boolean found = false;
				for( GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> newAdapter : newAdapters )
				{
					if( newAdapter == oldAdapter )
					{
						found = true;
						break;
					}
				}
				if( !found )
				{
					oldAdapter.handleReleased();
				}
			}
		}
		m_geometryAdapters = newAdapters;
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.geometries ) {
			//todo: update scene observer skin vector
			updateGeometryAdapters();
		} else if( property == m_element.frontFacingAppearance ) {
			AppearanceAdapter<? extends Appearance> newAdapter = AdapterFactory.getAdapterFor( m_element.frontFacingAppearance.getValue() );
			if( m_frontFacingAppearanceAdapter != newAdapter )
			{
				if( m_frontFacingAppearanceAdapter != null )
				{
					m_frontFacingAppearanceAdapter.handleReleased();
				}
				m_frontFacingAppearanceAdapter = newAdapter;
			}

		} else if( property == m_element.backFacingAppearance ) {
			AppearanceAdapter<? extends Appearance> newAdapter = AdapterFactory.getAdapterFor( m_element.backFacingAppearance.getValue() );
			if( m_backFacingAppearanceAdapter != newAdapter )
			{
				if( m_backFacingAppearanceAdapter != null )
				{
					m_backFacingAppearanceAdapter.handleReleased();
				}
				m_backFacingAppearanceAdapter = newAdapter;
			}

		} else if( property == m_element.scale ) {
			//todo: accessScale?
			updateScale( m_element.scale.getValue() );
		} else if( property == m_element.isShowing ) {
			m_isShowing = m_element.isShowing.getValue();
		} else if( property == m_element.silouette ) {
			this.silhouetteAdapter = AdapterFactory.getAdapterFor( m_element.silouette.getValue() );
		} else {
			super.propertyChanged( property );
		}
	}

	//todo: make private?
	protected AppearanceAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Appearance> m_frontFacingAppearanceAdapter = null;
	protected AppearanceAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Appearance> m_backFacingAppearanceAdapter = null;
	protected boolean m_isShowing = false;
	private double[] m_scale = new double[ 16 ];

	protected java.nio.DoubleBuffer m_scaleBuffer = java.nio.DoubleBuffer.wrap( m_scale );
	protected boolean m_isScaleIdentity = true;
	protected GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry>[] m_geometryAdapters = null;

	private SilhouetteAdapter silhouetteAdapter;
}
