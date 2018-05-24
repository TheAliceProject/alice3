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
package org.alice.ide.code;

import org.alice.ide.code.views.UserFunctionStatusView;
import org.lgna.croquet.AbstractSeverityStatusComposite;
import org.lgna.croquet.PlainStringValue;
import org.lgna.project.ast.UserMethod;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class UserFunctionStatusComposite extends AbstractSeverityStatusComposite<UserFunctionStatusView> {
	private static final String UNVIEWED_TEXT = "This message should never be visible";

	private static final class UnlocalizedPlainStringValue extends PlainStringValue {
		public UnlocalizedPlainStringValue( String text ) {
			super( UUID.randomUUID() );
			this.setText( text );
		}

		@Override
		protected void localize() {
		}
	}

	private final PlainStringValue metaStringValue = new UnlocalizedPlainStringValue( UNVIEWED_TEXT );
	private final ErrorStatus noReturnStatementError = this.createErrorStatus( "noReturnStatementError" );
	private final ErrorStatus notAllPathsEndInReturnStatementError = this.createErrorStatus( "notAllPathsEndInReturnStatementError" );
	private final ErrorStatus unreachableCodeError = this.createErrorStatus( "unreachableCodeError" );

	private final UserMethod method;
	private ErrorStatus errorStatus;

	public UserFunctionStatusComposite( UserMethod method ) {
		super( UUID.fromString( "5247e4d2-1de0-45b0-88f4-5a8667cfb60d" ) );
		this.method = method;
		this.getView().setVisible( false );
	}

	public PlainStringValue getMetaStringValue() {
		return this.metaStringValue;
	}

	public ErrorStatus getNoReturnStatementError() {
		return this.noReturnStatementError;
	}

	public ErrorStatus getNotAllPathsEndInReturnStatementError() {
		return this.notAllPathsEndInReturnStatementError;
	}

	public ErrorStatus getUnreachableCodeError() {
		return this.unreachableCodeError;
	}

	public UserMethod getMethod() {
		return this.method;
	}

	public ErrorStatus getErrorStatus() {
		return this.errorStatus;
	}

	public void setErrorStatus( ErrorStatus errorStatus ) {
		this.initializeIfNecessary();
		if( this.errorStatus != errorStatus ) {
			this.errorStatus = errorStatus;
			String text;
			if( errorStatus != null ) {
				text = errorStatus.getText();
			} else {
				text = UNVIEWED_TEXT;
			}
			synchronized( this.getView().getTreeLock() ) {
				this.getView().setVisible( errorStatus != null );
			}
			this.metaStringValue.setText( text );
		}
	}

	@Override
	protected UserFunctionStatusView createView() {
		return new UserFunctionStatusView( this );
	}
}
