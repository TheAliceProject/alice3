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

package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public class FieldDeclaredInAlice extends AbstractField implements MemberDeclaredInAlice {
	public edu.cmu.cs.dennisc.property.StringProperty name = new edu.cmu.cs.dennisc.property.StringProperty( this, null );
	public DeclarationProperty< AbstractType<?,?,?> > valueType = new DeclarationProperty< AbstractType<?,?,?> >( this );
	public edu.cmu.cs.dennisc.property.EnumProperty< Access > access = new edu.cmu.cs.dennisc.property.EnumProperty< Access >( this, Access.PUBLIC );
	public edu.cmu.cs.dennisc.property.EnumProperty< FieldModifierFinalVolatileOrNeither > finalVolatileOrNeither = new edu.cmu.cs.dennisc.property.EnumProperty< FieldModifierFinalVolatileOrNeither >( this, FieldModifierFinalVolatileOrNeither.NEITHER );
	public edu.cmu.cs.dennisc.property.BooleanProperty isStatic = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.BooleanProperty isTransient = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.BooleanProperty isDeletionAllowed = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.TRUE );

	public ExpressionProperty initializer = new ExpressionProperty( this ) {
		@Override
		public AbstractType<?,?,?> getExpressionType() {
			return FieldDeclaredInAlice.this.valueType.getValue();
		}
	};
	
	private AbstractTypeDeclaredInAlice<?> m_declaringType;
	private edu.cmu.cs.dennisc.alice.annotations.Visibility m_visibility = edu.cmu.cs.dennisc.alice.annotations.Visibility.PRIME_TIME; 

	public FieldDeclaredInAlice() {
	}
	public FieldDeclaredInAlice( String name, AbstractType<?,?,?> valueType, Expression initializer ) {
		this.name.setValue( name );
		this.valueType.setValue( valueType );
		this.initializer.setValue( initializer );
	}
	public FieldDeclaredInAlice( String name, Class< ? > valueCls, Expression initializer ) {
		this( name, TypeDeclaredInJava.get( valueCls ), initializer );
	}

	@Override
	public String getName() {
		return name.getValue();
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return this.name;
	}
	
	@Override
	public AbstractType<?,?,?> getValueType() {
		return valueType.getValue();
	}
	@Override
	public AbstractType<?,?,?> getDesiredValueType() {
		return getValueType();
	}

	@Override
	public AbstractTypeDeclaredInAlice<?> getDeclaringType() {
		return m_declaringType;
	}
	public void setDeclaringType( AbstractTypeDeclaredInAlice<?> declaringType ) {
		m_declaringType = declaringType;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		return m_visibility;
	}
	public void setVisibility( edu.cmu.cs.dennisc.alice.annotations.Visibility visibility ) {
		m_visibility = visibility;
	}

	@Override
	public AbstractMember getNextLongerInChain() {
		return null;
	}
	@Override
	public AbstractMember getNextShorterInChain() {
		return null;
	}

	@Override
	public Access getAccess() {
		return this.access.getValue();
	}
	
	@Override
	public boolean isStatic() {
		return this.isStatic.getValue();
	}
	@Override
	public boolean isFinal() {
		return finalVolatileOrNeither.getValue() == FieldModifierFinalVolatileOrNeither.FINAL;
	}
	@Override
	public boolean isVolatile() {
		return finalVolatileOrNeither.getValue() == FieldModifierFinalVolatileOrNeither.VOLATILE;
	}
	@Override
	public boolean isTransient() {
		return this.isStatic.getValue();
	}
}
