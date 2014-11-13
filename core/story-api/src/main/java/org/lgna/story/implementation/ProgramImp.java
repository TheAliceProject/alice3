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

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProgramImp {
	private static Object ACCEPTABLE_HACK_FOR_NOW_classForNextInstanceLock = new Object();
	private static Class<? extends ProgramImp> ACCEPTABLE_HACK_FOR_NOW_classForNextInstance;
	private static Class<?>[] ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes;
	private static Object[] ACCEPTABLE_HACK_FOR_NOW_bonusArguments;

	public static void ACCEPTABLE_HACK_FOR_NOW_setClassForNextInstance( Class<? extends ProgramImp> classForNextInstance, Class<?>[] bonusParameterTypes, Object[] bonusArguments ) {
		synchronized( ACCEPTABLE_HACK_FOR_NOW_classForNextInstanceLock ) {
			assert ACCEPTABLE_HACK_FOR_NOW_classForNextInstance == null : ACCEPTABLE_HACK_FOR_NOW_classForNextInstance;
			assert ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes == null : ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes;
			assert ACCEPTABLE_HACK_FOR_NOW_bonusArguments == null : ACCEPTABLE_HACK_FOR_NOW_bonusArguments;
			ACCEPTABLE_HACK_FOR_NOW_classForNextInstance = classForNextInstance;
			ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes = bonusParameterTypes;
			ACCEPTABLE_HACK_FOR_NOW_bonusArguments = bonusArguments;
		}
	}

	public static void ACCEPTABLE_HACK_FOR_NOW_setClassForNextInstance( Class<? extends ProgramImp> classForNextInstance ) {
		ACCEPTABLE_HACK_FOR_NOW_setClassForNextInstance( classForNextInstance, new Class<?>[] {}, new Object[] {} );
	}

	public static ProgramImp createInstance( org.lgna.story.SProgram abstraction ) {
		ProgramImp rv;
		synchronized( ACCEPTABLE_HACK_FOR_NOW_classForNextInstanceLock ) {
			if( ACCEPTABLE_HACK_FOR_NOW_classForNextInstance != null ) {

				Class<?>[] parameterTypes = new Class<?>[ ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes.length + 1 ];
				parameterTypes[ 0 ] = org.lgna.story.SProgram.class;
				System.arraycopy( ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes, 0, parameterTypes, 1, ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes.length );

				Object[] arguments = new Object[ ACCEPTABLE_HACK_FOR_NOW_bonusArguments.length + 1 ];
				arguments[ 0 ] = abstraction;
				System.arraycopy( ACCEPTABLE_HACK_FOR_NOW_bonusArguments, 0, arguments, 1, ACCEPTABLE_HACK_FOR_NOW_bonusArguments.length );

				java.lang.reflect.Constructor<? extends ProgramImp> cnstrctr = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( ACCEPTABLE_HACK_FOR_NOW_classForNextInstance, parameterTypes );
				assert cnstrctr != null : ACCEPTABLE_HACK_FOR_NOW_classForNextInstance;
				rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, arguments );
				ACCEPTABLE_HACK_FOR_NOW_classForNextInstance = null;
				ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes = null;
				ACCEPTABLE_HACK_FOR_NOW_bonusArguments = null;
			} else {
				rv = new DefaultProgramImp( abstraction );
			}
		}
		return rv;
	}

	private static class ToggleFullScreenAction extends javax.swing.AbstractAction {
		private java.awt.Rectangle prevNormalBounds;

		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)e.getSource();
			javax.swing.ButtonModel buttonModel = button.getModel();
			java.awt.Component root = javax.swing.SwingUtilities.getRoot( button );
			if( root != null ) {
				java.awt.Rectangle bounds;
				if( buttonModel.isSelected() ) {
					this.prevNormalBounds = root.getBounds();
					bounds = root.getGraphicsConfiguration().getBounds();
				} else {
					bounds = this.prevNormalBounds;
					this.prevNormalBounds = null;
				}
				if( bounds != null ) {
					root.setBounds( bounds );
				}
			}
		}
	};

	private static final class FullScreenIcon implements javax.swing.Icon {
		@Override
		public int getIconWidth() {
			return 24;
		}

		@Override
		public int getIconHeight() {
			return 16;
		}

		private static void paintRect( java.awt.Graphics2D g2, java.awt.Paint fillPaint, java.awt.Paint drawPaint, int x, int y, int width, int height ) {
			g2.setPaint( fillPaint );
			g2.fillRect( x, y, width, height );
			g2.setPaint( drawPaint );
			g2.drawRect( x, y, width, height );
		}

		@Override
		public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
			javax.swing.AbstractButton b = (javax.swing.AbstractButton)c;
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			javax.swing.ButtonModel buttonModel = b.getModel();
			java.awt.Paint fillPaint;
			if( buttonModel.isRollover() ) {
				fillPaint = java.awt.Color.WHITE;
			} else {
				fillPaint = java.awt.Color.LIGHT_GRAY;
			}
			java.awt.Paint drawPaint = java.awt.Color.DARK_GRAY;

			int W = this.getIconWidth();
			int H = this.getIconHeight();

			int w = 8;
			int h = 6;

			g2.translate( x, y );
			paintRect( g2, fillPaint, drawPaint, 0, 0, w, h );
			paintRect( g2, fillPaint, drawPaint, 0, H - h, w, h );
			paintRect( g2, fillPaint, drawPaint, W - w, H - h, w, h );
			paintRect( g2, fillPaint, drawPaint, W - w, 0, w, h );

			paintRect( g2, java.awt.Color.GRAY, drawPaint, 4, 3, W - 8, H - 6 );
			g2.translate( -x, -y );
		}
	}

	protected ProgramImp( org.lgna.story.SProgram abstraction, edu.cmu.cs.dennisc.render.OnscreenRenderTarget<?> onscreenRenderTarget ) {
		this.abstraction = abstraction;
		this.onscreenRenderTarget = onscreenRenderTarget;
		this.toggleFullScreenAction.putValue( javax.swing.Action.SMALL_ICON, new FullScreenIcon() );
	}

	protected void handleSpeedChange( double speedFactor ) {
		this.getAnimator().setSpeedFactor( speedFactor );
	}

	public javax.swing.Action getRestartAction() {
		return this.restartAction;
	}

	public void setRestartAction( javax.swing.Action restartAction ) {
		this.restartAction = restartAction;
	}

	public javax.swing.Action getToggleFullScreenAction() {
		return this.toggleFullScreenAction;
	}

	public java.awt.Rectangle getNormalDialogBounds( java.awt.Component awtComponent ) {
		if( this.toggleFullScreenAction.prevNormalBounds != null ) {
			return this.toggleFullScreenAction.prevNormalBounds;
		} else {
			return awtComponent.getBounds();
		}
	}

	private boolean isControlPanelDesired = true;

	public boolean isControlPanelDesired() {
		return this.isControlPanelDesired;
	}

	public void setControlPanelDesired( boolean isControlPanelDesired ) {
		this.isControlPanelDesired = isControlPanelDesired;
	}

	public org.lgna.story.SProgram getAbstraction() {
		return this.abstraction;
	}

	public edu.cmu.cs.dennisc.render.OnscreenRenderTarget<?> getOnscreenRenderTarget() {
		return this.onscreenRenderTarget;
	}

	public abstract edu.cmu.cs.dennisc.animation.Animator getAnimator();

	public double getSimulationSpeedFactor() {
		return this.simulationSpeedFactor;
	}

	public void setSimulationSpeedFactor( double simulationSpeedFactor ) {
		this.simulationSpeedFactor = simulationSpeedFactor;
	}

	private edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener automaticDisplayListener = new edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener() {
		@Override
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.render.event.AutomaticDisplayEvent e ) {
			ProgramImp.this.getAnimator().update();
		}
	};

	public void startAnimator() {
		edu.cmu.cs.dennisc.render.RenderFactory renderFactory = this.getOnscreenRenderTarget().getRenderFactory();
		renderFactory.addAutomaticDisplayListener( this.automaticDisplayListener );
		renderFactory.incrementAutomaticDisplayCount();
		this.isAnimatorStarted = true;
	}

	public void stopAnimator() {
		if( this.isAnimatorStarted ) {
			this.getAnimator().completeAll( null );
			edu.cmu.cs.dennisc.render.RenderFactory renderFactory = this.getOnscreenRenderTarget().getRenderFactory();
			renderFactory.decrementAutomaticDisplayCount();
			renderFactory.removeAutomaticDisplayListener( this.automaticDisplayListener );
			this.isAnimatorStarted = false;
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.isAnimatorStarted );
		}
	}

	private void addComponents( AwtContainerInitializer awtContainerInitializer ) {
		java.awt.Component awtLgComponent = this.getOnscreenRenderTarget().getAwtComponent();
		synchronized( awtLgComponent.getTreeLock() ) {
			javax.swing.JPanel controlPanel;
			if( this.isControlPanelDesired() ) {
				controlPanel = new ProgramControlPanel( this );
			} else {
				controlPanel = null;
			}
			awtContainerInitializer.addComponents( onscreenRenderTarget, controlPanel );
		}
	}

	private void requestFocusInWindow() {
		this.getOnscreenRenderTarget().getAwtComponent().requestFocusInWindow();
	}

	public static interface AwtContainerInitializer {
		public void addComponents( edu.cmu.cs.dennisc.render.OnscreenRenderTarget<?> onscreenRenderTarget, javax.swing.JPanel controlPanel );
	}

	private static class DefaultAwtContainerInitializer implements AwtContainerInitializer {
		private final java.awt.Container awtContainer;

		public DefaultAwtContainerInitializer( java.awt.Container awtContainer ) {
			this.awtContainer = awtContainer;
		}

		@Override
		public void addComponents( edu.cmu.cs.dennisc.render.OnscreenRenderTarget<?> onscreenRenderTarget, javax.swing.JPanel controlPanel ) {
			this.awtContainer.add( onscreenRenderTarget.getAwtComponent() );
			if( controlPanel != null ) {
				this.awtContainer.add( controlPanel, java.awt.BorderLayout.PAGE_START );
			}
			if( this.awtContainer instanceof javax.swing.JComponent ) {
				( (javax.swing.JComponent)this.awtContainer ).revalidate();
			}
		}
	}

	public void initializeInAwtContainer( AwtContainerInitializer awtContainerInitializer ) {
		this.addComponents( awtContainerInitializer );
		this.startAnimator();
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				requestFocusInWindow();
			}
		} );
	}

	public void initializeInAwtContainer( java.awt.Container awtContainer ) {
		this.initializeInAwtContainer( new DefaultAwtContainerInitializer( awtContainer ) );
	}

	public void initializeInFrame( final javax.swing.JFrame frame, final Runnable runnable ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				ProgramImp.this.addComponents( new DefaultAwtContainerInitializer( frame.getContentPane() ) );
				frame.setVisible( true );
				runnable.run();
				requestFocusInWindow();
			}
		} );
	}

	public void initializeInFrame( javax.swing.JFrame frame ) {
		final java.util.concurrent.CyclicBarrier barrier = new java.util.concurrent.CyclicBarrier( 2 );
		this.initializeInFrame( frame, new Runnable() {
			@Override
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
		this.addComponents( new DefaultAwtContainerInitializer( applet.getContentPane() ) );
		this.startAnimator();
	}

	public void shutDown() {
		this.onscreenRenderTarget.release();
		this.stopAnimator();
		this.isProgramClosedExceptionDesired = true;
	}

	/* package-private */void perform( edu.cmu.cs.dennisc.animation.Animation animation, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		if( this.isProgramClosedExceptionDesired ) {
			if( this.isAnimatorStarted ) {
				this.stopAnimator();
			}
			throw new org.lgna.common.ProgramClosedException();
		}
		this.getAnimator().invokeAndWait_ThrowRuntimeExceptionsIfNecessary( animation, animationObserver );
	}

	private final org.lgna.story.SProgram abstraction;
	private final edu.cmu.cs.dennisc.render.OnscreenRenderTarget<?> onscreenRenderTarget;
	private double simulationSpeedFactor = 1.0;
	private javax.swing.Action restartAction;
	private boolean isAnimatorStarted = false;
	private boolean isProgramClosedExceptionDesired = false;
	private final ToggleFullScreenAction toggleFullScreenAction = new ToggleFullScreenAction();
}
