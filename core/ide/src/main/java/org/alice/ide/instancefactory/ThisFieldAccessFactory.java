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
public class ThisFieldAccessFactory extends AbstractInstanceFactory {
	private static java.util.Map<org.lgna.project.ast.UserField, ThisFieldAccessFactory> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized ThisFieldAccessFactory getInstance( org.lgna.project.ast.UserField field ) {
		assert field != null;
		ThisFieldAccessFactory rv = map.get( field );
		if( rv != null ) {
			//pass
		} else {
			rv = new ThisFieldAccessFactory( field );
			map.put( field, rv );
		}
		return rv;
	}

	private final org.lgna.project.ast.UserField field;

	private ThisFieldAccessFactory( org.lgna.project.ast.UserField field ) {
		super( field.name );
		this.field = field;
	}

	@Override
	protected boolean isValid( org.lgna.project.ast.AbstractType<?, ?, ?> type, org.lgna.project.ast.AbstractCode code ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> fieldDeclaringType = this.field.getDeclaringType();
		if( fieldDeclaringType != null ) {
			return fieldDeclaringType.isAssignableFrom( type );
		} else {
			return false;
		}
	}

	public org.lgna.project.ast.UserField getField() {
		return this.field;
	}

	private org.lgna.project.ast.FieldAccess createFieldAccess( org.lgna.project.ast.Expression expression ) {
		return new org.lgna.project.ast.FieldAccess( expression, this.field );
	}

	@Override
	public org.lgna.project.ast.FieldAccess createTransientExpression() {
		return this.createFieldAccess( createTransientThisExpression() );
	}

	@Override
	public org.lgna.project.ast.FieldAccess createExpression() {
		return this.createFieldAccess( createThisExpression() );
	}

	@Override
	public org.lgna.project.ast.AbstractType<?, ?, ?> getValueType() {
		return this.field.getValueType();
	}

	@Override
	public org.lgna.croquet.icon.IconFactory getIconFactory() {
		org.lgna.croquet.icon.IconFactory fallbackIconFactory = org.alice.stageide.icons.IconFactoryManager.getIconFactoryForField( this.field );
		if( ( fallbackIconFactory != null ) && ( fallbackIconFactory != org.lgna.croquet.icon.EmptyIconFactory.getInstance() ) ) {
			//pass;
		} else {
			fallbackIconFactory = super.getIconFactory();
		}

		org.alice.ide.ProjectDocumentFrame projectDocumentFrame = org.alice.ide.IDE.getActiveInstance().getProjectDocumentFrame();
		if( projectDocumentFrame != null ) {
			return projectDocumentFrame.getIconFactoryManager().getIconFactory( this.field, fallbackIconFactory );
		} else {
			return fallbackIconFactory;
		}
	}

	@Override
	public String getRepr() {
		StringBuilder sb = new StringBuilder();
		sb.append( "this." );
		sb.append( this.field.getName() );
		return sb.toString();
	}
}
