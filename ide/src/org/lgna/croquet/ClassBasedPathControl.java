/**
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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
package org.lgna.croquet;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.BorderPanel.Constraint;
import org.lgna.croquet.components.Button;
import org.lgna.croquet.components.PopupButton;
import org.lgna.croquet.components.ViewController;
import org.lgna.project.ast.NamedUserType;


/**
 * @author dculyba
 *
 */
/*package-private*/ class SelectChildClassMenuModel extends org.lgna.croquet.PredeterminedMenuModel {
public static SelectChildClassMenuModel getInstance( org.lgna.croquet.TreeSelectionState<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType>> treeSelectionState, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType> treeNode, ClassBasedPathControl.Initializer initializer ) {
	edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: SelectChildClassPopupMenuOperation.getInstance()" );
	return new SelectChildClassMenuModel(treeSelectionState, treeNode, initializer);
}

private static final int ARROW_SIZE = 10;
private static final int ARROW_BORDER_HALF_SIZE = 3;
private static java.util.List< org.lgna.croquet.StandardMenuItemPrepModel > createModels( org.lgna.croquet.TreeSelectionState<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType>> treeSelectionState, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType> treeNode, ClassBasedPathControl.Initializer initializer ) {
	java.util.List< org.lgna.croquet.StandardMenuItemPrepModel > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	java.util.Enumeration< edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType> > enumeration = treeNode.children();
	if( enumeration != null ) {
		while( enumeration.hasMoreElements() ) {
			edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType> child = enumeration.nextElement();
			if( !child.isLeaf()) {
				list.add( SelectClassActionOperation.getInstance(treeSelectionState, child, initializer ).getMenuItemPrepModel() );
			} else {
				org.lgna.croquet.Operation<?> leafOperation = initializer.getOperationForLeaf( child );
				if( leafOperation != null ) {
					list.add( leafOperation.getMenuItemPrepModel() );
				}
			}
		}
	}
	return list;
}
private SelectChildClassMenuModel( org.lgna.croquet.TreeSelectionState<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType>> treeSelectionState, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType> treeNode, ClassBasedPathControl.Initializer initializer ) {
	super( java.util.UUID.fromString( "2f8ea1ea-ba5f-4ad0-81f3-77664659d1af" ), createModels( treeSelectionState, treeNode, initializer ) );
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

/*package-private*/ class ClassBasedDirectoryControl extends BorderPanel {
public static ClassBasedDirectoryControl getInstance( TreeSelectionState<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType>> treeSelectionState, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType> treeNode, ClassBasedPathControl.Initializer initializer ) {
	edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: DirectoryControl.getInstance()" );
	return new ClassBasedDirectoryControl( treeSelectionState, treeNode, initializer );
}

private ClassBasedDirectoryControl( TreeSelectionState<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType>> treeSelectionState, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType> treeNode, ClassBasedPathControl.Initializer initializer ) {
	PopupButton selectChildButton = SelectChildClassMenuModel.getInstance( treeSelectionState, treeNode, initializer ).getPopupPrepModel().createPopupButton();
	if( javax.swing.UIManager.getLookAndFeel().getName().contains( "Nimbus" ) ) {
			selectChildButton.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 2, 0, 2 ) );
		} else {
			selectChildButton.setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.GRAY ) );
		}
		this.addComponent( SelectClassActionOperation.getInstance( treeSelectionState, treeNode, initializer ).createButton(), Constraint.CENTER );
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
public class ClassBasedPathControl extends ViewController< javax.swing.JComponent, TreeSelectionState<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType>> > {
	private javax.swing.tree.TreeSelectionModel treeSelectionModel;
	private javax.swing.event.TreeSelectionListener treeSelectionListener = new javax.swing.event.TreeSelectionListener() {
		public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
			ClassBasedPathControl.this.refresh();
		}
	};
	
	//todo: better name
	public interface Initializer {
		public ActionOperation configure( ActionOperation rv, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType> treeNode );
		public Operation<?> getOperationForLeaf( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType> treeNode );
	}
	
	//private java.util.Map< edu.cmu.cs.dennisc.javax.swing.models.TreeNode<Class>, DirectoryControl > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
	private Initializer initializer;
	/*package-private*/ ClassBasedPathControl( TreeSelectionState<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType>> model, Initializer initializer ) {
		super( model );
		this.initializer = initializer;
	}
	
	private void refresh() {
		this.internalRemoveAllComponents();
		javax.swing.tree.TreePath treePath = this.treeSelectionModel.getSelectionPath();
		if( treePath != null ) {
			final int N = treePath.getPathCount();
			for( int i=0; i<N; i++ ) {
				edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType> treeNode = (edu.cmu.cs.dennisc.javax.swing.models.TreeNode<NamedUserType>)treePath.getPathComponent( i );
				if( !treeNode.isLeaf()) {
					this.internalAddComponent( ClassBasedDirectoryControl.getInstance( this.getModel(), treeNode, this.initializer) );
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
	
	/*package-private*/ void setSwingTreeModel( javax.swing.tree.TreeModel treeModel ) {
		//todo
	}
	/*package-private*/ void setSwingTreeSelectionModel( javax.swing.tree.TreeSelectionModel treeSelectionModel ) {
		if( this.treeSelectionModel != null ) {
			this.treeSelectionModel.removeTreeSelectionListener( this.treeSelectionListener );
		}
		this.treeSelectionModel = treeSelectionModel;
		this.refresh();
		if( this.treeSelectionModel != null ) {
			this.treeSelectionModel.addTreeSelectionListener( this.treeSelectionListener );
		}
	}
}
