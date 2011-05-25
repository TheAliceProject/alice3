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
public class StaticFieldAccessFillIn extends ExpressionFillInWithoutBlanks< edu.cmu.cs.dennisc.alice.ast.FieldAccess > {
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractField, StaticFieldAccessFillIn > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static StaticFieldAccessFillIn getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractField value ) {
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
	public static StaticFieldAccessFillIn getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractType type, String fieldName ) {
		return getInstance( type.findField( fieldName ) );
	}
	public static StaticFieldAccessFillIn getInstance( Class<?> cls, String fieldName ) {
		return getInstance( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls ), fieldName );
	}
	private final edu.cmu.cs.dennisc.alice.ast.FieldAccess transientValue;
	private StaticFieldAccessFillIn( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( java.util.UUID.fromString( "c0c8bc11-ed5b-4541-8e4a-45579e05b0d2" ) );
		this.transientValue = this.createValue( field );
	}
	private edu.cmu.cs.dennisc.alice.ast.FieldAccess createValue( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.TypeExpression( field.getDeclaringType() ), field );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.FieldAccess createValue( org.lgna.croquet.steps.CascadeFillInPrepStep context ) {
		return this.createValue( this.transientValue.field.getValue() );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.FieldAccess getTransientValue( org.lgna.croquet.steps.CascadeFillInPrepStep context ) {
		return this.transientValue;
	}
	@Override
	protected org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< StaticFieldAccessFillIn > createCodableResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< StaticFieldAccessFillIn >( this, this.transientValue.field.getValue(), edu.cmu.cs.dennisc.alice.ast.AbstractField.class );
	}
	
	@Override
	protected String getTutorialItemText() {
		return this.transientValue.field.getValue().getName();
	}
//	@Override
//	protected org.alice.ide.croquet.resolvers.FieldStaticGetInstanceKeyedResolver< StaticFieldAccessFillIn > createCodableResolver() {
//		return new org.alice.ide.croquet.resolvers.FieldStaticGetInstanceKeyedResolver< StaticFieldAccessFillIn >( this, this.transientValue.field.getValue() );
//	}
}
