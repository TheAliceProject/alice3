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

package org.alice.ide.member.views;

import org.alice.ide.IDE;
import org.alice.ide.declarationseditor.DeclarationTabState;
import org.alice.ide.member.MethodsSubComposite;
import org.alice.ide.members.components.templates.TemplateFactory;
import org.lgna.croquet.Operation;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.DragComponent;
import org.lgna.croquet.views.Hyperlink;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.PageAxisPanel;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.UserMethod;

import javax.swing.BorderFactory;

/**
 * @author Dennis Cosgrove
 */
public class MethodsSubView<C extends MethodsSubComposite> extends PageAxisPanel {
	public MethodsSubView( MethodsSubComposite composite ) {
		super( composite );
		this.setMaximumSizeClampedToPreferredSize( true );
		this.setBorder( BorderFactory.createEmptyBorder( 0, 8, 12, 0 ) );
	}

	@Override
	public C getComposite() {
		return (C)super.getComposite();
	}

	@Override
	protected void internalRefresh() {
		super.internalRefresh();
		C composite = this.getComposite();
		this.removeAllComponents();

		composite.updateTabTitle();

		for( AbstractMethod method : composite.getMethods() ) {
			DragComponent<?> dragComponent = TemplateFactory.getMethodInvocationTemplate( method );
			SwingComponentView<?> component;
			if( method instanceof UserMethod ) {
				UserMethod userMethod = (UserMethod)method;
				DeclarationTabState tabState = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
				Operation operation = tabState.getItemSelectionOperationForMethod( method );
				Hyperlink hyperlink = operation.createHyperlink();
				hyperlink.setClobberText( "edit" );
				component = new LineAxisPanel( hyperlink, BoxUtilities.createHorizontalSliver( 8 ), dragComponent );
			} else {
				component = dragComponent;
			}
			this.addComponent( component );
		}
	}
}
