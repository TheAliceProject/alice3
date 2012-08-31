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
package org.alice.stageide.modelresource;

/**
 * @author Dennis Cosgrove
 */
public class ResourceNodeTreeSelectionState extends org.lgna.croquet.CustomTreeSelectionState<ResourceNode> {
	private static class SingletonHolder {
		private static ResourceNodeTreeSelectionState instance = new ResourceNodeTreeSelectionState();
	}

	public static ResourceNodeTreeSelectionState getInstance() {
		return SingletonHolder.instance;
	}

	private ResourceNodeTreeSelectionState() {
		super( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "c37c9803-2482-4f1b-9731-b110f1f6fde7" ), ResourceNodeCodec.SINGLETON, TreeUtilities.getTreeBasedOnClassHierarchy() );
	}

	@Override
	protected javax.swing.Icon getIconForNode( ResourceNode node ) {
		return null;
		//return node.getSmallIcon();
	}

	@Override
	protected String getTextForNode( ResourceNode node ) {
		return node.toString();
	}

	@Override
	protected int getChildCount( ResourceNode parent ) {
		return parent.getChildren().size();
	}

	@Override
	protected ResourceNode getChild( ResourceNode parent, int index ) {
		return parent.getChildren().get( index );
	}

	@Override
	protected int getIndexOfChild( ResourceNode parent, ResourceNode child ) {
		return parent.getChildren().indexOf( child );
	}

	@Override
	public ResourceNode getParent( ResourceNode node ) {
		return node.getParent();
	}

	@Override
	protected ResourceNode getRoot() {
		return TreeUtilities.getTreeBasedOnClassHierarchy();
	}

	@Override
	public boolean isLeaf( ResourceNode node ) {
		return false;
		//return node.isLeaf();
	}
}
