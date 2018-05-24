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
package edu.cmu.cs.dennisc.codec;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UTFDataFormatException;

/**
 * @author Dennis Cosgrove
 */
public class InputStreamBinaryDecoder extends AbstractBinaryDecoder {
	private ObjectInputStream m_ois;

	public InputStreamBinaryDecoder( InputStream is ) {
		initialize( is );
	}

	public InputStreamBinaryDecoder( File file ) {
		try {
			initialize( new FileInputStream( file ) );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public InputStreamBinaryDecoder( String path ) {
		this( new File( path ) );
	}

	private void initialize( InputStream is ) {
		try {
			m_ois = new ObjectInputStream( is );
		} catch( IOException ioe ) {
			throw new RuntimeException( is.toString(), ioe );
		}
	}

	@Override
	public byte[] readFully( byte[] rv ) {
		try {
			m_ois.readFully( rv );
			return rv;
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	public byte[] readFully( byte[] rv, int offset, int length ) {
		try {
			m_ois.readFully( rv, offset, length );
			return rv;
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	public boolean decodeBoolean() {
		try {
			return m_ois.readBoolean();
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	public byte decodeByte() {
		try {
			return m_ois.readByte();
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	public char decodeChar() {
		try {
			return m_ois.readChar();
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	public double decodeDouble() {
		try {
			return m_ois.readDouble();
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	public float decodeFloat() {
		try {
			return m_ois.readFloat();
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	public int decodeInt() {
		try {
			return m_ois.readInt();
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	public long decodeLong() {
		try {
			return m_ois.readLong();
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	public short decodeShort() {
		try {
			return m_ois.readShort();
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	public String decodeString() {
		try {
			// todo?
			//			int length = m_ois.readInt();
			//			char[] chars = new char[ length ];
			//			for( int i=0; i<length; i++ ) {
			//				chars[ i ] = m_ois.readChar();
			//			}
			//			return new String( chars );
			boolean isNotNull = m_ois.readBoolean();
			if( isNotNull ) {
				return m_ois.readUTF();
			} else {
				return null;
			}
		} catch( UTFDataFormatException utfdfe ) {
			throw new RuntimeException( utfdfe );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
}
