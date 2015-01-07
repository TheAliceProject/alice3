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

package org.alice.ide.resource.manager;

/**
 * @author Dennis Cosgrove
 */
public final class ResourceManagerComposite extends org.lgna.croquet.LazyOperationUnadornedDialogCoreComposite<org.alice.ide.resource.manager.views.ResourceManagerView> {
	public ResourceManagerComposite() {
		super( java.util.UUID.fromString( "7351e244-fcd7-4b21-9b54-83254fc44db7" ) );
	}

	public ResourceSingleSelectTableRowState getResourceState() {
		return ResourceSingleSelectTableRowState.getInstance();
	}

	@Override
	protected org.alice.ide.resource.manager.views.ResourceManagerView createView() {
		return new org.alice.ide.resource.manager.views.ResourceManagerView( this );
	}

	private void reloadTableModel( org.lgna.project.Project project ) {
		this.getResourceState().reloadTableModel( project );
		java.util.Collection<org.lgna.common.Resource> currentResources = this.getResourceState().getItems();
		for( org.lgna.common.Resource resource : this.previousResources ) {
			if( currentResources.contains( resource ) ) {
				//pass
			} else {
				resource.removeNameListener( this.nameListener );
			}
		}
		for( org.lgna.common.Resource resource : currentResources ) {
			if( this.previousResources.contains( resource ) ) {
				//pass
			} else {
				resource.addNameListener( this.nameListener );
			}
		}
		this.previousResources = currentResources;
	}

	private void reloadTableModel() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.Project project;
		if( ide != null ) {
			project = ide.getProject();
		} else {
			project = null;
		}
		this.reloadTableModel( project );
	}

	@Override
	public void handlePreActivation() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.Project project;
		if( ide != null ) {
			project = ide.getProject();
			if( project != null ) {
				project.addResourceListener( this.resourceListener );
			}
		} else {
			project = null;
		}
		this.reloadTableModel( project );
		this.getResourceState().addAndInvokeNewSchoolValueListener( this.rowListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide != null ) {
			org.lgna.project.Project project = ide.getProject();
			if( project != null ) {
				project.removeResourceListener( this.resourceListener );
			}
		}
		this.getResourceState().removeNewSchoolValueListener( this.rowListener );
		for( org.lgna.common.Resource resource : this.previousResources ) {
			resource.removeNameListener( this.nameListener );
		}
		this.previousResources = java.util.Collections.emptyList();
		super.handlePostDeactivation();
	}

	private void handleSelection( org.lgna.common.Resource nextValue ) {
		boolean isSelected = nextValue != null;
		String renameAndReplaceToolTipText;

		String removeToolTipText;
		boolean isReferenced;
		if( isSelected ) {

			javax.swing.table.TableModel resourceTableModel = this.getResourceState().getSwingModel().getTableModel();
			javax.swing.ListSelectionModel listSelectionModel = this.getResourceState().getSwingModel().getListSelectionModel();

			isReferenced = (Boolean)resourceTableModel.getValueAt( listSelectionModel.getLeadSelectionIndex(), ResourceSingleSelectTableRowState.IS_REFERENCED_COLUMN_INDEX );
			renameAndReplaceToolTipText = null;
			if( isReferenced ) {
				removeToolTipText = null;
			} else {
				removeToolTipText = "cannot remove resources that are referenced";
			}
		} else {
			isReferenced = false;
			renameAndReplaceToolTipText = "select resource";
			removeToolTipText = renameAndReplaceToolTipText;
		}
		RenameResourceComposite.getInstance().getLaunchOperation().setEnabled( isSelected );
		RenameResourceComposite.getInstance().getLaunchOperation().setToolTipText( renameAndReplaceToolTipText );
		ReloadContentResourceOperation.getInstance().setEnabled( isSelected );
		ReloadContentResourceOperation.getInstance().setToolTipText( renameAndReplaceToolTipText );

		RemoveResourceOperation.getInstance().setEnabled( isSelected && ( isReferenced == false ) );
		RemoveResourceOperation.getInstance().setToolTipText( removeToolTipText );
	}

	private final org.lgna.project.event.ResourceListener resourceListener = new org.lgna.project.event.ResourceListener() {
		@Override
		public void resourceAdded( org.lgna.project.event.ResourceEvent e ) {
			reloadTableModel();
		}

		@Override
		public void resourceRemoved( org.lgna.project.event.ResourceEvent e ) {
			reloadTableModel();
		}
	};

	private final edu.cmu.cs.dennisc.pattern.event.NameListener nameListener = new edu.cmu.cs.dennisc.pattern.event.NameListener() {
		@Override
		public void nameChanging( edu.cmu.cs.dennisc.pattern.event.NameEvent nameEvent ) {
		}

		@Override
		public void nameChanged( edu.cmu.cs.dennisc.pattern.event.NameEvent nameEvent ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( nameEvent );
			getView().repaint();
		}
	};

	private final org.lgna.croquet.event.ValueListener<org.lgna.common.Resource> rowListener = new org.lgna.croquet.event.ValueListener<org.lgna.common.Resource>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.common.Resource> e ) {
			handleSelection( e.getNextValue() );
		}
	};

	private java.util.Collection<org.lgna.common.Resource> previousResources = java.util.Collections.emptyList();
}
