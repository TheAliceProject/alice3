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
