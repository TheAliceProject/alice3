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
public final class ToolPaletteTabbedPane<E> extends AbstractTabbedPane<E, AbstractTabbedPane.TabItemDetails> {
	private static class JToolPaletteTabTitle extends JTabTitle {
		public JToolPaletteTabTitle( javax.swing.JComponent jComponent, boolean isCloseAffordanceDesired ) {
			super( jComponent, isCloseAffordanceDesired );
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 32, 4, 4 ) );
			this.setOpaque( true );
		}
		@Override
		protected void paintComponent(java.awt.Graphics g) {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			java.awt.Paint paint;
			final double FACTOR;
			if( this.getModel().isArmed() ) {
				FACTOR = 1.3;
			} else {
				FACTOR = 1.15;
			}
			final double INVERSE_FACTOR = 1.0 / FACTOR;
			java.awt.Color colorA = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB(this.getBackground(), 1.0, FACTOR, FACTOR );
			java.awt.Color colorB = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB(this.getBackground(), 1.0, INVERSE_FACTOR, INVERSE_FACTOR );
			paint = new java.awt.GradientPaint(0,0, colorA, 0, this.getHeight(), colorB );
			g2.setPaint( paint );
			g2.fill( g2.getClip() );

			super.paintComponent(g);
			
			int height = this.getHeight();
			final int SIZE = 6;
			java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
			path.moveTo( SIZE, 0 );
			path.lineTo( -SIZE, SIZE );
			path.lineTo( -SIZE, -SIZE );
			path.closePath();
			
			int x = 18;
			int y = height/2;
			
			java.awt.geom.AffineTransform m = g2.getTransform();
			
			
			g.translate(x, y);
			if( this.getModel().isSelected() || this.getModel().isPressed() ) {
				g2.rotate( Math.PI/2 );
			}
			java.awt.Paint fillPaint = java.awt.Color.WHITE;
			if( this.getModel().isSelected() ) {
				//pass
			} else {
				if( this.getModel().isPressed() ) {
					fillPaint = java.awt.Color.YELLOW.darker();
				} else {
					if( this.getModel().isArmed() ) {
						fillPaint = java.awt.Color.YELLOW;
					}
				}
			}
			g2.setPaint( fillPaint );
			g2.fill( path );
			g2.setPaint( java.awt.Color.BLACK );
			g2.draw( path );
			
			g2.setTransform( m );
		}
	}

	private static class ToolPaletteTabTitle extends TabTitle {
		public ToolPaletteTabTitle( JComponent<?> innerTitleComponent, boolean isCloseAffordanceDesired ) {
			super( innerTitleComponent, isCloseAffordanceDesired );
		}
		@Override
		protected javax.swing.AbstractButton createAwtComponent() {
			return new JToolPaletteTabTitle( this.getInnerTitleComponent().getAwtComponent(), this.isCloseAffordanceDesired() );
		}
	}

//	private class ToolPaletteTab<E> extends Tab< E > {
//		private ToolPaletteTabTitle outerTileComponent;
//		public ToolPaletteTab( E item, ItemSelectionOperation.TabCreator< E > tabCreator ) {
//			super( item, tabCreator );
//			this.outerTileComponent = new ToolPaletteTabTitle( this.getInnerTitleComponent(), this.isCloseButtonDesired() );
//			this.outerTileComponent.getAwtComponent().getModel().addChangeListener( new javax.swing.event.ChangeListener() {
//				public void stateChanged(javax.swing.event.ChangeEvent e) {
//					getScrollPane().setVisible( outerTileComponent.getAwtComponent().getModel().isSelected() );
//					ToolPaletteTabbedPane.this.revalidateAndRepaint();
//				}
//			} );
//		}
//		@Override
//		public AbstractButton< ? > getOuterTitleComponent() {
//			return this.outerTileComponent;
//		}
//		@Override
//		public void select() {
//			throw new RuntimeException( "todo" );
//		}
//	}
//
	public ToolPaletteTabbedPane( ItemSelectionState.TabCreator< E > tabCreator ) {
		super( tabCreator );
	}
