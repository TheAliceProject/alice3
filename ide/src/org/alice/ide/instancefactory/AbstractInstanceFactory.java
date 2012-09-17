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

package org.alice.ide.instancefactory;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractInstanceFactory implements InstanceFactory {
	protected static org.lgna.project.ast.Expression createTransientThisExpression() {
		return new org.alice.ide.ast.CurrentThisExpression();
	}

	protected static org.lgna.project.ast.Expression createThisExpression() {
		return new org.lgna.project.ast.ThisExpression();
	}

	private final edu.cmu.cs.dennisc.property.InstanceProperty<?>[] mutablePropertiesOfInterest;
	private org.lgna.croquet.resolvers.Resolver resolver;

	public AbstractInstanceFactory( edu.cmu.cs.dennisc.property.InstanceProperty<?>... mutablePropertiesOfInterest ) {
		this.mutablePropertiesOfInterest = mutablePropertiesOfInterest;
	}

	protected abstract <F extends org.alice.ide.instancefactory.InstanceFactory> org.lgna.croquet.resolvers.Resolver<F> createResolver();

	public final <F extends org.alice.ide.instancefactory.InstanceFactory> org.lgna.croquet.resolvers.Resolver<F> getResolver() {
		if( this.resolver != null ) {
			//pass
		} else {
			this.resolver = this.createResolver();
		}
		return this.resolver;
	}

	public final edu.cmu.cs.dennisc.property.InstanceProperty<?>[] getMutablePropertiesOfInterest() {
		return this.mutablePropertiesOfInterest;
	}

	protected abstract boolean isValid( org.lgna.project.ast.AbstractType<?, ?, ?> type, org.lgna.project.ast.AbstractCode code );

	public final boolean isValid() {
		org.lgna.project.ast.AbstractType<?, ?, ?> type;
		org.lgna.project.ast.AbstractCode code;
		org.lgna.project.ast.AbstractDeclaration declaration = org.alice.ide.MetaDeclarationFauxState.getInstance().getValue();
		if( declaration instanceof org.lgna.project.ast.AbstractType<?, ?, ?> ) {
			type = (org.lgna.project.ast.AbstractType<?, ?, ?>)declaration;
			code = null;
		} else if( declaration instanceof org.lgna.project.ast.AbstractCode ) {
			code = (org.lgna.project.ast.AbstractCode)declaration;
			type = code.getDeclaringType();
		} else {
			code = null;
			type = null;
		}
		return this.isValid( type, code );
	}

	public org.lgna.croquet.icon.IconFactory getIconFactory() {
		return org.alice.stageide.icons.IconFactoryManager.getIconFactoryForType( this.getValueType() );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getRepr() );
		return sb.toString();
	}
}
