/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
/**
 * A class for small buffers of samples in linear, 32-bit
 * floating point format. All samples are normalized to the
 * interval [-1.0...1.0].
 * <p>
 * It is supposed to be a replacement of the byte[] stream architecture of JavaSound, especially for chains of AudioInputStreams. Ideally, all involved AudioInputStreams handle reading into a
 * FloatSampleBuffer.
 * <p>
 * Using a FloatSampleBuffer for streaming has some advantages:
 * <ul>
 * <li>no conversions from bytes have to be done during processing
 * <li>the sample size in bits is irrelevant - normalized range
 * <li>higher quality for processing
 * <li>separated channels
 * <li>potentially less copying of audio data, as processing of the float samples is generally done in-place. The same instance of a FloatSampleBuffer may be used from the data source to the final
 * data sink.
 * </ul>
 * Simple benchmarks showed that the computational power used by the conversion to and from float is neglectible without dithering, and significantly higher with dithering. An own implementation of a
 * random number generator may improve this.
 * <p>
 * It supports &quot;lazy&quot; deletion of samples and channels:
 * <ul>
 * <li>When the sample count is reduced, the arrays are not resized, but only the member variable <code>sampleCount</code> is reduced. A subsequent increase of the sample count (which will occur
 * frequently), will check that and eventually reuse the existing array.
 * <li>When a channel is deleted, it is not removed from memory but only hidden. Subsequent insertions of a channel will check whether a hidden channel can be reused.
 * </ul>
 * The lazy mechanism can save many array instantiation (and copy-) operations for the sake of performance. All relevant methods exist in a second version which allows explicitely to disable lazy
 * deletion.
 * <p>
 * Use the <code>reset</code> functions to clear the memory and remove hidden samples and channels.
 * <p>
 * Note that the lazy mechanism implies that the arrays returned from <code>getChannel(int)</code> may have a greater size than getSampleCount(). Consequently, be sure to never rely on the length
 * field of the sample arrays.
 * <p>
 * As an example, consider a chain of converters that all act on the same instance of FloatSampleBuffer. Some converters may decrease the sample count (e.g. sample rate converter) and delete channels
 * (e.g. PCM2PCM converter). So, processing of one chunk will decrease both. For the next chunk, all starts from the beginning. With the lazy mechanism, all float arrays are only created once for
 * processing all chunks.<br>
 * Having lazy disabled would require for each chunk that is processed
 * <ol>
 * <li>new instantiation of all channel arrays at the converter chain beginning as they have been either deleted or decreased in size during processing of the previous chunk, and
 * <li>re-instantiation of all channel arrays for the reduction of the sample count.
 * </ol>
 * <p>
 * By default, this class uses dithering for reduction of sample width (e.g. original data was 16bit, target data is 8bit). As dithering may be needed in other cases (especially when the float samples
 * are processed using DSP algorithms), or it is preferred to switch it off, dithering can be explicitely switched on or off with the method setDitherMode(int).<br>
 * For a discussion about dithering, see <a href="http://www.iqsoft.com/IQSMagazine/BobsSoapbox/Dithering.htm"> here</a> and <a href="http://www.iqsoft.com/IQSMagazine/BobsSoapbox/Dithering2.htm">
 * here</a>.
 *
 * @author Florian Bomers
 */

package org.alice.media.audio;

/*
 * FloatSampleBuffer.java
 */

import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioFormat;

public class FloatSampleBuffer {

	/** Whether the functions without lazy parameter are lazy or not. */
	private static final boolean LAZY_DEFAULT = true;

	private ArrayList channels = new ArrayList(); // contains for each channel a float array
	private int sampleCount = 0;
	private int channelCount = 0;
	private float sampleRate = 0;
	private int originalFormatType = 0;

	/**
	 * Constant for setDitherMode: dithering will be enabled if sample size is
	 * decreased
	 */
	public static final int DITHER_MODE_AUTOMATIC = 0;
	/** Constant for setDitherMode: dithering will be done */
	public static final int DITHER_MODE_ON = 1;
	/** Constant for setDitherMode: dithering will not be done */
	public static final int DITHER_MODE_OFF = 2;

