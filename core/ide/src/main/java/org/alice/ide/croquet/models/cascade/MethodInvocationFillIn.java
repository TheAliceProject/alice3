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

package org.alice.ide.croquet.models.cascade;

import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.ide.ast.IncompleteAstUtilities;
import org.alice.ide.croquet.models.ast.cascade.MethodUtilities;
import org.lgna.croquet.CascadeBlank;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.MethodInvocation;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class MethodInvocationFillIn extends ExpressionFillInWithExpressionBlanks<MethodInvocation> {
	//
	//
	//
	// todo: since a user method's signature can change, this should not inherit from ImmutableCascadeFillIn
	// extends org.lgna.croquet.CascadeFillIn<org.lgna.project.ast.MethodInvocation, org.lgna.project.ast.Expression> {
	//
	//
	//
	private final MethodInvocation transientValue;
	private final List<CascadeBlank<Expression>> lockedBlanks;

	private static List<CascadeBlank<Expression>> createBlanks( AbstractMethod method ) {
		return (List)Collections.unmodifiableList( Lists.newArrayList( MethodUtilities.createParameterBlanks( method ) ) );
	}

	public MethodInvocationFillIn( UUID id, Expression transientValueExpression, AbstractMethod method ) {
		super( id );
		if( method.isSignatureLocked() ) {
			this.lockedBlanks = createBlanks( method );
		} else {
			this.lockedBlanks = null;
		}
		this.transientValue = IncompleteAstUtilities.createIncompleteMethodInvocation( transientValueExpression, method );
	}

	protected abstract Expression createExpression( Expression transientValueExpression );

	@Override
	public List<CascadeBlank<Expression>> getBlanks() {
		if( this.lockedBlanks != null ) {
			return this.lockedBlanks;
		} else {
			return createBlanks( this.transientValue.method.getValue() );
		}
	}

	@Override
	protected MethodInvocation createValue( Expression[] expressions ) {
		return AstUtilities.createMethodInvocation( this.createExpression( this.transientValue.expression.getValue() ), this.transientValue.method.getValue(), expressions );
	}

	@Override
	public MethodInvocation getTransientValue( ItemNode<? super MethodInvocation, Expression> step ) {
		return this.transientValue;
	}
}
