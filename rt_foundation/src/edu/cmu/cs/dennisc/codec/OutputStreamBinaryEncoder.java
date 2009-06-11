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
public class OutputStreamBinaryEncoder extends AbstractBinaryEncoder {
	private java.io.ObjectOutputStream m_oos;

	public OutputStreamBinaryEncoder( java.io.OutputStream os ) {
		try {
			m_oos = new java.io.ObjectOutputStream( os );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Override
	protected void encodeBuffer( byte[] buffer ) {
		try {
			m_oos.write( buffer );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public void encode( boolean value ) {
		try {
			m_oos.writeBoolean( value );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public void encode( byte value ) {
		try {
			m_oos.writeByte( value );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public void encode( char value ) {
		try {
			m_oos.writeChar( value );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public void encode( double value ) {
		try {
			m_oos.writeDouble( value );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public void encode( float value ) {
		try {
			m_oos.writeFloat( value );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public void encode( int value ) {
		try {
			m_oos.writeInt( value );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public void encode( long value ) {
		try {
			m_oos.writeLong( value );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public void encode( short value ) {
		try {
			m_oos.writeShort( value );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public void encode( String value ) {
		try {
			m_oos.writeBoolean( value != null );
			m_oos.writeUTF( value );
			//m_oos.writeInt( value.length() );
			//m_oos.writeChars( value );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public void flush() {
		try {
			m_oos.flush();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
}
