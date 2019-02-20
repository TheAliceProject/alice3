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

package org.alice.ide.declarationseditor.type.components;

import org.alice.ide.ThemeUtilities;
import org.alice.ide.ast.rename.RenameFieldComposite;
import org.alice.ide.common.FieldDeclarationPane;
import org.alice.ide.croquet.models.ast.DeleteFieldOperation;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.lgna.croquet.Operation;
import org.lgna.croquet.SingleSelectListState;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.UserField;

import javax.swing.BorderFactory;
import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public abstract class FieldList extends MemberList<UserField> {
	public FieldList( SingleSelectListState<UserField, ?> model, Operation operation ) {
		super( model, operation );
		this.setBackgroundColor( ThemeUtilities.getActiveTheme().getFieldColor() );
	}

	@Override
	protected SwingComponentView<?> createButtonLineStart( UserField item ) {
		ManagementLevel managementLevel = item.managementLevel.getValue();
		if( managementLevel == ManagementLevel.MANAGED ) {
			Label label = new Label( "*" );
			label.setToolTipText( "managed by the scene editor" );
			label.setForegroundColor( Color.GRAY );
			label.scaleFont( 2.0f );
			label.setBorder( BorderFactory.createEmptyBorder( 0, 4, 0, 4 ) );
			return label;
		} else {
			return null;
		}
	}

	@Override
	protected SwingComponentView<?> createButtonCenter( UserField item ) {
		ManagementLevel managementLevel = item.managementLevel.getValue();
		return new FieldDeclarationPane( PreviewAstI18nFactory.getInstance(), item, managementLevel != ManagementLevel.MANAGED );
	}

	@Override
	protected SwingComponentView<?> createButtonLineEnd( UserField item ) {
		LineAxisPanel rv = new LineAxisPanel();
		rv.addComponent( RenameFieldComposite.getInstance( item ).getLaunchOperation().createButton() );
		if( item.isDeletionAllowed.getValue() ) {
			rv.addComponent( DeleteFieldOperation.getInstance( item ).createButton() );
		} else {
			//todo
			rv.addComponent( BoxUtilities.createHorizontalSliver( 64 ) );
		}
		return rv;
	}
}
