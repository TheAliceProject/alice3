/**
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import java.io.File;
import java.io.IOException;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import ch.randelshofer.media.quicktime.QuickTimeWriter;
import ch.randelshofer.media.quicktime.QuickTimeWriter.VideoFormat;

import edu.cmu.cs.dennisc.movie.MovieEncoder;

/**
 * @author dculyba
 *
 */
public class ImagesToQuickTimeEncoder implements MovieEncoder
{
	private QuickTimeWriter qtWriter;
	
	private static AudioFormat QUICKTIME_AUDIO_FORMAT = new AudioFormat(22050f, 16, 1, true, false);
	private static VideoFormat QUICKTIME_VIDEO_FORMAT = VideoFormat.JPG;
	private static long TIME_SCALE = 1000;
	
	private float framesPerSecond;
	private int currentTrackIndex;
	private int audioTrackIndex;
	private int videoTrackIndex;
	private boolean initializedVideo = false;
	private int width = -1;
	private int height = -1;

	private ProcessImagesRunnable imageProcessingRunnable = null;
	private Queue<BufferedImage> images = new LinkedList<BufferedImage>();
	private Object imageQueueLock = new Object();
	
	private BufferedImage thumbnailImage = null;
	private BufferedImage imageBuffer = null;
	private Graphics2D bufferGraphics = null;
	
	boolean isRunning = false;
	
	private int imageCount;
	
	private List<EncoderListener> listeners = new LinkedList<EncoderListener>();
	
	private File outputFile = null;
	
	private class ProcessImagesRunnable implements Runnable
	{
		private boolean isRunning;
		private boolean forceStop = false;
		private boolean isStopped = true;
		
		public void stop()
		{
			this.isRunning = false;
		}
		
		public void forceStop()
		{
			this.forceStop = true;
		}
		
		public void waitUntilStopped()
		{
			while (!this.isStopped)
			{
				try { Thread.sleep(20); } catch (Exception e) {}
			}
		}
		
		@Override
		public void run() {
			this.isRunning = true;
			this.isStopped = false;
			//Loop while we are running and there are images in the queue
			while (true){
				if (this.forceStop)
				{
					break;
				}
				//process all the available images
				while (true){
					if (this.forceStop)
					{
						break;
					}
					BufferedImage nextImage = null;
					synchronized (ImagesToQuickTimeEncoder.this.imageQueueLock) {
						if (ImagesToQuickTimeEncoder.this.images.isEmpty()){
							break;
						}
						else{
							nextImage = ImagesToQuickTimeEncoder.this.images.remove();
						}
					}
					ImagesToQuickTimeEncoder.this.addImageToMovie(nextImage);
				}
				//No more images to process
				//If we're not running anymore, break out
				if (this.forceStop || !this.isRunning){
					break;
				}
				//Else sleep to wait for more images
				else{
					try{
						Thread.sleep(50);
					}
					catch (Exception e){}
				}
			}
			this.isStopped = true;
		}
	}
	
	public ImagesToQuickTimeEncoder(float framesPerSecond)
	{
		this.framesPerSecond = framesPerSecond;
		this.imageCount = 0;
	}
	
	
	public ImagesToQuickTimeEncoder(float framesPerSecond, File out) throws IOException
	{
		this(framesPerSecond);
		setOutput(out);
	}
	
	public void setOutput(File out)
	{
		this.outputFile = out;
	}
	
	public void setDimensions(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	private void initializeVideo() throws IOException
	{
		if (!this.initializedVideo)
		{
			this.qtWriter.addVideoTrack(QUICKTIME_VIDEO_FORMAT, (long)(TIME_SCALE*this.framesPerSecond), this.width, this.height);
			this.videoTrackIndex = this.currentTrackIndex++;
			this.initializedVideo = true;
			
            this.imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            this.bufferGraphics = this.imageBuffer.createGraphics();
            this.bufferGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		}
	}
	
	private static BufferedImage deepCopy(BufferedImage bi)
    {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

	public void addListener(EncoderListener listener)
    {
        if (!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }
	
	public boolean isRunning()
	{
		return this.isRunning;
	}
	
	private void addImageToMovie(BufferedImage bufferedImage)
	{
		try
		{
			if (!this.initializedVideo)
			{
				int width = bufferedImage.getWidth();
				int height = bufferedImage.getHeight();
				setDimensions(width, height);
				initializeVideo();
			}
			this.bufferGraphics.drawImage(bufferedImage, 0, 0, width, height, null);
			this.qtWriter.writeFrame(this.videoTrackIndex, this.imageBuffer, TIME_SCALE);
			if (this.imageCount == 0)
			{
				this.thumbnailImage = deepCopy(bufferedImage);
			}
			this.imageCount++;
			for (EncoderListener l : this.listeners)
            {
                l.frameUpdate(this.imageCount);
            }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void addBufferedImage(BufferedImage bufferedImage)
    {
		try
		{
			synchronized (this.imageQueueLock) {
				this.images.add(deepCopy(bufferedImage));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
	
	public BufferedImage getThumbnailImage()
	{
		return this.thumbnailImage;
	}
	
	public void addAudioTrack(AudioInputStream audioStream, double length) throws IOException
	{ 
		this.qtWriter.addAudioTrack(QUICKTIME_AUDIO_FORMAT);
		this.audioTrackIndex = this.currentTrackIndex++;
		this.qtWriter.writeSample(this.audioTrackIndex, audioStream, (long)(length*TIME_SCALE));
	}
	
	public void close() throws IOException
	{
		this.qtWriter.close();
	}

	private void initializeWriter() throws IOException
	{
		if (this.imageProcessingRunnable != null)
		{
			this.imageProcessingRunnable.forceStop();
		}
		this.qtWriter = new QuickTimeWriter(this.outputFile);
		this.currentTrackIndex = 0;
		this.initializedVideo = false;
		if (this.width != -1 && this.height != -1)
		{
			this.initializeVideo();
		}
		this.imageProcessingRunnable = new ProcessImagesRunnable();
		Thread t = new Thread(this.imageProcessingRunnable);
		t.start();
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.cs.dennisc.movie.MovieEncoder#start()
	 */
	@Override
	public void start() {
		this.isRunning = true;
		this.imageCount = 0;
		try
		{
			initializeWriter();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		for (EncoderListener l : this.listeners)
        {
            l.encodingStarted();
        }
	}

	/* (non-Javadoc)
	 * @see edu.cmu.cs.dennisc.movie.MovieEncoder#stop()
	 */
	@Override
	public void stop() {
		if (this.imageProcessingRunnable != null)
		{
			this.imageProcessingRunnable.stop();
			this.imageProcessingRunnable.waitUntilStopped();
		}
		this.isRunning = false;
		boolean success = false;
		try
		{
			this.close();
			success = true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		for (EncoderListener l : this.listeners)
        {
            l.encodingFinished(success);
        }
		
	}
}
