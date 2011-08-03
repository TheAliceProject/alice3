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
package org.lgna.story.resourceutilities;

import java.lang.reflect.Field;
import java.util.List;

import org.lgna.project.ast.TypeDeclaredInAlice;
import org.lgna.story.resources.ModelResource;



/**
 * @author dculyba
 *
 */
public class ModelResourceTreeNode implements edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice>, Comparable<ModelResourceTreeNode>  {

	private edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> parent;
	private java.util.List< ModelResourceTreeNode > children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private TypeDeclaredInAlice aliceClass;
	private Class<?> resourceClass;
	private ModelResource modelResource;
	private String name;
	private boolean isSorted = false;
	
	public ModelResourceTreeNode( TypeDeclaredInAlice aliceClass, Class<?> resourceClass ) {
		this.aliceClass = aliceClass;
		if (this.aliceClass != null)
		{
			this.name = this.aliceClass.getName();
		}
		this.resourceClass = resourceClass;
		
	}
	public edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> getParent() {
		return this.parent;
	}
	public void setParent( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> parent ) {
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
	
	public TypeDeclaredInAlice getTypeDeclaredInAlice()
	{
		return this.aliceClass;
	}
	
	public void setTypeDeclaredInAlice(TypeDeclaredInAlice type)
	{
		this.aliceClass = type;
	}
	
	public boolean getAllowsChildren() {
		return true;
	}
	private java.util.List< ? extends edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> > getSortedChildren() {
		if( this.isSorted ) {
			//pass
		} else {
			java.util.Collections.sort( this.children );
			this.isSorted = true;
		}
		return this.children;
	}
	public java.util.Enumeration< ? extends edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> > children() {
		return java.util.Collections.enumeration( this.getSortedChildren() );
	}
	public java.util.Iterator iterator() {
		return this.children.iterator();
	}
	public edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> getChildAt(int childIndex) {
		return this.getSortedChildren().get( childIndex );
	}
	public int getChildCount() {
		return this.children.size();
	}
	public boolean isLeaf() {
		return this.children.size() == 0;
	}
	public int getIndex(javax.swing.tree.TreeNode node) {
		return this.getSortedChildren().indexOf( node );
	}
	
	public void addChild( ModelResourceTreeNode node ) {
		this.children.add( node );
		this.isSorted = false;
	}
	
	public void removeChild( ModelResourceTreeNode node ) {
		this.children.remove( node );
	}
	
	public ModelResourceTreeNode getChildWithValue( TypeDeclaredInAlice resourceClass ) {
		for( ModelResourceTreeNode child : this.children ) {
			if( resourceClass.equals( child.getValue() ) ) {
				return child;
			}
		}
		return null;
	}
	public ModelResourceTreeNode getDescendant( TypeDeclaredInAlice resourceClass ) {
		ModelResourceTreeNode rv = this.getChildWithValue(resourceClass);
		if (rv != null)
		{
			return rv;
		}
		if (this.getChildCount() > 0)
		{
			for (ModelResourceTreeNode child : this.children)
			{
				ModelResourceTreeNode result = child.getDescendant(resourceClass);
				if (result != null)
				{
					return result;
				}
			}
		}
		return null;
	}
	
	public TypeDeclaredInAlice getValue() {
		return this.aliceClass;
	}
	public String getName() {
		return this.name;
	}
	
	public Class<?> getResourceClass()
	{
		return this.resourceClass;
	}
	
	public void setModelResource(ModelResource modelResource)
	{
		this.modelResource = modelResource;
	}
	
	public ModelResource getModelResource()
	{
		return this.modelResource;
	}
	
	public int compareTo(ModelResourceTreeNode other) {
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
		return this.getValue().getName();
	}
	
	public void printTree()
	{
		this.printTree("");
	}
	
	private void printTree(String indent)
	{
		System.out.println(indent+"+"+this.name+" : "+this.aliceClass);
		indent += "  ";
		for (ModelResourceTreeNode child : this.children)
		{
			child.printTree(indent);
		}
	}
	
}
