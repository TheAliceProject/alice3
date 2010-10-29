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

package edu.cmu.cs.dennisc.lookingglass.opengl;

import javax.media.opengl.GL;

/**
 * @author Dennis Cosgrove
 */
public class VisualAdapter< E extends edu.cmu.cs.dennisc.scenegraph.Visual > extends LeafAdapter< E > {

	//todo: make private?
	protected AppearanceAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Appearance > m_frontFacingAppearanceAdapter = null;
	private AppearanceAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Appearance > m_backFacingAppearanceAdapter = null;
	private boolean m_isShowing = false;
	private double[] m_scale = new double[ 16 ];

	protected java.nio.DoubleBuffer m_scaleBuffer = java.nio.DoubleBuffer.wrap( m_scale );
	protected boolean m_isScaleIdentity = true;
	//protected GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > m_geometryAdapter = null;
	protected GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry >[] m_geometryAdapters = null;
	
//	public GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > getGeometryAdapter() {
//		return m_geometryAdapter;
//	}
	
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource(edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 inverseAbsoluteTransformationOfSource, int geometryIndex, int subElement) {
		if( 0 <= geometryIndex && geometryIndex < m_geometryAdapters.length ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 absoluteTransformation = this.m_element.getAbsoluteTransformation();
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createMultiplication(inverseAbsoluteTransformationOfSource, absoluteTransformation);
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
		if( m_isShowing && m_geometryAdapters != null && m_geometryAdapters.length > 0 ) {
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

	protected boolean isAlphaBlended() {
		if( m_geometryAdapters != null && m_geometryAdapters.length > 0 ) {
			synchronized( m_geometryAdapters ) {
				for( GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > geometryAdapter : m_geometryAdapters ) {
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
	public void setup( RenderContext rc ) {
		//pass
	}

	protected void actuallyRender( RenderContext rc ) {
		assert m_frontFacingAppearanceAdapter != null || m_backFacingAppearanceAdapter != null;

		if( m_isScaleIdentity ) {
			rc.gl.glDisable( GL.GL_NORMALIZE );
		} else {
			rc.gl.glPushMatrix();
			rc.gl.glMultMatrixd( m_scaleBuffer );

			//todo: what if scale is supposed to affect lighting?
			rc.gl.glEnable( GL.GL_NORMALIZE );
		}

		
//		//todo: remove
//		rc.gl.glEnable( GL.GL_NORMALIZE );
		
		if( m_frontFacingAppearanceAdapter == m_backFacingAppearanceAdapter ) {
			if( m_frontFacingAppearanceAdapter != null ) {
				m_frontFacingAppearanceAdapter.setPipelineState( rc, GL.GL_FRONT_AND_BACK );
				rc.gl.glDisable( GL.GL_CULL_FACE );
				synchronized( m_geometryAdapters ) {
					for( GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > geometryAdapter : m_geometryAdapters ) {
						geometryAdapter.render( rc );
					}
				}
				rc.gl.glEnable( GL.GL_CULL_FACE );
			} else {
				//should never reach here
			}
		} else {
			if( m_frontFacingAppearanceAdapter != null ) {
				rc.gl.glCullFace( GL.GL_BACK );
				m_frontFacingAppearanceAdapter.setPipelineState( rc, GL.GL_FRONT );
				synchronized( m_geometryAdapters ) {
					for( GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > geometryAdapter : m_geometryAdapters ) {
						geometryAdapter.render( rc );
					}
				}
			}
			if( m_backFacingAppearanceAdapter != null ) {
				rc.gl.glCullFace( GL.GL_FRONT );
				m_backFacingAppearanceAdapter.setPipelineState( rc, GL.GL_BACK );
				synchronized( m_geometryAdapters ) {
					for( GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > geometryAdapter : m_geometryAdapters ) {
						geometryAdapter.render( rc );
					}
				}
			}
		}

		if( m_isScaleIdentity ) {
			//pass
		} else {
			rc.gl.glPopMatrix();
		}
	}
	public void renderAlphaBlended( RenderContext rc ) {
		//System.out.println( "renderAlphaBlended: " + this );
		if( isActuallyShowing() ) {
			rc.gl.glPushMatrix();
			rc.gl.glMultMatrixd( accessAbsoluteTransformationAsBuffer() );
			actuallyRender( rc );
			rc.gl.glPopMatrix();
		}
	}
	@Override
	public void renderOpaque( RenderContext rc ) {
		if( isActuallyShowing() ) {
			if( isAlphaBlended() ) {
				//pass
			} else {
				actuallyRender( rc );
			}
		}
	}
	
	@Override
	public void renderGhost( RenderContext rc, GhostAdapter root ) {
		actuallyRender( rc );
	}

	@Override
	public void pick( PickContext pc, PickParameters pickParameters, ConformanceTestResults conformanceTestResults ) {
		if( isActuallyShowing() && isEthereal() == false) {
			
			boolean isSubElementActuallyRequired = pickParameters.isSubElementRequired() || conformanceTestResults.isPickFunctioningCorrectly()==false;
			
			if( m_isScaleIdentity ) {
				//pass
			} else {
				pc.gl.glPushMatrix();
				pc.gl.glMultMatrixd( m_scaleBuffer );
			}

			pc.gl.glPushName( pc.getPickNameForVisualAdapter( this ) );
			pc.gl.glEnable( GL.GL_CULL_FACE );
			if( m_backFacingAppearanceAdapter != null ) {
				pc.gl.glCullFace( GL.GL_FRONT );
				pc.gl.glPushName( 0 );
				synchronized( m_geometryAdapters ) {
					for( int i=0; i<m_geometryAdapters.length; i++ ) {
						pc.gl.glPushName( i );
						m_geometryAdapters[ i ].pick( pc, isSubElementActuallyRequired );
						pc.gl.glPopName();
					}
				}
				pc.gl.glPopName();
			}
			if( m_frontFacingAppearanceAdapter != null ) {
				pc.gl.glCullFace( GL.GL_BACK );
				pc.gl.glPushName( 1 );
				synchronized( m_geometryAdapters ) {
					for( int i=0; i<m_geometryAdapters.length; i++ ) {
						pc.gl.glPushName( i );
						m_geometryAdapters[ i ].pick( pc, isSubElementActuallyRequired );
						pc.gl.glPopName();
					}
				}
				pc.gl.glPopName();
			}
			pc.gl.glPopName();
			if( m_isScaleIdentity ) {
				//pass
			} else {
				pc.gl.glPopMatrix();
			}
		}
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.geometries ) {
			

			//todo: update scene observer skin vector

			
			m_geometryAdapters = AdapterFactory.getAdaptersFor( m_element.geometries.getValue(), GeometryAdapter.class );
		} else if( property == m_element.frontFacingAppearance ) {
			m_frontFacingAppearanceAdapter = AdapterFactory.getAdapterFor( m_element.frontFacingAppearance.getValue() );
		} else if( property == m_element.backFacingAppearance ) {
			m_backFacingAppearanceAdapter = AdapterFactory.getAdapterFor( m_element.backFacingAppearance.getValue() );
		} else if( property == m_element.scale ) {
			//todo: accessScale?
			updateScale( m_element.scale.getValue() );
		} else if( property == m_element.isShowing ) {
			m_isShowing = m_element.isShowing.getValue();
		} else {
			super.propertyChanged( property );
		}
	}
}
