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

/*package-private*/ class SelectChildDirectoryMenuModel<T> extends org.lgna.croquet.PredeterminedMenuModel {
	public static <T> SelectChildDirectoryMenuModel<T> getInstance( TreeSelectionState<T> treeSelectionState, T treeNode, PathControl.Initializer initializer ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: SelectChildDirectoryPopupMenuOperation.getInstance()" );
		return new SelectChildDirectoryMenuModel<T>(treeSelectionState, treeNode, initializer);
	}

	private static final int ARROW_SIZE = 10;
	private static final int ARROW_BORDER_HALF_SIZE = 3;
	private static <T> java.util.List< StandardMenuItemPrepModel > createModels( TreeSelectionState<T> treeSelectionState, T treeNode, PathControl.Initializer<T> initializer ) {
		edu.cmu.cs.dennisc.javax.swing.models.TreeModel< T > treeModel = treeSelectionState.getTreeModel();
		final int N = treeModel.getChildCount( treeNode );
		java.util.List< StandardMenuItemPrepModel > list = edu.cmu.cs.dennisc.java.util.Collections.newArrayListWithMinimumCapacity( N );
		for( int i=0; i<N; i++ ) {
			T child = treeModel.getChild( treeNode, i );
			if( treeModel.isLeaf( child ) ) {
				Operation<?> leafOperation = initializer.getOperationForLeaf( child );
				if( leafOperation != null ) {
					list.add( leafOperation.getMenuItemPrepModel() );
				}
			} else {
				ActionOperation operation = treeSelectionState.getSelectionOperationFor( child );
				initializer.configure( operation, child );
				list.add( operation.getMenuItemPrepModel() );
			}
		}
		return list;
	}
	private SelectChildDirectoryMenuModel( TreeSelectionState<T> treeSelectionState, T treeNode, PathControl.Initializer<T> initializer ) {
		super( java.util.UUID.fromString( "cc6a0de7-91b1-4a2b-86ff-21ca9de14bed" ), createModels( treeSelectionState, treeNode, initializer ) );
		javax.swing.Icon icon = new javax.swing.Icon() {
			public int getIconHeight() {
				return ARROW_SIZE + ARROW_BORDER_HALF_SIZE + ARROW_BORDER_HALF_SIZE;
			}

			public int getIconWidth() {
				return ARROW_SIZE + ARROW_BORDER_HALF_SIZE + ARROW_BORDER_HALF_SIZE;
			}

			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				if( c instanceof javax.swing.AbstractButton ) {
					javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading heading;
					if( button.getModel().isPressed() ) {
						heading = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.SOUTH;
					} else {
						heading = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.EAST;
					}
					g.setColor( java.awt.Color.BLACK );
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle( g, heading, x + ARROW_BORDER_HALF_SIZE, y + ARROW_BORDER_HALF_SIZE, ARROW_SIZE, ARROW_SIZE );
				}
			}
		};
		this.setSmallIcon( icon );
	}
}

/*package-private*/ class DirectoryControl<T> extends BorderPanel {
	public static <T> DirectoryControl<T> getInstance( TreeSelectionState<T> treeSelectionState, T treeNode, PathControl.Initializer<T> initializer ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: DirectoryControl.getInstance()" );
		return new DirectoryControl<T>( treeSelectionState, treeNode, initializer );
	}

	private DirectoryControl( TreeSelectionState<T> treeSelectionState, T treeNode, PathControl.Initializer<T> initializer ) {
		PopupButton selectChildButton = SelectChildDirectoryMenuModel.getInstance( treeSelectionState, treeNode, initializer ).getPopupPrepModel().createPopupButton();
		if( javax.swing.UIManager.getLookAndFeel().getName().contains( "Nimbus" ) ) {
			selectChildButton.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 2, 0, 2 ) );
		} else {
			selectChildButton.setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.GRAY ) );
		}
		ActionOperation operation = treeSelectionState.getSelectionOperationFor( treeNode );
		initializer.configure( operation, treeNode );
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
	
	//todo: better name
	public interface Initializer<T> {
		public ActionOperation configure( ActionOperation rv, T treeNode );
		public Operation<?> getOperationForLeaf( T treeNode );
	}

	//private java.util.Map< edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>, DirectoryControl > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
	private final Initializer<T> initializer;
	public PathControl( TreeSelectionState<T> model, Initializer<T> initializer ) {
		super( model );
		this.initializer = initializer;
		this.setSwingTreeSelectionModel( model.getTreeSelectionModel() );
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
					this.internalAddComponent( DirectoryControl.getInstance( this.getModel(), treeNode, this.initializer) );
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
