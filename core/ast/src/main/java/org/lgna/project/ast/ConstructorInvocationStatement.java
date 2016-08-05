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
public abstract class ConstructorInvocationStatement extends Statement implements ArgumentOwner {
	public ConstructorInvocationStatement() {
	}

	public ConstructorInvocationStatement( AbstractConstructor constructor, SimpleArgument... requiredArguments ) {
		this( constructor, requiredArguments, null, null );
	}

	public ConstructorInvocationStatement( AbstractConstructor constructor, SimpleArgument[] requiredArguments, SimpleArgument[] variableArguments, JavaKeyedArgument[] keyedArguments ) {
		assert constructor != null;
		this.constructor.setValue( constructor );
		this.requiredArguments.add( requiredArguments );
		if( variableArguments != null ) {
			this.variableArguments.add( variableArguments );
		}
		if( keyedArguments != null ) {
			this.keyedArguments.add( keyedArguments );
		}
	}

	@Override
	public DeclarationProperty<? extends AbstractCode> getParameterOwnerProperty() {
		return this.constructor;
	}

	@Override
	public org.lgna.project.ast.SimpleArgumentListProperty getRequiredArgumentsProperty() {
		return this.requiredArguments;
	}

	@Override
	public org.lgna.project.ast.SimpleArgumentListProperty getVariableArgumentsProperty() {
		return this.variableArguments;
	}

	@Override
	public org.lgna.project.ast.KeyedArgumentListProperty getKeyedArgumentsProperty() {
		return this.keyedArguments;
	}

	@Override
	public boolean contentEquals( Node o, ContentEqualsStrictness strictness, edu.cmu.cs.dennisc.property.PropertyFilter filter ) {
		if( super.contentEquals( o, strictness, filter ) ) {
			ConstructorInvocationStatement other = (ConstructorInvocationStatement)o;
			if( this.constructor.valueContentEquals( other.constructor, strictness, filter ) ) {
				if( this.requiredArguments.valueContentEquals( other.requiredArguments, strictness, filter ) ) {
					if( this.variableArguments.valueContentEquals( other.variableArguments, strictness, filter ) ) {
						return this.keyedArguments.valueContentEquals( other.keyedArguments, strictness, filter );
					}
				}
			}
		}
		return false;
	}

	public final DeclarationProperty<AbstractConstructor> constructor = DeclarationProperty.createReferenceInstance( this );
	public final SimpleArgumentListProperty requiredArguments = new SimpleArgumentListProperty( this );
	public final SimpleArgumentListProperty variableArguments = new SimpleArgumentListProperty( this );
	public final KeyedArgumentListProperty keyedArguments = new KeyedArgumentListProperty( this );
}
