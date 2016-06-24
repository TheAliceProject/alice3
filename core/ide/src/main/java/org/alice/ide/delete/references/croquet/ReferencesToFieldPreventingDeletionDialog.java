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
package org.alice.ide.delete.references.croquet;

import org.lgna.croquet.SimpleOperationInputDialogCoreComposite;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.UserField;

/**
 * @author Matt May
 */
public class ReferencesToFieldPreventingDeletionDialog extends SimpleOperationInputDialogCoreComposite<org.alice.ide.delete.references.croquet.views.ReferencesToFieldPreventingDeletionPane> {

	private final UserField field;
	private final java.util.List<org.lgna.project.ast.FieldAccess> references;

	private final StringValue singlularDescriptionText = this.createStringValue( "singlularDescriptionText" );
	private final StringValue pluralDescriptionText = this.createStringValue( "pluralDescriptionText" );

	public ReferencesToFieldPreventingDeletionDialog( UserField field, java.util.List<org.lgna.project.ast.FieldAccess> references ) {
		super( java.util.UUID.fromString( "e6ba357c-6490-4e88-a406-ba6567a4cc71" ), null );
		this.field = field;
		this.references = references;
	}

	@Override
	protected String modifyLocalizedText( org.lgna.croquet.Element element, String localizedText ) {
		localizedText = super.modifyLocalizedText( element, localizedText );
		if( element == this.pluralDescriptionText ) {
			localizedText = localizedText.replaceAll( "</referenceCount/>", Integer.toString( this.references.size() ) );
		}
		if( ( element == this.singlularDescriptionText ) || ( element == this.pluralDescriptionText ) ) {
			localizedText = localizedText.replaceAll( "</fieldName/>", this.field.getName() );
			localizedText = localizedText.replaceAll( "\\n", "<br>" );
			localizedText = "<html>" + localizedText + "</html>";
		}
		return localizedText;
	}

	@Override
	protected org.alice.ide.delete.references.croquet.views.ReferencesToFieldPreventingDeletionPane createView() {
		return new org.alice.ide.delete.references.croquet.views.ReferencesToFieldPreventingDeletionPane( this );
	}

	public UserField getField() {
		return this.field;
	}

	public StringValue getAppropriateDescriptionText() {
		if( this.references.size() == 1 ) {
			return this.singlularDescriptionText;
		} else {
			return this.pluralDescriptionText;
		}
	}

	@Override
	protected Edit createEdit( CompletionStep<?> completionStep ) {
		return null;
	}

	@Override
	protected org.lgna.croquet.AbstractSeverityStatusComposite.Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		return null;
	}
}
