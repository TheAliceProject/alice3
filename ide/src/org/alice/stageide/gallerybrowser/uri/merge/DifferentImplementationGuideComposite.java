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
package org.alice.stageide.gallerybrowser.uri.merge;

/**
 * @author Dennis Cosgrove
 */
public abstract class DifferentImplementationGuideComposite<M extends org.lgna.project.ast.Member> extends org.lgna.croquet.OperationInputDialogCoreComposite<org.alice.stageide.gallerybrowser.uri.merge.views.DifferentImplementationGuideView> {
	private final org.lgna.croquet.PlainStringValue header = this.createStringValue( this.createKey( "header" ) );
	private final org.lgna.croquet.StringState importNameState = this.createStringState( this.createKey( "importNameState" ) );
	private final org.lgna.croquet.StringState projectNameState = this.createStringState( this.createKey( "projectNameState" ) );
	private final org.lgna.croquet.ListSelectionState<DifferentImplementationTopLevelChoice> topLevelChoiceState = this.createListSelectionStateForEnum( this.createKey( "topLevelChoiceState" ), DifferentImplementationTopLevelChoice.class, null );
	private final org.lgna.croquet.ListSelectionState<DifferentImplementationSelectOne> selectOneState = this.createListSelectionStateForEnum( this.createKey( "selectOneState" ), DifferentImplementationSelectOne.class, null );

	private final ErrorStatus noTopLevelError = this.createErrorStatus( this.createKey( "noTopLevelError" ) );
	private final ErrorStatus noSelectOneError = this.createErrorStatus( this.createKey( "noSelectOneError" ) );

	private final edu.cmu.cs.dennisc.javax.swing.ColorCustomizer foregroundCustomizer = new edu.cmu.cs.dennisc.javax.swing.ColorCustomizer() {
		public java.awt.Color changeColorIfAppropriate( java.awt.Color defaultColor ) {
			return isRenameRequired() ? org.alice.stageide.gallerybrowser.uri.merge.views.MemberViewUtilities.ACTION_MUST_BE_TAKEN_COLOR : defaultColor;
		}
	};

	private final DifferentImplementation<M> differentImplementation;

	public DifferentImplementationGuideComposite( java.util.UUID migrationId, DifferentImplementation<M> differentImplementation ) {
		super( java.util.UUID.fromString( "fc03c28f-50b6-4d1c-b3f7-af1e609bc6b9" ), org.lgna.croquet.Application.INHERIT_GROUP );
		this.differentImplementation = differentImplementation;
	}

	public DifferentImplementation<M> getDifferentImplementation() {
		return this.differentImplementation;
	}

	public org.lgna.croquet.PlainStringValue getHeader() {
		return this.header;
	}

	public org.lgna.croquet.StringState getImportNameState() {
		return this.importNameState;
	}

	public org.lgna.croquet.StringState getProjectNameState() {
		return this.projectNameState;
	}

	public org.lgna.croquet.ListSelectionState<DifferentImplementationTopLevelChoice> getTopLevelChoiceState() {
		return this.topLevelChoiceState;
	}

	public org.lgna.croquet.ListSelectionState<DifferentImplementationSelectOne> getSelectOneState() {
		return this.selectOneState;
	}

	public edu.cmu.cs.dennisc.javax.swing.ColorCustomizer getForegroundCustomizer() {
		return this.foregroundCustomizer;
	}

	private boolean isRenameRequired() {
		//todo
		return this.getProjectNameState().getValue().contentEquals( this.getImportNameState().getValue() );
	}

	@Override
	protected org.alice.stageide.gallerybrowser.uri.merge.views.DifferentImplementationGuideView createView() {
		return new org.alice.stageide.gallerybrowser.uri.merge.views.DifferentImplementationGuideView( this );
	}

	@Override
	public void handlePreActivation() {
		boolean isImport = this.differentImplementation.getIsAddDesiredState().getValue();
		boolean isProject = this.differentImplementation.getIsKeepDesiredState().getValue();
		DifferentImplementationTopLevelChoice topLevelChoice;
		DifferentImplementationSelectOne selectOne;
		if( isImport ) {
			if( isProject ) {
				topLevelChoice = DifferentImplementationTopLevelChoice.KEEP_BOTH_AND_RENAME;
				selectOne = null;
			} else {
				topLevelChoice = DifferentImplementationTopLevelChoice.SELECT_ONE;
				selectOne = DifferentImplementationSelectOne.FROM_IMPORT;
			}
		} else {
			if( isProject ) {
				topLevelChoice = DifferentImplementationTopLevelChoice.SELECT_ONE;
				selectOne = DifferentImplementationSelectOne.ALREADY_IN_PROJECT;
			} else {
				topLevelChoice = null;
				selectOne = null;
			}
		}
		this.importNameState.setValueTransactionlessly( this.differentImplementation.getImportNameState().getValue() );
		this.projectNameState.setValueTransactionlessly( this.differentImplementation.getProjectNameState().getValue() );
		this.topLevelChoiceState.setValueTransactionlessly( topLevelChoice );
		this.selectOneState.setValueTransactionlessly( selectOne );
		super.handlePreActivation();
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep completionStep ) {
		DifferentImplementationTopLevelChoice topLevelChoice = this.topLevelChoiceState.getValue();
		boolean isImport;
		boolean isKeep;
		if( topLevelChoice == DifferentImplementationTopLevelChoice.KEEP_BOTH_AND_RENAME ) {
			isImport = true;
			isKeep = true;
			this.differentImplementation.getImportNameState().setValueTransactionlessly( this.importNameState.getValue() );
			this.differentImplementation.getProjectNameState().setValueTransactionlessly( this.projectNameState.getValue() );
		} else if( topLevelChoice == DifferentImplementationTopLevelChoice.SELECT_ONE ) {
			DifferentImplementationSelectOne selectOne = this.selectOneState.getValue();
			if( selectOne == DifferentImplementationSelectOne.FROM_IMPORT ) {
				isImport = true;
			} else if( selectOne == DifferentImplementationSelectOne.ALREADY_IN_PROJECT ) {
				isImport = false;
			} else {
				throw new org.lgna.croquet.CancelException();
			}
			isKeep = isImport == false;
		} else {
			throw new org.lgna.croquet.CancelException();
		}
		this.differentImplementation.getIsAddDesiredState().setValueTransactionlessly( isImport );
		this.differentImplementation.getIsKeepDesiredState().setValueTransactionlessly( isKeep );
		return null;
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep step ) {

		//todo
		this.getView().repaint();

		DifferentImplementationTopLevelChoice topLevelChoice = this.topLevelChoiceState.getValue();
		if( topLevelChoice == DifferentImplementationTopLevelChoice.KEEP_BOTH_AND_RENAME ) {
			return IS_GOOD_TO_GO_STATUS;
		} else if( topLevelChoice == DifferentImplementationTopLevelChoice.SELECT_ONE ) {
			DifferentImplementationSelectOne selectOne = this.selectOneState.getValue();
			if( selectOne != null ) {
				return IS_GOOD_TO_GO_STATUS;
			} else {
				return this.noSelectOneError;
			}
		} else {
			return this.noTopLevelError;
		}
	}
}
