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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class ArgumentListPropertyPane extends org.alice.ide.common.AbstractArgumentListPropertyPane {
	public ArgumentListPropertyPane( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.SimpleArgumentListProperty property ) {
		super( factory, property );
	}

	protected boolean isNameDesired( org.lgna.project.ast.AbstractParameter parameter ) {
		boolean rv;
		if( parameter.getName() != null ) {
			if( parameter instanceof org.lgna.project.ast.JavaMethodParameter ) {
				org.lgna.project.ast.JavaMethodParameter javaMethodParameter = (org.lgna.project.ast.JavaMethodParameter)parameter;
				org.lgna.project.ast.JavaMethod javaMethod = javaMethodParameter.getCode();
				rv = javaMethod.isParameterInShortestChainedMethod( javaMethodParameter ) == false;
			} else if( parameter instanceof org.lgna.project.ast.JavaConstructorParameter ) {
				//todo

				//				org.lgna.project.ast.ParameterDeclaredInJavaConstructor parameterDeclaredInJavaConstructor = (org.lgna.project.ast.ParameterDeclaredInJavaConstructor)parameter;
				//				org.lgna.project.ast.ConstructorDeclaredInJava constructorDeclaredInJava = parameterDeclaredInJavaConstructor.getConstructor();
				//				rv = constructorDeclaredInJava.isParameterInShortestChainedConstructor( parameterDeclaredInJavaConstructor ) == false;

				rv = true;
			} else {
				rv = true;
			}
		} else {
			rv = false;
		}
		return rv;
	}

	@Override
	protected org.lgna.croquet.views.AwtComponentView<?> createComponent( org.lgna.project.ast.SimpleArgument argument ) {
		org.lgna.croquet.views.SwingComponentView<?> prefixPane;
		if( org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava() ) {
			prefixPane = null;
		} else {
			org.lgna.project.ast.AbstractParameter parameter = argument.parameter.getValue();
			boolean isNameDesired = this.isNameDesired( parameter );
			if( isNameDesired ) {
				org.alice.ide.ast.components.DeclarationNameLabel label = new org.alice.ide.ast.components.DeclarationNameLabel( argument.parameter.getValue() );
				label.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
				prefixPane = new org.lgna.croquet.views.LineAxisPanel( org.lgna.croquet.views.BoxUtilities.createHorizontalSliver( 4 ), label, new org.lgna.croquet.views.Label( ": ", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE ) );
			} else {
				prefixPane = null;
			}
		}
		return this.getFactory().createArgumentPane( argument, prefixPane );
	}
}
