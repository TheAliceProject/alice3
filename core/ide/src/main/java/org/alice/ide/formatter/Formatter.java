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
package org.alice.ide.formatter;

import org.alice.stageide.modelresource.ClassResourceKey;

/**
 * Formats code expressed in org.lgna.project.ast.Statements.
 * Used to display the code on Alice tiles.
 * Concrete subclasses apply specific language choices.
 *
 * @author Dennis Cosgrove
 */
public abstract class Formatter {
	public Formatter( String repr ) {
		this.repr = repr;
	}

	public abstract String getHeaderTextForCode( org.lgna.project.ast.UserCode code );

	public abstract String getTrailerTextForCode( org.lgna.project.ast.UserCode code );

	public String getTemplateText( Class<?> cls ) {
		return edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "org.alice.ide.formatter.Templates");
	}

	public String getInfixExpressionText( org.lgna.project.ast.InfixExpression<?> infixExpression ) {
		String clsName = infixExpression.getClass().getName();
		java.util.ResourceBundle resourceBundle = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getUtf8Bundle( clsName, javax.swing.JComponent.getDefaultLocale() );
		Enum<?> e = infixExpression.operator.getValue();
		return resourceBundle.getString( e.name() );
	}

	public String getNameForDeclaration( org.lgna.project.ast.AbstractDeclaration declaration ) {
		return declaration.formatName(this::localizeName);
	}

	protected abstract String localizeName(String key, String name);

	public abstract boolean isTypeExpressionDesired();

	public abstract String getTextForThis();

	public abstract String getTextForNull();

	public String getTextForType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return type == null ? getTextForNull() : type.formatName(this::localizeName);
	}

	public abstract String getFinalText();

	@Override
	public String toString() {
		return this.repr;
	}

	private final String repr;

	public String galleryLabelFor( ClassResourceKey key) {
		String className = key.getSearchText();
		if (key.getType().isEnum()) {
			String params = key.isLeaf() ? "" : "\u2423";
			return String.format(getNewFormat(), className, params);
		} else {
			return String.format( getClassesFormat(), className );
		}
	}

	protected abstract String getClassesFormat();

	public abstract String getNewFormat();
}
