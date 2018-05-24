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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.event.NameEvent;
import edu.cmu.cs.dennisc.pattern.event.NameListener;
import org.alice.ide.IDE;
import org.alice.ide.ProjectDocumentFrame;
import org.alice.ide.resource.manager.views.ResourceManagerView;
import org.lgna.common.Resource;
import org.lgna.croquet.LazyOperationUnadornedDialogCoreComposite;
import org.lgna.croquet.Operation;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.project.Project;
import org.lgna.project.event.ResourceEvent;
import org.lgna.project.event.ResourceListener;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class ResourceManagerComposite extends LazyOperationUnadornedDialogCoreComposite<ResourceManagerView> {
	public ResourceManagerComposite( ProjectDocumentFrame projectDocumentFrame ) {
		super( UUID.fromString( "7351e244-fcd7-4b21-9b54-83254fc44db7" ) );
		this.projectDocumentFrame = projectDocumentFrame;
	}

	public ImportAudioResourceOperation getImportAudioResourceOperation() {
		return this.importAudioResourceOperation;
	}

	public ImportImageResourceOperation getImportImageResourceOperation() {
		return this.importImageResourceOperation;
	}

	public ResourceSingleSelectTableRowState getResourcesState() {
		return this.resourcesState;
	}

	public RenameResourceComposite getRenameResourceComposite() {
		return this.renameResourceComposite;
	}

	public Operation getRemoveResourceOperation() {
		return this.removeResourceOperation;
	}

	public Operation getReloadContentOperation() {
		return this.reloadContentOperation;
	}

	@Override
	protected ResourceManagerView createView() {
		return new ResourceManagerView( this );
	}

	private void reloadTableModel( Project project ) {
		this.resourcesState.reloadTableModel( project );
		Collection<Resource> currentResources = this.resourcesState.getItems();
		for( Resource resource : this.previousResources ) {
			if( currentResources.contains( resource ) ) {
				//pass
			} else {
				resource.removeNameListener( this.nameListener );
			}
		}
		for( Resource resource : currentResources ) {
			if( this.previousResources.contains( resource ) ) {
				//pass
			} else {
				resource.addNameListener( this.nameListener );
			}
		}
		this.previousResources = currentResources;
	}

	private Project getProject() {
		//TODO: use this.projectDocumentFrame
		IDE ide = IDE.getActiveInstance();
		Project project;
		if( ide != null ) {
			project = ide.getProject();
		} else {
			project = null;
		}
		return project;
	}

	private void reloadTableModel() {
		this.reloadTableModel( this.getProject() );
	}

	@Override
	public void handlePreActivation() {
		this.projectBeingListenedTo = this.getProject();
		if( this.projectBeingListenedTo != null ) {
			this.projectBeingListenedTo.addResourceListener( this.resourceListener );
		}
		this.reloadTableModel( this.projectBeingListenedTo );
		this.resourcesState.addAndInvokeNewSchoolValueListener( this.rowListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		if( this.projectBeingListenedTo != null ) {
			this.projectBeingListenedTo.removeResourceListener( this.resourceListener );
		}
		this.resourcesState.removeNewSchoolValueListener( this.rowListener );
		for( Resource resource : this.previousResources ) {
			resource.removeNameListener( this.nameListener );
		}
		this.previousResources = Collections.emptyList();
		super.handlePostDeactivation();
	}

	private void handleSelection( Resource nextValue ) {
		boolean isSelected = nextValue != null;
		String renameAndReplaceToolTipText;

		String removeToolTipText;
		boolean isReferenced;
		if( isSelected ) {

			TableModel resourceTableModel = this.resourcesState.getSwingModel().getTableModel();
			ListSelectionModel listSelectionModel = this.resourcesState.getSwingModel().getListSelectionModel();

			isReferenced = (Boolean)resourceTableModel.getValueAt( listSelectionModel.getLeadSelectionIndex(), ResourceSingleSelectTableRowState.IS_REFERENCED_COLUMN_INDEX );
			renameAndReplaceToolTipText = null;
			if( isReferenced ) {
				removeToolTipText = this.findLocalizedText( "referencedToolTip" );
			} else {
				removeToolTipText = null;
			}
		} else {
			isReferenced = false;
			renameAndReplaceToolTipText = this.findLocalizedText( "toolTip" );
			removeToolTipText = renameAndReplaceToolTipText;
		}
		this.renameResourceComposite.getLaunchOperation().setEnabled( isSelected );
		this.renameResourceComposite.getLaunchOperation().setToolTipText( renameAndReplaceToolTipText );
		this.reloadContentOperation.setEnabled( isSelected );
		this.reloadContentOperation.setToolTipText( renameAndReplaceToolTipText );

		this.removeResourceOperation.setEnabled( isSelected && ( isReferenced == false ) );
		this.removeResourceOperation.setToolTipText( removeToolTipText );
	}

	private final ResourceListener resourceListener = new ResourceListener() {
		@Override
		public void resourceAdded( ResourceEvent e ) {
			reloadTableModel();
		}

		@Override
		public void resourceRemoved( ResourceEvent e ) {
			reloadTableModel();
		}
	};

	private final NameListener nameListener = new NameListener() {
		@Override
		public void nameChanging( NameEvent nameEvent ) {
		}

		@Override
		public void nameChanged( NameEvent nameEvent ) {
			Logger.outln( nameEvent );
			getView().repaint();
		}
	};

	private final ValueListener<Resource> rowListener = new ValueListener<Resource>() {
		@Override
		public void valueChanged( ValueEvent<Resource> e ) {
			handleSelection( e.getNextValue() );
		}
	};

	private final ProjectDocumentFrame projectDocumentFrame;
	private final ResourceSingleSelectTableRowState resourcesState = new ResourceSingleSelectTableRowState();
	private final Operation reloadContentOperation = new ReloadContentResourceOperation( this.resourcesState );
	private final Operation removeResourceOperation = new RemoveResourceOperation( this.resourcesState );
	private final RenameResourceComposite renameResourceComposite = new RenameResourceComposite( this.resourcesState );
	private final ImportImageResourceOperation importImageResourceOperation = new ImportImageResourceOperation();
	private final ImportAudioResourceOperation importAudioResourceOperation = new ImportAudioResourceOperation();

	private Collection<Resource> previousResources = Collections.emptyList();
	private Project projectBeingListenedTo;
}
