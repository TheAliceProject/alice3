package uk.co.mmscomputing.image.operators;

import java.awt.image.*;

// Thomas W. Lipp, Grafikformate, MS Press, ISBN 3-86063-391-0 :  p 117
// Douglas A. Lyon, Image Processing in Java, Prentice Hall PTR, ISBN: 0-13-974577-7 p 361

// original:
// Paul Heckbert, "Color Image Quantization For Frame Buffer Display", SIGGRAPH '82 Proceedings, p 297

public class HeckbertQuantiziser{

  private int[]   colourCube;
  private int[]   rColourTable;
  private int[]   gColourTable;
  private int[]   bColourTable;
  private int     cti;
  private int     bitsPerPixel;
  private int     maxColours;
  private boolean mediancut,dither;

  public HeckbertQuantiziser(){
    this(1,true,true);
  }

  public HeckbertQuantiziser(int bpp,boolean mediancut,boolean dither){
    bitsPerPixel = bpp;
    maxColours   = 1<<bitsPerPixel;
    colourCube   = new int[32768];
    rColourTable = new int[maxColours];
    gColourTable = new int[maxColours];
    bColourTable = new int[maxColours];
    cti=0;
    this.mediancut=mediancut;
    this.dither=dither;
  }

  public BufferedImage filter(BufferedImage src){
    int w = src.getWidth();
    int h = src.getHeight();

    for(int y=0;y<h;y++){
      for(int x=0;x<w;x++){
        int c = src.getRGB(x,y);
        int r = (c>>9)&0x7C00;                 // reduce to 15bit colour
        int g = (c>>6)&0x03E0;
        int b = (c>>3)&0x001F;
        colourCube[r|g|b]++;                   // build histogram
      }
    }
    if(mediancut){
      cti=0;medianCut(0,31,0,31,0,31,maxColours,w*h);
    }else{
      cti=0;popularity();
    }
    for(int c=0;c<32768;c++){                  // get colour table index with smallest error for every cube entry
      int r = (c>>7)&0x00F8;
      int g = (c>>2)&0x00F8;
      int b = (c<<3)&0x00F8;

      int dr  = rColourTable[0]-r;
      int dg  = gColourTable[0]-g;
      int db  = bColourTable[0]-b;
      int err = dr*dr+dg*dg+db*db;

      int index=0;

      for(int i=0;i<maxColours;i++){
        dr = rColourTable[i]-r;
        dg = gColourTable[i]-g;
        db = bColourTable[i]-b;
        int e = dr*dr+dg*dg+db*db;
        if(e<err){err=e;index=i;}
      }
      colourCube[c]=index;                     // set colour table index with smallest error
    }

    byte[] rCT=new byte[cti];                  // need byte arrays for IndexColorModel
    byte[] gCT=new byte[cti];
    byte[] bCT=new byte[cti];

    for (int i=0; i<cti; i++) {                // copy colour tables
      rCT[i]=(byte)rColourTable[i];
      gCT[i]=(byte)gColourTable[i];
      bCT[i]=(byte)bColourTable[i];
    }

    IndexColorModel cm     = new IndexColorModel(bitsPerPixel,cti,rCT,gCT,bCT);
    BufferedImage   img;
    if(bitsPerPixel<=4){
      img=new BufferedImage(w,h,BufferedImage.TYPE_BYTE_BINARY,cm);
    }else{
      img=new BufferedImage(w,h,BufferedImage.TYPE_BYTE_INDEXED,cm);
    }
    Raster          raster = img.getRaster();
    DataBufferByte  dbuf   = (DataBufferByte)raster.getDataBuffer();    
    byte[]          buf    = dbuf.getData();
  
    int ww;
    switch(bitsPerPixel){
    case 4: ww=((w+1)>>1)<<1;break;
    case 2: ww=((w+3)>>2)<<2;break;
    case 1: ww=((w+7)>>3)<<3;break;
    default: ww=w;break;
    }

    for(int y=0;y<h;y++){
      for(int x=0;x<w;x++){
        int c = src.getRGB(x,y);
      
        int r = (c>>9)&0x7C00;                // reduce to 15bit colour
        int g = (c>>6)&0x03E0;
        int b = (c>>3)&0x001F;
        int i = colourCube[r|g|b];            // get index into colour table

        if(bitsPerPixel==8){
          buf[y*ww+x]=(byte)i;                // set colour table index
        }else if(bitsPerPixel==4){
          switch(x%2){
          case 0: buf[(y*ww+x)>>1] =(byte)(i<<4);break;
          case 1: buf[(y*ww+x)>>1]|=(byte)    i ;break;
          }
        }else if(bitsPerPixel==2){
          switch(x%4){
          case 0: buf[(y*ww+x)>>2] =(byte)(i<<6); break;
          case 1: buf[(y*ww+x)>>2]|=(byte)(i<<4); break;
          case 2: buf[(y*ww+x)>>2]|=(byte)(i<<2); break;
          case 3: buf[(y*ww+x)>>2]|=(byte) i    ; break;
          }
        }else if(bitsPerPixel==1){
          switch(x%8){
          case 0: buf[(y*ww+x)>>3] =(byte)(i<<7); break;
          case 1: buf[(y*ww+x)>>3]|=(byte)(i<<6); break;
          case 2: buf[(y*ww+x)>>3]|=(byte)(i<<5); break;
          case 3: buf[(y*ww+x)>>3]|=(byte)(i<<4); break;
          case 4: buf[(y*ww+x)>>3]|=(byte)(i<<3); break;
          case 5: buf[(y*ww+x)>>3]|=(byte)(i<<2); break;
          case 6: buf[(y*ww+x)>>3]|=(byte)(i<<1); break;
          case 7: buf[(y*ww+x)>>3]|=(byte) i    ; break;
          }
        }else{
          // shouldn't get here
        }
        if(dither){                           // Dither Floyd Steinberg
          int dr = ((c>>16)&0x00FF)-rColourTable[i];
          int dg = ((c>> 8)&0x00FF)-gColourTable[i];
          int db = ((c    )&0x00FF)-bColourTable[i];

          if( x<(w-1)            ){src.setRGB(x+1,y  ,dither1(src.getRGB(x+1,y  ),dr,dg,db));}
          if(            y<(h-1) ){src.setRGB(x  ,y+1,dither1(src.getRGB(x  ,y+1),dr,dg,db));}
          if((x<(w-1))&&(y<(h-1))){src.setRGB(x+1,y+1,dither2(src.getRGB(x+1,y+1),dr,dg,db));}
        }
      }
    }
    return img;
  }

