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

package org.lgna.croquet.components;

import org.lgna.croquet.*;

/*package-private*/ class DirectoryControl<T> extends BorderPanel {
	public static <T> DirectoryControl<T> getInstance( TreeSelectionState<T> treeSelectionState, T treeNode ) {
		return new DirectoryControl<T>( treeSelectionState, treeNode );
	}

	private DirectoryControl( TreeSelectionState<T> treeSelectionState, T treeNode ) {
		//PopupButton selectChildButton = SelectChildDirectoryMenuModel.getInstance( treeSelectionState, treeNode, initializer ).getPopupPrepModel().createPopupButton();
		PopupButton selectChildButton = treeSelectionState.getCascadeFor( treeNode ).getRoot().getPopupPrepModel().createPopupButton();
		if( javax.swing.UIManager.getLookAndFeel().getName().contains( "Nimbus" ) ) {
			selectChildButton.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 2, 0, 2 ) );
		} else {
			selectChildButton.setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.GRAY ) );
		}
		ActionOperation operation = treeSelectionState.getSelectionOperationFor( treeNode );
		//initializer.configure( operation, treeNode );
		Button button = operation.createButton();
		this.addComponent( button, Constraint.CENTER );
		this.addComponent( selectChildButton, Constraint.LINE_END );
	}

	@Override
	protected javax.swing.JPanel createJPanel() {
		return new DefaultJPanel() {
			@Override
			public java.awt.Dimension getMaximumSize() {
				return super.getPreferredSize();
			}
		};
	}
}
/**
 * @author Dennis Cosgrove
 */
public class PathControl<T> extends ViewController< javax.swing.JComponent, TreeSelectionState<T> > {
	private javax.swing.tree.TreeSelectionModel treeSelectionModel;
	private javax.swing.event.TreeSelectionListener treeSelectionListener = new javax.swing.event.TreeSelectionListener() {
		public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
			PathControl.this.refresh();
		}
	};
	public PathControl( TreeSelectionState<T> model ) {
		super( model );
		this.setSwingTreeSelectionModel( model.getSwingModel().getTreeSelectionModel() );
	}

	private void setSwingTreeSelectionModel( javax.swing.tree.TreeSelectionModel treeSelectionModel ) {
		if( this.treeSelectionModel != null ) {
			this.treeSelectionModel.removeTreeSelectionListener( this.treeSelectionListener );
		}
		this.treeSelectionModel = treeSelectionModel;
		this.refresh();
		if( this.treeSelectionModel != null ) {
			this.treeSelectionModel.addTreeSelectionListener( this.treeSelectionListener );
		}
	}

	private void refresh() {
		this.internalRemoveAllComponents();
		edu.cmu.cs.dennisc.javax.swing.models.TreeModel< T > treeModel = this.getModel().getTreeModel();
		javax.swing.tree.TreePath treePath = this.treeSelectionModel.getSelectionPath();
		if( treePath != null ) {
			final int N = treePath.getPathCount();
			for( int i=0; i<N; i++ ) {
				T treeNode = (T)treePath.getPathComponent( i );
				if( treeModel.isLeaf( treeNode ) ) {
					//pass
				} else {
					this.internalAddComponent( DirectoryControl.getInstance( this.getModel(), treeNode ) );
				}
			}
		}
		this.revalidateAndRepaint();
	}
	@Override
	protected javax.swing.JComponent createAwtComponent() {
		javax.swing.JPanel rv = new javax.swing.JPanel();
		rv.setLayout( new javax.swing.BoxLayout( rv, javax.swing.BoxLayout.LINE_AXIS ) );
		rv.setBackground( null );
		return rv;
	}
}
