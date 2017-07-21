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

import static com.jogamp.opengl.GL.GL_ALWAYS;
import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_CCW;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_CW;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_EQUAL;
import static com.jogamp.opengl.GL.GL_KEEP;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_REPLACE;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_STENCIL_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_STENCIL_TEST;
import static com.jogamp.opengl.GL2ES1.GL_CLIP_PLANE0;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;

import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public class GlrScene extends GlrComposite<edu.cmu.cs.dennisc.scenegraph.Scene> {
	@Override
	public void initialize( edu.cmu.cs.dennisc.scenegraph.Scene sgElement ) {
		super.initialize( sgElement );
		for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : edu.cmu.cs.dennisc.pattern.VisitUtilities.getAll( owner, edu.cmu.cs.dennisc.scenegraph.Component.class ) ) {
			GlrComponent<?> glrComponent = AdapterFactory.getAdapterFor( sgComponent );
			this.addDescendant( glrComponent );
		}
	}

	public GlrBackground getBackgroundAdapter() {
		return this.backgroundAdapter;
	}

	protected void addDescendant( GlrComponent<?> glrDescendant ) {
		if( glrDescendant instanceof GlrGhost ) {
			synchronized( this.glrGhostDescendants ) {
				this.glrGhostDescendants.add( (GlrGhost)glrDescendant );
			}
		} else if( glrDescendant instanceof GlrVisual<?> ) {
			synchronized( this.glrVisualDescendants ) {
				this.glrVisualDescendants.add( (GlrVisual<?>)glrDescendant );
			}
			if( glrDescendant instanceof GlrPlanarReflector ) {
				synchronized( this.glrPlanarReflectorDescendants ) {
					this.glrPlanarReflectorDescendants.add( (GlrPlanarReflector)glrDescendant );
				}
			}
		}
	}

	protected void removeDescendant( GlrComponent<?> glrDescendant ) {
		if( glrDescendant instanceof GlrGhost ) {
			synchronized( this.glrGhostDescendants ) {
				this.glrGhostDescendants.remove( glrDescendant );
			}
		} else if( glrDescendant instanceof GlrVisual<?> ) {
			synchronized( this.glrVisualDescendants ) {
				this.glrVisualDescendants.remove( glrDescendant );
			}
			if( glrDescendant instanceof GlrPlanarReflector ) {
				synchronized( this.glrPlanarReflectorDescendants ) {
					this.glrPlanarReflectorDescendants.remove( glrDescendant );
				}
			}
		}
	}

	@Deprecated
	public void EPIC_HACK_FOR_THUMBNAIL_MAKER_removeDescendant( GlrComponent<?> glrDescendant ) {
		this.removeDescendant( glrDescendant );
	}

	public void renderAlphaBlended( RenderContext rc ) {
		// todo depth sort
		//rc.gl.glDisable( GL_DEPTH_TEST );
		//		rc.gl.glDepthMask( false );
		//		try {
		synchronized( this.glrGhostDescendants ) {
			for( GlrGhost ghostAdapter : this.glrGhostDescendants ) {
				ghostAdapter.renderGhost( rc, ghostAdapter );
			}
		}
		synchronized( this.glrVisualDescendants ) {
			for( GlrVisual<? extends edu.cmu.cs.dennisc.scenegraph.Visual> visualAdapter : this.glrVisualDescendants ) {
				if( visualAdapter.isAlphaBlended() ) {
					//todo: adapters should be removed
					if( ( visualAdapter.owner != null ) && ( visualAdapter.owner.getRoot() instanceof edu.cmu.cs.dennisc.scenegraph.Scene ) ) {
						if( visualAdapter.isAllAlpha() ) {
							visualAdapter.renderAllAlphaBlended( rc );
						} else {
							visualAdapter.renderAlphaBlended( rc );
						}
					}
				}
			}
		}
		//		} finally {
		//			rc.gl.glDepthMask( true );
		//		}
		//rc.gl.glEnable( GL_DEPTH_TEST );
	}

	@Override
	public void setupAffectors( RenderContext rc ) {
		rc.setGlobalBrightness( this.globalBrightness );
		rc.beginAffectorSetup();
		super.setupAffectors( rc );
		rc.endAffectorSetup();
	}

	private void renderScene( RenderContext rc ) {
		rc.gl.glDisable( GL_BLEND );
		renderOpaque( rc );
		rc.gl.glEnable( GL_BLEND );
		rc.gl.glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
		renderAlphaBlended( rc );
		rc.gl.glDisable( GL_BLEND );
	}

	public void renderScene( RenderContext rc, GlrAbstractCamera<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter, GlrBackground backgroundAdapter ) {
		rc.gl.glMatrixMode( GL_MODELVIEW );
		synchronized( cameraAdapter ) {
			rc.gl.glLoadMatrixd( cameraAdapter.accessInverseAbsoluteTransformationAsBuffer() );
		}

		if( backgroundAdapter != null ) {
			//pass
		} else {
			backgroundAdapter = this.backgroundAdapter;
		}
		if( backgroundAdapter != null ) {
			backgroundAdapter.setup( rc );
		}

		if( this.glrPlanarReflectorDescendants.size() > 0 ) {
			GlrPlanarReflector planarReflectorAdapter = this.glrPlanarReflectorDescendants.get( 0 );
			if( planarReflectorAdapter.isFacing( cameraAdapter ) ) {
				rc.gl.glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT );
				rc.gl.glColorMask( false, false, false, false );
				rc.gl.glEnable( GL_STENCIL_TEST );
				rc.gl.glStencilFunc( GL_ALWAYS, 1, 1 );
				rc.gl.glStencilOp( GL_KEEP, GL_KEEP, GL_REPLACE );
				rc.gl.glDisable( GL_DEPTH_TEST );
				planarReflectorAdapter.renderStencil( rc, GlrVisual.RenderType.OPAQUE );
				rc.gl.glEnable( GL_DEPTH_TEST );
				rc.gl.glColorMask( true, true, true, true );
				rc.gl.glStencilFunc( GL_EQUAL, 1, 1 );
				rc.gl.glStencilOp( GL_KEEP, GL_KEEP, GL_KEEP );
				rc.gl.glEnable( GL_CLIP_PLANE0 );
				rc.gl.glPushMatrix();
				planarReflectorAdapter.applyReflection( rc );
				rc.gl.glFrontFace( GL_CW );
				setupAffectors( rc );
				renderScene( rc );
				rc.gl.glFrontFace( GL_CCW );
				rc.gl.glPopMatrix();
				rc.gl.glDisable( GL_CLIP_PLANE0 );
				rc.gl.glDisable( GL_STENCIL_TEST );
				setupAffectors( rc );
				rc.gl.glEnable( GL_BLEND );
				rc.gl.glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
				planarReflectorAdapter.renderStencil( rc, GlrVisual.RenderType.ALPHA_BLENDED );
				rc.gl.glDisable( GL_BLEND );
			} else {
				rc.gl.glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
				setupAffectors( rc );
			}
		} else {
			rc.gl.glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
			setupAffectors( rc );
		}
		renderScene( rc );
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.background ) {
			this.backgroundAdapter = AdapterFactory.getAdapterFor( owner.background.getValue() );
		} else if( property == owner.globalBrightness ) {
			this.globalBrightness = owner.globalBrightness.getValue();
		} else {
			super.propertyChanged( property );
		}
	}

	private GlrBackground backgroundAdapter;
	private float globalBrightness;
	private final java.util.List<GlrGhost> glrGhostDescendants = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private final java.util.List<GlrVisual<?>> glrVisualDescendants = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private final java.util.List<GlrPlanarReflector> glrPlanarReflectorDescendants = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private boolean isInitialized = false;
}
