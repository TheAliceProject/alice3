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

package org.alice.ide.croquet.models.cascade.literals;

/**
 * @author Dennis Cosgrove
 */
public class IntegerLiteralFillIn extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks<org.lgna.project.ast.IntegerLiteral> {
	private static java.util.Map<Integer, IntegerLiteralFillIn> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static IntegerLiteralFillIn getInstance( int value ) {
		synchronized( map ) {
			IntegerLiteralFillIn rv = map.get( value );
			if( rv != null ) {
				//pass
			} else {
				rv = new IntegerLiteralFillIn( value );
				map.put( value, rv );
			}
			return rv;
		}
	}

	private final org.lgna.project.ast.IntegerLiteral transientValue;

	private IntegerLiteralFillIn( int value ) {
		super( java.util.UUID.fromString( "edc6ae8d-6fb9-4678-b144-71b3e5c65300" ) );
		this.transientValue = new org.lgna.project.ast.IntegerLiteral( value );
	}

	@Override
	public org.lgna.project.ast.IntegerLiteral getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.IntegerLiteral, Void> node ) {
		return this.transientValue;
	}

	@Override
	public org.lgna.project.ast.IntegerLiteral createValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.IntegerLiteral, Void> node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		return new org.lgna.project.ast.IntegerLiteral( this.transientValue.value.getValue() );
	}
}
