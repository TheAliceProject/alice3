/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.croquet.models.project.find.core;

import java.util.List;

import javax.swing.Icon;

import org.alice.ide.IDE;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.LocalAccess;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.ParameterAccess;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserParameter;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class SearchObject<T extends AbstractDeclaration> {

	private final Class<T> cls;
	private final T searchObject;
	private final List<Expression> references = Collections.newArrayList();

	@SuppressWarnings( "unchecked" )
	public static final List<Class<? extends AbstractDeclaration>> clsList =
			Collections.newArrayList( AbstractField.class, AbstractMethod.class, UserParameter.class, UserLocal.class );

	public SearchObject( Class<T> cls, T object ) {
		assert clsList.contains( cls );
		this.cls = cls;
		this.searchObject = object;
	}

	public T getSearchObject() {
		return this.searchObject;
	}

	public Class<T> getObjectType() {
		return cls;
	}

	public String getName() {
		return ( (AbstractDeclaration)searchObject ).getName();
	}

	@Override
	public String toString() {
		return getName() + " (" + references.size() + ")";
	}

	public void addReference( Expression reference ) {
		references.add( reference );
	}

	public List<Expression> getReferences() {
		return references;
	}

	public Icon getIcon() {
		if( this.searchObject instanceof org.lgna.project.ast.AbstractMethod ) {
			org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)this.searchObject;
			if( method.isProcedure() ) {
				return org.alice.ide.declarationseditor.DeclarationTabState.getProcedureIcon();
			} else {
				return org.alice.ide.declarationseditor.DeclarationTabState.getFunctionIcon();
			}
		} else if( this.searchObject instanceof org.lgna.project.ast.AbstractField ) {
			return org.alice.ide.declarationseditor.DeclarationTabState.getFieldIcon();
		} else if( this.searchObject instanceof org.lgna.project.ast.AbstractConstructor ) {
			return org.alice.ide.declarationseditor.DeclarationTabState.getConstructorIcon();
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.searchObject );
			return null;
		}
	}

	public void stencilHighlightForReference( Expression reference ) {
		assert reference != null;
		if( searchObject instanceof AbstractField ) {
			assert reference instanceof FieldAccess;
			IDE.getActiveInstance().getHighlightStencil().showHighlightOverExpression( reference, "" );
		} else if( searchObject instanceof AbstractMethod ) {
			assert reference instanceof MethodInvocation;
			Statement statement = reference.getFirstAncestorAssignableTo( Statement.class );
			assert statement != null;
			IDE.getActiveInstance().getHighlightStencil().showHighlightOverStatement( statement, "" );
		} else if( searchObject instanceof UserParameter ) {
			assert reference instanceof ParameterAccess;
			IDE.getActiveInstance().getHighlightStencil().showHighlightOverExpression( reference, "" );
		} else if( searchObject instanceof UserLocal ) {
			assert reference instanceof LocalAccess;
			IDE.getActiveInstance().getHighlightStencil().showHighlightOverExpression( reference, "" );
		} else {
			assert false : searchObject.getClass();
		}
	}
}
