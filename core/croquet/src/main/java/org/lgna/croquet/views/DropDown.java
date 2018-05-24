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

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import org.lgna.croquet.PopupPrepModel;
import org.lgna.croquet.views.imp.DropDownButtonUI;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

/**
 * @author Dennis Cosgrove
 */
public class DropDown<M extends PopupPrepModel> extends AbstractPopupButton<M> {
	private static final int DEFAULT_AFFORDANCE_WIDTH = 6;
	private static final int DEFAULT_AFFORDANCE_HALF_HEIGHT = 5;
	private static final Color ARROW_COLOR = ColorUtilities.createGray( 191 );

	private SwingComponentView<?> prefixComponent;
	private SwingComponentView<?> mainComponent;
	private SwingComponentView<?> postfixComponent;

	public DropDown( M model, SwingComponentView<?> prefixComponent, SwingComponentView<?> mainComponent, SwingComponentView<?> postfixComponent ) {
		super( model );
		this.prefixComponent = prefixComponent;
		this.mainComponent = mainComponent;
		this.postfixComponent = postfixComponent;
		this.setMaximumSizeClampedToPreferredSize( true );
	}

	public DropDown( M model ) {
		this( model, null, null, null );
	}

	public SwingComponentView<?> getPrefixComponent() {
		return this.prefixComponent;
	}

	public void setPrefixComponent( SwingComponentView<?> prefixComponent ) {
		if( this.prefixComponent != prefixComponent ) {
			this.prefixComponent = prefixComponent;
			//			this.revalidateAndRepaint();
		}
	}

	public SwingComponentView<?> getMainComponent() {
		return this.mainComponent;
	}

	public void setMainComponent( SwingComponentView<?> mainComponent ) {
		if( this.mainComponent != mainComponent ) {
			this.mainComponent = mainComponent;
			//			this.revalidateAndRepaint();
		}
	}

	public SwingComponentView<?> getPostfixComponent() {
		return this.postfixComponent;
	}

	public void setPostfixComponent( SwingComponentView<?> postfixComponent ) {
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

	private final class JDropDownButton extends JToggleButton {
		public JDropDownButton() {
			this.setRolloverEnabled( true );
		}

		@Override
		public void updateUI() {
			//this.setUI( new javax.swing.plaf.basic.BasicButtonUI() );
			this.setUI( new DropDownButtonUI( (javax.swing.AbstractButton)this ) );
		}

		@Override
		public Dimension getPreferredSize() {
			return constrainPreferredSizeIfNecessary( super.getPreferredSize() );
		}

		@Override
		public Dimension getMaximumSize() {
			if( DropDown.this.isMaximumSizeClampedToPreferredSize() ) {
				return this.getPreferredSize();
			} else {
				return super.getMaximumSize();
			}
		}

		@Override
		public void paint( Graphics g ) {
			int x = 0;
			int y = 0;
			int width = this.getWidth();
			int height = this.getHeight();
			ButtonModel buttonModel = this.getModel();
			Graphics2D g2 = (Graphics2D)g;

			Paint prevPaint = g2.getPaint();
			boolean isActive = buttonModel.isRollover() || buttonModel.isPressed();
			if( isActive || DropDown.this.isInactiveFeedbackDesired() ) {
				if( isActive ) {
					if( buttonModel.isPressed() ) {
						g2.setColor( ColorUtilities.createGray( 127 ) );
					} else {
						g2.setColor( ColorUtilities.createGray( 220 ) );
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

			Color triangleFill;
			Color triangleOutline;
			if( isActive ) {
				triangleFill = Color.YELLOW;
				triangleOutline = Color.BLACK;
			} else {
				triangleFill = ARROW_COLOR;
				triangleOutline = null;
			}

			g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

			GeneralPath path = new GeneralPath();
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
				g2.setStroke( new BasicStroke( 3.0f ) );
				//				g2.setColor(java.awt.Color.BLUE);
				//				g2.draw(new java.awt.geom.Rectangle2D.Float(1.5f, 1.5f, width - 3.0f, height - 3.0f));
				int xMax = ( x + width ) - 1;
				int yMax = ( y + height ) - 1;
				if( buttonModel.isPressed() ) {
					g2.setColor( Color.BLACK );
				} else {
					g2.setColor( Color.WHITE );
				}
				g2.drawLine( x, yMax, x, y );
				g2.drawLine( x, y, xMax, y );
				if( buttonModel.isPressed() ) {
					g2.setColor( Color.WHITE );
				} else {
					g2.setColor( Color.BLACK );
				}
				g2.drawLine( x, yMax, xMax, yMax );
				g2.drawLine( xMax, yMax, xMax, y );
			} else {
				if( DropDown.this.isInactiveFeedbackDesired() ) {
					g2.setColor( Color.WHITE );
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
		rv.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
		//rv.setBackground(edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray(230));
		rv.setBackground( new Color( 230, 230, 230, 127 ) );
		rv.setFocusable( false );
		rv.setBorder( BorderFactory.createEmptyBorder( 1, 3, 1, 5 + getAffordanceWidth() ) );
		if( ( this.prefixComponent != null ) || ( this.mainComponent != null ) || ( this.postfixComponent != null ) ) {
			//			rv.setModel( new javax.swing.DefaultButtonModel() );
			//rv.setLayout(new javax.swing.BoxLayout(rv, javax.swing.BoxLayout.LINE_AXIS));
			rv.setLayout( new BorderLayout() );
			if( this.prefixComponent != null ) {
				rv.add( this.prefixComponent.getAwtComponent(), BorderLayout.LINE_START );
			}
			if( this.mainComponent != null ) {
				rv.add( this.mainComponent.getAwtComponent(), BorderLayout.CENTER );
			}
			if( this.postfixComponent != null ) {
				rv.add( this.postfixComponent.getAwtComponent(), BorderLayout.LINE_END );
			}
		}
		return rv;
	}
}
