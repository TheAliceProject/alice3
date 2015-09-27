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

package org.lgna.story;

/**
 * @author Dennis Cosgrove
 */
public enum Key {
	ENTER( java.awt.event.KeyEvent.VK_ENTER ),
	BACK_SPACE( java.awt.event.KeyEvent.VK_BACK_SPACE ),
	TAB( java.awt.event.KeyEvent.VK_TAB ),
	CANCEL( java.awt.event.KeyEvent.VK_CANCEL ),
	CLEAR( java.awt.event.KeyEvent.VK_CLEAR ),
	SHIFT( java.awt.event.KeyEvent.VK_SHIFT ),
	CONTROL( java.awt.event.KeyEvent.VK_CONTROL ),
	ALT( java.awt.event.KeyEvent.VK_ALT ),
	PAUSE( java.awt.event.KeyEvent.VK_PAUSE ),
	CAPS_LOCK( java.awt.event.KeyEvent.VK_CAPS_LOCK ),
	ESCAPE( java.awt.event.KeyEvent.VK_ESCAPE ),
	SPACE( java.awt.event.KeyEvent.VK_SPACE ),
	PAGE_UP( java.awt.event.KeyEvent.VK_PAGE_UP ),
	PAGE_DOWN( java.awt.event.KeyEvent.VK_PAGE_DOWN ),
	END( java.awt.event.KeyEvent.VK_END ),
	HOME( java.awt.event.KeyEvent.VK_HOME ),
	LEFT( java.awt.event.KeyEvent.VK_LEFT ),
	UP( java.awt.event.KeyEvent.VK_UP ),
	RIGHT( java.awt.event.KeyEvent.VK_RIGHT ),
	DOWN( java.awt.event.KeyEvent.VK_DOWN ),
	COMMA( java.awt.event.KeyEvent.VK_COMMA ),
	MINUS( java.awt.event.KeyEvent.VK_MINUS ),
	PERIOD( java.awt.event.KeyEvent.VK_PERIOD ),
	SLASH( java.awt.event.KeyEvent.VK_SLASH ),
	DIGIT_0( java.awt.event.KeyEvent.VK_0 ),
	DIGIT_1( java.awt.event.KeyEvent.VK_1 ),
	DIGIT_2( java.awt.event.KeyEvent.VK_2 ),
	DIGIT_3( java.awt.event.KeyEvent.VK_3 ),
	DIGIT_4( java.awt.event.KeyEvent.VK_4 ),
	DIGIT_5( java.awt.event.KeyEvent.VK_5 ),
	DIGIT_6( java.awt.event.KeyEvent.VK_6 ),
	DIGIT_7( java.awt.event.KeyEvent.VK_7 ),
	DIGIT_8( java.awt.event.KeyEvent.VK_8 ),
	DIGIT_9( java.awt.event.KeyEvent.VK_9 ),
	SEMICOLON( java.awt.event.KeyEvent.VK_SEMICOLON ),
	EQUALS( java.awt.event.KeyEvent.VK_EQUALS ),
	A( java.awt.event.KeyEvent.VK_A ),
	B( java.awt.event.KeyEvent.VK_B ),
	C( java.awt.event.KeyEvent.VK_C ),
	D( java.awt.event.KeyEvent.VK_D ),
	E( java.awt.event.KeyEvent.VK_E ),
	F( java.awt.event.KeyEvent.VK_F ),
	G( java.awt.event.KeyEvent.VK_G ),
	H( java.awt.event.KeyEvent.VK_H ),
	I( java.awt.event.KeyEvent.VK_I ),
	J( java.awt.event.KeyEvent.VK_J ),
	K( java.awt.event.KeyEvent.VK_K ),
	L( java.awt.event.KeyEvent.VK_L ),
	M( java.awt.event.KeyEvent.VK_M ),
	N( java.awt.event.KeyEvent.VK_N ),
	O( java.awt.event.KeyEvent.VK_O ),
	P( java.awt.event.KeyEvent.VK_P ),
	Q( java.awt.event.KeyEvent.VK_Q ),
	R( java.awt.event.KeyEvent.VK_R ),
	S( java.awt.event.KeyEvent.VK_S ),
	T( java.awt.event.KeyEvent.VK_T ),
	U( java.awt.event.KeyEvent.VK_U ),
	V( java.awt.event.KeyEvent.VK_V ),
	W( java.awt.event.KeyEvent.VK_W ),
	X( java.awt.event.KeyEvent.VK_X ),
	Y( java.awt.event.KeyEvent.VK_Y ),
	Z( java.awt.event.KeyEvent.VK_Z ),
	OPEN_BRACKET( java.awt.event.KeyEvent.VK_OPEN_BRACKET ),
	BACK_SLASH( java.awt.event.KeyEvent.VK_BACK_SLASH ),
	CLOSE_BRACKET( java.awt.event.KeyEvent.VK_CLOSE_BRACKET ),
	NUMPAD0( java.awt.event.KeyEvent.VK_NUMPAD0 ),
	NUMPAD1( java.awt.event.KeyEvent.VK_NUMPAD1 ),
	NUMPAD2( java.awt.event.KeyEvent.VK_NUMPAD2 ),
	NUMPAD3( java.awt.event.KeyEvent.VK_NUMPAD3 ),
	NUMPAD4( java.awt.event.KeyEvent.VK_NUMPAD4 ),
	NUMPAD5( java.awt.event.KeyEvent.VK_NUMPAD5 ),
	NUMPAD6( java.awt.event.KeyEvent.VK_NUMPAD6 ),
	NUMPAD7( java.awt.event.KeyEvent.VK_NUMPAD7 ),
	NUMPAD8( java.awt.event.KeyEvent.VK_NUMPAD8 ),
	NUMPAD9( java.awt.event.KeyEvent.VK_NUMPAD9 ),
	MULTIPLY( java.awt.event.KeyEvent.VK_MULTIPLY ),
	ADD( java.awt.event.KeyEvent.VK_ADD ),
	SEPARATER( java.awt.event.KeyEvent.VK_SEPARATER ),
	SEPARATOR( java.awt.event.KeyEvent.VK_SEPARATOR ),
	SUBTRACT( java.awt.event.KeyEvent.VK_SUBTRACT ),
	DECIMAL( java.awt.event.KeyEvent.VK_DECIMAL ),
	DIVIDE( java.awt.event.KeyEvent.VK_DIVIDE ),
	DELETE( java.awt.event.KeyEvent.VK_DELETE ),
	NUM_LOCK( java.awt.event.KeyEvent.VK_NUM_LOCK ),
	SCROLL_LOCK( java.awt.event.KeyEvent.VK_SCROLL_LOCK ),
	F1( java.awt.event.KeyEvent.VK_F1 ),
	F2( java.awt.event.KeyEvent.VK_F2 ),
	F3( java.awt.event.KeyEvent.VK_F3 ),
	F4( java.awt.event.KeyEvent.VK_F4 ),
	F5( java.awt.event.KeyEvent.VK_F5 ),
	F6( java.awt.event.KeyEvent.VK_F6 ),
	F7( java.awt.event.KeyEvent.VK_F7 ),
	F8( java.awt.event.KeyEvent.VK_F8 ),
	F9( java.awt.event.KeyEvent.VK_F9 ),
	F10( java.awt.event.KeyEvent.VK_F10 ),
	F11( java.awt.event.KeyEvent.VK_F11 ),
	F12( java.awt.event.KeyEvent.VK_F12 ),
	F13( java.awt.event.KeyEvent.VK_F13 ),
	F14( java.awt.event.KeyEvent.VK_F14 ),
	F15( java.awt.event.KeyEvent.VK_F15 ),
	F16( java.awt.event.KeyEvent.VK_F16 ),
	F17( java.awt.event.KeyEvent.VK_F17 ),
	F18( java.awt.event.KeyEvent.VK_F18 ),
	F19( java.awt.event.KeyEvent.VK_F19 ),
	F20( java.awt.event.KeyEvent.VK_F20 ),
	F21( java.awt.event.KeyEvent.VK_F21 ),
	F22( java.awt.event.KeyEvent.VK_F22 ),
	F23( java.awt.event.KeyEvent.VK_F23 ),
	F24( java.awt.event.KeyEvent.VK_F24 ),
	PRINTSCREEN( java.awt.event.KeyEvent.VK_PRINTSCREEN ),
	INSERT( java.awt.event.KeyEvent.VK_INSERT ),
	HELP( java.awt.event.KeyEvent.VK_HELP ),
	META( java.awt.event.KeyEvent.VK_META ),
	BACK_QUOTE( java.awt.event.KeyEvent.VK_BACK_QUOTE ),
	QUOTE( java.awt.event.KeyEvent.VK_QUOTE ),
	KP_UP( java.awt.event.KeyEvent.VK_KP_UP ),
	KP_DOWN( java.awt.event.KeyEvent.VK_KP_DOWN ),
	KP_LEFT( java.awt.event.KeyEvent.VK_KP_LEFT ),
	KP_RIGHT( java.awt.event.KeyEvent.VK_KP_RIGHT ),
	DEAD_GRAVE( java.awt.event.KeyEvent.VK_DEAD_GRAVE ),
	DEAD_ACUTE( java.awt.event.KeyEvent.VK_DEAD_ACUTE ),
	DEAD_CIRCUMFLEX( java.awt.event.KeyEvent.VK_DEAD_CIRCUMFLEX ),
	DEAD_TILDE( java.awt.event.KeyEvent.VK_DEAD_TILDE ),
	DEAD_MACRON( java.awt.event.KeyEvent.VK_DEAD_MACRON ),
	DEAD_BREVE( java.awt.event.KeyEvent.VK_DEAD_BREVE ),
	DEAD_ABOVEDOT( java.awt.event.KeyEvent.VK_DEAD_ABOVEDOT ),
	DEAD_DIAERESIS( java.awt.event.KeyEvent.VK_DEAD_DIAERESIS ),
	DEAD_ABOVERING( java.awt.event.KeyEvent.VK_DEAD_ABOVERING ),
	DEAD_DOUBLEACUTE( java.awt.event.KeyEvent.VK_DEAD_DOUBLEACUTE ),
	DEAD_CARON( java.awt.event.KeyEvent.VK_DEAD_CARON ),
	DEAD_CEDILLA( java.awt.event.KeyEvent.VK_DEAD_CEDILLA ),
	DEAD_OGONEK( java.awt.event.KeyEvent.VK_DEAD_OGONEK ),
	DEAD_IOTA( java.awt.event.KeyEvent.VK_DEAD_IOTA ),
	DEAD_VOICED_SOUND( java.awt.event.KeyEvent.VK_DEAD_VOICED_SOUND ),
	DEAD_SEMIVOICED_SOUND( java.awt.event.KeyEvent.VK_DEAD_SEMIVOICED_SOUND ),
	AMPERSAND( java.awt.event.KeyEvent.VK_AMPERSAND ),
	ASTERISK( java.awt.event.KeyEvent.VK_ASTERISK ),
	QUOTEDBL( java.awt.event.KeyEvent.VK_QUOTEDBL ),
	LESS( java.awt.event.KeyEvent.VK_LESS ),
	GREATER( java.awt.event.KeyEvent.VK_GREATER ),
	BRACELEFT( java.awt.event.KeyEvent.VK_BRACELEFT ),
	BRACERIGHT( java.awt.event.KeyEvent.VK_BRACERIGHT ),
	AT( java.awt.event.KeyEvent.VK_AT ),
	COLON( java.awt.event.KeyEvent.VK_COLON ),
	CIRCUMFLEX( java.awt.event.KeyEvent.VK_CIRCUMFLEX ),
	DOLLAR( java.awt.event.KeyEvent.VK_DOLLAR ),
	EURO_SIGN( java.awt.event.KeyEvent.VK_EURO_SIGN ),
	EXCLAMATION_MARK( java.awt.event.KeyEvent.VK_EXCLAMATION_MARK ),
	INVERTED_EXCLAMATION_MARK( java.awt.event.KeyEvent.VK_INVERTED_EXCLAMATION_MARK ),
	LEFT_PARENTHESIS( java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS ),
	NUMBER_SIGN( java.awt.event.KeyEvent.VK_NUMBER_SIGN ),
	PLUS( java.awt.event.KeyEvent.VK_PLUS ),
	RIGHT_PARENTHESIS( java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS ),
	UNDERSCORE( java.awt.event.KeyEvent.VK_UNDERSCORE ),
	WINDOWS( java.awt.event.KeyEvent.VK_WINDOWS ),
	CONTEXT_MENU( java.awt.event.KeyEvent.VK_CONTEXT_MENU ),
	FINAL( java.awt.event.KeyEvent.VK_FINAL ),
	CONVERT( java.awt.event.KeyEvent.VK_CONVERT ),
	NONCONVERT( java.awt.event.KeyEvent.VK_NONCONVERT ),
	ACCEPT( java.awt.event.KeyEvent.VK_ACCEPT ),
	MODECHANGE( java.awt.event.KeyEvent.VK_MODECHANGE ),
	KANA( java.awt.event.KeyEvent.VK_KANA ),
	KANJI( java.awt.event.KeyEvent.VK_KANJI ),
	ALPHANUMERIC( java.awt.event.KeyEvent.VK_ALPHANUMERIC ),
	KATAKANA( java.awt.event.KeyEvent.VK_KATAKANA ),
	HIRAGANA( java.awt.event.KeyEvent.VK_HIRAGANA ),
	FULL_WIDTH( java.awt.event.KeyEvent.VK_FULL_WIDTH ),
	HALF_WIDTH( java.awt.event.KeyEvent.VK_HALF_WIDTH ),
	ROMAN_CHARACTERS( java.awt.event.KeyEvent.VK_ROMAN_CHARACTERS ),
	ALL_CANDIDATES( java.awt.event.KeyEvent.VK_ALL_CANDIDATES ),
	PREVIOUS_CANDIDATE( java.awt.event.KeyEvent.VK_PREVIOUS_CANDIDATE ),
	CODE_INPUT( java.awt.event.KeyEvent.VK_CODE_INPUT ),
	JAPANESE_KATAKANA( java.awt.event.KeyEvent.VK_JAPANESE_KATAKANA ),
	JAPANESE_HIRAGANA( java.awt.event.KeyEvent.VK_JAPANESE_HIRAGANA ),
	JAPANESE_ROMAN( java.awt.event.KeyEvent.VK_JAPANESE_ROMAN ),
	KANA_LOCK( java.awt.event.KeyEvent.VK_KANA_LOCK ),
	INPUT_METHOD_ON_OFF( java.awt.event.KeyEvent.VK_INPUT_METHOD_ON_OFF ),
	CUT( java.awt.event.KeyEvent.VK_CUT ),
	COPY( java.awt.event.KeyEvent.VK_COPY ),
	PASTE( java.awt.event.KeyEvent.VK_PASTE ),
	UNDO( java.awt.event.KeyEvent.VK_UNDO ),
	AGAIN( java.awt.event.KeyEvent.VK_AGAIN ),
	FIND( java.awt.event.KeyEvent.VK_FIND ),
	PROPS( java.awt.event.KeyEvent.VK_PROPS ),
	STOP( java.awt.event.KeyEvent.VK_STOP ),
	COMPOSE( java.awt.event.KeyEvent.VK_COMPOSE ),
	ALT_GRAPH( java.awt.event.KeyEvent.VK_ALT_GRAPH ),
	BEGIN( java.awt.event.KeyEvent.VK_BEGIN ),
	UNDEFINED( java.awt.event.KeyEvent.VK_UNDEFINED );

	private static class MapContainer {
		private static java.util.Map<Integer, Key> map = new java.util.HashMap<Integer, Key>();
	}

	private int keyCode;

	private Key( int keyCode ) {
		this.keyCode = keyCode;
		MapContainer.map.put( this.keyCode, this );
	}

	/* package-private */int getInternal() {
		return this.keyCode;
	}

	/* package-private */static Key getInstanceFromKeyCode( int keyCode ) {
		return MapContainer.map.get( keyCode );
	}
}
