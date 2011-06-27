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
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.alice.media.audio.AudioTrackMixer;
import org.alice.media.audio.ScheduledAudioStream;

import ch.randelshofer.media.mp3.MP3AudioInputStream;
import ch.randelshofer.media.quicktime.QuickTimeWriter;
import ch.randelshofer.media.quicktime.QuickTimeWriter.VideoFormat;

import edu.cmu.cs.dennisc.animation.Animation;
import edu.cmu.cs.dennisc.animation.AnimationObserver;
import edu.cmu.cs.dennisc.animation.MediaPlayerObserver;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation;
import edu.cmu.cs.dennisc.movie.MovieEncoder;

/**
 * @author dculyba
 *
 */
public class ImagesToQuickTimeEncoder implements MovieEncoder, MediaPlayerObserver
{
//	private static AudioFormat QUICKTIME_AUDIO_FORMAT = new AudioFormat(22050f, 16, 1, true, false);
	private static float RATE_22 = 22050f;
	private static float RATE_44 = 44100f;
	private static AudioFormat QUICKTIME_AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, RATE_44, 16, 1, 2, RATE_44, false);
	private static VideoFormat QUICKTIME_VIDEO_FORMAT = VideoFormat.JPG;
	
	private float framesPerSecond;
	private int width = -1;
	private int height = -1;
	private File outputFile = null;
	private File frameDirectory;
	private BufferedImage thumbnailImage = null;
	boolean isRunning = false;
	private int frameCount;
	private List<ScheduledAudioStream> audioStreams = new LinkedList<ScheduledAudioStream>();
	
	private List<EncoderListener> listeners = new LinkedList<EncoderListener>();

	
	public static File createTempDirectory() throws IOException {
		final File temp;

		temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}

		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}

		return (temp);
	}
	
	public ImagesToQuickTimeEncoder(float framesPerSecond)
	{
		this.framesPerSecond = framesPerSecond;
		this.frameCount = 0;
	}
	
	private void initializeFrameDirectory()
	{
		try
		{
			this.frameDirectory = createTempDirectory();
		}
		catch (Exception e)
		{
			this.frameDirectory = null;
		}
	}
	
	private void writeVideoAndAudio(File[] imgFiles, File audioFile, QuickTimeWriter.VideoFormat videoFormat, boolean passThrough, String streaming) throws IOException {
        File tmpFile = streaming.equals("none") ? this.outputFile : new File(this.outputFile.getPath() + ".tmp");
        AudioInputStream audioIn = null;
        QuickTimeWriter qtOut = null;
        BufferedImage imgBuffer = null;
        Graphics2D g = null;

        try {
            // Determine audio format
            if (audioFile.getName().toLowerCase().endsWith(".mp3")) {
                audioIn = new MP3AudioInputStream(audioFile);
            } else {
                audioIn = AudioSystem.getAudioInputStream(audioFile);
            }
            AudioFormat audioFormat = audioIn.getFormat();
            boolean isVBR = false;

            // Determine duration of a single sample
            int asDuration = (int) (audioFormat.getSampleRate() / audioFormat.getFrameRate());
            int vsDuration = 100;
            // Create writer
            qtOut = new QuickTimeWriter(videoFormat == QuickTimeWriter.VideoFormat.RAW ? this.outputFile : tmpFile);
            qtOut.addAudioTrack(audioFormat); // audio in track 0
            qtOut.addVideoTrack(videoFormat, (int) (this.framesPerSecond * vsDuration), this.width, this.height);  // video in track 1

            // Create audio buffer
            int asSize;
            byte[] audioBuffer;
            if (isVBR) {
                // => variable bit rate: create audio buffer for a single frame
                asSize = audioFormat.getFrameSize();
                audioBuffer = new byte[asSize];
            } else {
                // => fixed bit rate: create audio buffer for half a second
                asSize = audioFormat.getChannels() * audioFormat.getSampleSizeInBits() / 8;
                audioBuffer = new byte[(int) (qtOut.getMediaTimeScale(0) / 2 * asSize)];
            }

            // Create video buffer
            if (!passThrough) {
                imgBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                g = imgBuffer.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            } // Main loop
            int movieTime = 0;
            int imgIndex = 0;
            boolean isAudioDone = false;
            while ((imgIndex < imgFiles.length || !isAudioDone)) {
                // Advance movie time by half a second (we interleave twice per second)
                movieTime += qtOut.getMovieTimeScale() / 2;

                // Advance audio to movie time + 1 second (audio must be ahead of video by 1 second)
                while (!isAudioDone && qtOut.getTrackDuration(0) < movieTime + qtOut.getMovieTimeScale()) {
                    int len = audioIn.read(audioBuffer);
                    if (len == -1) {
                        isAudioDone = true;
                    } else {
                        qtOut.writeSamples(0, len / asSize, audioBuffer, 0, len, asDuration);
                    }
                    if (isVBR) {
                        // => variable bit rate: format can change at any time
                        audioFormat = audioIn.getFormat();
                        if (audioFormat == null) {
                            break;
                        }
                        asSize = audioFormat.getFrameSize();
                        asDuration = (int) (audioFormat.getSampleRate() / audioFormat.getFrameRate());
                        if (audioBuffer.length < asSize) {
                            audioBuffer = new byte[asSize];
                        }
                    }
                }

                // Advance video to movie time
                while (imgIndex < imgFiles.length && qtOut.getTrackDuration(1) < movieTime) {
                    // catch up with video time
                    if (passThrough) {
                        qtOut.writeSample(1, imgFiles[imgIndex], vsDuration);
                    } else {
                        BufferedImage fImg = ImageIO.read(imgFiles[imgIndex]);
                        if (fImg == null) {
                            continue;
                        }
                        g.drawImage(fImg, 0, 0, width, height, null);
                        fImg.flush();
                        qtOut.writeFrame(1, imgBuffer, vsDuration);
                    }
                    ++imgIndex;
                }
            }
            if (streaming.equals("fastStart")) {
                qtOut.toWebOptimizedMovie(this.outputFile, false);
                tmpFile.delete();
            } else if (streaming.equals("fastStartCompressed")) {
                qtOut.toWebOptimizedMovie(this.outputFile, true);
                tmpFile.delete();
            }
                qtOut.close();
                qtOut = null;
        } catch (UnsupportedAudioFileException e) {
            IOException ioe = new IOException(e.getMessage());
            ioe.initCause(e);
            throw ioe;
        } finally {
            if (qtOut != null) {
                qtOut.close();
            }
            if (audioIn != null) {
                audioIn.close();
            }
            if (g != null) {
                g.dispose();
            }
            if (imgBuffer != null) {
                imgBuffer.flush();
            }
        }
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
	
	protected static String getFileNameForFrame(int frame, String extension)
    {
		return "frame_" + String.format("%05d", frame) + "." + extension;
	}
	
	private void addImageToMovie(BufferedImage bufferedImage)
	{
		try
		{
			if (this.width == -1 || this.height == -1)
			{
				this.width = bufferedImage.getWidth();
				this.height = bufferedImage.getHeight();
			}
			if (ImagesToQuickTimeEncoder.this.frameDirectory != null)
            {
                try
                {
                    ImageIO.write(
                            bufferedImage,
                            "png",
                            new File(ImagesToQuickTimeEncoder.this.frameDirectory,
                                    getFileNameForFrame(this.frameCount,
                                            "png")));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
			if (this.frameCount == 0)
			{
				this.thumbnailImage = deepCopy(bufferedImage);
			}
			this.frameCount++;
			for (EncoderListener l : this.listeners)
            {
                l.frameUpdate(this.frameCount);
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
			addImageToMovie(deepCopy(bufferedImage));
			
//			synchronized (this.imageQueueLock) {
//				this.images.add(deepCopy(bufferedImage));
//			}
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
	

	private void initializeWriter() throws IOException
	{
		if (this.frameDirectory != null)
		{
			FileUtilities.delete(this.frameDirectory);
		}
		initializeFrameDirectory();
		this.audioStreams.clear();
	}
	
	private double movieLength()
	{
		double totalTime = this.frameCount / this.framesPerSecond;
		return totalTime;
	}
	
	private File createAudioFile()
	{
		if (this.audioStreams.size() > 0)
		{
			AudioTrackMixer mixer = new AudioTrackMixer(QUICKTIME_AUDIO_FORMAT, movieLength());
			for (ScheduledAudioStream stream : this.audioStreams)
			{
				mixer.addScheduledStream(stream);
			}
			try
			{
				File tempFile = File.createTempFile("TEMP_soundTrack", ".wav");
				FileOutputStream out = new FileOutputStream(tempFile);
				mixer.write(out);
				out.close();
				return tempFile;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.cs.dennisc.movie.MovieEncoder#start()
	 */
	@Override
	public void start() {
		this.isRunning = true;
		this.frameCount = 0;
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
		this.isRunning = false;
		boolean success = false;
		File audioFile = null;
		try
		{
			audioFile = this.createAudioFile();
			File[] imgFiles = this.frameDirectory.listFiles();
			writeVideoAndAudio(imgFiles, audioFile, QUICKTIME_VIDEO_FORMAT, false, "none");
			success = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			success = false;
		}
		finally
		{
			if (audioFile != null)
			{
				FileUtilities.delete(audioFile);
			}
			if (this.frameDirectory != null)
			{
				FileUtilities.delete(this.frameDirectory);
			}
		}
		for (EncoderListener l : this.listeners)
        {
            l.encodingFinished(success);
        }
		
	}


	@Override
	public MediaPlayerObserver getMediaPlayerObserver() {
		return this;
//		return null;
	}


	@Override
	public void playerStarted(MediaPlayerAnimation playerAnimation, double playTime) {
		edu.cmu.cs.dennisc.media.Player player = playerAnimation.getPlayer();
		if (player instanceof edu.cmu.cs.dennisc.media.jmf.Player)
		{
			edu.cmu.cs.dennisc.media.jmf.Player jmfPlayer = (edu.cmu.cs.dennisc.media.jmf.Player)player;
			ScheduledAudioStream audioStream = new ScheduledAudioStream(jmfPlayer.getAudioResource(), playTime, jmfPlayer.getStartTime(), jmfPlayer.getStopTime(), jmfPlayer.getVolumeLevel());
			this.audioStreams.add(audioStream);
		}
	}


	@Override
	public void started(Animation animation) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void finished(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
