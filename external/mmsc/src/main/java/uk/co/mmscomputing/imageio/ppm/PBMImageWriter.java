package uk.co.mmscomputing.imageio.ppm;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.ArrayList;

import java.awt.image.*;

import javax.imageio.*;
import javax.imageio.spi.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;

import uk.co.mmscomputing.image.operators.*;

public class PBMImageWriter extends ImageWriter implements PPMConstants{

  protected PBMImageWriter(ImageWriterSpi originatingProvider){
    super(originatingProvider);
  }

  public IIOMetadata convertImageMetadata(IIOMetadata inData, ImageTypeSpecifier imageType, ImageWriteParam param){
    return null;
  }

  public IIOMetadata convertStreamMetadata(IIOMetadata inData,ImageWriteParam param){
    return null;
  }

  public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageType,ImageWriteParam param){
    return null;
  }

  public IIOMetadata getDefaultStreamMetadata(ImageWriteParam param){
    return null;
  }

  public boolean canInsertImage(int imageIndex)throws IOException{
    super.canInsertImage(imageIndex);
    return (imageIndex==0);		//	can deal only with one image
  }

  public void pbm(ImageOutputStream out, BufferedImage image)throws IOException{
    int width  = image.getWidth();
    int height = image.getHeight();
    PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, false);
    try{
      grabber.grabPixels();
    }catch(InterruptedException e){ 
      throw new IOException(getClass().getName()+".pbm: couldn't grab pixels from image !");
    }
    int bpl=((width%8)==0)?width>>3:(width+8)>>3;   // bytes per line
//    String header="P4\n"+(bpl<<3)+" "+height+"\n";
    String header="P4\n"+width+" "+height+"\n";
    out.write(header.getBytes());

    WritableRaster raster=image.getRaster();

    int k=0; byte b; int pixel;
    byte[] bitmap = new byte[bpl*height];
    for(int y=0;y<height;y++){
      for(int x=0;(x<width)&&(k<bitmap.length);){
        b=0;                                        // all white
        for(int bit=7;(bit>=0)&&(x<width);bit--){
          pixel=raster.getSample(x++,y,0);
          if(pixel==0){ b|=(1<<bit);}               // if black set bit
        }
        bitmap[k++]=b;
      }
    }
    out.write(bitmap);            
  }

  public void write(IIOMetadata streamMetadata,IIOImage img,ImageWriteParam param)throws IOException{
    ImageOutputStream out=(ImageOutputStream)getOutput();
    if(!(img.getRenderedImage() instanceof BufferedImage)){
      throw new IOException(getClass().getName()+"write: Can only write BufferedImage objects");
    }
    BufferedImage image=(BufferedImage)img.getRenderedImage();
    switch(image.getType()){
    case BufferedImage.TYPE_BYTE_BINARY: // 1, 2, 4 bit possible; expect 1 bit
      break;
    case BufferedImage.TYPE_BYTE_GRAY: 
      image=new Binarization().filter(image);
      break;
    default:
      image=new GrayScale().filter(image); 
      image=new Binarization().filter(image);
      break;
    }
    pbm(out,image);
  }

  public ImageWriteParam getDefaultWriteParam(){
    return new ImageWriteParam(getLocale());
  }
}
