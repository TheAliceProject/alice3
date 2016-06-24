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
package org.lgna.croquet.imp.booleanstate;

/**
 * @author Dennis Cosgrove
 */
public class BooleanStateSwingModel {
	/*package-private*/BooleanStateSwingModel( org.lgna.croquet.BooleanState state, javax.swing.ButtonModel buttonModel, boolean initialValue ) {
		this.state = state;
		this.buttonModel = buttonModel;
		this.buttonModel.setSelected( initialValue );
	}

	public javax.swing.ButtonModel getButtonModel() {
		return this.buttonModel;
	}

	public javax.swing.Action getAction() {
		return this.action;
	}

	private boolean isTextVariable() {
		return edu.cmu.cs.dennisc.java.util.Objects.notEquals( this.state.getTrueText(), this.state.getFalseText() );
	}

	private boolean isIconVariable() {
		return edu.cmu.cs.dennisc.java.util.Objects.notEquals( this.state.getTrueIcon(), this.state.getFalseIcon() );
	}

	private final org.lgna.croquet.BooleanState state;
	private final javax.swing.ButtonModel buttonModel;
	private final javax.swing.Action action = new javax.swing.AbstractAction() {
		@Override
		public Object getValue( String key ) {
			if( NAME.equals( key ) ) {
				return state.getTextFor( buttonModel.isSelected() );
			} else if( SMALL_ICON.equals( key ) ) {
				return state.getIconFor( buttonModel.isSelected() );
			} else {
				return super.getValue( key );
			}
		}

		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			boolean isSelected = buttonModel.isSelected();
			if( isTextVariable() ) {
				//this.firePropertyChange( NAME, getTextFor( !isSelected ), getTextFor( isSelected ) );
				this.putValue( NAME, state.getTextFor( isSelected ) );
			}
			if( isIconVariable() ) {
				//this.firePropertyChange( SMALL_ICON, getIconFor( !isSelected ), getIconFor( isSelected ) );
				this.putValue( SMALL_ICON, state.getIconFor( isSelected ) );
			}
		}
	};
}
