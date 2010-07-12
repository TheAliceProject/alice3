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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
abstract class IngredientListCellRenderer< E > extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< E > {
	private javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 2 );
	protected abstract String getSubPath();
	private java.net.URL getIngredientResourceName( org.alice.apis.stage.SkinTone skinTone, String clsName, String enumConstantName ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "images/" );
		sb.append( this.getSubPath() );
		sb.append( "/" );
		sb.append( skinTone );
		sb.append( "/" );
		sb.append( clsName );
		sb.append( "." );
		sb.append( enumConstantName );
		sb.append( ".png" );
		java.net.URL rv = IngredientListCellRenderer.class.getResource( sb.toString() );
		assert rv != null : sb;
		return rv;
	}

	private org.alice.apis.stage.BaseSkinTone getBaseSkinTone() {
		org.alice.apis.stage.Person person = PersonViewer.getSingleton().getPerson();
		if( person != null ) {
			org.alice.apis.stage.SkinTone skinTone = person.getSkinTone();
			if( skinTone instanceof org.alice.apis.stage.BaseSkinTone ) {
				return (org.alice.apis.stage.BaseSkinTone)skinTone;
			}
		}
		return org.alice.apis.stage.BaseSkinTone.DARK;
	}

	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, E value, int index, boolean isSelected, boolean cellHasFocus ) {
		assert rv != null;
		if( value != null ) {
			String clsName = value.getClass().getSimpleName();
			String enumConstantName = value.toString();
			
			org.alice.apis.stage.SkinTone baseSkinTone = this.getBaseSkinTone();

			java.net.URL urlForIcon = this.getIngredientResourceName( baseSkinTone, clsName, enumConstantName );
			rv.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
			rv.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM );

			rv.setOpaque( isSelected );
			if( isSelected ) {
				rv.setBackground( PersonEditor.SELECTED_COLOR );
			}
			
			javax.swing.Icon icon = new javax.swing.ImageIcon( urlForIcon );
			if( icon != null ) {
				rv.setIcon( icon );
				rv.setText( "" );
			} else {
				rv.setText( "image not found" );
			}
			rv.setBorder( this.border );
		} else {
			rv.setText( "null" );
		}
		return rv;
	}
}
