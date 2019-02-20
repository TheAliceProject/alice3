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
package org.alice.stageide.custom;

import org.alice.stageide.apis.org.lgna.story.codecs.KeyCodec;
import org.alice.stageide.custom.components.KeyViewController;
import org.lgna.croquet.Application;
import org.lgna.croquet.PrepModel;
import org.lgna.croquet.SimpleItemState;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.triggers.KeyEventTrigger;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.ComponentManager;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.Key;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class KeyState extends SimpleItemState<Key> {
	private static class SingletonHolder {
		private static KeyState instance = new KeyState();
	}

	public static KeyState getInstance() {
		return SingletonHolder.instance;
	}

	private Key value;

	private KeyState() {
		super( Application.INHERIT_GROUP, UUID.fromString( "2af70d3f-d130-4649-9272-e28c5ca5bc15" ), null, KeyCodec.SINGLETON );
	}

	@Override
	protected void localize() {
	}

	@Override
	protected Key getSwingValue() {
		return this.value;
	}

	private void updateViewControllers() {
		String text;
		if( this.value != null ) {
			text = this.value.toString();
		} else {
			text = null;
		}
		for( AwtComponentView<?> component : ComponentManager.getComponents( this ) ) {
			if( component instanceof KeyViewController ) {
				KeyViewController keyViewController = (KeyViewController)component;
				keyViewController.getAwtComponent().setText( text );
			}
		}
	}

	@Override
	protected void setSwingValue( Key nextValue ) {
		this.value = nextValue;
		this.updateViewControllers();
	}

	@Override
	public List<List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
		return Collections.emptyList();
	}

	public void handleKeyPressed( KeyViewController viewController, KeyEvent e ) {
		Key nextValue = EmployeesOnly.getKeyFromKeyCode( e.getKeyCode() );
		UserActivity activity = KeyEventTrigger.createUserActivity( viewController, e );
		this.value = nextValue;
		this.changeValueFromSwing( this.value, activity );
		this.updateViewControllers();
	}

	public KeyViewController createViewController() {
		return new KeyViewController( this );
	}
}
