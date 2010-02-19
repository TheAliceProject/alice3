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
package edu.cmu.cs.dennisc.croquet.swing;

/**
 * @author Dennis Cosgrove
 */
public abstract class GridPane extends Pane {
	protected GridPane( boolean isRowMajor, int hgap, int vgap, java.awt.Component[][] componentArrays ) {
		int rows;
		int cols;
		if( isRowMajor ) {
			rows = componentArrays.length;
			if( rows > 0 ) {
				cols = componentArrays[ 0 ].length;
			} else {
				cols = 0;
			}
		} else {
			cols = componentArrays.length;
			if( cols > 0 ) {
				rows = componentArrays[ 0 ].length;
			} else {
				rows = 0;
			}
		}
		setLayout( new java.awt.GridLayout( rows, cols, hgap, vgap ) );
		if( isRowMajor ) {
			for( java.awt.Component[] componentArray : componentArrays ) {
				for( java.awt.Component component : componentArray ) {
					this.add( component );
				}
			}
		} else {
			for( int r=0; r<rows; r++ ) {
				for( int c=0; c<cols; c++ ) {
					this.add( componentArrays[ c ][ r ] );
				}
			}
		}
	}
}
