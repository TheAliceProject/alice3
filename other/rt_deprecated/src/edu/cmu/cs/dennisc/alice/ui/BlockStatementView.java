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
package edu.cmu.cs.dennisc.alice.ui;

//class DoNothingView extends edu.cmu.cs.dennisc.alan.VerticallyLaidOutView { 
//	public DoNothingView() {
//		add( new edu.cmu.cs.dennisc.alan.TextLabel( "do nothing" ) );
//	}
//	
//}


/**
 * @author Dennis Cosgrove
 */
public abstract class BlockStatementView extends edu.cmu.cs.dennisc.alan.VerticallyLaidOutView {
//	private static DoNothingView s_doNothingView = new DoNothingView();
//	private class DockingBayRenderer extends edu.cmu.cs.dennisc.image.renderer.SoftClippedTranslucentRenderer {
//		//todo
//		//private java.awt.Color COLOR = edu.cmu.cs.dennisc.color.ColorUtilities.scaleHSB( new java.awt.Color( 255, 230, 180 ), 1.0f, 1.0f, 0.75f );
//		private java.awt.Paint COLOR = new java.awt.GradientPaint( 0, 0, new java.awt.Color( 0, 0, 255 ), 0, 128, new java.awt.Color( 0, 0, 0 ) );
//		private float m_actualHeight = Float.NaN;
//		private int m_outer = 12;
//		private int m_inner = 12;
//		private java.awt.Shape getShape() {
//			if( m_dragComponent != null ) {
//				float round = m_dragComponent.getDesiredRound();
//				float width = m_dragComponent.getDesiredWidth();
//				return new java.awt.geom.RoundRectangle2D.Float( m_outer, m_outer, width, m_actualHeight, round, round );
//			} else {
//				return null;
//			}
//		}
//		@Override
//		protected int getWidth() {
//			if( m_dragComponent != null ) {
//				return (int)( m_dragComponent.getDesiredWidth() + m_outer + m_outer + 0.5f );
//			} else {
//				return 0;
//			}
//		}
//		@Override
//		protected int getHeight() {
//			int rv;
//			if( m_dragComponent != null ) {
//				rv = (int)( m_dragComponent.getDesiredHeight() + m_outer + m_outer + 0.5f );
//			} else {
//				rv = 0;
//			}
//			return rv;
//		}
//		
//		private java.awt.Paint getPaint() {
//			return COLOR;
//		}
//		
//		public void setActualHeight( float actualHeight ) {
//			m_actualHeight = actualHeight;
//		}
//		@Override
//		public void paintBackground( java.awt.Graphics2D g2 ) {
//			//edu.cmu.cs.dennisc.awt.ShapeUtilties.paintBorder( g2, getShape(), new java.awt.Color(255, 255, 210), java.awt.Color.ORANGE, m_outer );
//		}
//		@Override
//		protected void renderClip( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
//			g2.fill( getShape() );
//		}
//		@Override
//		protected void renderForegroundIntoBufferedImage( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
//			java.awt.Shape shape = getShape();
//			g2.setComposite( java.awt.AlphaComposite.SrcAtop );
//			java.awt.Paint paint = getPaint();
//			g2.setPaint( paint );
//			g2.fill( shape );
//			//java.awt.Color color = (java.awt.Color)paint; 
//			//edu.cmu.cs.dennisc.awt.ShapeUtilties.paintBorder( g2, shape, color, java.awt.Color.BLACK, m_inner, java.awt.AlphaComposite.SRC_ATOP );
//		}
//		@Override
//		public void paint( java.awt.Graphics2D g2 ) {
//			super.paint( g2, getWidth(), (int)m_actualHeight );
//		}
//		
//	}
//	private DockingBayRenderer m_dockingBayRenderer = new DockingBayRenderer();
	private Spacer m_preSpacer = new Spacer();
	private Spacer m_postSpacer = new Spacer() {
		@Override
		protected float getVelocityMagnitude() {
			return super.getVelocityMagnitude() * 2.0f;
		}
	};
	private DragComponent m_dragComponent;
	private int m_dropIndex = -1;
	
	protected abstract java.awt.Point convertPoint( java.awt.event.MouseEvent e );

	private final int ZERO_CHILDREN_HEIGHT = 24;
	@Override
	protected void updateLocalBounds() {
		super.updateLocalBounds();
		if( getChildCount() > 0 ) {
			//pass
			m_localBounds.width += 8;
			m_localBounds.height += 8;
		} else {
			//todo
			m_localBounds.setFrame( 0, 0, 320, ZERO_CHILDREN_HEIGHT );
		}
	}
	public int getDropIndex() {
		return m_dropIndex;
	}

