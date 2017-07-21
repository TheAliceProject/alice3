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

import org.lgna.croquet.CancelException;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/class OperationFauxCascadeItem<F, B> extends org.lgna.croquet.CascadeFillIn<F, B> {
	public OperationFauxCascadeItem( org.lgna.croquet.Operation operation ) {
		super( java.util.UUID.fromString( "68f2167d-0763-4a43-8ce3-592e1562877c" ) );
		this.operation = operation;
	}

	@Override
	protected void initialize() {
		this.operation.initializeIfNecessary();
		super.initialize();
	}

	@Override
	public java.util.List<? extends org.lgna.croquet.CascadeBlank<B>> getBlanks() {
		return java.util.Collections.emptyList();
	}

	@Override
	public F getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node ) {
		return null;
	}

	@Override
	public F createValue( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		this.operation.fire();
		throw new CancelException();
	}

	@Override
	public String getMenuItemText( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node ) {
		return this.operation.getImp().getName();
	}

	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node ) {
		return this.operation.getImp().getSmallIcon();
	}

	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node ) {
		throw new Error( "Override of getMenuItemText and getMenuItemIcon should prevent this" );
	}

	private final org.lgna.croquet.Operation operation;
}
