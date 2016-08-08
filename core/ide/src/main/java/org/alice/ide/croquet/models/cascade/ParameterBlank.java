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

/**
 * @author Dennis Cosgrove
 */
public class ParameterBlank extends ExpressionBlank {
	private static java.util.Map<org.lgna.project.ast.AbstractParameter, ParameterBlank> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized ParameterBlank getInstance( org.lgna.project.ast.AbstractParameter parameter ) {
		assert parameter != null;
		ParameterBlank rv = map.get( parameter );
		if( rv != null ) {
			//pass
		} else {
			rv = new ParameterBlank( parameter );
			map.put( parameter, rv );
		}
		return rv;
	}

	private final org.lgna.project.ast.AbstractParameter parameter;

	private ParameterBlank( org.lgna.project.ast.AbstractParameter parameter ) {
		super( java.util.UUID.fromString( "84524eb1-7dbe-4481-8037-005d6402dbf3" ), parameter.getValueType(), parameter.getDetails() );
		this.parameter = parameter;
	}

	@Override
	protected void updateChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> children, org.lgna.croquet.imp.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> valueType = this.parameter.getValueType();
		org.lgna.project.ast.AbstractType<?, ?, ?> keywordFactoryType = valueType.getKeywordFactoryType();
		if( keywordFactoryType != null ) {
			Class<?> cls = ( (org.lgna.project.ast.JavaType)keywordFactoryType ).getClassReflectionProxy().getReification();
			for( java.lang.reflect.Method mthd : cls.getMethods() ) {
				org.lgna.project.ast.JavaType returnType = org.lgna.project.ast.JavaType.getInstance( mthd.getReturnType() );
				if( returnType == valueType ) {
					children.add( KeywordMenuModel.getInstance( org.lgna.project.ast.JavaMethod.getInstance( mthd ) ) );
				}
			}
		} else {
			children.add( ParameterNameSeparator.getInstance( this.parameter ) );
			super.updateChildren( children, blankNode );
		}
	}
}