  private int dither1(int c,int dr,int dg,int db){
    int r = ((c>>16)&0x00FF) + ((dr*3)>>3); 
    if(r<0){r=0;}else if(255<r){r=255;}
    int g = ((c>> 8)&0x00FF) + ((dg*3)>>3); 
    if(g<0){g=0;}else if(255<g){g=255;}
    int b = ((c    )&0x00FF) + ((db*3)>>3); 
    if(b<0){b=0;}else if(255<b){b=255;}
    return (r<<16)|(g<<8)|b;
  }

  private int dither2(int c,int dr,int dg,int db){
    int r = ((c>>16)&0x00FF) + (dr>>2); 
    if(r<0){r=0;}else if(255<r){r=255;}
    int g = ((c>> 8)&0x00FF) + (dr>>2); 
    if(g<0){g=0;}else if(255<g){g=255;}
    int b = ((c    )&0x00FF) + (db>>2); 
    if(b<0){b=0;}else if(255<b){b=255;}
    return (r<<16)|(g<<8)|b;
  }

  private void popularity(){
    for(int c=0;c<maxColours;c++){
      int index=0;
      int value=colourCube[0];
      for(int i=1;i<32768;i++){
        if(colourCube[i]>value){
          value=colourCube[i];
          index=i;
        }
      }
      colourCube[index]=1;                      // mark: done that one

      rColourTable[c]=(index>>7)&0x00F8;
      gColourTable[c]=(index>>2)&0x00F8;
      bColourTable[c]=(index<<3)&0x00F8;

      cti++;
    }
  }

