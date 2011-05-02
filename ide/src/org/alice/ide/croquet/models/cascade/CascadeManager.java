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

package org.alice.ide.croquet.models.cascade;

/**
 * @author Dennis Cosgrove
 */
public class CascadeManager {
	private CascadeManager() {
		throw new AssertionError();
	}
	public static ExpressionBlank getBlankForType( Class<?> cls ) {
		ExpressionBlank rv;
		if( cls != null ) {
			rv = new ExpressionBlank( java.util.UUID.fromString( "d03f9c80-7371-4d78-8579-63e392d18557" ), cls ) {
			};
			///todo: UnhandledBlank
		} else {
			rv = org.alice.ide.croquet.models.cascade.blanks.TypeUnsetBlank.getInstance();
		}
		return rv;
	}
	public static ExpressionBlank getBlankForType( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > type ) {
		Class<?> cls;
		if( type != null ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeDeclaredInJava = type.getFirstTypeEncounteredDeclaredInJava();
			assert typeDeclaredInJava != null : type;
			cls = typeDeclaredInJava.getClassReflectionProxy().getReification();
		} else {
			cls = null;
		}
		return getBlankForType( cls );
	}

	public static ExpressionBlank[] createBlanks( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? >... types ) {
		ExpressionBlank[] rv = new ExpressionBlank[ types.length ];
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = getBlankForType( types[ i ] );
		}
		return rv;
	}
	public static org.alice.ide.croquet.models.cascade.ExpressionBlank[] createBlanks( Class<?>... clses ) {
		return createBlanks( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( clses ) );
	}
	
	public static boolean isInclusionDesired( edu.cmu.cs.dennisc.croquet.CascadeItemContext< ?,?,? > context, edu.cmu.cs.dennisc.alice.ast.Expression previousExpression, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > desiredType ) {
//		if( this.previousExpression != null ) {
//		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> prevExpressionType = this.previousExpression.getType();
//		if( prevExpressionType != null && prevExpressionType.isAssignableTo( type ) ) {
//			if( blank.isTop() ) {
//				//pass
//			} else {
//				blank.addFillIn( new org.alice.ide.cascade.LabeledExpressionFillIn( this.previousExpression, "(current value)" ) );
//				blank.addSeparator();
//			}
//		}
//	}
		//this.leftOperandType.isAssignableFrom( previousExpression.getType() )
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: isInclusionDesired" );
		return true;
	}
	public static boolean isInclusionDesired( edu.cmu.cs.dennisc.croquet.CascadeItemContext< ?,?,? > context, edu.cmu.cs.dennisc.alice.ast.Expression previousExpression, Class<?> desiredCls ) {
		return isInclusionDesired( context, previousExpression, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( desiredCls ) );
	}
}
