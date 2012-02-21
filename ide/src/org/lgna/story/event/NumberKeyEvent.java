package org.lgna.story.event;

import java.util.List;

import org.lgna.story.Key;

import edu.cmu.cs.dennisc.java.util.Collections;

public class NumberKeyEvent extends KeyEvent {
	public static final List<Key> NUMBERS = Collections.newArrayList(
			Key.NUMPAD0, Key.NUMPAD1, Key.NUMPAD2, Key.NUMPAD3, Key.NUMPAD4, Key.NUMPAD5, Key.NUMPAD6, Key.NUMPAD7, Key.NUMPAD8, Key.NUMPAD9,
			Key.DIGIT_0, Key.DIGIT_1, Key.DIGIT_2, Key.DIGIT_3, Key.DIGIT_4, Key.DIGIT_5, Key.DIGIT_6, Key.DIGIT_7, Key.DIGIT_8, Key.DIGIT_9
			);
	private KeyEvent e;
	public NumberKeyEvent( KeyEvent e ) {
		super( e.getJavaEvent() );
		assert e.isDigit();
		this.e = e;
	}
	
	public Integer getNumber(){
		if ( e.getKey() == Key.NUMPAD0 || e.getKey() == Key.DIGIT_0 ) {
			return 0;
		} else if ( e.getKey() == Key.NUMPAD1 || e.getKey() == Key.DIGIT_1 ) {
			return 1;
		} else if ( e.getKey() == Key.NUMPAD2 || e.getKey() == Key.DIGIT_2 ) {
			return 2;
		} else if ( e.getKey() == Key.NUMPAD3 || e.getKey() == Key.DIGIT_3 ) {
			return 3;
		} else if ( e.getKey() == Key.NUMPAD4 || e.getKey() == Key.DIGIT_4 ) {
			return 4;
		} else if ( e.getKey() == Key.NUMPAD5 || e.getKey() == Key.DIGIT_5 ) {
			return 5;
		} else if ( e.getKey() == Key.NUMPAD6 || e.getKey() == Key.DIGIT_6 ) {
			return 6;
		} else if ( e.getKey() == Key.NUMPAD7 || e.getKey() == Key.DIGIT_7 ) {
			return 7;
		} else if ( e.getKey() == Key.NUMPAD8 || e.getKey() == Key.DIGIT_8 ) {
			return 8;
		} else if ( e.getKey() == Key.NUMPAD9 || e.getKey() == Key.DIGIT_9 ) {
			return 9;
		} else {
			return null;
		}
	}
}