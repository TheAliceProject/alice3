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
package org.alice.ide.croquet.models.project.TreeNodesAndManagers;

import java.util.List;

import javax.swing.Icon;

import org.alice.ide.ProjectApplication;
import org.lgna.croquet.CustomTreeSelectionState;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.UserField;

import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;

/**
 * @author Matt May
 */
public class InstanceSearchTreeManager extends CustomTreeSelectionState<FieldReferenceSearchTreeNode> {

	private FieldReferenceSearchTreeNode root = FieldReferenceSearchTreeNode.getRoot();

	public InstanceSearchTreeManager( FieldReferenceSearchTreeNode initialSelection ) {
		super( ProjectApplication.INFORMATION_GROUP, java.util.UUID.fromString( "d8242fe1-3ca6-444f-8608-3e593043b18e" ), FieldReferenceSearchTreeNode.getNewItemCodec(), initialSelection );
		refresh();
	}

	public void refresh() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		final org.lgna.project.ast.NamedUserType programType = ide.getStrippedProgramType();
		if( programType != null ) {
			root.removeAllChildren();
			IsInstanceCrawler<FieldAccess> crawler = IsInstanceCrawler.createInstance( FieldAccess.class );
			programType.crawl( crawler, true );
			FieldReferenceSearchTreeNode.initFields(ide.getSceneField());
			List<FieldAccess> fieldAccesses = crawler.getList();
			for( FieldAccess access : fieldAccesses ) {
				if( access.field.getValue() instanceof UserField ) {
					root.find( (UserField) access.field.getValue() ).addReference( access );
				}
			}
		}
		refreshAll();
	}

	@Override
	protected int getChildCount( FieldReferenceSearchTreeNode parent ) {
		return parent.getNumChildren();
	}

	@Override
	protected FieldReferenceSearchTreeNode getChild( FieldReferenceSearchTreeNode parent, int index ) {
		return parent.getChild( index );
	}

	@Override
	protected int getIndexOfChild( FieldReferenceSearchTreeNode parent, FieldReferenceSearchTreeNode child ) {
		return parent.getChildIndex( child );
	}

	@Override
	protected FieldReferenceSearchTreeNode getRoot() {
		return root;
	}

	@Override
	protected FieldReferenceSearchTreeNode getParent( FieldReferenceSearchTreeNode node ) {
		return node.getParent();
	}

	@Override
	public boolean isLeaf( FieldReferenceSearchTreeNode node ) {
		return node.getNumChildren() == 0;
	}

	@Override
	protected String getTextForNode( FieldReferenceSearchTreeNode node ) {
		return node.toString();
	}

	@Override
	protected Icon getIconForNode( FieldReferenceSearchTreeNode node ) {
		return node.getIcon();
	}

	public FieldReferenceSearchTreeNode find( UserField field ) {
		return root.find( field );
	}

}
