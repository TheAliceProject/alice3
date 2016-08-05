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
package org.alice.ide.ast.declaration.views;

/**
 * @author Dennis Cosgrove
 */
public class AddParameterView extends DeclarationView<org.lgna.project.ast.UserParameter> {
	private final org.lgna.croquet.views.Label label = new org.lgna.croquet.views.Label();
	private final org.lgna.croquet.views.BorderPanel warningPanel;

	public AddParameterView( org.alice.ide.ast.declaration.AddParameterComposite composite ) {
		super( composite );
		org.lgna.croquet.views.PageAxisPanel pane = new org.lgna.croquet.views.PageAxisPanel();
		pane.addComponent( this.label );
		pane.addComponent( org.lgna.croquet.views.BoxUtilities.createVerticalSliver( 8 ) );
		//TODO: Localize
		pane.addComponent( new org.lgna.croquet.views.LineAxisPanel( new org.lgna.croquet.views.Label( "Tip: look for " ), org.alice.ide.x.PreviewAstI18nFactory.getInstance().createExpressionPane( new org.lgna.project.ast.NullLiteral() ) ) );
		pane.addComponent( org.lgna.croquet.views.BoxUtilities.createVerticalSliver( 8 ) );
		pane.addComponent( composite.getIsRequirementToUpdateInvocationsUnderstoodState().createCheckBox() );

		org.lgna.croquet.views.Label warningLabel = new org.lgna.croquet.views.Label();
		warningLabel.setIcon( javax.swing.UIManager.getIcon( "OptionPane.warningIcon" ) );
		this.warningPanel = new org.lgna.croquet.views.BorderPanel.Builder().hgap( 32 ).lineStart( warningLabel ).center( pane ).build();

		this.warningPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 32, 8, 32, 8 ) );
		this.setBackgroundColor( org.alice.ide.ThemeUtilities.getActiveTheme().getParameterColor() );
	}

	@Override
	public org.lgna.croquet.views.SwingComponentView<?> createPreviewSubComponent() {
		org.alice.ide.ast.declaration.AddParameterComposite composite = (org.alice.ide.ast.declaration.AddParameterComposite)this.getComposite();
		org.lgna.project.ast.UserParameter parameter = composite.getPreviewValue();
		return new org.alice.ide.codeeditor.TypedParameterPane( null, parameter );
	}

	@Override
	protected org.lgna.croquet.views.BorderPanel createMainComponent() {
		org.lgna.croquet.views.BorderPanel rv = super.createMainComponent();
		rv.addPageEndComponent( this.warningPanel );
		return rv;
	}

	@Override
	protected void handleDisplayable() {
		org.alice.ide.ast.declaration.AddParameterComposite composite = (org.alice.ide.ast.declaration.AddParameterComposite)this.getComposite();
		org.lgna.project.ast.UserCode code = composite.getCode();
		java.util.List<org.lgna.project.ast.SimpleArgumentListProperty> argumentLists = org.alice.ide.IDE.getActiveInstance().getArgumentLists( code );
		final int N = argumentLists.size();
		this.warningPanel.setVisible( N > 0 );
		if( this.warningPanel.isVisible() ) {
			String codeText;
			if( code instanceof org.lgna.project.ast.AbstractMethod ) {
				org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)code;
				if( method.isProcedure() ) {
					codeText = "procedure";
				} else {
					codeText = "function";
				}
			} else {
				codeText = "constructor";
			}

			StringBuffer sb = new StringBuffer();
			sb.append( "<html><body>There " );
			if( N == 1 ) {
				sb.append( "is 1 invocation" );
			} else {
				sb.append( "are " );
				sb.append( N );
				sb.append( " invocations" );
			}
			sb.append( " to this " );
			sb.append( codeText );
			sb.append( " in your program.<br>You will need to fill in " );
			if( N == 1 ) {
				sb.append( "a value" );
			} else {
				sb.append( "values" );
			}
			sb.append( " for the new " );
			if( N == 1 ) {
				sb.append( "argument at the" );
			} else {
				sb.append( "arguments at each" );
			}
			sb.append( " invocation.</body></html>" );
			this.label.setText( sb.toString() );
		}
		super.handleDisplayable();
	}
}
