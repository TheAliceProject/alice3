/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.type.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class OtherTypeDialogPane extends org.lgna.croquet.components.MigPanel {
	private final org.lgna.croquet.components.Tree<org.alice.stageide.type.croquet.TypeNode> treeView;

	private final org.lgna.croquet.components.AbstractLabel descriptionLabel;
	private final org.lgna.croquet.event.ValueListener<org.alice.stageide.type.croquet.TypeNode> typeListener = new org.lgna.croquet.event.ValueListener<org.alice.stageide.type.croquet.TypeNode>() {
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.stageide.type.croquet.TypeNode> e ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					descriptionLabel.getAwtComponent().scrollRectToVisible( new java.awt.Rectangle( 0, 0, 1, 1 ) );
				}
			} );
		}
	};

	public OtherTypeDialogPane( org.alice.stageide.type.croquet.OtherTypeDialog composite ) {
		super( composite, "fill", "[grow 0, shrink 0][grow 0, shrink 0][grow, shrink]", "[grow 0, shrink 0][grow, shrink]" );

		org.lgna.croquet.ListSelectionState<org.alice.stageide.type.croquet.SelectionStyle> selectionStyleState = composite.getSelectionStyleState();

		this.treeView = composite.getTypeTreeState().createTree();
		this.treeView.setCellRenderer( new org.alice.stageide.type.croquet.views.renderers.TypeCellRenderer() );
		this.addComponent( selectionStyleState.getItemSelectedState( org.alice.stageide.type.croquet.SelectionStyle.DIRECT ).createToggleButton() );
		this.addComponent( selectionStyleState.getItemSelectedState( org.alice.stageide.type.croquet.SelectionStyle.LOWEST_COMMON_ANCESTOR ).createToggleButton() );

		this.descriptionLabel = composite.getDescriptionText().createLabel();
		this.descriptionLabel.setVerticalAlignment( org.lgna.croquet.components.VerticalAlignment.TOP );
		this.addComponent( new org.lgna.croquet.components.ScrollPane( descriptionLabel ), "spany 2, grow, wrap" );

		org.lgna.croquet.components.ScrollPane treeScrollPane = new org.lgna.croquet.components.ScrollPane( this.treeView ) {
			@Override
			protected edu.cmu.cs.dennisc.javax.swing.components.JScrollPaneCoveringLinuxPaintBug createJScrollPane() {
				return new edu.cmu.cs.dennisc.javax.swing.components.VerticalScrollBarPaintOmittingWhenAppropriateJScrollPane();
			}
		};
		treeScrollPane.setVerticalScrollbarPolicy( org.lgna.croquet.components.ScrollPane.VerticalScrollbarPolicy.ALWAYS );
		this.addComponent( treeScrollPane, "grow" );

		org.lgna.croquet.views.MultipleSelectionListView<org.lgna.project.ast.UserField> listView = composite.getSceneFieldsState().createMultipleSelectionListView();
		listView.setCellRenderer( new org.alice.stageide.type.croquet.views.renderers.FieldCellRenderer( composite.getTypeTreeState() ) );
		//listView.getAwtComponent().setEnabled( false );
		this.addComponent( listView, "grow" );
	}

	@Override
	public org.alice.stageide.type.croquet.OtherTypeDialog getComposite() {
		return (org.alice.stageide.type.croquet.OtherTypeDialog)super.getComposite();
	}

	@Override
	public void handleCompositePreActivation() {
		org.alice.stageide.type.croquet.OtherTypeDialog composite = this.getComposite();
		composite.getTypeTreeState().addNewSchoolValueListener( this.typeListener );
		this.treeView.expandAllRows();
		super.handleCompositePreActivation();
	}

	@Override
	public void handleCompositePostDeactivation() {
		org.alice.stageide.type.croquet.OtherTypeDialog composite = this.getComposite();
		composite.getTypeTreeState().removeNewSchoolValueListener( this.typeListener );
		super.handleCompositePostDeactivation();
	}
}
