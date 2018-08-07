/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.media.youtube.croquet;

import edu.cmu.cs.dennisc.animation.FrameObserver;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.gl.GlrCaptureFauxOnscreenRenderTarget;
import org.alice.media.video.WebmRecordingAdapter;
import org.alice.media.youtube.croquet.views.ImageRecordView;
import org.alice.media.youtube.croquet.views.icons.IsRecordingIcon;
import org.alice.stageide.program.VideoEncodingProgramContext;
import org.lgna.common.RandomUtilities;
import org.lgna.croquet.AbstractComposite;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.BoundedIntegerState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.MutableDataSingleSelectListState;
import org.lgna.croquet.WizardPageComposite;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SScene;
import org.lgna.story.implementation.SceneImp;
import org.lgna.story.implementation.eventhandling.EventManager;

import edu.cmu.cs.dennisc.matt.eventscript.FrameBasedAnimatorWithEventScript;
import edu.cmu.cs.dennisc.matt.eventscript.events.EventScriptEvent;
import edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation;

import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * @author Matt May
 */
public class ImageRecordComposite extends WizardPageComposite<ImageRecordView, ExportToYouTubeWizardDialogComposite> {
	private VideoEncodingProgramContext programContext;
	private WebmRecordingAdapter encoder;
	private final BooleanState isRecordingState = this.createBooleanState( "isRecordingState", false );
	private final BoundedIntegerState frameRateState = this.createBoundedIntegerState( "frameRateState", new BoundedIntegerDetails().minimum( 0 ).maximum( 96 ).initialValue( 24 ) );
	private final Status errorIsRecording = createErrorStatus( "errorIsRecording" );
	private final Status errorHasNotYetRecorded = createErrorStatus( "errorNothingIsRecorded" );
	private BufferedImage image;
	private int imageCount;

	public ImageRecordComposite( ExportToYouTubeWizardDialogComposite owner ) {
		super( UUID.fromString( "67306c85-667c-46e5-9898-2c19a2d6cd21" ), owner );
		this.isRecordingState.setIconForBothTrueAndFalse( new IsRecordingIcon() );
		this.isRecordingState.addNewSchoolValueListener( this.isRecordingListener );
		restartOperation.setEnabled( false );
	}

	private final ValueListener<Boolean> isRecordingListener = new ValueListener<Boolean>() {
		@Override
		public void valueChanged( ValueEvent<Boolean> e ) {
			setRecording( e.getNextValue() );
		}
	};

	private final ActionOperation restartOperation = createActionOperation( "restartImageRecorder", new Action() {

		@Override
		public Edit perform( CompletionStep<?> step, AbstractComposite.InternalActionOperation source ) throws CancelException {
			isRecordingState.setValueTransactionlessly( false );
			programContext.getProgramImp().getAnimator().removeFrameObserver( frameListener );
			programContext.getProgramImp().stopAnimator();
			resetData();
			restartOperation.setEnabled( false );
			return null;
		}

	} );

	@Override
	protected ImageRecordView createView() {
		return new ImageRecordView( this );
	}

