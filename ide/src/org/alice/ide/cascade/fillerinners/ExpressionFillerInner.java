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
package org.alice.ide.cascade.fillerinners;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionFillerInner {
//	protected static final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava RANDOM_UTILITIES_TYPE = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.random.RandomUtilities.class );
//	protected static final edu.cmu.cs.dennisc.alice.ast.TypeExpression RANDOM_UTILITIES_TYPE_EXPRESSION = org.alice.ide.ast.NodeUtilities.createTypeExpression( RANDOM_UTILITIES_TYPE );
//	protected static void addStaticMethodInvocationFillIn( edu.cmu.cs.dennisc.croquet.CascadeBlank blank, edu.cmu.cs.dennisc.alice.ast.TypeExpression typeExpression, String methodName, Class<?>... parameterClses ) {
//		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = typeExpression.value.getValue();
//		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = type.getDeclaredMethod( methodName, parameterClses );
//		assert method != null : methodName;
////		org.alice.ide.cascade.IncompleteMethodInvocationFillIn methodInvocationFillIn = new org.alice.ide.cascade.IncompleteMethodInvocationFillIn( typeExpression, method );
//		blank.addFillIn( org.alice.ide.croquet.models.cascade.StaticMethodInvocationArgumentsFillIn.getInstance( method ) );
//	}
//	protected static void addStaticFieldAccessFillIn( edu.cmu.cs.dennisc.croquet.CascadeBlank blank, edu.cmu.cs.dennisc.alice.ast.TypeExpression typeExpression, Class<?> valueCls, String fieldName ) {
//		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = typeExpression.value.getValue();
//		edu.cmu.cs.dennisc.alice.ast.AbstractField field = type.getDeclaredField( valueCls, fieldName );
//		assert field != null : fieldName;
////		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.FieldAccess >( new edu.cmu.cs.dennisc.alice.ast.FieldAccess( typeExpression, field ) ) );
//		blank.addFillIn( org.alice.ide.croquet.models.cascade.StaticFieldAccessFillIn.getInstance( field ) );
//	}

	private edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type;
	private Class< ? extends edu.cmu.cs.dennisc.alice.ast.Expression > expressionCls;
	public ExpressionFillerInner( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type, Class< ? extends edu.cmu.cs.dennisc.alice.ast.Expression > expressionCls ) {
		this.type = type;
		this.expressionCls = expressionCls;
	}
	public ExpressionFillerInner( Class<?> cls, Class< ? extends edu.cmu.cs.dennisc.alice.ast.Expression > expressionCls ) {
		this(  edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls ), expressionCls );
	}

	protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getType() {
		return this.type;
	}
	public boolean isAssignableTo( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		return this.type.isAssignableTo( type );
	}

//	protected void addNewInstanceFillIn( edu.cmu.cs.dennisc.croquet.CascadingMenuBlank blank, Object... args ) {
//		edu.cmu.cs.dennisc.alice.ast.Expression expression = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstanceForArguments( this.expressionCls, args );
//		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( expression ) ); 
//	}
	
	public abstract java.util.List< edu.cmu.cs.dennisc.croquet.CascadeItem > addItems( java.util.List< edu.cmu.cs.dennisc.croquet.CascadeItem > rv, boolean isTop, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression );
}
