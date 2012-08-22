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
public final class ResourceManagerComposite extends org.lgna.croquet.PlainDialogOperationComposite<org.alice.ide.resource.manager.views.ResourceManagerView> {
	private static class SingletonHolder {
		private static ResourceManagerComposite instance = new ResourceManagerComposite();
	}

	public static ResourceManagerComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final org.lgna.project.event.ResourceListener resourceListener = new org.lgna.project.event.ResourceListener() {
		public void resourceAdded( org.lgna.project.event.ResourceEvent e ) {
			reloadTableModel();
		}

		public void resourceRemoved( org.lgna.project.event.ResourceEvent e ) {
			reloadTableModel();
		}
	};

	private final org.lgna.croquet.State.ValueListener<org.lgna.common.Resource> rowListener = new org.lgna.croquet.State.ValueListener<org.lgna.common.Resource>() {
		public void changing( org.lgna.croquet.State<org.lgna.common.Resource> state, org.lgna.common.Resource prevValue, org.lgna.common.Resource nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<org.lgna.common.Resource> state, org.lgna.common.Resource prevValue, org.lgna.common.Resource nextValue, boolean isAdjusting ) {
			handleSelection( nextValue );
		}
	};

	private ResourceManagerComposite() {
		super( java.util.UUID.fromString( "7351e244-fcd7-4b21-9b54-83254fc44db7" ), org.lgna.croquet.Application.DOCUMENT_UI_GROUP );
	}

	public ResourceTableRowSelectionState getResourceState() {
		return ResourceTableRowSelectionState.getInstance();
	}

	@Override
	protected org.alice.ide.resource.manager.views.ResourceManagerView createView() {
		return new org.alice.ide.resource.manager.views.ResourceManagerView( this );
	}

	private void reloadTableModel( org.lgna.project.Project project ) {
		this.getResourceState().reloadTableModel( project );
	}

	private void reloadTableModel() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.Project project;
		if( ide != null ) {
			project = ide.getUpToDateProject();
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
			project = ide.getUpToDateProject();
			if( project != null ) {
				project.addResourceListener( this.resourceListener );
			}
		} else {
			project = null;
		}
		this.reloadTableModel( project );
		this.getResourceState().addAndInvokeValueListener( this.rowListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide != null ) {
			org.lgna.project.Project project = ide.getUpToDateProject();
			if( project != null ) {
				project.removeResourceListener( this.resourceListener );
			}
		}
		this.getResourceState().removeValueListener( this.rowListener );
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

			isReferenced = (Boolean)resourceTableModel.getValueAt( listSelectionModel.getLeadSelectionIndex(), ResourceTableRowSelectionState.IS_REFERENCED_COLUMN_INDEX );
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
		RenameResourceComposite.getInstance().getOperation().setEnabled( isSelected );
		RenameResourceComposite.getInstance().getOperation().setToolTipText( renameAndReplaceToolTipText );
		ReloadContentResourceOperation.getInstance().setEnabled( isSelected );
		ReloadContentResourceOperation.getInstance().setToolTipText( renameAndReplaceToolTipText );

		RemoveResourceOperation.getInstance().setEnabled( isSelected && ( isReferenced == false ) );
		RemoveResourceOperation.getInstance().setToolTipText( removeToolTipText );
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		ResourceManagerComposite.getInstance().getOperation().fire();
		System.exit( 0 );
	}
}
