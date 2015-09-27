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
package edu.cmu.cs.dennisc.java.io;

/**
 * @author Dennis Cosgrove
 */
public class SerializationUtilities {
	public static void serialize( java.io.Serializable serializable, java.io.ObjectOutputStream oos ) {
		try {
			oos.writeObject( serializable );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static void serialize( java.io.Serializable serializable, java.io.File outFile ) {
		try {
			java.io.FileOutputStream fos = new java.io.FileOutputStream( outFile );
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream( fos );
			oos.writeObject( serializable );
			fos.flush();
			fos.close();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( outFile.getAbsolutePath(), ioe );
		}
	}

	public static java.io.Serializable unserialize( java.io.ObjectInputStream ois ) {
		try {
			return (java.io.Serializable)ois.readObject();
		} catch( ClassNotFoundException cnfe ) {
			throw new RuntimeException( cnfe );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static java.io.Serializable unserialize( java.io.File inFile ) {
		try {
			java.io.FileInputStream fis = new java.io.FileInputStream( inFile );
			java.io.ObjectInputStream ois = new java.io.ObjectInputStream( fis );
			java.io.Serializable serializable = (java.io.Serializable)ois.readObject();
			fis.close();
			return serializable;
		} catch( ClassNotFoundException cnfe ) {
			throw new RuntimeException( inFile.getAbsolutePath(), cnfe );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( inFile.getAbsolutePath(), ioe );
		}
	}

	public static void serializeBufferedImage( java.awt.image.BufferedImage bufferedImage, java.io.ObjectOutputStream oos ) {
		try {
			if( bufferedImage != null ) {
				int width = bufferedImage.getWidth();
				int height = bufferedImage.getHeight();
				//				int imageType = m_bufferedImage.getType();
				//				if( imageType == java.awt.image.BufferedImage.TYPE_CUSTOM ) {
				//				}

				int[] pixels = bufferedImage.getRGB( 0, 0, width, height, null, 0, width );
				oos.writeObject( pixels );
				oos.writeInt( width );
				oos.writeInt( height );
			} else {
				oos.writeObject( null );
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static void serializeBufferedImage( java.awt.image.BufferedImage bufferedImage, java.io.File outFile ) {
		try {
			java.io.FileOutputStream fos = new java.io.FileOutputStream( outFile );
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream( fos );
			serializeBufferedImage( bufferedImage, oos );
			fos.flush();
			fos.close();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( outFile.getAbsolutePath(), ioe );
		}
	}

	public static java.awt.image.BufferedImage unserializeBufferedImage( java.io.ObjectInputStream ois ) {
		try {
			java.awt.image.BufferedImage bufferedImage;
			Object o = ois.readObject();
			if( o instanceof int[] ) {
				int[] pixels = (int[])o;
				int width = ois.readInt();
				int height = ois.readInt();
				bufferedImage = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
				bufferedImage.setRGB( 0, 0, width, height, pixels, 0, width );
			} else {
				assert o == null;
				bufferedImage = null;
			}
			return bufferedImage;
		} catch( ClassNotFoundException cnfe ) {
			throw new RuntimeException( cnfe );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static java.awt.image.BufferedImage unserializeBufferedImage( java.io.File inFile ) {
		try {
			java.io.FileInputStream fis = new java.io.FileInputStream( inFile );
			java.io.ObjectInputStream ois = new java.io.ObjectInputStream( fis );
			java.awt.image.BufferedImage bufferedImage = unserializeBufferedImage( ois );
			fis.close();
			return bufferedImage;
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( inFile.getAbsolutePath(), ioe );
		}
	}

}
