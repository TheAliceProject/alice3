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

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.ast.IncompleteAstUtilities;
import org.alice.ide.croquet.models.ast.cascade.MethodUtilities;
import org.lgna.croquet.CascadeBlank;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.MethodInvocation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class StaticMethodInvocationFillIn extends ExpressionFillInWithExpressionBlanks<MethodInvocation> {
	private static Map<AbstractMethod, StaticMethodInvocationFillIn> map = Maps.newHashMap();

	public static StaticMethodInvocationFillIn getInstance( AbstractMethod method ) {
		synchronized( map ) {
			StaticMethodInvocationFillIn rv = map.get( method );
			if( rv != null ) {
				//pass
			} else {
				List<? extends AbstractParameter> requiredParameters = method.getRequiredParameters();
				//note: assuming static methods are in java, which therefore do not change their signatures
				rv = new StaticMethodInvocationFillIn( method, MethodUtilities.createParameterBlanks( method ) );
				map.put( method, rv );
			}
			return rv;
		}
	}

	public static StaticMethodInvocationFillIn getInstance( AbstractType<?, ?, ?> type, String methodName, Class<?>... parameterClses ) {
		AbstractMethod method = type.getDeclaredMethod( methodName, parameterClses );
		assert method != null : methodName;
		return getInstance( method );
	}

	public static StaticMethodInvocationFillIn getInstance( Class<?> cls, String methodName, Class<?>... parameterClses ) {
		return getInstance( JavaType.getInstance( cls ), methodName, parameterClses );
	}

	private final MethodInvocation transientValue;

	private StaticMethodInvocationFillIn( AbstractMethod method, CascadeBlank<Expression>[] blanks ) {
		super( UUID.fromString( "fb3e7243-639b-43e7-8b70-ef7988ed7a97" ), blanks );
		this.transientValue = IncompleteAstUtilities.createIncompleteStaticMethodInvocation( method );
	}

	@Override
	protected MethodInvocation createValue( Expression[] expressions ) {
		return AstUtilities.createStaticMethodInvocation( this.transientValue.method.getValue(), expressions );
	}

	@Override
	public MethodInvocation getTransientValue( ItemNode<? super MethodInvocation, Expression> step ) {
		return this.transientValue;
	}
}
