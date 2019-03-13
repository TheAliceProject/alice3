package uk.co.mmscomputing.imageio.ppm;

import java.io.*;
import java.util.*;

import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.spi.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;

public class PPMImageReader extends ImageReader {

  private boolean gotHeader = false;

  private static final int PBM_ASCII = 1;
  private static final int PGM_ASCII = 2;
  private static final int PPM_ASCII = 3;
  private static final int PBM_RAW = 4;
  private static final int PGM_RAW = 5;
  private static final int PPM_RAW = 6;

  private int   format=-1;                       // 1 .. 6  
  private int   width=-1;                        // Width 
  private int   height=-1;                       // Height 
  private int   maxcolval=0;                     // max colour value

  protected PPMImageReader(ImageReaderSpi originatingProvider){
    super(originatingProvider);
  }

  public BufferedImage read(int imageIndex, ImageReadParam param)throws IOException{
    checkIndex(imageIndex);
    return read((ImageInputStream)getInput());
  }

  public int getHeight(int imageIndex)throws IOException{
    checkIndex(imageIndex);
    readHeader((ImageInputStream)getInput());
    return height;
  }

  public int getWidth(int imageIndex)throws IOException{
    checkIndex(imageIndex);
    readHeader((ImageInputStream)getInput());
    return width;
  }

  public Iterator getImageTypes(int imageIndex)throws IOException{
    checkIndex(imageIndex);
    readHeader((ImageInputStream)getInput());

    ImageTypeSpecifier imageType = null;
    java.util.List l = new ArrayList();
    imageType=ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);

