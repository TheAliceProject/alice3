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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
class HeavyweightOnscreenLookingGlass extends OnscreenLookingGlass implements edu.cmu.cs.dennisc.lookingglass.HeavyweightOnscreenLookingGlass {
	private javax.media.opengl.GLCanvas m_glCanvas;

	/* package-private */HeavyweightOnscreenLookingGlass( LookingGlassFactory lookingGlassFactory ) {
		super( lookingGlassFactory );
		m_glCanvas = LookingGlassFactory.getInstance().createGLCanvas();
		//m_glCanvas.getChosenGLCapabilities().getDepthBits();
		//m_glCanvas.setAutoSwapBufferMode( false );
		m_glCanvas.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}

			public void componentHidden( java.awt.event.ComponentEvent e ) {
			}

			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}

			public void componentResized( java.awt.event.ComponentEvent e ) {
				m_glCanvas.setMinimumSize( new java.awt.Dimension( 0, 0 ) );
				m_glCanvas.repaint();
			}
		} );
	}

	public void repaint() {
		getAWTComponent().repaint();
	}

	public java.awt.Component getAWTComponent() {
		return m_glCanvas;
	}

	public java.awt.Dimension getSize( java.awt.Dimension rv ) {
		return getAWTComponent().getSize( rv );
	}

	@Override
	protected javax.media.opengl.GLAutoDrawable getGLAutoDrawable() {
		return m_glCanvas;
	}
}
