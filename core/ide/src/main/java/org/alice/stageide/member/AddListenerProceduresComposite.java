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

package org.alice.stageide.member;

/**
 * @author Dennis Cosgrove
 */
public class AddListenerProceduresComposite extends org.alice.ide.member.FilteredJavaMethodsSubComposite {
	private static class SingletonHolder {
		private static AddListenerProceduresComposite instance = new AddListenerProceduresComposite();
	}

	public static AddListenerProceduresComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final java.util.Collection<String> names = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( "addDefaultModelManipulation", "addObjectMoverFor" );
	private final java.util.Comparator<org.lgna.project.ast.JavaMethod> comparator = new java.util.Comparator<org.lgna.project.ast.JavaMethod>() {
		@Override
		public int compare( org.lgna.project.ast.JavaMethod methodA, org.lgna.project.ast.JavaMethod methodB ) {
			return compareMethodNames( methodA, methodB );
		}
	};

	private AddListenerProceduresComposite() {
		super( java.util.UUID.fromString( "cfb5bd39-c07b-4436-a4e9-031dd25ca3b5" ), true );
	}

	@Override
	public java.util.Comparator<org.lgna.project.ast.JavaMethod> getComparator() {
		return this.comparator;
	}

	@Override
	public boolean isShowingDesired() {
		org.alice.ide.declarationseditor.DeclarationsEditorComposite composite = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite();
		if( composite != null ) {
			org.alice.ide.declarationseditor.DeclarationTabState tabState = composite.getTabState();
			if( tabState != null ) {
				org.alice.ide.declarationseditor.DeclarationComposite<?, ?> declarationComposite = tabState.getValue();
				if( declarationComposite instanceof org.alice.ide.declarationseditor.CodeComposite ) {
					org.alice.ide.declarationseditor.CodeComposite codeComposite = (org.alice.ide.declarationseditor.CodeComposite)declarationComposite;
					org.lgna.project.ast.AbstractCode code = codeComposite.getDeclaration();
					if( code != null ) {
						String name = code.getName();
						if( name != null ) {
							if( org.alice.stageide.StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME.equals( name ) ) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	protected boolean isAcceptingOf( org.lgna.project.ast.JavaMethod method ) {
		String name = method.getName();
		return method.isProcedure() && ( names.contains( name ) || ( name.startsWith( "add" ) && name.endsWith( "Listener" ) ) );
	}
}
