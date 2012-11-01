package edu.wustl.cse.lookingglass.media;

public class ImagesToWebmEncoder extends ImagesToFFmpegEncoder {

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
}
