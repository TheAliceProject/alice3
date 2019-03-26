package uk.co.mmscomputing.image.operators;

import java.awt.image.*;
import java.awt.color.*;
import java.util.*;

abstract public class Operator{

  static final int WHITE=0x00FFFFFF;
  static final int BLACK=0x00000000;

  static final int BWHITE=0xFF;
  static final int BBLACK=0x00;

  public BufferedImage filter(BufferedImage src){return null;};

/*
  int w, h, size;				//	width height of image data
  int[] px;					    //	pixel data

  public Operator(){
    w=-1; h=-1; size=0;px=null;
  }

  public Operator(int w, int h, int[] px){
    this.w=w;
    this.h=h;
    size=w*h;
    this.px=px;
  }

  public Operator(BufferedImage image){
    w=image.getWidth();
    h=image.getHeight();
    px=null;

    WritableRaster wr=image.getRaster();
    px=wr.getPixels(0,0,w,h,px);
  }

  public int f(int x, int y){
    if((x<0)||(y<0)||(x>=w)||(y>=h)){ return BWHITE; }
    return px[y*w+x];
  }

  public void f(int x, int y, int v){
    if((x<0)||(y<0)||(x>=w)||(y>=h)){ return; }
    px[y*w+x]=v;
  }

  public int f(int[] px, int x, int y){
    if((x<0)||(y<0)||(x>=w)||(y>=h)){ return BWHITE; }
    return px[y*w+x];
  }

  public void f(int[] px, int x, int y, int v){
    if((x<0)||(y<0)||(x>=w)||(y>=h)){ return; }
    px[y*w+x]=v;
  }
*/
}

