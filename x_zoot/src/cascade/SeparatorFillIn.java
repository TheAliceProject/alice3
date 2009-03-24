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
package cascade;

/**
 * @author Dennis Cosgrove
 */
public class SeparatorFillIn extends FillIn< Object > {
	private edu.cmu.cs.dennisc.moot.ZLineAxisPane pane;
	public SeparatorFillIn() {
		this( null );
	}
	public SeparatorFillIn( String text ) {
		if( text != null ) {
			javax.swing.JLabel label = new javax.swing.JLabel( text );
			label.setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
			this.pane = new edu.cmu.cs.dennisc.moot.ZLineAxisPane();
			this.pane.add( label );
		} else {
			this.pane = null;
		}
	}
	
	@Override
	protected javax.swing.JComponent getMenuItem() {
		return this.pane;
	}
	@Override
	protected void addChildren() {
	}
	@Override
	public Object getValue() {
		return null;
	}
}
