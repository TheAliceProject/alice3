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

package org.alice.ide.type;

/**
 * @author Dennis Cosgrove
 */
public class ExtendsTypeWithSuperArgumentFieldKey extends AbstractExtendsTypeKey {
	private final org.lgna.project.ast.AbstractField superArgumentField;

	public ExtendsTypeWithSuperArgumentFieldKey( org.lgna.project.ast.AbstractType<?, ?, ?> superType, org.lgna.project.ast.AbstractField superArgumentField ) {
		super( superType );
		assert superArgumentField != null;
		this.superArgumentField = superArgumentField;
	}

	//	@Override
	//	public boolean accept( org.lgna.project.ast.NamedUserType userType ) {
	//		if( super.accept( userType ) ) {
	//			org.lgna.project.ast.NamedUserConstructor constructor = userType.getDeclaredConstructor();
	//			if( constructor != null ) {
	//				org.lgna.project.ast.ConstructorInvocationStatement constructorInvocationStatement = constructor.body.getValue().constructorInvocationStatement.getValue();
	//				if( constructorInvocationStatement instanceof org.lgna.project.ast.SuperConstructorInvocationStatement ) {
	//					if( constructorInvocationStatement.requiredArguments.size() == 1 ) {
	//						org.lgna.project.ast.Expression argumentExpression = constructorInvocationStatement.requiredArguments.get( 0 ).expression.getValue();
	//						if( argumentExpression instanceof org.lgna.project.ast.FieldAccess ) {
	//							org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)argumentExpression;
	//							return fieldAccess.field.getValue() == this.superArgumentField;
	//						}
	//					}
	//				}
	//			}
	//		}
	//		return false;
	//	}
	@Override
	public int hashCode() {
		int rv = super.hashCode();
		rv = ( 37 * rv ) + this.superArgumentField.hashCode();
		return rv;
	}

	@Override
	protected boolean contentEquals( org.alice.ide.type.TypeKey other ) {
		// super class's equals methods ensures this.getClass() == other.getClass()
		return super.contentEquals( other ) && ( this.superArgumentField == ( (ExtendsTypeWithSuperArgumentFieldKey)other ).superArgumentField );
	}
}
