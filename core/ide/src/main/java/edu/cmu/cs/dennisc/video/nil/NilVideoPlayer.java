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
package edu.cmu.cs.dennisc.video.nil;

/**
 * @author Dennis Cosgrove
 */
public class NilVideoPlayer implements edu.cmu.cs.dennisc.video.VideoPlayer {
	private final NilVideoCanvas nilVideoCanvas = new NilVideoCanvas();
	private java.net.URI uri;
	private edu.cmu.cs.dennisc.java.awt.Painter<edu.cmu.cs.dennisc.video.VideoPlayer> painter;
	private long timeInMilliseconds;
	private float position;
	private boolean isMuted;
	private float volume;
	private boolean isPrepared;

	@Override
	public java.awt.Canvas getVideoSurface() {
		return this.nilVideoCanvas;
	}

	@Override
	public java.awt.Dimension getVideoSize() {
		return new java.awt.Dimension( 0, 0 );
	}

	@Override
	public edu.cmu.cs.dennisc.java.awt.Painter<edu.cmu.cs.dennisc.video.VideoPlayer> getPainter() {
		return this.painter;
	}

	@Override
	public void setPainter( edu.cmu.cs.dennisc.java.awt.Painter<edu.cmu.cs.dennisc.video.VideoPlayer> painter ) {
		this.painter = painter;
	}

	@Override
	public boolean prepareMedia( java.net.URI uri ) {
		this.uri = uri;
		this.isPrepared = true; //?
		return this.isPrepared;
	}

	@Override
	public boolean isPrepared() {
		return this.isPrepared;
	}

	@Override
	public void parse() {
	}

	@Override
	public boolean isPlayable() {
		return false;
	}

	@Override
	public boolean isPlaying() {
		return false;
	}

	@Override
	public void playResume() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void stop() {
	}

	@Override
	public long getTimeInMilliseconds() {
		return this.timeInMilliseconds;
	}

	@Override
	public void setTimeInMilliseconds( long timeInMilliseconds ) {
		this.timeInMilliseconds = timeInMilliseconds;
	}

	@Override
	public float getPosition() {
		return this.position;
	}

	@Override
	public void setPosition( float position ) {
		//todo?
		this.position = position;
	}

	@Override
	public long getLengthInMilliseconds() {
		return 0;
	}

	@Override
	public boolean isMuted() {
		return this.isMuted;
	}

	@Override
	public void setMuted( boolean isMuted ) {
		//todo?
		this.isMuted = isMuted;
	}

	@Override
	public float getVolume() {
		return this.volume;
	}

	@Override
	public void setVolume( float volume ) {
		//todo?
		this.volume = volume;
	}

	@Override
	public boolean writeSnapshot( java.io.File file ) {
		return false;
	}

	@Override
	public void addMediaListener( edu.cmu.cs.dennisc.video.event.MediaListener listener ) {
	}

	@Override
	public void removeMediaListener( edu.cmu.cs.dennisc.video.event.MediaListener listener ) {
	}

	@Override
	public void release() {
	}

	@Override
	public java.awt.Image getSnapshot() {
		return null;
	}
}
