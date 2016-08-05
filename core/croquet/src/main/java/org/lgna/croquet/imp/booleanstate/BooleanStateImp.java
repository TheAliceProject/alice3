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

import org.lgna.croquet.PrepModel;

/**
 * @author Dennis Cosgrove
 */
public class BooleanStateImp {
	public BooleanStateImp( org.lgna.croquet.BooleanState state, boolean initialValue, javax.swing.ButtonModel buttonModel ) {
		this.state = state;
		this.swingModel = new BooleanStateSwingModel( state, buttonModel, initialValue );
	}

	public BooleanStateSwingModel getSwingModel() {
		return this.swingModel;
	}

	public boolean isEnabled() {
		return this.swingModel.getAction().isEnabled();
	}

	public void setEnabled( boolean isEnabled ) {
		this.swingModel.getAction().setEnabled( isEnabled );
	}

	public void updateNameAndIcon( boolean value, String trueText, javax.swing.Icon trueIcon, String falseText, javax.swing.Icon falseIcon ) {
		String name;
		javax.swing.Icon icon;
		if( value ) {
			name = trueText;
			icon = trueIcon;
		} else {
			name = falseText;
			icon = falseIcon;
		}
		this.swingModel.getAction().putValue( javax.swing.Action.NAME, name );
		this.swingModel.getAction().putValue( javax.swing.Action.SMALL_ICON, icon );

		if( this.trueOperation != null ) {
			this.trueOperation.setName( trueText );
			this.trueOperation.setButtonIcon( trueIcon );
		}
		if( this.falseOperation != null ) {
			this.falseOperation.setName( falseText );
			this.falseOperation.setButtonIcon( falseIcon );
		}
	}

	public synchronized org.lgna.croquet.Operation getSetToTrueOperation() {
		if( this.trueOperation != null ) {
			//pass
		} else {
			this.trueOperation = new BooleanStateSetToValueOperation( this.state, true );
		}
		return this.trueOperation;
	}

	public synchronized org.lgna.croquet.Operation getSetToFalseOperation() {
		if( this.falseOperation != null ) {
			//pass
		} else {
			this.falseOperation = new BooleanStateSetToValueOperation( this.state, false );
		}
		return this.falseOperation;
	}

	public synchronized org.lgna.croquet.imp.booleanstate.BooleanStateMenuModel getMenuModel() {
		if( this.menuModel != null ) {
			//pass
		} else {
			this.menuModel = new org.lgna.croquet.imp.booleanstate.BooleanStateMenuModel( this.state );
		}
		return this.menuModel;
	}

	public synchronized BooleanStateMenuItemPrepModel getMenuItemPrepModel() {
		if( this.menuPrepModel != null ) {
			//pass
		} else {
			this.menuPrepModel = new BooleanStateMenuItemPrepModel( this.state );
		}
		return this.menuPrepModel;
	}

	public java.util.List<java.util.List<PrepModel>> getPotentialPrepModelPaths( org.lgna.croquet.edits.Edit edit ) {
		if( this.menuPrepModel != null ) {
			return edu.cmu.cs.dennisc.java.util.Lists.newArrayListOfSingleArrayList( this.menuPrepModel );
		} else {
			return java.util.Collections.emptyList();
		}
	}

	private final BooleanStateSwingModel swingModel;
	private final org.lgna.croquet.BooleanState state;
	private org.lgna.croquet.Operation trueOperation;
	private org.lgna.croquet.Operation falseOperation;
	private BooleanStateMenuModel menuModel;
	private BooleanStateMenuItemPrepModel menuPrepModel;
}
