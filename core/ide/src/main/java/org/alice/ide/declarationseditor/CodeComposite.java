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

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.IDE;
import org.alice.ide.code.UserFunctionStatusComposite;
import org.alice.ide.declarationseditor.code.components.AbstractCodeDeclarationView;
import org.alice.ide.declarationseditor.code.components.CodeDeclarationView;
import org.alice.ide.declarationseditor.events.components.EventListenersView;
import org.alice.stageide.StageIDE;
import org.lgna.croquet.AbstractSeverityStatusComposite;
import org.lgna.croquet.views.BooleanStateButton;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.UserMethod;

import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class CodeComposite extends DeclarationComposite<AbstractCode, AbstractCodeDeclarationView> {
	private static Map<AbstractCode, CodeComposite> map = Maps.newHashMap();

	public static synchronized CodeComposite getInstance( AbstractCode code ) {
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

	private CodeComposite( AbstractCode code ) {
		super( UUID.fromString( "b8043e06-495b-4f24-9cfb-0e447d97cc7c" ), code, AbstractCode.class );
		if( code instanceof UserMethod ) {
			UserMethod method = (UserMethod)code;
			if( method.isFunction() ) {
				this.userFunctionStatusComposite = new UserFunctionStatusComposite( method );
			} else {
				this.userFunctionStatusComposite = null;
			}
		} else {
			this.userFunctionStatusComposite = null;
		}
		this.handleAstChangeThatCouldBeOfInterest();
	}

	@Override
	public AbstractType<?, ?, ?> getType() {
		return this.getDeclaration().getDeclaringType();
	}

	public UserFunctionStatusComposite getUserFunctionStatusComposite() {
		return this.userFunctionStatusComposite;
	}

	@Override
	public boolean isValid() {
		return this.getDeclaration().isValid();
	}

	@Override
	public boolean isCloseable() {
		IDE ide = IDE.getActiveInstance();
		if( ide != null ) {
			return ide.getApiConfigurationManager().isTabClosable( this.getDeclaration() );
		} else {
			return false;
		}
	}

	@Override
	public void customizeTitleComponentAppearance( BooleanStateButton<?> button ) {
		super.customizeTitleComponentAppearance( button );
		button.scaleFont( 1.2f );
	}

	@Override
	protected AbstractCodeDeclarationView createView() {
		if( StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME.equals( this.getDeclaration().getName() ) ) {
			return new EventListenersView( this );
		} else {
			return new CodeDeclarationView( this );
		}
	}

	public void handleAstChangeThatCouldBeOfInterest() {
		if( this.userFunctionStatusComposite != null ) {
			AbstractSeverityStatusComposite.ErrorStatus prevErrorStatus = this.userFunctionStatusComposite.getErrorStatus();

			AbstractSeverityStatusComposite.ErrorStatus nextErrorStatus;
			AbstractCode code = this.getDeclaration();
			UserMethod method = (UserMethod)code;
			BlockStatement methodBody = method.body.getValue();
			if( methodBody.containsUnreachableCode() ) {
				nextErrorStatus = this.userFunctionStatusComposite.getUnreachableCodeError();
			} else {
				if( methodBody.containsAtLeastOneEnabledReturnStatement() ) {
					if( methodBody.containsAReturnForEveryPath() ) {
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

	private final UserFunctionStatusComposite userFunctionStatusComposite;
}
