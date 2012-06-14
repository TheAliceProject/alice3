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
package org.alice.media;

import java.io.File;
import java.io.IOException;

import org.alice.media.components.ImageRecordView;
import org.alice.media.encoder.ImagesToMOVEncoder;
import org.alice.media.encoder.ImagesToQuickTimeEncoder;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.BoundedIntegerState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.WizardPageComposite;
import org.lgna.project.ast.UserField;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.Scene;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.matt.EventManager;
import edu.cmu.cs.dennisc.matt.EventScript;
import edu.cmu.cs.dennisc.matt.FrameBasedAnimatorWithEventScript;
import edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation;

/**
 * @author Matt May
 */
public class ImageRecordComposite extends WizardPageComposite<ImageRecordView> {
	
	private final ExportToYouTubeWizardDialogComposite owner;
	private org.alice.stageide.program.VideoEncodingProgramContext programContext;
	private boolean isRecording;
	private ImagesToQuickTimeEncoder encoder;
	private Status errorIsRecording = createErrorStatus( this.createKey( "errorIsRecording" ) );
	private Status errorHasNotYetRecorded = createErrorStatus( this.createKey( "errorNothingIsRecorded" ) );
	
	private final ValueListener<Boolean> isRecordingListener = new ValueListener<Boolean>() {
		public void changing( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			toggleRecording();
		}
	};
	private final BooleanState isRecordingState = this.createBooleanState( this.createKey( "isRecordingState" ), false );

	private final BoundedIntegerState frameRate = this.createBoundedIntegerState( this.createKey( "frameRate" ), new BoundedIntegerDetails().minimum( 0 ).maximum( 96 ).initialValue( 24 ) );

	public ImageRecordComposite( ExportToYouTubeWizardDialogComposite owner ) {
		super( java.util.UUID.fromString( "67306c85-667c-46e5-9898-2c19a2d6cd21" ) );
		this.owner = owner;
		this.isRecordingState.setIconForBothTrueAndFalse( new IsRecordingIcon() );
	}

	@Override
	protected ImageRecordView createView() {
		return new ImageRecordView( this );
	}

	private java.awt.image.BufferedImage image;
	private int imageCount;
	private final edu.cmu.cs.dennisc.animation.FrameObserver frameListener = new edu.cmu.cs.dennisc.animation.FrameObserver() {
		public void update( double tCurrent ) {
			edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lookingGlass = programContext.getProgramImp().getOnscreenLookingGlass();
			if( lookingGlass.getWidth() > 0 && lookingGlass.getHeight() > 0 ) {
				if( image != null ) {
					//pass
				} else {
					image = lookingGlass.createBufferedImageForUseAsColorBuffer();
				}
				if( image != null ) {
					image = lookingGlass.getColorBuffer( image );
					handleImage( image, imageCount );
					imageCount++;
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "image is null" );
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "width:", lookingGlass.getWidth(), "height:", lookingGlass.getHeight() );
			}
		}
		public void complete() {
		}
	};

	public boolean isRecording() {
		return this.isRecording;
	}
	public void setRecording( boolean isRecording ) {
		if( this.isRecording != isRecording ) {
			if( this.isRecording ) {
				programContext.getProgramImp().stopAnimator();
				MediaPlayerAnimation.EPIC_HACK_setAnimationObserver( null );
			}
			this.isRecording = isRecording;
			if( this.isRecording ) {
				programContext.getProgramImp().startAnimator();
				programContext.getProgramImp().getAnimator().addFrameObserver( frameListener );
				encoder = new ImagesToQuickTimeEncoder( frameRate.getValue() );
				MediaPlayerAnimation.EPIC_HACK_setAnimationObserver( this.encoder );
				encoder.start();
				if( true ) {
					try {
						encoder.setOutput( File.createTempFile( "temp", ".mov" ) );
					} catch( IOException e ) {
						e.printStackTrace();
					}
				} else {
					encoder.setOutput( new File( FileUtilities.getDefaultDirectory(), "test.mov" ) );
				}
			} else {
				encoder.stop();
				programContext.getProgramImp().getAnimator().removeFrameObserver( frameListener );
			}
		}
	}

	private void toggleRecording() {
		this.setRecording( !this.isRecording() );
	}

	public BoundedIntegerState getFrameRate() {
		return frameRate;
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		final org.lgna.project.ast.NamedUserType programType = this.owner.getProject().getProgramType();
		final ImageRecordView recordView = this.getView();
		new Thread() {
			@Override
			public void run() {
				super.run();
				org.lgna.croquet.components.BorderPanel lookingGlassContainer = recordView.getLookingGlassContainer();
				image = null;
				imageCount = 0;

				programContext = new org.alice.stageide.program.VideoEncodingProgramContext( programType, frameRate.getValue() );
				programContext.initializeInContainer( lookingGlassContainer.getAwtComponent() );

				getView().revalidateAndRepaint();
				
				EventScript script = owner.getScript();

				UserInstance programInstance = programContext.getProgramInstance();
				UserField sceneField = programInstance.getType().fields.get( 0 );
				Scene scene = programContext.getProgramInstance().getFieldValueInstanceInJava( sceneField, Scene.class );
				SceneImp sceneImp = ImplementationAccessor.getImplementation( scene );
				EventManager manager = sceneImp.getEventManager();
				programContext.getProgramImp().setAnimator( new FrameBasedAnimatorWithEventScript( script, manager ) );
				programContext.setActiveScene();
			}
		}.start();
		
		this.isRecordingState.addValueListener( this.isRecordingListener );
	}

	@Override
	public void handlePostDeactivation() {
		this.isRecordingState.removeValueListener( this.isRecordingListener );
		programContext.getProgramImp().getAnimator().removeFrameObserver( this.frameListener );
		this.setRecording( false );
		programContext.cleanUpProgram();
		if( encoder != null && encoder.getOutputFile() != null ) {
			owner.setFile( encoder.getOutputFile() );
		}
		super.handlePostDeactivation();
	}

	private void handleImage( java.awt.image.BufferedImage image, int imageCount ) {
		if( image != null ) {
			encoder.addBufferedImage( image );
		}
	}

	@Override
	public Status getPageStatus( org.lgna.croquet.history.CompletionStep<?> step ) {
		if(isRecording) {
			System.out.println("isRecording: " + errorIsRecording.getText());
			return errorIsRecording;
		} else if (encoder == null || encoder.getOutputFile() == null){
			System.out.println( "no file found: " + errorHasNotYetRecorded.getText());
			return errorHasNotYetRecorded;
		}
		return IS_GOOD_TO_GO_STATUS;
	}

	public BooleanState getIsRecordingState() {
		return this.isRecordingState;
	}
}
