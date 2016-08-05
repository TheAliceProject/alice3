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

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class DropDown<M extends org.lgna.croquet.PopupPrepModel> extends AbstractPopupButton<M> {
	private static final int DEFAULT_AFFORDANCE_WIDTH = 6;
	private static final int DEFAULT_AFFORDANCE_HALF_HEIGHT = 5;
	private static final java.awt.Color ARROW_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 191 );

	private org.lgna.croquet.views.SwingComponentView<?> prefixComponent;
	private org.lgna.croquet.views.SwingComponentView<?> mainComponent;
	private org.lgna.croquet.views.SwingComponentView<?> postfixComponent;

	public DropDown( M model, org.lgna.croquet.views.SwingComponentView<?> prefixComponent, org.lgna.croquet.views.SwingComponentView<?> mainComponent, org.lgna.croquet.views.SwingComponentView<?> postfixComponent ) {
		super( model );
		this.prefixComponent = prefixComponent;
		this.mainComponent = mainComponent;
		this.postfixComponent = postfixComponent;
		this.setMaximumSizeClampedToPreferredSize( true );
	}

	public DropDown( M model ) {
		this( model, null, null, null );
	}

	public org.lgna.croquet.views.SwingComponentView<?> getPrefixComponent() {
		return this.prefixComponent;
	}

	public void setPrefixComponent( org.lgna.croquet.views.SwingComponentView<?> prefixComponent ) {
		if( this.prefixComponent != prefixComponent ) {
			this.prefixComponent = prefixComponent;
			//			this.revalidateAndRepaint();
		}
	}

	public org.lgna.croquet.views.SwingComponentView<?> getMainComponent() {
		return this.mainComponent;
	}

	public void setMainComponent( org.lgna.croquet.views.SwingComponentView<?> mainComponent ) {
		if( this.mainComponent != mainComponent ) {
			this.mainComponent = mainComponent;
			//			this.revalidateAndRepaint();
		}
	}

	public org.lgna.croquet.views.SwingComponentView<?> getPostfixComponent() {
		return this.postfixComponent;
	}

	public void setPostfixComponent( org.lgna.croquet.views.SwingComponentView<?> postfixComponent ) {
		if( this.postfixComponent != postfixComponent ) {
			this.postfixComponent = postfixComponent;
			//			this.revalidateAndRepaint();
		}
	}

	protected boolean isInactiveFeedbackDesired() {
		return true;
	}

	protected int getAffordanceWidth() {
		return DEFAULT_AFFORDANCE_WIDTH;
	}

	protected int getAffordanceHalfHeight() {
		return DEFAULT_AFFORDANCE_HALF_HEIGHT;
	}

	//	@Override
	//	public void appendPrepStepsIfNecessary( org.lgna.croquet.history.Transaction transaction ) {
	//		super.appendPrepStepsIfNecessary( transaction );
	//		if( transaction.containsPrepStep( transaction, this.getModel(), org.lgna.croquet.history.PopupPrepStep.class ) ) {
	//			//pass
	//		} else {
	//			org.lgna.croquet.history.PopupPrepStep.createAndAddToTransaction( transaction, this.getModel(), new org.lgna.croquet.triggers.SimulatedTrigger() );
	//		}
	//	}

	private final class JDropDownButton extends javax.swing.JToggleButton {
		public JDropDownButton() {
			this.setRolloverEnabled( true );
		}

		@Override
		public void updateUI() {
			//this.setUI( new javax.swing.plaf.basic.BasicButtonUI() );
			this.setUI( new org.lgna.croquet.views.imp.DropDownButtonUI( (javax.swing.AbstractButton)this ) );
		}

		@Override
		public java.awt.Dimension getPreferredSize() {
			return constrainPreferredSizeIfNecessary( super.getPreferredSize() );
		}

		@Override
		public java.awt.Dimension getMaximumSize() {
			if( DropDown.this.isMaximumSizeClampedToPreferredSize() ) {
				return this.getPreferredSize();
			} else {
				return super.getMaximumSize();
			}
		}

		@Override
		public void paint( java.awt.Graphics g ) {
			int x = 0;
			int y = 0;
			int width = this.getWidth();
			int height = this.getHeight();
			javax.swing.ButtonModel buttonModel = this.getModel();
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			java.awt.Paint prevPaint = g2.getPaint();
			boolean isActive = buttonModel.isRollover() || buttonModel.isPressed();
			if( isActive || DropDown.this.isInactiveFeedbackDesired() ) {
				if( isActive ) {
					if( buttonModel.isPressed() ) {
						g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 127 ) );
					} else {
						g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 220 ) );
					}
				} else {
					g2.setColor( this.getBackground() );
				}
				g2.fillRect( x, y, width, height );
			}

			super.paint( g );

			int AFFORDANCE_WIDTH = getAffordanceWidth();
			int AFFORDANCE_HALF_HEIGHT = getAffordanceHalfHeight();

			float x0 = ( x + width ) - 4 - AFFORDANCE_WIDTH;
			float x1 = x0 + AFFORDANCE_WIDTH;
			float xC = ( x0 + x1 ) / 2;

			float yC = ( y + height ) / 2;
			float y0 = yC - AFFORDANCE_HALF_HEIGHT;
			float y1 = yC + AFFORDANCE_HALF_HEIGHT;

			java.awt.Color triangleFill;
			java.awt.Color triangleOutline;
			if( isActive ) {
				triangleFill = java.awt.Color.YELLOW;
				triangleOutline = java.awt.Color.BLACK;
			} else {
				triangleFill = ARROW_COLOR;
				triangleOutline = null;
			}

			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

			java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
			path.moveTo( x0, y0 );
			path.lineTo( xC, y1 );
			path.lineTo( x1, y0 );
			path.closePath();

			g2.setColor( triangleFill );
			g2.fill( path );
			if( triangleOutline != null ) {
				g2.setColor( triangleOutline );
				g2.draw( path );
			}

			if( isActive ) {
				g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
				//				g2.setColor(java.awt.Color.BLUE);
				//				g2.draw(new java.awt.geom.Rectangle2D.Float(1.5f, 1.5f, width - 3.0f, height - 3.0f));
				int xMax = ( x + width ) - 1;
				int yMax = ( y + height ) - 1;
				if( buttonModel.isPressed() ) {
					g2.setColor( java.awt.Color.BLACK );
				} else {
					g2.setColor( java.awt.Color.WHITE );
				}
				g2.drawLine( x, yMax, x, y );
				g2.drawLine( x, y, xMax, y );
				if( buttonModel.isPressed() ) {
					g2.setColor( java.awt.Color.WHITE );
				} else {
					g2.setColor( java.awt.Color.BLACK );
				}
				g2.drawLine( x, yMax, xMax, yMax );
				g2.drawLine( xMax, yMax, xMax, y );
			} else {
				if( DropDown.this.isInactiveFeedbackDesired() ) {
					g2.setColor( java.awt.Color.WHITE );
					//g2.drawRect( x, y, width-1, height-1 );
					g2.drawLine( x, y, x + width, y );
					g2.drawLine( x, y, x, y + height );
				}
			}

			g2.setPaint( prevPaint );
		}
	};

	@Override
	protected javax.swing.AbstractButton createSwingButton() {
		javax.swing.AbstractButton rv = new JDropDownButton();
		rv.setRolloverEnabled( true );
		rv.setOpaque( false );
		rv.setCursor( java.awt.Cursor.getPredefinedCursor( java.awt.Cursor.DEFAULT_CURSOR ) );
		//rv.setBackground(edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray(230));
		rv.setBackground( new java.awt.Color( 230, 230, 230, 127 ) );
		rv.setFocusable( false );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 1, 3, 1, 5 + getAffordanceWidth() ) );
		if( ( this.prefixComponent != null ) || ( this.mainComponent != null ) || ( this.postfixComponent != null ) ) {
			//			rv.setModel( new javax.swing.DefaultButtonModel() );
			//rv.setLayout(new javax.swing.BoxLayout(rv, javax.swing.BoxLayout.LINE_AXIS));
			rv.setLayout( new java.awt.BorderLayout() );
			if( this.prefixComponent != null ) {
				rv.add( this.prefixComponent.getAwtComponent(), java.awt.BorderLayout.LINE_START );
			}
			if( this.mainComponent != null ) {
				rv.add( this.mainComponent.getAwtComponent(), java.awt.BorderLayout.CENTER );
			}
			if( this.postfixComponent != null ) {
				rv.add( this.postfixComponent.getAwtComponent(), java.awt.BorderLayout.LINE_END );
			}
		}
		return rv;
	}
}
