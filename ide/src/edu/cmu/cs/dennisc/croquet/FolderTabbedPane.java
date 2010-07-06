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

package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public final class FolderTabbedPane<E> extends AbstractTabbedPane< E, FolderTabbedPane.FolderTabItemDetails > {
	private static final int EAST_TAB_PAD = 32;
	public static final java.awt.Color DEFAULT_BACKGROUND_COLOR = new java.awt.Color( 173, 167, 208 );

	private static class CloseButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
		private static final java.awt.Color BASE_COLOR = new java.awt.Color( 127, 63, 63 );
		private static final java.awt.Color HIGHLIGHT_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.shiftHSB( BASE_COLOR, 0, 0, +0.25f );
		private static final java.awt.Color PRESS_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.shiftHSB( BASE_COLOR, 0, 0, -0.125f );

		private int getIconWidth() {
			return 14;
		}
		private int getIconHeight() {
			return getIconWidth();
		}

		@Override
		public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			javax.swing.ButtonModel model = button.getModel();

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			float size = Math.min( this.getIconWidth(), this.getIconHeight() ) * 0.9f;

			float w = size;
			float h = size * 0.25f;
			float xC = -w * 0.5f;
			float yC = -h * 0.5f;
			java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( xC, yC, w, h, h, h );

			java.awt.geom.Area area0 = new java.awt.geom.Area( rr );
			java.awt.geom.Area area1 = new java.awt.geom.Area( rr );

			java.awt.geom.AffineTransform m0 = new java.awt.geom.AffineTransform();
			m0.rotate( Math.PI * 0.25 );
			area0.transform( m0 );

			java.awt.geom.AffineTransform m1 = new java.awt.geom.AffineTransform();
			m1.rotate( Math.PI * 0.75 );
			area1.transform( m1 );

			area0.add( area1 );

			int x0 = 0;
			int y0 = 0;
			
			java.awt.geom.AffineTransform m = new java.awt.geom.AffineTransform();
			m.translate( x0 + getIconWidth() / 2, y0 + getIconWidth() / 2 );
			area0.transform( m );

			java.awt.Paint prevPaint = g2.getPaint();
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			if( model.isRollover() ) {
				if( model.isPressed() ) {
					g2.setPaint( PRESS_COLOR );
				} else {
					g2.setPaint( HIGHLIGHT_COLOR );
				}
			} else {
				g2.setPaint( java.awt.Color.WHITE );
			}
			javax.swing.AbstractButton parent = (javax.swing.AbstractButton)button.getParent();
			if( parent.isSelected() ) {
				g2.fill( area0 );
				g2.setPaint( java.awt.Color.BLACK );
			} else {
				g2.setPaint( java.awt.Color.DARK_GRAY );
			}
			g2.draw( area0 );
			g2.setPaint( prevPaint );
		}
		@Override
		public java.awt.Dimension getPreferredSize(javax.swing.JComponent c) {
			return new java.awt.Dimension( this.getIconWidth(), this.getIconHeight() );
		}
	}

	protected static abstract class JTabTitle extends javax.swing.AbstractButton {
		private java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
			public void mouseEntered( java.awt.event.MouseEvent e ) {
				JTabTitle.this.setArmed( true );
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
				JTabTitle.this.setArmed( false );
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
				JTabTitle.this.setPressed( true );
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
				JTabTitle.this.setPressed( false );
			}
			public void mouseClicked(java.awt.event.MouseEvent e) {
				JTabTitle.this.select();
			}
		};

		protected void setArmed( boolean isArmed ) {
			this.getModel().setArmed( isArmed );
		}
		protected void setPressed( boolean isPressed ) {
			this.getModel().setPressed( isPressed );
		}
		protected void select() {
			this.setSelected( true );
		}

		private javax.swing.JComponent jComponent;
		private javax.swing.JButton closeButton;
		private java.awt.event.ActionListener closeActionListener;
		public JTabTitle( javax.swing.JComponent jComponent, java.awt.event.ActionListener closeActionListener ) {
			this.jComponent = jComponent;
			this.setModel( new javax.swing.JToggleButton.ToggleButtonModel() );
			this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
			this.add( this.jComponent );
			this.closeActionListener = closeActionListener;
			if( this.closeActionListener != null ) {
				this.closeButton = new javax.swing.JButton();
				this.closeButton.setUI( new CloseButtonUI() );
				this.closeButton.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
				this.closeButton.setOpaque( false );
				this.add( this.closeButton );
			}
		}
		@Override
		public void setFont(java.awt.Font font) {
			super.setFont(font);
			this.jComponent.setFont( font );
		}
		@Override
		public void addNotify() {
			super.addNotify();
			this.addMouseListener( this.mouseListener );
			if( this.closeButton != null ) {
				assert this.closeActionListener != null;
				this.closeButton.addActionListener( this.closeActionListener );
			}
		}
		@Override
		public void removeNotify() {
			if( this.closeButton != null ) {
				assert this.closeActionListener != null;
				this.closeButton.removeActionListener( this.closeActionListener );
			}
			this.removeMouseListener( this.mouseListener );
			super.removeNotify();
		}
	}

	@Deprecated
	private final static BooleanState TAB_TITLE_BOOLEAN_STATE = null;
	
	protected static abstract class TabTitle extends AbstractButton< javax.swing.AbstractButton, BooleanState > {
		private JTabTitle jTabTitle;
		public TabTitle( JTabTitle jTabTitle ) {
			super( TAB_TITLE_BOOLEAN_STATE );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: TAB_TITLE_BOOLEAN_STATE" );
			this.jTabTitle = jTabTitle;
		}
		@Override
		protected final javax.swing.AbstractButton createAwtComponent() {
			return this.jTabTitle;
		}
	}

	private static class JFolderTabTitle extends JTabTitle {
		public JFolderTabTitle( javax.swing.JComponent jComponent, java.awt.event.ActionListener closeButtonActionListener ) {
			super( jComponent, closeButtonActionListener );
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 8 ) );
			this.setOpaque( false );
			this.setBackground( null );
			this.setAlignmentY( 1.0f );
		}
		protected void repaintPlus() {
			this.getParent().repaint( this.getX(), this.getY(), this.getWidth() + EAST_TAB_PAD, this.getHeight() );
		}
		@Override
		protected void setArmed( boolean isArmed ) {
			super.setArmed(isArmed);
			this.repaintPlus();
		}
		@Override
		protected void setPressed( boolean isPressed ) {
			super.setPressed(isPressed);
			this.repaintPlus();
		}
		@Override
		protected void select() {
			super.select();
			this.repaintPlus();
		}
	}

