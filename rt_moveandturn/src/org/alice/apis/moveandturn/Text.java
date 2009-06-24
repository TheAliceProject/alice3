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
package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public class Text extends Model {
	private edu.cmu.cs.dennisc.scenegraph.Text m_sgText = new edu.cmu.cs.dennisc.scenegraph.Text();
	private double m_letterHeight = 1.0;
	private StringBuffer m_sb = new StringBuffer();
	@Override
	protected void createSGGeometryIfNecessary() {
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		return m_sgText;
	}
	
	private void updateSGText() {
		m_sgText.text.setValue( m_sb.toString() );
	}
	
	public String getValue() {
		return m_sb.toString();
	}
	public void setValue( String text ) {
		m_sb = new StringBuffer( text );
		updateSGText();
	}

	private void updateScale() {
		//todo:
		final double FACTOR = 1/12.0;
		getSGVisual().scale.setValue( edu.cmu.cs.dennisc.math.ScaleUtilities.newScaleMatrix3d( FACTOR, FACTOR, FACTOR ) );
	}
	
	public Font getFont() {
		return new Font( m_sgText.font.getValue() );
	}
	public void setFont( Font font ) {
		m_sgText.font.setValue( font.getAsAWTFont() );
		updateScale();
	}
	
	public Double getLetterHeight() {
		return m_letterHeight;
	}
	public void setLetterHeight( Number letterHeight ) {
		m_letterHeight = letterHeight.doubleValue();
		updateScale();
	}

	public void append( Object value ) {
		m_sb.append( value );
		updateSGText();
	}
	
	public Character charAt( Integer index ) {
		return m_sb.charAt( index );
	}

	public void delete( Integer start, Integer end ) {
		m_sb.delete( start, end );
		updateSGText();
	}
	public void deleteCharAt( Integer index ) {
		m_sb.deleteCharAt( index );
		updateSGText();
	}

	public Integer indexOf( String s ) {
		return m_sb.indexOf( s );
	}
	public Integer indexOf( String s, Integer fromIndex ) {
		return m_sb.indexOf( s, fromIndex );
	}

	public void insert( Integer offset, Object value ) {
		m_sb.append( value );
		updateSGText();
	}

	public Integer lastIndexOf( String s ) {
		return m_sb.lastIndexOf( s );
	}
	public Integer lastIndexOf( String s, Integer fromIndex ) {
		return m_sb.lastIndexOf( s, fromIndex );
	}
	
	//todo: rename length?
	public Integer getLength() {
		return m_sb.length();
	}

	public void replace( Integer start, Integer end, String s ) {
		m_sb.replace( start, end, s );
		updateSGText();
	}

	public void setCharAt( Integer index, Character c ) {
		m_sb.setCharAt( index, c );
		updateSGText();
	}
	
//	public void setLength( Integer length ) {
//		m_sb.setLength( length );
//		updateSGText();
//	}
}
