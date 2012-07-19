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

import java.util.List;

import org.alice.ide.IDE;
import org.alice.ide.croquet.models.project.TreeNodesAndManagers.FieldReferenceSearchTreeNode;
import org.alice.ide.croquet.models.project.views.FieldReferenceView;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.SimpleComposite;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserMethod;

/**
 * @author Matt May
 */
public class FieldReferenceComposite extends SimpleComposite<FieldReferenceView> implements ValueListener<FieldReferenceSearchTreeNode> {

	private ListSelectionState<FieldAccess> references = createListSelectionState( createKey( "references" ), FieldAccess.class, org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.FieldAccess.class ), -1 );
	private boolean isJumpDesired = true;
	private ValueListener<FieldAccess> adapter = new ValueListener<FieldAccess>() {

		public void changing( State<FieldAccess> state, FieldAccess prevValue, FieldAccess nextValue, boolean isAdjusting ) {
		}

		public void changed( State<FieldAccess> state, FieldAccess prevValue, FieldAccess nextValue, boolean isAdjusting ) {
			FieldAccess selection = state.getValue();
			if( selection != null && isJumpDesired ) {
				UserMethod ancestor = selection.getParent().getFirstAncestorAssignableTo( UserMethod.class );
				if( ancestor == null ) {
					NamedUserType type = selection.getFirstAncestorAssignableTo( NamedUserType.class );
					IDE.getActiveInstance().selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite.getInstance( type ) );
					IDE.getActiveInstance().showHighlightStencil( nextValue.expression.getValue(), null );
				} else {
					IDE.getActiveInstance().selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite.getInstance( ancestor ) );
					IDE.getActiveInstance().showHighlightStencil( nextValue.expression.getValue(), null );
				}
			}
		}
	};

	public FieldReferenceComposite( FieldSearchTabCompsoite fieldSearchCompsoite ) {
		super( java.util.UUID.fromString( "50a45fd7-6ec1-4549-8131-1da15ddac7fb" ) );
		fieldSearchCompsoite.addListener( this );
		references.addValueListener( adapter );
	}

	public void changing( State<FieldReferenceSearchTreeNode> state, FieldReferenceSearchTreeNode prevValue, FieldReferenceSearchTreeNode nextValue, boolean isAdjusting ) {
	}

	public void changed( State<FieldReferenceSearchTreeNode> state, FieldReferenceSearchTreeNode prevValue, FieldReferenceSearchTreeNode nextValue, boolean isAdjusting ) {
		if( nextValue != null ) {
			List<FieldAccess> nextReferences = nextValue.getReferences();
			this.references.setListData( -1, nextReferences );
		}
	}

	@Override
	protected FieldReferenceView createView() {
		return new FieldReferenceView( this );
	}

	public ListSelectionState<FieldAccess> getReferences() {
		return this.references;
	}

	public void refresh() {
	}

}
