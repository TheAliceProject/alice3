/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.codeeditor;

import org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertCascade;

/**
 * @author Dennis Cosgrove
 */
public class EmptyStatementListAffordanceBorder implements javax.swing.border.Border {
	private static final String TEXT = "drop statement here";
	private static final java.awt.Stroke SOLID_STROKE = new java.awt.BasicStroke( 2.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
	private static final java.awt.Color HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 220 );
	private static final java.awt.Color SHADOW_COLOR = new java.awt.Color( 63, 63, 63 );
	private static final java.awt.Color TOP_COLOR = new java.awt.Color( 0, 0, 0, 63 );
	private static final java.awt.Color BOTTOM_COLOR = new java.awt.Color( 191, 191, 191, 63 );

	private static final java.awt.Stroke DASHED_STROKE = new java.awt.BasicStroke( 1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0, new float[] { 9.0f, 3.0f }, 0 );

	private static final java.awt.Insets EMPTY_INSETS = new java.awt.Insets( 0, 12, 16, 64 );

	private final org.alice.ide.x.AstI18nFactory factory;
	private final org.lgna.project.ast.StatementListProperty alternateListProperty;

	private final java.awt.Insets normalInsets;
	private final int minimum;

	private boolean isDrawingDesired = true;

	public EmptyStatementListAffordanceBorder( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.StatementListProperty alternateListProperty, java.awt.Insets normalInsets, int minimum ) {
		this.factory = factory;
		this.alternateListProperty = alternateListProperty;
		this.normalInsets = normalInsets;
		this.minimum = minimum;
	}

	private static java.awt.geom.Rectangle2D getStringBounds( java.awt.Component c, java.awt.Graphics g ) {
		java.awt.FontMetrics fontMetrics = c.getFontMetrics( c.getFont() );
		if( fontMetrics != null ) {
			return fontMetrics.getStringBounds( TEXT, g );
		} else {
			return null;
		}
	}

	public java.awt.Insets getBorderInsets( java.awt.Component c ) {
		java.awt.Container container = (java.awt.Container)c;
		if( this.isVirtuallyEmpty( container ) ) {
			java.awt.Graphics g = c.getGraphics();
			java.awt.geom.Rectangle2D bounds = getStringBounds( c, g );
			if( g != null ) {
				g.dispose();
			}
			if( bounds != null ) {
				return new java.awt.Insets( EMPTY_INSETS.top, EMPTY_INSETS.left, EMPTY_INSETS.bottom + (int)bounds.getHeight(), EMPTY_INSETS.right + (int)bounds.getWidth() );
			} else {
				return EMPTY_INSETS;
			}
		} else {
			return this.normalInsets;
		}
	}

	public boolean isVirtuallyEmpty( java.awt.Container container ) {
		return container.getComponentCount() <= this.minimum;
	}

	public boolean isBorderOpaque() {
		return false;
	}

	private boolean isDashed() {
		return ( alternateListProperty != null ) && ( alternateListProperty.size() > 0 );
	}

	private boolean isEditable() {
		return this.factory instanceof org.alice.ide.x.ProjectEditorAstI18nFactory;
	}

	public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int w, int h ) {
		java.awt.Container container = (java.awt.Container)c;
		if( this.isVirtuallyEmpty( container ) ) {
			if( this.isEditable() ) {
				if( this.isDrawingDesired() ) {
					java.awt.geom.Rectangle2D bounds = getStringBounds( c, g );

					int width = ( EMPTY_INSETS.right + (int)bounds.getWidth() ) - 16;
					int height = ( EMPTY_INSETS.bottom + (int)bounds.getHeight() ) - 4;
					int dx = x + EMPTY_INSETS.left;
					int dy;
					if( container.getComponentCount() == 0 ) {
						dy = y + EMPTY_INSETS.top + 2;
					} else {
						dy = ( y + h ) - height - 2;
					}
					g.translate( dx, dy );
					if( this.isDashed() ) {
						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
						g2.setColor( java.awt.Color.GRAY );
						g2.setStroke( DASHED_STROKE );
						java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( 1, 1, width - 3, height - 3, 8, 8 );
						g2.draw( rr );
					} else {
						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
						java.awt.Paint prevPaint = g2.getPaint();

						java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( 0, 0, width - 1, height - 1, 8, 8 );
						g2.setPaint( new java.awt.GradientPaint( 0, 0, TOP_COLOR, 0, height, BOTTOM_COLOR ) );
						g2.fill( rr );

						edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.draw3DRoundRectangle( g2, rr, SHADOW_COLOR, HIGHLIGHT_COLOR, SOLID_STROKE );
						g2.setPaint( prevPaint );
					}
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					g.setColor( java.awt.Color.BLACK );
					Object prevTextAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING );
					g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, TEXT, 8, 0, (int)bounds.getWidth(), height );
					g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, prevTextAntialiasing );
					g.translate( -dx, -dy );
				}
			}
		}
	}

	public boolean isDrawingDesired() {
		return this.isDrawingDesired && ( StatementInsertCascade.EPIC_HACK_isActive() == false );
	}

	public void setDrawingDesired( boolean isDrawingDesired ) {
		this.isDrawingDesired = isDrawingDesired;
	}
}
