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

package edu.cmu.cs.dennisc.render.gl.imp;

import edu.cmu.cs.dennisc.render.gl.GlDrawableUtils;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.ChangeHandler;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrAbstractCamera;
import edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults;

/**
 * @author Dennis Cosgrove
 */
public final class SynchronousPicker implements edu.cmu.cs.dennisc.render.SynchronousPicker {
	private static class ActualPicker {
		private static final int SELECTION_CAPACITY = 256;
		private final PickContext pickContext = new PickContext( true );
		private final java.nio.IntBuffer selectionAsIntBuffer;

		private final com.jogamp.opengl.GLCapabilitiesChooser glCapabilitiesChooser;
		private final com.jogamp.opengl.GLCapabilities glRequestedCapabilities;
		private final com.jogamp.opengl.GLContext glShareContext;

		private PickParameters pickParameters;

		public ActualPicker() {
			final int SIZEOF_INT = 4;
			java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocateDirect( SIZEOF_INT * SELECTION_CAPACITY );
			byteBuffer.order( java.nio.ByteOrder.nativeOrder() );
			this.selectionAsIntBuffer = byteBuffer.asIntBuffer();

			com.jogamp.opengl.GLProfile glProfile = com.jogamp.opengl.GLProfile.getDefault();
			this.glRequestedCapabilities = new com.jogamp.opengl.GLCapabilities( glProfile );
			this.glRequestedCapabilities.setDoubleBuffered( false );
			this.glCapabilitiesChooser = new com.jogamp.opengl.DefaultGLCapabilitiesChooser();
			this.glShareContext = null;
		}

		public void setPickParameters( edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int x, int y, boolean isSubElementRequired, edu.cmu.cs.dennisc.render.PickObserver pickObserver ) {
			this.pickParameters = new PickParameters( renderTarget, sgCamera, x, y, isSubElementRequired, pickObserver );
		}

		public void clearPickParameters() {
			this.pickParameters = null;
		}

		public java.util.List<edu.cmu.cs.dennisc.render.PickResult> accessAllPickResults() {
			return this.pickParameters.accessAllPickResults();
		}

		public edu.cmu.cs.dennisc.render.PickResult accessFrontMostPickResult() {
			return this.pickParameters.accessFrontMostPickResult();
		}

		private OffscreenDrawable glOffscreenDrawable;

		private synchronized OffscreenDrawable getOffscreenDrawable() {
			if( this.glOffscreenDrawable != null ) {
				//pass
			} else {
				this.glOffscreenDrawable = OffscreenDrawable.createInstance( new OffscreenDrawable.DisplayCallback() {
					@Override
					public void display( com.jogamp.opengl.GL2 gl ) {
						sharedActualPicker.performPick( gl );
					}
				}, glRequestedCapabilities, glCapabilitiesChooser, glShareContext, 1, 1 );
			}
			return this.glOffscreenDrawable;
		}

