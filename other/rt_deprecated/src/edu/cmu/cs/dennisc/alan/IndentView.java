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

package edu.cmu.cs.dennisc.alan;

/**
 * @author Dennis Cosgrove
 */
public class IndentView extends View {
	private float m_indent;
	public IndentView( float indent ) {
		m_indent = indent;
	}
	
	public float getIndent() {
		return m_indent;
	}
	public void setIndent( float indent ) {
		m_indent = indent;
		setLayoutRequired( true );
	}

	@Override
	protected void calculateLocalBounds( java.awt.Graphics g ) {
	}
	
	@Override
	protected void paintForeground( java.awt.Graphics2D g2, float x, float y, float width, float height ) {
	}
}
