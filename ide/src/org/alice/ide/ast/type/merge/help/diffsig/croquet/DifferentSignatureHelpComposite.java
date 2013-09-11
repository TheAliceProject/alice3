/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.ast.type.merge.help.diffsig.croquet;

import org.alice.ide.ast.type.merge.croquet.DifferentSignature;
import org.alice.ide.ast.type.merge.help.croquet.PotentialNameChangerHelpComposite;

/**
 * @author Dennis Cosgrove
 */
public abstract class DifferentSignatureHelpComposite<M extends org.lgna.project.ast.Member> extends PotentialNameChangerHelpComposite<org.alice.ide.ast.type.merge.help.diffsig.croquet.views.DifferentSignatureHelpView, M, DifferentSignature<M>> {
	private final org.lgna.croquet.ListSelectionState<DifferentSignatureChoice> topLevelChoiceState = this.createListSelectionStateForEnum( this.createKey( "topLevelChoiceState" ), DifferentSignatureChoice.class, null );

	private final org.lgna.croquet.State.ValueListener<DifferentSignatureChoice> topLevelListener = new org.lgna.croquet.State.ValueListener<DifferentSignatureChoice>() {
		public void changing( org.lgna.croquet.State<DifferentSignatureChoice> state, DifferentSignatureChoice prevValue, DifferentSignatureChoice nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<DifferentSignatureChoice> state, DifferentSignatureChoice prevValue, DifferentSignatureChoice nextValue, boolean isAdjusting ) {
			handleChanged();
		}
	};

	public DifferentSignatureHelpComposite( java.util.UUID migrationId, DifferentSignature<M> differentSignature ) {
		super( migrationId, differentSignature );
		StringBuilder sb = new StringBuilder();
		sb.append( "<html>" );

		sb.append( "Class file </filename/> contains a </kindOfMember/> \"</memberName/>\" which " );
		sb.append( "<strong>TODO</strong>" );
		sb.append( " to a </kindOfMember/> already in your project.<p><p>" );
		sb.append( "You have two options:" );
		sb.append( "<ol>" );
		sb.append( "<li><strong>add and retain both versions</strong>  note: renaming at least one will be required" );
		sb.append( "<li><strong>retain only the version already in your project</strong>" );
		sb.append( "</ol>" );
		sb.append( "</html>" );

		org.lgna.project.ast.Member member = differentSignature.getImportHub().getMember();
		String kindOfMemberText = "TODO_member";
		String text = sb.toString();
		text = org.alice.ide.ast.type.merge.croquet.AddMembersPage.modifyFilenameLocalizedText( text, differentSignature.getUriForDescriptionPurposesOnly() );
		text = text.replaceAll( "</kindOfMember/>", kindOfMemberText );
		text = text.replaceAll( "</memberName/>", member.getName() );
		this.getHeader().setText( text );
	}

	public org.lgna.croquet.ListSelectionState<DifferentSignatureChoice> getTopLevelChoiceState() {
		return this.topLevelChoiceState;
	}

	@Override
	protected boolean isRetainBothSelected() {
		return this.topLevelChoiceState.getValue() == DifferentSignatureChoice.RETAIN_BOTH_AND_RENAME;
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
			topLevelChoice = DifferentSignatureChoice.RETAIN_BOTH_AND_RENAME;
		} else {
			topLevelChoice = DifferentSignatureChoice.RETAIN_VERSION_ALREADY_IN_PROJECT;
		}
		this.topLevelChoiceState.setValueTransactionlessly( topLevelChoice );
		this.topLevelChoiceState.addValueListener( this.topLevelListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		this.topLevelChoiceState.removeValueListener( this.topLevelListener );
	}

	private void handleChanged() {
		DifferentSignatureChoice topLevelChoice = this.topLevelChoiceState.getValue();
		boolean isImport;
		if( topLevelChoice == DifferentSignatureChoice.RETAIN_BOTH_AND_RENAME ) {
			isImport = true;
		} else if( topLevelChoice == DifferentSignatureChoice.RETAIN_VERSION_ALREADY_IN_PROJECT ) {
			isImport = false;
		} else {
			//should never happen
			isImport = false;
		}
		this.getPotentialNameChanger().getImportHub().getIsDesiredState().setValueTransactionlessly( isImport );
	}
}
