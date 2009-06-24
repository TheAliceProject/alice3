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
package edu.cmu.cs.dennisc.alan.adapters.swing;

/**
 * @author Dennis Cosgrove
 */
public class Icon implements javax.swing.Icon {
	private edu.cmu.cs.dennisc.alan.CompositeView m_compositeView = null;
	public Icon() {
	}
	public Icon( edu.cmu.cs.dennisc.alan.CompositeView compositeView ) {
		setCompositeView( compositeView );
	}
	public edu.cmu.cs.dennisc.alan.CompositeView getCompositeView() {
		return m_compositeView;
	}
	public void setCompositeView( edu.cmu.cs.dennisc.alan.CompositeView compositeView ) {
		m_compositeView = compositeView;
	}
	public int getIconWidth() {
		return (int)m_compositeView.getWidth();
	}
	public int getIconHeight() {
		return (int)m_compositeView.getHeight();
	}
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		m_compositeView.paintFromOrigin( g2, 0, 0 );
	}

}
