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

package org.lookingglassandalice.storytelling.implementation;

/**
 * @author Dennis Cosgrove
 */
public class ProgramImplementation {
	private final org.lookingglassandalice.storytelling.Program abstraction;
	private final edu.cmu.cs.dennisc.animation.Animator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();
	private final edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createHeavyweightOnscreenLookingGlass();
	
	private double simulationSpeedFactor = 1.0; 
	public ProgramImplementation( org.lookingglassandalice.storytelling.Program abstraction ) {
		this.abstraction = abstraction;
	}

	public org.lookingglassandalice.storytelling.Program getAbstraction() {
		return this.abstraction;
	}
	public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		return this.onscreenLookingGlass;
	}
	
	public double getSimulationSpeedFactor() {
		return this.simulationSpeedFactor;
	}
	public void setSimulationSpeedFactor( double simulationSpeedFactor ) {
		this.simulationSpeedFactor = simulationSpeedFactor;
	}
	
	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			ProgramImplementation.this.animator.update();
		}
	};
	private void startAnimator() {
		this.onscreenLookingGlass.getLookingGlassFactory().addAutomaticDisplayListener( this.automaticDisplayListener );
		this.onscreenLookingGlass.getLookingGlassFactory().incrementAutomaticDisplayCount();
	}
	private void stopAnimator() {
		this.onscreenLookingGlass.getLookingGlassFactory().decrementAutomaticDisplayCount();
		this.onscreenLookingGlass.getLookingGlassFactory().removeAutomaticDisplayListener( this.automaticDisplayListener );
	}
	public void initializeInFrame( final javax.swing.JFrame frame, final Runnable runnable ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				frame.getContentPane().add( ProgramImplementation.this.onscreenLookingGlass.getAWTComponent() );
				frame.setVisible( true );
				runnable.run();
			}
		} );
	}
	public void initializeInFrame( String[] args ) {
		edu.cmu.cs.dennisc.croquet.Frame frame = new edu.cmu.cs.dennisc.croquet.Frame();
		frame.setSize( 640, 480 );
		frame.setDefaultCloseOperation( edu.cmu.cs.dennisc.croquet.Frame.DefaultCloseOperation.DISPOSE );
		final java.util.concurrent.CyclicBarrier barrier = new java.util.concurrent.CyclicBarrier( 2 );
		this.initializeInFrame( frame.getAwtComponent(), new Runnable() {
			public void run() {
				try {
					barrier.await();
				} catch( InterruptedException ie ) {
					throw new RuntimeException( ie );
				} catch( java.util.concurrent.BrokenBarrierException bbe ) {
					throw new RuntimeException( bbe );
				}
			}
		} );
		try {
			barrier.await();
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		} catch( java.util.concurrent.BrokenBarrierException bbe ) {
			throw new RuntimeException( bbe );
		}
		this.startAnimator();
	}
	public void initializeInApplet( javax.swing.JApplet applet ) {
		applet.getContentPane().add( this.onscreenLookingGlass.getAWTComponent(), java.awt.BorderLayout.CENTER );
		this.startAnimator();
	}
	public void shutDown() {
		this.stopAnimator();
	}
	
	/*package-private*/ void perform( edu.cmu.cs.dennisc.animation.Animation animation, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		this.animator.invokeAndWait_ThrowRuntimeExceptionsIfNecessary( animation, animationObserver );
	}
}
