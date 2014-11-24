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
	private final boolean isMultiple;
	private transient int count;

	public MoveStatementEdit( org.lgna.croquet.history.CompletionStep<org.alice.ide.ast.code.MoveStatementOperation> completionStep, org.alice.ide.ast.draganddrop.BlockStatementIndexPair fromLocation, org.lgna.project.ast.Statement statement, org.alice.ide.ast.draganddrop.BlockStatementIndexPair toLocation, boolean isMultiple ) {
		super( completionStep, statement );
		this.fromLocation = fromLocation;
		this.toLocation = toLocation;
		this.isMultiple = isMultiple;
	}

	public MoveStatementEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.fromLocation = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.toLocation = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.isMultiple = binaryDecoder.decodeBoolean();
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.fromLocation );
		binaryEncoder.encode( this.toLocation );
		binaryEncoder.encode( this.isMultiple );
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

	private static void move( org.lgna.project.ast.StatementListProperty remove, int removeIndex, org.lgna.project.ast.StatementListProperty add, int addIndex ) {
		org.lgna.project.ast.Statement statement = remove.get( removeIndex );
		remove.remove( removeIndex );
		add.add( addIndex, statement );
	}

	private int getCount() {
		if( this.isMultiple ) {
			return org.alice.ide.ast.code.ShiftDragStatementUtilities.calculateShiftMoveCount( fromLocation, toLocation );
		} else {
			return 1;
		}
	}

	@Override
	public void doOrRedoInternal( boolean isDo ) {
		int toDelta = this.getToDelta();
		org.lgna.project.ast.StatementListProperty from = this.fromLocation.getBlockStatement().statements;
		org.lgna.project.ast.StatementListProperty to = this.toLocation.getBlockStatement().statements;
		int fromIndex = this.fromLocation.getIndex();
		int toIndex = this.toLocation.getIndex() + toDelta;

		this.count = this.getCount();
		if( ( this.count > 1 ) && ( from == to ) ) {
			org.lgna.project.ast.StatementListProperty statementListProperty = from; // identical so it doesn't matter which we choose
			if( fromIndex > toIndex ) {
				java.util.List<org.lgna.project.ast.Statement> l = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( statementListProperty.subList( fromIndex, fromIndex + count ) );
				statementListProperty.removeExclusive( fromIndex, fromIndex + count );
				statementListProperty.addAll( toIndex, l );
			} else {
				throw new UnsupportedOperationException( fromIndex + " " + toIndex );
			}
		} else {
			for( int i = 0; i < this.count; i++ ) {
				move( from, fromIndex, to, toIndex + i );
			}
		}
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	public void undoInternal() {
		int toDelta = this.getToDelta();

		org.lgna.project.ast.StatementListProperty from = this.fromLocation.getBlockStatement().statements;
		org.lgna.project.ast.StatementListProperty to = this.toLocation.getBlockStatement().statements;
		int fromIndex = this.fromLocation.getIndex();
		int toIndex = this.toLocation.getIndex() + toDelta;

		if( ( this.count > 1 ) && ( from == to ) ) {
			org.lgna.project.ast.StatementListProperty statementListProperty = from; // identical so it doesn't matter which we choose
			if( fromIndex > toIndex ) {
				java.util.List<org.lgna.project.ast.Statement> l = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( statementListProperty.subList( toIndex, toIndex + count ) );
				statementListProperty.removeExclusive( toIndex, toIndex + count );
				statementListProperty.addAll( fromIndex, l );
			} else {
				throw new UnsupportedOperationException( fromIndex + " " + toIndex );
			}
		} else {
			for( int i = 0; i < this.count; i++ ) {
				move( to, toIndex, from, fromIndex + i );
			}
		}
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "move: " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, this.getStatement(), org.lgna.croquet.Application.getLocale() );
	}

	@Override
	public org.lgna.croquet.edits.ReplacementAcceptability getReplacementAcceptability( org.lgna.croquet.edits.Edit replacementCandidate ) {
		if( replacementCandidate instanceof MoveStatementEdit ) {
			return org.lgna.croquet.edits.ReplacementAcceptability.TO_BE_HONEST_I_DIDNT_EVEN_REALLY_CHECK;
		} else {
			return org.lgna.croquet.edits.ReplacementAcceptability.createRejection( "edit is not an instance of MoveStatementEdit" );
		}
	}
}
