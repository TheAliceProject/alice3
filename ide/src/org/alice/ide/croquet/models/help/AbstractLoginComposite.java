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
package org.alice.ide.croquet.models.help;

import java.util.UUID;

import org.alice.ide.croquet.models.help.views.LoginView;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Group;
import org.lgna.croquet.OperationInputDialogCoreComposite;
import org.lgna.croquet.StringState;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;

/**
 * @author Matt May
 */
public abstract class AbstractLoginComposite<V extends LoginView> extends OperationInputDialogCoreComposite<V> {
	private final BooleanState isRememberingState = this.createPreferenceBooleanState( this.createKey( "isRememberingState" ), false );
	protected final StringState userNameState = this.createPreferenceStringState( createKey( "userNameState" ), "", this.isRememberingState );
	protected final StringState passwordState = this.createPreferenceStringState( createKey( "passwordState" ), "", this.isRememberingState, java.util.UUID.fromString( "fa5a952b-d1d2-4c29-80f3-88dec338f8f9" ) );
	protected final BooleanState displayPasswordValue = createBooleanState( createKey( "displayPasswordState" ), false );
	protected final BooleanState isLoggedIn = createBooleanState( createKey( "isLoggedIn" ), false );
	private Status status;
	protected Status loginFailedStatus = createWarningStatus( createKey( "warningLoginFailed" ) );
	private final ActionOperation logOutOperation = createActionOperation( createKey( "logOutOperation" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			logout();
			isLoggedIn.setValueTransactionlessly( false );
			return null;
		}
	} );
	protected LogInOutComposite parent;

	public StringState getUserNameState() {
		return this.userNameState;
	}

	public StringState getPasswordState() {
		return this.passwordState;
	}

	public BooleanState getDisplayPasswordValue() {
		return this.displayPasswordValue;
	}

	public BooleanState getIsLoggedIn() {
		return this.isLoggedIn;
	}

	public BooleanState getIsRememberingState() {
		return this.isRememberingState;
	}

	public AbstractLoginComposite( UUID migrationId, Group operationGroup ) {
		super( migrationId, operationGroup );
	}

	@Override
	protected Edit createEdit( CompletionStep<?> completionStep ) {
		//note: work is done in isClearedForCommit
		return null;
	}

	@Override
	protected boolean isClearedForCommit() {
		if( super.isClearedForCommit() ) {
			boolean loginSuccess = tryToLogin();
			if( loginSuccess ) {
				isLoggedIn.setValueTransactionlessly( true );
				status = IS_GOOD_TO_GO_STATUS;
			} else {
				status = loginFailedStatus;
			}
			refreshStatus();
			return loginSuccess;
		} else {
			return false;
		}
	}

	@Override
	protected V createView() {
		return (V)new LoginView( this );
	}

	@Override
	protected org.lgna.croquet.AbstractSeverityStatusComposite.Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		return this.status;
	}

	protected abstract boolean tryToLogin();

	public abstract void logout();

	public final ActionOperation getLogOutOperation() {
		return logOutOperation;
	}

	public String updateUserNameForWelcomeString() {
		return "";
	}

	public void setParent( LogInOutComposite logInOutComposite ) {
		if( this.parent != null ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, this.parent );
		}
		this.parent = logInOutComposite;
	}

	public LogInOutComposite getParent() {
		return this.parent;
	}
}
