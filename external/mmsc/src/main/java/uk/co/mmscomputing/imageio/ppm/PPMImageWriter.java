package uk.co.mmscomputing.imageio.ppm;

import java.io.*;
import java.util.*;

import java.awt.image.*;

import javax.imageio.*;
import javax.imageio.spi.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;

public class PPMImageWriter extends ImageWriter{

  protected PPMImageWriter(ImageWriterSpi originatingProvider){
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

  public void ppm(ImageOutputStream out, BufferedImage image)throws IOException{
    int width  = image.getWidth();
    int height = image.getHeight();
    int data[] = new int[width*height];
    PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, data, 0, width);
    try{
      grabber.grabPixels();
    }catch(InterruptedException e){ 
      throw new IOException(getClass().getName()+".ppm: couldn't grab pixels from image !");
    }

    String header="P6\n"+width+" "+height+"\n255\n";
    out.write(header.getBytes());

    ColorModel model = grabber.getColorModel();

    int k=0; int col;
    byte[] bitmap = new byte[width*height*3];
    for(int y=0;y<height;y++){
      for(int x=0;x<width;x++){
        col=data[y*width+x];
        bitmap[k++]=(byte)model.getRed(col);
        bitmap[k++]=(byte)model.getGreen(col);
        bitmap[k++]=(byte)model.getBlue(col);
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
    ppm(out,image);
  }

  public ImageWriteParam getDefaultWriteParam(){
    return new ImageWriteParam(getLocale());
  }
}
