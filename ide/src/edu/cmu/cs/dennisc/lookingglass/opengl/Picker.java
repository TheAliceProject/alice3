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

/**
 * @author Dennis Cosgrove
 */
public final class Picker implements edu.cmu.cs.dennisc.lookingglass.Picker {
	private static final boolean IS_HARDWARE_ACCELERATION_DESIRED = com.sun.opengl.impl.Debug.isPropertyDefined( "jogl.gljpanel.nohw" ) == false;

	private static class ActualPicker {

		private static final int SELECTION_CAPACITY = 256;
		private final PickContext pickContext = new PickContext();
		private final java.nio.IntBuffer selectionAsIntBuffer;

		private final javax.media.opengl.GLCapabilitiesChooser glCapabilitiesChooser;
		private final javax.media.opengl.GLCapabilities glRequestedCapabilities;
		//		private final com.sun.opengl.impl.GLDrawableFactoryImpl glFactory;
		private final javax.media.opengl.GLContext glShareContext;

		private PickParameters pickParameters;

		public ActualPicker() {
			final int SIZEOF_INT = 4;
			java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocateDirect( SIZEOF_INT * SELECTION_CAPACITY );
			byteBuffer.order( java.nio.ByteOrder.nativeOrder() );
			this.selectionAsIntBuffer = byteBuffer.asIntBuffer();

			this.glRequestedCapabilities = new javax.media.opengl.GLCapabilities();
			this.glRequestedCapabilities.setDoubleBuffered( false );
			this.glCapabilitiesChooser = new javax.media.opengl.DefaultGLCapabilitiesChooser();
			//			this.glFactory = com.sun.opengl.impl.GLDrawableFactoryImpl.getFactoryImpl();
			this.glShareContext = null;
		}

		public void setPickParameters( edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int x, int y, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
			this.pickParameters = new PickParameters( lookingGlass, sgCamera, x, y, isSubElementRequired, pickObserver );
		}

		public void clearPickParameters() {
			this.pickParameters = null;
		}

		public java.util.List<edu.cmu.cs.dennisc.lookingglass.PickResult> accessAllPickResults() {
			return this.pickParameters.accessAllPickResults();
		}

		public edu.cmu.cs.dennisc.lookingglass.PickResult accessFrontMostPickResult() {
			return this.pickParameters.accessFrontMostPickResult();
		}

		private OffscreenDrawable glOffscreenDrawable;

		private synchronized OffscreenDrawable getOffscreenDrawable() {
			if( this.glOffscreenDrawable != null ) {
				//pass
			} else {
				OffscreenDrawable od = null;
				if( IS_HARDWARE_ACCELERATION_DESIRED && GlDrawableUtilities.canCreateGlPixelBuffer() ) {
					od = new PixelBufferOffscreenDrawable() {
						@Override
						protected void actuallyDisplay( javax.media.opengl.GL gl ) {
							sharedActualPicker.performPick( gl );
						}
					};
					try {
						od.initialize( glRequestedCapabilities, glCapabilitiesChooser, glShareContext, 1, 1 );
					} catch( javax.media.opengl.GLException gle ) {
						try {
							od.destroy();
						} catch( Throwable t ) {
							//pass
						}
						od = null;
					}
				}
				if( od != null ) {
					//pass
				} else {
					od = new SoftwareOffscreenDrawable() {
						@Override
						protected void actuallyDisplay( javax.media.opengl.GL gl ) {
							sharedActualPicker.performPick( gl );
						}
					};
					try {
						od.initialize( glRequestedCapabilities, glCapabilitiesChooser, glShareContext, 1, 1 );
					} catch( javax.media.opengl.GLException gle ) {
						try {
							od.destroy();
						} catch( Throwable t ) {
							//pass
						}
						od = null;
						throw gle;
					}
				}
				this.glOffscreenDrawable = od;
			}
			return this.glOffscreenDrawable;
		}

