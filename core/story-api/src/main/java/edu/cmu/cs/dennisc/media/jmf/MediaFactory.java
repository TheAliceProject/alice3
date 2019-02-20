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
package edu.cmu.cs.dennisc.media.jmf;

import com.sun.media.codec.audio.mp3.JavaDecoder;
import edu.cmu.cs.dennisc.javax.media.protocol.ByteArrayDataSource;
import edu.cmu.cs.dennisc.javax.media.renderer.audio.FixedJavaSoundRenderer;
import org.lgna.common.event.ResourceContentEvent;
import org.lgna.common.event.ResourceContentListener;
import org.lgna.common.resources.AudioResource;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.protocol.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class MediaFactory extends edu.cmu.cs.dennisc.media.MediaFactory {
	private Map<AudioResource, DataSource> audioResourceToDataSourceMap = new HashMap<AudioResource, DataSource>();
	private ResourceContentListener resourceContentListener = new ResourceContentListener() {
		@Override
		public void contentChanging( ResourceContentEvent e ) {
		}

		@Override
		public void contentChanged( ResourceContentEvent e ) {
			MediaFactory.this.forget( (AudioResource)e.getTypedSource() );
		}
	};
	static {
		System.out.print( "Attempting to register mp3 capability... " );
		JavaDecoder.main( new String[] {} );
		FixedJavaSoundRenderer.usurpControlFromJavaSoundRenderer();
	}

	private static MediaFactory singleton;
	static {
		MediaFactory.singleton = new MediaFactory();
	}

	public static MediaFactory getSingleton() {
		return MediaFactory.singleton;
	}

	private MediaFactory() {
	}

	private void forget( AudioResource audioResource ) {
		this.audioResourceToDataSourceMap.remove( audioResource );
		audioResource.removeContentListener( this.resourceContentListener );
	}

	public AudioResource createAudioResource( File file ) throws IOException {
		String contentType = AudioResource.getContentType( file );
		if( contentType != null ) {
			final AudioResource rv = new AudioResource( file, contentType );
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					Player player = new Player( createJMFPlayer( rv ), 1.0, 0.0, Double.NaN, rv );
					player.realize();
					rv.setDuration( player.getDuration() );
				}
			};
			final boolean USE_THREAD_JUST_TO_BE_SORT_OF_SAFE = true;
			if( USE_THREAD_JUST_TO_BE_SORT_OF_SAFE ) {
				new Thread( runnable ).start();
			} else {
				runnable.run();
			}
			return rv;
		} else {
			throw new RuntimeException( "content type not found for " + file );
		}
	}

	private javax.media.Player createJMFPlayer( AudioResource audioResource ) {
		assert audioResource != null;
		DataSource dataSource = this.audioResourceToDataSourceMap.get( audioResource );
		if( dataSource != null ) {
			//pass
		} else {
			dataSource = new ByteArrayDataSource( audioResource.getData(), audioResource.getContentType() );
			audioResource.addContentListener( this.resourceContentListener );
			this.audioResourceToDataSourceMap.put( audioResource, dataSource );
		}
		try {
			return Manager.createRealizedPlayer( dataSource );
		} catch( CannotRealizeException cre ) {
			throw new RuntimeException( audioResource.toString(), cre );
		} catch( NoPlayerException npe ) {
			throw new RuntimeException( audioResource.toString(), npe );
		} catch( IOException ioe ) {
			throw new RuntimeException( audioResource.toString(), ioe );
		}
	}

	@Override
	public Player createPlayer( AudioResource audioResource, double volume, double startTime, double stopTime ) {
		Player player = new Player( createJMFPlayer( audioResource ), volume, startTime, stopTime, audioResource );
		if( Double.isNaN( audioResource.getDuration() ) ) {
			player.realize();
			audioResource.setDuration( player.getDuration() );
		}
		return player;
	}
}
