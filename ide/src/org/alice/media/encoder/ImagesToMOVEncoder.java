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
package org.alice.media.encoder;

import java.io.*;
import java.io.ObjectInputStream.GetField;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;

import javax.imageio.ImageIO;
import javax.media.*;
import javax.media.control.*;
import javax.media.protocol.*;
import javax.media.datasink.*;
import javax.media.format.VideoFormat;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.alice.media.YouTubeUploader;

import edu.cmu.cs.dennisc.animation.AnimationObserver;
import edu.cmu.cs.dennisc.animation.MediaPlayerObserver;
import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.movie.MovieEncoder;

/**
 * @author David Culyba
 */

/*
 * @(#)JpegImagesToMovie.java	1.3 01/03/13
 *
 * Copyright (c) 1999-2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

/**
 * This program takes a list of JPEG image files and convert them into a
 * QuickTime movie.
 */
public class ImagesToMOVEncoder implements ControllerListener,
        DataSinkListener, MovieEncoder
{
    private int frameRate = 30;
    private BlockingQueue<BufferedImage> images = new LinkedBlockingQueue<BufferedImage>();
    private Object waitSync = new Object();
    private boolean stateTransitionOK = true;
    private Object waitFileSync = new Object();
    private boolean fileDone = false;
    private boolean fileSuccess = true;

    private boolean hasStarted = false;
    private boolean isRunning = false;
    private int height = -1;
    private int width = -1;

    private DataSink dsink;
    private Processor p;
    private ImageDataSource ids;

    private File outputFile;
    private boolean encodingSuccess = false;
    private BufferedImage firstFrame;

    private boolean dumpFramesToFile = false;
    private String frameOutputDirectory = null;
    private int frameCount = 0;

    private List<EncoderListener> listeners = new LinkedList<EncoderListener>();

    public ImagesToMOVEncoder()
    {

    }

    public ImagesToMOVEncoder(File outputFile)
    {
        this.setOutputFile(outputFile);
    }

    public ImagesToMOVEncoder(int frameRate)
    {
        this.setFrameRate(frameRate);
    }

    public ImagesToMOVEncoder(File outputFile, int frameRate)
    {
        this.setOutputFile(outputFile);
        this.setFrameRate(frameRate);
    }

    public void addListener(EncoderListener listener)
    {
        if (!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }

    public void setFrameOutputDirectory(String outputDirectory)
    {
        this.frameOutputDirectory = outputDirectory;
        if (this.frameOutputDirectory != null)
        {
            this.dumpFramesToFile = true;
        } else
        {
            this.dumpFramesToFile = false;
        }
    }

    public void setOutputFile(File outputFile)
    {
        this.outputFile = outputFile;
    }

    public File getOutputFile()
    {
        return this.outputFile;
    }

    public boolean getEncodingSuccess()
    {
        return this.encodingSuccess;
    }

    public void setFrameRate(int frameRate)
    {
        this.frameRate = frameRate;
    }

    private MediaLocator createMediaLocator(String url)
    {
        MediaLocator ml;
        if (url.indexOf(":") > 0 && (ml = new MediaLocator(url)) != null)
        {
            return ml;
        }

        if (url.startsWith(File.separator))
        {
            if ((ml = new MediaLocator("file:" + url)) != null)
            {
                return ml;
            }
        } else
        {
            String file = "file:" + System.getProperty("user.dir")
                    + File.separator + url;
            if ((ml = new MediaLocator(file)) != null)
            {
                return ml;
            }
        }
        return null;
    }

    private boolean doIt()
    {

        MediaLocator outML = this.createMediaLocator("file://"
                + this.outputFile.getAbsolutePath());
        if (this.images.size() <= 0)
        {
            return false;
        }
        this.ids = new ImageDataSource(this.width, this.height, this.frameRate,
                this.images);
        try
        {
            // System.err.println(
            // "- create processor for the image datasource ..." );
            this.p = Manager.createProcessor(this.ids);
        } catch (Exception e)
        {
            System.err
                    .println("Yikes!  Cannot create a processor from the data source.");
            return false;
        }

        this.p.addControllerListener(this);

        // Put the Processor into configured state so we can set
        // some processing options on the processor.
        this.p.configure();
        if (!waitForState(this.p, Processor.Configured))
        {
            System.err.println("Failed to configure the processor.");
            return false;
        }

        // Set the output content descriptor to QuickTime.
        this.p.setContentDescriptor(new ContentDescriptor(
                FileTypeDescriptor.QUICKTIME));

        // Query for the processor for supported formats.
        // Then set it on the processor.
        TrackControl tcs[] = this.p.getTrackControls();
        Format f[] = tcs[0].getSupportedFormats();
        if (f == null || f.length <= 0)
        {
            System.err.println("The mux does not support the input format: "
                    + tcs[0].getFormat());
            return false;
        }

        tcs[0].setFormat(f[0]);

        // System.err.println( "Setting the track format to: " + f[ 0 ] );

        // We are done with programming the processor. Let's just
        // realize it.
        this.p.realize();
        if (!waitForState(this.p, Controller.Realized))
        {
            System.err.println("Failed to realize the processor.");
            return false;
        }

        // Now, we'll need to create a DataSink.
        if ((this.dsink = createDataSink(this.p, outML)) == null)
        {
            System.err
                    .println("Failed to create a DataSink for the given output MediaLocator: "
                            + outML);
            return false;
        }

        this.dsink.addDataSinkListener(this);
        this.fileDone = false;
        // System.err.println( "start processing..." );

        // OK, we can now start the actual transcoding.
        try
        {
            this.p.start();
            this.dsink.start();
        } catch (IOException e)
        {
            System.err.println("IO error during processing");
            return false;
        }
        return true;
    }

    public boolean isRunning()
    {
        return this.isRunning;
    }

    public BufferedImage getFirstFrame()
    {
        return this.firstFrame;
    }

    protected static String getFileNameForFrame(int frame, String extension)
    {
        return "frame_" + String.format("%05d", frame) + "." + extension;
    }

    static BufferedImage deepCopy(BufferedImage bi)
    {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void addBufferedImage(BufferedImage bufferedImage)
    {
        // TODO Auto-generated method stub
        if (this.isRunning)
        {
            try
            {
                while (!this.canAddImage())
                {
                    Thread.sleep(20);
                }
                if (this.dumpFramesToFile)
                {
                    try
                    {
                        ImageIO.write(
                                bufferedImage,
                                "png",
                                new File(this.frameOutputDirectory,
                                        getFileNameForFrame(this.frameCount,
                                                "png")));
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                this.images.put(deepCopy(bufferedImage));
                if (!this.hasStarted)
                {
                    this.hasStarted = true;
                    this.height = bufferedImage.getHeight();
                    this.width = bufferedImage.getWidth();
                    this.firstFrame = new java.awt.image.BufferedImage(width,
                            height, java.awt.image.BufferedImage.TYPE_3BYTE_BGR);

                    // java.awt.Graphics g = this.firstFrame.getGraphics();
                    // g.drawImage( bufferedImage, 0, 0, null );
                    // // todo: investigate - does dispose ensure the image is
                    // finished
                    // // drawing?
                    // g.dispose();

                    this.doIt();
                }
                this.frameCount++;
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
        {
            System.err.println("Trying to add an image after we've stopped!");
        }
    }

    public boolean canAddImage()
    {
        if (!this.images.isEmpty())
        {
            return false;
        }
        return true;
    }

    public void start()
    {
        // TODO Auto-generated method stub
        this.images.clear();
        this.isRunning = true;
        this.frameCount = 0;
        if (this.dumpFramesToFile)
        {
            File directory = new File(this.frameOutputDirectory);
            directory.mkdirs();
        }
        for (EncoderListener l : this.listeners)
        {
            l.encodingStarted();
        }
    }

	public void stop()
    {
        this.hasStarted = false;
        this.isRunning = false;
        if (this.ids != null)
        {
            this.ids.doneAddingImages();
        }
        // Wait for EndOfStream event.
        waitForFileDone();
        // Cleanup.
        this.encodingSuccess = true;
        try
        {
            this.dsink.close();
        } catch (Exception e)
        {
            this.encodingSuccess = false;
        }
        this.p.removeControllerListener(this);
        this.p.close();
        this.ids = null;
        this.p = null;
        this.dsink = null;

        for (EncoderListener l : this.listeners)
        {
            l.encodingFinished(this.encodingSuccess);
        }

    }

    /**
     * Create the DataSink.
     */
    private DataSink createDataSink(Processor p, MediaLocator outML)
    {

        DataSource ds;

        if ((ds = p.getDataOutput()) == null)
        {
            System.err
                    .println("Something is really wrong: the processor does not have an output DataSource");
            return null;
        }

        DataSink dsink;

        try
        {
            // System.err.println( "- create DataSink for: " + outML );
            dsink = Manager.createDataSink(ds, outML);
            dsink.open();
        } catch (Exception e)
        {
            System.err.println("Cannot create the DataSink: " + e);
            return null;
        }

        return dsink;
    }

    /**
     * Block until the processor has transitioned to the given state. Return
     * false if the transition failed.
     */
    boolean waitForState(Processor p, int state)
    {
        synchronized (this.waitSync)
        {
            try
            {
                while (p.getState() < state && stateTransitionOK)
                {
                    waitSync.wait();
                }
            } catch (Exception e)
            {
            }
        }
        return this.stateTransitionOK;
    }

    /**
     * Controller Listener.
     */
	public void controllerUpdate(ControllerEvent evt)
    {

        if (evt instanceof ConfigureCompleteEvent
                || evt instanceof RealizeCompleteEvent
                || evt instanceof PrefetchCompleteEvent)
        {
            synchronized (this.waitSync)
            {
                stateTransitionOK = true;
                waitSync.notifyAll();
            }
        } else if (evt instanceof ResourceUnavailableEvent)
        {
            synchronized (this.waitSync)
            {
                stateTransitionOK = false;
                waitSync.notifyAll();
            }
        } else if (evt instanceof EndOfMediaEvent)
        {
            evt.getSourceController().stop();
            evt.getSourceController().close();
        }
    }

    /**
     * Block until file writing is done.
     */
    boolean waitForFileDone()
    {
        synchronized (this.waitFileSync)
        {
            try
            {
                while (!fileDone)
                {
                    waitFileSync.wait();
                }
            } catch (Exception e)
            {
            }
        }
        return this.fileSuccess;
    }

    /**
     * Event handler for the file writer.
     */
	public void dataSinkUpdate(DataSinkEvent evt)
    {

        if (evt instanceof EndOfStreamEvent)
        {
            synchronized (this.waitFileSync)
            {
                fileDone = true;
                waitFileSync.notifyAll();
            }
        } else if (evt instanceof DataSinkErrorEvent)
        {
            synchronized (this.waitFileSync)
            {
                fileDone = true;
                fileSuccess = false;
                waitFileSync.notifyAll();
            }
        }
    }

    // /////////////////////////////////////////////
    //
    // Inner classes.
    // /////////////////////////////////////////////

    /**
     * A DataSource to read from a list of JPEG image files and turn that into a
     * stream of JMF buffers. The DataSource is not seekable or positionable.
     */
    class ImageDataSource extends PullBufferDataSource
    {
        ImageSourceStream streams[];

        ImageDataSource(int width, int height, int frameRate,
                BlockingQueue<BufferedImage> images)
        {
            streams = new ImageSourceStream[1];
            streams[0] = new ImageSourceStream(width, height, frameRate, images);
        }

        @Override
        public void setLocator(MediaLocator source)
        {
        }

        @Override
        public MediaLocator getLocator()
        {
            return null;
        }

        /**
         * Content type is of RAW since we are sending buffers of video frames
         * without a container format.
         */
        @Override
        public String getContentType()
        {
            return ContentDescriptor.RAW;
        }

        @Override
        public void connect()
        {
        }

        @Override
        public void disconnect()
        {
        }

        @Override
        public void start()
        {
        }

        @Override
        public void stop()
        {
        }

        public void doneAddingImages()
        {
            streams[0].doneAddingImages();
        }

        /**
         * Return the ImageSourceStreams.
         */
        @Override
        public PullBufferStream[] getStreams()
        {
            return streams;
        }

        /**
         * We could have derived the duration from the number of frames and
         * frame rate. But for the purpose of this program, it's not necessary.
         */
        @Override
        public Time getDuration()
        {
            return DURATION_UNKNOWN;
        }

        @Override
        public Object[] getControls()
        {
            return new Object[0];
        }

        @Override
        public Object getControl(String type)
        {
            return null;
        }
    }

    /**
     * The source stream to go along with ImageDataSource.
     */
    class ImageSourceStream implements PullBufferStream
    {

        BlockingQueue<BufferedImage> images;
        int width, height;
        VideoFormat format;
        BufferedImage jpegBuffer;

        int imageCount = 0; // index of the next image to be read.
        boolean ended = false;
        boolean doneReadingImages = false;

        public ImageSourceStream(int width, int height, int frameRate,
                BlockingQueue<BufferedImage> images)
        {
            this.width = width;
            this.height = height;
            this.images = images;
            this.format = new VideoFormat(VideoFormat.JPEG, new Dimension(
                    width, height), Format.NOT_SPECIFIED, Format.byteArray,
                    (float) frameRate);
        }

        /**
         * We should never need to block assuming data are read from files.
         */
		public boolean willReadBlock()
        {
            if (!this.doneReadingImages && this.images.isEmpty())
            {
                return true;
            }
            return false;
        }

        /**
         * This is called from the Processor to read a frame worth of video
         * data.
         */
		public void read(Buffer buf) throws IOException
        {

            // Check if we've finished all the frames.
            if (this.doneReadingImages && this.images.isEmpty())
            {
                // We are done. Set EndOfMedia.
                // System.err.println( "Done reading all images." );
                this.ended = true;
                this.jpegBuffer = null;
                buf.setEOM(true);
                buf.setOffset(0);
                buf.setLength(0);
                return;
            }
            try
            {

                BufferedImage image = images.take();

                BufferedImage jpegImage = null;

                if (image.getType() == java.awt.image.BufferedImage.TYPE_3BYTE_BGR)
                {
                    jpegImage = image;
                }

                if (jpegImage == null)
                {
                    if (this.jpegBuffer == null)
                    {
                        this.jpegBuffer = new java.awt.image.BufferedImage(
                                width, height,
                                java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
                    }
                    java.awt.Graphics2D g = this.jpegBuffer.createGraphics();
                    g.drawImage(image, 0, 0, null);
                    // todo: investigate - does dispose ensure the image is
                    // finished
                    // drawing?
                    g.dispose();
                    jpegImage = this.jpegBuffer;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.setUseCache(false);
                javax.imageio.ImageIO.write(jpegImage, "jpeg", baos);

                baos.flush();
                byte[] imageBytes = baos.toByteArray();
                buf.setData(imageBytes);

                buf.setOffset(0);
                buf.setLength(imageBytes.length);
                buf.setFormat(format);
                buf.setFlags(buf.getFlags() | Buffer.FLAG_KEY_FRAME);

                for (EncoderListener l : ImagesToMOVEncoder.this.listeners)
                {
                    l.frameUpdate(this.imageCount);
                }
                this.imageCount++;
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        /**
         * Return the format of each video frame. That will be JPEG.
         */
		public Format getFormat()
        {
            return format;
        }

		public ContentDescriptor getContentDescriptor()
        {
            return new ContentDescriptor(ContentDescriptor.RAW);
        }

		public long getContentLength()
        {
            return 0;
        }

		public boolean endOfStream()
        {
            return ended;
        }

		public Object[] getControls()
        {
            return new Object[0];
        }
		
		public Object getControl(String type)
        {
            return null;
        }

        public void doneAddingImages()
        {
            this.doneReadingImages = true;
        }
    }

	public MediaPlayerObserver getMediaPlayerObserver() {
		return null;
	}

}
