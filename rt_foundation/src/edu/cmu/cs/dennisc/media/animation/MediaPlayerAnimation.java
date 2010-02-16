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
package edu.cmu.cs.dennisc.media.animation;

import edu.cmu.cs.dennisc.media.Player;

/**
 * @author Dennis Cosgrove
 */
public class MediaPlayerAnimation implements edu.cmu.cs.dennisc.animation.Animation {
	private Player player;
	private boolean isStarted = false;

	public MediaPlayerAnimation( Player player ) {
		this.player = player;
		this.isStarted = false;
	}
	public void reset() {
		this.isStarted = false;
	}
	public double update( double tCurrent, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		if( this.isStarted ) {
			//pass
		} else {
			//this.player.prefetch();
			this.player.start();
			this.isStarted = true;
		}
		return this.player.getTimeRemaining();
	}
	public void complete( edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		this.player.stop();
	}
}
