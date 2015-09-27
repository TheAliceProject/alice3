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

package org.alice.ide.declarationseditor.type.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberList<E> extends org.lgna.croquet.views.ItemSelectablePanel<E> {
	protected final float NAME_FONT_SCALE = 1.5f;

	protected class MemberButton extends org.lgna.croquet.views.BooleanStateButton<javax.swing.AbstractButton> {
		public MemberButton( org.lgna.croquet.BooleanState booleanState, org.lgna.croquet.views.SwingComponentView<?> lineStart, org.lgna.croquet.views.SwingComponentView<?> center, org.lgna.croquet.views.SwingComponentView<?> lineEnd ) {
			super( booleanState );
			if( lineStart != null ) {
				this.addComponent( lineStart, org.lgna.croquet.views.BorderPanel.Constraint.LINE_START );
			}
			if( center != null ) {
				this.addComponent( center, org.lgna.croquet.views.BorderPanel.Constraint.CENTER );
			}
			if( lineEnd != null ) {
				this.addComponent( lineEnd, org.lgna.croquet.views.BorderPanel.Constraint.LINE_END );
			}

		}

		@Override
		protected javax.swing.AbstractButton createAwtComponent() {
			javax.swing.JToggleButton rv = new javax.swing.JToggleButton() {
				@Override
				public java.awt.Dimension getMaximumSize() {
					java.awt.Dimension rv = super.getPreferredSize();
					rv.width = Short.MAX_VALUE;
					return rv;
				}

				@Override
				protected void paintComponent( java.awt.Graphics g ) {
					//super.paintComponent(g);
					edu.cmu.cs.dennisc.java.awt.GraphicsContext gc = edu.cmu.cs.dennisc.java.awt.GraphicsContext.getInstanceAndPushGraphics( g );
					gc.pushAndSetAntialiasing( true );
					try {
						java.awt.Color color;
						color = MemberList.this.getBackgroundColor();
						if( this.getModel().isSelected() ) {
							color = color.darker();
						}
						g.setColor( color );
						g.fillRoundRect( 0, 0, this.getWidth() - 1, this.getHeight() - 1, 8, 8 );
						if( this.getModel().isRollover() ) {
							color = java.awt.Color.DARK_GRAY;
						} else {
							color = MemberList.this.getBackgroundColor().darker();
						}
						g.setColor( color );
						edu.cmu.cs.dennisc.java.awt.KnurlUtilities.paintKnurl5( g, 2, 2, 6, this.getHeight() - 5 );
						g.drawRoundRect( 0, 0, this.getWidth() - 1, this.getHeight() - 1, 8, 8 );
					} finally {
						gc.popAll();
					}
				}
			};
			rv.setOpaque( false );
			rv.setLayout( new java.awt.BorderLayout( 8, 0 ) );
			rv.setRolloverEnabled( true );
			return rv;
		}

		public void addComponent( org.lgna.croquet.views.AwtComponentView<?> component, org.lgna.croquet.views.BorderPanel.Constraint constraint ) {
			this.internalAddComponent( component, constraint.getInternal() );
		}
	}

	private org.lgna.croquet.views.PageAxisPanel pageAxisPanel = new org.lgna.croquet.views.PageAxisPanel();

	public MemberList( org.lgna.croquet.SingleSelectListState<E, ?> model, org.lgna.croquet.Operation... operations ) {
		super( model );
		this.internalAddComponent( pageAxisPanel );
		for( org.lgna.croquet.Operation operation : operations ) {
			if( operation != null ) {
				this.internalAddComponent( org.lgna.croquet.views.BoxUtilities.createVerticalSliver( 4 ) );
				this.internalAddComponent( operation.createButton() );
			}
		}
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new javax.swing.BoxLayout( jPanel, javax.swing.BoxLayout.PAGE_AXIS );
	}

	protected abstract org.lgna.croquet.views.SwingComponentView<?> createButtonLineStart( E item );

	protected abstract org.lgna.croquet.views.SwingComponentView<?> createButtonCenter( E item );

	protected abstract org.lgna.croquet.views.SwingComponentView<?> createButtonLineEnd( E item );

	@Override
	protected org.lgna.croquet.views.BooleanStateButton<?> createButtonForItemSelectedState( E item, org.lgna.croquet.BooleanState itemSelectedState ) {
		MemberButton memberButton = new MemberButton( itemSelectedState,
				this.createButtonLineStart( item ),
				this.createButtonCenter( item ),
				this.createButtonLineEnd( item )
				);
		return memberButton;
	}

	@Override
	protected void removeAllDetails() {
		for( org.lgna.croquet.views.BooleanStateButton<?> button : this.getAllButtons() ) {
			button.setVisible( false );
		}
	}

	@Override
	protected void addPrologue( int count ) {
		//this.pageAxisPanel.internalRemoveAllComponents();
		for( org.lgna.croquet.views.BooleanStateButton<?> button : this.getAllButtons() ) {
			button.setVisible( false );
		}
	}

	@Override
	protected void addItem( E item, org.lgna.croquet.views.BooleanStateButton<?> button ) {
		button.setVisible( true );
	}

	@Override
	protected void addEpilogue() {
	}
}
