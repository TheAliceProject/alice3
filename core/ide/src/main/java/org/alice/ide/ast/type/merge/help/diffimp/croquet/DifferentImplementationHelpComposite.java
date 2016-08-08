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
package org.alice.ide.ast.type.merge.help.diffimp.croquet;

import org.alice.ide.ast.type.merge.croquet.DifferentImplementation;
import org.alice.ide.ast.type.merge.help.croquet.PotentialNameChangerHelpComposite;

/**
 * @author Dennis Cosgrove
 */
public abstract class DifferentImplementationHelpComposite<M extends org.lgna.project.ast.Member> extends PotentialNameChangerHelpComposite<org.alice.ide.ast.type.merge.help.diffimp.croquet.views.DifferentImplementationHelpView, M, DifferentImplementation<M>> {
	private final org.lgna.croquet.codecs.EnumCodec.LocalizationCustomizer<DifferentImplementationChoice> localizationCustomizer = new org.lgna.croquet.codecs.EnumCodec.LocalizationCustomizer<DifferentImplementationChoice>() {
		@Override
		public String customize( String localization, DifferentImplementationChoice value ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( localization, value );
			return org.alice.ide.ast.type.merge.croquet.AddMembersPage.modifyFilenameLocalizedText( localization, getPotentialNameChanger().getUriForDescriptionPurposesOnly() );
		}
	};

	private final org.lgna.croquet.ImmutableDataSingleSelectListState<DifferentImplementationChoice> choiceState = this.createImmutableListStateForEnum( "choiceStates", DifferentImplementationChoice.class, this.localizationCustomizer, null );

	private final ErrorStatus noTopLevelError = this.createErrorStatus( "noTopLevelError" );

	private final org.lgna.croquet.PlainStringValue selectOneHeader = this.createStringValue( "selectOneHeader" );

	private final org.lgna.croquet.event.ValueListener<DifferentImplementationChoice> topLevelListener = new org.lgna.croquet.event.ValueListener<DifferentImplementationChoice>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationChoice> e ) {
			handleChanged();
		}
	};

	public DifferentImplementationHelpComposite( java.util.UUID migrationId, DifferentImplementation<M> differentImplementation, String signatureText, String implementationPluralText ) {
		super( migrationId, differentImplementation );
		StringBuilder sb = new StringBuilder();
		sb.append( "<html>" );
		sb.append( "Class file </filename/> contains a </kindOfMember/> <strong>\"</memberName/>\"</strong> which has " );
		sb.append( signatureText );
		sb.append( " to a </kindOfMember/> already in your project.<p><p>" );
		sb.append( "They have different " );
		sb.append( implementationPluralText );
		sb.append( " which leaves you with three options:" );
		sb.append( "<ol>" );
		sb.append( "<li><strong>add and retain both</strong> versions (note: renaming at least one will be required)" );
		sb.append( "<li>only <strong>add</strong> the version in </filename/>" );
		//sb.append( " (thereby replacing the version already in your project)" );
		sb.append( "<li>only <strong>retain</strong> the version already in your project" );
		//sb.append( " (thereby ignoring version in </filename/>)" );
		sb.append( "</ol>" );
		sb.append( "</html>" );

		String kindOfMemberText;
		M member = differentImplementation.getImportHub().getMember();
		if( member instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)member;
			kindOfMemberText = method.isProcedure() ? "procedure" : "function";
		} else {
			kindOfMemberText = "property";
		}
		String text = sb.toString();
		text = org.alice.ide.ast.type.merge.croquet.AddMembersPage.modifyFilenameLocalizedText( text, differentImplementation.getUriForDescriptionPurposesOnly() );
		text = text.replaceAll( "</kindOfMember/>", kindOfMemberText );
		text = text.replaceAll( "</memberName/>", member.getName() );
		this.getHeader().setText( text );
	}

	public org.lgna.croquet.PlainStringValue getSelectOneHeader() {
		return this.selectOneHeader;
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<DifferentImplementationChoice> getChoiceState() {
		return this.choiceState;
	}

	@Override
	protected org.alice.ide.ast.type.merge.help.diffimp.croquet.views.DifferentImplementationHelpView createView() {
		return new org.alice.ide.ast.type.merge.help.diffimp.croquet.views.DifferentImplementationHelpView( this );
	}

	@Override
	protected boolean isRetainBothSelected() {
		return this.choiceState.getValue() == DifferentImplementationChoice.ADD_AND_RETAIN_BOTH;
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep step ) {
		Status rv = super.getStatusPreRejectorCheck( step );
		if( rv == IS_GOOD_TO_GO_STATUS ) {
			DifferentImplementationChoice topLevelChoice = this.choiceState.getValue();
			if( topLevelChoice != null ) {
				//pass
			} else {
				rv = this.noTopLevelError;
			}
		}
		return rv;
	}

	@Override
	public void handlePreActivation() {
		boolean isImport = this.getPotentialNameChanger().getImportHub().getIsDesiredState().getValue();
		boolean isProject = this.getPotentialNameChanger().getProjectHub().getIsDesiredState().getValue();
		DifferentImplementationChoice topLevelChoice;
		if( isImport ) {
			if( isProject ) {
				topLevelChoice = DifferentImplementationChoice.ADD_AND_RETAIN_BOTH;
			} else {
				topLevelChoice = DifferentImplementationChoice.ONLY_ADD_VERSION_IN_CLASS_FILE;
			}
		} else {
			if( isProject ) {
				topLevelChoice = DifferentImplementationChoice.ONLY_RETAIN_VERSION_ALREADY_IN_PROJECT;
			} else {
				topLevelChoice = null;
			}
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
		DifferentImplementationChoice choice = this.choiceState.getValue();
		boolean isImport;
		boolean isKeep;
		if( choice == DifferentImplementationChoice.ADD_AND_RETAIN_BOTH ) {
			isImport = true;
			isKeep = true;
		} else if( choice == DifferentImplementationChoice.ONLY_ADD_VERSION_IN_CLASS_FILE ) {
			isImport = true;
			isKeep = false;
		} else if( choice == DifferentImplementationChoice.ONLY_RETAIN_VERSION_ALREADY_IN_PROJECT ) {
			isImport = false;
			isKeep = true;
		} else {
			isImport = false;
			isKeep = false;
		}
		this.getPotentialNameChanger().getImportHub().getIsDesiredState().setValueTransactionlessly( isImport );
		this.getPotentialNameChanger().getProjectHub().getIsDesiredState().setValueTransactionlessly( isKeep );
	}
}
