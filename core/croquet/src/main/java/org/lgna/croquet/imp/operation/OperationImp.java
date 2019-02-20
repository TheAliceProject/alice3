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
package org.lgna.croquet.imp.operation;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.pattern.Lazy;
import org.lgna.croquet.Operation;
import org.lgna.croquet.PrepModel;
import org.lgna.croquet.StandardMenuItemPrepModel;
import org.lgna.croquet.edits.Edit;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class OperationImp {
	public OperationImp( Operation operation ) {
		this.operation = operation;
		this.swingModel = new OperationSwingModel( operation );
	}

	public OperationSwingModel getSwingModel() {
		return this.swingModel;
	}

	public String getName() {
		return String.class.cast( this.swingModel.action.getValue( Action.NAME ) );
	}

	public void setName( String name ) {
		this.swingModel.action.putValue( Action.NAME, name );
	}

	public void setShortDescription( String shortDescription ) {
		this.swingModel.action.putValue( Action.SHORT_DESCRIPTION, shortDescription );
	}

	public void setSmallIcon( Icon icon ) {
		this.swingModel.action.putValue( Action.SMALL_ICON, icon );
	}

	public void setAcceleratorKey( KeyStroke acceleratorKey ) {
		this.swingModel.action.putValue( Action.ACCELERATOR_KEY, acceleratorKey );
	}

	public StandardMenuItemPrepModel getMenuItemPrepModel() {
		return this.menuItemPrepModel.get();
	}

	public List<List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
		if( this.menuItemPrepModel.peek() != null ) {
			return Lists.newArrayListOfSingleArrayList( this.menuItemPrepModel.get() );
		} else {
			return Collections.emptyList();
		}
	}

	private final Operation operation;
	private final OperationSwingModel swingModel;
	private final Lazy<StandardMenuItemPrepModel> menuItemPrepModel = new Lazy<StandardMenuItemPrepModel>() {
		@Override
		protected StandardMenuItemPrepModel create() {
			return new OperationMenuItemPrepModel( operation );
		}
	};
}
