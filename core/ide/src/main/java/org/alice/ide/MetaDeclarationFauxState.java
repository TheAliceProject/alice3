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

package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public class MetaDeclarationFauxState {
	public static interface ValueListener {
		public void changed( org.lgna.project.ast.AbstractDeclaration prevValue, org.lgna.project.ast.AbstractDeclaration nextValue );
	};

	public MetaDeclarationFauxState( ProjectDocumentFrame projectDocumentFrame ) {
		this.projectDocumentFrame = projectDocumentFrame;
		this.projectDocumentFrame.getPerspectiveState().addNewSchoolValueListener( this.perspectiveListener );
		this.projectDocumentFrame.getDeclarationsEditorComposite().getTabState().addNewSchoolValueListener( this.declarationTabListener );
		this.prevDeclaration = this.getValue();
	}

	public org.lgna.project.ast.AbstractDeclaration getValue() {
		if( this.projectDocumentFrame.isInSetupScenePerspective() ) {
			return IDE.getActiveInstance().getPerformEditorGeneratedSetUpMethod();
		} else {
			org.alice.ide.declarationseditor.DeclarationComposite<?, ?> declarationComposite = this.projectDocumentFrame.getDeclarationsEditorComposite().getTabState().getValue();
			return declarationComposite != null ? declarationComposite.getDeclaration() : null;
		}
	}

	public org.lgna.project.ast.AbstractType<?, ?, ?> getType() {
		org.lgna.project.ast.AbstractDeclaration declaration = this.getValue();
		if( declaration != null ) {
			if( declaration instanceof org.lgna.project.ast.AbstractType<?, ?, ?> ) {
				org.lgna.project.ast.AbstractType<?, ?, ?> type = (org.lgna.project.ast.AbstractType<?, ?, ?>)declaration;
				return type;
			} else if( declaration instanceof org.lgna.project.ast.AbstractMember ) {
				org.lgna.project.ast.AbstractMember member = (org.lgna.project.ast.AbstractMember)declaration;
				return member.getDeclaringType();
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( declaration );
				return null;
			}
		} else {
			return null;
		}
	}

	public void addValueListener( ValueListener valueListener ) {
		this.valueListeners.add( valueListener );
	}

	public void addAndInvokeValueListener( ValueListener valueListener ) {
		this.addValueListener( valueListener );
		//note: same value for prev and next
		valueListener.changed( this.prevDeclaration, this.getValue() );
	}

	public void removeValueListener( ValueListener valueListener ) {
		this.valueListeners.remove( valueListener );
	}

	private void fireChanged() {
		org.lgna.project.ast.AbstractDeclaration nextValue = this.getValue();
		if( this.prevDeclaration != nextValue ) {
			for( ValueListener valueListener : this.valueListeners ) {
				valueListener.changed( this.prevDeclaration, nextValue );
			}
			this.prevDeclaration = nextValue;
		}
	}

	private void handleIsSceneEditorExpandedChanged() {
		this.fireChanged();
	}

	private void handleDeclarationTabChanged() {
		this.fireChanged();
	}

	private final ProjectDocumentFrame projectDocumentFrame;
	private final java.util.List<ValueListener> valueListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final org.lgna.croquet.event.ValueListener<org.alice.ide.perspectives.ProjectPerspective> perspectiveListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.perspectives.ProjectPerspective>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.perspectives.ProjectPerspective> e ) {
			MetaDeclarationFauxState.this.handleIsSceneEditorExpandedChanged();
		}
	};
	private final org.lgna.croquet.event.ValueListener<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> declarationTabListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> e ) {
			MetaDeclarationFauxState.this.handleDeclarationTabChanged();
		}
	};
	private org.lgna.project.ast.AbstractDeclaration prevDeclaration;
}
