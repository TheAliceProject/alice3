/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

package edu.cmu.cs.dennisc.nebulous.javabased;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
//import static com.jogamp.opengl.GL.*;
//import static com.jogamp.opengl.GL2.*;

public class Texture
{
    private static final ColorModel glAlphaColorModel = 
        new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
            new int[] {8,8,8,8},
            true,
            false,
            ComponentColorModel.TRANSLUCENT,
            DataBuffer.TYPE_BYTE);

    /** The color model for the GL2 image */
    private static final  ColorModel glColorModel =
            new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,0},
                false,
                false,
                ComponentColorModel.OPAQUE,
                DataBuffer.TYPE_BYTE);
    
    protected static ByteBuffer convertImageData(BufferedImage bufferedImage, boolean flip) 
    { 
        ByteBuffer imageBuffer = null; 
        WritableRaster raster;
        BufferedImage texImage = null;
        int texWidth = bufferedImage.getWidth();
        int texHeight = bufferedImage.getHeight();
        // create a raster that can be used by OpenGL as a source
        // for a texture
        boolean forceAlpha = false;
        if (bufferedImage.getColorModel().hasAlpha() || forceAlpha) {
//            depth = 32;
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
        } else {
//            depth = 24;
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable());
        }
        
     // copy the source image into the produced image
        Graphics2D g = (Graphics2D) texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        if (flip) {
            g.scale(1,-1);
            g.drawImage(bufferedImage,0,-texHeight,null);
        } else {
            g.drawImage(bufferedImage,0,0,null);
        }
        
        // build a byte buffer from the temporary image 
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 
        imageBuffer = ByteBuffer.allocateDirect(data.length); 
        imageBuffer.order(ByteOrder.nativeOrder()); 
        imageBuffer.put(data, 0, data.length); 
        imageBuffer.flip();
        g.dispose();
        
        return imageBuffer; 
    } 
    
//    protected static BufferedImage makeAwtImage(com.jme.image.Image jmeImage, boolean flip)
//    {
//        ByteBuffer imageData = jmeImage.getData().get(0);
//        byte[] imageBytes = new byte[imageData.remaining()];
//        imageData.get(imageBytes, 0, imageBytes.length);
//        int byteSize = com.jme.image.Image.getEstimatedByteSize(jmeImage.getFormat());
//        BufferedImage img = null;
//        if (byteSize == 4) 
//        {
//            //The alpha seems messed up, so we're going to take the alpha away
//            for (int i=0; i<imageBytes.length; i++)
//            {
//                if (i%4 == 3)
//                {
//                    imageBytes[i] = (byte)0xff;
//                }
//            }
//            BufferedImage img1 = new BufferedImage(jmeImage.getWidth(), jmeImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
//            img1.getRaster().setDataElements(0, 0, jmeImage.getWidth(), jmeImage.getHeight(), imageBytes);
//            img = new BufferedImage(jmeImage.getWidth(), jmeImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//            Graphics2D g = (Graphics2D) img.getGraphics();
//            g.setColor(new Color(0f,0f,0f,0f));
//            g.fillRect(0,0,img.getWidth(),img.getHeight());
//            if (flip)
//            {
//                g.scale(1,-1);
//                g.drawImage(img1,0,-img.getHeight(),null);
//            }
//            else
//            {
//                g.drawImage(img1,0,0,null);
//            }
//            try
//            {
//                ImageIO.write(img, "png", new File("C:/test.png"));
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//        else if (byteSize == 3)
//        {
//            img = new BufferedImage(jmeImage.getWidth(), jmeImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//            img.getRaster().setDataElements(0, 0, jmeImage.getWidth(), jmeImage.getHeight(), imageBytes);
//        }
//        return img;
//    }
    
    
    private static final boolean IS_MIPMAPPING_DESIRED = true;
    private static List<Integer> sNamesToForget = new LinkedList<Integer>();

    private String name;
    private BufferedImage image;
    private ByteBuffer buffer;
    private int textureID = -1;
    private boolean isMipMapped = false;
    
    public Texture(File file) throws IOException
    {
        String fileName = Utilities.getNameFromFile(file);
        this.init(file, fileName);
    }
    
    public Texture(File file, String name) throws IOException
    {
        this.init(file, name);
    }
    
    public Texture(BufferedImage image, String name)
    {
        this.init(image, name);
    }
    
