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
package org.alice.ide.javacode.croquet;

/**
 * @author Dennis Cosgrove
 */
public class JavaCodeFrameComposite extends org.lgna.croquet.FrameCompositeWithInternalIsShowingState<org.lgna.croquet.views.Panel> {
	private static class SingletonHolder {
		private static JavaCodeFrameComposite instance = new JavaCodeFrameComposite();
	}

	public static JavaCodeFrameComposite getInstance() {
		return SingletonHolder.instance;
	}

	private JavaCodeFrameComposite() {
		super( java.util.UUID.fromString( "7015e117-22dd-49a1-a194-55d5fe17f821" ), org.lgna.croquet.Application.DOCUMENT_UI_GROUP );
	}

	@Override
	protected org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return new org.lgna.croquet.views.ScrollPane();
	}

	@Override
	protected org.lgna.croquet.views.Panel createView() {
		return new org.lgna.croquet.views.BorderPanel.Builder()
				//.center( this.stringValue.createImmutableEditorPane( 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextFamily.MONOSPACED ) )
				.center( this.javaCodeView )
				.build();
	}

	@Override
	protected Integer getWiderGoldenRatioSizeFromHeight() {
		return 400;
	}

	@Override
	public void handlePreActivation() {
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getMetaDeclarationFauxState().addAndInvokeValueListener( this.declarationListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getMetaDeclarationFauxState().removeValueListener( this.declarationListener );
	}

	private final org.alice.ide.MetaDeclarationFauxState.ValueListener declarationListener = new org.alice.ide.MetaDeclarationFauxState.ValueListener() {
		@Override
		public void changed( org.lgna.project.ast.AbstractDeclaration prevValue, org.lgna.project.ast.AbstractDeclaration nextValue ) {
			javaCodeView.setDeclaration( nextValue );
		}
	};

	private final org.alice.ide.javacode.croquet.views.JavaCodeView javaCodeView = new org.alice.ide.javacode.croquet.views.JavaCodeView();

}
