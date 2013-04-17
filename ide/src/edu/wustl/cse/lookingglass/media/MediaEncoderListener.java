package edu.wustl.cse.lookingglass.media;

public interface MediaEncoderListener {
	public void encodingStarted( boolean success );

	public void encodingFinished( boolean success );

	public void frameUpdate( int frameCount, java.awt.image.BufferedImage frame );
}
