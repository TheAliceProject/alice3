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
public abstract class AbstractTypeMethodsPane extends AbstractTypeMembersPane {
	public AbstractTypeMethodsPane( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		super( type );
	}
	protected abstract org.lgna.croquet.components.Component< ? > createProcedureTemplate( org.lgna.project.ast.AbstractMethod method );
	protected abstract org.lgna.croquet.components.Component< ? > createFunctionTemplate( org.lgna.project.ast.AbstractMethod method );

	@Override
	protected Iterable< org.lgna.croquet.components.Component< ? >> createTemplates( org.lgna.project.ast.JavaGetterSetterPair getterSetterPair ) {
		return null;
	}
	@Override
	protected Iterable< org.lgna.croquet.components.Component< ? > > createTemplates( org.lgna.project.ast.AbstractMember member ) {
		org.lgna.croquet.components.Component< ? > component;
		if( member instanceof org.lgna.project.ast.AbstractMethod ) {
			org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)member;
			if( method.getNextShorterInChain() != null ) {
				component = null;
			} else {
				if( method.isProcedure() ) {
					component = createProcedureTemplate( method );
				} else if( method.isFunction() ) {
					component = createFunctionTemplate( method );
				} else {
					component = null;
				}
			}
		} else {
			component = null;
		}
		java.util.List< org.lgna.croquet.components.Component< ? > > rv;
		if( component != null ) {
			//line.add( javax.swing.Box.createHorizontalStrut( INDENT ) );
			//if( member.isDeclaredInAlice() ) {
			if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
				//pass
			} else {
				if( member instanceof org.lgna.project.ast.AbstractCode ) {
					org.lgna.project.ast.AbstractCode code = (org.lgna.project.ast.AbstractCode)member;
					if( code.isUserAuthored() ) {
						org.lgna.croquet.components.LineAxisPanel line = new org.lgna.croquet.components.LineAxisPanel();
						line.addComponent( org.alice.ide.operations.ast.FocusCodeOperation.getInstance( code ).createButton() );
						line.addComponent( component );
						component = line;
					}
				}
			}
			rv = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( new org.lgna.croquet.components.Component< ? >[] { component } );
		} else {
			rv = null;
		}
		return rv;
	}
}
