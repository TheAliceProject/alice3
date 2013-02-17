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

import java.io.File;

import org.alice.media.components.MoviePlayerView;
import org.lgna.croquet.ActionOperation;

/**
 * @author Matt May
 */
public class MoviePlayerComposite extends org.lgna.croquet.SimpleComposite<MoviePlayerView> {
	private final edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer;
	private java.io.File file;
	private final ActionOperation previewOperation = this.createActionOperation( this.createKey( "previewOperation" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			videoPlayer.prepareMedia( file );
			videoPlayer.playResume();
			return null;
		}
	} );

	public MoviePlayerComposite() {
		super( java.util.UUID.fromString( "28ea7f67-1f3f-443f-a3fb-130676779b5f" ) );
		this.videoPlayer = edu.cmu.cs.dennisc.video.VideoUtilties.createVideoPlayer();
	}

	@Override
	protected MoviePlayerView createView() {
		return new MoviePlayerView( this );
	}

	public void setMovie( File file ) {
		if( this.file != null ) {
			//shut down old file
		}
		this.file = file;
		if( this.file != null ) {
			//perhaps initialize?
		}
	}

	public edu.cmu.cs.dennisc.video.VideoPlayer getVideoPlayer() {
		return this.videoPlayer;
	}

	public ActionOperation getPlayOperation() {
		return previewOperation;
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}
		File file = new File( "C:/Users/dennisc/Videos/test.webm" );
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		MoviePlayerComposite moviePlayerComposite = new MoviePlayerComposite();
		moviePlayerComposite.setMovie( file );
		app.getFrame().setMainComposite( moviePlayerComposite );
		app.getFrame().pack();
		app.getFrame().setDefaultCloseOperation( org.lgna.croquet.components.Frame.DefaultCloseOperation.EXIT );
		app.getFrame().setVisible( true );

	}
}
