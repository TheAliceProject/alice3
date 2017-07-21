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

package org.alice.ide.resource.manager;

/**
 * @author Dennis Cosgrove
 */
public class RenameResourceComposite extends org.alice.ide.ast.rename.RenameComposite<org.alice.ide.resource.manager.views.ResourceRenamePanel> {
	public RenameResourceComposite( org.lgna.croquet.ItemState<org.lgna.common.Resource> resourceState ) {
		super( java.util.UUID.fromString( "52410415-1293-4857-9e35-4d52bc4f2a9d" ), new org.alice.ide.name.validators.ResourceNameValidator() );
		this.resourceState = resourceState;
	}

	private org.lgna.common.Resource getResource() {
		return this.resourceState.getValue();
	}

	@Override
	protected String getInitialValue() {
		org.lgna.common.Resource resource = this.getResource();
		return resource != null ? resource.getName() : null;
	}

	@Override
	protected org.alice.ide.resource.manager.views.ResourceRenamePanel createView() {
		return new org.alice.ide.resource.manager.views.ResourceRenamePanel( this );
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		org.lgna.common.Resource resource = this.getResource();
		if( resource != null ) {
			return new org.alice.ide.resource.manager.edits.RenameResourceEdit( completionStep, resource, resource.getName(), this.getNameState().getValue() );
		} else {
			return null;
		}
	}

	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		org.lgna.common.Resource resource = this.resourceState.getValue();
		( (org.alice.ide.name.validators.ResourceNameValidator)this.getNameValidator() ).setResource( resource );
		this.getView().setResource( resource );
		super.handlePreShowDialog( step );
	}

	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		super.handlePostHideDialog( completionStep );
		this.getView().onHide();
	}

	private final org.lgna.croquet.ItemState<org.lgna.common.Resource> resourceState;
}
