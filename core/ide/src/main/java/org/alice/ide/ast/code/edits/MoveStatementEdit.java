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

package org.alice.ide.ast.code.edits;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.ide.ast.code.MoveStatementOperation;
import org.alice.ide.ast.code.ShiftDragStatementUtilities;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.croquet.edits.ast.StatementEdit;
import org.alice.ide.project.ProjectChangeOfInterestManager;
import org.lgna.croquet.Application;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.NodeUtilities;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class MoveStatementEdit extends StatementEdit<MoveStatementOperation> {
	private BlockStatementIndexPair fromLocation;
	private BlockStatementIndexPair toLocation;
	private final boolean isMultiple;
	private transient int count;

	public MoveStatementEdit( CompletionStep<MoveStatementOperation> completionStep, BlockStatementIndexPair fromLocation, Statement statement, BlockStatementIndexPair toLocation, boolean isMultiple ) {
		super( completionStep, statement );
		this.fromLocation = fromLocation;
		this.toLocation = toLocation;
		this.isMultiple = isMultiple;
	}

	public MoveStatementEdit( BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.fromLocation = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.toLocation = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.isMultiple = binaryDecoder.decodeBoolean();
	}

	@Override
	public void encode( BinaryEncoder binaryEncoder ) {
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

	private static void move( StatementListProperty remove, int removeIndex, StatementListProperty add, int addIndex ) {
		Statement statement = remove.get( removeIndex );
		remove.remove( removeIndex );
		add.add( addIndex, statement );
	}

	private int getCount() {
		if( this.isMultiple ) {
			return ShiftDragStatementUtilities.calculateShiftMoveCount( fromLocation, toLocation );
		} else {
			return 1;
		}
	}

	@Override
	public void doOrRedoInternal( boolean isDo ) {
		int toDelta = this.getToDelta();
		StatementListProperty from = this.fromLocation.getBlockStatement().statements;
		StatementListProperty to = this.toLocation.getBlockStatement().statements;
		int fromIndex = this.fromLocation.getIndex();
		int toIndex = this.toLocation.getIndex() + toDelta;

		this.count = this.getCount();
		if( ( this.count > 1 ) && ( from == to ) ) {
			StatementListProperty statementListProperty = from; // identical so it doesn't matter which we choose
			if( fromIndex > toIndex ) {
				List<Statement> l = Lists.newArrayList( statementListProperty.subList( fromIndex, fromIndex + count ) );
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
		ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	public void undoInternal() {
		int toDelta = this.getToDelta();

		StatementListProperty from = this.fromLocation.getBlockStatement().statements;
		StatementListProperty to = this.toLocation.getBlockStatement().statements;
		int fromIndex = this.fromLocation.getIndex();
		int toIndex = this.toLocation.getIndex() + toDelta;

		if( ( this.count > 1 ) && ( from == to ) ) {
			StatementListProperty statementListProperty = from; // identical so it doesn't matter which we choose
			if( fromIndex > toIndex ) {
				List<Statement> l = Lists.newArrayList( statementListProperty.subList( toIndex, toIndex + count ) );
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
		ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "move: " );
		NodeUtilities.safeAppendRepr( rv, this.getStatement(), Application.getLocale() );
	}
}
