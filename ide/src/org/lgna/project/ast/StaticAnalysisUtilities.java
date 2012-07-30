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
package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public class StaticAnalysisUtilities {
	private static boolean isEnabledNonCommment( Statement statement ) {
		if( statement instanceof Comment ) {
			return false;
		} else {
			if( statement.isEnabled.getValue() ) {
				return true;
			} else {
				return false;
			}
		}
	}
	private static int getIndexOfLastEnabledNonComment( BlockStatement blockStatement ) {
		final int N = blockStatement.statements.size();
		for( int i=N-1; i>=0; i-- ) {
			org.lgna.project.ast.Statement statement = blockStatement.statements.get( i );
			if( isEnabledNonCommment( statement ) ) {
				return i;
			}
		}
		return -1;
	}
	
	private static boolean containsUnreachableCode( BlockStatement blockStatement ) {
		//todo: a lot more work could go into this function
		//is a while loop false?
		//is a count loop 0?
		//is a for each loop empty?
		final int lastIndex = getIndexOfLastEnabledNonComment( blockStatement );
		for( int i=0; i<=lastIndex; i++ ) {
			boolean isLastIndex = i == lastIndex;
			org.lgna.project.ast.Statement statement = blockStatement.statements.get( i );
			if( isEnabledNonCommment( statement ) ) {
				if( statement instanceof ReturnStatement ) {
					if( isLastIndex ) {
						//pass
					} else {
						return true;
					}
				} else if( statement instanceof AbstractStatementWithBody ) {
					AbstractStatementWithBody abstractStatementWithBody = (AbstractStatementWithBody)statement;
					if( containsUnreachableCode( abstractStatementWithBody.body.getValue() ) ) {
						return true;
					}
				} else if( statement instanceof ConditionalStatement ) {
					ConditionalStatement conditionalStatement = (ConditionalStatement)statement;
					for( BooleanExpressionBodyPair booleanExpressionBodyPair : conditionalStatement.booleanExpressionBodyPairs ) {
						if( containsUnreachableCode( booleanExpressionBodyPair.body.getValue() ) ) {
							return true;
						}
					}
					if( containsUnreachableCode( conditionalStatement.elseBody.getValue() ) ) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public static boolean containsUnreachableCode( UserMethod method ) {
		return containsUnreachableCode( method.body.getValue() );
	}
	
	private static boolean containsAtLeastOneEnabledReturnStatement( BlockStatement blockStatement ) {
		for( Statement statement : blockStatement.statements ) {
			if( statement.isEnabled.getValue() ) {
				if( statement instanceof ReturnStatement ) {
					return true;
				} else if( statement instanceof AbstractStatementWithBody ) {
					AbstractStatementWithBody abstractStatementWithBody = (AbstractStatementWithBody)statement;
					if( containsAtLeastOneEnabledReturnStatement( abstractStatementWithBody.body.getValue() ) ) {
						return true;
					}
				} else if( statement instanceof ConditionalStatement ) {
					ConditionalStatement conditionalStatement = (ConditionalStatement)statement;
					for( BooleanExpressionBodyPair booleanExpressionBodyPair : conditionalStatement.booleanExpressionBodyPairs ) {
						if( containsAtLeastOneEnabledReturnStatement( booleanExpressionBodyPair.body.getValue() ) ) {
							return true;
						}
					}
					if( containsAtLeastOneEnabledReturnStatement( conditionalStatement.elseBody.getValue() ) ) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public static boolean containsAtLeastOneEnabledReturnStatement( UserMethod method ) {
		return containsAtLeastOneEnabledReturnStatement( method.body.getValue() );
	}
	
	private static boolean containsAReturnForEveryPath( BlockStatement blockStatement ) {
		final int lastIndex = getIndexOfLastEnabledNonComment( blockStatement );
		if( lastIndex >= 0 ) {
			Statement statement = blockStatement.statements.get( lastIndex );
			if( statement instanceof ReturnStatement ) {
				return true;
			} else if( statement instanceof ConditionalStatement ) {
				ConditionalStatement conditionalStatement = (ConditionalStatement)statement;
				for( BooleanExpressionBodyPair booleanExpressionBodyPair : conditionalStatement.booleanExpressionBodyPairs ) {
					if( containsAReturnForEveryPath( booleanExpressionBodyPair.body.getValue() ) ) {
						//
					} else {
						return false;
					}
				}
				return containsAReturnForEveryPath( conditionalStatement.elseBody.getValue() );
			}
		}
		return false;
	}
	public static boolean containsAReturnForEveryPath( UserMethod method ) {
		return containsAReturnForEveryPath( method.body.getValue() );
	}

}
