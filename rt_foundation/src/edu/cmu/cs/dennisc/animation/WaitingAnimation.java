/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.animation;

/**
 * @author Dennis Cosgrove
 */
public class WaitingAnimation {
	private Animation m_animation;
	private AnimationObserver m_animationObserver;
	private Thread m_thread;
	private Exception m_exception;

	public WaitingAnimation( Animation animation, AnimationObserver animationObserver, Thread thread ) {
		m_animation = animation;
		m_animationObserver = animationObserver;
		m_thread = thread;
	}
	public Animation getAnimation() {
		return m_animation;
	}
	public AnimationObserver getAnimationObserver() {
		return m_animationObserver;
	}
	public Thread getThread() {
		return m_thread;
	}
	public Exception getException() {
		return m_exception;
	}
	public void setException( Exception exception ) {
		m_exception = exception;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( this.getClass().getName() );
		sb.append( "[animation=" );
		sb.append( m_animation );
		sb.append( ";observer=" );
		sb.append( m_animationObserver );
		sb.append( ";thread=" );
		sb.append( m_thread );
		sb.append( "]" );
		return sb.toString();
	}
}
