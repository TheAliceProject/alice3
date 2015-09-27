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
public abstract class AbstractDeclaration extends AbstractNode implements Declaration {
	public abstract boolean isUserAuthored();

	@Override
	public abstract edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists();

	@Override
	public boolean isAppropriatelyIdenitifiedById() {
		return this.isUserAuthored();
	}

	@Override
	public boolean contentEquals( Node o, ContentEqualsStrictness strictness, edu.cmu.cs.dennisc.property.PropertyFilter filter ) {
		if( super.contentEquals( o, strictness, filter ) ) {
			AbstractDeclaration other = (AbstractDeclaration)o;
			if( strictness == ContentEqualsStrictness.DECLARATIONS_EQUAL ) {
				return this == other;
			} else if( strictness == ContentEqualsStrictness.DECLARATIONS_HAVE_SAME_NAME ) {
				return edu.cmu.cs.dennisc.java.util.Objects.equals( this.getName(), other.getName() );
			} else {
				throw new RuntimeException( strictness.name() );
			}
		}
		return false;
	}

	@Override
	protected java.util.Set<AbstractDeclaration> fillInDeclarationSet( java.util.Set<AbstractDeclaration> rv, java.util.Set<AbstractNode> nodes ) {
		rv.add( this );
		return super.fillInDeclarationSet( rv, nodes );
	}

	@Override
	public String getName() {
		edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.getNamePropertyIfItExists();
		if( nameProperty != null ) {
			return nameProperty.getValue();
		} else {
			return null;
		}
	}

	@Override
	public void setName( String name ) {
		edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.getNamePropertyIfItExists();
		if( nameProperty != null ) {
			nameProperty.setValue( name );
		} else {
			throw new RuntimeException( this + " " + name );
		}
	}

	@Override
	protected void appendRepr( org.lgna.project.ast.localizer.AstLocalizer localizer ) {
		localizer.appendDeclaration( this );
	}

	@Override
	protected StringBuilder appendStringDetails( StringBuilder rv ) {
		super.appendStringDetails( rv );
		rv.append( this.getName() );
		return rv;
	}
}
