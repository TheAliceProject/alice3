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

package org.alice.ide.croquet.models.cascade.string;

/**
 * @author Dennis Cosgrove
 */
public class StringConcatinationLeftAndRightOperandsFillIn extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithExpressionBlanks<org.lgna.project.ast.StringConcatenation> {
	private static class SingletonHolder {
		private static StringConcatinationLeftAndRightOperandsFillIn instance = new StringConcatinationLeftAndRightOperandsFillIn();
	}

	public static StringConcatinationLeftAndRightOperandsFillIn getInstance() {
		return SingletonHolder.instance;
	}

	private final org.lgna.project.ast.StringConcatenation transientValue;

	private StringConcatinationLeftAndRightOperandsFillIn() {
		super( java.util.UUID.fromString( "58068432-cef0-4f9d-bfef-c841ea165172" ), org.alice.ide.croquet.models.cascade.ExpressionBlank.createBlanks( Object.class, Object.class ) );
		this.transientValue = org.alice.ide.ast.IncompleteAstUtilities.createIncompleteStringConcatenation();
	}

	@Override
	protected org.lgna.project.ast.StringConcatenation createValue( org.lgna.project.ast.Expression[] expressions ) {
		assert expressions.length == 2;
		return new org.lgna.project.ast.StringConcatenation( expressions[ 0 ], expressions[ 1 ] );
	}

	@Override
	public org.lgna.project.ast.StringConcatenation getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.StringConcatenation, org.lgna.project.ast.Expression> step ) {
		return this.transientValue;
	}
}
