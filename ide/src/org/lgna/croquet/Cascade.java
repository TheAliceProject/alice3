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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class Cascade<T> extends CompletionModel {
	private final Class< T > componentType;
	private final CascadeRoot< T > root;

	private final CascadePopupPrepModel<T> popupPrepModel;
	public Cascade( Group group, java.util.UUID id, Class< T > componentType, CascadeBlank< T >[] blanks ) {
		super( group, id );
		this.componentType = componentType;
		this.root = new CascadeRoot< T >( this );
		assert blanks != null;
		for( int i=0; i<blanks.length; i++ ) {
			assert blanks[ i ] != null : this;
			root.addBlank( blanks[ i ] );
		}
		this.popupPrepModel = new CascadePopupPrepModel<T>( this );
	}
	public Cascade( Group group, java.util.UUID id, Class< T > componentType, CascadeBlank< T > blank ) {
		this( group, id, componentType, new CascadeBlank[] { blank } );
	}
	public CascadePopupPrepModel<T> getPopupPrepModel() {
		return this.popupPrepModel;
	}
	@Override
	protected void localize() {
	}
	@Override
	public org.lgna.croquet.history.Step< ? > fire( org.lgna.croquet.triggers.Trigger trigger ) {
		return null;
	}
	@Override
	public boolean isAlreadyInState( org.lgna.croquet.edits.Edit< ? > edit ) {
		return false;
	}
	public CascadeRoot< T > getRoot() {
		return this.root;
	}
	public Class< T > getComponentType() {
		return this.componentType;
	}
	protected void prologue() {
	}
	protected void epilogue() {
	}
	protected abstract org.lgna.croquet.edits.Edit< ? extends Cascade< T > > createEdit( org.lgna.croquet.history.CascadeCompletionStep< T > step, T[] values );
	private CascadeMenuItemPrepModel<T> menuItemPrepModel;
	public synchronized CascadeMenuItemPrepModel<T> getMenuItemPrepModel() {
		if( this.menuItemPrepModel != null ) {
			//pass
		} else {
			this.menuItemPrepModel = new CascadeMenuItemPrepModel<T>( this );
		}
		return this.menuItemPrepModel;
	}
	
	public void handleCompletion( org.lgna.croquet.history.CascadeCompletionStep< T > step, T[] values ) {
		try {
			org.lgna.croquet.edits.Edit< ? extends Cascade< T > > edit = this.createEdit( step, values );
			step.commitAndInvokeDo( edit );
		} finally {
			this.getPopupPrepModel().handleFinally();
		}
	}
	public void handleCancel( org.lgna.croquet.history.CascadeCompletionStep< T > completionStep, org.lgna.croquet.triggers.Trigger trigger, CancelException ce ) {
		try {
			if( completionStep != null ) {
				completionStep.cancel();
			} else {
				org.lgna.croquet.history.TransactionManager.addCancelCompletionStep( this, trigger );
			}
		} finally {
			this.getPopupPrepModel().handleFinally();
		}
	}
	
	
}