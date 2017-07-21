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
public abstract class TreeDirectoryViewController<T> extends PanelViewController<org.lgna.croquet.SingleSelectTreeState<T>> {
	private static class InternalPanel<T> extends MigPanel {
		public InternalPanel() {
			super( null, "insets 0", "", "" );
		}

		@Override
		protected void internalRefresh() {
			this.internalRemoveAllComponents();

			//todo
			TreeDirectoryViewController<T> owner = (TreeDirectoryViewController<T>)this.getParent();
			if( owner != null ) {
				java.util.List<T> children = owner.getChildren();
				if( children != null ) {
					for( T child : children ) {
						SwingComponentView<?> component = owner.getComponentFor( child );
						if( component != null ) {
							this.internalAddComponent( component );
						}
					}
				}
			}
		}
	}

	private final org.lgna.croquet.State.ValueListener<T> valueListener = new org.lgna.croquet.State.ValueListener<T>() {
		@Override
		public void changing( org.lgna.croquet.State<T> state, T prevValue, T nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( org.lgna.croquet.State<T> state, T prevValue, T nextValue, boolean isAdjusting ) {
			TreeDirectoryViewController.this.handleSelectionChange( state, prevValue, nextValue, isAdjusting );
		}
	};

	public TreeDirectoryViewController( org.lgna.croquet.SingleSelectTreeState<T> model ) {
		super( model, new InternalPanel<T>() );

	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getModel().addValueListener( this.valueListener );
	}

	@Override
	protected void handleUndisplayable() {
		this.getModel().removeValueListener( this.valueListener );
		super.handleUndisplayable();
	}

	protected abstract SwingComponentView<?> getComponentFor( T value );

	protected java.util.List<T> getChildren() {
		org.lgna.croquet.SingleSelectTreeState<T> model = this.getModel();
		T node = model.getSelectedNode();
		if( node != null ) {
			if( model.isLeaf( node ) ) {
				node = model.getParent( node );
			}
			return model.getChildren( node );
		} else {
			return java.util.Collections.emptyList();
		}
	}

	protected void handleSelectionChange( org.lgna.croquet.State<T> state, T prevValue, T nextValue, boolean isAdjusting ) {
		this.getInternalPanel().refreshLater();
	}
}
