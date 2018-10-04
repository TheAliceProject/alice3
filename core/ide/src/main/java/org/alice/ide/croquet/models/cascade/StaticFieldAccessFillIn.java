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
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.TypeExpression;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class StaticFieldAccessFillIn extends ExpressionFillInWithoutBlanks<FieldAccess> {
	private static Map<AbstractField, StaticFieldAccessFillIn> map = Maps.newHashMap();

	public static StaticFieldAccessFillIn getInstance( AbstractField value ) {
		synchronized( map ) {
			StaticFieldAccessFillIn rv = map.get( value );
			if( rv != null ) {
				//pass
			} else {
				rv = new StaticFieldAccessFillIn( value );
				map.put( value, rv );
			}
			return rv;
		}
	}

	public static StaticFieldAccessFillIn getInstance( AbstractType type, String fieldName ) {
		return getInstance( type.findField( fieldName ) );
	}

	public static StaticFieldAccessFillIn getInstance( Field fld ) {
		return getInstance( JavaField.getInstance( fld ) );
	}

	public static StaticFieldAccessFillIn getInstance( Class<?> cls, String fieldName ) {
		return getInstance( JavaType.getInstance( cls ), fieldName );
	}

	private final FieldAccess transientValue;

	private StaticFieldAccessFillIn( AbstractField field ) {
		super( UUID.fromString( "dff6296d-9651-4c0a-98a1-57cd62ea2010" ) );
		this.transientValue = this.createValue( field );
	}

	private FieldAccess createValue( AbstractField field ) {
		return new FieldAccess( new TypeExpression( field.getDeclaringType() ), field );
	}

	@Override
	public FieldAccess createValue( ItemNode<? super FieldAccess, Void> node ) {
		return this.createValue( this.transientValue.field.getValue() );
	}

	@Override
	public FieldAccess getTransientValue( ItemNode<? super FieldAccess, Void> node ) {
		return this.transientValue;
	}
}