	private static Random random = null;
	private float ditherBits = 0.8f;
	private boolean doDither = false; // set in convertFloatToBytes
	// e.g. the sample rate converter may want to force dithering
	private int ditherMode = DITHER_MODE_AUTOMATIC;

	// sample width (must be in order !)
	private static final int F_8 = 1;
	private static final int F_16 = 2;
	private static final int F_24 = 3;
	private static final int F_32 = 4;
	private static final int F_SAMPLE_WIDTH_MASK = F_8 | F_16 | F_24 | F_32;
	// format bit-flags
	private static final int F_SIGNED = 8;
	private static final int F_BIGENDIAN = 16;

	// supported formats
	private static final int CT_8S = F_8 | F_SIGNED;
	private static final int CT_8U = F_8;
	private static final int CT_16SB = F_16 | F_SIGNED | F_BIGENDIAN;
	private static final int CT_16SL = F_16 | F_SIGNED;
	private static final int CT_24SB = F_24 | F_SIGNED | F_BIGENDIAN;
	private static final int CT_24SL = F_24 | F_SIGNED;
	private static final int CT_32SB = F_32 | F_SIGNED | F_BIGENDIAN;
	private static final int CT_32SL = F_32 | F_SIGNED;

	//////////////////////////////// initialization /////////////////////////////////

	public FloatSampleBuffer() {
		this( 0, 0, 1 );
	}

	public FloatSampleBuffer( int channelCount, int sampleCount, float sampleRate ) {
		init( channelCount, sampleCount, sampleRate, LAZY_DEFAULT );
	}

	public FloatSampleBuffer( byte[] buffer, int offset, int byteCount,
			AudioFormat format ) {
		this( format.getChannels(),
				byteCount / ( ( format.getSampleSizeInBits() / 8 ) * format.getChannels() ),
				format.getSampleRate() );
		initFromByteArray( buffer, offset, byteCount, format );
	}

	protected void init( int channelCount, int sampleCount, float sampleRate ) {
		init( channelCount, sampleCount, sampleRate, LAZY_DEFAULT );
	}

	protected void init( int channelCount, int sampleCount, float sampleRate, boolean lazy ) {
		if( ( channelCount < 0 ) || ( sampleCount < 0 ) ) {
			throw new IllegalArgumentException(
					"Invalid parameters in initialization of FloatSampleBuffer." );
		}
		setSampleRate( sampleRate );
		if( ( getSampleCount() != sampleCount ) || ( getChannelCount() != channelCount ) ) {
			createChannels( channelCount, sampleCount, lazy );
		}
	}

	private void createChannels( int channelCount, int sampleCount, boolean lazy ) {
		this.sampleCount = sampleCount;
		// lazy delete of all channels. Intentionally lazy !
		this.channelCount = 0;
		for( int ch = 0; ch < channelCount; ch++ ) {
			insertChannel( ch, false, lazy );
		}
		if( !lazy ) {
			// remove hidden channels
			while( channels.size() > channelCount ) {
				channels.remove( channels.size() - 1 );
			}
		}
	}

	public void initFromByteArray( byte[] buffer, int offset, int byteCount,
			AudioFormat format ) {
		initFromByteArray( buffer, offset, byteCount, format, LAZY_DEFAULT );
	}

	public void initFromByteArray( byte[] buffer, int offset, int byteCount,
			AudioFormat format, boolean lazy ) {
		if( ( offset + byteCount ) > buffer.length ) {
			throw new IllegalArgumentException( "FloatSampleBuffer.initFromByteArray: buffer too small." );
		}
		boolean signed = format.getEncoding().equals( AudioFormat.Encoding.PCM_SIGNED );
		if( !signed &&
				!format.getEncoding().equals( AudioFormat.Encoding.PCM_UNSIGNED ) ) {
			throw new IllegalArgumentException( "FloatSampleBuffer: only PCM samples are possible." );
		}
		int bytesPerSample = format.getSampleSizeInBits() / 8;
		int bytesPerFrame = bytesPerSample * format.getChannels();
		int thisSampleCount = byteCount / bytesPerFrame;
		init( format.getChannels(), thisSampleCount, format.getSampleRate(), lazy );
		int formatType = getFormatType( format.getSampleSizeInBits(),
				signed, format.isBigEndian() );
		// save format for automatic dithering mode
		originalFormatType = formatType;
		for( int ch = 0; ch < format.getChannels(); ch++ ) {
			convertByteToFloat( buffer, offset, sampleCount, getChannel( ch ),
					bytesPerFrame, formatType );
			offset += bytesPerSample; // next channel
		}

	}

