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

package org.alice.ide.ast.code.edits;

/**
 * @author Dennis Cosgrove
 */
public class MoveStatementEdit extends org.alice.ide.croquet.edits.ast.StatementEdit<org.alice.ide.ast.code.MoveStatementOperation> {
	private org.alice.ide.ast.draganddrop.BlockStatementIndexPair fromLocation;
	private org.alice.ide.ast.draganddrop.BlockStatementIndexPair toLocation;

	public MoveStatementEdit( org.lgna.croquet.history.CompletionStep<org.alice.ide.ast.code.MoveStatementOperation> completionStep, org.alice.ide.ast.draganddrop.BlockStatementIndexPair fromLocation, org.lgna.project.ast.Statement statement, org.alice.ide.ast.draganddrop.BlockStatementIndexPair toLocation ) {
		super( completionStep, statement );
		this.fromLocation = fromLocation;
		this.toLocation = toLocation;
	}

	public MoveStatementEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.fromLocation = binaryDecoder.decodeBinaryEncodableAndDecodable();
		org.lgna.project.Project project = org.alice.ide.ProjectStack.peekProject();
		this.toLocation = binaryDecoder.decodeBinaryEncodableAndDecodable();
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.fromLocation );
		binaryEncoder.encode( this.toLocation );
	}

	private int getToDelta() {
		int toDelta;
		if( this.fromLocation.getBlockStatement() == this.toLocation.getBlockStatement() ) {
			if( this.fromLocation.getIndex() < this.toLocation.getIndex() ) {
				toDelta = -1;
			} else {
				toDelta = 0;
			}
		} else {
			toDelta = 0;
		}
		return toDelta;
	}

	@Override
	public void doOrRedoInternal( boolean isDo ) {
		int toDelta = this.getToDelta();
		this.fromLocation.getBlockStatement().statements.remove( this.fromLocation.getIndex() );
		this.toLocation.getBlockStatement().statements.add( this.toLocation.getIndex() + toDelta, this.getStatement() );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	public void undoInternal() {
		int toDelta = this.getToDelta();
		this.toLocation.getBlockStatement().statements.remove( this.toLocation.getIndex() + toDelta );
		this.fromLocation.getBlockStatement().statements.add( this.fromLocation.getIndex(), this.getStatement() );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "move: " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, this.getStatement(), org.lgna.croquet.Application.getLocale() );
	}

	@Override
	public void addKeyValuePairs( org.lgna.croquet.Retargeter retargeter, org.lgna.croquet.edits.Edit<?> edit ) {
		MoveStatementEdit replacementEdit = (MoveStatementEdit)edit;
		retargeter.addKeyValuePair( this.fromLocation, replacementEdit.fromLocation );
		retargeter.addKeyValuePair( this.toLocation, replacementEdit.toLocation );
	}

	@Override
	public void retarget( org.lgna.croquet.Retargeter retargeter ) {
		this.fromLocation = retargeter.retarget( this.fromLocation );
		this.toLocation = retargeter.retarget( this.toLocation );
	}

	@Override
	public org.lgna.croquet.edits.ReplacementAcceptability getReplacementAcceptability( org.lgna.croquet.edits.Edit<?> replacementCandidate ) {
		if( replacementCandidate instanceof MoveStatementEdit ) {
			return org.lgna.croquet.edits.ReplacementAcceptability.TO_BE_HONEST_I_DIDNT_EVEN_REALLY_CHECK;
		} else {
			return org.lgna.croquet.edits.ReplacementAcceptability.createRejection( "edit is not an instance of MoveStatementEdit" );
		}
	}
}
