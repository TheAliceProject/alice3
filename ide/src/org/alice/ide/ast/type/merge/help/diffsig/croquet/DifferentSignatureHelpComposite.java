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
	private final org.lgna.croquet.ListSelectionState<DifferentSignatureTopLevelChoice> topLevelChoiceState = this.createListSelectionStateForEnum( this.createKey( "topLevelChoiceState" ), DifferentSignatureTopLevelChoice.class, null );

	private final ErrorStatus noTopLevelError = this.createErrorStatus( this.createKey( "noTopLevelError" ) );

	private final edu.cmu.cs.dennisc.javax.swing.ColorCustomizer foregroundCustomizer = new edu.cmu.cs.dennisc.javax.swing.ColorCustomizer() {
		public java.awt.Color changeColorIfAppropriate( java.awt.Color defaultColor ) {
			return isRenameRequired() ? org.alice.ide.ast.type.merge.croquet.views.MemberViewUtilities.ACTION_MUST_BE_TAKEN_COLOR : defaultColor;
		}
	};

	public DifferentSignatureHelpComposite( java.util.UUID migrationId, DifferentSignature<M> differentSignature ) {
		super( migrationId, differentSignature );
	}

	public org.lgna.croquet.ListSelectionState<DifferentSignatureTopLevelChoice> getTopLevelChoiceState() {
		return this.topLevelChoiceState;
	}

	public edu.cmu.cs.dennisc.javax.swing.ColorCustomizer getForegroundCustomizer() {
		return this.foregroundCustomizer;
	}

	private boolean isRenameRequired() {
		//todo
		return this.getProjectNameState().getValue().contentEquals( this.getImportNameState().getValue() );
	}

	@Override
	protected org.alice.ide.ast.type.merge.help.diffsig.croquet.views.DifferentSignatureHelpView createView() {
		return new org.alice.ide.ast.type.merge.help.diffsig.croquet.views.DifferentSignatureHelpView( this );
	}

	@Override
	public void handlePreActivation() {
		boolean isImport = this.getPotentialNameChanger().getImportHub().getIsDesiredState().getValue();
		DifferentSignatureTopLevelChoice topLevelChoice;
		if( isImport ) {
			topLevelChoice = DifferentSignatureTopLevelChoice.KEEP_BOTH_AND_RENAME;
		} else {
			topLevelChoice = DifferentSignatureTopLevelChoice.KEEP_ORIGINAL_IN_PROJECT;
		}
		this.getImportNameState().setValueTransactionlessly( this.getPotentialNameChanger().getImportHub().getNameState().getValue() );
		this.getProjectNameState().setValueTransactionlessly( this.getPotentialNameChanger().getProjectHub().getNameState().getValue() );
		this.topLevelChoiceState.setValueTransactionlessly( topLevelChoice );
		super.handlePreActivation();
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep completionStep ) {
		DifferentSignatureTopLevelChoice topLevelChoice = this.topLevelChoiceState.getValue();
		boolean isImport;
		if( topLevelChoice == DifferentSignatureTopLevelChoice.KEEP_BOTH_AND_RENAME ) {
			isImport = true;
			this.getPotentialNameChanger().getImportHub().getNameState().setValueTransactionlessly( this.getImportNameState().getValue() );
			this.getPotentialNameChanger().getProjectHub().getNameState().setValueTransactionlessly( this.getProjectNameState().getValue() );
		} else if( topLevelChoice == DifferentSignatureTopLevelChoice.KEEP_ORIGINAL_IN_PROJECT ) {
			isImport = false;
		} else {
			throw new org.lgna.croquet.CancelException();
		}
		this.getPotentialNameChanger().getImportHub().getIsDesiredState().setValueTransactionlessly( isImport );
		return null;
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep step ) {

		//todo
		this.getView().repaint();

		DifferentSignatureTopLevelChoice topLevelChoice = this.topLevelChoiceState.getValue();
		if( topLevelChoice != null ) {
			return IS_GOOD_TO_GO_STATUS;
		} else {
			return this.noTopLevelError;
		}
	}
}
