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
public class ToolPaletteTitle extends BooleanStateButton<javax.swing.AbstractButton> {
	public static enum RenderingStyle {
		LIGHT_UP_ICON_ONLY {
			@Override
			public boolean isShaded( javax.swing.ButtonModel buttonModel ) {
				return false;
			}
		},
		SHADE_WHEN_ACTIVE {
			@Override
			public boolean isShaded( javax.swing.ButtonModel buttonModel ) {
				return buttonModel.isRollover();
			}
		},
		SHADE_AGGRESSIVELY {
			@Override
			public boolean isShaded( javax.swing.ButtonModel buttonModel ) {
				return true;
			}
		};
		public abstract boolean isShaded( javax.swing.ButtonModel buttonModel );
	}

	private static class ArrowIcon extends edu.cmu.cs.dennisc.javax.swing.icons.AbstractArrowIcon {
		public ArrowIcon( int size ) {
			super( size );
		}

		@Override
		public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			javax.swing.ButtonModel buttonModel = button.getModel();
			Heading heading;
			if( buttonModel.isSelected() || buttonModel.isPressed() ) {
				heading = Heading.SOUTH;
			} else {
				heading = Heading.EAST;
			}
			java.awt.geom.GeneralPath path = this.createPath( x, y, heading );
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			java.awt.Paint fillPaint;
			java.awt.Paint drawPaint = java.awt.Color.BLACK;
			if( buttonModel.isPressed() ) {
				fillPaint = java.awt.Color.WHITE;
			} else {
				if( buttonModel.isRollover() ) {
					fillPaint = java.awt.Color.YELLOW;
				} else {
					fillPaint = java.awt.Color.DARK_GRAY;
					drawPaint = null;
				}
			}
			Object antialiasingValue = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

