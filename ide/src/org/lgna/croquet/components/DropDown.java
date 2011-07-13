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

package org.lgna.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class DropDown< M extends org.lgna.croquet.Model >  extends org.lgna.croquet.components.AbstractButton<javax.swing.AbstractButton,M> {
	private static final int AFFORDANCE_WIDTH = 6;
	private static final int AFFORDANCE_HALF_HEIGHT = 5;
	private static final java.awt.Color ARROW_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray(191);

	private org.lgna.croquet.components.Component<?> prefixComponent;
	private org.lgna.croquet.components.Component<?> mainComponent;
	private org.lgna.croquet.components.Component<?> postfixComponent;
	public DropDown( M model, org.lgna.croquet.components.Component<?> prefixComponent, org.lgna.croquet.components.Component<?> mainComponent, org.lgna.croquet.components.Component<?> postfixComponent ) {
		super(model);
		this.prefixComponent = prefixComponent;
		this.mainComponent = mainComponent;
		this.postfixComponent = postfixComponent;
		this.setMaximumSizeClampedToPreferredSize( true );
	}
	public DropDown( M model ) {
		this( model, null, null, null );
	}

	public org.lgna.croquet.components.Component< ? > getPrefixComponent() {
		return this.prefixComponent;
	}
	public void setPrefixComponent( org.lgna.croquet.components.Component< ? > prefixComponent ) {
		this.prefixComponent = prefixComponent;
	}
	public org.lgna.croquet.components.Component<?> getMainComponent() {
		return this.mainComponent;
	}
	public void setMainComponent( org.lgna.croquet.components.Component< ? > mainComponent ) {
		this.mainComponent = mainComponent;
	}
	public org.lgna.croquet.components.Component< ? > getPostfixComponent() {
		return this.postfixComponent;
	}
	public void setPostfixComponent( org.lgna.croquet.components.Component< ? > postfixComponent ) {
		this.postfixComponent = postfixComponent;
	}

	protected abstract javax.swing.Action getAction();
	protected boolean isInactiveFeedbackDesired() {
		return true;
	}
	
	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		class JPopupMenuButton extends javax.swing.JButton {
			public JPopupMenuButton() {
				this.setRolloverEnabled(true);
				this.setAction( DropDown.this.getAction() );
			}
			@Override
			public void updateUI() {
				this.setUI(new javax.swing.plaf.basic.BasicButtonUI());
			}
			@Override
			public java.awt.Dimension getMaximumSize() {
				return this.getPreferredSize();
			}
			@Override
			public void paint(java.awt.Graphics g) {
				int x = 0;
				int y = 0;
				int width = this.getWidth();
				int height = this.getHeight();
				javax.swing.ButtonModel buttonModel = this.getModel();
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;

				java.awt.Paint prevPaint = g2.getPaint();
				boolean isActive = buttonModel.isRollover();
				if (isActive || DropDown.this.isInactiveFeedbackDesired()) {
					if (isActive) {
						g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 220 ) );
					} else {
						g2.setColor(this.getBackground());
					}
					g2.fillRect(x, y, width, height);
				}

				super.paint( g );
				
				float x0 = x + width - 4 - AFFORDANCE_WIDTH;
				float x1 = x0 + AFFORDANCE_WIDTH;
				float xC = (x0 + x1) / 2;

				float yC = (y + height) / 2;
				float y0 = yC - AFFORDANCE_HALF_HEIGHT;
				float y1 = yC + AFFORDANCE_HALF_HEIGHT;

				java.awt.Color triangleFill;
				java.awt.Color triangleOutline;
				if (isActive) {
					triangleFill = java.awt.Color.YELLOW;
					triangleOutline = java.awt.Color.BLACK;
				} else {
					triangleFill = ARROW_COLOR;
					triangleOutline = null;
				}

				g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

				java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
				path.moveTo(x0, y0);
				path.lineTo(xC, y1);
				path.lineTo(x1, y0);
				path.closePath();

				g2.setColor(triangleFill);
				g2.fill(path);
				if (triangleOutline != null) {
					g2.setColor(triangleOutline);
					g2.draw(path);
				}

				if (isActive) {
					g2.setStroke(new java.awt.BasicStroke(3.0f));
//					g2.setColor(java.awt.Color.BLUE);
//					g2.draw(new java.awt.geom.Rectangle2D.Float(1.5f, 1.5f, width - 3.0f, height - 3.0f));
					int xMax = x+width-1;
					int yMax = y+height-1;
					g2.setColor( java.awt.Color.WHITE );
					g2.drawLine( x, yMax, x, y );
					g2.drawLine( x, y, xMax, y );
					g2.setColor( java.awt.Color.BLACK );
					g2.drawLine( x, yMax, xMax, yMax );
					g2.drawLine( xMax, yMax, xMax, y );
					
				} else {
					if (DropDown.this.isInactiveFeedbackDesired()) {
						g2.setColor(java.awt.Color.WHITE);
						//g2.drawRect( x, y, width-1, height-1 );
						g2.drawLine(x + 1, y + 1, x + width - 4, y + 1);
						g2.drawLine(x + 1, y + 1, x + 1, y + height - 4);
					}
				}

				g2.setPaint(prevPaint);
			}
		};

		javax.swing.AbstractButton rv = new JPopupMenuButton();

		int insetLeft = 3;
		if (this.prefixComponent != null || this.mainComponent != null || this.postfixComponent != null) {
			//			rv.setModel( new javax.swing.DefaultButtonModel() );
			rv.setLayout(new javax.swing.BoxLayout(rv, javax.swing.BoxLayout.LINE_AXIS));
			if (this.prefixComponent != null) {
				rv.add(this.prefixComponent.getAwtComponent());
			}
			if (this.mainComponent != null) {
				rv.add(this.mainComponent.getAwtComponent());
			}
			if (this.postfixComponent != null) {
				rv.add(this.postfixComponent.getAwtComponent());
			}
		} else {
			insetLeft += 3;
		}
		rv.setRolloverEnabled(true);
		rv.setOpaque(false);
		rv.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
		//rv.setBackground(edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray(230));
		rv.setBackground( new java.awt.Color( 230, 230, 230, 127 ) );
		rv.setBorder(javax.swing.BorderFactory.createEmptyBorder( 1, insetLeft, 1, 5 + AFFORDANCE_WIDTH));
		return rv;
	}

}
