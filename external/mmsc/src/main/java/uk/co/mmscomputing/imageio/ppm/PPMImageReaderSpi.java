package uk.co.mmscomputing.imageio.ppm;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Locale;
import javax.imageio.*;
import javax.imageio.spi.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;

public class PPMImageReaderSpi extends ImageReaderSpi {

  static final String vendorName="mm's computing";
  static final String version="0.0.1";
  static final String readerClassName="uk.co.mmscomputing.imageio.ppm.PPMImageReader";
  static final String[] names={"pbm","PBM","pgm","PGM","ppm","PPM"};
  static final String[] suffixes={"pbm","PBM","pgm","PGM","ppm","PPM"};
  static final String[] MIMETypes={"image/pbm","image/pgm","image/ppm"};
  static final String[] writerSpiNames={"uk.co.mmscomputing.imageio.ppm.PPMImageWriterSpi"};

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

  public PPMImageReaderSpi(){
    super(	vendorName,		version,
		names,			suffixes,
		MIMETypes,		readerClassName,
		STANDARD_INPUT_TYPE,	writerSpiNames,
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

  public ImageReader createReaderInstance(Object extension)throws IOException{
    return new PPMImageReader(this);
  }

  public boolean canDecodeInput(Object source)throws IOException{
    if(!(source instanceof ImageInputStream)) { return false; }
    ImageInputStream stream = (ImageInputStream)source;
//    stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    byte[] pn = new byte[2];
    try{
      stream.mark();
      stream.readFully(pn);
      stream.reset();
    }catch(IOException e){
      return false;
    }
    return(pn[0]==(byte)'P')&&((pn[1]==(byte)'4')||(pn[1]==(byte)'5')||(pn[1]==(byte)'6'));
  }

  public String getDescription(Locale locale){
    return "mmsc ppm decoder";
  }
}