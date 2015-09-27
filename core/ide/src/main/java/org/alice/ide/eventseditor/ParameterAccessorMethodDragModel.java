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
package org.alice.ide.eventseditor;

/**
 * @author Dennis Cosgrove
 */
public class ParameterAccessorMethodDragModel extends org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel {
	//	private static class InternalComposite extends org.lgna.croquet.Composite {
	//		private ParameterAccessorMethodDragModel dragModel;
	//		public InternalComposite( ParameterAccessorMethodDragModel dragModel ) {
	//			super( java.util.UUID.fromString( "a01541b1-6b7b-499d-b13c-3268cebcc0b9" ) );
	//			this.dragModel = dragModel;
	//		}
	//		@Override
	//		protected void localize() {
	//		}
	//		@Override
	//		public boolean contains( org.lgna.croquet.Model dragModel ) {
	//			return false;
	//		}
	//		@Override
	//		protected org.lgna.croquet.components.View<?,?> createView() {
	//			return new org.alice.ide.eventseditor.components.EventAccessorMethodDragView( this.dragModel );
	//		}
	//	}

	private static class InternalDropModel extends org.alice.ide.croquet.models.ast.cascade.ProjectExpressionPropertyCascade {
		private final org.lgna.project.ast.UserParameter parameter;
		private final org.lgna.project.ast.AbstractMethod method;

		private InternalDropModel( org.lgna.project.ast.ExpressionProperty expressionProperty, org.lgna.project.ast.UserParameter parameter, org.lgna.project.ast.AbstractMethod method ) {
			super( java.util.UUID.fromString( "9645fc2e-6797-438b-9abd-289255b7d027" ), expressionProperty, org.alice.ide.croquet.models.ast.cascade.MethodUtilities.createParameterBlanks( method ) );
			this.parameter = parameter;
			this.method = method;
		}

		@Override
		protected org.lgna.project.ast.Expression createExpression( org.lgna.project.ast.Expression[] expressions ) {
			return org.lgna.project.ast.AstUtilities.createMethodInvocation( new org.lgna.project.ast.ParameterAccess( this.parameter ), this.method, expressions );
		}
	}

	private static edu.cmu.cs.dennisc.map.MapToMap<org.lgna.project.ast.UserParameter, org.lgna.project.ast.AbstractMethod, ParameterAccessorMethodDragModel> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static synchronized ParameterAccessorMethodDragModel getInstance( org.lgna.project.ast.UserParameter parameter, org.lgna.project.ast.AbstractMethod method ) {
		return mapToMap.getInitializingIfAbsent( parameter, method, new edu.cmu.cs.dennisc.map.MapToMap.Initializer<org.lgna.project.ast.UserParameter, org.lgna.project.ast.AbstractMethod, ParameterAccessorMethodDragModel>() {
			@Override
			public ParameterAccessorMethodDragModel initialize( org.lgna.project.ast.UserParameter parameter, org.lgna.project.ast.AbstractMethod method ) {
				return new ParameterAccessorMethodDragModel( parameter, method );
			}
		} );
	}

	//	private final InternalComposite composite = new InternalComposite( this );
	private final org.lgna.project.ast.UserParameter parameter;
	private final org.lgna.project.ast.AbstractMethod method;

	private ParameterAccessorMethodDragModel( org.lgna.project.ast.UserParameter parameter, org.lgna.project.ast.AbstractMethod method ) {
		super( java.util.UUID.fromString( "c41ee1e7-aaea-4fa0-80fe-9b969998acb5" ) );
		this.parameter = parameter;
		this.method = method;
	}

	public org.lgna.project.ast.UserParameter getParameter() {
		return this.parameter;
	}

	public org.lgna.project.ast.AbstractMethod getMethod() {
		return this.method;
	}

	//	public org.lgna.croquet.Composite< ? > getComposite() {
	//		return this.composite;
	//	}

	@Override
	public boolean isPotentialStatementCreator() {
		return false;
	}

	@Override
	protected org.lgna.croquet.Model getDropModel( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		throw new AssertionError();
	}

	@Override
	protected org.lgna.croquet.Model getDropModel( org.lgna.project.ast.ExpressionProperty expressionProperty ) {
		return new InternalDropModel( expressionProperty, this.parameter, this.method );
	}

	@Override
	public org.lgna.project.ast.AbstractType<?, ?, ?> getType() {
		return this.method.getReturnType();
	}
}
