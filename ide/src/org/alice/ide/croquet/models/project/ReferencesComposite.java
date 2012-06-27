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
package org.alice.ide.croquet.models.project;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import org.alice.ide.croquet.models.project.views.ReferencesView;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.TabComposite;
import org.lgna.croquet.components.Tree;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class ReferencesComposite extends TabComposite<ReferencesView> implements ValueListener<SearchTreeNode> {
	private final ReferencesDialogManager manager;
	private final Tree<SearchTreeNode> tree;
	private final PlainStringValue selectedMethod = createStringValue( this.createKey( "selectedMethod" ) );

	public ReferencesComposite( Map<UserMethod,LinkedList<MethodInvocation>> methodParentMap ) {
		super( java.util.UUID.fromString( "bddb8484-a469-4617-9dac-b066b65d4c64" ) );
		manager = new ReferencesDialogManager( methodParentMap );
		tree = new Tree<SearchTreeNode>( manager );
		getTree().setRootVisible( false );
		manager.setOwner( getTree() );
	}

	public PlainStringValue getSelectedMethod() {
		return this.selectedMethod;
	}

	@Override
	public boolean isCloseable() {
		return false;
	}

	@Override
	protected ReferencesView createView() {
		return new ReferencesView( this );
	}

	public void changing( State<SearchTreeNode> state, SearchTreeNode prevValue, SearchTreeNode nextValue, boolean isAdjusting ) {
	}

	public void changed( State<SearchTreeNode> state, SearchTreeNode prevValue, SearchTreeNode nextValue, boolean isAdjusting ) {
		manager.update( nextValue );
		if( nextValue != null ) {
			selectedMethod.setText( "     " + nextValue.getContent().getName() );
		} else {
			selectedMethod.setText( "" );
		}
	}

	public Tree<SearchTreeNode> getTree() {
		return this.tree;
	}

	public class ReferencesDialogManager extends SearchTreeManager {

		public ReferencesDialogManager( Map<UserMethod,LinkedList<MethodInvocation>> methodParentMap ) {
			super( java.util.UUID.fromString( "7b2c3b3d-062b-49ce-a746-13d2bba8f572" ), methodParentMap );
		}

		public void update( SearchTreeNode newValue ) {
			if( newValue != null ) {
				hideAll();
				ArrayList<SearchTreeNode> hiddenList2 = Collections.newArrayList( hiddenList );
				for( SearchTreeNode node : hiddenList2 ) {
					if( node.getContent().equals( newValue.getContent() ) && node.getDepth() <= SHOULD_BE_EXPANDED ) {
						show( node );
					}
				}
				crawl( root, 0 );
				this.refresh( root );
				setProperExpandedLevels( root );
			}
		}

		private void crawl( SearchTreeNode root, int depth ) {
			for( SearchTreeNode child : root.getChildren() ) {
				crawl( child, depth + 1 );
			}
		}
	}
}
