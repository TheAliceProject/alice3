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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
abstract class IngredientListCellRenderer< E > extends swing.ListCellRenderer< E > {
	private javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 2 );
	protected abstract String getSubPath();
	private String getIngredientPath( org.alice.apis.stage.SkinTone skinTone, String clsName, String enumConstantName ) {
		java.io.File ROOT = org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory();
		String rv = ROOT.getAbsolutePath();
		rv += "/personbuilder/";
		rv += this.getSubPath();
		rv += "/";
		rv += skinTone;
		rv += "/";
		rv += clsName;
		rv += ".";
		rv += enumConstantName;
		rv += ".png";
		return rv;
	}

	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, E value, int index, boolean isSelected, boolean cellHasFocus ) {
		if( value != null ) {
			String clsName = value.getClass().getSimpleName();
			String enumConstantName = value.toString();
			String pathForIcon = this.getIngredientPath( org.alice.apis.stage.BaseSkinTone.DARK, clsName, enumConstantName );
			rv.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
			rv.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM );

			rv.setText( "" );
			rv.setIcon( new javax.swing.ImageIcon( pathForIcon ) );
			rv.setBorder( this.border );
		} else {
			rv.setText( "null" );
		}
		return rv;
	}
}
