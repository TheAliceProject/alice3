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

package edu.cmu.cs.dennisc.java.util;

public class BufferUtilities {
	private BufferUtilities() {
		throw new AssertionError();
	}

	public static byte[] convertByteBufferToArray( java.nio.ByteBuffer buf ) {
		if( buf.hasArray() ) {
			return buf.array();
		} else {
			buf.rewind();
			byte[] array = new byte[ buf.remaining() ];
			int index = 0;
			while( buf.hasRemaining() ) {
				array[ index++ ] = buf.get();
			}
			return array;
		}
	}

	public static char[] convertCharBufferToArray( java.nio.CharBuffer buf ) {
		if( buf.hasArray() ) {
			return buf.array();
		} else {
			buf.rewind();
			char[] array = new char[ buf.remaining() ];
			int index = 0;
			while( buf.hasRemaining() ) {
				array[ index++ ] = buf.get();
			}
			return array;
		}
	}

	public static short[] convertShortBufferToArray( java.nio.ShortBuffer buf ) {
		if( buf.hasArray() ) {
			return buf.array();
		} else {
			buf.rewind();
			short[] array = new short[ buf.remaining() ];
			int index = 0;
			while( buf.hasRemaining() ) {
				array[ index++ ] = buf.get();
			}
			return array;
		}
	}

	public static int[] convertIntBufferToArray( java.nio.IntBuffer buf ) {
		if( buf.hasArray() ) {
			return buf.array();
		} else {
			buf.rewind();
			int[] array = new int[ buf.remaining() ];
			int index = 0;
			while( buf.hasRemaining() ) {
				array[ index++ ] = buf.get();
			}
			return array;
		}
	}

	public static long[] convertLongBufferToArray( java.nio.LongBuffer buf ) {
		if( buf.hasArray() ) {
			return buf.array();
		} else {
			buf.rewind();
			long[] array = new long[ buf.remaining() ];
			int index = 0;
			while( buf.hasRemaining() ) {
				array[ index++ ] = buf.get();
			}
			return array;
		}
	}

	public static float[] convertFloatBufferToArray( java.nio.FloatBuffer buf ) {
		if( buf.hasArray() ) {
			return buf.array();
		} else {
			buf.rewind();
			float[] array = new float[ buf.remaining() ];
			int index = 0;
			while( buf.hasRemaining() ) {
				array[ index++ ] = buf.get();
			}
			return array;
		}
	}

	public static double[] convertDoubleBufferToArray( java.nio.DoubleBuffer buf ) {
		if( buf.hasArray() ) {
			return buf.array();
		} else {
			buf.rewind();
			double[] array = new double[ buf.remaining() ];
			int index = 0;
			while( buf.hasRemaining() ) {
				array[ index++ ] = buf.get();
			}
			return array;
		}
	}

	public static java.nio.DoubleBuffer createDirectDoubleBuffer( double[] data ) {
		if( data == null ) {
			return null;
		}
		java.nio.DoubleBuffer buf = java.nio.ByteBuffer.allocateDirect( ( Double.SIZE / 8 ) * data.length ).order( java.nio.ByteOrder.nativeOrder() ).asDoubleBuffer();
		buf.clear();
		buf.put( data );
		buf.flip();
		return buf;
	}

	public static java.nio.DoubleBuffer copyDoubleBuffer( java.nio.DoubleBuffer data ) {
		if( data == null ) {
			return null;
		}
		java.nio.DoubleBuffer buf = createDirectDoubleBuffer( convertDoubleBufferToArray( data ) );
		return buf;
	}

	public static java.nio.FloatBuffer createDirectFloatBuffer( float[] data ) {
		if( data == null ) {
			return null;
		}
		java.nio.FloatBuffer buf = java.nio.ByteBuffer.allocateDirect( ( Float.SIZE / 8 ) * data.length ).order( java.nio.ByteOrder.nativeOrder() ).asFloatBuffer();
		buf.clear();
		buf.put( data );
		buf.flip();
		return buf;
	}