		private void performPick( com.jogamp.opengl.GL2 gl ) {
			this.pickContext.gl = gl;
			ConformanceTestResults.SINGLETON.updateSynchronousPickInformationIfNecessary( gl, GlDrawableUtils.canCreateGlPixelBuffer(), this.glOffscreenDrawable instanceof PixelBufferOffscreenDrawable );

			ConformanceTestResults.SynchronousPickDetails pickDetails = ConformanceTestResults.SINGLETON.getSynchronousPickDetails();

			if( pickParameters != null ) {
				edu.cmu.cs.dennisc.render.PickObserver pickObserver = pickParameters.getPickObserver();
				if( pickObserver != null ) {
					pickObserver.prePick();
				}
				ChangeHandler.handleBufferedChanges();
				edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = pickParameters.getSGCamera();
				GlrAbstractCamera<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor( sgCamera );

				this.selectionAsIntBuffer.rewind();
				this.pickContext.gl.glSelectBuffer( SELECTION_CAPACITY, this.selectionAsIntBuffer );

				this.pickContext.gl.glRenderMode( com.jogamp.opengl.GL2.GL_SELECT );
				this.pickContext.gl.glInitNames();

				edu.cmu.cs.dennisc.render.RenderTarget renderTarget = pickParameters.getRenderTarget();
				java.awt.Rectangle actualViewport = renderTarget.getActualViewportAsAwtRectangle( sgCamera );
				this.pickContext.gl.glViewport( actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height );
				cameraAdapter.performPick( this.pickContext, pickParameters, actualViewport );
				this.pickContext.gl.glFlush();

				this.selectionAsIntBuffer.rewind();
				int length = this.pickContext.gl.glRenderMode( com.jogamp.opengl.GL2.GL_RENDER );
				//todo: invesigate negative length
				//assert length >= 0;

				if( length > 0 ) {
					SelectionBufferInfo[] selectionBufferInfos = new SelectionBufferInfo[ length ];
					int offset = 0;
					for( int i = 0; i < length; i++ ) {
						selectionBufferInfos[ i ] = new SelectionBufferInfo( this.pickContext, this.selectionAsIntBuffer, offset );
						offset += 7;
					}

					if( pickDetails.isPickFunctioningCorrectly() ) {
						double x = pickParameters.getX();
						double y = pickParameters.getFlippedY( actualViewport );

						edu.cmu.cs.dennisc.math.Matrix4x4 m = new edu.cmu.cs.dennisc.math.Matrix4x4();
						m.translation.set( actualViewport.width - ( 2 * ( x - actualViewport.x ) ), actualViewport.height - ( 2 * ( y - actualViewport.y ) ), 0, 1 );
						edu.cmu.cs.dennisc.math.ScaleUtilities.applyScale( m, actualViewport.width, actualViewport.height, 1.0 );

						edu.cmu.cs.dennisc.math.Matrix4x4 p = new edu.cmu.cs.dennisc.math.Matrix4x4();
						cameraAdapter.getActualProjectionMatrix( p, actualViewport );

						m.applyMultiplication( p );
						m.invert();
						for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
							selectionBufferInfo.updatePointInSource( m );
						}
					} else {
						edu.cmu.cs.dennisc.math.Ray ray = new edu.cmu.cs.dennisc.math.Ray();
						ray.setNaN();
						cameraAdapter.getRayAtPixel( ray, pickParameters.getX(), pickParameters.getY(), actualViewport );
						ray.accessDirection().normalize();
						edu.cmu.cs.dennisc.math.AffineMatrix4x4 inverseAbsoluteTransformation = sgCamera.getInverseAbsoluteTransformation();
						for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
							selectionBufferInfo.updatePointInSource( ray, inverseAbsoluteTransformation );
						}
					}

					if( length > 1 ) {
						//				float front0 = selectionBufferInfos[ 0 ].getZFront();
						//				boolean isDifferentiated = false;
						//				for( int i=1; i<length; i++ ) {
						//					if( front0 == selectionBufferInfos[ i ].getZFront() ) {
						//						//pass
						//					} else {
						//						isDifferentiated = true;
						//						break;
						//					}
						//				}
						//				java.util.Comparator< SelectionBufferInfo > comparator;
						//				if( isDifferentiated ) {
						//					comparator = new java.util.Comparator< SelectionBufferInfo >() {
						//						public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
						//							return Float.compare( sbi1.getZFront(), sbi2.getZFront() );
						//						}
						//					};
						//				} else {
						//					if( conformanceTestResults.isPickFunctioningCorrectly() ) {
						//						edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: conformance test reports pick is functioning correctly" );
						//						comparator = null;
						//					} else {
						//						edu.cmu.cs.dennisc.math.Ray ray = new edu.cmu.cs.dennisc.math.Ray();
						//						ray.setNaN();
						//						cameraAdapter.getRayAtPixel( ray, pickParameters.getX(), pickParameters.getY(), actualViewport);
						//						for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
						//							selectionBufferInfo.updatePointInSource( ray );
						//						}
						//						comparator = new java.util.Comparator< SelectionBufferInfo >() {
						//							public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
						//								return Double.compare( sbi1.getPointInSource().z, sbi2.getPointInSource().z );
						//							}
						//						};
						//					}
						//				}
						java.util.Comparator<SelectionBufferInfo> comparator;
						if( pickDetails.isPickFunctioningCorrectly() ) {
							comparator = new java.util.Comparator<SelectionBufferInfo>() {
								@Override
								public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
									return Float.compare( sbi1.getZFront(), sbi2.getZFront() );
								}
							};
						} else {
							comparator = new java.util.Comparator<SelectionBufferInfo>() {
								@Override
								public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
									double z1 = -sbi1.getPointInSource().z;
									double z2 = -sbi2.getPointInSource().z;
									return Double.compare( z1, z2 );
								}
							};
						}
						java.util.Arrays.sort( selectionBufferInfos, comparator );
					}
					for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
						pickParameters.addPickResult( sgCamera, selectionBufferInfo.getSgVisual(), selectionBufferInfo.isFrontFacing(), selectionBufferInfo.getSGGeometry(), selectionBufferInfo.getSubElement(), selectionBufferInfo.getPointInSource() );
					}
				}
				if( pickObserver != null ) {
					pickObserver.postPick();
				}
				ChangeHandler.handleBufferedChanges();
			}
		}

		private edu.cmu.cs.dennisc.render.PickResult pickFrontMost( RenderTargetImp rtImp, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.render.PickObserver pickObserver ) {
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = rtImp.getCameraAtPixel( xPixel, yPixel );
			OffscreenDrawable impl = this.getOffscreenDrawable();
			if( impl != null ) {
				this.setPickParameters( rtImp.getRenderTarget(), sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
				try {
					if( sgCamera != null ) {
						impl.display();
					}
					return this.accessFrontMostPickResult();
				} finally {
					this.clearPickParameters();
				}
			} else {
				return new edu.cmu.cs.dennisc.render.PickResult( sgCamera );
			}
		}

		private java.util.List<edu.cmu.cs.dennisc.render.PickResult> pickAll( RenderTargetImp rtImp, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.render.PickObserver pickObserver ) {
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = rtImp.getCameraAtPixel( xPixel, yPixel );
			OffscreenDrawable impl = this.getOffscreenDrawable();
			if( impl != null ) {
				this.setPickParameters( rtImp.getRenderTarget(), sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
				try {
					if( sgCamera != null ) {
						impl.display();
					}
					return this.accessAllPickResults();
				} finally {
					this.clearPickParameters();
				}
			} else {
				return java.util.Collections.emptyList();
			}
		}
	}

	private static ActualPicker sharedActualPicker = new ActualPicker();

	public SynchronousPicker( RenderTargetImp rtImp ) {
		this.rtImp = rtImp;
	}

	@Override
	public java.util.List<edu.cmu.cs.dennisc.render.PickResult> pickAll( int xPixel, int yPixel, edu.cmu.cs.dennisc.render.PickSubElementPolicy pickSubElementPolicy ) {
		return this.pickAll( xPixel, yPixel, pickSubElementPolicy, null );
	}

	@Override
	public java.util.List<edu.cmu.cs.dennisc.render.PickResult> pickAll( int xPixel, int yPixel, edu.cmu.cs.dennisc.render.PickSubElementPolicy pickSubElementPolicy, edu.cmu.cs.dennisc.render.PickObserver pickObserver ) {
		synchronized( sharedActualPicker ) {
			return sharedActualPicker.pickAll( this.rtImp, xPixel, yPixel, pickSubElementPolicy == edu.cmu.cs.dennisc.render.PickSubElementPolicy.REQUIRED, pickObserver );
		}
	}

	@Override
	public edu.cmu.cs.dennisc.render.PickResult pickFrontMost( int xPixel, int yPixel, edu.cmu.cs.dennisc.render.PickSubElementPolicy pickSubElementPolicy ) {
		return this.pickFrontMost( xPixel, yPixel, pickSubElementPolicy, null );
	}

	@Override
	public edu.cmu.cs.dennisc.render.PickResult pickFrontMost( int xPixel, int yPixel, edu.cmu.cs.dennisc.render.PickSubElementPolicy pickSubElementPolicy, edu.cmu.cs.dennisc.render.PickObserver pickObserver ) {
		synchronized( sharedActualPicker ) {
			return sharedActualPicker.pickFrontMost( this.rtImp, xPixel, yPixel, pickSubElementPolicy == edu.cmu.cs.dennisc.render.PickSubElementPolicy.REQUIRED, pickObserver );
		}
	}

	private final RenderTargetImp rtImp;
}
