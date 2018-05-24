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
package org.lgna.story.resourceutilities;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.javax.swing.models.TreeNode;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.SModel;
import org.lgna.story.resources.ModelResource;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * @author dculyba
 * 
 */
public class ModelResourceTreeNode implements TreeNode<JavaType>, Comparable<ModelResourceTreeNode> {
	public ModelResourceTreeNode( NamedUserType aliceClass, Class<? extends ModelResource> resourceClass, Class<? extends SModel> modelClass ) {
		this.userType = aliceClass;
		if( this.userType != null ) {
			this.name = this.userType.getName();
		}
		this.resourceClass = resourceClass;
		this.modelClass = modelClass;
		this.resourceJavaType = JavaType.getInstance( this.resourceClass );
	}

	@Override
	public TreeNode<JavaType> getParent() {
		return this.parent;
	}

	public void setParent( TreeNode<JavaType> parent ) {
		if( this.parent instanceof ModelResourceTreeNode ) {
			ModelResourceTreeNode parentNode = (ModelResourceTreeNode)this.parent;
			parentNode.removeChild( this );
		}
		this.parent = parent;
		if( this.parent instanceof ModelResourceTreeNode ) {
			ModelResourceTreeNode parentNode = (ModelResourceTreeNode)this.parent;
			parentNode.addChild( this );
		}
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	private List<? extends ModelResourceTreeNode> getSortedChildren() {
		if( this.isSorted ) {
			//pass
		} else {
			Collections.sort( this.children );
			this.isSorted = true;
		}
		return this.children;
	}

	public List<? extends ModelResourceTreeNode> childrenList() {
		return this.getSortedChildren();
	}

	@Override
	public Enumeration<? extends ModelResourceTreeNode> children() {
		return Collections.enumeration( this.getSortedChildren() );
	}

	@Override
	public Iterator iterator() {
		return this.children.iterator();
	}

	@Override
	public TreeNode<JavaType> getChildAt( int childIndex ) {
		return this.getSortedChildren().get( childIndex );
	}

	@Override
	public int getChildCount() {
		return this.children.size();
	}

	@Override
	public boolean isLeaf() {
		return this.children.size() == 0;
	}

	@Override
	public int getIndex( javax.swing.tree.TreeNode node ) {
		return this.getSortedChildren().indexOf( node );
	}

	public void addChild( ModelResourceTreeNode node ) {
		this.children.add( node );
		this.isSorted = false;
	}

	public void removeChild( ModelResourceTreeNode node ) {
		this.children.remove( node );
	}

	public ModelResourceTreeNode getChildWithValue( NamedUserType resourceClass ) {
		for( ModelResourceTreeNode child : this.children ) {
			if( ( resourceClass != null ) && resourceClass.equals( child.getValue() ) ) {
				return child;
			}
		}
		return null;
	}

	public ModelResourceTreeNode getDescendant( NamedUserType resourceClass ) {
		ModelResourceTreeNode rv = this.getChildWithValue( resourceClass );
		if( rv != null ) {
			return rv;
		}
		if( this.getChildCount() > 0 ) {
			for( ModelResourceTreeNode child : this.children ) {
				ModelResourceTreeNode result = child.getDescendant( resourceClass );
				if( result != null ) {
					return result;
				}
			}
		}
		return null;
	}

	public ModelResourceTreeNode getChildWithJavaType( AbstractType<?, ?, ?> type ) {
		for( ModelResourceTreeNode child : this.children ) {
			if( ( type != null ) && ( child.resourceJavaType != null ) && type.isAssignableFrom( child.resourceJavaType ) ) {
				return child;
			}
		}
		return null;
	}

	public ModelResourceTreeNode getDescendantOfJavaType( AbstractType<?, ?, ?> type ) {
		ModelResourceTreeNode rv = this.getChildWithJavaType( type );
		if( rv != null ) {
			return rv;
		}
		if( this.getChildCount() > 0 ) {
			for( ModelResourceTreeNode child : this.children ) {
				ModelResourceTreeNode result = child.getDescendantOfJavaType( type );
				if( result != null ) {
					return result;
				}
			}
		}
		return null;
	}

	public ModelResourceTreeNode getChildWithUserType( AbstractType<?, ?, ?> type ) {
		for( ModelResourceTreeNode child : this.children ) {
			if( ( type != null ) && ( child.userType != null ) && child.userType.isAssignableTo( type ) ) {
				return child;
			}
		}
		return null;
	}

	public ModelResourceTreeNode getDescendantOfUserType( AbstractType<?, ?, ?> type ) {
		ModelResourceTreeNode rv = this.getChildWithUserType( type );
		if( rv != null ) {
			return rv;
		}
		if( this.getChildCount() > 0 ) {
			for( ModelResourceTreeNode child : this.children ) {
				ModelResourceTreeNode result = child.getDescendantOfUserType( type );
				if( result != null ) {
					return result;
				}
			}
		}
		return null;
	}

	public void setUserType( NamedUserType type ) {
		this.userType = type;
	}

	public NamedUserType getUserType() {
		return this.userType;
	}

	@Override
	public JavaType getValue() {
		return this.resourceJavaType;
	}

	public String getName() {
		return this.name;
	}

	public Class<? extends ModelResource> getResourceClass() {
		return this.resourceClass;
	}

	public Class<? extends SModel> getModelClass() {
		return this.modelClass;
	}

	public boolean hasModelClass() {
		return this.modelClass != null;
	}

	public JavaType getResourceJavaType() {
		return this.resourceJavaType;
	}

	public void setModelResourceInstance( ModelResource modelResource ) {
		this.modelResourceInstance = modelResource;
	}

	public ModelResource getModelResourceInstance() {
		return this.modelResourceInstance;
	}

	public void setJavaField( JavaField field ) {
		this.resourceJavaField = field;
	}

	public JavaField getJavaField() {
		return this.resourceJavaField;
	}

	@Override
	public int compareTo( ModelResourceTreeNode other ) {
		if( this.getAllowsChildren() ) {
			if( other.getAllowsChildren() ) {
				return this.getName().compareToIgnoreCase( other.getName() );
			} else {
				return -1;
			}
		} else {
			if( other.getAllowsChildren() ) {
				return 1;
			} else {
				return this.getName().compareToIgnoreCase( other.getName() );
			}
		}
	}

	@Override
	public String toString() {
		JavaType value = this.getValue();
		return value != null ? value.getName() : null;
	}

	public void printTree() {
		this.printTree( "" );
	}

	private void printTree( String indent ) {
		System.out.println( indent + "+" + this.name + " : " + this.userType );
		indent += "  ";
		for( ModelResourceTreeNode child : this.children ) {
			child.printTree( indent );
		}
	}

	private TreeNode<JavaType> parent;
	private List<ModelResourceTreeNode> children = Lists.newLinkedList();
	private NamedUserType userType;
	private Class<? extends ModelResource> resourceClass;

	private JavaType resourceJavaType;

	private Class<? extends SModel> modelClass = null;
	private JavaField resourceJavaField = null;
	private ModelResource modelResourceInstance = null;
	private String name;
	private boolean isSorted = false;
}
