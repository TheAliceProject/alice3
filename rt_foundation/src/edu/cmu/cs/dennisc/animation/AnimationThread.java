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

//todo?
/**
 * @author Dennis Cosgrove
 */
@Deprecated
public class AnimationThread extends edu.cmu.cs.dennisc.lang.ThreadWithRevealingToString {
	private Animator m_animator;
	private Animation m_animation;
	private AnimationObserver m_animationObserver;
	public AnimationThread( Animator animator, Animation animation, AnimationObserver animationObserver ) {
		m_animator = animator;
		m_animation = animation;
		m_animationObserver = animationObserver;
	}
	@Override
	public void run() {
		m_animator.invokeAndWait_ThrowRuntimeExceptionsIfNecessary( m_animation, m_animationObserver );
	}
	
	@Override
	protected StringBuffer updateRepr(StringBuffer rv) {
		rv = super.updateRepr(rv);
		rv.append( ";animator=" );
		rv.append( m_animator );
		rv.append( ";animation=" );
		rv.append( m_animation );
		rv.append( ";observer=" );
		rv.append( m_animationObserver );
		return rv;
	}
}
