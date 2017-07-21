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
package org.alice.stageide.type.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class OtherTypeDialogPane extends org.lgna.croquet.views.MigPanel {
	private final org.lgna.croquet.views.Tree<org.alice.stageide.type.croquet.TypeNode> treeView;

	private final org.lgna.croquet.views.AbstractLabel descriptionLabel;
	private final org.lgna.croquet.event.ValueListener<org.alice.stageide.type.croquet.TypeNode> typeListener = new org.lgna.croquet.event.ValueListener<org.alice.stageide.type.croquet.TypeNode>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.stageide.type.croquet.TypeNode> e ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					descriptionLabel.getAwtComponent().scrollRectToVisible( new java.awt.Rectangle( 0, 0, 1, 1 ) );
					javax.swing.tree.TreePath treePath = treeView.getSelectionPath();
					if( treePath != null ) {
						treeView.scrollPathToVisible( treePath );
					}
				}
			} );
		}
	};

	public OtherTypeDialogPane( org.alice.stageide.type.croquet.OtherTypeDialog composite ) {
		super( composite, "fill", "[grow 0, shrink 0]16[grow 0, shrink 0]4[grow, shrink]", "[grow 0, shrink 0][grow 0, shrink 0][grow, shrink]" );

		this.treeView = composite.getTypeTreeState().createTree();
		this.treeView.setCellRenderer( new org.alice.stageide.type.croquet.views.renderers.TypeCellRenderer() );
		this.addComponent( new org.lgna.croquet.views.Label( "Filtering", edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ) );
		this.addComponent( new org.lgna.croquet.views.Label( "Selection", edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ) );
		this.addComponent( new org.lgna.croquet.views.Label( "Available Procedures, Functions, and Properties", edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ), "wrap" );

		this.addComponent( org.lgna.croquet.views.Separator.createInstanceSeparatingTopFromBottom(), "growx" );
		this.addComponent( org.lgna.croquet.views.Separator.createInstanceSeparatingTopFromBottom(), "growx" );
		this.addComponent( org.lgna.croquet.views.Separator.createInstanceSeparatingTopFromBottom(), "growx, wrap" );

		org.lgna.croquet.views.FolderTabbedPane tabbedPane = composite.getTabState().createFolderTabbedPane();
		this.addComponent( tabbedPane, "grow" );

		org.lgna.croquet.views.ScrollPane treeScrollPane = new org.lgna.croquet.views.VerticalScrollBarPaintOmittingWhenAppropriateScrollPane( this.treeView );
		this.addComponent( treeScrollPane, "grow" );

		this.descriptionLabel = composite.getDescriptionText().createLabel();
		this.descriptionLabel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.descriptionLabel.setVerticalAlignment( org.lgna.croquet.views.VerticalAlignment.TOP );
		org.lgna.croquet.views.ScrollPane descriptionScrollPane = new org.lgna.croquet.views.ScrollPane( descriptionLabel );
		this.descriptionLabel.setBackgroundColor( java.awt.Color.WHITE );
		descriptionScrollPane.setBackgroundColor( java.awt.Color.WHITE );
		this.addComponent( descriptionScrollPane, "grow" );

		java.awt.Color color = org.alice.ide.ThemeUtilities.getActiveTheme().getTypeColor();
		color = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 0.9, 1.1 );
		this.setBackgroundColor( color );
		tabbedPane.setBackgroundColor( color );
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