    l.add(imageType);
    return l.iterator();
  }

  public int getNumImages(boolean allowSearch)throws IOException{
    return 1;
  }

  public IIOMetadata getImageMetadata(int imageIndex)throws IOException{
    checkIndex(imageIndex);
    return null;
  }

  public IIOMetadata getStreamMetadata() throws IOException{
    return null;
  }

  private void checkIndex(int imageIndex) {
    if (imageIndex != 0) {
      throw new IndexOutOfBoundsException(getClass().getName()+".checkIndex: Bad index in ppm image reader");
    }
  }

  // ascii parser routines

  private char readAsciiChar(ImageInputStream in) throws IOException{
	  char c;
    do{
      c=(char)in.read();
      if(c=='#'){                                           // comment : read until end of line
	      do{
		      c=(char)in.read();
		    }while((c!='\n')&&(c!='\r'));
	    }
	  }while((c==' ')||(c=='\t')||(c=='\n')||(c=='\r'));      // white space
    return c;
	}

  private int readAsciiInt(ImageInputStream in)throws IOException{
	  char c=readAsciiChar(in);
	  if((c<'0')||('9'<c)){
	    throw new IOException(getClass().getName()+".readAsciiInt: Expected ascii integer." );
    }
	  int i=0;
    do{
	    i=i*10+c-'0';
	    c=(char)in.read();
	  }while(('0'<=c)&&(c<='9'));
	  return i;
	}

  private void readHeader(ImageInputStream in)throws IOException{
    if (gotHeader) { return; }
    gotHeader = true;

    byte[] pn = new byte[2];
    in.readFully(pn);
    if(pn[0]!=(byte)'P'){ throw new IOException(getClass().getName()+".readHeader: Invalid PPM File. Missing 'P'.");}
    format=pn[1]-'0';
    switch(format){
    case PBM_ASCII: 
    case PGM_ASCII: 
    case PPM_ASCII: throw new IOException(getClass().getName()+".readHeader: Unsupported ASCII PPM File Format : P"+pn[1]);
    case PBM_RAW: 
  	  width=readAsciiInt(in);
  	  height=readAsciiInt(in);
      maxcolval=1;
      break;
    case PGM_RAW: 
  	  width=readAsciiInt(in);
  	  height=readAsciiInt(in);
  	  maxcolval=readAsciiInt(in);
      break;
    case PPM_RAW: 
  	  width=readAsciiInt(in);
  	  height=readAsciiInt(in);
  	  maxcolval=readAsciiInt(in);
      break;
    default:  throw new IOException("Invalid PPM File. Unknown Format ["+format+"]");
    }    
  }

  private BufferedImage read(ImageInputStream in)throws IOException{
    readHeader(in);

    byte[] data;
    switch(format){
    case PBM_RAW: 
      data = new byte[(width*height)>>3];
      in.readFully(data);
      return pbm(width,height,data);
    case PGM_RAW: 
      data = new byte[width*height];
      in.readFully(data);
      return pgm(width,height,maxcolval,data);
    case PPM_RAW: 
      data = new byte[width*height*3];
      in.readFully(data);
      return ppm(width,height,maxcolval,data);
    default: 
      throw new IOException(getClass().getName()+".read: Unsupported File Format.");
    }
  }

  static public BufferedImage ppm(int width, int height, int maxcolval, byte[] data){
    if(maxcolval<256){
      BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      int r,g,b,k=0,pixel;
      if(maxcolval==255){                                      // don't scale
        for(int y=0;y<height;y++){
          for(int x=0;(x<width)&&((k+3)<data.length);x++){
            r=data[k++] & 0xFF;
            g=data[k++] & 0xFF;
            b=data[k++] & 0xFF;
            pixel=0xFF000000+(r<<16)+(g<<8)+b;
            image.setRGB(x,y,pixel);
          }
        }
      }else{
        for(int y=0;y<height;y++){
          for(int x=0;(x<width)&&((k+3)<data.length);x++){
            r=data[k++] & 0xFF;r=((r*255)+(maxcolval>>1))/maxcolval;  // scale to 0..255 range
            g=data[k++] & 0xFF;g=((g*255)+(maxcolval>>1))/maxcolval;
            b=data[k++] & 0xFF;b=((b*255)+(maxcolval>>1))/maxcolval;
            pixel=0xFF000000+(r<<16)+(g<<8)+b;
            image.setRGB(x,y,pixel);
          }
        }
      }
      return image;
    }else{

      // no 48 bit colour type available in java ?

      BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      int r,g,b,k=0,pixel;

      for(int y=0;y<height;y++){
        for(int x=0;(x<width)&&((k+6)<data.length);x++){
          r=(data[k++] & 0xFF)|((data[k++] & 0xFF)<<8);r=((r*255)+(maxcolval>>1))/maxcolval;  // scale to 0..255 range
          g=(data[k++] & 0xFF)|((data[k++] & 0xFF)<<8);g=((g*255)+(maxcolval>>1))/maxcolval;
          b=(data[k++] & 0xFF)|((data[k++] & 0xFF)<<8);b=((b*255)+(maxcolval>>1))/maxcolval;
          pixel=0xFF000000+(r<<16)+(g<<8)+b;
          image.setRGB(x,y,pixel);
        }
      }
      return image;
    }
  }

  static public BufferedImage pgm(int width, int height, int maxcolval, byte[] data){
    if(maxcolval<256){
      BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
      WritableRaster raster=image.getRaster();
      int g,k=0,pixel;
      if(maxcolval==255){                                      // don't scale
        for(int y=0;y<height;y++){
          for(int x=0;(x<width)&&(k<data.length);x++){
            raster.setSample(x,y,0,data[k++] & 0xFF);
          }
        }
      }else{
        for(int y=0;y<height;y++){
          for(int x=0;(x<width)&&(k<data.length);x++){
            pixel=(((data[k++] & 0xFF)*255)+(maxcolval>>1))/maxcolval;  // scale to 0..255 range
            raster.setSample(x,y,0,pixel);
          }
        }
      }
      return image;
    }else{                                                     // 16 bit gray scale image
      BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_USHORT_GRAY);
      WritableRaster raster=image.getRaster();
      int g,k=0,sample,pixel;
      if(maxcolval==65535){                                    // don't scale
        for(int y=0;y<height;y++){
          for(int x=0;(x<width)&&(k<data.length-1);x++){
            sample=(data[k++] & 0xFF)|((data[k++] & 0xFF)<<8);
            raster.setSample(x,y,0,sample);
          }
        }
      }else{
        for(int y=0;y<height;y++){
          for(int x=0;(x<width)&&(k<data.length-1);x++){
            sample=(data[k++] & 0xFF)|((data[k++] & 0xFF)<<8);
            pixel=((sample*65535)+(maxcolval>>1))/maxcolval;   // scale to 0..65535 range
            raster.setSample(x,y,0,pixel);
          }
        }
      }
      return image;
    }
  }

  static public BufferedImage pbm(int width, int height, byte[] data){
    BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY);
    WritableRaster raster=image.getRaster();
    int k=0;
    int bytesPerLine=((width%8)==0)?width>>3:(width+8)>>3;
    for(int y=0;y<height;y++){
      for(int x=0;(x<bytesPerLine)&&(k<data.length);x++){
        byte b=data[k++];
        for(int bit=0;bit<8;bit++){
          int xx=(x<<3)+(7-bit);
          if(xx<width){                                            // last byte in line may have padding bits
            int pixel=((b&(1<<bit))==0)?0xFFFFFFFF:0xFF000000;     // inversion
            raster.setSample(xx,y,0,pixel);
          }                                                        // else ignore padding bits
        }
      }
    }
    return image;
  }
}