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
package org.lgna.debug.tree.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class DebugFrame<T> extends org.lgna.croquet.FrameComposite<org.lgna.debug.tree.croquet.views.DebugFrameView<T>> {
	public DebugFrame( java.util.UUID migrationId ) {
		super( migrationId );
		this.markOperation.setName( "mark" );
		this.refreshOperation.setName( "refresh" );
		this.isPruningDesiredState.setTextForBothTrueAndFalse( "prune?" );
	}

	public org.lgna.croquet.Operation getMarkOperation() {
		return this.markOperation;
	}

	public org.lgna.croquet.Operation getRefreshOperation() {
		return this.refreshOperation;
	}

	public org.lgna.croquet.BooleanState getIsPruningDesiredState() {
		return this.isPruningDesiredState;
	}

	public javax.swing.tree.TreeModel getMarkTreeModel() {
		return this.markTreeModel;
	}

	public javax.swing.tree.TreeModel getCurrentTreeModel() {
		return this.currentTreeModel;
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.markOperation.fire();
		this.refreshOperation.fire();
		this.isPruningDesiredState.addNewSchoolValueListener( this.isPruningDesiredListener );
	}

	@Override
	public void handlePostDeactivation() {
		this.isPruningDesiredState.removeNewSchoolValueListener( this.isPruningDesiredListener );
		super.handlePostDeactivation();
	}

	@Override
	protected org.lgna.debug.tree.croquet.views.DebugFrameView<T> createView() {
		return new org.lgna.debug.tree.croquet.views.DebugFrameView<T>( this );
	}

	protected abstract org.lgna.debug.tree.core.ZTreeNode.Builder<T> capture();

	private static <T> void updateValuesToMute( java.util.Set<T> set, org.lgna.debug.tree.core.ZTreeNode<T> zTreeNode ) {
		set.add( zTreeNode.getValue() );
		if( zTreeNode.isLeaf() ) {
			//pass
		} else {
			java.util.Enumeration<org.lgna.debug.tree.core.ZTreeNode<T>> e = zTreeNode.children();
			while( e.hasMoreElements() ) {
				updateValuesToMute( set, e.nextElement() );
			}
		}
	}

	private static <T> java.util.Set<T> createValuesToMute( org.lgna.debug.tree.core.ZTreeNode<T> markRoot ) {
		java.util.Set<T> valuesToMute = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
		updateValuesToMute( valuesToMute, markRoot );
		return java.util.Collections.unmodifiableSet( valuesToMute );
	}

	private static <T> void prune( org.lgna.debug.tree.core.ZTreeNode.Builder<T> builder, java.util.Set<T> valuesToMute ) {
		java.util.Iterator<org.lgna.debug.tree.core.ZTreeNode.Builder<T>> iterator = builder.getChildBuildersIterator();
		while( iterator.hasNext() ) {
			org.lgna.debug.tree.core.ZTreeNode.Builder<T> childBuilder = iterator.next();
			prune( childBuilder, valuesToMute );
			T childValue = childBuilder.getValue();
			if( childBuilder.isEmpty() && valuesToMute.contains( childValue ) ) {
				iterator.remove();
			}
		}
	}

	private final org.lgna.croquet.Operation markOperation = this.createActionOperation( "markOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			org.lgna.debug.tree.core.ZTreeNode.Builder<T> builder = capture();
			org.lgna.debug.tree.core.ZTreeNode<T> markRoot = builder.build();
			markTreeModel.setRoot( markRoot );
			java.util.Set<T> valuesToMute;
			if( isPruningDesiredState.getValue() ) {
				currentTreeModel.setRoot( null );
				valuesToMute = null;
			} else {
				currentTreeModel.setRoot( markRoot );
				valuesToMute = createValuesToMute( markRoot );
			}
			getView().expandAllRowsAndUpdateCurrentTreeRenderer( valuesToMute );
			return null;
		}
	} );
	private final org.lgna.croquet.Operation refreshOperation = this.createActionOperation( "refreshOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			org.lgna.debug.tree.core.ZTreeNode<T> markRoot = (org.lgna.debug.tree.core.ZTreeNode<T>)markTreeModel.getRoot();
			java.util.Set<T> valuesToMute = createValuesToMute( markRoot );

			org.lgna.debug.tree.core.ZTreeNode.Builder<T> builder = capture();

			org.lgna.debug.tree.core.ZTreeNode<T> currentRoot;
			if( isPruningDesiredState.getValue() ) {
				prune( builder, valuesToMute );
				if( builder.isEmpty() && valuesToMute.contains( builder.getValue() ) ) {
					currentRoot = null;
				} else {
					currentRoot = builder.build();
				}
			} else {
				currentRoot = builder.build();
			}

			currentTreeModel.setRoot( currentRoot );
			getView().expandAllRowsAndUpdateCurrentTreeRenderer( valuesToMute );
			return null;
		}
	} );

	private final org.lgna.croquet.event.ValueListener<Boolean> isPruningDesiredListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<java.lang.Boolean> e ) {
			refreshOperation.fire();
		}
	};

	private final org.lgna.croquet.BooleanState isPruningDesiredState = this.createBooleanState( "isPruningDesiredState", true );

	private final javax.swing.tree.DefaultTreeModel markTreeModel = new javax.swing.tree.DefaultTreeModel( null );
	private final javax.swing.tree.DefaultTreeModel currentTreeModel = new javax.swing.tree.DefaultTreeModel( null );
}
