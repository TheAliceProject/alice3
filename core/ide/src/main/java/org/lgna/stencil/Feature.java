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
package org.lgna.stencil;

/**
 * @author Dennis Cosgrove
 */
public abstract class Feature {
	private static final int ARROW_HEAD_LENGTH = 19;
	private static final double ARROW_HEAD_LENGTH_SQUARED = ARROW_HEAD_LENGTH * ARROW_HEAD_LENGTH;
	private static final int ARROW_HEAD_HALF_HEIGHT = 6;

	public enum ConnectionPreference {
		NORTH_SOUTH,
		EAST_WEST
	}

	public enum Connection {
		NORTH( javax.swing.SwingConstants.CENTER, javax.swing.SwingConstants.LEADING, false ),
		SOUTH( javax.swing.SwingConstants.CENTER, javax.swing.SwingConstants.TRAILING, false ),
		EAST( javax.swing.SwingConstants.TRAILING, javax.swing.SwingConstants.CENTER, true ),
		WEST( javax.swing.SwingConstants.LEADING, javax.swing.SwingConstants.CENTER, true );
		private int xConstraint;
		private int yConstraint;
		boolean isCurveDesired;

		private Connection( int xConstraint, int yConstraint, boolean isCurveDesired ) {
			this.xConstraint = xConstraint;
			this.yConstraint = yConstraint;
			this.isCurveDesired = isCurveDesired;
		}

		public java.awt.Point getPoint( java.awt.Rectangle bounds ) {
			java.awt.Point rv = edu.cmu.cs.dennisc.java.awt.RectangleUtilities.getPoint( bounds, this.xConstraint, this.yConstraint );
			if( this.xConstraint == javax.swing.SwingConstants.CENTER ) {
				rv.x = Math.min( rv.x, bounds.x + 128 );
			}
			return rv;
		}

		public boolean isCurveDesired() {
			return this.isCurveDesired;
		}
	}

	private static final java.awt.Stroke ARROW_STROKE = new java.awt.BasicStroke( 3.0f );

	private org.lgna.croquet.resolvers.RuntimeResolver<? extends org.lgna.croquet.views.TrackableShape> trackableShapeResolver;
	private ConnectionPreference connectionPreference;
	private Integer heightConstraint = null;
	private boolean isEntered = false;

	public Feature( org.lgna.croquet.resolvers.RuntimeResolver<? extends org.lgna.croquet.views.TrackableShape> trackableShapeResolver, ConnectionPreference connectionPreference ) {
		//assert trackableShape != null;
		this.trackableShapeResolver = trackableShapeResolver;
		this.connectionPreference = connectionPreference;
	}

	private static final String IS_GOOD_TO_GO = null;

	public static boolean isGoodToGo( String status ) {
		return status == IS_GOOD_TO_GO;
	}

	public String getStatus() {
		org.lgna.croquet.views.TrackableShape trackableShape = this.trackableShapeResolver.getResolved();
		if( trackableShape != null ) {
			return IS_GOOD_TO_GO;//trackableShape.isInView();
		} else {
			//edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.trackableShapeResolver );
			return "cannot resolve " + this.trackableShapeResolver;
		}
	}

	protected abstract boolean isPathRenderingDesired();

