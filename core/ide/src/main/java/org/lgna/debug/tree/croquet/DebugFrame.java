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

import edu.cmu.cs.dennisc.java.util.Sets;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.FrameComposite;
import org.lgna.croquet.Operation;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.UserActivity;
import org.lgna.debug.tree.core.ZTreeNode;
import org.lgna.debug.tree.croquet.views.DebugFrameView;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class DebugFrame<T> extends FrameComposite<DebugFrameView<T>> {
	public DebugFrame( UUID migrationId ) {
		super( migrationId );
		this.markOperation.setName( "mark" );
		this.refreshOperation.setName( "refresh" );
		this.isPruningDesiredState.setTextForBothTrueAndFalse( "prune?" );
	}

	public Operation getMarkOperation() {
		return this.markOperation;
	}

	public Operation getRefreshOperation() {
		return this.refreshOperation;
	}

	public BooleanState getIsPruningDesiredState() {
		return this.isPruningDesiredState;
	}

	public TreeModel getMarkTreeModel() {
		return this.markTreeModel;
	}

	public TreeModel getCurrentTreeModel() {
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
	protected DebugFrameView<T> createView() {
		return new DebugFrameView<T>( this );
	}

	protected abstract ZTreeNode.Builder<T> capture();

	private static <T> void updateValuesToMute( Set<T> set, ZTreeNode<T> zTreeNode ) {
		set.add( zTreeNode.getValue() );
		if( zTreeNode.isLeaf() ) {
			//pass
		} else {
			Enumeration<ZTreeNode<T>> e = zTreeNode.children();
			while( e.hasMoreElements() ) {
				updateValuesToMute( set, e.nextElement() );
			}
		}
	}

	private static <T> Set<T> createValuesToMute( ZTreeNode<T> markRoot ) {
		Set<T> valuesToMute = Sets.newHashSet();
		updateValuesToMute( valuesToMute, markRoot );
		return Collections.unmodifiableSet( valuesToMute );
	}

	private static <T> void prune( ZTreeNode.Builder<T> builder, Set<T> valuesToMute ) {
		Iterator<ZTreeNode.Builder<T>> iterator = builder.getChildBuildersIterator();
		while( iterator.hasNext() ) {
			ZTreeNode.Builder<T> childBuilder = iterator.next();
			prune( childBuilder, valuesToMute );
			T childValue = childBuilder.getValue();
			if( childBuilder.isEmpty() && valuesToMute.contains( childValue ) ) {
				iterator.remove();
			}
		}
	}

	private final Operation markOperation = this.createActionOperation( "markOperation", new Action() {
		@Override
		public Edit perform( UserActivity userActivity, InternalActionOperation source ) throws CancelException {
			ZTreeNode.Builder<T> builder = capture();
			ZTreeNode<T> markRoot = builder.build();
			markTreeModel.setRoot( markRoot );
			Set<T> valuesToMute;
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
	private final Operation refreshOperation = this.createActionOperation( "refreshOperation", new Action() {
		@Override
		public Edit perform( UserActivity userActivity, InternalActionOperation source ) throws CancelException {
			ZTreeNode<T> markRoot = (ZTreeNode<T>)markTreeModel.getRoot();
			Set<T> valuesToMute = createValuesToMute( markRoot );

			ZTreeNode.Builder<T> builder = capture();

			ZTreeNode<T> currentRoot;
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

	private final ValueListener<Boolean> isPruningDesiredListener = new ValueListener<Boolean>() {
		@Override
		public void valueChanged( ValueEvent<Boolean> e ) {
			refreshOperation.fire();
		}
	};

	private final BooleanState isPruningDesiredState = this.createBooleanState( "isPruningDesiredState", true );

	private final DefaultTreeModel markTreeModel = new DefaultTreeModel( null );
	private final DefaultTreeModel currentTreeModel = new DefaultTreeModel( null );
}
