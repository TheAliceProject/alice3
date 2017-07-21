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
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public class DefaultExpressionPropertyCascade extends org.alice.ide.croquet.models.ast.cascade.ExpressionPropertyCascade {
	private static java.util.Map<org.lgna.project.ast.ExpressionProperty, DefaultExpressionPropertyCascade> projectGroupMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static java.util.Map<org.lgna.project.ast.ExpressionProperty, DefaultExpressionPropertyCascade> inheritGroupMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static org.lgna.project.ast.AbstractType<?, ?, ?> getDesiredType( org.lgna.project.ast.ExpressionProperty expressionProperty, org.lgna.project.ast.AbstractType<?, ?, ?> desiredType ) {
		if( desiredType != null ) {
			//pass
		} else {
			desiredType = expressionProperty.getExpressionType();
		}
		return desiredType;
	}

	public static synchronized DefaultExpressionPropertyCascade getInstance( org.lgna.croquet.Group group, org.lgna.project.ast.ExpressionProperty expressionProperty, org.lgna.project.ast.AbstractType<?, ?, ?> desiredType ) {
		desiredType = getDesiredType( expressionProperty, desiredType );
		java.util.Map<org.lgna.project.ast.ExpressionProperty, DefaultExpressionPropertyCascade> map;
		if( group == org.lgna.croquet.Application.PROJECT_GROUP ) {
			map = projectGroupMap;
		} else if( group == org.lgna.croquet.Application.INHERIT_GROUP ) {
			map = inheritGroupMap;
		} else {
			throw new RuntimeException( group.toString() );
		}
		DefaultExpressionPropertyCascade rv = map.get( expressionProperty );
		if( rv != null ) {
			//pass
		} else {
			rv = new DefaultExpressionPropertyCascade( group, expressionProperty, desiredType );
			map.put( expressionProperty, rv );
		}
		return rv;
	}

	private DefaultExpressionPropertyCascade( org.lgna.croquet.Group group, org.lgna.project.ast.ExpressionProperty expressionProperty, org.lgna.project.ast.AbstractType<?, ?, ?> desiredType ) {
		super( group, java.util.UUID.fromString( "77532795-0674-4ba4-ad18-989ee9ca0507" ), expressionProperty, org.alice.ide.croquet.models.cascade.ExpressionBlank.createBlanks( desiredType ) );
	}

	@Override
	protected org.lgna.project.ast.Expression createExpression( org.lgna.project.ast.Expression[] expressions ) {
		assert expressions.length == 1;
		return expressions[ 0 ];
	}
}
