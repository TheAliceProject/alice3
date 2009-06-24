/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.codec;

/**
 * @author Dennis Cosgrove
 */
public class InputStreamBinaryDecoder extends AbstractBinaryDecoder {
	private java.io.ObjectInputStream m_ois;
	public InputStreamBinaryDecoder( java.io.InputStream is ) {
		initialize( is );
	}
	public InputStreamBinaryDecoder( java.io.File file ) {
		try {
			initialize( new java.io.FileInputStream( file ) );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public InputStreamBinaryDecoder( String path ) {
		this( new java.io.File( path ) );
	}
	private void initialize( java.io.InputStream is ) {
		try {
			m_ois = new java.io.ObjectInputStream( is );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	protected byte[] decodeByteArray( byte[] rv ) {
		try {
			m_ois.readFully( rv );
			return rv;
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public boolean decodeBoolean() {
		try {
			return m_ois.readBoolean();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public byte decodeByte() {
		try {
			return m_ois.readByte();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public char decodeChar() {
		try {
			return m_ois.readChar();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public double decodeDouble() {
		try {
			return m_ois.readDouble();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public float decodeFloat() {
		try {
			return m_ois.readFloat();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public int decodeInt() {
		try {
			return m_ois.readInt();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public long decodeLong() {
		try {
			return m_ois.readLong();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public short decodeShort() {
		try {
			return m_ois.readShort();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public String decodeString() {
		try {
			// todo?
			//			int length = m_ois.readInt();
			//			char[] chars = new char[ length ];
			//			for( int i=0; i<length; i++ ) {
			//				chars[ i ] = m_ois.readChar();
			//			}
			//			return new String( chars );
			if( m_ois.readBoolean() ) {
				return m_ois.readUTF();
			} else {
				return null;
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
}
