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
package org.alice.ide.x;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractProjectEditorAstI18nFactory extends MutableAstI18nFactory {
	private static final boolean IS_MUTABLE = true;

	public AbstractProjectEditorAstI18nFactory() {
		super( org.lgna.croquet.Application.PROJECT_GROUP );
	}

	@Override
	public java.awt.Paint getInvalidExpressionPaint( java.awt.Paint paint, int x, int y, int width, int height ) {
		return java.awt.Color.RED;
	}

	@Override
	public boolean isSignatureLocked( org.lgna.project.ast.Code code ) {
		if( IS_MUTABLE ) {
			return org.alice.stageide.StoryApiConfigurationManager.getInstance().isSignatureLocked( code );
		} else {
			return true;
		}
	}

	@Override
	protected float getDeclarationNameFontScale() {
		if( Float.isNaN( this.declarationNameFontScale ) ) {
			return super.getDeclarationNameFontScale();
		} else {
			return this.declarationNameFontScale;
		}
	}

	public boolean isDraggable( org.lgna.project.ast.Statement statement ) {
		return IS_MUTABLE;
	}

	@Override
	protected boolean isDropDownDesiredFor( org.lgna.project.ast.ExpressionProperty expressionProperty ) {
		if( IS_MUTABLE ) {
			return super.isDropDownDesiredFor( expressionProperty );
		} else {
			return false;
		}
	}

	@Override
	public org.alice.ide.common.AbstractStatementPane createStatementPane( org.lgna.croquet.DragModel dragModel, org.lgna.project.ast.Statement statement, org.lgna.project.ast.StatementListProperty statementListProperty ) {
		if( this.isDraggable( statement ) ) {
			//pass
		} else {
			dragModel = null;
		}
		return super.createStatementPane( dragModel, statement, statementListProperty );
	}

	public org.lgna.croquet.views.SwingComponentView<?> createCodeHeader( org.lgna.project.ast.UserCode code ) {
		final boolean IS_FORMATTER_READY_FOR_PRIME_TIME = false;
		if( IS_FORMATTER_READY_FOR_PRIME_TIME ) {
			org.alice.ide.formatter.Formatter formatter = org.alice.ide.croquet.models.ui.formatter.FormatterState.getInstance().getValue();
			String headerText = formatter.getHeaderTextForCode( code );
			if( ( headerText != null ) && ( headerText.length() > 0 ) ) {
				org.alice.ide.i18n.Page page = new org.alice.ide.i18n.Page( headerText );
				this.declarationNameFontScale = 1.8f;
				try {
					return this.createComponent( page, code );
				} finally {
					this.declarationNameFontScale = Float.NaN;
				}
			} else {
				return null;
			}
		} else {
			if( code instanceof org.lgna.project.ast.UserMethod ) {
				org.lgna.project.ast.UserMethod userMethod = (org.lgna.project.ast.UserMethod)code;
				return new org.alice.ide.codeeditor.MethodHeaderPane( this, userMethod, false );
			} else if( code instanceof org.lgna.project.ast.NamedUserConstructor ) {
				org.lgna.project.ast.NamedUserConstructor userConstructor = (org.lgna.project.ast.NamedUserConstructor)code;
				return new org.alice.ide.codeeditor.ConstructorHeaderPane( userConstructor, false );
			} else {
				throw new RuntimeException();
			}
		}
	}

	private float declarationNameFontScale = Float.NaN;
}
