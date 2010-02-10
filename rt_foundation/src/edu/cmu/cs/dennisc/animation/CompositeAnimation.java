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
public abstract class CompositeAnimation implements Animation {
	private Animation[] m_animations;
	public CompositeAnimation( Animation... animations ) {
		setAnimations( animations );
	}
	protected void setAnimations( Animation[] animations ) {
		m_animations = animations;
		this.reset();
	}
	
	public Animation[] getAnimations() {
		return m_animations;
	}
	
	public void reset() {
		for( Animation animation : m_animations ) {
			animation.reset();
		}
	}
	public void complete( AnimationObserver animationObserver ) {
		for( Animation animation : m_animations ) {
			animation.complete( animationObserver );
		}
	}
}
