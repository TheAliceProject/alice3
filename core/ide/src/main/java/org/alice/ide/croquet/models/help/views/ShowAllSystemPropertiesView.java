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
package org.alice.ide.croquet.models.help.views;

/**
 * @author Dennis Cosgrove
 */
public class ShowAllSystemPropertiesView extends org.lgna.croquet.views.BorderPanel {
	public ShowAllSystemPropertiesView( org.alice.ide.croquet.models.help.ShowAllSystemPropertiesComposite composite ) {
		super( composite );
		java.util.Properties properties = System.getProperties();
		java.util.Enumeration<String> nameEnum = (java.util.Enumeration<String>)properties.propertyNames();
		java.util.SortedSet<String> names = new java.util.TreeSet<String>();
		int max = 0;
		while( nameEnum.hasMoreElements() ) {
			String name = nameEnum.nextElement();
			names.add( name );
			max = Math.max( max, name.length() );
		}
		String formatString = "%-" + ( max + 1 ) + "s";
		StringBuffer sb = new StringBuffer();
		for( String name : names ) {
			sb.append( String.format( formatString, name ) );
			sb.append( ": " );
			sb.append( System.getProperty( name ) );
			sb.append( "\n" );
		}

		javax.swing.JTextArea textArea = new javax.swing.JTextArea( sb.toString() );
		textArea.setEditable( false );

		final boolean IS_SET_FONT_TO_DERIVED_FONT_WORKING = false;
		if( IS_SET_FONT_TO_DERIVED_FONT_WORKING ) {
			edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( textArea, edu.cmu.cs.dennisc.java.awt.font.TextFamily.MONOSPACED );
		} else {
			java.awt.Font font = textArea.getFont();
			textArea.setFont( new java.awt.Font( "Monospaced", font.getStyle(), font.getSize() ) );
		}

		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( textArea );

		this.getAwtComponent().add( scrollPane );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );

		this.setPreferredSize( edu.cmu.cs.dennisc.java.awt.DimensionUtilities.createWiderGoldenRatioSizeFromWidth( 640 ) );
	}
}