//	private static class FolderTabTitle extends TabTitle {
//		public FolderTabTitle( JComponent<?> innerTitleComponent, java.awt.event.ActionListener closeButtonActionListener ) {
//			super( new JFolderTabTitle( innerTitleComponent.getAwtComponent(), closeButtonActionListener ) );
//		}
//	}
	
	private static class FolderTabTitle extends AbstractButton<javax.swing.AbstractButton,BooleanState> {
		public FolderTabTitle( BooleanState booleanState ) {
			super( booleanState );
		}
		@Override
		protected javax.swing.AbstractButton createAwtComponent() {
			javax.swing.JRadioButton rv = new javax.swing.JRadioButton() {
				@Override
				public void updateUI() {
					this.setUI( new javax.swing.plaf.basic.BasicButtonUI() );
				}
				@Override
				public void repaint() {
					java.awt.Container parent = this.getParent();
					if( parent != null ) {
						parent.repaint( this.getX(), this.getY(), this.getWidth() + EAST_TAB_PAD, this.getHeight() );
					} else {
						super.repaint();
					}
				}
			};
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 8 ) );
			rv.setOpaque( false );
			rv.setAlignmentY( 1.0f );
			return rv;
		}
	}

	private static java.awt.Color SELECTED_BORDER_COLOR = java.awt.Color.WHITE;
	private static java.awt.Color UNSELECTED_BORDER_COLOR = java.awt.Color.DARK_GRAY;

	private static class TitlesPanel extends JComponent< javax.swing.JPanel > {
		private static final int NORTH_AREA_PAD = 1;
		// private static java.awt.Stroke SELECTED_STROKE = new
		// java.awt.BasicStroke( 3.0f );
		private static java.awt.Stroke SELECTED_STROKE = new java.awt.BasicStroke( 1.0f );
		private static java.awt.Stroke UNSELECTED_STROKE = new java.awt.BasicStroke( 2.0f );

		@Override
		protected javax.swing.JPanel createAwtComponent() {
			javax.swing.JPanel rv = new javax.swing.JPanel() {
				private java.awt.geom.GeneralPath addToPath( java.awt.geom.GeneralPath rv, float x, float y, float width, float height, boolean isContinuation ) {
					float a = height * 0.25f;
					float x0 = x + width - EAST_TAB_PAD / 2;
					float x1 = x + width + EAST_TAB_PAD;
					// x0 += EAST_TAB_PAD;
					float cx0 = x0 + EAST_TAB_PAD * 0.75f;
					float cx1 = x0;

					float y0 = y + NORTH_AREA_PAD;
					float y1 = y + height;// + this.contentBorderInsets.top;
					float cy0 = y0;
					float cy1 = y1;

					float xA = x + a;
					float yA = y + a;

					if( isContinuation ) {
						rv.lineTo( x1, y1 );
					} else {
						rv.moveTo( x1, y1 );
					}
					rv.lineTo( x1, y1-1 );
					rv.curveTo( cx1, cy1, cx0, cy0, x0, y0 );
					rv.lineTo( xA, y0 );
					rv.quadTo( x, y0, x, yA );
					rv.lineTo( x, y1 );

					return rv;
				}

				private void paintTabBorder( java.awt.Graphics2D g2, javax.swing.AbstractButton button ) {
					int x = button.getX();
					int y = button.getY();
					int width = button.getWidth();
					int height = button.getHeight();
					java.awt.geom.GeneralPath path = this.addToPath( new java.awt.geom.GeneralPath(), x, y, width, height, false );
					g2.draw( path );
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
					for( int i=0; i<N; i++ ) {
						java.awt.Component component = components[ N-1-i ];
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
			};
			rv.setLayout( new javax.swing.BoxLayout( rv, javax.swing.BoxLayout.LINE_AXIS ) );
			rv.setBackground( DEFAULT_BACKGROUND_COLOR );
			return rv;
		}
		public void addComponent( Component< ? > component ) {
			this.internalAddComponent( component );
		}
		public void removeComponent( Component< ? > component ) {
			this.internalRemoveComponent( component );
		}
	}


	private CardPanel cardPanel = new CardPanel();
	private TitlesPanel titlesPanel = new TitlesPanel();
	private BorderPanel headerPanel = new BorderPanel();

	/*package-private*/ class FolderTabItemDetails extends AbstractTabbedPane.TabItemDetails {
		private CardPanel.Key cardPanelKey;
		public FolderTabItemDetails( E item, AbstractButton< ?,? > button, java.util.UUID id, ScrollPane scrollPane, JComponent<?> mainComponent ) {
			super( item, button, id, scrollPane, mainComponent );
			this.cardPanelKey = cardPanel.createKey( this.getRootComponent(), this.getId() );
		}
		public CardPanel.Key getCardPanelKey() {
			return cardPanelKey;
		}
		@Override
		public void setSelected(boolean isSelected) {
			super.setSelected(isSelected);
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "setSelected", this, isSelected ); 
			if( isSelected ) {
				cardPanel.show( this.cardPanelKey );
				cardPanel.revalidateAndRepaint();
			}
		}
	}

	public FolderTabbedPane( ListSelectionState<E> model, ListSelectionState.TabCreator< E > tabCreator ) {
		super( model, tabCreator );
		this.cardPanel.setBackgroundColor( null );
		this.headerPanel.setBackgroundColor( null );
		this.headerPanel.getAwtComponent().setOpaque( false );
		this.titlesPanel.setBackgroundColor( null );
		this.headerPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 12, 0, 0, 0 ) );
		this.cardPanel.setBorder( new javax.swing.border.Border() {
			public java.awt.Insets getBorderInsets( java.awt.Component c ) {
				return new java.awt.Insets( 1,1,0,0 );
			}
			public boolean isBorderOpaque() {
				return true;
			}
			public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
				if( titlesPanel.getComponentCount() > 0 ) {
					g.setColor( SELECTED_BORDER_COLOR );
					g.fillRect( x, y, width, 1 );
					g.fillRect( x, y, 1, height );
					
					for( Component< ? > component : titlesPanel.getComponents() ) {
						if( component instanceof AbstractButton< ?, ? > ) {
							AbstractButton< ?, ? > button = (AbstractButton< ?, ? >)component;
							if( button.getAwtComponent().getModel().isSelected() ) {
								java.awt.Rectangle bounds = button.getBounds( headerPanel );
								g.setColor( button.getBackgroundColor() );
								g.fillRect( bounds.x, y, bounds.width + EAST_TAB_PAD, 1 );
							}
						}
					}
				}
			}
		} );
		this.setBackgroundColor( DEFAULT_BACKGROUND_COLOR );
		this.getAwtComponent().setOpaque( true );
	}

	public void setHeaderLeadingComponent( JComponent< ? > component ) {
		if( component.isOpaque() ) {
			//pass
		} else {
			component.setBackgroundColor( DEFAULT_BACKGROUND_COLOR );
		}
		component.setAlignmentY( 1.0f );
		this.headerPanel.addComponent( component, BorderPanel.Constraint.LINE_START );
		this.headerPanel.revalidateAndRepaint();
	}
	public void setHeaderTrailingComponent( JComponent< ? > component ) {
		if( component.isOpaque() ) {
			//pass
		} else {
			component.setBackgroundColor( DEFAULT_BACKGROUND_COLOR );
		}
		component.setAlignmentY( 1.0f );
		this.headerPanel.addComponent( component, BorderPanel.Constraint.LINE_END );
		this.headerPanel.revalidateAndRepaint();
	}
	@Override
	protected javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = super.createAwtComponent();
		this.headerPanel.addComponent( this.titlesPanel, BorderPanel.Constraint.CENTER );
		rv.add( this.headerPanel.getAwtComponent(), java.awt.BorderLayout.NORTH );
		rv.add( this.cardPanel.getAwtComponent(), java.awt.BorderLayout.CENTER );
		return rv;
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.BorderLayout();
	}

	@Override
	protected AbstractButton< ?, BooleanState > createTitleButton( BooleanState booleanState ) {
		return new FolderTabTitle( booleanState );
	}
	@Override
	protected FolderTabItemDetails createTabItemDetails( E item, java.util.UUID id, AbstractButton< ?, BooleanState > titleButton, ScrollPane scrollPane, JComponent<?> mainComponent, java.awt.event.ActionListener closeButtonActionListener ) {
//		AbstractButton<?,?> button = new FolderTabTitle(innerTitleComponent, closeButtonActionListener);
		JComponent<?> root;
		if( scrollPane != null ) {
			root = scrollPane;
		} else {
			root = mainComponent;
		}
		root.setVisible( false );
		root.setBackgroundColor( null );
		return new FolderTabItemDetails( item, titleButton, id, scrollPane, mainComponent );
	};
	

	@Override
	protected void removeAllDetails() {
		this.titlesPanel.removeAllComponents();
		this.cardPanel.removeAllComponents();
	}
	@Override
	protected void addPrologue(int count) {
	}
	@Override
	protected void addItem(FolderTabbedPane.FolderTabItemDetails folderTabItemDetails) {
		this.titlesPanel.addComponent( folderTabItemDetails.getButton() );
		this.cardPanel.addComponent( folderTabItemDetails.getCardPanelKey() );
	}
	@Override
	protected void addEpilogue() {
	}
	
