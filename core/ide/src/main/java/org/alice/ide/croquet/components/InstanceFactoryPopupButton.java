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

package org.alice.ide.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactoryPopupButton extends org.lgna.croquet.views.CustomItemStatePopupButton<org.alice.ide.instancefactory.InstanceFactory> {
	// note: for singleton ThisInstanceFactory
	private final org.lgna.croquet.event.ValueListener<org.lgna.project.ast.NamedUserType> typeListener = new org.lgna.croquet.event.ValueListener<org.lgna.project.ast.NamedUserType>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.project.ast.NamedUserType> e ) {
			InstanceFactoryPopupButton.this.repaint();
		}
	};

	public InstanceFactoryPopupButton( org.alice.ide.instancefactory.croquet.InstanceFactoryState instanceFactoryState ) {
		super( instanceFactoryState );
		this.getAwtComponent().setLayout( new javax.swing.BoxLayout( this.getAwtComponent(), javax.swing.BoxLayout.LINE_AXIS ) );
	}

	@Override
	protected javax.swing.AbstractButton createSwingButton() {
		return new JFauxComboBoxPopupButton() {
			@Override
			public void invalidate() {
				super.invalidate();
				refreshIfNecessary();
			}
		};
	}

	private org.alice.ide.instancefactory.InstanceFactory nextValue;

	@Override
	protected void handleChanged( org.lgna.croquet.State<org.alice.ide.instancefactory.InstanceFactory> state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
		this.nextValue = nextValue;
		this.refreshLater();
	}

	private boolean isInTheMidstOfRefreshing = false;
	private boolean isRefreshNecessary = true;

	protected void internalRefresh() {
		this.internalForgetAndRemoveAllComponents();
		org.lgna.croquet.views.SwingComponentView<?> expressionPane = org.alice.ide.x.PreviewAstI18nFactory.getInstance().createExpressionPane( nextValue != null ? nextValue.createTransientExpression() : null );

		for( javax.swing.JLabel label : edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( expressionPane.getAwtComponent(), edu.cmu.cs.dennisc.pattern.HowMuch.COMPONENT_AND_DESCENDANTS, javax.swing.JLabel.class ) ) {
			edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToScaledFont( label, 2.0f );
		}

		if( nextValue != null ) {
			org.lgna.croquet.icon.IconFactory iconFactory = nextValue.getIconFactory();
			if( ( iconFactory != null ) && ( iconFactory != org.lgna.croquet.icon.EmptyIconFactory.getInstance() ) ) {
				final boolean IS_TRIMMED_ICON_DESIRED = true;
				java.awt.Dimension size = IS_TRIMMED_ICON_DESIRED ? iconFactory.getTrimmedSizeForHeight( org.alice.ide.Theme.DEFAULT_SMALL_ICON_SIZE.height ) : org.alice.ide.Theme.DEFAULT_SMALL_ICON_SIZE;
				javax.swing.Icon icon = iconFactory.getIcon( size );
				if( icon != null ) {
					this.internalAddComponent( new org.lgna.croquet.views.Label( icon ) );
				}
			}
		}
		this.internalAddComponent( expressionPane );

		this.revalidateAndRepaint();
	}

	private void refreshIfNecessary() {
		if( this.isRefreshNecessary ) {
			if( this.isInTheMidstOfRefreshing ) {
				//pass
			} else {
				this.isInTheMidstOfRefreshing = true;
				try {
					//this.forgetAndRemoveAllComponents();
					synchronized( this.getTreeLock() ) {
						this.internalRefresh();
					}
					this.isRefreshNecessary = false;
				} finally {
					this.isInTheMidstOfRefreshing = false;
				}
			}
		}
	}

	public final void refreshLater() {
		this.isRefreshNecessary = true;
		this.revalidateAndRepaint();
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.refreshLater();
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().addValueListener( this.typeListener );
	}

	@Override
	protected void handleUndisplayable() {
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().removeValueListener( this.typeListener );
		super.handleUndisplayable();
	}
};
