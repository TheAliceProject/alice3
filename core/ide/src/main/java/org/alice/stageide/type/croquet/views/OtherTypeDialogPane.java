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

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import org.alice.ide.ThemeUtilities;
import org.alice.stageide.type.croquet.OtherTypeDialog;
import org.alice.stageide.type.croquet.TypeNode;
import org.alice.stageide.type.croquet.views.renderers.TypeCellRenderer;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.AbstractLabel;
import org.lgna.croquet.views.FolderTabbedPane;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.Separator;
import org.lgna.croquet.views.Tree;
import org.lgna.croquet.views.VerticalAlignment;
import org.lgna.croquet.views.VerticalScrollBarPaintOmittingWhenAppropriateScrollPane;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import java.awt.Color;
import java.awt.Rectangle;

/**
 * @author Dennis Cosgrove
 */
public class OtherTypeDialogPane extends MigPanel {
	private final Tree<TypeNode> treeView;

	private final AbstractLabel descriptionLabel;
	private final ValueListener<TypeNode> typeListener = new ValueListener<TypeNode>() {
		@Override
		public void valueChanged( ValueEvent<TypeNode> e ) {
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					descriptionLabel.getAwtComponent().scrollRectToVisible( new Rectangle( 0, 0, 1, 1 ) );
					TreePath treePath = treeView.getSelectionPath();
					if( treePath != null ) {
						treeView.scrollPathToVisible( treePath );
					}
				}
			} );
		}
	};

	public OtherTypeDialogPane( OtherTypeDialog composite ) {
		super( composite, "fill", "[grow 0, shrink 0]16[grow 0, shrink 0]4[grow, shrink]", "[grow 0, shrink 0][grow 0, shrink 0][grow, shrink]" );

		this.treeView = composite.getTypeTreeState().createTree();
		this.treeView.setCellRenderer( new TypeCellRenderer() );
		this.addComponent( new Label( "Filtering", TextWeight.BOLD ) );
		this.addComponent( new Label( "Selection", TextWeight.BOLD ) );
		this.addComponent( new Label( "Available Procedures, Functions, and Properties", TextWeight.BOLD ), "wrap" );

		this.addComponent( Separator.createInstanceSeparatingTopFromBottom(), "growx" );
		this.addComponent( Separator.createInstanceSeparatingTopFromBottom(), "growx" );
		this.addComponent( Separator.createInstanceSeparatingTopFromBottom(), "growx, wrap" );

		FolderTabbedPane tabbedPane = composite.getTabState().createFolderTabbedPane();
		this.addComponent( tabbedPane, "grow" );

		ScrollPane treeScrollPane = new VerticalScrollBarPaintOmittingWhenAppropriateScrollPane( this.treeView );
		this.addComponent( treeScrollPane, "grow" );

		this.descriptionLabel = composite.getDescriptionText().createLabel();
		this.descriptionLabel.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.descriptionLabel.setVerticalAlignment( VerticalAlignment.TOP );
		ScrollPane descriptionScrollPane = new ScrollPane( descriptionLabel );
		this.descriptionLabel.setBackgroundColor( Color.WHITE );
		descriptionScrollPane.setBackgroundColor( Color.WHITE );
		this.addComponent( descriptionScrollPane, "grow" );

		Color color = ThemeUtilities.getActiveTheme().getTypeColor();
		color = ColorUtilities.scaleHSB( color, 1.0, 0.9, 1.1 );
		this.setBackgroundColor( color );
		tabbedPane.setBackgroundColor( color );
	}

	@Override
	public OtherTypeDialog getComposite() {
		return (OtherTypeDialog)super.getComposite();
	}

	@Override
	public void handleCompositePreActivation() {
		OtherTypeDialog composite = this.getComposite();
		composite.getTypeTreeState().addNewSchoolValueListener( this.typeListener );
		this.treeView.expandAllRows();
		super.handleCompositePreActivation();
	}

	@Override
	public void handleCompositePostDeactivation() {
		OtherTypeDialog composite = this.getComposite();
		composite.getTypeTreeState().removeNewSchoolValueListener( this.typeListener );
		super.handleCompositePostDeactivation();
	}
}
