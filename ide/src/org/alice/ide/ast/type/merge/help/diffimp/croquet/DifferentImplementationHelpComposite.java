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
package org.alice.ide.ast.type.merge.help.diffimp.croquet;

import org.alice.ide.ast.type.merge.croquet.DifferentImplementation;
import org.alice.ide.ast.type.merge.help.croquet.PotentialNameChangerHelpComposite;

/**
 * @author Dennis Cosgrove
 */
public abstract class DifferentImplementationHelpComposite<M extends org.lgna.project.ast.Member> extends PotentialNameChangerHelpComposite<org.alice.ide.ast.type.merge.help.diffimp.croquet.views.DifferentImplementationHelpView, M, DifferentImplementation<M>> {
	private final org.lgna.croquet.ListSelectionState<DifferentImplementationTopLevelChoice> topLevelChoiceState = this.createListSelectionStateForEnum( this.createKey( "topLevelChoiceState" ), DifferentImplementationTopLevelChoice.class, null );
	private final org.lgna.croquet.ListSelectionState<DifferentImplementationSelectOne> selectOneState = this.createListSelectionStateForEnum( this.createKey( "selectOneState" ), DifferentImplementationSelectOne.class, null );

	private final ErrorStatus noTopLevelError = this.createErrorStatus( this.createKey( "noTopLevelError" ) );
	private final ErrorStatus noSelectOneError = this.createErrorStatus( this.createKey( "noSelectOneError" ) );

	private final org.lgna.croquet.State.ValueListener<DifferentImplementationTopLevelChoice> topLevelListener = new org.lgna.croquet.State.ValueListener<DifferentImplementationTopLevelChoice>() {
		public void changing( org.lgna.croquet.State<DifferentImplementationTopLevelChoice> state, DifferentImplementationTopLevelChoice prevValue, DifferentImplementationTopLevelChoice nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<DifferentImplementationTopLevelChoice> state, DifferentImplementationTopLevelChoice prevValue, DifferentImplementationTopLevelChoice nextValue, boolean isAdjusting ) {
			handleChanged();
		}
	};

	private final org.lgna.croquet.State.ValueListener<DifferentImplementationSelectOne> selectOneListener = new org.lgna.croquet.State.ValueListener<DifferentImplementationSelectOne>() {
		public void changing( org.lgna.croquet.State<DifferentImplementationSelectOne> state, DifferentImplementationSelectOne prevValue, DifferentImplementationSelectOne nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<DifferentImplementationSelectOne> state, DifferentImplementationSelectOne prevValue, DifferentImplementationSelectOne nextValue, boolean isAdjusting ) {
			handleChanged();
		}
	};

	public DifferentImplementationHelpComposite( java.util.UUID migrationId, DifferentImplementation<M> differentImplementation ) {
		super( migrationId, differentImplementation );
	}

	public org.lgna.croquet.ListSelectionState<DifferentImplementationTopLevelChoice> getTopLevelChoiceState() {
		return this.topLevelChoiceState;
	}

	public org.lgna.croquet.ListSelectionState<DifferentImplementationSelectOne> getSelectOneState() {
		return this.selectOneState;
	}

	@Override
	protected org.alice.ide.ast.type.merge.help.diffimp.croquet.views.DifferentImplementationHelpView createView() {
		return new org.alice.ide.ast.type.merge.help.diffimp.croquet.views.DifferentImplementationHelpView( this );
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

	@Override
	public void handlePreActivation() {
		boolean isImport = this.getPotentialNameChanger().getImportHub().getIsDesiredState().getValue();
		boolean isProject = this.getPotentialNameChanger().getProjectHub().getIsDesiredState().getValue();
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
		//		this.getImportNameState().setValueTransactionlessly( this.getPotentialNameChanger().getImportHub().getNameState().getValue() );
		//		this.getProjectNameState().setValueTransactionlessly( this.getPotentialNameChanger().getProjectHub().getNameState().getValue() );
		this.topLevelChoiceState.setValueTransactionlessly( topLevelChoice );
		this.selectOneState.setValueTransactionlessly( selectOne );
		this.topLevelChoiceState.addValueListener( this.topLevelListener );
		this.selectOneState.addValueListener( this.selectOneListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		this.selectOneState.removeValueListener( this.selectOneListener );
		this.topLevelChoiceState.removeValueListener( this.topLevelListener );
	}

	private void handleChanged() {
		DifferentImplementationTopLevelChoice topLevelChoice = this.topLevelChoiceState.getValue();
		boolean isImport;
		boolean isKeep;
		if( topLevelChoice == DifferentImplementationTopLevelChoice.KEEP_BOTH_AND_RENAME ) {
			isImport = true;
			isKeep = true;
		} else if( topLevelChoice == DifferentImplementationTopLevelChoice.SELECT_ONE ) {
			DifferentImplementationSelectOne selectOne = this.selectOneState.getValue();
			if( selectOne == DifferentImplementationSelectOne.FROM_IMPORT ) {
				isImport = true;
				isKeep = false;
			} else if( selectOne == DifferentImplementationSelectOne.ALREADY_IN_PROJECT ) {
				isImport = false;
				isKeep = true;
			} else {
				isImport = false;
				isKeep = false;
			}
		} else if( topLevelChoice == DifferentImplementationTopLevelChoice.SELECT_IMPORT ) {
			isImport = true;
			isKeep = false;
		} else if( topLevelChoice == DifferentImplementationTopLevelChoice.SELECT_PROJECT ) {
			isImport = false;
			isKeep = true;
		} else {
			isImport = false;
			isKeep = false;
		}
		this.getPotentialNameChanger().getImportHub().getIsDesiredState().setValueTransactionlessly( isImport );
		this.getPotentialNameChanger().getProjectHub().getIsDesiredState().setValueTransactionlessly( isKeep );
	}

	//	@Override
	//	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep completionStep ) {
	//		DifferentImplementationTopLevelChoice topLevelChoice = this.topLevelChoiceState.getValue();
	//		boolean isImport;
	//		boolean isKeep;
	//		if( topLevelChoice == DifferentImplementationTopLevelChoice.KEEP_BOTH_AND_RENAME ) {
	//			isImport = true;
	//			isKeep = true;
	//			this.getPotentialNameChanger().getImportHub().getNameState().setValueTransactionlessly( this.getImportNameState().getValue() );
	//			this.getPotentialNameChanger().getProjectHub().getNameState().setValueTransactionlessly( this.getProjectNameState().getValue() );
	//		} else if( topLevelChoice == DifferentImplementationTopLevelChoice.SELECT_ONE ) {
	//			DifferentImplementationSelectOne selectOne = this.selectOneState.getValue();
	//			if( selectOne == DifferentImplementationSelectOne.FROM_IMPORT ) {
	//				isImport = true;
	//			} else if( selectOne == DifferentImplementationSelectOne.ALREADY_IN_PROJECT ) {
	//				isImport = false;
	//			} else {
	//				throw new org.lgna.croquet.CancelException();
	//			}
	//			isKeep = isImport == false;
	//		} else {
	//			throw new org.lgna.croquet.CancelException();
	//		}
	//		this.getPotentialNameChanger().getImportHub().getIsDesiredState().setValueTransactionlessly( isImport );
	//		this.getPotentialNameChanger().getProjectHub().getIsDesiredState().setValueTransactionlessly( isKeep );
	//		return null;
	//	}
}
