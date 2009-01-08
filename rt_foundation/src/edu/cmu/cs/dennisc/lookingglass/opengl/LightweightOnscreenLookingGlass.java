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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
class LightweightOnscreenLookingGlass extends OnscreenLookingGlass implements edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass{
	private javax.media.opengl.GLJPanel m_glPanel = new javax.media.opengl.GLJPanel() {
		@Override
		public void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );
//			try {
//				super.paintComponent( g );
//			} catch( Exception e ) {
//				e.printStackTrace();
//				System.exit( 0 );
//			}
			
			
			//todo:
			
			
//			try {
//				LightweightLookingGlass.this.firePainted( (java.awt.Graphics2D)g );
//			} catch( Exception e ) {
//				e.printStackTrace();
//			}
			
			
			
		}
	};
	
	/*package-private*/ LightweightOnscreenLookingGlass( LookingGlassFactory lookingGlassFactory ) {
		super( lookingGlassFactory );
	}
	
	public javax.swing.JPanel getJPanel() {
		return m_glPanel;
	}
	public java.awt.Component getAWTComponent() {
		return getJPanel();
	}
	public java.awt.Dimension getSize( java.awt.Dimension rv ) {
		return getJPanel().getSize( rv );
	}
	public void repaint() {
		getJPanel().repaint();
	}
	
	@Override
	protected javax.media.opengl.GLAutoDrawable getGLAutoDrawable() {
		return m_glPanel;
	}
}