//	@Override
//	protected AbstractTabbedPane.Tab< E > createTab( E item, ItemSelectionOperation.TabCreator< E > tabCreator ) {
//		return new FolderTab<E>( item, tabCreator );
//	}
//	@Override
//	protected void addTab(AbstractTabbedPane.Tab<E> tab) {
//		this.headerPane.addComponent( tab.getOuterTitleComponent() );
//		this.cardPanel.addComponent( ((FolderTab<E>)tab).getCardPanelKey() );
////		this.revalidateAndRepaint();
//	}
//	@Override
//	protected void removeTab(AbstractTabbedPane.Tab<E> tab) {
//		this.headerPane.removeComponent( tab.getOuterTitleComponent() );
//		this.cardPanel.removeComponent( ((FolderTab<E>)tab).getCardPanelKey() );
//	}

	
	//
	//	/* package-private */class Key {
	//		private AbstractButton<?> headerComponent;
	//		private TabStateOperation tabStateOperation;
	//		private CardPanel.Key mainComponentKey;
	//
	//		private Key( AbstractButton<?> headerComponent, Component<?> mainComponent, TabStateOperation tabStateOperation ) {
	//			this.headerComponent = headerComponent;
	//			this.mainComponentKey = FolderTabbedPane.this.cardPanel.createKey( mainComponent, tabStateOperation.getIndividualUUID().toString());
	//			this.tabStateOperation = tabStateOperation;
	//		}
	//		public TabStateOperation getTabStateOperation() {
	//			return this.tabStateOperation;
	//		}
	//	}
	//	private java.util.Map<TabStateOperation, CardPanel.Key> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	//	private CardPanel.Key getKey( TabStateOperation tabStateOperation ) {
	//		CardPanel.Key rv = this.map.get( tabStateOperation );
	//		if( rv != null ) {
	//			//pass
	//		} else {
	//			rv = this.cardPanel.createKey( tabStateOperation.getSingletonScrollPane(), tabStateOperation.getIndividualUUID().toString() );
	//			this.map.put( tabStateOperation, rv );
	//		}
	//		return rv;
	//	}
	//	@Override
	//	/* package-private */ void addTab(TabStateOperation<?> tabStateOperation) {
	//		super.addTab(tabStateOperation);
	//		this.headerPane.addComponent(tabStateOperation.getSingletonTabTitle( this ));
	//		this.cardPanel.addComponent(this.getKey(tabStateOperation));
	//	}
	//	
	//	@Override
	//	/* package-private */ void removeTab(TabStateOperation<?> tabStateOperation) {
	//		super.removeTab(tabStateOperation);
	//		this.cardPanel.removeComponent(this.getKey(tabStateOperation));
	//		this.headerPane.removeComponent(tabStateOperation.getSingletonTabTitle( this ));
	//	}
	//
	//	@Override
	//	/* package-private */ void selectTab(TabStateOperation<?> tabStateOperation) {
	//		if( tabStateOperation != null ) {
	//			this.cardPanel.show(this.getKey(tabStateOperation));
	//		} else {
	//			this.cardPanel.show( null );
	//		}
	//	}
}
