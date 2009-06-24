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
package edu.cmu.cs.dennisc.lookingglass;

/**
 * @author Dennis Cosgrove
 */
public abstract class DefaultProgram extends Program {
	private edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass m_onscreenLookingGlass;
	public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		return m_onscreenLookingGlass;
	}

	protected boolean isLightweightOnscreenLookingGlassDesired() {
		return false;
	}

	protected void initializeAWT( java.awt.Container container, edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		container.setLayout( new java.awt.BorderLayout() );
		container.add( m_onscreenLookingGlass.getAWTComponent(), java.awt.BorderLayout.CENTER );
	}
	protected void exitAWT( java.awt.Container container, edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		container.remove( m_onscreenLookingGlass.getAWTComponent() );
	}
	
	@Override
	protected void preInitialize() {
		super.preInitialize();
		if( isLightweightOnscreenLookingGlassDesired() ) {
			m_onscreenLookingGlass = getLookingGlassFactory().createLightweightOnscreenLookingGlass();
		} else {
			m_onscreenLookingGlass = getLookingGlassFactory().createHeavyweightOnscreenLookingGlass();
		}
		initializeAWT( getContentPane(), m_onscreenLookingGlass );
	}
	
	@Override
	protected boolean handleWindowClosing( java.awt.event.WindowEvent e ) {
		if( super.handleWindowClosing( e ) ) {
			exitAWT( getContentPane(), m_onscreenLookingGlass );
			m_onscreenLookingGlass.release();
			return true;
		} else {
			return false;
		}
	}
}
