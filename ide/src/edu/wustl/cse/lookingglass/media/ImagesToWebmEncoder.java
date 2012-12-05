package edu.wustl.cse.lookingglass.media;

import java.io.File;
import java.util.List;

import org.alice.media.audio.ScheduledAudioStream;

import edu.cmu.cs.dennisc.java.lang.RuntimeUtilities;
import edu.cmu.cs.dennisc.java.util.Collections;

public class ImagesToWebmEncoder extends ImagesToFFmpegEncoder {

	private List<ScheduledAudioStream> audioResources = Collections.newLinkedList();

	public ImagesToWebmEncoder( double frameRate, java.awt.Dimension dimension ) {
		super( frameRate, dimension );
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
		audioResources.add( resource );
	}

	public void mergeAudio() {
		// ffmpeg -i video.flv -i audio.mp3 -acodec copy -vcodec copy -ab 128k -ar 44100 output.flv
		//		AudioResource audioResource = audioResources.get( 0 ).getAudioResource();
		//		System.out.println( audioResource.getName() );
		//		System.out.println(audioResource.get);
		//		System.out.println( "hello" );
		//		System.out.println( this.ffmpegCommand );
		String blah = this.getVideoPath();
		String newPath = blah.substring( 0, blah.length() - 5 );
		newPath += "1.webm";
		System.out.println( newPath );
		RuntimeUtilities.exec( this.ffmpegCommand, "-i", this.getVideoPath(), "-i", "C:/Users/Matt/Downloads/blorp.wav", newPath );
		//
		File oldFile = new File( this.getVideoPath() );
		oldFile.delete();
		File newFile = new File( newPath );
		newFile.renameTo( new File( this.getVideoPath() ) );
		System.out.println( newPath );
		System.out.println( this.getVideoPath() );
		blah = this.getVideoPath();
		newPath = blah.substring( 0, blah.length() - 5 );
		newPath += "1.webm";
		System.out.println( newPath );
		RuntimeUtilities.exec( this.ffmpegCommand, "-i", this.getVideoPath(), "-i", "C:/Users/Matt/Downloads/whoosh.wav", newPath );

		oldFile = new File( this.getVideoPath() );
		oldFile.delete();
		newFile = new File( newPath );
		newFile.renameTo( new File( this.getVideoPath() ) );
		System.out.println( newPath );
		System.out.println( this.getVideoPath() );
		//		ProcessBuilder audioProcessBuilder = new ProcessBuilder( this.ffmpegCommand, "-i", "C:/Users/Matt/Downloads/blorp.wav", "-i", this.getVideoPath()/* , audioCodec */, "copy", this.getVideoPath() );
		//		try {
		//			audioProcessBuilder.start();
		//			System.out.println( "start called" );
		//		} catch( IOException e ) {
		//			e.printStackTrace();
		//		}
	}
}