	public void initFromFloatSampleBuffer( FloatSampleBuffer source ) {
		init( source.getChannelCount(), source.getSampleCount(), source.getSampleRate() );
		for( int ch = 0; ch < getChannelCount(); ch++ ) {
			System.arraycopy( source.getChannel( ch ), 0, getChannel( ch ), 0, sampleCount );
		}
	}

	/**
	 * deletes all channels, frees memory... This also removes hidden channels
	 * by lazy remove.
	 */
	public void reset() {
		init( 0, 0, 1, false );
	}

	/**
	 * destroys any existing data and creates new channels. It also destroys
	 * lazy removed channels and samples.
	 */
	public void reset( int channels, int sampleCount, float sampleRate ) {
		init( channels, sampleCount, sampleRate, false );
	}

	//////////////////////////////// conversion back to bytes /////////////////////////////////

	/**
	 * returns the required size of the buffer when convertToByteArray(..) is
	 * called
	 */
	public int getByteArrayBufferSize( AudioFormat format ) {
		if( !format.getEncoding().equals( AudioFormat.Encoding.PCM_SIGNED ) &&
				!format.getEncoding().equals( AudioFormat.Encoding.PCM_UNSIGNED ) ) {
			throw new IllegalArgumentException( "FloatSampleBuffer: only PCM samples are possible." );
		}
		int bytesPerSample = format.getSampleSizeInBits() / 8;
		int bytesPerFrame = bytesPerSample * format.getChannels();
		return bytesPerFrame * getSampleCount();
	}

	/**
	 * throws exception when buffer is too small or <code>format</code> doesn't
	 * match
	 */
	public void convertToByteArray( byte[] buffer, int offset, AudioFormat format ) {
		int byteCount = getByteArrayBufferSize( format );
		if( ( offset + byteCount ) > buffer.length ) {
			throw new IllegalArgumentException( "FloatSampleBuffer.convertToByteArray: buffer too small." );
		}
		boolean signed = format.getEncoding().equals( AudioFormat.Encoding.PCM_SIGNED );
		if( !signed &&
				!format.getEncoding().equals( AudioFormat.Encoding.PCM_UNSIGNED ) ) {
			throw new IllegalArgumentException( "FloatSampleBuffer.convertToByteArray: only PCM samples are allowed." );
		}
		if( format.getSampleRate() != getSampleRate() ) {
			throw new IllegalArgumentException( "FloatSampleBuffer.convertToByteArray: different samplerates." );
		}
		if( format.getChannels() != getChannelCount() ) {
			throw new IllegalArgumentException( "FloatSampleBuffer.convertToByteArray: different channel count." );
		}
		int bytesPerSample = format.getSampleSizeInBits() / 8;
		int bytesPerFrame = bytesPerSample * format.getChannels();
		int formatType = getFormatType( format.getSampleSizeInBits(),
				signed, format.isBigEndian() );
		for( int ch = 0; ch < format.getChannels(); ch++ ) {
			convertFloatToByte( getChannel( ch ), sampleCount,
					buffer, offset,
					bytesPerFrame, formatType );
			offset += bytesPerSample; // next channel
		}

	}

