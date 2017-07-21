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
package edu.cmu.cs.dennisc.media.animation;

import edu.cmu.cs.dennisc.media.MediaPlayerObserver;

/**
 * @author Dennis Cosgrove
 */
public class MediaPlayerAnimation implements edu.cmu.cs.dennisc.animation.Animation {
	//	private static final double CLOSE_ENOUGH_TO_ZERO = 0.0001;
	private edu.cmu.cs.dennisc.media.Player player;
	private boolean isStarted = false;

	private double startTime = 0;

	private static MediaPlayerObserver EPIC_HACK_mediaPlayerObserver;

	public static void EPIC_HACK_setAnimationObserver( MediaPlayerObserver mediaPlayerObserver ) {
		EPIC_HACK_mediaPlayerObserver = mediaPlayerObserver;
	}

	public MediaPlayerAnimation( edu.cmu.cs.dennisc.media.Player player ) {
		this.player = player;
		this.isStarted = false;
	}

	@Override
	public void reset() {
		this.isStarted = false;
	}

	@Override
	public double update( double tCurrent, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		if( this.isStarted ) {
			//pass
		} else {
			//this.player.prefetch();
			this.isStarted = true;
			if( EPIC_HACK_mediaPlayerObserver != null ) {
				EPIC_HACK_mediaPlayerObserver.mediaPlayerStarted( this, tCurrent );
				this.player.start();
				this.startTime = tCurrent;
			}
			else
			{
				this.player.start();
			}
		}

		double timeRemaining = 0;
		if( EPIC_HACK_mediaPlayerObserver != null )
		{
			if( this.player instanceof edu.cmu.cs.dennisc.media.jmf.Player )
			{
				edu.cmu.cs.dennisc.media.jmf.Player jmfPlayer = (edu.cmu.cs.dennisc.media.jmf.Player)player;
				double startTime = Double.isNaN( jmfPlayer.getStartTime() ) ? 0 : jmfPlayer.getStartTime();
				double endTime = Double.isNaN( jmfPlayer.getStopTime() ) ? jmfPlayer.getDuration() : jmfPlayer.getStopTime();
				double totalTime = endTime - startTime;
				double timeElapsed = tCurrent - this.startTime;
				if( timeElapsed >= totalTime )
				{
					timeRemaining = 0;
				}
				else
				{
					timeRemaining = totalTime - timeElapsed;
				}
			}
		}
		else
		{
			timeRemaining = this.player.getTimeRemaining();
		}
		//		if( timeRemaining < CLOSE_ENOUGH_TO_ZERO ) {
		//			timeRemaining = 0.0;
		//		}
		return timeRemaining;
	}

	@Override
	public void complete( edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		this.player.stop();
		if( animationObserver != null ) {
			animationObserver.finished( this );
		}
	}

	public edu.cmu.cs.dennisc.media.Player getPlayer()
	{
		return this.player;
	}
}
