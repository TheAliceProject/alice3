package edu.wustl.cse.lookingglass.media;

public abstract class ImagesToFFmpegEncoder {

	protected double frameRate;
	protected int frameCount = 0;
	private java.awt.Dimension frameDimension;
	private String videoPath;

	private boolean isRunning = false;
	private boolean success = true;

	protected String ffmpegCommand;
	private Process ffmpegProcess;
	private java.io.OutputStream ffmpegStdOut;
	private java.io.BufferedReader ffmpegStdErr;
	private java.io.BufferedReader ffmpegStdIn;
	private StringBuilder ffmpegInput;
	private StringBuilder ffmpegError;

	private java.util.List<MediaEncoderListener> listeners = new java.util.LinkedList<MediaEncoderListener>();

	protected ImagesToFFmpegEncoder( double frameRate, java.awt.Dimension dimension )
	{
		this.frameRate = frameRate;
		this.frameDimension = dimension;
		this.frameCount = -1;
		this.setFFmpegCommand();

		// ffmpeg requires that the dimensions must be divisible by two.
		assert ( ( frameDimension.getWidth() % 2 ) == 0 );
		assert ( ( frameDimension.getHeight() % 2 ) == 0 );
	}

	protected abstract String getVideoCodec();

	protected abstract String getVideoExtension();

	protected String getVideoBitrate() {
		return "10000k";
	}

	public void setVideoPath( String path ) {
		assert path.endsWith( "." + this.getVideoExtension() );
		this.videoPath = path;
	}

	public String getVideoPath() {
		return this.videoPath;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void addListener( MediaEncoderListener listener )
	{
		if( !this.listeners.contains( listener ) ) {
			this.listeners.add( listener );
		}
	}

	public void removeListener( MediaEncoderListener listener ) {
		this.listeners.remove( listener );
	}

	//<alice>
	private static String getFfmpegPath() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			return null;
		} else {
			String installPath = System.getProperty( "org.alice.ide.IDE.install.dir" );
			java.io.File installDir = new java.io.File( installPath );
			java.io.File ffmpegFile = new java.io.File( installDir.getParent(), "lib/ffmpeg" );
			StringBuilder sb = new StringBuilder();
			sb.append( ffmpegFile.getAbsolutePath() );
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				sb.append( "/windows" );
			} else if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
				sb.append( "/macosx" );
			} else {
				throw new RuntimeException();
			}
			return sb.toString();
		}
	}

	//</alice>

	private void setFFmpegCommand() {
		// Find the ffmpeg process
		//<alice>
		//String nativePath = edu.wustl.cse.lookingglass.utilities.NativeLibLoader.getOsPath( "ffmpeg" );
		String nativePath = getFfmpegPath();
		//</alice>
		if( nativePath == null ) {
			// Hope it's on the system path
			this.ffmpegCommand = "ffmpeg";
		} else {
			String ext = "";
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				ext = ".exe";
			}

			nativePath = nativePath + "/bin/ffmpeg" + ext;
			if( !( new java.io.File( nativePath ) ).exists() ) {
				this.ffmpegCommand = "ffmpeg";
			} else {
				this.ffmpegCommand = nativePath;
			}
		}
	}

	public synchronized boolean start()
	{
		assert this.isRunning == false;
		assert this.videoPath != null;

		this.frameCount = -1;
		this.isRunning = true;
		this.success = true;

		try {
			// Don't cache images during this process
			javax.imageio.ImageIO.setUseCache( false );

			// Start ffmpeg
			ProcessBuilder ffmpegProcessBuilder = new ProcessBuilder( this.ffmpegCommand, "-y", "-r", String.format( "%d", (int)this.frameRate ), "-f", "image2pipe", "-vcodec", "ppm", "-i", "-", "-vcodec", this.getVideoCodec(), "-b", this.getVideoBitrate(), "-pix_fmt", "yuv420p", this.videoPath );
			this.ffmpegProcess = ffmpegProcessBuilder.start();

			this.ffmpegStdOut = new java.io.BufferedOutputStream( this.ffmpegProcess.getOutputStream() );
			this.ffmpegStdOut.flush();
			this.ffmpegStdErr = new java.io.BufferedReader( new java.io.InputStreamReader( this.ffmpegProcess.getErrorStream() ) );
			this.ffmpegStdIn = new java.io.BufferedReader( new java.io.InputStreamReader( this.ffmpegProcess.getInputStream() ) );

			this.ffmpegInput = new StringBuilder();
			this.ffmpegError = new StringBuilder();

			// Windows requires that we close all other streams, otherwise the output stream for ffmpeg will lock.
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				this.ffmpegProcess.getInputStream().close();
				this.ffmpegProcess.getErrorStream().close();

				this.ffmpegStdErr = null;
				this.ffmpegStdIn = null;
			}
		} catch( Exception e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "failed to create ffmpeg process for encoding", this.ffmpegCommand );
			handleEncodingError( e );
		}

		if( this.isRunning ) {
			for( MediaEncoderListener l : this.listeners )
			{
				l.encodingStarted( this.success );
			}
		}
		return this.success;
	}

	public synchronized void addBufferedImage( java.awt.image.BufferedImage frame ) {
		assert this.isRunning;
		assert frame != null;
		assert ( frame.getWidth() == this.frameDimension.getWidth() );
		assert ( frame.getHeight() == this.frameDimension.getHeight() );

		this.frameCount++;
		try {
			synchronized( this.ffmpegStdOut ) {
				javax.imageio.ImageIO.write( frame, "ppm", this.ffmpegStdOut );
				this.ffmpegStdOut.flush();
			}

			for( MediaEncoderListener l : this.listeners )
			{
				l.frameUpdate( this.frameCount, frame );
			}
		} catch( java.io.IOException e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "failed to write frame " + frame + " to ffmpeg" );
			handleEncodingError( e );
		}
	}

	public synchronized boolean stop()
	{
		if( this.isRunning == false ) {
			return this.success;
		}
		this.isRunning = false;

		// Reset image caching back to default
		javax.imageio.ImageIO.setUseCache( true );

		try {
			synchronized( this.ffmpegStdOut ) {
				this.ffmpegStdOut.close();
			}
		} catch( Exception e ) {
			handleEncodingError( e );
		}

		int status = -1;
		try {
			status = this.ffmpegProcess.waitFor();
		} catch( InterruptedException e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "encoding failed; interrupted exception", e );
			handleEncodingError( e );
		}

		if( status != 0 ) {
			this.success = false;
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "encoding failed; status != 0", status, this.ffmpegInput, this.ffmpegError );
			throw new EncodingException( this.ffmpegInput.toString(), this.ffmpegError.toString() );
		}

		return this.success;
	}

	private void handleEncodingError( Exception e ) {
		this.isRunning = false;
		this.success = false;
		readStream( this.ffmpegStdIn, this.ffmpegInput );
		readStream( this.ffmpegStdErr, this.ffmpegError );
		throw new EncodingException( e, ( this.ffmpegInput == null ) ? null : this.ffmpegInput.toString(), ( this.ffmpegError == null ) ? null : this.ffmpegError.toString() );
	}

	private void readStream( java.io.BufferedReader reader, StringBuilder string ) {
		if( ( reader == null ) || ( string == null ) ) {
			return;
		}
		try {
			while( reader.ready() ) {
				string.append( reader.readLine() );
				string.append( "\n" );
			}
		} catch( java.io.IOException e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "unable to read ffmpeg error", e );
		}
	}

	public static void main( String[] args ) {
		System.out.println( ImagesToFFmpegEncoder.getFfmpegPath() );
	}
}
