/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.media;

import java.awt.Component;
import java.io.File;

import org.alice.media.components.MoviePlayerView;
import org.lgna.croquet.ActionOperation;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

/**
 * @author Matt May
 */
public class MoviePlayerComposite extends org.lgna.croquet.SimpleComposite<MoviePlayerView> {

	private MoviePlayerView view;
	private uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private File videoFile;

	private final ActionOperation previewOperation = this.createActionOperation( this.createKey( "previewOperation" ), new Action() {

		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			assert videoFile != null;
			mediaPlayerComponent.getMediaPlayer().playMedia( videoFile.getAbsolutePath() );
			return null;
		}
	} );

	public MoviePlayerComposite( File file ) {
		super( java.util.UUID.fromString( "28ea7f67-1f3f-443f-a3fb-130676779b5f" ) );
		com.sun.jna.NativeLibrary.addSearchPath( uk.co.caprica.vlcj.runtime.RuntimeUtil.getLibVlcLibraryName(), "c:/Program Files/VideoLAN/VLC/" );
		com.sun.jna.Native.loadLibrary( uk.co.caprica.vlcj.runtime.RuntimeUtil.getLibVlcLibraryName(), uk.co.caprica.vlcj.binding.LibVlc.class );
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
	}

	@Override
	protected MoviePlayerView createView() {
		view = new MoviePlayerView( this );
		return view;
	}

	public void handlePlayerRealized( Component visualComponent, Component controlComponent ) {
		getView().handlePlayerRealized( visualComponent, controlComponent );
		getView().revalidateAndRepaint();
	}

	public void setMovie( File file ) {
		view.setMovie( file );
		videoFile = file;
	}

	public EmbeddedMediaPlayerComponent getPlayer() {
		return this.mediaPlayerComponent;
	}

	public ActionOperation getPlayOperation() {
		return previewOperation;
	}
}
