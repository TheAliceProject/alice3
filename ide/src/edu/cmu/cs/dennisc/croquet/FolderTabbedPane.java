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

	private static class FolderTabTitle extends TabTitle {
		public FolderTabTitle( JComponent<?> innerTitleComponent, java.awt.event.ActionListener closeButtonActionListener ) {
			super( new JFolderTabTitle( innerTitleComponent.getAwtComponent(), closeButtonActionListener ) );
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
		return booleanState.createCheckBox();
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