//	@Override
//	protected AbstractTabbedPane.Tab< E > createTab( E item, ItemSelectionOperation.TabCreator< E > tabCreator ) {
//		return new ToolPaletteTab<E>( item, tabCreator );
//	}

	@Override
	protected TabItemDetails createTabItemDetails( E item, java.util.UUID id, JComponent<?> innerTitleComponent, ScrollPane scrollPane, JComponent<?> mainComponent, boolean isCloseAffordanceDesired ) {
		AbstractButton<?> button = new ToolPaletteTabTitle(innerTitleComponent, isCloseAffordanceDesired);
		scrollPane.setVisible( false );
		return new TabItemDetails( item, button, id, innerTitleComponent, scrollPane, mainComponent, isCloseAffordanceDesired ) {
			@Override
			public void setSelected(boolean isSelected) {
				super.setSelected(isSelected);
				for( TabItemDetails tabItemDetails : getAllItemDetails() ) {
					tabItemDetails.getScrollPane().setVisible( tabItemDetails == this );
				}
			}
		};
	};
	
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.GridBagLayout();
	}

	@Override
	protected void removeAllDetails() {
		this.removeAllComponents();
	}
	@Override
	protected void addPrologue(int count) {
	}
	@Override
	protected void addItem( AbstractTabbedPane.TabItemDetails itemDetails) {
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0f;
		gbc.weighty = 0.0f;
		this.internalAddComponent( itemDetails.getButton(), gbc );
		gbc.weighty = 1.0f;
		this.internalAddComponent( itemDetails.getScrollPane(), gbc );
	}
	@Override
	protected void addEpilogue() {
	}
	
	
//	@Override
//	protected void addTab(edu.cmu.cs.dennisc.croquet.AbstractTabbedPane.Tab<E> tab) {
//		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
//		gbc.fill = java.awt.GridBagConstraints.BOTH;
//		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//		gbc.weightx = 1.0f;
//		gbc.weighty = 0.0f;
//		this.internalAddComponent( tab.getOuterTitleComponent(), gbc );
//		gbc.weighty = 1.0f;
//		this.internalAddComponent( tab.getScrollPane(), gbc );
//	}
//	
//	@Override
//	protected void removeTab(edu.cmu.cs.dennisc.croquet.AbstractTabbedPane.Tab<E> tab) {
//		this.removeComponent( tab.getOuterTitleComponent() );
//		this.removeComponent( tab.getScrollPane() );
//	}
	
	
//	private java.util.List< TabStateOperation<?> > tabStateOperations = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
//	@Override
//	/*package-private*/ void addTab(edu.cmu.cs.dennisc.croquet.TabStateOperation<?> tabStateOperation) {
//		super.addTab(tabStateOperation);
//		this.tabStateOperations.add( tabStateOperation );
//		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
//		gbc.fill = java.awt.GridBagConstraints.BOTH;
//		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//		gbc.weightx = 1.0f;
//		gbc.weighty = 0.0f;
//		this.panel.addComponent( tabStateOperation.getSingletonTabTitle( this ), gbc );
//		gbc.weighty = 1.0f;
//		this.panel.addComponent( tabStateOperation.getSingletonScrollPane(), gbc );
//		//tabStateOperation.getSingletonScrollPane().setVisible( tabStateOperation.getState() );
//	}
//	@Override
//	/*package-private*/ void removeTab(edu.cmu.cs.dennisc.croquet.TabStateOperation<?> tabStateOperation) {
//		super.removeTab(tabStateOperation);
//		this.tabStateOperations.remove( tabStateOperation );
//		this.panel.removeComponent( tabStateOperation.getSingletonTabTitle( this ) );
//		this.panel.removeComponent( tabStateOperation.getSingletonScrollPane() );
//	}
//	@Override
//	/*package-private*/ void selectTab(edu.cmu.cs.dennisc.croquet.TabStateOperation<?> nextTabStateOperation) {
//		for( TabStateOperation tabStateOperation : this.tabStateOperations ) {
//			tabStateOperation.getSingletonScrollPane().setVisible( tabStateOperation == nextTabStateOperation );
//		}
//		this.revalidateAndRepaint();
//	}
//	@Override
//	protected Component< ? >[] getTabTitles() {
//		final int N = this.tabStateOperations.size();
//		Component< ? >[] rv = new Component< ? >[ N ];
//		for( int i=0; i<N; i++ ) {
//			rv[ i ] = this.tabStateOperations.get( i ).getSingletonTabTitle( this );
//		}
//		return rv;
//	}
}
