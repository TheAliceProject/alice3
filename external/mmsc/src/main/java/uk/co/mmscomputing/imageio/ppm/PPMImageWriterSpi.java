package uk.co.mmscomputing.imageio.ppm;

import java.io.IOException;
import java.util.Locale;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.imageio.spi.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;

public class PPMImageWriterSpi extends ImageWriterSpi {

  static final String vendorName="mm's computing";
  static final String version="0.0.1";
  static final String writerClassName="uk.co.mmscomputing.imageio.ppm.PPMImageWriter";
  static final String[] names={"ppm","PPM"};
  static final String[] suffixes={"ppm","PPM"};
  static final String[] MIMETypes={"image/ppm"};
  static final String[] readerSpiNames={"uk.co.mmscomputing.imageio.ppm.PPMImageReaderSpi"};

  static final boolean supportsStandardStreamMetadataFormat = false;
  static final String nativeStreamMetadataFormatName = null;
  static final String nativeStreamMetadataFormatClassName = null;
  static final String[] extraStreamMetadataFormatNames = null;
  static final String[] extraStreamMetadataFormatClassNames = null;
  static final boolean supportsStandardImageMetadataFormat = false;
  static final String nativeImageMetadataFormatName =null;//"uk.co.mmscomputing.imageio.ppm.PPMFormatMetadata 0.0.1";
  static final String nativeImageMetadataFormatClassName =null;//"uk.co.mmscomputing.imageio.ppm.PPMFormatMetadata";
  static final String[] extraImageMetadataFormatNames = null;
  static final String[] extraImageMetadataFormatClassNames = null;

  public PPMImageWriterSpi(){
    super(	vendorName,		version,
		names,			suffixes,
		MIMETypes,		writerClassName,
		STANDARD_OUTPUT_TYPE,	readerSpiNames,
		supportsStandardStreamMetadataFormat,
		nativeStreamMetadataFormatName,
		nativeStreamMetadataFormatClassName,
		extraStreamMetadataFormatNames,
                extraStreamMetadataFormatClassNames,
		supportsStandardImageMetadataFormat,
		nativeImageMetadataFormatName,
                nativeImageMetadataFormatClassName,
                extraImageMetadataFormatNames,
		extraImageMetadataFormatClassNames
    );
  }

  public ImageWriter createWriterInstance(Object extension)throws IOException{
    return new PPMImageWriter(this);
  }

  public boolean canEncodeImage(ImageTypeSpecifier type){
//    return (type.getBufferedImageType()==BufferedImage.TYPE_INT_RGB);
    return true;
  }

  public String getDescription(Locale locale){
    return "mmsc ppm encoder";
  }

}