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

package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public class ProjectDocument implements org.lgna.croquet.Document {

	private final org.lgna.project.Project project;
	private final org.lgna.croquet.history.TransactionHistory transactionHistory;
	private final org.alice.ide.ProjectHistoryManager projectHistoryManager;

	private final org.alice.ide.type.TypeCache typeCache;

	public ProjectDocument( org.lgna.project.Project project ) {
		this.project = project;
		this.transactionHistory = new org.lgna.croquet.history.TransactionHistory();
		this.projectHistoryManager = new org.alice.ide.ProjectHistoryManager( this );

		// TODO: We need to store this transaction history as part of the profile file
		//this.putValueFor( org.lgna.croquet.history.TransactionHistory.INTERACTION_HISTORY_PROPERTY_KEY, this.transactionHistory );

		this.typeCache = new org.alice.ide.type.TypeCache( this.project );
	}

	public org.lgna.project.Project getProject() {
		return this.project;
	}

	public org.alice.ide.type.TypeCache getTypeCache() {
		return this.typeCache;
	}

	@Override
	public org.lgna.croquet.history.TransactionHistory getRootTransactionHistory() {
		return this.transactionHistory;
	}

	@Override
	public org.lgna.croquet.undo.UndoHistory getUndoHistory( org.lgna.croquet.Group group ) {
		return this.projectHistoryManager.getGroupHistory( group );
	}
}
