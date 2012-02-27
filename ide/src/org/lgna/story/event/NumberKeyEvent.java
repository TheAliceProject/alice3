package org.lgna.story.event;

import org.lgna.story.Key;

public class NumberKeyEvent extends AbstractKeyEvent {
	public static final java.util.List<Key> NUMBERS = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( 
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
		if( key == Key.NUMPAD0 || key == Key.DIGIT_0 ) {
			return 0;
		} else if( key == Key.NUMPAD1 || key == Key.DIGIT_1 ) {
			return 1;
		} else if( key == Key.NUMPAD2 || key == Key.DIGIT_2 ) {
			return 2;
		} else if( key == Key.NUMPAD3 || key == Key.DIGIT_3 ) {
			return 3;
		} else if( key == Key.NUMPAD4 || key == Key.DIGIT_4 ) {
			return 4;
		} else if( key == Key.NUMPAD5 || key == Key.DIGIT_5 ) {
			return 5;
		} else if( key == Key.NUMPAD6 || key == Key.DIGIT_6 ) {
			return 6;
		} else if( key == Key.NUMPAD7 || key == Key.DIGIT_7 ) {
			return 7;
		} else if( key == Key.NUMPAD8 || key == Key.DIGIT_8 ) {
			return 8;
		} else if( key == Key.NUMPAD9 || key == Key.DIGIT_9 ) {
			return 9;
		} else {
			return null;
		}
	}
}