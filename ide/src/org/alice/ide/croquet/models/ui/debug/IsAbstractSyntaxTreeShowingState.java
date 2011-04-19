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
package org.alice.ide.croquet.models.ui.debug;

import org.alice.ide.croquet.models.IsFrameShowingState;

class ASTModel extends edu.cmu.cs.dennisc.javax.swing.models.AbstractMutableTreeModel< Object > {
	public Object getRoot() {
		return org.alice.ide.IDE.getSingleton().getProject();
	}
	public boolean isLeaf( Object node ) {
		if( node instanceof edu.cmu.cs.dennisc.property.Property<?> ) {
			return node instanceof edu.cmu.cs.dennisc.property.ListProperty<?> == false;
		} else {
			return false;
		}
	}
	public int getChildCount(Object parent) {
		if( parent instanceof edu.cmu.cs.dennisc.alice.Project ) {
			return org.alice.ide.IDE.getSingleton().getTypesDeclaredInAlice().size();
		} else if( parent instanceof edu.cmu.cs.dennisc.alice.ast.AbstractNode ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractNode node = (edu.cmu.cs.dennisc.alice.ast.AbstractNode)parent;
			return node.getProperties().size();
		} else if( parent instanceof edu.cmu.cs.dennisc.property.ListProperty<?> ) {
			edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)parent;
			return listProperty.size();
		} else {
			return 0;
		}
	}
	public Object getChild(Object parent, int index) {
		if( parent instanceof edu.cmu.cs.dennisc.alice.Project ) {
			//edu.cmu.cs.dennisc.alice.Project project = (edu.cmu.cs.dennisc.alice.Project)parent;
			return org.alice.ide.IDE.getSingleton().getTypesDeclaredInAlice().get( index );
		} else if( parent instanceof edu.cmu.cs.dennisc.alice.ast.AbstractNode ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractNode node = (edu.cmu.cs.dennisc.alice.ast.AbstractNode)parent;
			return node.getProperties().get( index );
		} else if( parent instanceof edu.cmu.cs.dennisc.property.ListProperty<?> ) {
			edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)parent;
			return listProperty.get( index );
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	public int getIndexOfChild( Object parent, Object child ) {
		if( parent instanceof edu.cmu.cs.dennisc.alice.Project ) {
			edu.cmu.cs.dennisc.alice.Project project = (edu.cmu.cs.dennisc.alice.Project)parent;
			return 0;
		} else if( parent instanceof edu.cmu.cs.dennisc.alice.ast.AbstractNode ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractNode node = (edu.cmu.cs.dennisc.alice.ast.AbstractNode)parent;
			return node.getProperties().indexOf( child );
		} else if( parent instanceof edu.cmu.cs.dennisc.property.ListProperty<?> ) {
			edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)parent;
			return listProperty.indexOf( child );
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	public javax.swing.tree.TreePath getTreePath( Object node ) {
		java.util.List< Object > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		list.add( node );
		//todo
		return new javax.swing.tree.TreePath( list.toArray() );
	}
}

class ASTCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.TreeCellRenderer< Object > {
	@Override
	protected javax.swing.JLabel updateListCellRendererComponent(javax.swing.JLabel rv, javax.swing.JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		StringBuilder sb = new StringBuilder();
		if( value instanceof edu.cmu.cs.dennisc.alice.ast.AbstractNode ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractNode node = (edu.cmu.cs.dennisc.alice.ast.AbstractNode)value;
			sb.append( node.getClass().getSimpleName() );
			sb.append( ": " );
			sb.append( node.getName() );
		} else if( value instanceof edu.cmu.cs.dennisc.property.InstanceProperty<?> ) {
			edu.cmu.cs.dennisc.property.InstanceProperty<?> property = (edu.cmu.cs.dennisc.property.InstanceProperty<?>)value;
			sb.append( property.getName() );
			if( property instanceof edu.cmu.cs.dennisc.property.ListProperty<?> ) {
				edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)property;
				sb.append( " (count=" );
				sb.append( listProperty.size() );
				sb.append( ")" );
			} else {
				sb.append( ": " );
				sb.append( property.getValue() );
			}
		} else {
			sb.append( rv.getText() );
		}
		rv.setText( sb.toString() );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class IsAbstractSyntaxTreeShowingState extends IsFrameShowingState {
	private static class SingletonHolder {
		private static IsAbstractSyntaxTreeShowingState instance = new IsAbstractSyntaxTreeShowingState();
	}
	public static IsAbstractSyntaxTreeShowingState getInstance() {
		return SingletonHolder.instance;
	}
	private final ASTModel treeModel = new ASTModel();

	private IsAbstractSyntaxTreeShowingState() {
		super( org.alice.ide.ProjectApplication.INFORMATION_GROUP, java.util.UUID.fromString( "3fb1e733-1736-476d-b40c-7729c82f0b21" ), false );
	}
	@Override
	protected void localize() {
		super.localize();
		this.setTextForBothTrueAndFalse( "Abstract Syntax Tree" );
	}
	@Override
	protected java.awt.Component createPane() {
		final javax.swing.JTree tree = new javax.swing.JTree( treeModel );
		tree.setCellRenderer( new ASTCellRenderer() );
		for( int i=0; i<tree.getRowCount(); i++ ) {
			tree.expandRow( i );
		}
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( tree ) {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMaximumSize( super.getPreferredSize(), 640, 480 );
			}
		};
		scrollPane.getVerticalScrollBar().setUnitIncrement( 12 );
		return scrollPane;
	}
	@Override
	protected void handleChanged( boolean value ) {
		super.handleChanged( value );
		this.treeModel.reload();
	}
}