	public java.awt.Rectangle getBoundsForRepaint( org.lgna.croquet.views.AwtComponentView<?> asSeenBy ) {
		org.lgna.croquet.views.TrackableShape trackableShape = this.getTrackableShape();
		if( trackableShape != null ) {
			java.awt.Insets boundsInsets = this.getBoundsInsets();
			if( boundsInsets != null ) {
				java.awt.Shape shape = trackableShape.getShape( asSeenBy, boundsInsets );
				if( shape != null ) {
					return shape.getBounds();
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public void setHeightConstraint( Integer heightConstraint ) {
		this.heightConstraint = heightConstraint;
	}

	private static void repaintAll() {
		org.lgna.croquet.Application.getActiveInstance().getDocumentFrame().getFrame().getContentPane().repaint();
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "repaintAll" );
	}

	private java.awt.event.HierarchyBoundsListener hierarchyBoundsListener = new java.awt.event.HierarchyBoundsListener() {
		@Override
		public void ancestorMoved( java.awt.event.HierarchyEvent e ) {
			repaintAll();
		}

		@Override
		public void ancestorResized( java.awt.event.HierarchyEvent e ) {
			repaintAll();
		}
	};
	private java.awt.event.ComponentListener componentListener = new java.awt.event.ComponentListener() {
		@Override
		public void componentShown( java.awt.event.ComponentEvent e ) {
		}

		@Override
		public void componentHidden( java.awt.event.ComponentEvent e ) {
		}

		@Override
		public void componentMoved( java.awt.event.ComponentEvent e ) {
			repaintAll();
		}

		@Override
		public void componentResized( java.awt.event.ComponentEvent e ) {
			repaintAll();
		}
	};

	protected org.lgna.croquet.resolvers.RuntimeResolver<? extends org.lgna.croquet.views.TrackableShape> getTrackableShapeResolver() {
		return this.trackableShapeResolver;
	}

	private org.lgna.croquet.views.TrackableShape trackableShape;

	public void updateTrackableShapeIfNecessary() {
		org.lgna.croquet.views.TrackableShape nextTrackableShape = this.trackableShapeResolver.getResolved();
		if( nextTrackableShape != this.trackableShape ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.info( "trackableShape change" );
			if( this.trackableShape != null ) {
				this.trackableShape.removeHierarchyBoundsListener( this.hierarchyBoundsListener );
				this.trackableShape.removeComponentListener( this.componentListener );
			}
			this.trackableShape = nextTrackableShape;
			if( this.trackableShape != null ) {
				this.trackableShape.addComponentListener( this.componentListener );
				this.trackableShape.addHierarchyBoundsListener( this.hierarchyBoundsListener );
			}
		}
	}

	public void bind() {
		this.updateTrackableShapeIfNecessary();
	}

	public void unbind() {
		this.trackableShape = null;
	}

	public org.lgna.croquet.views.TrackableShape getTrackableShape() {
		return this.trackableShape;
	}

	//todo: move this to TrackableShape?
	public boolean isPotentiallyScrollable() {
		return true;
	}

	public boolean isEntered() {
		return this.isEntered;
	}

	public void setEntered( boolean isEntered ) {
		this.isEntered = isEntered;
	}

	private static java.awt.Shape constrainHeightIfNecessary( java.awt.Shape shape, Integer heightConstraint ) {
		if( heightConstraint != null ) {
			if( shape instanceof java.awt.geom.Rectangle2D ) {
				java.awt.geom.Rectangle2D rect = (java.awt.geom.Rectangle2D)shape;
				rect.setFrame( rect.getX(), rect.getY(), rect.getWidth(), Math.min( rect.getHeight(), heightConstraint ) );
				return rect;
			} else {
				//todo
				return shape;
			}
		} else {
			return shape;
		}
	}

	public ConnectionPreference getConnectionPreference() {
		return this.connectionPreference;
	}

	private static int getXForWestLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int x = featureComponentBounds.x;
		x -= 200;
		x -= noteBounds.width;
		return x;
	}

	private static int getXForEastLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int x = featureComponentBounds.x;
		x += featureComponentBounds.width;
		x += 200;
		return x;
	}

	private static int getYForNorthLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int y = featureComponentBounds.y;
		y -= 200;
		y -= noteBounds.height;
		return y;
	}

