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

package org.alice.ide.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactoryPopupButton extends org.lgna.croquet.components.CustomItemStatePopupButton<org.alice.ide.instancefactory.InstanceFactory> {
	private static class MainComponent extends org.lgna.croquet.components.BorderPanel {
		private org.alice.ide.instancefactory.InstanceFactory nextValue;

		private void handleChanged( org.alice.ide.instancefactory.InstanceFactory nextValue ) {
			this.nextValue = nextValue;
			this.refreshLater();
		}

		@Override
		protected void internalRefresh() {
			super.internalRefresh();
			this.forgetAndRemoveAllComponents();
			org.lgna.croquet.components.JComponent<?> expressionPane = org.alice.ide.x.PreviewAstI18nFactory.getInstance().createExpressionPane( nextValue != null ? nextValue.createTransientExpression() : null );

			for( javax.swing.JLabel label : edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( expressionPane.getAwtComponent(), edu.cmu.cs.dennisc.pattern.HowMuch.COMPONENT_AND_DESCENDANTS, javax.swing.JLabel.class ) ) {
				edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToScaledFont( label, 2.0f );
			}

			this.addCenterComponent( expressionPane );
			if( nextValue != null ) {
				org.lgna.croquet.icon.IconFactory iconFactory = nextValue.getIconFactory();
				if( ( iconFactory != null ) && ( iconFactory != org.lgna.croquet.icon.EmptyIconFactory.SINGLETON ) ) {
					javax.swing.Icon icon = iconFactory.getIcon( org.alice.ide.Theme.DEFAULT_SMALL_ICON_SIZE );
					if( icon != null ) {
						this.addLineStartComponent( new org.lgna.croquet.components.Label( icon ) );
					}
				}
			}
		}
	};

	// note: for singleton ThisInstanceFactory
	private final org.lgna.croquet.State.ValueListener<org.lgna.project.ast.NamedUserType> typeListener = new org.lgna.croquet.State.ValueListener<org.lgna.project.ast.NamedUserType>() {
		public void changing( org.lgna.croquet.State<org.lgna.project.ast.NamedUserType> state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<org.lgna.project.ast.NamedUserType> state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
			InstanceFactoryPopupButton.this.repaint();
		}
	};
	private final MainComponent mainComponent = new MainComponent();
	private static final int SIZE = 16;
	private static final javax.swing.Icon ARROW_ICON = new edu.cmu.cs.dennisc.javax.swing.icons.DropDownArrowIcon( SIZE );

	public InstanceFactoryPopupButton( org.alice.ide.instancefactory.croquet.InstanceFactoryState instanceFactoryState ) {
		super( instanceFactoryState );
		this.getAwtComponent().setLayout( new java.awt.BorderLayout() );
		this.getAwtComponent().removeAll();
		this.internalAddComponent( this.mainComponent, java.awt.BorderLayout.LINE_START );
	}

	private static final java.awt.Color TOP_COLOR = new java.awt.Color( 255, 255, 255, 91 );
	private static final java.awt.Color BOTTOM_COLOR = new java.awt.Color( 57, 105, 138, 91 );
	private static final java.awt.Color LINE_COLOR = new java.awt.Color( 169, 176, 190 );

	private static final java.awt.Color SELECTED_COLOR = new java.awt.Color( 57, 105, 138 );
	private static final java.awt.Color SELECTED_LINE_COLOR = java.awt.Color.DARK_GRAY;

	@Override
	protected javax.swing.AbstractButton createSwingButton() {
		final int COMBO_PAD = 12;
		final int OUTER_PAD = 6;
		JPopupButton rv = new JPopupButton() {
			@Override
			public java.awt.Insets getMargin() {
				java.awt.Insets rv = super.getMargin();
				if( rv != null ) {
					rv.right += SIZE;
					rv.right += COMBO_PAD;
					rv.right += OUTER_PAD;
				}
				return rv;
			}

			@Override
			protected void paintBorder( java.awt.Graphics g ) {
				super.paintBorder( g );
				java.awt.Insets insets = this.getInsets();
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				int width = this.getWidth();
				int height = this.getHeight();
				int x = ( width - insets.right ) + OUTER_PAD;
				if( this.getUI().getClass().getSimpleName().contains( "Synth" ) ) {
					double round = 8;
					double inset = 2;
					java.awt.geom.RoundRectangle2D r = new java.awt.geom.RoundRectangle2D.Double( inset, inset, width - ( inset * 2 ), ( height - ( inset * 2 ) ) + 1, round, round );

					java.awt.Shape prevClip = g2.getClip();

					g2.setClip( edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createIntersection( prevClip, r ) );

					javax.swing.ButtonModel buttonModel = this.getModel();
					boolean isSelected = buttonModel.isPressed() || buttonModel.isSelected();
					if( isSelected ) {
						g2.setPaint( SELECTED_COLOR );
					} else {
						g2.setPaint( new java.awt.GradientPaint( width, 0, TOP_COLOR, width, ( 2 * height ) / 3, BOTTOM_COLOR ) );
					}
					g.fillRect( x, 0, width - x, height );
					if( isSelected ) {
						g2.setPaint( SELECTED_LINE_COLOR );
					} else {
						g2.setPaint( LINE_COLOR );
					}
					g2.fillRect( x, 0, 1, height );
					g2.setClip( prevClip );
				}
				ARROW_ICON.paintIcon( this, g2, x + COMBO_PAD, ( height - SIZE ) / 2 );
			}
		};
		rv.setHorizontalTextPosition( javax.swing.SwingConstants.LEADING );
		return rv;
	}

	@Override
	protected void handleChanged( org.lgna.croquet.State<org.alice.ide.instancefactory.InstanceFactory> state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
		this.mainComponent.handleChanged( nextValue );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.declarationseditor.TypeState.getInstance().addValueListener( this.typeListener );
	}

	@Override
	protected void handleUndisplayable() {
		org.alice.ide.declarationseditor.TypeState.getInstance().removeValueListener( this.typeListener );
		super.handleUndisplayable();
	}
};