	private final FrameObserver frameListener = new FrameObserver() {
		@Override
		public void update( final double tCurrent ) {
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					getView().updateTime( tCurrent );
				}
			} );
			OnscreenRenderTarget<?> renderTarget = programContext.getProgramImp().getOnscreenRenderTarget();
			if( renderTarget instanceof GlrCaptureFauxOnscreenRenderTarget ) {
				GlrCaptureFauxOnscreenRenderTarget captureLookingGlass = (GlrCaptureFauxOnscreenRenderTarget)renderTarget;
				captureLookingGlass.captureImage( new GlrCaptureFauxOnscreenRenderTarget.Observer() {
					@Override
					public void handleImage( BufferedImage image, boolean isUpSideDown ) {
						if( image != null ) {
							if( isUpSideDown ) {
								ImageRecordComposite.this.handleImage( image, imageCount, isUpSideDown );
								imageCount++;
							}
						}
					}
				} );
			} else {
				Dimension drawableSize = renderTarget.getDrawableSize();
				if( ( drawableSize.width > 0 ) && ( drawableSize.height > 0 ) ) {
					if( image != null ) {
						//pass
					} else {
						image = renderTarget.getSynchronousImageCapturer().createBufferedImageForUseAsColorBuffer();
						//					image = new BufferedImage( lookingGlass.getWidth(), lookingGlass.getHeight(), BufferedImage.TYPE_3BYTE_BGR );
					}
					if( image != null ) {
						boolean[] atIsUpsideDown = { false };
						synchronized( image ) {
							image = renderTarget.getSynchronousImageCapturer().getColorBufferNotBotheringToFlipVertically( image, atIsUpsideDown );
							if( atIsUpsideDown[ 0 ] ) {
								handleImage( image, imageCount, atIsUpsideDown[ 0 ] );
							} else {
								System.out.println( "SEVERE: CONTENT_TYPE IS NOT UPSIDE DOWN" );
							}
						}
						imageCount++;
					} else {
						Logger.severe( "image is null" );
					}
				} else {
					Logger.severe( "width:", drawableSize.width, "height:", drawableSize.height );
				}
			}
		}

		@Override
		public void complete() {
		}
	};

	public boolean isRecording() {
		return this.isRecordingState.getValue();
	}

	public void setRecording( boolean isRecording ) {
		if( !this.isRecordingState.getValue() ) {
			this.encoder.stopAnimationsAndClear();
			programContext.getProgramImp().getAnimator().setSpeedFactor( 0 );
			MediaPlayerAnimation.EPIC_HACK_setAnimationObserver( null );
			encoder.stopVideoEncoding();
		} else {
			MediaPlayerAnimation.EPIC_HACK_setAnimationObserver( this.encoder );
			programContext.getProgramImp().getAnimator().setFramesPerSecond( this.frameRateState.getValue() );
			this.encoder.setFrameRate( frameRateState.getValue() );
			this.frameRateState.setEnabled( false );
			programContext.getProgramImp().startAnimator();
			programContext.getProgramImp().getAnimator().setSpeedFactor( 1 );
			restartOperation.setEnabled( true );
			this.encoder.startVideoEncoding();
		}
	}

	public BoundedIntegerState getFrameRateState() {
		return frameRateState;
	}

	@Override
	public void handlePostDeactivation() {
		isRecordingState.setValueTransactionlessly( false );
		if( ( encoder != null ) ) {
			this.getOwner().setTempRecordedVideoFile( this.encoder.getEncodedVideoFile() );
		}
		this.getView().disableLookingGlassContainer();
		super.handlePostDeactivation();
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		ExportToYouTubeWizardDialogComposite owner = this.getOwner();
		this.getView().setEventListPaneVisible( owner.getEventList().getData().getItemCount() > 0 );

		getEventList().clear();
		if( owner.getEventScript() != null ) {
			for( EventScriptEvent event : owner.getEventScript().getEventList() ) {
				getEventList().addItem( event );
			}
		}
		RandomUtilities.setSeed( owner.getRandomSeed() );
		this.getView().enableLookingGlassContainer();
	}

	private void handleImage( BufferedImage image, int imageCount, boolean isUpsideDown ) {
		if( image != null ) {
			encoder.addBufferedImage( image, isUpsideDown );
		}
	}

	@Override
	public Status getPageStatus( CompletionStep<?> step ) {
		if( isRecordingState.getValue() ) {
			return errorIsRecording;
		} else if( imageCount == 0 ) {
			return errorHasNotYetRecorded;
		}
		return IS_GOOD_TO_GO_STATUS;
	}

	public BooleanState getIsRecordingState() {
		return this.isRecordingState;
	}

	public MutableDataSingleSelectListState<EventScriptEvent> getEventList() {
		return getOwner().getEventList();
	}

	private void restartProgramContext() {
		ExportToYouTubeWizardDialogComposite owner = this.getOwner();
		restartOperation.setEnabled( false );
		RandomUtilities.setSeed( owner.getRandomSeed() );
		NamedUserType programType = owner.getProject().getProgramType();
		image = null;
		imageCount = 0;

		programContext = new VideoEncodingProgramContext( programType, frameRateState.getValue() );
		programContext.initializeInContainer( this.getView().getLookingGlassContainer().getAwtComponent() );
		getView().revalidateAndRepaint();

		UserInstance programInstance = programContext.getProgramInstance();
		UserField sceneField = programInstance.getType().fields.get( 0 );
		SScene scene = programContext.getProgramInstance().getFieldValueInstanceInJava( sceneField, SScene.class );
		SceneImp sceneImp = EmployeesOnly.getImplementation( scene );
		EventManager manager = sceneImp.getEventManager();

		programContext.getProgramImp().setAnimator( new FrameBasedAnimatorWithEventScript( owner.getEventScript(), manager ) );
		programContext.getProgramImp().getAnimator().addFrameObserver( frameListener );
		programContext.setActiveScene();
		getView().updateTime( 0 );
		encoder = new WebmRecordingAdapter();
		frameRateState.setEnabled( true );
		encoder.setDimension( programContext.getOnscreenRenderTarget().getDrawableSize() );
		this.encoder.initializeAudioRecording();
	}

	public ActionOperation getRestartOperation() {
		return this.restartOperation;
	}

	@Override
	public void resetData() {
		BorderPanel lookingGlassContainer = getView().getLookingGlassContainer();
		if( ( getView() != null ) && ( lookingGlassContainer != null ) ) {
			synchronized( lookingGlassContainer.getTreeLock() ) {
				lookingGlassContainer.removeAllComponents();
			}
		}
		restartProgramContext();
	}

	@Override
	public void handlePostHideDialog( CompletionStep<?> step ) {
		super.handlePostHideDialog( step );
		programContext.cleanUpProgram();
	}
}
