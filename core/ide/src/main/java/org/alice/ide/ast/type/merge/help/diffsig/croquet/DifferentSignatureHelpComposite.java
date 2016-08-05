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
package org.alice.ide.ast.type.merge.help.diffsig.croquet;

import org.alice.ide.ast.type.merge.croquet.DifferentSignature;
import org.alice.ide.ast.type.merge.help.croquet.PotentialNameChangerHelpComposite;

/**
 * @author Dennis Cosgrove
 */
public abstract class DifferentSignatureHelpComposite<M extends org.lgna.project.ast.Member> extends PotentialNameChangerHelpComposite<org.alice.ide.ast.type.merge.help.diffsig.croquet.views.DifferentSignatureHelpView, M, DifferentSignature<M>> {
	private final org.lgna.croquet.ImmutableDataSingleSelectListState<DifferentSignatureChoice> choiceState = this.createImmutableListStateForEnum( "choiceState", DifferentSignatureChoice.class, null );

	private final org.lgna.croquet.event.ValueListener<DifferentSignatureChoice> topLevelListener = new org.lgna.croquet.event.ValueListener<DifferentSignatureChoice>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureChoice> e ) {
			handleChanged();
		}
	};

	public DifferentSignatureHelpComposite( java.util.UUID migrationId, DifferentSignature<M> differentSignature, String signatureText ) {
		super( migrationId, differentSignature );
		StringBuilder sb = new StringBuilder();
		sb.append( "<html>" );
		sb.append( "Class file </filename/> contains a </kindOfMember/> <strong>\"</memberName/>\"</strong> which has a " );
		sb.append( signatureText );
		sb.append( " different to that of a </kindOfMember/> already in your project.<p><p>" );
		sb.append( "You have two options:" );
		sb.append( "<ol>" );
		sb.append( "<li><strong>add and retain both</strong> versions (note: renaming at least one will be required)" );
		sb.append( "<li>only <strong>retain</strong> the version already in your project" );
		sb.append( "</ol>" );
		sb.append( "</html>" );

		String kindOfMemberText;
		M member = differentSignature.getImportHub().getMember();
		if( member instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)member;
			kindOfMemberText = method.isProcedure() ? "procedure" : "function";
		} else {
			kindOfMemberText = "property";
		}
		String text = sb.toString();
		text = org.alice.ide.ast.type.merge.croquet.AddMembersPage.modifyFilenameLocalizedText( text, differentSignature.getUriForDescriptionPurposesOnly() );
		text = text.replaceAll( "</kindOfMember/>", kindOfMemberText );
		text = text.replaceAll( "</memberName/>", member.getName() );
		this.getHeader().setText( text );
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<DifferentSignatureChoice> getChoiceState() {
		return this.choiceState;
	}

	@Override
	protected boolean isRetainBothSelected() {
		return this.choiceState.getValue() == DifferentSignatureChoice.ADD_AND_RETAIN_BOTH;
	}

	@Override
	protected org.alice.ide.ast.type.merge.help.diffsig.croquet.views.DifferentSignatureHelpView createView() {
		return new org.alice.ide.ast.type.merge.help.diffsig.croquet.views.DifferentSignatureHelpView( this );
	}

	@Override
	public void handlePreActivation() {
		boolean isImport = this.getPotentialNameChanger().getImportHub().getIsDesiredState().getValue();
		DifferentSignatureChoice topLevelChoice;
		if( isImport ) {
			topLevelChoice = DifferentSignatureChoice.ADD_AND_RETAIN_BOTH;
		} else {
			topLevelChoice = DifferentSignatureChoice.ONLY_RETAIN_VERSION_ALREADY_IN_PROJECT;
		}
		this.choiceState.setValueTransactionlessly( topLevelChoice );
		this.choiceState.addNewSchoolValueListener( this.topLevelListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		this.choiceState.removeNewSchoolValueListener( this.topLevelListener );
	}

	private void handleChanged() {
		DifferentSignatureChoice choice = this.choiceState.getValue();
		boolean isImport;
		if( choice == DifferentSignatureChoice.ADD_AND_RETAIN_BOTH ) {
			isImport = true;
		} else if( choice == DifferentSignatureChoice.ONLY_RETAIN_VERSION_ALREADY_IN_PROJECT ) {
			isImport = false;
		} else {
			//should never happen
			isImport = false;
		}
		this.getPotentialNameChanger().getImportHub().getIsDesiredState().setValueTransactionlessly( isImport );
	}
}
