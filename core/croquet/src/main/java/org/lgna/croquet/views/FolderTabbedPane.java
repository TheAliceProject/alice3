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

import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.Operation;
import org.lgna.croquet.TabState;

/**
 * @author Dennis Cosgrove
 */
public class FolderTabbedPane<E extends org.lgna.croquet.TabComposite<?>> extends CardBasedTabbedPane<E> {
	private static final int TRAILING_TAB_PAD = 32;
	public static final java.awt.Color DEFAULT_BACKGROUND_COLOR = new java.awt.Color( 173, 167, 208 ).darker();

	private static class FolderTabTitleUI extends javax.swing.plaf.basic.BasicToggleButtonUI {
		@Override
		public java.awt.Dimension getPreferredSize( javax.swing.JComponent c ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			java.awt.Font font = button.getFont();
			java.awt.FontMetrics fm = button.getFontMetrics( font );
			String text = button.getText();
			javax.swing.Icon icon = button.getIcon();
			java.awt.Dimension size;
			if( icon != null ) {
				int verticalAlignment = button.getVerticalAlignment();
				int horizontalAlignment = button.getHorizontalAlignment();
				int verticalTextPosition = button.getVerticalTextPosition();
				int horizontalTextPosition = button.getHorizontalTextPosition();
				java.awt.Rectangle viewR = new java.awt.Rectangle( Short.MAX_VALUE, Short.MAX_VALUE );
				java.awt.Rectangle iconR = new java.awt.Rectangle();
				java.awt.Rectangle textR = new java.awt.Rectangle();
				int textIconGap = button.getIconTextGap();
				javax.swing.SwingUtilities.layoutCompoundLabel( c, fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewR, iconR, textR, textIconGap );

				size = iconR.union( textR ).getSize();
			} else {
				size = fm.getStringBounds( text, button.getGraphics() ).getBounds().getSize();
			}

			java.awt.Insets insets = button.getInsets();
			size.width += insets.left + insets.right;
			size.height += insets.top + insets.bottom;

			if( button.getComponentCount() > 0 ) {
				for( java.awt.Component component : button.getComponents() ) {
					//if( component.isVisible() ) {
					size.width += 4;
					size.width += component.getPreferredSize().width;
					//}
				}
			}

			return size;
		}

