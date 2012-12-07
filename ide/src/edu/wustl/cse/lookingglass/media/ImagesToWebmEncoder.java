package edu.wustl.cse.lookingglass.media;

import java.io.File;
import java.io.IOException;

import org.alice.media.audio.AudioCompiler;
import org.alice.media.audio.ScheduledAudioStream;

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
		if( getSoundTrack() != null ) {
			String tempVideoPath = this.getVideoPath();
			String newPath = tempVideoPath.substring( 0, tempVideoPath.length() - 5 );
			newPath += "1.webm";
			RuntimeUtilities.execSilent( this.ffmpegCommand, "-i", this.getVideoPath(), "-i", getSoundTrack().getAbsolutePath(), newPath );
			File oldFile = new File( this.getVideoPath() );
			oldFile.delete();
			File newFile = new File( newPath );
			newFile.renameTo( new File( this.getVideoPath() ) );
		}
	}

	public double getLength() {
		return frameCount / frameRate;
	}
}
