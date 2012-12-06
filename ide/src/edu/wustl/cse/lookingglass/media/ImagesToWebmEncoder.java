package edu.wustl.cse.lookingglass.media;

import java.io.File;
import java.io.IOException;

import org.alice.media.audio.AudioCompiler;
import org.alice.media.audio.ScheduledAudioStream;
import org.lgna.common.resources.AudioResource;

import edu.cmu.cs.dennisc.java.lang.RuntimeUtilities;

public class ImagesToWebmEncoder extends ImagesToFFmpegEncoder {

	AudioCompiler aCompiler;

	public ImagesToWebmEncoder( double frameRate, java.awt.Dimension dimension ) {
		super( frameRate, dimension );
		try {
			aCompiler = new AudioCompiler( File.createTempFile( "tempAudio", ".wav" ) );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getVideoCodec() {
		return "libvpx";
	}

	@Override
	protected String getVideoExtension() {
		return "webm";
	}

	public synchronized void addAudio( ScheduledAudioStream resource ) {
		aCompiler.addAudio( resource );
	}

	private File getSoundTrack() {
		return aCompiler.mix( getLength() );
	}

	public void mergeAudio() {
		//remove
		try {
			addAudio( new ScheduledAudioStream( new AudioResource( new File( "C:/Users/Matt/Downloads/blorp.wav" ) ), 0 ) );
			addAudio( new ScheduledAudioStream( new AudioResource( new File( "C:/Users/Matt/Downloads/whoosh.wav" ) ), 0 ) );
		} catch( IOException e ) {
			e.printStackTrace();
		}
		//remove
		String blah = this.getVideoPath();
		String newPath = blah.substring( 0, blah.length() - 5 );
		newPath += "1.webm";

		RuntimeUtilities.exec( this.ffmpegCommand, "-i", this.getVideoPath(), "-i", getSoundTrack().getAbsolutePath(), newPath );
		File oldFile = new File( this.getVideoPath() );
		oldFile.delete();
		File newFile = new File( newPath );
		newFile.renameTo( new File( this.getVideoPath() ) );
	}

	public double getLength() {
		return frameCount / frameRate;
	}
}
