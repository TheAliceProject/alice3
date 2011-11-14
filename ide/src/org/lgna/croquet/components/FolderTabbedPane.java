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

import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.Operation;

/*package-private*/ class FolderTabItemDetails<E extends org.lgna.croquet.TabComposite< ? >> extends TabItemDetails<E, FolderTabItemDetails<E>, FolderTabbedPane<E>> {
	private final CardPanel.Key cardPanelKey;
	public FolderTabItemDetails( FolderTabbedPane< E > panel, E item, BooleanStateButton< ? extends javax.swing.AbstractButton > button, ScrollPane scrollPane ) {
		super( panel, item, button, scrollPane );
		this.cardPanelKey = panel.getCardPanel().createKey( this.getRootComponent(), this.getTabId() );
	}
	public CardPanel.Key getCardPanelKey() {
		return this.cardPanelKey;
	}
	@Override
	public void setSelected(boolean isSelected) {
		super.setSelected(isSelected);
		if( isSelected ) {
			this.getPanel().getCardPanel().showKey( this.cardPanelKey );
			this.getPanel().getCardPanel().revalidateAndRepaint();
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class FolderTabbedPane<E extends org.lgna.croquet.TabComposite< ? >> extends AbstractTabbedPane< E, FolderTabItemDetails<E>, FolderTabbedPane<E> > {
	private static final int TRAILING_TAB_PAD = 32;
	public static final java.awt.Color DEFAULT_BACKGROUND_COLOR = new java.awt.Color( 173, 167, 208 );
	private static class FolderTabTitleUI extends javax.swing.plaf.basic.BasicButtonUI {
		@Override
		public java.awt.Dimension getPreferredSize(javax.swing.JComponent c) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			java.awt.Font font = button.getFont();
			java.awt.FontMetrics fm = button.getFontMetrics( font );
			String text = button.getText();
			javax.swing.Icon icon = button.getIcon();
			int verticalAlignment = button.getVerticalAlignment();
			int horizontalAlignment = button.getHorizontalAlignment();
			int verticalTextPosition = button.getVerticalTextPosition();
			int horizontalTextPosition = button.getHorizontalTextPosition();
			java.awt.Rectangle viewR = new java.awt.Rectangle( Short.MAX_VALUE, Short.MAX_VALUE );
			java.awt.Rectangle iconR = new java.awt.Rectangle();
			java.awt.Rectangle textR = new java.awt.Rectangle();
			int textIconGap = button.getIconTextGap();
			javax.swing.SwingUtilities.layoutCompoundLabel(c, fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewR, iconR, textR, textIconGap);
			
	        java.awt.Rectangle bounds = iconR.union(textR);

	        java.awt.Insets insets = button.getInsets();
	        bounds.width += insets.left + insets.right;
	        bounds.height += insets.top + insets.bottom;

	        for( java.awt.Component component : button.getComponents() ) {
	        	//if( component.isVisible() ) {
	        		bounds.width += 2;
			        bounds.width += component.getPreferredSize().width;
	        	//}
	        }
	        
	        return bounds.getSize();
		}
	}
	
	private static class JFolderTabTitle extends javax.swing.JRadioButton {
		private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				JFolderTabTitle.this.revalidate();
			}
		};
		public JFolderTabTitle() {
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 8 ) );
			this.setOpaque( false );
			this.setAlignmentY( java.awt.Component.BOTTOM_ALIGNMENT );
			this.setLayout( new javax.swing.SpringLayout() );
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
	
	private static class FolderTabTitle extends BooleanStateButton<javax.swing.AbstractButton> {
		public FolderTabTitle( BooleanState booleanState, java.awt.event.ActionListener closeButtonActionListener ) {
			super( booleanState );
			if( closeButtonActionListener != null ) {
				javax.swing.AbstractButton awtButton = this.getAwtComponent();
				javax.swing.JButton closeButton = new edu.cmu.cs.dennisc.javax.swing.components.JCloseButton( true );
				closeButton.addActionListener( closeButtonActionListener );
				edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal horizontal;
				if( this.getAwtComponent().getComponentOrientation() == java.awt.ComponentOrientation.LEFT_TO_RIGHT ) {
					horizontal = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal.EAST;
				} else {
					horizontal = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal.WEST;
				}
				edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( awtButton, closeButton, horizontal, -1, edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Vertical.NORTH, 4 );
			}
		}
		@Override
		protected javax.swing.AbstractButton createAwtComponent() {
			final JFolderTabTitle rv = new JFolderTabTitle() {
				@Override
				public void setComponentOrientation(java.awt.ComponentOrientation o) {
					super.setComponentOrientation(o);
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: adjust spring" );
				}
			};
			return rv;
		}
	}

	private static java.awt.Color SELECTED_BORDER_COLOR = java.awt.Color.WHITE;
	private static java.awt.Color UNSELECTED_BORDER_COLOR = java.awt.Color.DARK_GRAY;

	private static class TitlesPanel extends LineAxisPanel {
		private static final int NORTH_AREA_PAD = 1;
		// private static java.awt.Stroke SELECTED_STROKE = new
		// java.awt.BasicStroke( 3.0f );
		private static java.awt.Stroke SELECTED_STROKE = new java.awt.BasicStroke( 1.0f );
		private static java.awt.Stroke UNSELECTED_STROKE = new java.awt.BasicStroke( 2.0f );

		@Override
		protected javax.swing.JPanel createJPanel() {
			javax.swing.JPanel rv = new javax.swing.JPanel() {
				private java.awt.geom.GeneralPath addToPath( java.awt.geom.GeneralPath rv, float x, float y, float width, float height, boolean isContinuation ) {
					float a = height * 0.25f;

					float xStart;
					float xEnd;
					float xA;
					float tabPad;
					if( this.getComponentOrientation() == java.awt.ComponentOrientation.LEFT_TO_RIGHT ) {
						xStart = x;
						xEnd = x + width - 1;
						tabPad = TRAILING_TAB_PAD;
						xA = xStart + a;
					} else {
						xStart = x + width - 1;
						xEnd = x;
						tabPad = -TRAILING_TAB_PAD;
						xA = xStart - a;
					}
					float xCurve0 = xEnd - tabPad / 2;
					float xCurve1 = xEnd + tabPad;
					float cx0 = xCurve0 + tabPad * 0.75f;
					float cx1 = xCurve0;
					
					float y0 = y + NORTH_AREA_PAD;
					float y1 = y + height;// + this.contentBorderInsets.top;
					float cy0 = y0;
					float cy1 = y1;

					float yA = y + a;

					if( isContinuation ) {
						rv.lineTo( xCurve1, y1 );
					} else {
						rv.moveTo( xCurve1, y1 );
					}
					rv.lineTo( xCurve1, y1-1 );
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
			//rv.setBackground( this.getBackgroundColor() );
			return rv;
		}
	}
	

	private final CardPanel cardPanel = new CardPanel();
	private final TitlesPanel titlesPanel = new TitlesPanel();
	private final BorderPanel innerHeaderPanel = new BorderPanel();
	private final BorderPanel outerHeaderPanel = new BorderPanel();
	
	//private java.util.Map<E, javax.swing.Action> mapItemToAction = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private javax.swing.Action getActionFor( E item ) {
		return FolderTabbedPane.this.getModel().createActionForItem( item );
	}
	
	//todo: PopupOperation
	private class PopupOperation extends ActionOperation {
		public PopupOperation() {
			super( Application.UI_STATE_GROUP, java.util.UUID.fromString( "7923b4c8-6a9f-4c8b-99b5-909ae6c0889a" ) );
		}
		@Override
		protected void localize() {
			super.localize();
			this.setName( ">>" );
		}
		@Override
		protected void perform(org.lgna.croquet.history.ActionOperationStep step) {
			javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
			javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
			for( E item : FolderTabbedPane.this.getModel() ) {
				javax.swing.JCheckBoxMenuItem checkBox = new javax.swing.JCheckBoxMenuItem( getActionFor( item ) );
				checkBox.setSelected( FolderTabbedPane.this.getModel().getSelectedItem() == item );
				popupMenu.add( checkBox );
				buttonGroup.add( checkBox );
			}
			org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
			ViewController<?,?> viewController = trigger.getViewController();
			popupMenu.show( viewController.getAwtComponent(), 0, viewController.getHeight() );
		}
	}
	
	private class PopupButton extends Button {
		public PopupButton( Operation<?> operation ) {
			super( operation );
		}
		@Override
		protected javax.swing.JButton createAwtComponent() {
			javax.swing.JButton rv = new javax.swing.JButton() {
				private boolean isNecessary() {
					java.awt.Container parent = this.getParent();
					if( parent != null ) {
						int width = parent.getWidth();
						int preferredWidth = parent.getPreferredSize().width;
						return width < preferredWidth;
					} else {
						return false;
					}
				}
				@Override
				public void paint(java.awt.Graphics g) {
					if( isNecessary() ) {
						super.paint(g);
					} else {
						g.setColor( FolderTabbedPane.this.getBackgroundColor() );
						g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
					}
				}
				@Override
				public boolean contains(int x, int y) {
					if( isNecessary() ) {
						return super.contains(x, y);
					} else {
						return false;
					}
				}
			};
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2,4,2,4 ) );
			return rv;
		}
	}

	public FolderTabbedPane( ListSelectionState<E> model ) {
		super( model );
		this.cardPanel.setBackgroundColor( null );
		this.innerHeaderPanel.setBackgroundColor( null );
		this.innerHeaderPanel.getAwtComponent().setOpaque( false );
		this.titlesPanel.setBackgroundColor( null );
		this.innerHeaderPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 0, 0, 0 ) );
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
								java.awt.Rectangle bounds = button.getBounds( innerHeaderPanel );
								g.setColor( button.getBackgroundColor() );
								int x0;
								if( c.getComponentOrientation() == java.awt.ComponentOrientation.LEFT_TO_RIGHT ) {
									x0 = bounds.x;
								} else {
									x0 = bounds.x - TRAILING_TAB_PAD;
								}
								g.fillRect( x0, y, bounds.width - 1 + TRAILING_TAB_PAD, 1 );
							}
						}
					}
				}
			}
		} );
		this.setBackgroundColor( DEFAULT_BACKGROUND_COLOR );
		this.getAwtComponent().setOpaque( true );

		PopupOperation popupOperation = new PopupOperation();
		this.setInnerHeaderTrailingComponent( new PopupButton( popupOperation ) );
	}

	/*package-private*/ CardPanel getCardPanel() {
		return this.cardPanel;
	}
	
	public void setHeaderLeadingComponent( JComponent< ? > component ) {
		if( component.isOpaque() ) {
			//pass
		} else {
			component.setBackgroundColor( this.getBackgroundColor() );
		}
		component.setAlignmentY( java.awt.Component.BOTTOM_ALIGNMENT );
		this.innerHeaderPanel.addComponent( component, BorderPanel.Constraint.LINE_START );
		this.innerHeaderPanel.revalidateAndRepaint();
	}
	private void setInnerHeaderTrailingComponent( JComponent< ? > component ) {
		if( component.isOpaque() ) {
			//pass
		} else {
			component.setBackgroundColor( this.getBackgroundColor() );
		}
		component.setAlignmentY( java.awt.Component.BOTTOM_ALIGNMENT );
		this.innerHeaderPanel.addComponent( component, BorderPanel.Constraint.LINE_END );
		this.innerHeaderPanel.revalidateAndRepaint();
	}
	public void setHeaderTrailingComponent( JComponent< ? > component ) {
		if( component.isOpaque() ) {
			//pass
		} else {
			component.setBackgroundColor( this.getBackgroundColor() );
		}
		component.setAlignmentY( java.awt.Component.BOTTOM_ALIGNMENT );
		this.outerHeaderPanel.addComponent( component, BorderPanel.Constraint.LINE_END );
		this.outerHeaderPanel.revalidateAndRepaint();
	}
	@Override
	protected javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = super.createAwtComponent();
		this.innerHeaderPanel.addComponent( this.titlesPanel, BorderPanel.Constraint.CENTER );
		this.outerHeaderPanel.addComponent( this.innerHeaderPanel, BorderPanel.Constraint.CENTER );
		rv.add( this.outerHeaderPanel.getAwtComponent(), java.awt.BorderLayout.NORTH );
		rv.add( this.cardPanel.getAwtComponent(), java.awt.BorderLayout.CENTER );
		return rv;
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.BorderLayout();
	}

	@Override
	protected BooleanStateButton< ? extends javax.swing.AbstractButton > createTitleButton( BooleanState booleanState, java.awt.event.ActionListener closeButtonActionListener ) {
		return new FolderTabTitle( booleanState, closeButtonActionListener );
	}
	@Override
	protected FolderTabItemDetails<E> createTabItemDetails( E item, BooleanStateButton< ? extends javax.swing.AbstractButton > titleButton, ScrollPane scrollPane ) {
		FolderTabItemDetails<E> rv = new FolderTabItemDetails<E>( this, item, titleButton, scrollPane );
		rv.getRootComponent().setVisible( false );
		return rv;
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
	protected void addItem(FolderTabItemDetails<E> folderTabItemDetails) {
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