	/**
	 * Creates a new byte[] buffer and returns it. Throws an exception when
	 * sample rate doesn't match.
	 *
	 * @see #convertToByteArray(byte[], int, AudioFormat)
	 *
	 *      public byte[] convertToByteArray(AudioFormat format) { // throws
	 *      exception when sampleRate doesn't match // creates a new byte[]
	 *      buffer and returns it byte[] res=new
	 *      byte[getByteArrayBufferSize(format)]; convertToByteArray(res, 0,
	 *      format); return res; }
	 *
	 *      //////////////////////////////// actions
	 *      /////////////////////////////////
	 *
	 *      /** Resizes this buffer.
	 *      <p>
	 *      If <code>keepOldSamples</code> is true, as much as possible samples
	 *      are retained. If the buffer is enlarged, silence is added at the
	 *      end. If <code>keepOldSamples</code> is false, existing samples are
	 *      discarded and the buffer contains random samples.
	 */
	public void changeSampleCount( int newSampleCount, boolean keepOldSamples ) {
		int oldSampleCount = getSampleCount();
		Object[] oldChannels = null;
		if( keepOldSamples ) {
			oldChannels = getAllChannels();
		}
		init( getChannelCount(), newSampleCount, getSampleRate() );
		if( keepOldSamples ) {
			// copy old channels and eventually silence out new samples
			int copyCount = newSampleCount < oldSampleCount ? newSampleCount : oldSampleCount;
			for( int ch = 0; ch < getChannelCount(); ch++ ) {
				float[] oldSamples = (float[])oldChannels[ ch ];
				float[] newSamples = (float[])getChannel( ch );
				if( oldSamples != newSamples ) {
					// if this sample array was not object of lazy delete
					System.arraycopy( oldSamples, 0, newSamples, 0, copyCount );
				}
				if( oldSampleCount < newSampleCount ) {
					// silence out new samples
					for( int i = oldSampleCount; i < newSampleCount; i++ ) {
						newSamples[ i ] = 0.0f;
					}
				}
			}
		}
	}

	public void makeSilence() {
		// silence all channels
		if( getChannelCount() > 0 ) {
			makeSilence( 0 );
			for( int ch = 1; ch < getChannelCount(); ch++ ) {
				copyChannel( 0, ch );
			}
		}
	}

	public void makeSilence( int channel ) {
		float[] samples = getChannel( 0 );
		for( int i = 0; i < getSampleCount(); i++ ) {
			samples[ i ] = 0.0f;
		}
	}

	public void addChannel( boolean silent ) {
		// creates new, silent channel
		insertChannel( getChannelCount(), silent );
	}

	/**
	 * lazy insert of a (silent) channel at position <code>index</code>.
	 */
	public void insertChannel( int index, boolean silent ) {
		insertChannel( index, silent, LAZY_DEFAULT );
	}

	/**
	 * Inserts a channel at position <code>index</code>.
	 * <p>
	 * If <code>silent</code> is true, the new channel will be silent. Otherwise
	 * it will contain random data.
	 * <p>
	 * If <code>lazy</code> is true, hidden channels which have at least
	 * getSampleCount() elements will be examined for reusage as inserted
	 * channel.<br>
	 * If <code>lazy</code> is false, still hidden channels are reused, but it
	 * is assured that the inserted channel has exactly getSampleCount()
	 * elements, thus not wasting memory.
	 */
	public void insertChannel( int index, boolean silent, boolean lazy ) {
		int physSize = channels.size();
		int virtSize = getChannelCount();
		float[] newChannel = null;
		if( physSize > virtSize ) {
			// there are hidden channels. Try to use one.
			for( int ch = virtSize; ch < physSize; ch++ ) {
				float[] thisChannel = (float[])channels.get( ch );
				if( ( lazy && ( thisChannel.length >= getSampleCount() ) )
						|| ( !lazy && ( thisChannel.length == getSampleCount() ) ) ) {
					// we found a matching channel. Use it !
					newChannel = thisChannel;
					channels.remove( ch );
					break;
				}
			}
		}
		if( newChannel == null ) {
			newChannel = new float[ getSampleCount() ];
		}
		channels.add( index, newChannel );
		this.channelCount++;
		if( silent ) {
			makeSilence( index );
		}
	}

	/** performs a lazy remove of the channel */
	public void removeChannel( int channel ) {
		removeChannel( channel, LAZY_DEFAULT );
	}

	/**
	 * Removes a channel. If lazy is true, the channel is not physically
	 * removed, but only hidden. These hidden channels are reused by subsequent
	 * calls to addChannel or insertChannel.
	 */
	public void removeChannel( int channel, boolean lazy ) {
		if( !lazy ) {
			channels.remove( channel );
		} else if( channel < ( getChannelCount() - 1 ) ) {
			// if not already, move this channel at the end
			channels.add( channels.remove( channel ) );
		}
		channelCount--;
	}

