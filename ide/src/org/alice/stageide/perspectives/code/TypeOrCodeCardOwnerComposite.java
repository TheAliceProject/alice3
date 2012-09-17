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

package org.alice.stageide.perspectives.code;

/**
 * @author Dennis Cosgrove
 */
public class TypeOrCodeCardOwnerComposite extends org.lgna.croquet.CardOwnerComposite {
	private static class SingletonHolder {
		private static TypeOrCodeCardOwnerComposite instance = new TypeOrCodeCardOwnerComposite();
	}

	public static TypeOrCodeCardOwnerComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final org.lgna.croquet.State.ValueListener<org.alice.ide.declarationseditor.DeclarationComposite> declarationListener = new org.lgna.croquet.State.ValueListener<org.alice.ide.declarationseditor.DeclarationComposite>() {
		public void changing( org.lgna.croquet.State<org.alice.ide.declarationseditor.DeclarationComposite> state, org.alice.ide.declarationseditor.DeclarationComposite prevValue, org.alice.ide.declarationseditor.DeclarationComposite nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<org.alice.ide.declarationseditor.DeclarationComposite> state, org.alice.ide.declarationseditor.DeclarationComposite prevValue, org.alice.ide.declarationseditor.DeclarationComposite nextValue, boolean isAdjusting ) {
			TypeOrCodeCardOwnerComposite.this.handleDeclarationStateChanged( nextValue );
		}
	};

	private TypeOrCodeCardOwnerComposite() {
		super( java.util.UUID.fromString( "698a5480-5af2-47af-8faa-9cc8d82f4fe8" ),
				org.alice.ide.typehierarchy.TypeHierarchyComposite.getInstance(),
				org.alice.ide.members.MembersComposite.getInstance() );
	}

	private void handleDeclarationStateChanged( org.alice.ide.declarationseditor.DeclarationComposite nextValue ) {
		org.lgna.croquet.Composite<?> composite;
		if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
			if( nextValue != null ) {
				if( nextValue.getDeclaration() instanceof org.lgna.project.ast.AbstractType ) {
					composite = org.alice.ide.typehierarchy.TypeHierarchyComposite.getInstance();
				} else {
					composite = org.alice.ide.members.MembersComposite.getInstance();
				}
			} else {
				composite = null;
			}
		} else {
			composite = org.alice.ide.members.MembersComposite.getInstance();
		}
		this.showCard( composite );
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		org.alice.ide.declarationseditor.DeclarationTabState.getInstance().addAndInvokeValueListener( this.declarationListener );
	}

	@Override
	public void handlePostDeactivation() {
		org.alice.ide.declarationseditor.DeclarationTabState.getInstance().removeValueListener( this.declarationListener );
		super.handlePostDeactivation();
	}
}
