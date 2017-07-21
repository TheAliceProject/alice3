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

package org.alice.ide.croquet.models.ast.cascade.statement;

/**
 * @author Dennis Cosgrove
 */
public class ReturnStatementInsertCascade extends StatementInsertCascade {
	private static org.lgna.project.ast.AbstractType<?, ?, ?> getReturnType( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		org.lgna.project.ast.AbstractCode code = blockStatementIndexPair.getBlockStatement().getFirstAncestorAssignableTo( org.lgna.project.ast.AbstractCode.class );
		if( code instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)code;
			if( method.isFunction() ) {
				return method.returnType.getValue();
			}
		}
		return null;
	}

	private static java.util.Map<org.alice.ide.ast.draganddrop.BlockStatementIndexPair, ReturnStatementInsertCascade> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized ReturnStatementInsertCascade getInstance( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		assert blockStatementIndexPair != null;
		ReturnStatementInsertCascade rv = map.get( blockStatementIndexPair );
		if( rv != null ) {
			//pass
		} else {
			rv = new ReturnStatementInsertCascade( blockStatementIndexPair );
			map.put( blockStatementIndexPair, rv );
		}
		return rv;
	}

	private ReturnStatementInsertCascade( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		super( java.util.UUID.fromString( "6b1dae07-066f-4250-92e8-db1eacd32801" ), blockStatementIndexPair, false, org.alice.ide.croquet.models.cascade.ExpressionBlank.createBlanks( getReturnType( blockStatementIndexPair ) ) );
	}

	@Override
	protected org.lgna.project.ast.Statement createStatement( org.lgna.project.ast.Expression... expressions ) {
		return org.lgna.project.ast.AstUtilities.createReturnStatement( getReturnType( this.getBlockStatementIndexPair() ), expressions[ 0 ] );
	}
}