		@Override
		public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			javax.swing.Icon icon = button.getIcon();
			if( icon != null ) {
				super.paint( g, c );
			} else {
				String text = button.getText();
				java.awt.Insets insets = button.getInsets();
				int x = insets.left;
				if( button.getComponentOrientation().isLeftToRight() ) {
					//pass
				} else {
					for( java.awt.Component component : button.getComponents() ) {
						x += component.getPreferredSize().width;
						x += 4;
					}
				}
				g.drawString( text, x, button.getBaseline( c.getWidth(), c.getHeight() ) );
			}
		}
	}

	private static class JFolderTabTitle extends javax.swing.JToggleButton {
		private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged( java.awt.event.ItemEvent e ) {
				JFolderTabTitle.this.revalidate();
			}
		};

		public JFolderTabTitle() {
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 8 ) );
			this.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
			this.setAlignmentY( java.awt.Component.BOTTOM_ALIGNMENT );
			this.setHorizontalTextPosition( javax.swing.SwingConstants.LEADING );
			this.setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
			this.setLayout( new javax.swing.SpringLayout() );
		}

		@Override
		public boolean isOpaque() {
			return false;
		}

		@Override
		public void updateUI() {
			this.setUI( new FolderTabTitleUI() );
		}

		@Override
		public void addNotify() {
			super.addNotify();
			this.getModel().addItemListener( this.itemListener );
		}

		@Override
		public void removeNotify() {
			this.getModel().removeItemListener( this.itemListener );
			super.removeNotify();
		}

		@Override
		public void repaint() {
			java.awt.Container parent = this.getParent();
			if( parent != null ) {
				parent.repaint( this.getX(), this.getY(), this.getWidth() + TRAILING_TAB_PAD, this.getHeight() );
			} else {
				super.repaint();
			}
		}
	}

	private class FolderTabTitle extends BooleanStateButton<javax.swing.AbstractButton> {
		private final javax.swing.JButton closeButton;

		public FolderTabTitle( final E item, BooleanState booleanState ) {
			super( booleanState );

			if( item.isPotentiallyCloseable() ) {
				java.awt.event.ActionListener closeButtonActionListener = new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed( java.awt.event.ActionEvent e ) {
						FolderTabbedPane.this.getModel().removeItemAndSelectAppropriateReplacement( item );
					}
				};
				this.closeButton = new edu.cmu.cs.dennisc.javax.swing.components.JCloseButton( true );
				this.closeButton.addActionListener( closeButtonActionListener );
			} else {
				this.closeButton = null;
			}
		}

		public void setCloseable( boolean isCloseable ) {
			if( this.closeButton != null ) {
				if( isCloseable != ( this.closeButton.getParent() != null ) ) {
					javax.swing.AbstractButton awtButton = this.getAwtComponent();
					if( isCloseable ) {
						edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal horizontal;
						if( this.getComponentOrientation().isLeftToRight() ) {
							horizontal = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal.EAST;
						} else {
							horizontal = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal.WEST;
						}
						edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( awtButton, this.closeButton, horizontal, -1, edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Vertical.NORTH, 2 );
					} else {
						awtButton.remove( this.closeButton );
					}
					awtButton.revalidate();
					awtButton.repaint();
				}
			}
		}

		@Override
		protected javax.swing.AbstractButton createAwtComponent() {
			JFolderTabTitle rv = new JFolderTabTitle();
			//			{
			//				@Override
			//				public void setComponentOrientation(java.awt.ComponentOrientation componentOrientation) {
			//					super.setComponentOrientation(componentOrientation);
			//					edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "adjust spring based on ", componentOrientation );
			//				}
			//			};
			return rv;
		}
	}

	private static java.awt.Color SELECTED_BORDER_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 221 );
	//private static java.awt.Color SELECTED_BORDER_COLOR = java.awt.Color.RED;
	private static java.awt.Color UNSELECTED_BORDER_COLOR = java.awt.Color.DARK_GRAY;

	protected static class TitlesPanel extends LineAxisPanel {
		private static final int NORTH_AREA_PAD = 1;
		// private static java.awt.Stroke SELECTED_STROKE = new
		// java.awt.BasicStroke( 3.0f );
		private static java.awt.Stroke SELECTED_STROKE = new java.awt.BasicStroke( 1.0f );
		private static java.awt.Stroke UNSELECTED_STROKE = new java.awt.BasicStroke( 2.0f );

		protected static class JTitlesPanel extends javax.swing.JPanel {
			@Override
			public java.awt.Dimension getPreferredSize() {
				java.awt.Dimension rv = super.getPreferredSize();
				rv.width += TRAILING_TAB_PAD;
				return rv;
			}

			private java.awt.geom.GeneralPath addToPath( java.awt.geom.GeneralPath rv, float x, float y, float width, float height, boolean isContinuation ) {
				float a = height * 0.25f;

				float xStart;
				float xEnd;
				float xA;
				float tabPad;
				if( this.getComponentOrientation().isLeftToRight() ) {
					xStart = x;
					xEnd = ( x + width ) - 1;
					tabPad = TRAILING_TAB_PAD;
					xA = xStart + a;
				} else {
					xStart = ( x + width ) - 1;
					xEnd = x;
					tabPad = -TRAILING_TAB_PAD;
					xA = xStart - a;
				}

				float xCurve0 = xEnd - ( tabPad / 2 );
				float xCurve1 = xEnd + tabPad;
				float cx0 = xCurve0 + ( tabPad * 0.75f );
				float cx1 = xCurve0;

				float y0 = y + NORTH_AREA_PAD;
				float y1 = y + height + 1;// + this.contentBorderInsets.top;
				float cy0 = y0;
				float cy1 = y1;

				float yA = y + a;

				if( isContinuation ) {
					rv.lineTo( xCurve1, y1 );
				} else {
					rv.moveTo( xCurve1, y1 );
				}
				rv.lineTo( xCurve1, y1 - 1 );
				rv.curveTo( cx1, cy1, cx0, cy0, xCurve0, y0 );
				rv.lineTo( xA, y0 );
				rv.quadTo( xStart, y0, xStart, yA );
				rv.lineTo( xStart, y1 );

				return rv;
			}

			private void paintTabBorder( java.awt.Graphics2D g2, javax.swing.AbstractButton button ) {
				int x = button.getX();
				int y = button.getY();
				int width = button.getWidth();
				int height = button.getHeight();
				java.awt.geom.GeneralPath path = this.addToPath( new java.awt.geom.GeneralPath(), x, y, width, height, false );
				java.awt.Shape prevClip = g2.getClip();
				try {
					if( button.getModel().isSelected() ) {
						java.awt.Rectangle bounds = prevClip.getBounds();
						bounds.height += 1;
						//todo: investigate
						//							java.awt.Rectangle lineBelow = new java.awt.Rectangle( , , , 1 );
						//							java.awt.Shape clip = edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createUnion( prevClip, lineBelow );
						//							g2.setClip( clip );
						g2.setClip( bounds );
					}

					g2.draw( path );
				} finally {
					g2.setClip( prevClip );
				}
			}

			private void paintTabBackground( java.awt.Graphics2D g2, javax.swing.AbstractButton button ) {
				java.awt.Color prev = g2.getColor();
				try {
					int x = button.getX();
					int y = button.getY();
					int width = button.getWidth();
					int height = button.getHeight();
					boolean isSelected = button.isSelected();
					boolean isRollover = button.getModel().isArmed();
					java.awt.Color color = button.getBackground();
					if( isSelected ) {
						// pass
					} else {
						color = color.darker();
						if( isRollover ) {
							// pass
						} else {
							color = color.darker();
						}
					}
					g2.setColor( color );
					java.awt.geom.GeneralPath path = addToPath( new java.awt.geom.GeneralPath(), x, y, width, height, false );
					g2.fill( path );
				} finally {
					g2.setColor( prev );
				}
			}

			@Override
			protected void paintChildren( java.awt.Graphics g ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				javax.swing.AbstractButton selectedButton = null;
				java.awt.Component[] components = this.getComponents();
				final int N = components.length;
				for( int i = 0; i < N; i++ ) {
					java.awt.Component component = components[ N - 1 - i ];
					if( component instanceof javax.swing.AbstractButton ) {
						javax.swing.AbstractButton button = (javax.swing.AbstractButton)component;
						if( button.isSelected() ) {
							selectedButton = button;
						} else {
							g2.setColor( UNSELECTED_BORDER_COLOR );
							g2.setStroke( UNSELECTED_STROKE );
							this.paintTabBorder( g2, button );
							this.paintTabBackground( g2, button );
						}
					}
				}
				if( selectedButton != null ) {
					this.paintTabBackground( g2, selectedButton );
					g2.setColor( SELECTED_BORDER_COLOR );
					g2.setStroke( SELECTED_STROKE );
					this.paintTabBorder( g2, selectedButton );
				}
				super.paintChildren( g2 );
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
			}
		}

		@Override
		protected javax.swing.JPanel createJPanel() {
			return new JTitlesPanel();
		}
	}

	private final TitlesPanel titlesPanel = this.createTitlesPanel();
	private final ScrollPane titlesScrollPane = new ScrollPane( this.titlesPanel );
	private final BorderPanel innerHeaderPanel = new BorderPanel();
	private final BorderPanel outerHeaderPanel = new BorderPanel();

	protected TitlesPanel createTitlesPanel() {
		return new TitlesPanel();
	}

	//private java.util.Map<E, javax.swing.Action> mapItemToAction = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private javax.swing.Action getActionFor( E item ) {
		Operation operation = this.getModel().getItemSelectionOperation( item );
		operation.initializeIfNecessary();
		return operation.getImp().getSwingModel().getAction();
	}

	//todo: PopupOperation
	private class PopupOperation extends ActionOperation {
		public PopupOperation() {
			super( Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "7923b4c8-6a9f-4c8b-99b5-909ae6c0889a" ) );
		}

		@Override
		protected void localize() {
			super.localize();
			this.setName( ">>" );
		}

		@Override
		protected void perform( org.lgna.croquet.history.CompletionStep<?> step ) {
			org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
			javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
			javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
			for( E item : FolderTabbedPane.this.getModel() ) {
				if( item != null ) {
					javax.swing.JCheckBoxMenuItem checkBox = new javax.swing.JCheckBoxMenuItem( getActionFor( item ) );
					checkBox.setSelected( FolderTabbedPane.this.getModel().getValue() == item );
					popupMenu.add( checkBox );
					buttonGroup.add( checkBox );
				} else {
					popupMenu.addSeparator();
				}
			}
			ViewController<?, ?> viewController = trigger.getViewController();
			popupMenu.show( viewController.getAwtComponent(), 0, viewController.getHeight() );
		}
	}

	private class PopupButton extends OperationButton<javax.swing.JButton, Operation> {
		public PopupButton( Operation operation ) {
			super( operation );
		}

		@Override
		protected final javax.swing.JButton createAwtComponent() {
			javax.swing.JButton rv = new javax.swing.JButton() {
				@Override
				public String getText() {
					if( isTextClobbered() ) {
						return getClobberText();
					} else {
						return super.getText();
					}
				}

				private boolean isNecessary() {
					java.awt.Container parent = this.getParent();
					if( parent != null ) {
						int width = parent.getWidth();
						int preferredWidth = parent.getPreferredSize().width;
						return width < ( preferredWidth - TRAILING_TAB_PAD );
					} else {
						return false;
					}
				}

				@Override
				public void paint( java.awt.Graphics g ) {
					if( isNecessary() ) {
						super.paint( g );
					} else {
						g.setColor( FolderTabbedPane.this.getBackgroundColor() );
						g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
					}
				}

				@Override
				public boolean contains( int x, int y ) {
					if( isNecessary() ) {
						return super.contains( x, y );
					} else {
						return false;
					}
				}
			};
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 4, 2, 4 ) );
			return rv;
		}
	}

	private class ScrollListener implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
		private Integer pressedLocationX;
		private Integer pressedViewPositionX;

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
			this.pressedLocationX = e.getPoint().x;
			this.pressedViewPositionX = titlesScrollPane.getAwtComponent().getViewport().getViewPosition().x;
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			this.pressedLocationX = null;
			this.pressedViewPositionX = null;
		}

		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseDragged( java.awt.event.MouseEvent e ) {
			if( this.pressedLocationX != null ) {
				java.awt.Dimension viewSize = titlesScrollPane.getAwtComponent().getViewport().getView().getSize();
				java.awt.Rectangle viewportRect = titlesScrollPane.getAwtComponent().getViewport().getViewRect();

				int xDelta = this.pressedLocationX - e.getX();
				int value = this.pressedViewPositionX + xDelta;
				value = Math.max( value, 0 );
				value = Math.min( value, viewSize.width - viewportRect.width );

				titlesScrollPane.getAwtComponent().getViewport().setViewPosition( new java.awt.Point( value, 0 ) );
			}
		}
	}

	private final ScrollListener scrollListener = new ScrollListener();

	public FolderTabbedPane( TabState<E, ?> model ) {
		super( model );
		org.lgna.croquet.CardOwnerComposite cardOwner = this.getCardOwner();
		cardOwner.getView().setBackgroundColor( null );
		this.innerHeaderPanel.setBackgroundColor( null );
		this.titlesPanel.setBackgroundColor( DEFAULT_BACKGROUND_COLOR );
		this.titlesScrollPane.setBackgroundColor( DEFAULT_BACKGROUND_COLOR );
		this.titlesScrollPane.setHorizontalScrollbarPolicy( org.lgna.croquet.views.ScrollPane.HorizontalScrollbarPolicy.NEVER );
		this.titlesScrollPane.setVerticalScrollbarPolicy( org.lgna.croquet.views.ScrollPane.VerticalScrollbarPolicy.NEVER );

		this.titlesScrollPane.addMouseListener( this.scrollListener );
		this.titlesScrollPane.addMouseMotionListener( this.scrollListener );

		this.titlesScrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 0, 0, 0 ) );
		cardOwner.getView().setBorder( new javax.swing.border.Border() {
			@Override
			public java.awt.Insets getBorderInsets( java.awt.Component c ) {
				return new java.awt.Insets( 1, 1, 0, 0 );
			}

			@Override
			public boolean isBorderOpaque() {
				return true;
			}

			@Override
			public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
				if( titlesPanel.getComponentCount() > 0 ) {
					g.setColor( SELECTED_BORDER_COLOR );
					g.fillRect( x, y, width, 1 );
					g.fillRect( x, y, 1, height );
					for( AwtComponentView<?> component : titlesPanel.getComponents() ) {
						if( component instanceof AbstractButton<?, ?> ) {
							AbstractButton<?, ?> button = (AbstractButton<?, ?>)component;
							if( button.getAwtComponent().getModel().isSelected() ) {
								java.awt.Rectangle bounds = button.getBounds( AwtContainerView.lookup( c ) );
								g.setColor( button.getBackgroundColor() );
								int x0;
								if( c.getComponentOrientation().isLeftToRight() ) {
									x0 = bounds.x;
								} else {
									x0 = bounds.x - TRAILING_TAB_PAD;
								}
								g.fillRect( x0, y, ( bounds.width - 1 ) + TRAILING_TAB_PAD, 1 );
							}
						}
					}
				}
			}
		} );
		this.setBackgroundColor( DEFAULT_BACKGROUND_COLOR );
		PopupOperation popupOperation = new PopupOperation();
		this.setInnerHeaderTrailingComponent( new PopupButton( popupOperation ) );

	}

	public SwingComponentView<?> getHeaderLeadingComponent() {
		return (SwingComponentView<?>)this.innerHeaderPanel.getLineStartComponent();
	}

	public void setHeaderLeadingComponent( SwingComponentView<?> component ) {
		if( component != null ) {
			component.setAlignmentY( java.awt.Component.BOTTOM_ALIGNMENT );
			this.innerHeaderPanel.addLineStartComponent( component );
		} else {
			AwtComponentView<?> prevComponent = this.innerHeaderPanel.getLineStartComponent();
			if( prevComponent != null ) {
				this.innerHeaderPanel.removeComponent( prevComponent );
			}
		}
		this.innerHeaderPanel.revalidateAndRepaint();
	}

	private void setInnerHeaderTrailingComponent( SwingComponentView<?> component ) {
		if( component != null ) {
			if( component.isOpaque() ) {
				//pass
			} else {
				component.setBackgroundColor( this.getBackgroundColor() );
			}
			component.setAlignmentY( java.awt.Component.BOTTOM_ALIGNMENT );
			this.innerHeaderPanel.addLineEndComponent( component );
		} else {
			AwtComponentView<?> prevComponent = this.innerHeaderPanel.getLineEndComponent();
			if( prevComponent != null ) {
				this.innerHeaderPanel.removeComponent( prevComponent );
			}
		}
		this.innerHeaderPanel.revalidateAndRepaint();
	}

	public void setHeaderTrailingComponent( SwingComponentView<?> component ) {
		if( component != null ) {
			if( component.isOpaque() ) {
				//pass
			} else {
				component.setBackgroundColor( this.getBackgroundColor() );
			}
			component.setAlignmentY( java.awt.Component.BOTTOM_ALIGNMENT );
			this.outerHeaderPanel.addLineEndComponent( component );
		} else {
			AwtComponentView<?> prevComponent = this.outerHeaderPanel.getLineEndComponent();
			if( prevComponent != null ) {
				this.outerHeaderPanel.removeComponent( prevComponent );
			}
		}
		this.outerHeaderPanel.revalidateAndRepaint();
	}

	@Override
	protected javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = super.createAwtComponent();
		this.innerHeaderPanel.addCenterComponent( this.titlesScrollPane );
		this.outerHeaderPanel.addCenterComponent( this.innerHeaderPanel );
		rv.add( this.outerHeaderPanel.getAwtComponent(), java.awt.BorderLayout.PAGE_START );
		rv.add( this.getCardOwner().getView().getAwtComponent(), java.awt.BorderLayout.CENTER );
		return rv;
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.BorderLayout();
	}

	@Override
	protected BooleanStateButton<? extends javax.swing.AbstractButton> createTitleButton( E item, BooleanState itemSelectedState ) {
		return new FolderTabTitle( item, itemSelectedState );
	}

	@Override
	protected void removeAllDetails() {
		super.removeAllDetails();
		this.titlesPanel.removeAllComponents();
	}

	@Override
	protected void addItem( E item, BooleanStateButton<?> button ) {
		super.addItem( item, button );
		if( button instanceof FolderTabbedPane.FolderTabTitle ) {
			FolderTabTitle title = (FolderTabTitle)button;
			title.setCloseable( item.isCloseable() );
		}
		this.titlesPanel.addComponent( button );
	}

	@Override
	protected void addSeparator() {
		super.addSeparator();
		this.titlesPanel.addComponent( BoxUtilities.createHorizontalSliver( 16 ) );
	}

	@Override
	public void setBackgroundColor( java.awt.Color color ) {
		super.setBackgroundColor( color );
		this.titlesPanel.setBackgroundColor( color );
		this.titlesScrollPane.setBackgroundColor( color );
	}
}
