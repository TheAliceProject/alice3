package uk.co.mmscomputing.image.operators;

import java.awt.image.*;
import java.awt.color.*;
import java.util.*;

public class GrayScale extends Operator{

  private BufferedImage grayscale(BufferedImage image){
    /*
    Grayscale: the java way
    */
    if(image.getType()==BufferedImage.TYPE_BYTE_GRAY){ return image; }
    int imageType = BufferedImage.TYPE_BYTE_GRAY;

    int w=image.getWidth();
    int h=image.getHeight();
    BufferedImage newImg=new BufferedImage(w,h,imageType);
    ColorSpace srcSpace=image.getColorModel().getColorSpace();
    ColorSpace newSpace=newImg.getColorModel().getColorSpace();
    ColorConvertOp convert=new ColorConvertOp(srcSpace,newSpace,null);
    convert.filter(image,newImg);
    return newImg;
  }

  private BufferedImage grayscale2(BufferedImage src){
    /*
    Grayscale: by hand
    */
    int w=src.getWidth();
    int h=src.getHeight();
    int size=w*h;

    WritableRaster swr=src.getRaster();
    DataBuffer sdb=swr.getDataBuffer();

    BufferedImage dest=new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
    WritableRaster dwr=dest.getRaster();
    DataBuffer ddb=dwr.getDataBuffer();

    int x, y, rgb, r, g, b, gr;

    for(int i=0; i<size; i++){
      rgb=sdb.getElem(i);
      r=(rgb>>>16)&0xFF;
      g=(rgb>>>8)&0xFF;
      b=rgb&0xFF;
      gr=((r*77+g*151+b*28)>>>8)&0xFF;    		//	0.3 x red + 0.59 x green + 0.11 x blue
      ddb.setElem(i,gr);
    }
    return dest;
  }

  public BufferedImage filter(BufferedImage src){
    return grayscale(src);
  }
}