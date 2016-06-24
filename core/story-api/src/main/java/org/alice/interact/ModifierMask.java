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
package org.alice.interact;

import java.awt.event.KeyEvent;

import edu.cmu.cs.dennisc.java.awt.event.KeyEventUtilities;

/**
 * @author David Culyba
 */
public final class ModifierMask {
	private static enum TestType {
		ALL_MUST_BE_VALID,
		ANY_MAY_BE_VALID,
	}

	public static ModifierKey[] NO_MODIFIERS_DOWN = { ModifierKey.NOT_CONTROL, ModifierKey.NOT_ALT, ModifierKey.NOT_SHIFT };
	public static ModifierKey[] JUST_SHIFT = { ModifierKey.NOT_CONTROL, ModifierKey.NOT_ALT, ModifierKey.SHIFT };
	public static ModifierKey[] JUST_CONTROL = { ModifierKey.CONTROL, ModifierKey.NOT_ALT, ModifierKey.NOT_SHIFT };
	public static ModifierKey[] JUST_ALT = { ModifierKey.NOT_CONTROL, ModifierKey.ALT, ModifierKey.NOT_SHIFT };

	public static enum ModifierKey {
		CONTROL( KeyEventUtilities.getQuoteControlUnquoteKey(), false ),
		NOT_CONTROL( KeyEventUtilities.getQuoteControlUnquoteKey(), true ),
		ALT( KeyEventUtilities.getQuoteAltUnquoteKey(), false ),
		NOT_ALT( KeyEventUtilities.getQuoteAltUnquoteKey(), true ),
		SHIFT( KeyEvent.VK_SHIFT, false ),
		NOT_SHIFT( KeyEvent.VK_SHIFT, true );

		private ModifierKey( int keyValue, boolean inverted ) {
			this.keyValue = keyValue;
			this.inverted = inverted;
		}

		public int getKeyValue() {
			return this.keyValue;
		}

		public boolean testKey( InputState state ) {
			boolean isDown = state.isKeyDown( this.keyValue );
			if( this.inverted ) {
				return !isDown;
			} else {
				return isDown;
			}
		}

		private final int keyValue;
		private final boolean inverted;
	}

	public ModifierMask() {
		this( new ModifierKey[ 0 ], TestType.ALL_MUST_BE_VALID );
	}

	public ModifierMask( ModifierKey[] keys, TestType testType ) {
		this.testType = testType;
		this.keys = keys;
	}

	public ModifierMask( ModifierKey[] keys ) {
		this( keys, TestType.ALL_MUST_BE_VALID );
	}

	public ModifierMask( ModifierKey key ) {
		this( key, TestType.ALL_MUST_BE_VALID );
	}

	public ModifierMask( ModifierKey key, TestType testType ) {
		this( new ModifierKey[] { key }, testType );
	}

	public boolean anyValid( InputState state ) {
		for( ModifierKey key : this.keys ) {
			if( key.testKey( state ) ) {
				return true;
			}
		}
		return false;
	}

	public boolean allValid( InputState state ) {
		for( int i = 0; i < this.keys.length; i++ ) {
			if( !this.keys[ i ].testKey( state ) ) {
				return false;
			}
		}
		return true;
	}

	public boolean test( InputState state ) {
		switch( this.testType ) {
		case ANY_MAY_BE_VALID:
			return anyValid( state );
		case ALL_MUST_BE_VALID:
			return allValid( state );
		default:
			return false;
		}
	}

	private final ModifierKey[] keys;
	private final TestType testType;
}
