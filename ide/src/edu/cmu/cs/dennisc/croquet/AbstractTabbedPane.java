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
public abstract class AbstractTabbedPane<E,D extends AbstractTabbedPane.TabItemDetails> extends ItemSelectablePanel<E, D> {
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
	
	protected class TabItemDetails extends ItemDetails {
		private java.util.UUID id;
		private JComponent<?> mainComponent;
		private ScrollPane scrollPane;
		public TabItemDetails( E item, AbstractButton< ?,BooleanState > titleButton, java.util.UUID id, ScrollPane scrollPane, JComponent<?> mainComponent ) {
			super( item, titleButton );
			assert id != null;
			this.id = id;
			this.mainComponent = mainComponent;
			this.scrollPane = scrollPane;

			titleButton.setBackgroundColor( this.mainComponent.getBackgroundColor() );
			if( this.scrollPane != null ) {
				this.scrollPane.setViewportView( this.mainComponent );
			}
		}
		public java.util.UUID getId() {
			return this.id;
		}
		public JComponent< ? > getMainComponent() {
			return this.mainComponent;
		}
		public ScrollPane getScrollPane() {
			return this.scrollPane;
		}
		public JComponent< ? > getRootComponent() {
			if( this.scrollPane != null ) {
				return this.scrollPane;
			} else {
				return this.mainComponent;
			}
		}
	}
	private ListSelectionState.TabCreator< E > tabCreator;
	@Override
	public void setFont(java.awt.Font font) {
		super.setFont( font );
		for( D tabItemDetails : this.getAllItemDetails() ) {
			tabItemDetails.getButton().setFont( font );
		}
	}

	protected abstract AbstractButton< ?, BooleanState > createTitleButton( BooleanState booleanState );
	protected abstract D createTabItemDetails( E item, java.util.UUID id, AbstractButton< ?, BooleanState > titleButton, ScrollPane scrollPane, JComponent<?> mainComponent, java.awt.event.ActionListener closeButtonActionListener );
	@Override
	protected final D createItemDetails( final E item ) {
		java.util.UUID id = this.tabCreator.getId( item );
		assert id != null : item;
		java.awt.event.ActionListener closeButtonActionListener;
		if( this.tabCreator.isCloseAffordanceDesired() ) {
			closeButtonActionListener = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					AbstractTabbedPane.this.getModel().removeItem( item );
				}
			};
		} else {
			closeButtonActionListener = null;
		}
		BooleanState booleanState = new BooleanState( Application.UI_STATE_GROUP, java.util.UUID.fromString( "a6ed465d-39f4-4604-a5d0-e6c9463606b0" ), false );
		AbstractButton< ?, BooleanState > titleButton = this.createTitleButton( booleanState );
		this.tabCreator.customizeTitleComponent( booleanState, titleButton, item );
		return createTabItemDetails( item, id, titleButton, this.tabCreator.createScrollPane( item ), this.tabCreator.createMainComponent( item ), closeButtonActionListener );
	}
	
	public AbstractTabbedPane( ListSelectionState<E> model, ListSelectionState.TabCreator< E > tabCreator ) {
		super( model );
		this.tabCreator = tabCreator;
	}
	
	/*package-private*/ JComponent< ? > getMainComponentFor( E item ) {
		TabItemDetails tabItemDetails = this.getItemDetails( item );
		if( tabItemDetails != null ) {
			return tabItemDetails.getMainComponent();
		} else {
			return null;
		}
	}
	/*package-private*/ ScrollPane getScrollPaneFor( E item ) {
		TabItemDetails tabItemDetails = this.getItemDetails( item );
		if( tabItemDetails != null ) {
			return tabItemDetails.getScrollPane();
		} else {
			return null;
		}
	}
	/*package-private*/ JComponent< ? > getRootComponentFor( E item ) {
		TabItemDetails tabItemDetails = this.getItemDetails( item );
		if( tabItemDetails != null ) {
			return tabItemDetails.getRootComponent();
		} else {
			return null;
		}
	}
	
		
//	protected abstract Tab<E> createTab( E item, ItemSelectionOperation.TabCreator< E > tabCreator );
//	protected abstract void addTab( Tab<E> tab );
//	protected abstract void removeTab( Tab<E> tab );
//	@Override
//	protected AbstractButton<?> createButton(E item) {
//		Tab< E > tab = createTab( item, this.tabCreator );
//		AbstractButton<?> rv = tab.getOuterTitleComponent();
//		map.put(rv, tab);
//		return rv;
//	}
//	@Override
//	protected void removeAllButtons() {
//		for( Tab<E> tab : this.map.values() ) {
//			this.removeTab( tab );
//		}
//	}
//	@Override
//	protected void addPrologue( int count ) {
//	}
//	@Override
//	protected final void addButton(edu.cmu.cs.dennisc.croquet.AbstractButton<?> button) {
//		Tab< E > tab = this.map.get( button );
//		assert tab != null;
//		this.addTab( tab );
//	}
//	@Override
//	protected void addEpilogue() {
//	}
//	
//	@Override
//	protected void handleItemSelected(E item) {
//		javax.swing.JOptionPane.showMessageDialog( null, "todo: handleItemSelected" );
//	}
	
//	/* package-private */ void addTab( E item ) {
//		Tab<E> tab = this.map.get( item );
//		if( tab != null ) {
//			//pass
//		} else {
//			tab = this.createTab( item, this.tabCreator );
//			this.map.put( item, tab );
//		}
//		this.revalidateAndRepaint();
//	}
//	/* package-private */ void removeTab( E item ) {
//		this.revalidateAndRepaint();
//	}
//	/* package-private */ void selectTab( E item ) {
//		Tab<E> tab = this.map.get( item );
//		assert tab != null;
//		tab.select();
//	}
}
