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
package org.alice.ide.croquet.models.ast.cascade;

/**
 * @author Dennis Cosgrove
 */
public class ArgumentCascade extends ProjectExpressionPropertyCascade {
	private static java.util.Map<org.lgna.project.ast.SimpleArgument, ArgumentCascade> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	public static synchronized ArgumentCascade getInstance( org.lgna.project.ast.SimpleArgument argument ) {
		ArgumentCascade rv = map.get( argument );
		if( rv != null ) {
			//pass
		} else {
			rv = new ArgumentCascade( argument );
			map.put( argument, rv );
		}
		return rv;
	}

	private final org.lgna.project.ast.SimpleArgument argument;

	private ArgumentCascade( org.lgna.project.ast.SimpleArgument argument ) {
		super( java.util.UUID.fromString( "c60b0eec-d8ac-4256-a8be-54b16605fc0e" ), argument.expression, org.alice.ide.croquet.models.cascade.ParameterBlank.getInstance( argument.parameter.getValue() ) );
		this.argument = argument;
	}

	//	@Override
	//	public org.lgna.project.ast.ExpressionProperty getExpressionProperty() {
	//		return this.argument.expression;
	//	}
	//	@Override
	//	protected String getTitle() {
	//		return this.argument.parameter.getValue().getName();
	//	}
	@Override
	public org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver<ArgumentCascade> createResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver<ArgumentCascade>( this, org.lgna.project.ast.SimpleArgument.class, this.argument );
	}

	@Override
	protected org.lgna.project.ast.Expression createExpression( org.lgna.project.ast.Expression[] expressions ) {
		assert expressions.length == 1;
		return expressions[ 0 ];
	}
}