	private static int getYForSouthLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int y = featureComponentBounds.y;
		y += featureComponentBounds.height;
		y += 200;
		return y;
	}

	/* package-private */Connection calculateActualConnection( org.lgna.croquet.views.AwtComponentView<?> container, org.lgna.croquet.views.SwingComponentView<?> note ) {
		Connection actualConnection = null;
		org.lgna.croquet.views.TrackableShape featureTrackableShape = this.getTrackableShape();
		if( featureTrackableShape != null ) {
			java.awt.Shape shape = featureTrackableShape.getShape( container, null );
			if( shape != null ) {
				shape = constrainHeightIfNecessary( shape, this.heightConstraint );
				java.awt.Rectangle containerBounds = container.getLocalBounds();
				java.awt.Rectangle noteBounds = note.getBounds( container );
				java.awt.Rectangle featureComponentBounds = shape.getBounds();
				if( ( noteBounds != null ) && ( featureComponentBounds != null ) ) {
					final int x = featureComponentBounds.x - noteBounds.x;
					final int y = featureComponentBounds.y - noteBounds.y;

					Feature.ConnectionPreference connectionPreference = this.getConnectionPreference();
					if( connectionPreference == Feature.ConnectionPreference.EAST_WEST ) {
						if( x >= 32 ) {
							actualConnection = Feature.Connection.WEST;
						} else {
							if( x <= ( containerBounds.width - noteBounds.width - 32 ) ) {
								actualConnection = Feature.Connection.EAST;
							}
						}
					}
					if( actualConnection != null ) {
						//pass
					} else {
						if( y >= 32 ) {
							actualConnection = Feature.Connection.NORTH;
						} else {
							if( y <= ( containerBounds.height - noteBounds.height - 32 ) ) {
								actualConnection = Feature.Connection.SOUTH;
							} else {
								actualConnection = Connection.WEST;
							}
						}
					}
				}
			}
		}
		if( actualConnection != null ) {
			//pass
		} else {
			actualConnection = Feature.Connection.SOUTH;
		}
		return actualConnection;
	}

	public java.awt.Point calculateNoteLocation( org.lgna.croquet.views.AwtContainerView<?> container, org.lgna.croquet.views.AwtComponentView<?> note ) {
		java.awt.Rectangle containerBounds = container.getLocalBounds();
		java.awt.Rectangle noteBounds = note.getBounds( container );

		java.awt.Point rv = new java.awt.Point();
		Connection actualConnection = null;
		org.lgna.croquet.views.TrackableShape featureTrackableShape = this.getTrackableShape();
		if( featureTrackableShape != null ) {
			java.awt.Shape shape = featureTrackableShape.getShape( container, null );
			if( shape != null ) {
				shape = constrainHeightIfNecessary( shape, this.heightConstraint );
				java.awt.Rectangle featureComponentBounds = shape.getBounds();
				int xFeatureComponentCenter = featureComponentBounds.x + ( featureComponentBounds.width / 2 );
				int xCardCenter = ( containerBounds.x + containerBounds.width ) / 2;
				int yFeatureComponentCenter = featureComponentBounds.y + ( featureComponentBounds.height / 2 );
				int yCardCenter = ( containerBounds.y + containerBounds.height ) / 2;

				Feature.ConnectionPreference connectionPreference = this.getConnectionPreference();
				if( connectionPreference == Feature.ConnectionPreference.EAST_WEST ) {
					rv.x = getXForWestLayout( noteBounds, featureComponentBounds );
					if( rv.x >= 32 ) {
						actualConnection = Feature.Connection.WEST;
					} else {
						rv.x = getXForEastLayout( noteBounds, featureComponentBounds );
						if( rv.x <= ( containerBounds.width - noteBounds.width - 32 ) ) {
							actualConnection = Feature.Connection.EAST;
						}
					}
				}
				if( actualConnection != null ) {
					rv.y = yFeatureComponentCenter;
					if( yFeatureComponentCenter < yCardCenter ) {
						rv.y += 128;
					} else {
						rv.y -= noteBounds.height;
						rv.y -= 32;
					}
				} else {
					rv.y = getYForNorthLayout( noteBounds, featureComponentBounds );
					if( rv.y >= 32 ) {
						actualConnection = Feature.Connection.NORTH;
					} else {
						rv.y = getYForSouthLayout( noteBounds, featureComponentBounds );
						if( rv.y <= ( containerBounds.height - noteBounds.height - 32 ) ) {
							actualConnection = Feature.Connection.SOUTH;
						} else {
							class Inset implements Comparable<Inset> {
								private boolean isX;
								private int value;
								private Feature.Connection connection;

								public Inset( boolean isX, int value, Feature.Connection connection ) {
									this.isX = isX;
									this.value = value;
									this.connection = connection;
								}

								@Override
								public int compareTo( Inset o ) {
									return o.value - this.value;
								}
							}
							Inset[] insets = {
									new Inset( true, featureComponentBounds.x, Connection.WEST ),
									new Inset( true, containerBounds.width - ( featureComponentBounds.x + featureComponentBounds.width ), Connection.EAST ),
									new Inset( false, featureComponentBounds.y, Connection.NORTH ),
									new Inset( false, containerBounds.height - ( featureComponentBounds.y + featureComponentBounds.height ), Connection.SOUTH ),
							};
							java.util.Arrays.sort( insets );
							actualConnection = insets[ 0 ].connection;
							final int PAD = 64;
							if( insets[ 0 ].isX ) {
								rv.x = insets[ 0 ].value;
								if( actualConnection == Connection.WEST ) {
									rv.x -= noteBounds.width;
									rv.x -= PAD;
								} else {
									rv.x += PAD;
								}
								rv.y = yFeatureComponentCenter;
							} else {
								rv.x = xFeatureComponentCenter;
								rv.y = insets[ 0 ].value;
								if( actualConnection == Connection.NORTH ) {
									rv.y -= noteBounds.height;
									rv.y -= PAD;
								} else {
									rv.y += PAD;
								}
							}
							//note: return
							return rv;
						}
					}
					rv.x = xFeatureComponentCenter;
					if( xFeatureComponentCenter < xCardCenter ) {
						rv.x += 200;
					} else {
						rv.x -= noteBounds.width;
						rv.x -= 200;
					}
				}
			}
		}
		return rv;
	}

	protected abstract java.awt.Insets getBoundsInsets();

	protected abstract java.awt.Insets getContainsInsets();

	protected abstract java.awt.Insets getPaintInsets();

	public java.awt.Shape getShape( org.lgna.croquet.views.AwtComponentView<?> asSeenBy, java.awt.Insets insets ) {
		org.lgna.croquet.views.TrackableShape trackableShape = this.getTrackableShape();
		if( trackableShape != null ) {
			if( trackableShape.isInView() ) {
				java.awt.Shape shape = trackableShape.getVisibleShape( asSeenBy, insets );
				shape = constrainHeightIfNecessary( shape, this.heightConstraint );
				return shape;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public java.awt.geom.Area getAreaToSubstractForContains( org.lgna.croquet.views.AwtComponentView<?> asSeenBy ) {
		java.awt.Shape shape = this.getShape( asSeenBy, this.getContainsInsets() );
		if( shape != null ) {
			return new java.awt.geom.Area( shape );
		} else {
			return null;
		}
	}

	public java.awt.geom.Area getAreaToSubstractForPaint( org.lgna.croquet.views.AwtComponentView<?> asSeenBy ) {
		java.awt.Shape shape = this.getShape( asSeenBy, this.getPaintInsets() );
		if( shape != null ) {
			return new java.awt.geom.Area( shape );
		} else {
			return null;
		}
	}

	protected abstract void paint( java.awt.Graphics2D g2, java.awt.Shape shape, Connection actualConnection );

	private static void drawPath( java.awt.Graphics2D g2, float xFrom, float yFrom, float xTo, float yTo, boolean isCurveDesired ) {
		edu.cmu.cs.dennisc.math.polynomial.Polynomial xPolynomial;
		edu.cmu.cs.dennisc.math.polynomial.Polynomial yPolynomial;
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( xFrom, yFrom );
		if( isCurveDesired ) {
			float xVector = xTo - xFrom;
			float yVector = yTo - yFrom;
			final float A = 0.15f;
			final float B = 1.0f;

			float xA = xFrom + ( xVector * A );
			float yA = yFrom + ( yVector * A );

			float xB = xFrom + ( xVector * B );
			float yB = yFrom + ( yVector * B );

			float xC0 = xB;
			float yC0 = yA;
			float xC1 = xA;
			float yC1 = yB;

			path.curveTo( xC0, yC0, xC1, yC1, xTo, yTo );

			xPolynomial = new edu.cmu.cs.dennisc.math.polynomial.BezierCubic( xFrom, xC0, xC1, xTo );
			yPolynomial = new edu.cmu.cs.dennisc.math.polynomial.BezierCubic( yFrom, yC0, yC1, yTo );
		} else {
			float xC = xTo;
			float yC = yFrom;
			path.quadTo( xC, yC, xTo, yTo );
			xPolynomial = new edu.cmu.cs.dennisc.math.polynomial.BezierQuadratic( xFrom, xC, xTo );
			yPolynomial = new edu.cmu.cs.dennisc.math.polynomial.BezierQuadratic( yFrom, yC, yTo );

		}
		g2.draw( path );

		final double tDelta = 0.01;
		double theta = Double.NaN;
		double t = 0.9;
		while( true ) {

			double xApproaching = xPolynomial.evaluate( t );
			double yApproaching = yPolynomial.evaluate( t );

			double xDelta = xApproaching - xTo;
			double yDelta = yApproaching - yTo;

			t += tDelta;

			boolean isCloseEnough = ( ( xDelta * xDelta ) + ( yDelta * yDelta ) ) < ARROW_HEAD_LENGTH_SQUARED;
			boolean isBreaking = isCloseEnough || ( t >= 1.0 );

			if( isBreaking ) {
				theta = Math.atan2( yDelta, xDelta );
				break;
			}

		}

		if( Double.isNaN( theta ) ) {
			//pass
		} else {
			java.awt.geom.AffineTransform m = g2.getTransform();
			try {
				g2.translate( xTo, yTo );
				g2.rotate( theta );
				java.awt.geom.GeneralPath arrowHeadPath = new java.awt.geom.GeneralPath();
				arrowHeadPath.moveTo( 0, 0 );
				arrowHeadPath.lineTo( ARROW_HEAD_LENGTH, ARROW_HEAD_HALF_HEIGHT );
				arrowHeadPath.lineTo( ARROW_HEAD_LENGTH, -ARROW_HEAD_HALF_HEIGHT );
				arrowHeadPath.closePath();
				g2.fill( arrowHeadPath );
			} finally {
				g2.setTransform( m );
			}
		}
	}

	public final void paint( java.awt.Graphics2D g2, org.lgna.croquet.views.AwtComponentView<?> asSeenBy, org.lgna.croquet.views.SwingComponentView<?> note ) {
		java.awt.Shape shape = this.getShape( asSeenBy, this.getPaintInsets() );
		if( shape != null ) {
			Connection actualConnection = this.calculateActualConnection( asSeenBy, note );
			java.awt.Paint prevPaint = g2.getPaint();
			java.awt.Stroke prevStroke = g2.getStroke();

			this.paint( g2, shape, actualConnection );

			if( this.isPathRenderingDesired() ) {
				g2.setPaint( java.awt.Color.BLACK );

				org.lgna.croquet.views.AwtComponentView<?> component;
				if( note.getComponentCount() > 0 ) {
					component = note.getComponent( 0 );
				} else {
					component = note;
				}
				java.awt.Rectangle noteBounds = component.getBounds( asSeenBy );
				java.awt.Rectangle shapeBounds = shape.getBounds();
				if( shapeBounds != null ) {
					java.awt.Point ptComponent = actualConnection.getPoint( shapeBounds );
					int xContraint;
					if( noteBounds.x > ptComponent.x ) {
						xContraint = javax.swing.SwingConstants.LEADING;
					} else {
						xContraint = javax.swing.SwingConstants.TRAILING;
					}
					java.awt.Point ptNote = edu.cmu.cs.dennisc.java.awt.RectangleUtilities.getPoint( noteBounds, xContraint, javax.swing.SwingConstants.CENTER );
					g2.setStroke( ARROW_STROKE );
					drawPath( g2, ptNote.x, ptNote.y, ptComponent.x, ptComponent.y, actualConnection.isCurveDesired() );
				}
			}

			g2.setStroke( prevStroke );
			g2.setPaint( prevPaint );
		}
	}

	public void appendDetailedReport( StringBuilder sb ) {
		sb.append( "\t\t" );
		sb.append( this.getClass().getName() );
		sb.append( ": " );
		String status = this.getStatus();
		sb.append( status != null ? status : "IS_GOOD_TO_GO" );
		sb.append( "\n" );
	}
}
