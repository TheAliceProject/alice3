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

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class ToolPaletteTabbedPane<E extends org.lgna.croquet.TabComposite<?>> extends TabbedPane<E> {

	private static class AccordionLayoutManager implements java.awt.LayoutManager {
		//private final java.util.List<java.awt.Component> awtComponents = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

		@Override
		public void addLayoutComponent( String name, java.awt.Component awtComponent ) {
			//this.awtComponents.add( awtComponent );
		}

		@Override
		public void removeLayoutComponent( java.awt.Component awtComponent ) {
			//this.awtComponents.remove( awtComponent );
		}

		@Override
		public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
			return new java.awt.Dimension( 0, 0 );
		}

		@Override
		public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
			final int N = parent.getComponentCount();
			if( ( N % 2 ) == 0 ) {
				java.awt.Dimension rv = new java.awt.Dimension( 0, 0 );
				for( int i = 0; i < N; i += 2 ) {
					javax.swing.AbstractButton button = (javax.swing.AbstractButton)parent.getComponent( i );
					java.awt.Component contents = parent.getComponent( i + 1 );
					javax.swing.ButtonModel buttonModel = button.getModel();
					rv.width = Math.max( rv.width, button.getPreferredSize().width );
					rv.width = Math.max( rv.width, contents.getPreferredSize().width );

					//todo: investigate why not preferredSize
					rv.height += button.getHeight();
					if( buttonModel.isSelected() ) {
						rv.height += contents.getPreferredSize().height;
					}
				}
				return rv;
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( N );
				return new java.awt.Dimension( 100, 100 );
			}
		}

		@Override
		public void layoutContainer( java.awt.Container parent ) {
			final int N = parent.getComponentCount();
			if( ( N % 2 ) == 0 ) {
				java.awt.Dimension parentSize = parent.getSize();
				int consumedHeight = 0;
				for( int i = 0; i < N; i += 2 ) {
					javax.swing.AbstractButton button = (javax.swing.AbstractButton)parent.getComponent( i );
					int height = button.getPreferredSize().height;
					button.setSize( parentSize.width, height );
					consumedHeight += height;
				}
				for( int i = 0; i < N; i += 2 ) {
					javax.swing.AbstractButton button = (javax.swing.AbstractButton)parent.getComponent( i );
					java.awt.Component contents = parent.getComponent( i + 1 );
					javax.swing.ButtonModel buttonModel = button.getModel();
					if( buttonModel.isSelected() ) {
						contents.setSize( parentSize.width, parentSize.height - consumedHeight );
					} else {
						contents.setSize( 0, 0 );
					}
				}
				int y = 0;
				for( int i = 0; i < N; i++ ) {
					java.awt.Component component = parent.getComponent( i );
					component.setLocation( 0, y );
					y += component.getHeight();
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( N );
			}
		}

	}

	//	private E card;

	public ToolPaletteTabbedPane( org.lgna.croquet.TabState<E, ?> model ) {
		super( model );
	}

	@Override
	protected BooleanStateButton<? extends javax.swing.AbstractButton> createTitleButton( E item, org.lgna.croquet.BooleanState itemSelectedState ) {
		ToolPaletteTitle rv = new ToolPaletteTitle( itemSelectedState );
		rv.setPartOfAccordion( true );
		rv.setHorizontalAlignment( HorizontalAlignment.LEADING );
		return rv;
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new AccordionLayoutManager();
	}

	@Override
	protected void handleValueChanged( E card ) {
		//		if( card != this.card ) {
		//			if( this.card != null ) {
		//				this.card.handlePostDeactivation();
		//			}
		//			this.card = card;
		//			if( this.card != null ) {
		//				this.card.handlePreActivation();
		//			}
		this.revalidateAndRepaint();
		//		}
	}

	@Override
	protected void removeAllDetails() {
		this.internalRemoveAllComponents();
	}

	@Override
	protected void addPrologue( int count ) {
	}

	@Override
	protected void addItem( E item, BooleanStateButton<?> button ) {
		this.internalAddComponent( button );
		this.internalAddComponent( item.getRootComponent() );
	}

	@Override
	protected void addEpilogue() {
	}
}
