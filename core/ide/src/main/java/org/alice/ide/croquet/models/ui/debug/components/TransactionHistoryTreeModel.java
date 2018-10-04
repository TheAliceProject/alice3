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

package org.alice.ide.croquet.models.ui.debug.components;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.javax.swing.models.AbstractMutableTreeModel;
import org.lgna.croquet.history.PrepStep;
import org.lgna.croquet.history.ActivityNode;
import org.lgna.croquet.history.UserActivity;

import javax.swing.tree.TreePath;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class TransactionHistoryTreeModel extends AbstractMutableTreeModel<Object> {

	private UserActivity root;

	TransactionHistoryTreeModel( UserActivity root ) {
		this.root = root;
	}

	@Override
	public UserActivity getRoot() {
		return this.root;
	}

	@Override
	public boolean isLeaf( Object node ) {
		return node instanceof PrepStep;
	}

	@Override
	public int getChildCount( Object parent ) {
		if( parent instanceof UserActivity ) {
			UserActivity activity = (UserActivity) parent;
			return activity.getChildStepCount();
		}
		return 0;
	}

	@Override
	public Object getChild( Object parent, int index ) {
		if( parent instanceof UserActivity ) {
			UserActivity activity = (UserActivity)parent;
			return activity.getChildAt( index );
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public int getIndexOfChild( Object parent, Object child ) {
		if( parent instanceof UserActivity ) {
			UserActivity transaction = (UserActivity)parent;
			if( child instanceof PrepStep<?> ) {
				return transaction.getIndexOfPrepStep( (PrepStep<?>)child );
			} else {
				return -1;
			}
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	private void updatePath( List<Object> path, ActivityNode node ) {
		if( node != null ) {
			updatePath( path, node.getOwner() );
			path.add( node );
		}
	}

	@Override
	public TreePath getTreePath( Object node ) {
		List<Object> list = Lists.newLinkedList();
		updatePath( list, (ActivityNode) node );
		return new TreePath( list.toArray() );
	}
}