	/**
	 * both source and target channel have to exist. targetChannel will be
	 * overwritten
	 */
	public void copyChannel( int sourceChannel, int targetChannel ) {
		float[] source = getChannel( sourceChannel );
		float[] target = getChannel( targetChannel );
		System.arraycopy( source, 0, target, 0, getSampleCount() );
	}

	//////////////////////////////// properties /////////////////////////////////

	public int getChannelCount() {
		return channelCount;
	}

	public int getSampleCount() {
		return sampleCount;
	}

	public float getSampleRate() {
		return sampleRate;
	}

	/**
	 * Sets the sample rate of this buffer. NOTE: no conversion is done. The
	 * samples are only re-interpreted.
	 */
	public void setSampleRate( float sampleRate ) {
		if( sampleRate <= 0 ) {
			throw new IllegalArgumentException( "Invalid samplerate for FloatSampleBuffer." );
		}
		this.sampleRate = sampleRate;
	}

	/**
	 * NOTE: the returned array may be larger than sampleCount. So in any case,
	 * sampleCount is to be respected.
	 */
	public float[] getChannel( int channel ) {
		if( ( channel < 0 ) || ( channel >= getChannelCount() ) ) {
			throw new IllegalArgumentException(
					"FloatSampleBuffer: invalid channel number." );
		}
		return (float[])channels.get( channel );
	}

	public Object[] getAllChannels() {
		Object[] res = new Object[ getChannelCount() ];
		for( int ch = 0; ch < getChannelCount(); ch++ ) {
			res[ ch ] = getChannel( ch );
		}
		return res;
	}

	/**
	 * Set the number of bits for dithering. Typically, a value between 0.2 and
	 * 0.9 gives best results.
	 * <p>
	 * Note: this value is only used, when dithering is actually performed.
	 */
	public void setDitherBits( float ditherBits ) {
		if( ditherBits <= 0 ) {
			throw new IllegalArgumentException( "DitherBits must be greater than 0" );
		}
		this.ditherBits = ditherBits;
	}

	public float getDitherBits() {
		return ditherBits;
	}

	/**
	 * Sets the mode for dithering. This can be one of:
	 * <ul>
	 * <li>DITHER_MODE_AUTOMATIC: it is decided automatically, whether dithering
	 * is necessary - in general when sample size is decreased.
	 * <li>DITHER_MODE_ON: dithering will be forced
	 * <li>DITHER_MODE_OFF: dithering will not be done.
	 * </ul>
	 */
	public void setDitherMode( int mode ) {
		if( ( mode != DITHER_MODE_AUTOMATIC )
				&& ( mode != DITHER_MODE_ON )
				&& ( mode != DITHER_MODE_OFF ) ) {
			throw new IllegalArgumentException( "Illegal DitherMode" );
		}
		this.ditherMode = mode;
	}

	public int getDitherMode() {
		return ditherMode;
	}

	/////////////////////////////// "low level" conversion functions ////////////////////////////////

	public int getFormatType( int ssib, boolean signed, boolean bigEndian ) {
		int bytesPerSample = ssib / 8;
		int res = 0;
		if( ssib == 8 ) {
			res = F_8;
		} else if( ssib == 16 ) {
			res = F_16;
		} else if( ssib == 24 ) {
			res = F_24;
		} else if( ssib == 32 ) {
			res = F_32;
		}
		if( res == 0 ) {
			throw new IllegalArgumentException( "FloatSampleBuffer: unsupported sample size of "
					+ ssib + " bits per sample." );
		}
		if( !signed && ( bytesPerSample > 1 ) ) {
			throw new IllegalArgumentException( "FloatSampleBuffer: unsigned samples larger than "
					+ "8 bit are not supported" );
		}
		if( signed ) {
			res |= F_SIGNED;
		}
		if( bigEndian && ( ssib != 8 ) ) {
			res |= F_BIGENDIAN;
		}
		return res;
	}

	private static final float twoPower7 = 128.0f;
	private static final float twoPower15 = 32768.0f;
	private static final float twoPower23 = 8388608.0f;
	private static final float twoPower31 = 2147483648.0f;

	private static final float invTwoPower7 = 1 / twoPower7;
	private static final float invTwoPower15 = 1 / twoPower15;
	private static final float invTwoPower23 = 1 / twoPower23;
	private static final float invTwoPower31 = 1 / twoPower31;

