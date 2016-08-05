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

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class ItemDropDown<T, CM extends org.lgna.croquet.CustomItemState<T>> extends DropDown<org.lgna.croquet.Cascade.InternalRoot.InternalPopupPrepModel<T>> {
	public ItemDropDown( CM state, org.lgna.croquet.views.SwingComponentView<?> prefixComponent, org.lgna.croquet.views.SwingComponentView<?> mainComponent, org.lgna.croquet.views.SwingComponentView<?> postfixComponent ) {
		super( state.getCascadeRoot().getPopupPrepModel(), prefixComponent, mainComponent, postfixComponent );
	}

	public ItemDropDown( CM model ) {
		this( model, null, null, null );
	}

	private final org.lgna.croquet.State.ValueListener<T> valueListener = new org.lgna.croquet.State.ValueListener<T>() {
		@Override
		public void changing( org.lgna.croquet.State<T> state, T prevValue, T nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( org.lgna.croquet.State<T> state, T prevValue, T nextValue, boolean isAdjusting ) {
			ItemDropDown.this.handleChanged( state, prevValue, nextValue, isAdjusting );
		}
	};

	protected abstract void handleChanged( org.lgna.croquet.State<T> state, T prevValue, T nextValue, boolean isAdjusting );

	private org.lgna.croquet.CustomItemState<T> getState() {
		org.lgna.croquet.Cascade.InternalRoot.InternalPopupPrepModel<T> model = this.getModel();
		org.lgna.croquet.CustomItemState.InternalRoot<T> root = (org.lgna.croquet.CustomItemState.InternalRoot<T>)model.getCascadeRoot();
		return root.getCompletionModel();
	}

	@Override
	protected void handleAddedTo( org.lgna.croquet.views.AwtComponentView<?> parent ) {
		this.getState().addAndInvokeValueListener( this.valueListener );
		super.handleAddedTo( parent );
	}

	@Override
	protected void handleRemovedFrom( org.lgna.croquet.views.AwtComponentView<?> parent ) {
		super.handleRemovedFrom( parent );
		this.getState().removeValueListener( this.valueListener );
	}
}
