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
package org.alice.ide.meta;

/**
 * @author Dennis Cosgrove
 */
public class DeclarationMeta {
	private DeclarationMeta() {
		throw new AssertionError();
	}

	private static final org.lgna.croquet.event.ValueListener<org.alice.ide.perspectives.ProjectPerspective> perspectiveListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.perspectives.ProjectPerspective>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.perspectives.ProjectPerspective> e ) {
			handlePrespectiveChanged();
		}
	};
	private static final org.lgna.croquet.event.ValueListener<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> declarationTabListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> e ) {
			handleDeclarationTabChanged();
		}
	};

	private static final java.util.List<org.lgna.croquet.event.ValueListener<org.lgna.project.ast.AbstractDeclaration>> declarationListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private static final java.util.List<org.lgna.croquet.event.ValueListener<org.lgna.project.ast.AbstractType<?, ?, ?>>> typeListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private static org.lgna.project.ast.AbstractDeclaration prevDeclaration;
	static {
		org.alice.ide.ProjectDocumentFrame projectDocumentFrame = org.alice.ide.IDE.getActiveInstance().getDocumentFrame();
		projectDocumentFrame.getPerspectiveState().addNewSchoolValueListener( perspectiveListener );
		projectDocumentFrame.getDeclarationsEditorComposite().getTabState().addNewSchoolValueListener( declarationTabListener );
		prevDeclaration = getDeclaration();
	}

	public static void addTypeMetaStateValueListener( org.lgna.croquet.event.ValueListener<org.lgna.project.ast.AbstractType<?, ?, ?>> typeListener ) {
		typeListeners.add( typeListener );
	}

	public static void removeTypeMetaStateValueListener( org.lgna.croquet.event.ValueListener<org.lgna.project.ast.AbstractType<?, ?, ?>> typeListener ) {
		typeListeners.remove( typeListener );
	}

	public static void addDeclarationMetaStateValueListener( org.lgna.croquet.event.ValueListener<org.lgna.project.ast.AbstractDeclaration> declarationListener ) {
		declarationListeners.add( declarationListener );
	}

	public static void removeDeclarationMetaStateValueListener( org.lgna.croquet.event.ValueListener<org.lgna.project.ast.AbstractDeclaration> declarationListener ) {
		declarationListeners.remove( declarationListener );
	}

	public static org.lgna.project.ast.AbstractDeclaration getDeclaration() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide.getDocumentFrame().isInSetupScenePerspective() ) {
			return ide != null ? ide.getPerformEditorGeneratedSetUpMethod() : null;
		} else {
			org.alice.ide.declarationseditor.DeclarationComposite declarationComposite = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
			return declarationComposite != null ? declarationComposite.getDeclaration() : null;
		}
	}

	private static org.lgna.project.ast.AbstractType<?, ?, ?> getType( org.lgna.project.ast.AbstractDeclaration declaration ) {
		if( declaration instanceof org.lgna.project.ast.Code ) {
			org.lgna.project.ast.Code code = (org.lgna.project.ast.Code)declaration;
			return code.getDeclaringType();
		} else if( declaration instanceof org.lgna.project.ast.AbstractType<?, ?, ?> ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = (org.lgna.project.ast.AbstractType<?, ?, ?>)declaration;
			return type;
		} else {
			return null;
		}
	}

	public static org.lgna.project.ast.AbstractType<?, ?, ?> getType() {
		return getType( getDeclaration() );
	}

	private static void fireChanged() {
		org.lgna.project.ast.AbstractDeclaration nextDeclaration = getDeclaration();
		if( prevDeclaration != nextDeclaration ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> prevType = getType( prevDeclaration );
			org.lgna.project.ast.AbstractType<?, ?, ?> nextType = getType( nextDeclaration );
			if( prevType != nextType ) {
				org.lgna.croquet.event.ValueEvent<org.lgna.project.ast.AbstractType<?, ?, ?>> e = org.lgna.croquet.event.ValueEvent.createInstance( prevType, nextType );
				for( org.lgna.croquet.event.ValueListener<org.lgna.project.ast.AbstractType<?, ?, ?>> typeListener : typeListeners ) {
					typeListener.valueChanged( e );
				}
			}
			org.lgna.croquet.event.ValueEvent<org.lgna.project.ast.AbstractDeclaration> e = org.lgna.croquet.event.ValueEvent.createInstance( prevDeclaration, nextDeclaration );
			for( org.lgna.croquet.event.ValueListener<org.lgna.project.ast.AbstractDeclaration> declarationListener : declarationListeners ) {
				declarationListener.valueChanged( e );
			}
			prevDeclaration = nextDeclaration;
		}
	}

	private static void handlePrespectiveChanged() {
		fireChanged();
	}

	private static void handleDeclarationTabChanged() {
		fireChanged();
	}
}