		private void performPick( javax.media.opengl.GL gl ) {
			this.pickContext.gl = gl;
			ConformanceTestResults.SINGLETON.updatePickInformationIfNecessary( GlDrawableUtilities.canCreateGlPixelBuffer(), this.glOffscreenDrawable instanceof PixelBufferOffscreenDrawable, gl );

			ConformanceTestResults.PickDetails pickDetails = ConformanceTestResults.SINGLETON.getPickDetails();

			if( pickParameters != null ) {
				edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver = pickParameters.getPickObserver();
				if( pickObserver != null ) {
					pickObserver.prePick();
					ChangeHandler.handleBufferedChanges();
				}
				edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = pickParameters.getSGCamera();
				AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor( sgCamera );

				this.selectionAsIntBuffer.rewind();
				this.pickContext.gl.glSelectBuffer( SELECTION_CAPACITY, this.selectionAsIntBuffer );

				this.pickContext.gl.glRenderMode( javax.media.opengl.GL.GL_SELECT );
				this.pickContext.gl.glInitNames();

				edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass = pickParameters.getLookingGlass();
				java.awt.Rectangle actualViewport = lookingGlass.getActualViewport( sgCamera );
				this.pickContext.gl.glViewport( actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height );
				cameraAdapter.performPick( this.pickContext, pickParameters, actualViewport );
				this.pickContext.gl.glFlush();

				this.selectionAsIntBuffer.rewind();
				int length = this.pickContext.gl.glRenderMode( javax.media.opengl.GL.GL_RENDER );

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
								public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
									return Float.compare( sbi1.getZFront(), sbi2.getZFront() );
								}
							};
						} else {
							comparator = new java.util.Comparator<SelectionBufferInfo>() {
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
						pickParameters.addPickResult( sgCamera, selectionBufferInfo.getSGVisual(), selectionBufferInfo.isFrontFacing(), selectionBufferInfo.getSGGeometry(), selectionBufferInfo.getSubElement(), selectionBufferInfo.getPointInSource() );
					}
				}
				if( pickObserver != null ) {
					pickObserver.postPick();
					ChangeHandler.handleBufferedChanges();
				}
			}
		}

		private edu.cmu.cs.dennisc.lookingglass.PickResult pickFrontMost( edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = lookingGlass.getCameraAtPixel( xPixel, yPixel );
			OffscreenDrawable impl = this.getOffscreenDrawable();
			if( impl != null ) {
				this.setPickParameters( lookingGlass, sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
				try {
					if( sgCamera != null ) {
						impl.display();
					}
					return this.accessFrontMostPickResult();
				} finally {
					this.clearPickParameters();
				}
			} else {
				return new edu.cmu.cs.dennisc.lookingglass.PickResult( sgCamera );
			}
		}

		private java.util.List<edu.cmu.cs.dennisc.lookingglass.PickResult> pickAll( edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = lookingGlass.getCameraAtPixel( xPixel, yPixel );
			OffscreenDrawable impl = this.getOffscreenDrawable();
			if( impl != null ) {
				this.setPickParameters( lookingGlass, sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
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

	private final AbstractLookingGlass lookingGlass;

	public Picker( AbstractLookingGlass lookingGlass ) {
		this.lookingGlass = lookingGlass;
	}

	public java.util.List<edu.cmu.cs.dennisc.lookingglass.PickResult> pickAll( int xPixel, int yPixel, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy pickSubElementPolicy ) {
		return this.pickAll( xPixel, yPixel, pickSubElementPolicy, null );
	}

	public java.util.List<edu.cmu.cs.dennisc.lookingglass.PickResult> pickAll( int xPixel, int yPixel, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy pickSubElementPolicy, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		synchronized( sharedActualPicker ) {
			return sharedActualPicker.pickAll( this.lookingGlass, xPixel, yPixel, pickSubElementPolicy == edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy.REQUIRED, pickObserver );
		}
	}

	public edu.cmu.cs.dennisc.lookingglass.PickResult pickFrontMost( int xPixel, int yPixel, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy pickSubElementPolicy ) {
		return this.pickFrontMost( xPixel, yPixel, pickSubElementPolicy, null );
	}

	public edu.cmu.cs.dennisc.lookingglass.PickResult pickFrontMost( int xPixel, int yPixel, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy pickSubElementPolicy, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		synchronized( sharedActualPicker ) {
			return sharedActualPicker.pickFrontMost( this.lookingGlass, xPixel, yPixel, pickSubElementPolicy == edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy.REQUIRED, pickObserver );
		}
	}
}
