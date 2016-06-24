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
package org.lgna.story.event;

import org.lgna.story.Key;

public class NumberKeyEvent extends AbstractKeyEvent {
	public static final java.util.List<Key> NUMBERS = edu.cmu.cs.dennisc.java.util.Lists.newArrayList(
			Key.NUMPAD0, Key.NUMPAD1, Key.NUMPAD2, Key.NUMPAD3, Key.NUMPAD4, Key.NUMPAD5, Key.NUMPAD6, Key.NUMPAD7, Key.NUMPAD8, Key.NUMPAD9,
			Key.DIGIT_0, Key.DIGIT_1, Key.DIGIT_2, Key.DIGIT_3, Key.DIGIT_4, Key.DIGIT_5, Key.DIGIT_6, Key.DIGIT_7, Key.DIGIT_8, Key.DIGIT_9
			);

	public NumberKeyEvent( java.awt.event.KeyEvent e ) {
		super( e );
		char ch = this.getKeyChar();
		assert Character.isDigit( ch ) : ch;
	}

	public NumberKeyEvent( AbstractKeyEvent other ) {
		this( other.getJavaEvent() );
	}

	public Integer getNumber() {
		Key key = this.getKey();
		if( ( key == Key.NUMPAD0 ) || ( key == Key.DIGIT_0 ) ) {
			return 0;
		} else if( ( key == Key.NUMPAD1 ) || ( key == Key.DIGIT_1 ) ) {
			return 1;
		} else if( ( key == Key.NUMPAD2 ) || ( key == Key.DIGIT_2 ) ) {
			return 2;
		} else if( ( key == Key.NUMPAD3 ) || ( key == Key.DIGIT_3 ) ) {
			return 3;
		} else if( ( key == Key.NUMPAD4 ) || ( key == Key.DIGIT_4 ) ) {
			return 4;
		} else if( ( key == Key.NUMPAD5 ) || ( key == Key.DIGIT_5 ) ) {
			return 5;
		} else if( ( key == Key.NUMPAD6 ) || ( key == Key.DIGIT_6 ) ) {
			return 6;
		} else if( ( key == Key.NUMPAD7 ) || ( key == Key.DIGIT_7 ) ) {
			return 7;
		} else if( ( key == Key.NUMPAD8 ) || ( key == Key.DIGIT_8 ) ) {
			return 8;
		} else if( ( key == Key.NUMPAD9 ) || ( key == Key.DIGIT_9 ) ) {
			return 9;
		} else {
			return null;
		}
	}
}
