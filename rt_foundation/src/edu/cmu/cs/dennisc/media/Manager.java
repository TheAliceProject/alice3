package edu.cmu.cs.dennisc.media;


public class Manager {
	static {
	    System.out.print( "Attempting to register mp3 capability... " );
	    com.sun.media.codec.audio.mp3.JavaDecoder.main( new String[] {} );
	}
	public static javax.media.Player getPlayer( java.net.URL source ) {
		//todo
		try {
			return javax.media.Manager.createPlayer( source );
		} catch( javax.media.NoPlayerException npe ) {
			throw new RuntimeException( npe );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
}
