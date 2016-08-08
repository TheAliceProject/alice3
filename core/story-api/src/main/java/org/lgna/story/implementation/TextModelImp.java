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

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public class TextModelImp extends SimpleModelImp {
	public TextModelImp( org.lgna.story.STextModel abstraction ) {
		this.abstraction = abstraction;
		this.sgText.text.setValue( "" );

		final int height = 12;
		final double scale = 1.0 / height;
		this.sgText.font.setValue( new java.awt.Font( null, 0, height ) );
		this.getSgVisuals()[ 0 ].geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.sgText } );
		this.getSgVisuals()[ 0 ].scale.setValue( edu.cmu.cs.dennisc.math.ScaleUtilities.newScaleMatrix3d( scale, scale, scale ) );
	}

	@Override
	public org.lgna.story.STextModel getAbstraction() {
		return this.abstraction;
	}

	private void updateSGText() {
		this.sgText.text.setValue( this.sb.toString() );
	}

	public String getValue() {
		return this.sb.toString();
	}

	public void setValue( String text ) {
		this.sb = new StringBuffer( text );
		updateSGText();
	}

	public java.awt.Font getFont() {
		return this.sgText.font.getValue();
	}

	public void setFont( java.awt.Font font ) {
		this.sgText.font.setValue( font );
	}

	public void append( Object value ) {
		this.sb.append( value );
		this.updateSGText();
	}

	public char charAt( int index ) {
		return this.sb.charAt( index );
	}

	public void delete( int start, int end ) {
		this.sb.delete( start, end );
		this.updateSGText();
	}

	public void deleteCharAt( int index ) {
		this.sb.deleteCharAt( index );
		this.updateSGText();
	}

	public int indexOf( String s ) {
		return this.sb.indexOf( s );
	}

	public int indexOf( String s, int fromIndex ) {
		return this.sb.indexOf( s, fromIndex );
	}

	public void insert( int offset, Object value ) {
		this.sb.append( value );
		this.updateSGText();
	}

	public int lastIndexOf( String s ) {
		return this.sb.lastIndexOf( s );
	}

	public int lastIndexOf( String s, int fromIndex ) {
		return this.sb.lastIndexOf( s, fromIndex );
	}

	//todo: rename length?
	public int getLength() {
		return this.sb.length();
	}

	public void replace( int start, int end, String s ) {
		this.sb.replace( start, end, s );
		this.updateSGText();
	}

	public void setCharAt( int index, Character c ) {
		this.sb.setCharAt( index, c );
		this.updateSGText();
	}

	@Override
	public void setSize( edu.cmu.cs.dennisc.math.Dimension3 size ) {
		this.setScale( getScaleForSize( size ) );
	}

	private final org.lgna.story.STextModel abstraction;
	private final edu.cmu.cs.dennisc.scenegraph.Text sgText = new edu.cmu.cs.dennisc.scenegraph.Text();
	private StringBuffer sb = new StringBuffer();
}