	/* public */
	private static void convertByteToFloat( byte[] input, int offset, int sampleCount,
			float[] output, int bytesPerFrame,
			int formatType ) {
		//if (TDebug.TraceAudioConverter) {
		//    TDebug.out("FloatSampleBuffer.convertByteToFloat, formatType="
		//           +formatType2Str(formatType));
		//}
		int sample;
		for( sample = 0; sample < sampleCount; sample++ ) {
			// do conversion
			switch( formatType ) {
			case CT_8S:
				output[ sample ] =
						( (float)input[ offset ] ) * invTwoPower7;
				break;
			case CT_8U:
				output[ sample ] =
						( (float)( ( input[ offset ] & 0xFF ) - 128 ) ) * invTwoPower7;
				break;
			case CT_16SB:
				output[ sample ] =
						( (float)( ( input[ offset ] << 8 )
								| ( input[ offset + 1 ] & 0xFF ) ) ) * invTwoPower15;
				break;
			case CT_16SL:
				output[ sample ] =
						( (float)( ( input[ offset + 1 ] << 8 )
								| ( input[ offset ] & 0xFF ) ) ) * invTwoPower15;
				break;
			case CT_24SB:
				output[ sample ] =
						( (float)( ( input[ offset ] << 16 )
								| ( ( input[ offset + 1 ] & 0xFF ) << 8 )
								| ( input[ offset + 2 ] & 0xFF ) ) ) * invTwoPower23;
				break;
			case CT_24SL:
				output[ sample ] =
						( (float)( ( input[ offset + 2 ] << 16 )
								| ( ( input[ offset + 1 ] & 0xFF ) << 8 )
								| ( input[ offset ] & 0xFF ) ) ) * invTwoPower23;
				break;
			case CT_32SB:
				output[ sample ] =
						( (float)( ( input[ offset ] << 24 )
								| ( ( input[ offset + 1 ] & 0xFF ) << 16 )
								| ( ( input[ offset + 2 ] & 0xFF ) << 8 )
								| ( input[ offset + 3 ] & 0xFF ) ) ) * invTwoPower31;
				break;
			case CT_32SL:
				output[ sample ] =
						( (float)( ( input[ offset + 3 ] << 24 )
								| ( ( input[ offset + 2 ] & 0xFF ) << 16 )
								| ( ( input[ offset + 1 ] & 0xFF ) << 8 )
								| ( input[ offset ] & 0xFF ) ) ) * invTwoPower31;
				break;
			default:
				throw new IllegalArgumentException( "Unsupported formatType=" + formatType );
			}
			offset += bytesPerFrame;
		}
	}

	protected byte quantize8( float sample ) {
		if( doDither ) {
			sample += random.nextFloat() * ditherBits;
		}
		if( sample >= 127.0f ) {
			return (byte)127;
		} else if( sample <= -128 ) {
			return (byte)-128;
		} else {
			return (byte)( sample < 0 ? ( sample - 0.5f ) : ( sample + 0.5f ) );
		}
	}

	protected int quantize16( float sample ) {
		if( doDither ) {
			sample += random.nextFloat() * ditherBits;
		}
		if( sample >= 32767.0f ) {
			return 32767;
		} else if( sample <= -32768.0f ) {
			return -32768;
		} else {
			return (int)( sample < 0 ? ( sample - 0.5f ) : ( sample + 0.5f ) );
		}
	}

	protected int quantize24( float sample ) {
		if( doDither ) {
			sample += random.nextFloat() * ditherBits;
		}
		if( sample >= 8388607.0f ) {
			return 8388607;
		} else if( sample <= -8388608.0f ) {
			return -8388608;
		} else {
			return (int)( sample < 0 ? ( sample - 0.5f ) : ( sample + 0.5f ) );
		}
	}

	protected int quantize32( float sample ) {
		if( doDither ) {
			sample += random.nextFloat() * ditherBits;
		}
		if( sample >= 2147483647.0f ) {
			return 2147483647;
		} else if( sample <= -2147483648.0f ) {
			return -2147483648;
		} else {
			return (int)( sample < 0 ? ( sample - 0.5f ) : ( sample + 0.5f ) );
		}
	}

