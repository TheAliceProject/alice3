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

package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public class NamedUserConstructor extends UserConstructor implements UserCode {
	public NamedUserConstructor() {
	}

	public NamedUserConstructor( UserParameter[] parameters, ConstructorBlockStatement body ) {
		this.requiredParameters.add( parameters );
		this.body.setValue( body );
	}

	@Override
	protected Object convertPropertyValueIfNecessary( edu.cmu.cs.dennisc.property.InstanceProperty property, Object value ) {
		value = super.convertPropertyValueIfNecessary( property, value );
		if( property == this.body ) {
			if( value instanceof BlockStatement ) {
				if( value instanceof ConstructorBlockStatement ) {
					//pass
				} else {
					BlockStatement prevBlockStatement = (BlockStatement)value;
					Statement[] buffer = new Statement[ prevBlockStatement.statements.size() ];
					ConstructorBlockStatement constructorBlockStatement = new ConstructorBlockStatement( new SuperConstructorInvocationStatement(), prevBlockStatement.statements.toArray( buffer ) );
					constructorBlockStatement.isEnabled.setValue( prevBlockStatement.isEnabled.getValue() );
					value = constructorBlockStatement;
				}
			}
		}
		return value;
	}

	@Override
	public org.lgna.project.ast.ManagementLevel getManagementLevel() {
		return this.managementLevel.getValue();
	}

	@Override
	public NodeProperty<ConstructorBlockStatement> getBodyProperty() {
		return this.body;
	}

	@Override
	public NodeListProperty<UserParameter> getRequiredParamtersProperty() {
		return this.requiredParameters;
	}

	@Override
	public org.lgna.project.annotations.Visibility getVisibility() {
		return this.visibility;
	}

	public void setVisibility( org.lgna.project.annotations.Visibility visibility ) {
		this.visibility = visibility;
	}

	@Override
	public AbstractCode getNextLongerInChain() {
		return null;
	}

	@Override
	public AbstractCode getNextShorterInChain() {
		return null;
	}

	@Override
	public boolean isSignatureLocked() {
		return this.isSignatureLocked.getValue();
	}

	@Override
	public java.util.List<UserParameter> getRequiredParameters() {
		return this.requiredParameters.getValue();
	}

	@Override
	public AccessLevel getAccessLevel() {
		return this.accessLevel.getValue();
	}

	@Override
	public void appendJava( JavaCodeGenerator generator ) {
		generator.appendMemberPrefix( this );
		generator.appendAccessLevel( this.getAccessLevel() );
		generator.appendTypeName( this.getDeclaringType() );
		generator.appendParameters( this );
		this.body.getValue().appendJava( generator );
		generator.appendMemberPostfix( this );
	}

	public final NodeListProperty<UserParameter> requiredParameters = new NodeListProperty<UserParameter>( this );
	public final edu.cmu.cs.dennisc.property.EnumProperty<AccessLevel> accessLevel = new edu.cmu.cs.dennisc.property.EnumProperty<AccessLevel>( this, AccessLevel.PUBLIC );
	public final NodeProperty<ConstructorBlockStatement> body = new NodeProperty<ConstructorBlockStatement>( this );
	public final edu.cmu.cs.dennisc.property.EnumProperty<ManagementLevel> managementLevel = new edu.cmu.cs.dennisc.property.EnumProperty<ManagementLevel>( this, ManagementLevel.NONE );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isSignatureLocked = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isDeletionAllowed = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );

	private org.lgna.project.annotations.Visibility visibility = org.lgna.project.annotations.Visibility.PRIME_TIME;
}
