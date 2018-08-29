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
package org.alice.ide.croquet.edits.ast;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Objects;
import org.alice.ide.IDE;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.croquet.models.ast.InsertStatementCompletionModel;
import org.alice.ide.project.ProjectChangeOfInterestManager;
import org.lgna.croquet.Application;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractStatementWithBody;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.ConditionalStatement;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.NodeUtilities;
import org.lgna.project.ast.Statement;

import javax.swing.undo.CannotUndoException;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class InsertStatementEdit<M extends InsertStatementCompletionModel> extends StatementEdit<M> {
	public static final int AT_END = Short.MAX_VALUE;
	private BlockStatement blockStatement;
	private int specifiedIndex;
	private Expression[] initialExpressions;
	private final boolean isEnveloping;

	public InsertStatementEdit( UserActivity userActivity, BlockStatementIndexPair blockStatementIndexPair, Statement statement, Expression[] initialExpressions, boolean isEnveloping ) {
		super( userActivity, statement );
		BlockStatementIndexPair fromHistoryBlockStatementIndexPair = this.findFirstDropSite( BlockStatementIndexPair.class );
		if( Objects.equals( blockStatementIndexPair, fromHistoryBlockStatementIndexPair ) ) {
			//pass
		} else {
			//edu.cmu.cs.dennisc.java.util.logging.Logger.severe( blockStatementIndexPair, fromHistoryBlockStatementIndexPair );
		}
		this.blockStatement = blockStatementIndexPair.getBlockStatement();
		this.specifiedIndex = blockStatementIndexPair.getIndex();
		this.initialExpressions = initialExpressions;
		this.isEnveloping = isEnveloping;
	}

	public InsertStatementEdit( UserActivity userActivity, BlockStatementIndexPair blockStatementIndexPair, Statement statement, Expression[] initialExpressions ) {
		this( userActivity, blockStatementIndexPair, statement, initialExpressions, false );
	}

	public InsertStatementEdit( UserActivity userActivity, BlockStatementIndexPair blockStatementIndexPair, Statement statement ) {
		this( userActivity, blockStatementIndexPair, statement, new Expression[] {} );
	}

	public InsertStatementEdit( BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		IDE ide = IDE.getActiveInstance();
		Project project = ide.getProject();
		UUID blockStatementId = binaryDecoder.decodeId();
		this.blockStatement = ProgramTypeUtilities.lookupNode( project, blockStatementId );
		this.specifiedIndex = binaryDecoder.decodeInt();
		UUID[] ids = binaryDecoder.decodeIdArray();
		final int N = ids.length;
		this.initialExpressions = new Expression[ N ];
		for( int i = 0; i < N; i++ ) {
			this.initialExpressions[ i ] = ProgramTypeUtilities.lookupNode( project, ids[ i ] );
		}
		this.isEnveloping = binaryDecoder.decodeBoolean();
	}

	@Override
	public void encode( BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.blockStatement.getId() );
		binaryEncoder.encode( this.specifiedIndex );
		final int N = this.initialExpressions.length;
		UUID[] ids = new UUID[ N ];
		for( int i = 0; i < N; i++ ) {
			ids[ i ] = this.initialExpressions[ i ].getId();
		}
		binaryEncoder.encode( ids );
		binaryEncoder.encode( this.isEnveloping );
	}

	public BlockStatement getBlockStatement() {
		return this.blockStatement;
	}

	public int getSpecifiedIndex() {
		return this.specifiedIndex;
	}

	public Expression[] getInitialExpressions() {
		return this.initialExpressions;
	}

	private int getActualIndex() {
		return Math.min( this.specifiedIndex, this.blockStatement.statements.size() );
	}

	private static BlockStatement getDst( Statement statement ) {
		if( statement instanceof AbstractStatementWithBody ) {
			AbstractStatementWithBody statementWithBody = (AbstractStatementWithBody)statement;
			return statementWithBody.body.getValue();
		} else if( statement instanceof ConditionalStatement ) {
			ConditionalStatement conditionalStatement = (ConditionalStatement)statement;
			return conditionalStatement.booleanExpressionBodyPairs.get( 0 ).body.getValue();
		} else {
			return null;
		}
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		Statement statement = this.getStatement();
		int actualIndex = this.getActualIndex();
		if( this.isEnveloping ) {
			BlockStatement dst = getDst( statement );
			while( this.blockStatement.statements.size() > actualIndex ) {
				Statement s = this.blockStatement.statements.get( actualIndex );
				this.blockStatement.statements.remove( actualIndex );
				dst.statements.add( s );
			}
		}
		this.blockStatement.statements.add( actualIndex, statement );
		//todo: remove
		ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	protected final void undoInternal() {
		Statement statement = this.getStatement();
		int actualIndex = this.getActualIndex();
		if( this.blockStatement.statements.get( actualIndex ) == statement ) {
			this.blockStatement.statements.remove( actualIndex );
			if( this.isEnveloping ) {
				BlockStatement dst = getDst( statement );
				while( dst.statements.size() > 0 ) {
					Statement s = dst.statements.get( 0 );
					dst.statements.remove( 0 );
					this.blockStatement.statements.add( s );
				}
			}
			//todo: remove
			ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
		} else {
			throw new CannotUndoException();
		}
	}

	//	@Override
	//	public edu.cmu.cs.dennisc.croquet.Edit< edu.cmu.cs.dennisc.croquet.ActionOperation > getAcceptableReplacement( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
	//		org.lgna.project.ast.BlockStatement replacementBlockStatement = retargeter.retarget( this.blockStatement );
	//		org.lgna.project.ast.Statement replacementStatement = retargeter.retarget( this.statement );
	//		return new InsertStatementEdit( replacementBlockStatement, this.index, replacementStatement );
	//	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		Statement statement = this.getStatement();
		rv.append( "insert: " );
		NodeUtilities.safeAppendRepr( rv, statement, Application.getLocale() );
	}
}