//    public Texture( com.jme.image.Texture jmeTexture, String name, boolean flip )
//    {
//        this.init(makeAwtImage(jmeTexture.getImage(), flip), name);
//    }
    
    private void init(BufferedImage image, String name)
    {
        this.image = image;
        this.name = name;
        this.buffer = convertImageData(this.image, true);
    }
    
    private void init(File imageFile, String name) throws IOException
    {
        BufferedImage bi = ImageIO.read(imageFile);
        this.init(bi, name);
    }
    
    public boolean hasAlpha()
    {
        if (image != null)
        {
            return image.getColorModel().hasAlpha();
        }
        return false;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public BufferedImage getImage()
    {
        return this.image;
    }
    
    public File getOutputFile(String rootPath)
    {
        File outputFile =  new File(rootPath, this.name+".png");
        return outputFile;
    }
    
    public File writeToFile(String rootPath) throws IOException
    {
        File rootPathFile = new File(rootPath);
        return writeToFile(rootPathFile);
    }
    
    public File writeToFile(File rootPath) throws IOException
    {
        File outputFile =  new File(rootPath, this.name+".png");
        ImageIO.write(this.image, "png", outputFile);
        return outputFile;
    }
    
    public void writeToStream(OutputStream oStream) throws IOException
    {
        ImageIO.write(this.image, "png", oStream);
    }
    
//    private boolean setUpImage(edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc)
//    {
//        while( rc.gl.glGetError() != GL_NO_ERROR ) {
//            //pass
//        }
//        int glPixelFormat = GL_RGB;
//        if (this.hasAlpha())
//        {
//            glPixelFormat = GL_RGBA;
//            
//        }
//        else
//        {
//            glPixelFormat = GL_RGB;
//        }
//        if( IS_MIPMAPPING_DESIRED ) {
//            rc.glu.gluBuild2DMipmaps( GL_TEXTURE_2D,          //target
//                                     glPixelFormat,             //internal level
//                                     this.image.getWidth(),     //width
//                                     this.image.getHeight(),    //height
//                                     glPixelFormat,             //format
//                                     GL_UNSIGNED_BYTE,       //type
//                                     this.buffer );             //data
//            if( rc.gl.glGetError() != GL_NO_ERROR ) {
//                return false;
//            }
//        }
//        rc.gl.glTexImage2D(GL_TEXTURE_2D, 0, glPixelFormat, this.image.getWidth(), this.image.getHeight(), 0, glPixelFormat, GL_UNSIGNED_BYTE, this.buffer );
//        return IS_MIPMAPPING_DESIRED;
//    }
//    
//    public void setUpTexture(edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc)
//    {
//        if (sNamesToForget.size() > 0)
//        {
//            int[] namesToForget = Utilities.convertListToIntArray(sNamesToForget);
//            rc.gl.glDeleteTextures( namesToForget.length, namesToForget, 0 );
//        }
//        sNamesToForget.clear();
//
//        if( this.image != null && this.buffer != null ) 
//        {
//            if( this.textureID != -1 && rc.gl.glIsTexture( this.textureID ) ) 
//            {
//                rc.gl.glBindTexture(GL_TEXTURE_2D, this.textureID );
//            } 
//            else 
//            {
//                int[] ids = new int[1];
//                rc.gl.glGenTextures( 1, ids, 0 );
//                this.textureID = ids[0];
//                rc.gl.glBindTexture(GL_TEXTURE_2D, this.textureID );
//                this.isMipMapped = this.setUpImage(rc);
//            }
//            rc.gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
//            rc.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
//            rc.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
//            if( this.isMipMapped ) 
//            {
//                rc.gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
//            } 
//            else 
//            {
//                rc.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//            }
//            rc.gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//            rc.gl.glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
//            rc.gl.glEnable( GL_TEXTURE_2D );
//            if (this.hasAlpha())
//            {
//                rc.gl.glEnable (GL_BLEND);
//                rc.gl.glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
//            }
//            else
//            {
//                rc.gl.glDisable (GL_BLEND);
//            }
//        }
//    }
}
