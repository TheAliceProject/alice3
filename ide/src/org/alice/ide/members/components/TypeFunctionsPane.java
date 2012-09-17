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
package org.alice.ide.members.components;

/**
 * @author Dennis Cosgrove
 */
public class TypeFunctionsPane extends AbstractTypeMethodsPane {
	public TypeFunctionsPane( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		super( type );
	}

	@Override
	protected edu.cmu.cs.dennisc.property.ListProperty<? extends org.lgna.project.ast.UserMember>[] getListPropertiesToListenTo( org.lgna.project.ast.NamedUserType type ) {
		return new edu.cmu.cs.dennisc.property.ListProperty[] { type.methods, type.constructors };
	}

	@Override
	protected org.lgna.croquet.components.Button createDeclareMemberButton( org.lgna.project.ast.NamedUserType type ) {
		return org.alice.ide.ast.declaration.AddFunctionComposite.getInstance( type ).getOperation().createButton();
	}

	@Override
	protected org.lgna.croquet.components.Button createEditConstructorButton( org.lgna.project.ast.NamedUserType type ) {
		org.lgna.project.ast.NamedUserConstructor constructor = type.getDeclaredConstructor();
		if( constructor != null ) {
			return org.alice.ide.declarationseditor.DeclarationTabState.getInstance().getItemSelectionOperation( constructor ).createButton();
		} else {
			return null;
		}
	}

	@Override
	protected org.lgna.croquet.components.Component<?> createFunctionTemplate( org.lgna.project.ast.AbstractMethod method ) {
		return org.alice.ide.members.components.templates.TemplateFactory.getFunctionInvocationTemplate( method );
	}

	@Override
	protected org.lgna.croquet.components.Component<?> createProcedureTemplate( org.lgna.project.ast.AbstractMethod method ) {
		return null;
	}
}
