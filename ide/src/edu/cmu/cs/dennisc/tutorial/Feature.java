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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ abstract class Feature {
	private static final int ARROW_HEAD_WIDTH = 19;
	private static final int ARROW_HEAD_HALF_HEIGHT = 6;
	public enum ConnectionPreference {
		NORTH_SOUTH,
		EAST_WEST
	}
	public enum Connection {
		NORTH( javax.swing.SwingConstants.CENTER, javax.swing.SwingConstants.LEADING, false, Math.PI/2 ),
		SOUTH( javax.swing.SwingConstants.CENTER, javax.swing.SwingConstants.TRAILING, false, -Math.PI/2 ),
		EAST( javax.swing.SwingConstants.TRAILING, javax.swing.SwingConstants.CENTER, true, Math.PI ),
		WEST( javax.swing.SwingConstants.LEADING, javax.swing.SwingConstants.CENTER, true, 0 );
		int xConstraint;
		int yConstraint;
		boolean isCurveDesired;
		double theta;
		private Connection(int xConstraint, int yConstraint, boolean isCurveDesired, double theta ) {
			this.xConstraint = xConstraint;
			this.yConstraint = yConstraint;
			this.isCurveDesired = isCurveDesired;
			this.theta = theta;
		}
		public int getXConstraint() {
			return this.xConstraint;
		}
		public int getYConstraint() {
			return this.yConstraint;
		}
		public boolean isCurveDesired() {
			return this.isCurveDesired;
		}
		
		public void fillArrowHead( java.awt.Graphics2D g2, float x, float y ) {
			java.awt.geom.AffineTransform m = g2.getTransform();
			try {
				g2.translate( x, y );
				g2.rotate( theta );
				java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
				path.moveTo( 0, 0 );
				path.lineTo( -ARROW_HEAD_WIDTH, ARROW_HEAD_HALF_HEIGHT );
				path.lineTo( -ARROW_HEAD_WIDTH, -ARROW_HEAD_HALF_HEIGHT );
				path.closePath();
				g2.fill( path );
			} finally {
				g2.setTransform( m );
			}
		}
	}
	private static final java.awt.Stroke ARROW_STROKE = new java.awt.BasicStroke( 3.0f ); 

	private edu.cmu.cs.dennisc.croquet.Resolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > trackableShapeResolver;
	private ConnectionPreference connectionPreference;
	public Feature( edu.cmu.cs.dennisc.croquet.Resolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > trackableShapeResolver, ConnectionPreference connectionPreference ) {
		//assert trackableShape != null;
		this.trackableShapeResolver = trackableShapeResolver;
		this.connectionPreference = connectionPreference;
	}
	
	public edu.cmu.cs.dennisc.croquet.TrackableShape getTrackableShape() {
		return this.trackableShapeResolver.getResolved();
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
	
	/*package-private*/ Connection calculateActualConnection( edu.cmu.cs.dennisc.croquet.Component<?> container, edu.cmu.cs.dennisc.croquet.JComponent<?> note ) {
		Connection actualConnection = null;
		edu.cmu.cs.dennisc.croquet.TrackableShape featureTrackableShape = this.getTrackableShape();
		if( featureTrackableShape != null ) {
			java.awt.Shape shape = featureTrackableShape.getShape( container, null );
			if( shape != null ) {
				java.awt.Rectangle containerBounds = container.getLocalBounds();
				java.awt.Rectangle noteBounds = note.getBounds( container );
				java.awt.Rectangle featureComponentBounds = shape.getBounds();
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
		if( actualConnection != null ) {
			//pass
		} else {
			actualConnection = Feature.Connection.SOUTH;
		}
		return actualConnection;
	}
	/*package-private*/ java.awt.Point calculateNoteLocation( edu.cmu.cs.dennisc.croquet.Container< ? > container, edu.cmu.cs.dennisc.croquet.Component< ? > note ) {
		java.awt.Rectangle containerBounds = container.getLocalBounds();
		java.awt.Rectangle noteBounds = note.getBounds( container );
		
		java.awt.Point rv = new java.awt.Point();
		Connection actualConnection = null;
		edu.cmu.cs.dennisc.croquet.TrackableShape featureTrackableShape = this.getTrackableShape();
		if( featureTrackableShape != null ) {
			java.awt.Shape shape = featureTrackableShape.getShape( container, null );
			if( shape != null ) {
				java.awt.Rectangle featureComponentBounds = shape.getBounds();
				int xFeatureComponentCenter = featureComponentBounds.x + featureComponentBounds.width/2;
				int xCardCenter = ( containerBounds.x + containerBounds.width ) / 2;
				int yFeatureComponentCenter = featureComponentBounds.y + featureComponentBounds.height/2;
				int yCardCenter = ( containerBounds.y + containerBounds.height ) / 2;

				Feature.ConnectionPreference connectionPreference = this.getConnectionPreference();
				if( connectionPreference == Feature.ConnectionPreference.EAST_WEST ) {
					rv.x = getXForWestLayout( noteBounds, featureComponentBounds );
					if( rv.x >= 32 ) {
						actualConnection = Feature.Connection.WEST;
					} else {
						rv.x = getXForEastLayout(noteBounds, featureComponentBounds);
						if( rv.x <= ( containerBounds.width - noteBounds.width - 32 ) ) {
							actualConnection = Feature.Connection.EAST;
						}
					}
				}
				if( actualConnection != null ) {
					rv.y = yFeatureComponentCenter;
					if( yFeatureComponentCenter < yCardCenter ) {
						rv.y += 100;
					} else {
						rv.y -= noteBounds.height;
						rv.y -= 100;
					}
				} else {
					rv.y = getYForNorthLayout( noteBounds, featureComponentBounds );
					if( rv.y >= 32 ) {
						actualConnection = Feature.Connection.NORTH;
					} else {
						rv.y = getYForSouthLayout( noteBounds, featureComponentBounds);
						if( rv.y <= ( containerBounds.height - noteBounds.height - 32 ) ) {
							actualConnection = Feature.Connection.SOUTH;
						} else {
							class Inset implements Comparable< Inset >{
								private boolean isX;
								private int value;
								private Feature.Connection connection;
								public Inset( boolean isX, int value, Feature.Connection connection ) {
									this.isX = isX;
									this.value = value;
									this.connection = connection;
								}
								public int compareTo(Inset o) {
									return o.value - this.value; 
								}
							}
							Inset[] insets = {
									new Inset( true, featureComponentBounds.x, Connection.WEST ),	
									new Inset( true, containerBounds.width - (featureComponentBounds.x+featureComponentBounds.width), Connection.EAST ),	
									new Inset( false, featureComponentBounds.y, Connection.NORTH ),	
									new Inset( false, containerBounds.height - (featureComponentBounds.y+featureComponentBounds.height), Connection.SOUTH ),	
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
	protected abstract java.awt.Insets getContainsInsets();
	protected abstract java.awt.Insets getPaintInsets();
	
	protected java.awt.Shape getShape( edu.cmu.cs.dennisc.croquet.Component<?> asSeenBy, java.awt.Insets insets ) {
		edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape = this.trackableShapeResolver.getResolved();
		if( trackableShape != null ) {
			if( trackableShape.isInView() ) {
				return trackableShape.getVisibleShape( asSeenBy, insets );
			} else {
				edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = trackableShape.getScrollPaneAncestor();
				if( scrollPane != null ) {
					javax.swing.JScrollBar scrollBar = scrollPane.getAwtComponent().getVerticalScrollBar();
					java.awt.Rectangle rect = javax.swing.SwingUtilities.convertRectangle(scrollBar.getParent(), scrollBar.getBounds(), asSeenBy.getAwtComponent() );
					return edu.cmu.cs.dennisc.java.awt.RectangleUtilties.inset( rect, this.getPaintInsets() );
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}
	public java.awt.geom.Area getAreaToSubstractForContains( edu.cmu.cs.dennisc.croquet.Component<?> asSeenBy ) {
		java.awt.Shape shape = this.getShape( asSeenBy, this.getContainsInsets() );
		if( shape != null ) {
			return new java.awt.geom.Area( shape );
		} else {
			return null;
		}
	}
	public java.awt.geom.Area getAreaToSubstractForPaint( edu.cmu.cs.dennisc.croquet.Component<?> asSeenBy ) {
		java.awt.Shape shape = this.getShape( asSeenBy, this.getPaintInsets() );
		if( shape != null ) {
			return new java.awt.geom.Area( shape );
		} else {
			return null;
		}
	}
	protected abstract void paint( java.awt.Graphics2D g2, java.awt.Shape shape );
	private static void drawPath( java.awt.Graphics2D g2, float xFrom, float yFrom, float xTo, float yTo, boolean isCurveDesired ) {
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( xFrom, yFrom );
		if( isCurveDesired ) {
			float xVector = xTo-xFrom; 
			float yVector = yTo-yFrom;
			final float A = 0.15f;
			final float B = 1.0f;
			
			float xA = xFrom + xVector*A;
			float yA = yFrom + yVector*A;

			float xB = xFrom + xVector*B;
			float yB = yFrom + yVector*B;
			
			path.curveTo( xB, yA, xA, yB, xTo, yTo );

			//g2.drawLine( (int)xB, (int)yA, (int)xA, (int)yB );
		} else {
			path.quadTo(xTo, yFrom, xTo, yTo);
		}
		g2.draw( path );
	}
	public final void paint( java.awt.Graphics2D g2, edu.cmu.cs.dennisc.croquet.Component<?> asSeenBy, edu.cmu.cs.dennisc.croquet.JComponent<?> note ) {
		java.awt.Shape shape = this.getShape( asSeenBy, this.getPaintInsets() );
		if( shape != null ) {
			Connection actualConnection = this.calculateActualConnection( asSeenBy, note );
			java.awt.Paint prevPaint = g2.getPaint();
			java.awt.Stroke prevStroke = g2.getStroke();

			this.paint( g2, shape );
			g2.setPaint( java.awt.Color.BLACK );

			java.awt.Rectangle noteBounds = note.getComponent( 0 ).getBounds( asSeenBy );
			java.awt.Rectangle shapeBounds = shape.getBounds();
			if( shapeBounds != null ) {
				java.awt.Point ptComponent = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( shapeBounds, actualConnection.getXConstraint(), actualConnection.getYConstraint() );
				int xContraint;
				if( noteBounds.x > ptComponent.x ) {
					xContraint = javax.swing.SwingConstants.LEADING;
				} else {
					xContraint = javax.swing.SwingConstants.TRAILING;
				}
				java.awt.Point ptNote = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( noteBounds, xContraint, javax.swing.SwingConstants.CENTER );
				g2.setStroke( ARROW_STROKE );
				drawPath( g2, ptNote.x, ptNote.y, ptComponent.x, ptComponent.y, actualConnection.isCurveDesired() );
				actualConnection.fillArrowHead(g2, ptComponent.x, ptComponent.y);
			}
			
			g2.setStroke( prevStroke );
			g2.setPaint( prevPaint );
		}
	}
}
