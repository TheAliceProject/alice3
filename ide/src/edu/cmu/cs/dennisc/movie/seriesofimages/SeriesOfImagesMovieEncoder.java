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
package edu.cmu.cs.dennisc.movie.seriesofimages;

/**
 * @author Dennis Cosgrove
 */
public class SeriesOfImagesMovieEncoder implements edu.cmu.cs.dennisc.movie.MovieEncoder {
	private String m_directoryPath;
	private String m_fileName;
	private java.text.NumberFormat m_numberFormat;
	private String m_extension;
	private int m_index;

	public SeriesOfImagesMovieEncoder( String directoryPath, String fileName, String localizedPattern, String extension ) {
		m_directoryPath = directoryPath;
		m_fileName = fileName;
		m_numberFormat = java.text.NumberFormat.getInstance();
		if( m_numberFormat instanceof java.text.DecimalFormat ) {
			java.text.DecimalFormat df = (java.text.DecimalFormat)m_numberFormat;
			df.applyLocalizedPattern( localizedPattern );
		}
		m_extension = extension;
		m_index = -1;
	}
	
	private void getPathForIndex( StringBuffer sb, int index ) {
		sb.append( m_directoryPath );
		sb.append( java.io.File.separatorChar );
		sb.append( m_fileName );
		sb.append( m_numberFormat.format( index ) );
		sb.append( "." );
		sb.append( m_extension );
	}
	public String getPathForIndex( int index ) {
		StringBuffer sb = new StringBuffer();
		getPathForIndex( sb, index );
		return sb.toString();
	}
	
	public void start() {
		m_index = 0;
	}
	public void addBufferedImage( java.awt.image.BufferedImage bufferedImage ) {
		assert m_index != -1;
		String path = getPathForIndex( m_index++ );
		java.io.File file = new java.io.File( path );
		file.getParentFile().mkdirs();
		edu.cmu.cs.dennisc.image.ImageUtilities.write( path, bufferedImage );
	}
	public void stop() {
		m_index = -1;
	}
}
