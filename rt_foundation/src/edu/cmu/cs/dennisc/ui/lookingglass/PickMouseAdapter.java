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
package edu.cmu.cs.dennisc.ui.lookingglass;

/**
 * @author Dennis Cosgrove
 */
public abstract class PickMouseAdapter implements java.awt.event.MouseListener {
	private edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass m_onscreenLookingGlass;

	public PickMouseAdapter() {
		this( null );
	}
	public PickMouseAdapter( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		setOnscreenLookingGlass( onscreenLookingGlass );
	}

	protected abstract void handlePickResult( edu.cmu.cs.dennisc.lookingglass.PickResult pickResult );
	
	public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		return m_onscreenLookingGlass;
	}
	public void setOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		m_onscreenLookingGlass = onscreenLookingGlass;
	}

	public void mousePressed( java.awt.event.MouseEvent e ) {
		assert m_onscreenLookingGlass != null;
		handlePickResult( m_onscreenLookingGlass.pickFrontMost( e.getX(), e.getY(), /*isSubElementRequired=*/false ) );
	}
	public void mouseReleased( java.awt.event.MouseEvent e ) {
	}
	public void mouseClicked( java.awt.event.MouseEvent arg0 ) {
	}
	public void mouseEntered( java.awt.event.MouseEvent e ) {
	}
	public void mouseExited( java.awt.event.MouseEvent e ) {
	}
}