			g2.setPaint( fillPaint );
			g2.fill( path );
			if( drawPaint != null ) {
				g2.setPaint( drawPaint );
				g2.draw( path );
			}
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, antialiasingValue );
		}
	}

	private static final ArrowIcon ARROW_ICON = new ArrowIcon( 12 );

	private static java.awt.Insets SUPPRESSED_INSETS = new java.awt.Insets( 0, 0, 0, 0 );
	private static java.awt.Insets INERT_INSETS = new java.awt.Insets( 2, 2, 2, 2 );
	private static java.awt.Insets ACTIVE_INSETS = new java.awt.Insets( 2, 10 + ARROW_ICON.getIconWidth(), 2, 2 );

	private static enum ToolPaletteTitleBorder implements javax.swing.border.Border {
		SINGLETON;
		@Override
		public java.awt.Insets getBorderInsets( java.awt.Component c ) {
			JToolPaletteTitle b = (JToolPaletteTitle)c;
			if( b.isSuppressed ) {
				return SUPPRESSED_INSETS;
			} else {
				if( b.isInert ) {
					return INERT_INSETS;
				} else {
					return ACTIVE_INSETS;
				}
			}
		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}

		@Override
		public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
		}
	}

	private static class JToolPaletteTitle extends javax.swing.JToggleButton {
		private boolean isRoundedOnTop = false;
		private boolean isPartOfAccordion = false;
		private RenderingStyle renderingStyle = RenderingStyle.SHADE_AGGRESSIVELY;
		private boolean isInert = false;
		private boolean isSuppressed = false;

		@Override
		public boolean contains( int x, int y ) {
			if( this.isInert || ( this.isPartOfAccordion && this.isSelected() ) ) {
				return false;
			} else {
				return super.contains( x, y );
			}
		}

		//		@Override
		//		public java.awt.Dimension getMinimumSize() {
		//			if( this.isSuppressed ) {
		//				return new java.awt.Dimension( 0, 0 );
		//			} else {
		//				return super.getMinimumSize();
		//			}
		//		}
		//
		//		@Override
		//		public java.awt.Dimension getPreferredSize() {
		//			if( this.isSuppressed ) {
		//				return new java.awt.Dimension( 0, 0 );
		//			} else {
		//				return super.getPreferredSize();
		//			}
		//		}
		//
		//		@Override
		//		public java.awt.Dimension getMaximumSize() {
		//			if( this.isSuppressed ) {
		//				return new java.awt.Dimension( 0, 0 );
		//			} else {
		//				return super.getMaximumSize();
		//			}
		//		}

		public boolean isRoundedOnTop() {
			return this.isRoundedOnTop;
		}

		public void setRoundedOnTop( boolean isRoundedOnTop ) {
			if( this.isRoundedOnTop != isRoundedOnTop ) {
				this.isRoundedOnTop = isRoundedOnTop;
				this.repaint();
			}
		}

		public boolean isPartOfAccordion() {
			return this.isPartOfAccordion;
		}

		public void setPartOfAccordion( boolean isPartOfAccordion ) {
			if( this.isPartOfAccordion != isPartOfAccordion ) {
				this.isPartOfAccordion = isPartOfAccordion;
				this.repaint();
			}
		}

		public RenderingStyle getRenderingStyle() {
			return this.renderingStyle;
		}

		public void setRenderingStyle( RenderingStyle renderingStyle ) {
			if( this.renderingStyle != renderingStyle ) {
				this.renderingStyle = renderingStyle;
				this.repaint();
			}
		}

		public boolean isInert() {
			return this.isInert;
		}

		public void setInert( boolean isInert ) {
			if( this.isInert != isInert ) {
				this.isInert = isInert;
				this.repaint();
			}
		}

		public boolean isSuppressed() {
			return this.isSuppressed;
		}

		public void setSuppressed( boolean isSuppressed ) {
			if( this.isSuppressed != isSuppressed ) {
				this.isSuppressed = isSuppressed;
				this.revalidate();
				this.repaint();
			}
		}

		@Override
		public boolean isOpaque() {
			return this.isRoundedOnTop == false;
		}

		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			super.paintComponent( g );
			if( this.isSuppressed ) {
				//pass
			} else {
				if( this.isInert ) {
					//pass
				} else {
					int x = this.isRoundedOnTop ? 8 : 4;
					int height = this.getHeight();
					int iconHeight = ARROW_ICON.getIconHeight();
					int y = ( height - iconHeight ) / 2;
					ARROW_ICON.paintIcon( this, g2, x, y );
				}
			}
		}

		@Override
		public void updateUI() {
			this.setUI( new ToolPaletteTitleButtonUI() );
		}
	}

	private static java.awt.Shape createRoundedOnTopShape( int width, int height, int round ) {
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( 0, height );
		path.lineTo( 0, round );
		path.quadTo( 0, 0, round, 0 );
		path.lineTo( width - round, 0 );
		path.quadTo( width, 0, width, round );
		path.lineTo( width, height );
		path.closePath();
		return path;
	}

	private static class ToolPaletteTitleButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
		@Override
		public java.awt.Dimension getMinimumSize( javax.swing.JComponent c ) {
			JToolPaletteTitle b = (JToolPaletteTitle)c;
			if( b.isSuppressed ) {
				return new java.awt.Dimension( 0, 0 );
			} else {
				return super.getMinimumSize( c );
			}
		}

		@Override
		public java.awt.Dimension getPreferredSize( javax.swing.JComponent c ) {
			JToolPaletteTitle b = (JToolPaletteTitle)c;
			if( b.isSuppressed ) {
				return new java.awt.Dimension( 0, 0 );
			} else {
				return super.getPreferredSize( c );
			}
		}

		@Override
		public java.awt.Dimension getMaximumSize( javax.swing.JComponent c ) {
			JToolPaletteTitle b = (JToolPaletteTitle)c;
			if( b.isSuppressed ) {
				return new java.awt.Dimension( 0, 0 );
			} else {
				return super.getMaximumSize( c );
			}
		}

		@Override
		public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			java.awt.Shape prevClip = g2.getClip();
			try {
				JToolPaletteTitle b = (JToolPaletteTitle)c;
				if( b.isSuppressed() ) {
					//pass
				} else {
					if( b.isInert() ) {
						//pass
					} else {
						javax.swing.ButtonModel buttonModel = b.getModel();
						if( b.isRoundedOnTop() ) {
							g2.setClip( edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createIntersection( prevClip, createRoundedOnTopShape( b.getWidth(), b.getHeight(), ARROW_ICON.getIconWidth() ) ) );
						}

						java.awt.Rectangle r = javax.swing.SwingUtilities.getLocalBounds( c );
						java.awt.Color background = c.getBackground();
						RenderingStyle renderingStyle = b.getRenderingStyle();
						if( renderingStyle.isShaded( buttonModel ) ) {
							if( buttonModel.isPressed() ) {
								g2.setPaint( background.darker() );
								g2.fillRect( 0, 0, b.getWidth(), b.getHeight() );
							} else {
								double brightnessScale;
								if( buttonModel.isRollover() ) {
									brightnessScale = 1.2;
								} else {
									brightnessScale = 1.1;
								}
								java.awt.Color HIGHLIGHT_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( background, 1.0, 1.0, brightnessScale );
								java.awt.Color SHADOW_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( background, 1.0, 1.0, 0.8 );
								edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillGradientRectangle( g2, r, SHADOW_COLOR, HIGHLIGHT_COLOR, background, 0.4f );
							}
						} else {
							g2.setPaint( background );
							g2.fillRect( 0, 0, b.getWidth(), b.getHeight() );
						}
					}
				}
				super.paint( g, c );
			} finally {
				g2.setClip( prevClip );
			}
		}
	}

	public ToolPaletteTitle( org.lgna.croquet.BooleanState booleanState ) {
		super( booleanState );
	}

	public boolean isRoundedOnTop() {
		return this.getJPaletteTitle().isRoundedOnTop();
	}

	public void setRoundedOnTop( boolean isRoundedOnTop ) {
		this.getJPaletteTitle().setRoundedOnTop( isRoundedOnTop );
	}

	public boolean isPartOfAccordion() {
		return this.getJPaletteTitle().isPartOfAccordion();
	}

	public void setPartOfAccordion( boolean isPartOfAccordion ) {
		this.getJPaletteTitle().setPartOfAccordion( isPartOfAccordion );
	}

	public RenderingStyle getRenderingStyle() {
		return this.getJPaletteTitle().getRenderingStyle();
	}

	public void setRenderingStyle( RenderingStyle renderingStyle ) {
		this.getJPaletteTitle().setRenderingStyle( renderingStyle );
	}

	public boolean isInert() {
		return this.getJPaletteTitle().isInert();
	}

	public void setInert( boolean isInert ) {
		this.getJPaletteTitle().setInert( isInert );
	}

	public boolean isSuppressed() {
		return this.getJPaletteTitle().isSuppressed();
	}

	public void setSuppressed( boolean isSuppressed ) {
		this.getJPaletteTitle().setSuppressed( isSuppressed );
	}

	private JToolPaletteTitle getJPaletteTitle() {
		return (JToolPaletteTitle)this.getAwtComponent();
	}

	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		javax.swing.AbstractButton rv = new JToolPaletteTitle();
		rv.setRolloverEnabled( true );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
		rv.setBorder( ToolPaletteTitleBorder.SINGLETON );
		rv.setOpaque( false );
		return rv;
	}
}
