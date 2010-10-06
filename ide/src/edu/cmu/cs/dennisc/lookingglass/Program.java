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
package edu.cmu.cs.dennisc.lookingglass;

/**
 * @author Dennis Cosgrove
 */
public abstract class Program extends edu.cmu.cs.dennisc.program.Program {
	private static LookingGlassFactory s_lookingGlassFactory;

	public edu.cmu.cs.dennisc.lookingglass.LookingGlassFactory getLookingGlassFactory() {
		if( s_lookingGlassFactory == null ) {
			s_lookingGlassFactory = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton();
		}
		return s_lookingGlassFactory;
	}
	private boolean m_isReleaseRenderingLockNecessary = false;

	private void acquireRenderingLock() {
		s_lookingGlassFactory.acquireRenderingLock();
		m_isReleaseRenderingLockNecessary = true;
	}
	private void releaseRenderingLock() {
		if( m_isReleaseRenderingLockNecessary ) {
			s_lookingGlassFactory.releaseRenderingLock();
		}
	}
	
	public void invokeLater( Runnable runnable ) {
		s_lookingGlassFactory.invokeLater( runnable );
	}
	public void invokeAndWait( Runnable runnable ) throws InterruptedException, java.lang.reflect.InvocationTargetException {
		s_lookingGlassFactory.invokeAndWait( runnable );
	}
	public void invokeAndWait_ThrowRuntimeExceptionsIfNecessary( Runnable runnable ) {
		s_lookingGlassFactory.invokeAndWait_ThrowRuntimeExceptionsIfNecessary( runnable );
	}
	
	@Override
	protected void preInitialize() {
		getLookingGlassFactory();
		acquireRenderingLock();
	}

	@Override
	protected void postInitialize( boolean success ) {
		releaseRenderingLock();
		if( success ) {
			//todo: investigate
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					java.awt.Container contentPane = getContentPane();
//					if( contentPane instanceof javax.swing.JComponent ) {
//						((javax.swing.JComponent)contentPane).revalidate();
//					} else {
						contentPane.invalidate();
						contentPane.doLayout();
//					}
				}
			} );
			s_lookingGlassFactory.incrementAutomaticDisplayCount();
			s_lookingGlassFactory.invokeAndWait_ThrowRuntimeExceptionsIfNecessary( new Runnable() {
				public void run() {
					//pass
				}
			} );
		}
	}

	
	@Override
	protected void preRun() {
	}
	@Override
	protected void postRun() {
	}
	
//	@Override
//	protected void handleShownForTheFirstTime() {
//	}
	@Override
	protected boolean handleWindowClosing( java.awt.event.WindowEvent e ) {
		s_lookingGlassFactory.decrementAutomaticDisplayCount();
		return true;
	}
	@Override
	protected void handleWindowClosed( java.awt.event.WindowEvent e ) {
	}
}
