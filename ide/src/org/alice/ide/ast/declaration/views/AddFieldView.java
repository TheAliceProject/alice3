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

package org.alice.ide.ast.declaration.views;

/**
 * @author Dennis Cosgrove
 */
public class AddFieldView extends AddDeclarationView<org.lgna.project.ast.UserField> {
	private final org.lgna.croquet.components.Label typeIconView = new org.lgna.croquet.components.Label( org.lgna.croquet.icon.EmptyIconFactory.SINGLETON.getIcon( org.alice.ide.Theme.DEFAULT_LARGE_ICON_SIZE ) );

	public AddFieldView( org.alice.ide.ast.declaration.AddFieldComposite composite ) {
		super( composite );
	}

	@Override
	public org.lgna.croquet.components.JComponent<?> createPreviewSubComponent() {
		org.alice.ide.ast.declaration.AddFieldComposite composite = (org.alice.ide.ast.declaration.AddFieldComposite)this.getComposite();
		org.lgna.project.ast.UserField field = composite.getPreviewValue();
		return new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.x.PreviewAstI18nFactory.getInstance(), field );
	}

	@Override
	protected org.lgna.croquet.components.JComponent<?> createPageStartComponent() {
		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel.Builder()
				.center( super.createPageStartComponent() )
				.lineEnd( this.typeIconView )
				.build();
		return rv;
	}

	@Override
	protected boolean isPreviewDesired() {
		return org.alice.stageide.croquet.models.gallerybrowser.preferences.IsPromptIncludingPreviewState.getInstance().getValue();
	}

	@Override
	public void handleValueTypeChanged( org.lgna.project.ast.AbstractType<?, ?, ?> nextType ) {
		super.handleValueTypeChanged( nextType );
		org.lgna.croquet.icon.IconFactory iconFactory = org.alice.stageide.icons.IconFactoryManager.getIconFactoryForType( nextType );
		this.typeIconView.setIcon( iconFactory.getIcon( iconFactory.getDefaultSize( org.alice.ide.Theme.DEFAULT_LARGE_ICON_SIZE ) ) );
		this.typeIconView.revalidateAndRepaint();
	}
}