	public static java.nio.FloatBuffer copyFloatBuffer( java.nio.FloatBuffer data ) {
		if( data == null ) {
			return null;
		}
		java.nio.FloatBuffer buf = createDirectFloatBuffer( convertFloatBufferToArray( data ) );
		return buf;
	}

	public static java.nio.IntBuffer createDirectIntBuffer( int[] data ) {
		if( data == null ) {
			return null;
		}
		java.nio.IntBuffer buf = java.nio.ByteBuffer.allocateDirect( ( Integer.SIZE / 8 ) * data.length ).order( java.nio.ByteOrder.nativeOrder() ).asIntBuffer();
		buf.clear();
		buf.put( data );
		buf.flip();
		return buf;
	}

	public static java.nio.IntBuffer copyIntBuffer( java.nio.IntBuffer data ) {
		if( data == null ) {
			return null;
		}
		java.nio.IntBuffer buf = createDirectIntBuffer( convertIntBufferToArray( data ) );
		return buf;
	}

	public static java.nio.LongBuffer createDirectLongBuffer( long[] data ) {
		if( data == null ) {
			return null;
		}
		java.nio.LongBuffer buf = java.nio.ByteBuffer.allocateDirect( ( Long.SIZE / 8 ) * data.length ).order( java.nio.ByteOrder.nativeOrder() ).asLongBuffer();
		buf.clear();
		buf.put( data );
		buf.flip();
		return buf;
	}

	public static java.nio.LongBuffer copyLongBuffer( java.nio.LongBuffer data ) {
		if( data == null ) {
			return null;
		}
		java.nio.LongBuffer buf = createDirectLongBuffer( convertLongBufferToArray( data ) );
		return buf;
	}

	public static java.nio.ByteBuffer createDirectByteBuffer( byte[] data ) {
		if( data == null ) {
			return null;
		}
		java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocateDirect( ( Byte.SIZE / 8 ) * data.length ).order( java.nio.ByteOrder.nativeOrder() );
		buf.clear();
		buf.put( data );
		buf.flip();
		return buf;
	}

	public static java.nio.ByteBuffer copyByteBuffer( java.nio.ByteBuffer data ) {
		if( data == null ) {
			return null;
		}
		java.nio.ByteBuffer buf = createDirectByteBuffer( convertByteBufferToArray( data ) );
		return buf;
	}

	public static java.nio.CharBuffer createDirectCharBuffer( char[] data ) {
		if( data == null ) {
			return null;
		}
		java.nio.CharBuffer buf = java.nio.ByteBuffer.allocateDirect( ( Character.SIZE / 8 ) * data.length ).order( java.nio.ByteOrder.nativeOrder() ).asCharBuffer();
		buf.clear();
		buf.put( data );
		buf.flip();
		return buf;
	}

	public static java.nio.CharBuffer copyCharBuffer( java.nio.CharBuffer data ) {
		if( data == null ) {
			return null;
		}
		java.nio.CharBuffer buf = createDirectCharBuffer( convertCharBufferToArray( data ) );
		return buf;
	}

	public static java.nio.ShortBuffer createDirectShortBuffer( short[] data ) {
		if( data == null ) {
			return null;
		}
		java.nio.ShortBuffer buf = java.nio.ByteBuffer.allocateDirect( ( Short.SIZE / 8 ) * data.length ).order( java.nio.ByteOrder.nativeOrder() ).asShortBuffer();
		buf.clear();
		buf.put( data );
		buf.flip();
		return buf;
	}

	public static java.nio.ShortBuffer copyShortBuffer( java.nio.ShortBuffer data ) {
		if( data == null ) {
			return null;
		}
		java.nio.ShortBuffer buf = createDirectShortBuffer( convertShortBufferToArray( data ) );
		return buf;
	}
}