	public void resetSpacerDesiredHeights() {
		m_preSpacer.setDesiredHeight( 0.0f );
		for( edu.cmu.cs.dennisc.alan.View child : accessChildren() ) {
			assert child instanceof StatementView;
			StatementView statementView = (StatementView)child;
			statementView.getPreSpacer().setDesiredHeight( 0.0f );
			statementView.getPostSpacer().setDesiredHeight( 0.0f );
		}
		m_postSpacer.setDesiredHeight( 0.0f );
	}
	public void resetSpacers() {
		m_preSpacer.reset();
		for( edu.cmu.cs.dennisc.alan.View child : accessChildren() ) {
			assert child instanceof StatementView;
			StatementView statementView = (StatementView)child;
			statementView.getPreSpacer().reset();
			statementView.getPostSpacer().reset();
		}
		m_postSpacer.reset();
	}
	public void handleDragEntered( DragComponent dragComponent ) {
		m_dragComponent = (DragComponent)dragComponent;
	}
	public void handleDragUpdated( java.awt.geom.Point2D.Float pt ) {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "dragUpdated", e );
		if( m_dragComponent != null ) {
			
			resetSpacerDesiredHeights();
			
			float height = m_dragComponent.getDesiredHeight();
			final int N = getChildCount();
			if( N > 0 ) {
				float halfHeight = height * 0.5f;
				m_dropIndex = N;
				for( int i=0; i<N; i++ ) {
					edu.cmu.cs.dennisc.alan.View child = getChild( i );
					float y = child.getYMin( true );
					if( ( y + child.getHeight()*0.5f ) > pt.y ) {
						m_dropIndex = i;
						break;
					}
				}
				if( m_dropIndex == 0 ) {
					edu.cmu.cs.dennisc.alan.View child = getChild( m_dropIndex );
					assert child instanceof StatementView;
					StatementView statementView = (StatementView)child;
					m_preSpacer.setDesiredHeight( halfHeight );
					statementView.getPreSpacer().setDesiredHeight( halfHeight );
				} else if( m_dropIndex < N ) {
					edu.cmu.cs.dennisc.alan.View prevChild = getChild( m_dropIndex-1 );
					assert prevChild instanceof StatementView;
					StatementView prevStatementView = (StatementView)prevChild;
					prevStatementView.getPostSpacer().setDesiredHeight( halfHeight );

					edu.cmu.cs.dennisc.alan.View postChild = getChild( m_dropIndex );
					assert postChild instanceof StatementView;
					StatementView postStatementView = (StatementView)postChild;
					postStatementView.getPreSpacer().setDesiredHeight( halfHeight );
				} else {
					edu.cmu.cs.dennisc.alan.View prevChild = getChild( N-1 );
					assert prevChild instanceof StatementView;
					StatementView prevStatementView = (StatementView)prevChild;
					prevStatementView.getPostSpacer().setDesiredHeight( halfHeight );
					m_postSpacer.setDesiredHeight( halfHeight );
				}
			} else {
				m_postSpacer.setDesiredHeight( height );
				m_dropIndex = 0;
			}
		}
	}
	public abstract void handlDragDropped();
	public void handleDragExited() {
		resetSpacerDesiredHeights();
		//m_dragComponent = null;
	}

	private double m_tPrev = Double.NaN;
	public boolean update() {
		boolean isRefreshRequired = false;
		double t = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
		if( Double.isNaN( m_tPrev ) ) {
			//pass
		} else {
			double tDelta = t - m_tPrev;
			int count = getChildCount();
			if( count > 0 ) {
				StatementView sv0 = getChild( 0, StatementView.class );
				m_preSpacer.setMaximumHeight( sv0.getHeight() * 0.5f );
				for( edu.cmu.cs.dennisc.alan.View child : accessChildren() ) {
					assert child instanceof StatementView;
					StatementView statementView = (StatementView)child;
					float maximumHeight = statementView.getHeight() * 0.5f;
					statementView.getPreSpacer().setMaximumHeight( maximumHeight );
					statementView.getPostSpacer().setMaximumHeight( maximumHeight );
				}
				StatementView svLast = getChild( count-1, StatementView.class );
				m_postSpacer.setMaximumHeight( svLast.getHeight() * 0.5f );
			} else {
				//float maximumHeight = ZERO_CHILDREN_HEIGHT * 0.5f;
				m_preSpacer.setMaximumHeight( ZERO_CHILDREN_HEIGHT );
				m_postSpacer.setMaximumHeight( ZERO_CHILDREN_HEIGHT );
			}
			isRefreshRequired |= m_preSpacer.update( tDelta );
			for( edu.cmu.cs.dennisc.alan.View child : accessChildren() ) {
				assert child instanceof StatementView;
				StatementView statementView = (StatementView)child;
				isRefreshRequired |= statementView.getPreSpacer().update( tDelta );
				isRefreshRequired |= statementView.getPostSpacer().update( tDelta );
			}
			isRefreshRequired |= m_postSpacer.update( tDelta );
		}
		m_tPrev = t;
		return isRefreshRequired;
	}
	
	public edu.cmu.cs.dennisc.math.Point2f getDropOffset( float x, float y ) {
		edu.cmu.cs.dennisc.math.Point2f rv = new edu.cmu.cs.dennisc.math.Point2f();
		if( m_dragComponent != null ) {
			float xOffset = x + m_leftInset;
			float yOffset = y;
			switch( m_dropIndex ) {
			case -1:
				xOffset = Float.NaN;
				yOffset = Float.NaN;
				break;
			case 0:
				if( getChildCount() > 0 ) {
					StatementView sv0 = getChild( 0, StatementView.class );
					yOffset += sv0.accessLocalBounds().y + sv0.getYTranslation() - m_preSpacer.getCurrentHeight();
				}
				break;
			default:
				if( getChildCount() > m_dropIndex ) {
					StatementView svI = getChild( m_dropIndex-1, StatementView.class );
					StatementView svJ = getChild( m_dropIndex, StatementView.class );
					yOffset += svJ.accessLocalBounds().y + svJ.getYTranslation() - svI.getPostSpacer().getCurrentHeight();
				} else {
					yOffset += getHeight();
				}
			}
			rv.set( xOffset, yOffset );
		} else {
			rv.setNaN();
		}
		return rv;
	}
	private void paintStatementDockingBay( java.awt.Graphics2D g2, boolean isHighlighted, float x, float y, float actualHeight ) { 
		assert m_dragComponent != null;
		float width = m_dragComponent.getDesiredWidth();
		float round = m_dragComponent.getDesiredRound();
		java.awt.geom.RoundRectangle2D.Float roundRect = new java.awt.geom.RoundRectangle2D.Float( 0, 0, width, actualHeight, round, round );
		g2.translate( x, y );
		try {
			java.awt.Paint paint;
			if( isHighlighted ) {
//				//paint = new java.awt.Color( 152, 111, 57 );
//				g2.translate( -m_dockingBayRenderer.m_outer, -m_dockingBayRenderer.m_outer );
//				//m_dockingBayRenderer.paint( g2, m_dockingBayRenderer.getWidth(), Math.min( (int)actualHeight, 32 ) );
//				m_dockingBayRenderer.setActualHeight( actualHeight );
//				m_dockingBayRenderer.paint( g2 );
//				g2.translate( +m_dockingBayRenderer.m_outer, +m_dockingBayRenderer.m_outer );
				paint = new java.awt.GradientPaint( 0, 0, new java.awt.Color( 0x99, 0x66, 0x33, 128 ), 0, actualHeight, new java.awt.Color( 0x66, 0x33, 0x0, 192 ) );
				//g2.setPaint( java.awt.Color.DARK_GRAY );
			} else {
				paint = new java.awt.Color( 152, 133, 107 );
				//g2.setPaint( new java.awt.Color( 152, 133, 107 ) );
			}
			g2.setPaint( paint );
			g2.fill( roundRect );
			g2.setPaint( java.awt.Color.BLACK );
			g2.draw( roundRect );
		} finally {
			g2.translate( -x, -y );
		}
	}

	@Override
	protected void paintBackground( java.awt.Graphics2D g2, float x, float y, float width, float height ) {
		super.paintBackground( g2, x, y, width, height );
//		if( getChildCount() == 0 ) {
//			g2.drawString( "do nothing", x, y+height );
//		}
		if( m_dragComponent != null ) {
			if( m_dropIndex != -1 ) {
				//todo
				float yAdjust = edu.cmu.cs.dennisc.alan.VerticalFlowLayoutManager.Y_SPACE * 0.5f;
				
				float space;
				float xOffset = x + m_leftInset;

				final int N = getChildCount();
				if( N>0 ) {
					StatementView sv0 = getChild( 0, StatementView.class );
					space = sv0.getPreSpacer().getCurrentHeight() + m_preSpacer.getCurrentHeight();
					if( space > 0.0f ) {
						float yOffset = sv0.accessLocalBounds().y + sv0.getYTranslation() - m_preSpacer.getCurrentHeight();
						yOffset -= yAdjust;
						paintStatementDockingBay( g2, m_dropIndex == 0, xOffset, yOffset, space );
					}
					for( int j=1; j<N; j++ ) {
						int i = j-1;
						StatementView svI = getChild( i, StatementView.class );
						StatementView svJ = getChild( j, StatementView.class );
						
						space = svI.getPostSpacer().getCurrentHeight() + svJ.getPreSpacer().getCurrentHeight();
						if( space > 0.0f ) {
							float yOffset = svJ.accessLocalBounds().y + svJ.getYTranslation() - svI.getPostSpacer().getCurrentHeight();
							
							yOffset -= yAdjust;
							
							paintStatementDockingBay( g2, m_dropIndex == j, xOffset, yOffset, space );
						}
					}
				}
				space = m_postSpacer.getCurrentHeight();
				if( space > 0.0f ) {
					float yOffset;
					if( N > 0 ) {
						StatementView svLast = getChild( N-1, StatementView.class );
						yOffset = height - svLast.getPostSpacer().getCurrentHeight();
						yOffset -= yAdjust;
						space += svLast.getPostSpacer().getCurrentHeight();
					} else {
						yOffset = 0;
					}
					paintStatementDockingBay( g2, m_dropIndex == N, xOffset, yOffset, space );
				}
			}
		}
	}
}
