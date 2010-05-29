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
public abstract class AbstractTabbedPane<E> extends AbstractRadioButtons<E> {
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
				JTabTitle.this.getModel().setArmed( true );
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
				JTabTitle.this.getModel().setArmed( false );
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
				JTabTitle.this.getModel().setPressed( true );
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
				JTabTitle.this.getModel().setPressed( false );
			}
			public void mouseClicked(java.awt.event.MouseEvent e) {
				JTabTitle.this.select();
			}
		};
		private void select() {
			this.setSelected( true );
			this.getParent().repaint();
			//this.getParent().repaint( this.getX(), this.getY(), this.getWidth() + EAST_TAB_PAD, this.getHeight() );
		}

		private javax.swing.JComponent jComponent;
		private javax.swing.JButton closeButton = new javax.swing.JButton();

		public JTabTitle( javax.swing.JComponent jComponent, boolean isCloseAffordanceDesired ) {
			this.jComponent = jComponent;
			this.setModel( new javax.swing.JToggleButton.ToggleButtonModel() );
			this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
			this.add( this.jComponent );
			if( isCloseAffordanceDesired ) {
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
		}
		@Override
		public void removeNotify() {
			this.removeMouseListener( this.mouseListener );
			super.removeNotify();
		}
	}

	protected static abstract class TabTitle extends AbstractButton< javax.swing.AbstractButton > {
		private JComponent<?> innerTitleComponent;
		private boolean isCloseAffordanceDesired;
		public TabTitle( JComponent<?> innerTitleComponent, boolean isCloseAffordanceDesired ) {
			this.innerTitleComponent = innerTitleComponent;
			this.isCloseAffordanceDesired = isCloseAffordanceDesired;
		}
		protected JComponent<?> getInnerTitleComponent() {
			return innerTitleComponent;
		}
		protected boolean isCloseAffordanceDesired() {
			return this.isCloseAffordanceDesired;
		}
	}

	protected static abstract class Tab<E> {
		private E item;
		private java.util.UUID id;
		private JComponent<?> titleComponent;
		private JComponent<?> mainComponent;
		private ScrollPane scrollPane;
		private boolean isCloseButtonDesired;
		public Tab( E item, ItemSelectionOperation.TabCreator< E > tabCreator ) {
			this.item = item;
			this.id = tabCreator.getId( this.item );
			this.titleComponent = tabCreator.createInnerTitleComponent( this.item );
			this.mainComponent = tabCreator.createMainComponent( this.item );
			this.scrollPane = tabCreator.createScrollPane( this.item );
			this.scrollPane.setViewportView( this.mainComponent );
			this.isCloseButtonDesired = tabCreator.isCloseAffordanceDesired();
		}
		public E getItem() {
			return this.item;
		}
		public java.util.UUID getId() {
			return this.id;
		}
		public JComponent< ? > getInnerTitleComponent() {
			return this.titleComponent;
		}
		public JComponent< ? > getMainComponent() {
			return this.mainComponent;
		}
		public ScrollPane getScrollPane() {
			return this.scrollPane;
		}
		public boolean isCloseButtonDesired() {
			return this.isCloseButtonDesired;
		}
		public abstract AbstractButton< ? > getOuterTitleComponent();
		public abstract void select();
	}
	private java.util.Map< AbstractButton<?>, Tab<E> > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private ItemSelectionOperation.TabCreator< E > tabCreator;
	public AbstractTabbedPane( ItemSelectionOperation.TabCreator< E > tabCreator ) {
		this.tabCreator = tabCreator;
	}
	@Override
	public void setFont(java.awt.Font font) {
		super.setFont( font );
		for( Tab<E> tab : this.map.values() ) {
			tab.getInnerTitleComponent().setFont( font );
		}
	}

	protected abstract Tab<E> createTab( E item, ItemSelectionOperation.TabCreator< E > tabCreator );
	protected abstract void addTab( Tab<E> tab );
	protected abstract void removeTab( Tab<E> tab );
	@Override
	protected AbstractButton<?> createButton(E item) {
		Tab< E > tab = createTab( item, this.tabCreator );
		AbstractButton<?> rv = tab.getOuterTitleComponent();
		map.put(rv, tab);
		return rv;
	}
	@Override
	protected void removeAllButtons() {
		for( Tab<E> tab : this.map.values() ) {
			this.removeTab( tab );
		}
	}
	@Override
	protected void addPrologue( int count ) {
	}
	@Override
	protected final void addButton(edu.cmu.cs.dennisc.croquet.AbstractButton<?> button) {
		Tab< E > tab = this.map.get( button );
		assert tab != null;
		this.addTab( tab );
	}
	@Override
	protected void addEpilogue() {
	}
	
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
