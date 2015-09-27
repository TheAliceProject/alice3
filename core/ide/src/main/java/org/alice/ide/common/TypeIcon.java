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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class TypeIcon implements javax.swing.Icon {
	private static final int INDENT_PER_DEPTH = 12;
	private static final int BONUS_GAP = 4;
	private final org.lgna.project.ast.AbstractType<?, ?, ?> type;
	private final TypeBorder border;
	private final boolean isIndentForDepthAndMemberCountTextDesired;
	private final java.awt.Font typeFont;
	private final java.awt.Font bonusFont;

	public static TypeIcon getInstance( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return new TypeIcon( type );
	}

	public TypeIcon( org.lgna.project.ast.AbstractType<?, ?, ?> type, boolean isIndentForDepthAndMemberCountTextDesired, java.awt.Font typeFont, java.awt.Font bonusFont ) {
		this.type = type;
		this.border = TypeBorder.getSingletonFor( type );
		this.isIndentForDepthAndMemberCountTextDesired = isIndentForDepthAndMemberCountTextDesired;
		this.typeFont = typeFont;
		this.bonusFont = bonusFont;
	}

	public TypeIcon( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		this( type, false, javax.swing.UIManager.getFont( "defaultFont" ), null );
	}

	protected java.awt.Font getTypeFont() {
		return this.typeFont;
	}

	public java.awt.Font getBonusFont() {
		return this.bonusFont;
	}

	protected java.awt.Color getTextColor( java.awt.Component c ) {
		if( c.isEnabled() ) {
			return java.awt.Color.BLACK;
		} else {
			return java.awt.Color.GRAY;
		}
	}

	private String getTypeText() {
		org.alice.ide.formatter.Formatter formatter = org.alice.ide.croquet.models.ui.formatter.FormatterState.getInstance().getValue();
		return formatter.getTextForType( this.type );
	}

	private String getBonusText() {
		if( isIndentForDepthAndMemberCountTextDesired ) {
			if( this.type instanceof org.lgna.project.ast.NamedUserType ) {
				org.lgna.project.ast.NamedUserType userType = (org.lgna.project.ast.NamedUserType)this.type;
				int count = 0;
				for( org.lgna.project.ast.UserMethod method : userType.methods ) {
					if( method.getManagementLevel() == org.lgna.project.ast.ManagementLevel.NONE ) {
						count += 1;
					}
				}
				count += userType.fields.size();
				if( count > 0 ) {
					StringBuilder sb = new StringBuilder();
					sb.append( "(" );
					sb.append( count );
					sb.append( ")" );
					return sb.toString();
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

	private static java.awt.geom.Rectangle2D getTextBounds( String text, java.awt.Font font ) {
		if( text != null ) {
			java.awt.Graphics g = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.getGraphics();
			java.awt.FontMetrics fm;
			if( font != null ) {
				fm = g.getFontMetrics( font );
			} else {
				fm = g.getFontMetrics();
			}
			return fm.getStringBounds( text, g );
		} else {
			return new java.awt.geom.Rectangle2D.Float( 0, 0, 0, 0 );
		}
	}

	private java.awt.geom.Rectangle2D getTypeTextBounds() {
		return getTextBounds( this.getTypeText(), this.getTypeFont() );
	}

	private java.awt.geom.Rectangle2D getBonusTextBounds() {
		return getTextBounds( this.getBonusText(), this.getBonusFont() );
	}

	private int getBorderWidth() {
		java.awt.Insets insets = this.border.getBorderInsets( null );
		java.awt.geom.Rectangle2D typeTextBounds = this.getTypeTextBounds();
		return insets.left + insets.right + (int)typeTextBounds.getWidth();
	}

	private int getBorderHeight() {
		java.awt.Insets insets = this.border.getBorderInsets( null );
		java.awt.geom.Rectangle2D bounds = this.getTypeTextBounds();
		return insets.top + insets.bottom + (int)bounds.getHeight();
	}

	@Override
	public int getIconWidth() {
		int rv = this.getBorderWidth();
		if( this.isIndentForDepthAndMemberCountTextDesired ) {
			int depth = org.lgna.project.ast.StaticAnalysisUtilities.getUserTypeDepth( type );
			if( depth > 0 ) {
				rv += ( depth * INDENT_PER_DEPTH );
			}
		}
		if( this.isIndentForDepthAndMemberCountTextDesired ) {
			rv += BONUS_GAP;
			java.awt.geom.Rectangle2D bonusTextBounds = this.getBonusTextBounds();
			rv += (int)bonusTextBounds.getWidth();
		}
		return rv;
	}

	@Override
	public int getIconHeight() {
		return this.getBorderHeight();
	}

	@Override
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {

		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		java.awt.geom.AffineTransform prevTransform = g2.getTransform();

		//g.setColor( java.awt.Color.BLUE );
		//g.fillRect( x, y, this.getIconWidth(), this.getIconHeight() );

		int typePlusBonusWidth = this.getIconWidth();
		if( this.isIndentForDepthAndMemberCountTextDesired ) {
			int depth = org.lgna.project.ast.StaticAnalysisUtilities.getUserTypeDepth( type );
			if( depth > 0 ) {
				int dx = depth * INDENT_PER_DEPTH;
				g2.translate( dx, 0 );
				typePlusBonusWidth -= dx;
				typePlusBonusWidth -= BONUS_GAP;
			}
		}

		int w = this.getBorderWidth();
		int h = this.getBorderHeight();

		//g.setColor( java.awt.Color.GREEN );
		//g.fillRect( x, y, typePlusBonusWidth, this.getIconHeight() );

		//g.setColor( java.awt.Color.RED );
		//g.fillRect( x, y, w, h );
		this.border.paintBorder( c, g, x, y, w, h );
		g.setColor( this.getTextColor( c ) );

		java.awt.Font prevFont = g.getFont();
		g.setFont( this.getTypeFont() );
		edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, this.getTypeText(), x, y, w, h );

		if( this.isIndentForDepthAndMemberCountTextDesired ) {
			if( this.bonusFont != null ) {
				g.setFont( this.bonusFont );
				edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, this.getBonusText(), x + w + BONUS_GAP, y, typePlusBonusWidth - w, h );
			}
		}
		g.setFont( prevFont );
		g2.setTransform( prevTransform );
	}
}
