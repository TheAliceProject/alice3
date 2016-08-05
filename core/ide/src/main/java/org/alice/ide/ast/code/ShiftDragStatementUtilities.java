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
package org.alice.ide.ast.code;

/**
 * @author Dennis Cosgrove
 */
public class ShiftDragStatementUtilities {
	private ShiftDragStatementUtilities() {
		throw new AssertionError();
	}

	private static boolean isEqualToOrAncestor( org.lgna.project.ast.Statement fromI, org.lgna.project.ast.Node toBlock ) {
		if( toBlock instanceof org.lgna.project.ast.Statement ) {
			if( fromI == toBlock ) {
				return true;
			} else {
				return isEqualToOrAncestor( fromI, toBlock.getParent() );
			}
		} else {
			return false;
		}
	}

	public static int calculateShiftMoveCount( org.alice.ide.ast.draganddrop.BlockStatementIndexPair fromLocation, org.alice.ide.ast.draganddrop.BlockStatementIndexPair toLocation ) {
		org.lgna.project.ast.BlockStatement fromBlock = fromLocation.getBlockStatement();
		org.lgna.project.ast.BlockStatement toBlock = toLocation.getBlockStatement();
		if( fromBlock == toBlock ) {
			int fromIndex = fromLocation.getIndex();
			int toIndex = toLocation.getIndex();
			if( fromIndex > toIndex ) {
				return fromBlock.statements.size() - fromIndex;
			} else {
				return 0;
			}
		} else {
			int count = 0;
			for( int i = fromLocation.getIndex(); i < fromBlock.statements.size(); i++ ) {
				org.lgna.project.ast.Statement fromI = fromBlock.statements.get( i );
				if( isEqualToOrAncestor( fromI, toBlock ) ) {
					break;
				} else {
					count++;
				}
			}
			return count;
		}
	}

	public static boolean isCandidateForEnvelop( org.lgna.croquet.DragModel dragModel ) {
		if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.PotentiallyEnvelopingStatementTemplateDragModel ) {
			//org.alice.ide.ast.draganddrop.statement.PotentiallyEnvelopingStatementTemplateDragModel potentiallyEnvelopingStatementTemplateDragModel = (org.alice.ide.ast.draganddrop.statement.PotentiallyEnvelopingStatementTemplateDragModel)dragModel;
			return true;
		} else if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.StatementDragModel ) {
			final boolean IS_READY_FOR_PRIME_TIME = false;
			if( IS_READY_FOR_PRIME_TIME ) {
				org.alice.ide.ast.draganddrop.statement.StatementDragModel statementDragModel = (org.alice.ide.ast.draganddrop.statement.StatementDragModel)dragModel;
				org.lgna.project.ast.Statement statement = statementDragModel.getStatement();
				if( statement instanceof org.lgna.project.ast.AbstractStatementWithBody ) {
					org.lgna.project.ast.AbstractStatementWithBody statementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)statement;
					return statementWithBody.body.getValue().statements.size() == 0;
				} else if( statement instanceof org.lgna.project.ast.ConditionalStatement ) {
					org.lgna.project.ast.ConditionalStatement conditionalStatement = (org.lgna.project.ast.ConditionalStatement)statement;
					if( conditionalStatement.elseBody.getValue().statements.size() == 0 ) {
						for( org.lgna.project.ast.BooleanExpressionBodyPair booleanExpressionBodyPair : conditionalStatement.booleanExpressionBodyPairs ) {
							if( booleanExpressionBodyPair.body.getValue().statements.size() == 0 ) {
								//pass
							} else {
								return false;
							}
						}
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
