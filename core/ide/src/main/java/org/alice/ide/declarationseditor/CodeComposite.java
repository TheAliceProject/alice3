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
package org.alice.ide.declarationseditor;

/**
 * @author Dennis Cosgrove
 */
public class CodeComposite extends DeclarationComposite<org.lgna.project.ast.AbstractCode, org.alice.ide.declarationseditor.code.components.AbstractCodeDeclarationView> {
	private static java.util.Map<org.lgna.project.ast.AbstractCode, CodeComposite> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized CodeComposite getInstance( org.lgna.project.ast.AbstractCode code ) {
		if( code != null ) {
			CodeComposite rv = map.get( code );
			if( rv != null ) {
				//pass
			} else {
				rv = new CodeComposite( code );
				map.put( code, rv );
			}
			return rv;
		} else {
			return null;
		}
	}

	private CodeComposite( org.lgna.project.ast.AbstractCode code ) {
		super( java.util.UUID.fromString( "b8043e06-495b-4f24-9cfb-0e447d97cc7c" ), code, org.lgna.project.ast.AbstractCode.class );
		if( code instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)code;
			if( method.isFunction() ) {
				this.userFunctionStatusComposite = new org.alice.ide.code.UserFunctionStatusComposite( method );
			} else {
				this.userFunctionStatusComposite = null;
			}
		} else {
			this.userFunctionStatusComposite = null;
		}
		this.handleAstChangeThatCouldBeOfInterest();
	}

	@Override
	public org.lgna.project.ast.AbstractType<?, ?, ?> getType() {
		return this.getDeclaration().getDeclaringType();
	}

	public org.alice.ide.code.UserFunctionStatusComposite getUserFunctionStatusComposite() {
		return this.userFunctionStatusComposite;
	}

	@Override
	public boolean isValid() {
		return this.getDeclaration().isValid();
	}

	@Override
	public boolean isCloseable() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide != null ) {
			return ide.getApiConfigurationManager().isTabClosable( this.getDeclaration() );
		} else {
			return false;
		}
	}

	@Override
	public void customizeTitleComponentAppearance( org.lgna.croquet.views.BooleanStateButton<?> button ) {
		super.customizeTitleComponentAppearance( button );
		button.scaleFont( 1.2f );
	}

	@Override
	protected org.alice.ide.declarationseditor.code.components.AbstractCodeDeclarationView createView() {
		if( org.alice.stageide.StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME.equals( this.getDeclaration().getName() ) ) {
			return new org.alice.ide.declarationseditor.events.components.EventListenersView( this );
		} else {
			return new org.alice.ide.declarationseditor.code.components.CodeDeclarationView( this );
		}
	}

	public void handleAstChangeThatCouldBeOfInterest() {
		if( this.userFunctionStatusComposite != null ) {
			org.lgna.croquet.AbstractSeverityStatusComposite.ErrorStatus prevErrorStatus = this.userFunctionStatusComposite.getErrorStatus();

			org.lgna.croquet.AbstractSeverityStatusComposite.ErrorStatus nextErrorStatus;
			org.lgna.project.ast.AbstractCode code = this.getDeclaration();
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)code;
			if( org.lgna.project.ast.StaticAnalysisUtilities.containsUnreachableCode( method ) ) {
				nextErrorStatus = this.userFunctionStatusComposite.getUnreachableCodeError();
			} else {
				if( org.lgna.project.ast.StaticAnalysisUtilities.containsAtLeastOneEnabledReturnStatement( method ) ) {
					if( org.lgna.project.ast.StaticAnalysisUtilities.containsAReturnForEveryPath( method ) ) {
						nextErrorStatus = null;
					} else {
						nextErrorStatus = this.userFunctionStatusComposite.getNotAllPathsEndInReturnStatementError();
					}
				} else {
					nextErrorStatus = this.userFunctionStatusComposite.getNoReturnStatementError();
				}
			}
			if( prevErrorStatus != nextErrorStatus ) {
				this.userFunctionStatusComposite.setErrorStatus( nextErrorStatus );
				this.getView().revalidateAndRepaint();
			}
		}
	}

	private final org.alice.ide.code.UserFunctionStatusComposite userFunctionStatusComposite;
}
