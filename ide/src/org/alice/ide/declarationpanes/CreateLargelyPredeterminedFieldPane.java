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
package org.alice.ide.declarationpanes;

/**
 * @author Dennis Cosgrove
 */
public abstract class CreateLargelyPredeterminedFieldPane extends org.alice.ide.declarationpanes.AbstractCreateFieldPane {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType valueType;
	private edu.cmu.cs.dennisc.alice.ast.Expression initializer;

	public CreateLargelyPredeterminedFieldPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, Class< ? > cls, edu.cmu.cs.dennisc.alice.ast.AbstractType valueType ) {
		super( declaringType );
		if( cls != null ) {
			assert valueType == null;
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls );
			this.valueType = this.getTypeDeclaredInAliceFor( typeDeclaredInJava );
		} else {
			assert valueType != null;
			this.valueType = valueType;
		}
		this.initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation( this.valueType );
	}
	
	protected edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getTypeDeclaredInAliceFor( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeDeclaredInJava ) {
		return getIDE().getTypeDeclaredInAliceFor( typeDeclaredInJava );
	}

	@Override
	protected String getDefaultNameText() {
		return this.getIDE().getPotentialInstanceNameFor( this.getDeclaringType(), this.valueType );
	}

	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		return this.valueType;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return this.initializer;
	}

	@Override
	protected boolean isEditableValueTypeComponentDesired() {
		return false;
	}
	
	@Override
	protected boolean isEditableInitializerComponentDesired() {
		return false;
	}
	
	@Override
	protected boolean isIsReassignableComponentDesired() {
		return false;
	}
	@Override
	protected boolean isReassignable() {
		return false;
	}
	@Override
	protected boolean isIsReassignableComponentEnabled() {
		return false;
	}
	@Override
	protected boolean getIsReassignableInitialState() {
		return false;
	}
	
	@Override
	protected edu.cmu.cs.dennisc.croquet.KComponent< ? > createValueTypeComponent() {
		edu.cmu.cs.dennisc.croquet.KLineAxisPanel valueTypeLine = new edu.cmu.cs.dennisc.croquet.KLineAxisPanel();
		valueTypeLine.addComponent( new org.alice.ide.common.TypeComponent( CreateLargelyPredeterminedFieldPane.this.valueType ) );
		if( CreateLargelyPredeterminedFieldPane.this.valueType instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			valueTypeLine.addComponent( new edu.cmu.cs.dennisc.croquet.KLabel( " which extends ", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT ) );
			valueTypeLine.addComponent( new org.alice.ide.common.TypeComponent( CreateLargelyPredeterminedFieldPane.this.valueType.getSuperType() ) );
//			valueTypeLine.add( zoot.ZLabel.acquire( " ) ", zoot.font.ZTextPosture.OBLIQUE, zoot.font.ZTextWeight.LIGHT ) );
		}
		return valueTypeLine;
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.KComponent< ? > createInitializerComponent() {
		return new edu.cmu.cs.dennisc.croquet.KLineAxisPanel( getIDE().getPreviewFactory().createExpressionPane( this.getInitializer() ) );
	}
}
