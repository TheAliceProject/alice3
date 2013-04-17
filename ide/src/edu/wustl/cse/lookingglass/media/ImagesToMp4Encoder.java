package edu.wustl.cse.lookingglass.media;

public class ImagesToMp4Encoder extends ImagesToFFmpegEncoder {

	public ImagesToMp4Encoder( double frameRate, java.awt.Dimension dimension ) {
		super( frameRate, dimension );
	}

	@Override
	protected String getVideoCodec() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
			return "h264";
		} else {
			return "libx264";
		}
	}

	@Override
	protected String getVideoExtension() {
		return "mp4";
	}
}