  private void medianCut(int r1,int r2,int g1,int g2,int b1,int b2,int noOfColours,int noOfPixels){
    if(0<noOfPixels){
      int rlen=r2-r1;
      int glen=g2-g1;
      int blen=b2-b1;

      if((rlen==0)&&(glen==0)&&(blen==0)){                   // one pixel cube
        rColourTable[cti]=r1<<3;
        gColourTable[cti]=g1<<3;
        bColourTable[cti]=b1<<3;
        cti++;
      }else if((noOfColours==1)||(noOfPixels==1)){
        int rCount=0,gCount=0,bCount=0;
        for(int r=r1;r<=r2;r++){
          for(int g=g1;g<=g2;g++){
            for(int b=b1;b<=b2;b++){
              int count=colourCube[(r<<10)|(g<<5)|b];
              if(0<count){
                rCount+=r*count;
                gCount+=g*count;
                bCount+=b*count;
              }
            }
          }
        }
        rColourTable[cti]=(rCount/noOfPixels)<<3;
        gColourTable[cti]=(gCount/noOfPixels)<<3;
        bColourTable[cti]=(bCount/noOfPixels)<<3;
        cti++;
      }else if((blen>glen)&&(blen>rlen)){                    // blue
        int newNoOfPixels=0,oldNoOfPixels=0;
        int b=b1-1;
        while(newNoOfPixels<(noOfPixels/2)){
          b++;oldNoOfPixels=newNoOfPixels;
          for(int r=r1;r<=r2;r++){
            for(int g=g1;g<=g2;g++){
              newNoOfPixels+=colourCube[(r<<10)|(g<<5)|b];
            }
          }
        }
        if(b<b2){
          medianCut(r1,r2,g1,g2,b1  ,b   ,noOfColours/2,newNoOfPixels);
          medianCut(r1,r2,g1,g2,b +1,b2  ,noOfColours/2,noOfPixels-newNoOfPixels);
        }else{
          medianCut(r1,r2,g1,g2,b1  ,b -1,noOfColours/2,oldNoOfPixels);
          medianCut(r1,r2,g1,g2,b   ,b2  ,noOfColours/2,noOfPixels-oldNoOfPixels);
        }
      }else if(glen>rlen){                                   // green
        int newNoOfPixels=0,oldNoOfPixels=0;
        int g=g1-1;
        while(newNoOfPixels<(noOfPixels/2)){
          g++;oldNoOfPixels=newNoOfPixels;
          for(int r=r1;r<=r2;r++){
            for(int b=b1;b<=b2;b++){
              newNoOfPixels+=colourCube[(r<<10)|(g<<5)|b];
            }
          }
        }
        if(g<g2){
          medianCut(r1,r2,g1  ,g   ,b1,b2,noOfColours/2,newNoOfPixels);
          medianCut(r1,r2,g +1,g2  ,b1,b2,noOfColours/2,noOfPixels-newNoOfPixels);
        }else{
          medianCut(r1,r2,g1  ,g -1,b1,b2,noOfColours/2,oldNoOfPixels);
          medianCut(r1,r2,g   ,g2  ,b1,b2,noOfColours/2,noOfPixels-oldNoOfPixels);
        }
      }else{                                                 // red
        int newNoOfPixels=0,oldNoOfPixels=0;
        int r=r1-1;
        while(newNoOfPixels<(noOfPixels/2)){
          r++;oldNoOfPixels=newNoOfPixels;
          for(int g=g1;g<=g2;g++){
            for(int b=b1;b<=b2;b++){
              newNoOfPixels+=colourCube[(r<<10)|(g<<5)|b];
            }
          }
        }
        if(r<r2){
          medianCut(r1  ,r   ,g1,g2,b1,b2,noOfColours/2,newNoOfPixels);
          medianCut(r +1,r2  ,g1,g2,b1,b2,noOfColours/2,noOfPixels-newNoOfPixels);
        }else{
          medianCut(r1  ,r -1,g1,g2,b1,b2,noOfColours/2,oldNoOfPixels);
          medianCut(r   ,r2  ,g1,g2,b1,b2,noOfColours/2,noOfPixels-oldNoOfPixels);
        }
      }
    }
  }
}

// see also
// Anthony Dekker; Kohonen neural networks for optimal colour quantization in Volume 5, 
// pp 351-367 of the journal Network: Computation in Neural Systems, Institute of Physics Publishing, 1994
// http://members.ozemail.com.au/~dekker/NEUQUANT.HTML [2006-02-10]
