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

package org.alice.stageide.program;

/**
 * @author Dennis Cosgrove
 */
public class VideoEncodingProgramContext extends ProgramContext {
	private static final boolean IS_CAPTURE_READY_FOR_PRIME_TIME = false;
	private static final java.awt.Dimension SIZE = new java.awt.Dimension( 640, 360 );

	private static edu.cmu.cs.dennisc.render.OnscreenRenderTarget<?> createOnscreenRenderTarget() {
		edu.cmu.cs.dennisc.render.RenderCapabilities requestedCapabilities = new edu.cmu.cs.dennisc.render.RenderCapabilities.Builder().build();
		return IS_CAPTURE_READY_FOR_PRIME_TIME ? new edu.cmu.cs.dennisc.render.gl.GlrCaptureFauxOnscreenRenderTarget( SIZE, null, requestedCapabilities ) : edu.cmu.cs.dennisc.render.gl.GlrRenderFactory.getInstance().createHeavyweightOnscreenRenderTarget( requestedCapabilities );
	}

	public static class FrameBasedProgramImp extends org.lgna.story.implementation.ProgramImp {
		private edu.cmu.cs.dennisc.animation.FrameBasedAnimator animator = new edu.cmu.cs.dennisc.animation.FrameBasedAnimator();

		public FrameBasedProgramImp( org.lgna.story.SProgram abstraction ) {
			super( abstraction, createOnscreenRenderTarget() );
		}

		@Override
		public edu.cmu.cs.dennisc.animation.FrameBasedAnimator getAnimator() {
			return this.animator;
		}

		public void setAnimator( edu.cmu.cs.dennisc.animation.FrameBasedAnimator animator ) {
			this.animator = animator;
		}

		private boolean isAnimating;

		class AnimatorThread extends Thread {
			@Override
			public void run() {
				while( isAnimating ) {
					animator.update();
					//edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 1 );
				}
			}
		}

		@Override
		public void startAnimator() {
			if( IS_CAPTURE_READY_FOR_PRIME_TIME ) {
				this.isAnimating = true;
				new AnimatorThread().start();
			} else {
				super.startAnimator();
			}
		}

		@Override
		public void stopAnimator() {
			if( IS_CAPTURE_READY_FOR_PRIME_TIME ) {
				this.isAnimating = false;
			} else {
				super.stopAnimator();
			}
		}
	}

	public VideoEncodingProgramContext( org.lgna.project.ast.NamedUserType programType, double frameRate ) {
		super( programType );
		this.getProgramImp().getAnimator().setFramesPerSecond( frameRate );
	}

	public VideoEncodingProgramContext( double frameRate ) {
		this( getUpToDateProgramTypeFromActiveIde(), frameRate );
	}

	@Override
	public FrameBasedProgramImp getProgramImp() {
		return (FrameBasedProgramImp)super.getProgramImp();
	}

	@Override
	protected org.lgna.project.virtualmachine.UserInstance createProgramInstance( org.lgna.project.ast.NamedUserType programType ) {
		org.lgna.story.implementation.ProgramImp.ACCEPTABLE_HACK_FOR_NOW_setClassForNextInstance( FrameBasedProgramImp.class );
		return super.createProgramInstance( programType );
	}

	//todo: add String[] args?
	public void initializeInContainer( java.awt.Container container ) {
		java.awt.Component awtComponent = this.getProgramImp().getOnscreenRenderTarget().getAwtComponent();
		awtComponent.setSize( SIZE );
		container.add( awtComponent );
	}
}
