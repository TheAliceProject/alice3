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

package org.alice.ide.typeeditor;

/*package-private*/ class FieldItemDetails extends MemberItemDetails< org.lgna.project.ast.UserField, FieldItemDetails, FieldList > {
	public FieldItemDetails( FieldList panel, org.lgna.project.ast.UserField item, org.lgna.croquet.components.BooleanStateButton< javax.swing.AbstractButton > button ) {
		super( panel, item, button );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class FieldList extends MemberList< org.lgna.project.ast.UserField, FieldItemDetails > {
	public FieldList( org.lgna.project.ast.NamedUserType type ) {
		super( FieldState.getInstance( type ), org.alice.ide.croquet.models.declaration.NonGalleryFieldDeclarationOperation.getInstance( type ), org.alice.ide.croquet.models.declaration.UnspecifiedValueTypeGalleryFieldDeclarationOperation.getInstance( type ) );
		this.setBackgroundColor( org.alice.ide.IDE.getActiveInstance().getTheme().getFieldColor() );
	}
	@Override
	protected org.alice.ide.typeeditor.FieldItemDetails createItemDetails( org.lgna.project.ast.UserField item, org.lgna.croquet.BooleanState booleanState, MemberButton button ) {
		button.addComponent( org.alice.ide.croquet.models.ast.rename.RenameFieldOperation.getInstance( item ).createButton(), org.lgna.croquet.components.BorderPanel.Constraint.LINE_START );
		button.addComponent( 
				new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.IDE.getActiveInstance().getPreviewFactory(), item ),
				org.lgna.croquet.components.BorderPanel.Constraint.CENTER 
		);
		if( org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().isFieldDeletable( item ) ) {
			button.addComponent( org.alice.ide.croquet.models.ast.DeleteFieldOperation.getInstance( item ).createButton(), org.lgna.croquet.components.BorderPanel.Constraint.LINE_END );
		}
		return new FieldItemDetails( this, item, button );
	}
}
