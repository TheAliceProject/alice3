package uk.co.mmscomputing.image.operators;

import java.util.Properties;
import java.awt.image.*;

public class ImageTypeConvertOp extends Operator{

  private Properties params;

  public ImageTypeConvertOp(Properties params){
    this.params=params;

    int newType=Integer.parseInt(params.getProperty("type"));
    int bpp=Integer.parseInt(params.getProperty("bpp"));

    switch(newType){
    case BufferedImage.TYPE_BYTE_BINARY:
      if((bpp!=4)&&(bpp!=2)&&(bpp!=1)){
        throw new IllegalArgumentException(getClass().getName()
            +"<init>:\n\tTYPE_BYTE_INDEXED supports only 1,2,4 bits per pixel. Not "+bpp+" bpp.");
      }
      break;
    case BufferedImage.TYPE_BYTE_INDEXED:
      if((bpp!=8)&&(bpp!=4)&&(bpp!=2)&&(bpp!=1)){
        throw new IllegalArgumentException(getClass().getName()
            +"<init>:\n\tTYPE_BYTE_INDEXED supports only 1,2,4,8 bits per pixel. Not "+bpp+" bpp.");
      }
      break;
    case BufferedImage.TYPE_BYTE_GRAY:
      if(bpp!=8){
        throw new IllegalArgumentException(getClass().getName()
            +"<init>:\n\tTYPE_BYTE_GRAY supports only 8 bits per pixel. Not "+bpp+" bpp.");
      }
      break;
    default: throw new IllegalArgumentException(getClass().getName()+"<init>:\n\tUnsupported BufferedImageType");
    }
  }

  public BufferedImage filter(BufferedImage src){
    int newType=Integer.parseInt(params.getProperty("type"));
    switch(newType){
    case BufferedImage.TYPE_BYTE_BINARY:  return filterByteBinary(src);
    case BufferedImage.TYPE_BYTE_INDEXED: return filterByteIndexed(src);
    case BufferedImage.TYPE_BYTE_GRAY:    return filterGrayScaled(src);
    }
    return src;
  }

  private BufferedImage filterByteIndexed(BufferedImage src){
    int bpp=Integer.parseInt(params.getProperty("bpp"));
    switch(bpp){    
    case 1: return new Binarization().filter(src);
//    case 1: return new HeckbertQuantiziser(1,true,false).filter(src);
    case 2: return new HeckbertQuantiziser(2,true,false).filter(src);
    case 4: return new HeckbertQuantiziser(4,true,false).filter(src);
    case 8: return new DekkerQuantiziser().filter(src);
    }
    return src;
  }

  private BufferedImage filterByteBinary(BufferedImage src){
    int bpp=Integer.parseInt(params.getProperty("bpp"));
    int threshold;
    try{threshold=Integer.parseInt(params.getProperty("threshold"));}catch(Exception e){threshold=50;}
    switch(bpp){    
    case 1: return new Binarization(threshold).filter(src);
    case 2: return new HeckbertQuantiziser(2,true,false).filter(src);
    case 4: return new HeckbertQuantiziser(4,true,false).filter(src);
    }
    return src;
  }

  private BufferedImage filterGrayScaled(BufferedImage src){
    int bpp=Integer.parseInt(params.getProperty("bpp"));
    switch(bpp){    
    case 8: return new GrayScale().filter(src);
    }
    return src;
  }

}



