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

package org.alice.stageide.typecontext.components;

class SelectedTypeView extends org.lgna.croquet.components.BorderPanel {
	private final org.lgna.croquet.components.Label label = new org.lgna.croquet.components.Label( "selected type:" );
	private final org.lgna.croquet.components.Label typeLabel = new org.lgna.croquet.components.Label();
	private final org.lgna.croquet.components.Label snapshotLabel = new org.lgna.croquet.components.Label();
	private final org.lgna.croquet.meta.event.MetaStateValueListener<org.lgna.project.ast.NamedUserType> typeListener = new org.lgna.croquet.meta.event.MetaStateValueListener<org.lgna.project.ast.NamedUserType>() {
		public void metaStateValueChanged( org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue ) {
			SelectedTypeView.this.handleTypeStateChanged( nextValue );
		}
	};

	public SelectedTypeView() {
		//this.typeLabel.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.CENTER );
		this.snapshotLabel.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.CENTER );
		this.addPageStartComponent( new org.lgna.croquet.components.LineAxisPanel( this.label, this.typeLabel ) );
		this.addCenterComponent( this.snapshotLabel );
	}

	private void handleTypeStateChanged( org.lgna.project.ast.NamedUserType nextValue ) {
		this.typeLabel.setIcon( org.alice.ide.common.TypeIcon.getInstance( nextValue ) );
		String snapshotText = null;
		javax.swing.Icon snapshotIcon = null;
		if( nextValue != null ) {
			org.lgna.croquet.icon.IconFactory iconFactory = org.alice.stageide.icons.IconFactoryManager.getIconFactoryForType( nextValue );
			if( iconFactory != null ) {
				snapshotIcon = iconFactory.getIcon( iconFactory.getDefaultSize( org.alice.ide.Theme.DEFAULT_LARGE_ICON_SIZE ) );
			}
		}
		this.snapshotLabel.setText( snapshotText );
		this.snapshotLabel.setIcon( snapshotIcon );
		this.revalidateAndRepaint();
	}

	@Override
	protected void handleAddedTo( org.lgna.croquet.components.Component<?> parent ) {
		super.handleAddedTo( parent );
		org.alice.ide.declarationseditor.DeclarationsEditorComposite.getInstance().getMetaState().addAndInvokeMetaStateValueListener( this.typeListener, null );
	}

	@Override
	protected void handleRemovedFrom( org.lgna.croquet.components.Component<?> parent ) {
		org.alice.ide.declarationseditor.DeclarationsEditorComposite.getInstance().getMetaState().removeMetaStateValueListener( this.typeListener );
		super.handleRemovedFrom( parent );
	}
}

class ReturnToSceneTypeButton extends org.lgna.croquet.components.Button {
	private static javax.swing.Icon BACK_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( NonSceneTypeView.class.getResource( "images/24/back.png" ) );
	private final org.lgna.croquet.components.Label thumbnailLabel = new org.lgna.croquet.components.Label(
			new edu.cmu.cs.dennisc.javax.swing.icons.AlphaIcon( org.alice.stageide.icons.SceneIconFactory.getInstance().getIcon( org.alice.ide.Theme.DEFAULT_SMALL_ICON_SIZE ), 0.5f )
			);
	private final org.lgna.croquet.components.Label typeIconLabel = new org.lgna.croquet.components.Label();

	public ReturnToSceneTypeButton( org.lgna.croquet.Operation operation ) {
		super( operation );
		javax.swing.JButton jButton = this.getAwtComponent();
		jButton.setLayout( new javax.swing.BoxLayout( jButton, javax.swing.BoxLayout.LINE_AXIS ) );
		this.internalAddComponent( new org.lgna.croquet.components.Label( BACK_ICON ) );
		this.internalAddComponent( new org.lgna.croquet.components.Label( "back to:" ) );
		this.internalAddComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 8 ) );
		this.internalAddComponent( this.thumbnailLabel );
		this.internalAddComponent( this.typeIconLabel );
	}

	@Override
	protected void handleHierarchyChanged( java.awt.event.HierarchyEvent e ) {
		super.handleHierarchyChanged( e );
		org.alice.stageide.icons.SceneIconFactory.getInstance().markAllIconsDirty();

		//todo:
		org.lgna.project.ast.NamedUserType sceneType = org.alice.ide.IDE.getActiveInstance().getSceneType();
		this.typeIconLabel.setIcon( org.alice.ide.common.TypeIcon.getInstance( sceneType ) );
		this.revalidateAndRepaint();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class NonSceneTypeView extends org.lgna.croquet.components.CornerSpringPanel {
	private final org.lgna.croquet.State.ValueListener<Boolean> isEmphasizingClassesListener = new org.lgna.croquet.State.ValueListener<Boolean>() {
		public void changing( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			if( isAdjusting ) {
				//pass
			} else {
				NonSceneTypeView.this.handleEmphasizingClassesChanged( nextValue );
			}
		}
	};
	//private final ReturnToSceneTypeButton returnToSceneTypeButton = new ReturnToSceneTypeButton( org.alice.stageide.typecontext.SelectSceneTypeOperation.getInstance() );

	private final ReturnToSceneTypeButton returnToSceneTypeButton;

	public NonSceneTypeView( org.alice.stageide.typecontext.NonSceneTypeComposite composite ) {
		super( composite );
		this.setNorthWestComponent( new SelectedTypeView() );
		this.setNorthEastComponent( org.alice.stageide.run.RunComposite.getInstance().getOperation().createButton() );

		org.lgna.croquet.Operation returnToSceneTypeOperation = composite.getSelectSceneTypeOperation();
		returnToSceneTypeOperation.initializeIfNecessary();
		returnToSceneTypeOperation.setName( "" );
		this.returnToSceneTypeButton = new ReturnToSceneTypeButton( returnToSceneTypeOperation );
	}

	@Override
	protected javax.swing.JPanel createJPanel() {
		return new DefaultJPanel() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return new java.awt.Dimension( 320, 240 );
			}
		};
	}

	private final void handleEmphasizingClassesChanged( boolean nextValue ) {
		this.setSouthWestComponent( nextValue ? returnToSceneTypeButton : null );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().addAndInvokeValueListener( this.isEmphasizingClassesListener );
	}

	@Override
	protected void handleUndisplayable() {
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().removeValueListener( this.isEmphasizingClassesListener );
		super.handleUndisplayable();
	}
}