	// should be static and public, but dithering needs class members
	private void convertFloatToByte( float[] input, int sampleCount,
			byte[] output, int offset,
			int bytesPerFrame, int formatType ) {
		//if (TDebug.TraceAudioConverter) {
		//    TDebug.out("FloatSampleBuffer.convertFloatToByte, formatType="
		//               +"formatType2Str(formatType));
		//}

		// let's see whether dithering is necessary
		switch( ditherMode ) {
		case DITHER_MODE_AUTOMATIC:
			doDither = ( originalFormatType & F_SAMPLE_WIDTH_MASK ) > ( formatType & F_SAMPLE_WIDTH_MASK );
			break;
		case DITHER_MODE_ON:
			doDither = true;
			break;
		case DITHER_MODE_OFF:
			doDither = false;
			break;
		}
		if( doDither && ( random == null ) ) {
			// create the random number generator for dithering
			random = new Random();
		}
		int inIndex;
		int iSample;
		for( inIndex = 0; inIndex < sampleCount; inIndex++ ) {
			// do conversion
			switch( formatType ) {
			case CT_8S:
				output[ offset ] = quantize8( input[ inIndex ] * twoPower7 );
				break;
			case CT_8U:
				output[ offset ] = (byte)( quantize8( input[ inIndex ] * twoPower7 ) + 128 );
				break;
			case CT_16SB:
				iSample = quantize16( input[ inIndex ] * twoPower15 );
				output[ offset ] = (byte)( iSample >> 8 );
				output[ offset + 1 ] = (byte)( iSample & 0xFF );
				break;
			case CT_16SL:
				iSample = quantize16( input[ inIndex ] * twoPower15 );
				output[ offset + 1 ] = (byte)( iSample >> 8 );
				output[ offset ] = (byte)( iSample & 0xFF );
				break;
			case CT_24SB:
				iSample = quantize24( input[ inIndex ] * twoPower23 );
				output[ offset ] = (byte)( iSample >> 16 );
				output[ offset + 1 ] = (byte)( ( iSample >>> 8 ) & 0xFF );
				output[ offset + 2 ] = (byte)( iSample & 0xFF );
				break;
			case CT_24SL:
				iSample = quantize24( input[ inIndex ] * twoPower23 );
				output[ offset + 2 ] = (byte)( iSample >> 16 );
				output[ offset + 1 ] = (byte)( ( iSample >>> 8 ) & 0xFF );
				output[ offset ] = (byte)( iSample & 0xFF );
				break;
			case CT_32SB:
				iSample = quantize32( input[ inIndex ] * twoPower31 );
				output[ offset ] = (byte)( iSample >> 24 );
				output[ offset + 1 ] = (byte)( ( iSample >>> 16 ) & 0xFF );
				output[ offset + 2 ] = (byte)( ( iSample >>> 8 ) & 0xFF );
				output[ offset + 3 ] = (byte)( iSample & 0xFF );
				break;
			case CT_32SL:
				iSample = quantize32( input[ inIndex ] * twoPower31 );
				output[ offset + 3 ] = (byte)( iSample >> 24 );
				output[ offset + 2 ] = (byte)( ( iSample >>> 16 ) & 0xFF );
				output[ offset + 1 ] = (byte)( ( iSample >>> 8 ) & 0xFF );
				output[ offset ] = (byte)( iSample & 0xFF );
				break;
			default:
				throw new IllegalArgumentException( "Unsupported formatType=" + formatType );
			}
			offset += bytesPerFrame;
		}
	}

	/**
	 * Debugging function
	 */
	private static String formatType2Str( int formatType ) {
		String res = "" + formatType + ": ";
		switch( formatType & F_SAMPLE_WIDTH_MASK ) {
		case F_8:
			res += "8bit";
			break;
		case F_16:
			res += "16bit";
			break;
		case F_24:
			res += "24bit";
			break;
		case F_32:
			res += "32bit";
			break;
		}
		res += ( ( formatType & F_SIGNED ) == F_SIGNED ) ? " signed" : " unsigned";
		if( ( formatType & F_SAMPLE_WIDTH_MASK ) != F_8 ) {
			res += ( ( formatType & F_BIGENDIAN ) == F_BIGENDIAN ) ? " big endian" : " little endian";
		}
		return res;
	}

}

/*** FloatSampleBuffer.java ***/
