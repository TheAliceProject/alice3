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
public class InstanceFactoryDropDown< M extends org.lgna.croquet.CustomItemState< org.alice.ide.instancefactory.InstanceFactory > > extends org.lgna.croquet.components.ItemDropDown< org.alice.ide.instancefactory.InstanceFactory, M > {
	public static final java.awt.Dimension DEFAULT_ICON_SIZE = new java.awt.Dimension( 40, 30 );
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
				javax.swing.Icon icon = nextValue.getIconFactory().getIcon( DEFAULT_ICON_SIZE );
				if( icon != null ) {
					this.addLineStartComponent( new org.lgna.croquet.components.Label( icon ) );
				}
			}
		}
	};
	private final MainComponent mainComponent = new MainComponent();
	public InstanceFactoryDropDown( M model ) {
		super( model );
		this.setMainComponent( this.mainComponent );
		this.getAwtComponent().setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
		this.setBackgroundColor( new java.awt.Color( 127, 127, 191 ) );
	}
	@Override
	protected int getAffordanceHalfHeight() {
		return super.getAffordanceHalfHeight() * 2;
	}
	@Override
	protected int getAffordanceWidth() {
		return super.getAffordanceWidth() * 2;
	}
	@Override
	protected void handleChanged( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
		this.mainComponent.handleChanged( nextValue );
	}
};
