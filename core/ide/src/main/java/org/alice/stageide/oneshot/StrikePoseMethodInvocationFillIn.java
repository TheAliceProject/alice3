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

package org.alice.stageide.oneshot;

/**
 * @author Dennis Cosgrove
 */
public class StrikePoseMethodInvocationFillIn extends org.lgna.croquet.ImmutableCascadeFillIn<MethodInvocationEditFactory, org.lgna.project.ast.Expression> {
	private static edu.cmu.cs.dennisc.map.MapToMap<org.alice.ide.instancefactory.InstanceFactory, org.lgna.project.ast.MethodInvocation, StrikePoseMethodInvocationFillIn> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static StrikePoseMethodInvocationFillIn getInstance( org.alice.ide.instancefactory.InstanceFactory instanceFactory, org.lgna.project.ast.MethodInvocation method ) {
		return mapToMap.getInitializingIfAbsent( instanceFactory, method, new edu.cmu.cs.dennisc.map.MapToMap.Initializer<org.alice.ide.instancefactory.InstanceFactory, org.lgna.project.ast.MethodInvocation, StrikePoseMethodInvocationFillIn>() {
			@Override
			public StrikePoseMethodInvocationFillIn initialize( org.alice.ide.instancefactory.InstanceFactory instanceFactory, org.lgna.project.ast.MethodInvocation method ) {
				return new StrikePoseMethodInvocationFillIn( instanceFactory, method );
			}
		} );
	}

	private final org.alice.ide.instancefactory.InstanceFactory instanceFactory;
	private final org.lgna.project.ast.MethodInvocation strikePoseInvocation;

	private StrikePoseMethodInvocationFillIn( org.alice.ide.instancefactory.InstanceFactory instanceFactory, org.lgna.project.ast.MethodInvocation methodInvocation ) {
		super( java.util.UUID.fromString( "00505921-ef68-4c5a-bbf9-40e9a4dc56cd" ), new org.alice.ide.croquet.models.cascade.ParameterBlank[ 0 ] );
		this.instanceFactory = instanceFactory;
		this.strikePoseInvocation = methodInvocation;
		this.strikePoseInvocation.expression.setValue( instanceFactory.createExpression() );
	}

	private org.lgna.project.ast.JavaMethod getMethod() {
		return (org.lgna.project.ast.JavaMethod)this.strikePoseInvocation.method.getValue();
	}

	protected org.alice.stageide.oneshot.MethodInvocationEditFactory createMethodInvocationEditFactory( org.alice.ide.instancefactory.InstanceFactory instanceFactory, org.lgna.project.ast.JavaMethod method, org.lgna.project.ast.Expression[] argumentExpressions ) {
		return new StrikePoseMethodInvocationEditFactory( instanceFactory, method, argumentExpressions );
	}

	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.imp.cascade.ItemNode<? super MethodInvocationEditFactory, org.lgna.project.ast.Expression> itemNode ) {
		return org.alice.ide.x.PreviewAstI18nFactory.getInstance().createStatementPane( new org.lgna.project.ast.ExpressionStatement( this.strikePoseInvocation ) ).getAwtComponent();
	}

	@Override
	public MethodInvocationEditFactory createValue( org.lgna.croquet.imp.cascade.ItemNode<? super MethodInvocationEditFactory, org.lgna.project.ast.Expression> itemNode, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		java.util.ArrayList<org.lgna.project.ast.SimpleArgument> arguments = this.strikePoseInvocation.requiredArguments.getValue();
		org.lgna.project.ast.Expression[] argumentExpressions = new org.lgna.project.ast.Expression[ arguments.size() ];
		for( int i = 0; i < arguments.size(); i++ ) {
			argumentExpressions[ i ] = arguments.get( i ).expression.getValue();
		}
		new StrikePoseMethodInvocationEditFactory( this.instanceFactory, this.getMethod(), argumentExpressions );
		return this.createMethodInvocationEditFactory( this.instanceFactory, this.getMethod(), argumentExpressions );
	}

	@Override
	public MethodInvocationEditFactory getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super MethodInvocationEditFactory, org.lgna.project.ast.Expression> itemNode ) {
		return null;
	}
}
