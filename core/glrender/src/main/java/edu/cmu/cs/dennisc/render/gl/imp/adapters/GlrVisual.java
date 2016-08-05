/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import static com.jogamp.opengl.GL.GL_BACK;
import static com.jogamp.opengl.GL.GL_CULL_FACE;
import static com.jogamp.opengl.GL.GL_FRONT;
import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.PickParameters;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.Appearance;

/**
 * @author Dennis Cosgrove
 */
public class GlrVisual<T extends edu.cmu.cs.dennisc.scenegraph.Visual> extends GlrLeaf<T> implements edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrRenderContributor {

	public static enum RenderType {
		OPAQUE,
		ALPHA_BLENDED,
		GHOST,
		SILHOUETTE,
		ALL
	}

	//for tree node
	/* package-private */GlrAppearance<? extends edu.cmu.cs.dennisc.scenegraph.Appearance> getFrontFacingAppearanceAdapter() {
		return this.glrFrontFacingAppearance;
	}

	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 inverseAbsoluteTransformationOfSource, int geometryIndex, int subElement ) {
		if( ( 0 <= geometryIndex ) && ( geometryIndex < this.glrGeometries.length ) ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 absoluteTransformation = this.owner.getAbsoluteTransformation();
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createMultiplication( inverseAbsoluteTransformationOfSource, absoluteTransformation );
			this.glrGeometries[ geometryIndex ].getIntersectionInSource( rv, ray, m, subElement );
		}
		return rv;
	}

	private boolean isEthereal() {
		if( this.glrFrontFacingAppearance != null ) {
			if( this.glrFrontFacingAppearance.isEthereal() ) {
				//pass
			} else {
				return false;
			}
		}
		if( this.glrBackFacingAppearance != null ) {
			if( this.glrBackFacingAppearance.isEthereal() ) {
				//pass
			} else {
				return false;
			}
		}
		return true;
	}

	protected boolean isActuallyShowing() {
		if( this.isShowing && ( this.glrGeometries != null ) && ( this.glrGeometries.length > 0 ) ) {
			if( this.glrFrontFacingAppearance != null ) {
				if( this.glrFrontFacingAppearance.isActuallyShowing() ) {
					return true;
				}
			}
			if( this.glrBackFacingAppearance != null ) {
				if( this.glrBackFacingAppearance.isActuallyShowing() ) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean hasOpaque() {
		if( ( this.glrGeometries != null ) && ( this.glrGeometries.length > 0 ) ) {
			if( this.glrFrontFacingAppearance != null ) {
				if( this.glrFrontFacingAppearance.isAlphaBlended() ) {
					return false;
				}
			}
			if( this.glrBackFacingAppearance != null ) {
				if( this.glrBackFacingAppearance.isAlphaBlended() ) {
					return false;
				}
			}

			synchronized( this.glrGeometries ) {
				for( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : this.glrGeometries ) {
					if( geometryAdapter.hasOpaque() ) {
						return true;
					}
				}
			}

		}
		return false;
	}

	protected boolean isAllAlpha() {
		if( this.glrFrontFacingAppearance != null ) {
			if( this.glrFrontFacingAppearance.isAllAlphaBlended() ) {
				return true;
			}
		}
		if( this.glrBackFacingAppearance != null ) {
			if( this.glrBackFacingAppearance.isAllAlphaBlended() ) {
				return true;
			}
		}
		return false;
	}

	protected boolean isAlphaBlended() {
		if( ( this.glrGeometries != null ) && ( this.glrGeometries.length > 0 ) ) {
			synchronized( this.glrGeometries ) {
				for( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : this.glrGeometries ) {
					if( geometryAdapter.isAlphaBlended() ) {
						return true;
					}
				}
			}
			if( this.glrFrontFacingAppearance != null ) {
				if( this.glrFrontFacingAppearance.isAlphaBlended() ) {
					return true;
				}
			}
			if( this.glrBackFacingAppearance != null ) {
				if( this.glrBackFacingAppearance.isAlphaBlended() ) {
					return true;
				}
			}
		}
		return false;
	}

	private void updateScale( edu.cmu.cs.dennisc.math.Matrix3x3 m ) {
		this.isScaleIdentity = m.isIdentity();
		m.getAsColumnMajorArray16( this.scale );
	}

	@Override
	protected void handleReleased()
	{
		super.handleReleased();
		synchronized( this.glrGeometries ) {
			for( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : this.glrGeometries ) {
				if( geometryAdapter.owner != null )
				{
					geometryAdapter.handleReleased();
				}
			}
			this.glrGeometries = null;
		}
		if( ( this.glrFrontFacingAppearance != null ) && ( this.glrFrontFacingAppearance.owner != null ) )
		{
			this.glrFrontFacingAppearance.handleReleased();
		}
		this.glrFrontFacingAppearance = null;
		if( ( this.glrBackFacingAppearance != null ) && ( this.glrBackFacingAppearance.owner != null ) )
		{
			this.glrBackFacingAppearance.handleReleased();
		}
		this.glrBackFacingAppearance = null;
	}

	protected void renderGeometry( edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc, RenderType renderType ) {
		synchronized( this.glrGeometries ) {
			for( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : this.glrGeometries ) {
				geometryAdapter.render( rc, renderType );
			}
		}
	}

	private void preSilhouette( RenderContext rc ) {
		rc.gl.glClearStencil( 0 );
		rc.gl.glClear( com.jogamp.opengl.GL.GL_STENCIL_BUFFER_BIT );
		rc.gl.glEnable( com.jogamp.opengl.GL.GL_STENCIL_TEST );
		rc.gl.glStencilFunc( com.jogamp.opengl.GL.GL_ALWAYS, 1, -1 );
		rc.gl.glStencilOp( com.jogamp.opengl.GL.GL_KEEP, com.jogamp.opengl.GL.GL_KEEP, com.jogamp.opengl.GL.GL_REPLACE );
	}

	private void postSilouette( RenderContext rc, int face ) {
		rc.gl.glStencilFunc( com.jogamp.opengl.GL2.GL_NOTEQUAL, 1, -1 );
		rc.gl.glStencilOp( com.jogamp.opengl.GL.GL_KEEP, com.jogamp.opengl.GL.GL_KEEP, com.jogamp.opengl.GL.GL_REPLACE );

		this.silhouetteAdapter.setup( rc, face );
		this.renderGeometry( rc, RenderType.SILHOUETTE );
		rc.gl.glDisable( com.jogamp.opengl.GL.GL_STENCIL_TEST );
		rc.gl.glLineWidth( 1.0f );
	}

	protected void actuallyRender( RenderContext rc, RenderType renderType ) {
		assert ( this.glrFrontFacingAppearance != null ) || ( this.glrBackFacingAppearance != null );

		if( this.isScaleIdentity ) {
			//pass
		} else {
			rc.gl.glPushMatrix();
			rc.gl.glMultMatrixd( this.scaleBuffer );
			rc.incrementScaledCount();
		}
		if( this.glrFrontFacingAppearance == this.glrBackFacingAppearance ) {
			if( this.glrFrontFacingAppearance != null ) {
				this.glrFrontFacingAppearance.setPipelineState( rc, GL_FRONT_AND_BACK );
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
			if( this.glrFrontFacingAppearance != null ) {
				rc.gl.glCullFace( GL_BACK );
				if( this.silhouetteAdapter != null ) {
					this.preSilhouette( rc );
				}
				this.glrFrontFacingAppearance.setPipelineState( rc, GL_FRONT );
				this.renderGeometry( rc, renderType );
				if( this.silhouetteAdapter != null ) {
					this.postSilouette( rc, GL_FRONT );
				}

			}
			if( this.glrBackFacingAppearance != null ) {
				rc.gl.glCullFace( GL_FRONT );
				if( this.silhouetteAdapter != null ) {
					this.preSilhouette( rc );
				}
				this.glrBackFacingAppearance.setPipelineState( rc, GL_BACK );
				this.renderGeometry( rc, renderType );
				if( this.silhouetteAdapter != null ) {
					this.postSilouette( rc, GL_BACK );
				}
			}
		}

		if( this.isScaleIdentity ) {
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
	public void renderGhost( RenderContext rc, GlrGhost root ) {
		actuallyRender( rc, RenderType.GHOST );
	}

	protected void pickGeometry( edu.cmu.cs.dennisc.render.gl.imp.PickContext pc, boolean isSubElementActuallyRequired ) {
		synchronized( this.glrGeometries ) {
			for( int i = 0; i < this.glrGeometries.length; i++ ) {
				pc.gl.glPushName( i );
				this.glrGeometries[ i ].pick( pc, isSubElementActuallyRequired );
				pc.gl.glPopName();
			}
		}
	}

	@Override
	public void pick( PickContext pc, PickParameters pickParameters ) {
		if( this.owner.isPickable.getValue() && isActuallyShowing() && ( isEthereal() == false ) ) {
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

			if( this.isScaleIdentity ) {
				//pass
			} else {
				pc.gl.glPushMatrix();
				pc.incrementScaledCount();
				pc.gl.glMultMatrixd( this.scaleBuffer );
			}

			pc.gl.glPushName( pc.getPickNameForVisualAdapter( this ) );
			pc.gl.glEnable( GL_CULL_FACE );
			if( this.glrBackFacingAppearance != null ) {
				pc.gl.glCullFace( GL_FRONT );
				pc.gl.glPushName( 0 );
				this.pickGeometry( pc, isSubElementActuallyRequired );
				pc.gl.glPopName();
			}
			if( this.glrFrontFacingAppearance != null ) {
				pc.gl.glCullFace( GL_BACK );
				pc.gl.glPushName( 1 );
				this.pickGeometry( pc, isSubElementActuallyRequired );
				pc.gl.glPopName();
			}
			pc.gl.glPopName();
			if( this.isScaleIdentity ) {
				//pass
			} else {
				pc.decrementScaledCount();
				pc.gl.glPopMatrix();
			}
		}
	}

	protected void updateGeometryAdapters() {
		GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry>[] newAdapters = AdapterFactory.getAdaptersFor( owner.geometries.getValue(), GlrGeometry.class );
		if( this.glrGeometries != null )
		{
			for( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> oldAdapter : this.glrGeometries )
			{
				boolean found = false;
				for( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> newAdapter : newAdapters )
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
		this.glrGeometries = newAdapters;
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.geometries ) {
			//todo: update scene observer skin vector
			updateGeometryAdapters();
		} else if( property == owner.frontFacingAppearance ) {
			GlrAppearance<? extends Appearance> newAdapter = AdapterFactory.getAdapterFor( owner.frontFacingAppearance.getValue() );
			if( this.glrFrontFacingAppearance != newAdapter )
			{
				if( this.glrFrontFacingAppearance != null )
				{
					this.glrFrontFacingAppearance.handleReleased();
				}
				this.glrFrontFacingAppearance = newAdapter;
			}

		} else if( property == owner.backFacingAppearance ) {
			GlrAppearance<? extends Appearance> newAdapter = AdapterFactory.getAdapterFor( owner.backFacingAppearance.getValue() );
			if( this.glrBackFacingAppearance != newAdapter )
			{
				if( this.glrBackFacingAppearance != null )
				{
					this.glrBackFacingAppearance.handleReleased();
				}
				this.glrBackFacingAppearance = newAdapter;
			}

		} else if( property == owner.scale ) {
			//todo: accessScale?
			updateScale( owner.scale.getValue() );
		} else if( property == owner.isShowing ) {
			this.isShowing = owner.isShowing.getValue();
		} else if( property == owner.silouette ) {
			this.silhouetteAdapter = AdapterFactory.getAdapterFor( owner.silouette.getValue() );
		} else {
			super.propertyChanged( property );
		}
	}

	//todo: make private?
	protected GlrAppearance<?> glrFrontFacingAppearance = null;
	protected GlrAppearance<?> glrBackFacingAppearance = null;
	protected boolean isShowing = false;

	private final double[] scale = new double[ 16 ];
	private final java.nio.DoubleBuffer scaleBuffer = java.nio.DoubleBuffer.wrap( this.scale );
	private boolean isScaleIdentity = true;

	protected GlrGeometry<?>[] glrGeometries = null;

	private GlrSilhouette silhouetteAdapter;
}
