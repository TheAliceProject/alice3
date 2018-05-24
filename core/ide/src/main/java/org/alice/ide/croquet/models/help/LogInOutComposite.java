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
package org.alice.ide.croquet.models.help;

import java.util.UUID;

import org.lgna.croquet.CardOwnerComposite;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;

/**
 * @author Matt May
 */
public class LogInOutComposite extends CardOwnerComposite {

	private final LogInCard logInCard;
	private final LogOutCard logOutCard;
	private AbstractLoginComposite composite;
	private final ValueListener<Boolean> isLoggedInListener = new ValueListener<Boolean>() {
		@Override
		public void valueChanged( ValueEvent<Boolean> e ) {
			updateLogInOutComposite();
		}
	};

	public LogInOutComposite( UUID id, AbstractLoginComposite loginComposite ) {
		super( id );
		this.composite = loginComposite;
		this.logInCard = new LogInCard( loginComposite );
		this.logOutCard = new LogOutCard( loginComposite.getLogOutOperation() );

		this.addCard( this.logInCard );
		this.addCard( this.logOutCard );

		//todo: move to activate/deactivate
		composite.getIsLoggedIn().addNewSchoolValueListener( isLoggedInListener );
	}

	private void updateLogInOutComposite() {
		if( composite.getIsLoggedIn().getValue() ) {
			logOutCard.updateWelcomeString( composite.updateUserNameForWelcomeString() );
			this.showCard( logOutCard );
		} else {
			this.showCard( logInCard );
		}
		this.getView().getAwtComponent().repaint();
	}

	public AbstractLoginComposite getComposite() {
		return this.composite;
	}

	public LogOutCard getLogOutCard() {
		return this.logOutCard;
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		Thread loginThread = new Thread( new Runnable() {

			@Override
			public void run() {
				if( composite.getIsRememberingState().getValue() && ( composite.getUserNameState().getValue().length() > 0 ) && ( composite.getPasswordState().getValue().length() > 0 ) ) {
					composite.isClearedForCommit();
				}
			}
		} );
		loginThread.start();
	}

	public boolean getCanConnect() {
		return composite.getCanLogIn();
	}

	public void addListener( LogInOutListener listener ) {
		composite.addListener( listener );
	}
}
