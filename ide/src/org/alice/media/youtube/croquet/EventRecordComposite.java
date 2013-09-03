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
package org.alice.media.youtube.croquet;

import java.awt.event.KeyEvent;

import org.alice.ide.declarationseditor.events.KeyboardEventListenerMenu;
import org.alice.ide.declarationseditor.events.MouseEventListenerMenu;
import org.alice.media.components.EventRecordView;
import org.alice.stageide.StageIDE;
import org.alice.stageide.program.RunProgramContext;
import org.lgna.common.RandomUtilities;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.WizardPageComposite;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserMethod;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.animation.FrameObserver;
import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer;
import edu.cmu.cs.dennisc.matt.EventScript;
import edu.cmu.cs.dennisc.matt.EventScript.EventWithTime;
import edu.cmu.cs.dennisc.matt.EventScriptListener;
import edu.cmu.cs.dennisc.matt.MouseEventWrapper;

/**
 * @author Matt May
 */
public class EventRecordComposite extends WizardPageComposite<EventRecordView> {
	private static final java.util.List<org.lgna.project.ast.JavaMethod> interactiveMethods;
	private static final java.util.List<org.lgna.project.ast.JavaMethod> randomNumberFunctionList;
	static {
		java.util.List<org.lgna.project.ast.JavaMethod> list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		list.addAll( MouseEventListenerMenu.ALL_MOUSE_CLICK_EVENT_METHODS );
		list.addAll( KeyboardEventListenerMenu.ALL_KEYBOARD_EVENT_METHODS );
		interactiveMethods = java.util.Collections.unmodifiableList( list );
		randomNumberFunctionList = Collections.newLinkedList();
	};

	private final ExportToYouTubeWizardDialogComposite owner;
	private RunProgramContext programContext;
	private EventScript script;
	org.lgna.croquet.components.BorderPanel lookingGlassContainer;
	private double timeInSeconds = 0;
	private final ErrorStatus cannotAdvanceBecauseRecording = this.createErrorStatus( this.createKey( "cannotAdvanceBecauseRecording" ) );
	private final ListSelectionState<EventWithTime> eventList = createListSelectionState( createKey( "eventList" ), EventWithTime.class, new ItemCodec<EventWithTime>() {

		public Class<EventWithTime> getValueClass() {
			return EventWithTime.class;
		}

		public EventWithTime decodeValue( BinaryDecoder binaryDecoder ) {
			throw new RuntimeException( "todo" );
		}

		public void encodeValue( BinaryEncoder binaryEncoder, EventWithTime value ) {
			throw new RuntimeException( "todo" );
		}

		public void appendRepresentation( StringBuilder sb, EventWithTime value ) {
			String eventType = "";
			if( value.getEvent() instanceof MouseEventWrapper ) {
				eventType = owner.getMouseEventName().getText();
			} else if( value.getEvent() instanceof KeyEvent ) {
				eventType = owner.getKeyBoardEventName().getText();
			} else {
				eventType = "UNKNOWN EVENT TYPE: " + value.getEvent().getClass().getSimpleName();
			}
			sb.append( value.getReportForEventType( eventType ) );
		}
	}, -1 );

	private final EventScriptListener listener = new EventScriptListener() {

		public void fireChanged( EventWithTime event ) {
			eventList.addItem( event );
		}
	};

	private BooleanState isRecordingState = this.createBooleanState( this.createKey( "isRecordingState" ), false );

	private final ValueListener<Boolean> isRecordingListener = new ValueListener<Boolean>() {
		public void changing( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		public void changed( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			if( isRecordingState.getValue() ) {
				programContext.getProgramImp().startAnimator();
				programContext.getProgramImp().getAnimator().setSpeedFactor( 1 );
				restartRecording.setEnabled( true );
			} else {
				programContext.getProgramImp().getAnimator().setSpeedFactor( 0 );
			}
		}
	};

	public EventRecordComposite( ExportToYouTubeWizardDialogComposite owner ) {
		super( java.util.UUID.fromString( "35d34417-8c0c-4f06-b919-5945b336b596" ) );
		assert owner != null;
		this.owner = owner;
		isRecordingState.addValueListener( isRecordingListener );
		//isRecordingState.setIconForBothTrueAndFalse(  );
	}

	private FrameObserver frameListener = new FrameObserver() {

		public void update( double tCurrent ) {
			timeInSeconds = tCurrent;
			getView().updateTime();
		}

		public void complete() {
		}
	};
	private final ActionOperation restartRecording = this.createActionOperation( this.createKey( "restart" ), new Action() {

		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			if( isRecordingState.getValue() ) {
				getView().getPlayPauseButton().doClick();
			}
			resetData();
			return null;
		}

	} );

	@Override
	public void handlePostDeactivation() {
		if( isRecordingState.getValue() ) {
			getView().getPlayPauseButton().doClick();
		}
		super.handlePostDeactivation();
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		lookingGlassContainer = getView().getLookingGlassContainer();
		if( programContext == null ) {
			restartProgramContext();
		}
	}

	private void restartProgramContext() {
		restartRecording.setEnabled( false );
		if( ( programContext != null ) ) {
			programContext.getProgramImp().getAnimator().removeFrameObserver( frameListener );
		}
		if( containsRandom() ) {
			stashSeed( System.currentTimeMillis() );
			RandomUtilities.setSeed( owner.getRandomSeed() );
		}
		programContext = new RunProgramContext( owner.getProject().getProgramType() );
		programContext.getProgramImp().setControlPanelDesired( false );
		programContext.initializeInContainer( lookingGlassContainer.getAwtComponent(), 640, 360 );
		programContext.getProgramImp().getAnimator().addFrameObserver( frameListener );
		programContext.getProgramImp().stopAnimator();
		programContext.setActiveScene();
		script = ( (SceneImp)ImplementationAccessor.getImplementation( programContext.getProgram().getActiveScene() ) ).getTranscript();
		owner.setScript( script );
		eventList.clear();
		script.addListener( this.listener );
		this.timeInSeconds = 0;
		getView().updateTime();
	}

	public BooleanState getPlayRecordedOperation() {
		return this.isRecordingState;
	}

	public ActionOperation getRestartRecording() {
		return this.restartRecording;
	}

	public EventScript getScript() {
		return this.script;
	}

	@Override
	protected EventRecordView createView() {
		return new EventRecordView( this );
	}

	@Override
	public Status getPageStatus( org.lgna.croquet.history.CompletionStep<?> step ) {
		return isRecordingState.getValue() ? cannotAdvanceBecauseRecording : IS_GOOD_TO_GO_STATUS;
	}

	@Override
	protected boolean isOptional() {
		return true;
	}

	@Override
	protected boolean isAutoAdvanceWorthAttempting() {
		return true;
	}

	@Override
	protected boolean isClearedForAutoAdvance( org.lgna.croquet.history.CompletionStep<?> step ) {
		return ( !this.containsInputEvents() && !this.containsRandom() );
	}

	private boolean containsRandom() {
		StageIDE ide = StageIDE.getActiveInstance();
		RandomNumberFinder crawler = new RandomNumberFinder();
		ide.crawlFilteredProgramType( crawler );
		return crawler.getContainsRandom();
	}

	private void stashSeed( long currentTimeMillis ) {
		this.owner.setRandomSeed( currentTimeMillis );
	}

	class RandomNumberFinder implements edu.cmu.cs.dennisc.pattern.Crawler {

		private boolean containsRandom = false;

		public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
			if( crawlable instanceof MethodInvocation ) {
				if( ( (Statement)( (MethodInvocation)crawlable ).getFirstAncestorAssignableTo( Statement.class ) ).isEnabled.getValue() ) {
					return;
				}
				AbstractMethod method = ( (MethodInvocation)crawlable ).method.getValue();
				if( method.isFunction() ) {
					if( method.getDeclaringType() instanceof JavaType ) {
						JavaType jType = (JavaType)method.getDeclaringType();
						if( jType.getClassReflectionProxy().getReification().equals( RandomUtilities.class ) ) {
							containsRandom = true;
						}
					}
				}
			}
		}

		public boolean getContainsRandom() {
			return containsRandom;
		}
	}

	private boolean containsInputEvents() {
		NamedUserType sceneType = StageIDE.getActiveInstance().getSceneType();
		UserMethod initializeEventListeners = sceneType.getDeclaredMethod( StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME );
		BlockStatement body = initializeEventListeners.body.getValue();
		for( Statement statement : body.statements ) {
			if( statement.isEnabled.getValue() ) {
				if( statement instanceof ExpressionStatement ) {
					ExpressionStatement expressionStatement = (ExpressionStatement)statement;
					if( expressionStatement.expression.getValue() instanceof MethodInvocation ) {
						AbstractMethod method = ( (MethodInvocation)expressionStatement.expression.getValue() ).method.getValue();
						if( interactiveMethods.contains( method ) ) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public ListSelectionState<EventWithTime> getEventList() {
		return this.eventList;
	}

	public double getTimeInSeconds() {
		return this.timeInSeconds;
	}

	@Override
	public void resetData() {
		if( lookingGlassContainer != null ) {
			synchronized( lookingGlassContainer.getTreeLock() ) {
				lookingGlassContainer.removeAllComponents();
			}
		}
		lookingGlassContainer = getView().getLookingGlassContainer();
		restartProgramContext();
	}

	@Override
	public void handlePostHideDialog( CompletionStep<?> step ) {
		programContext.cleanUpProgram();
		super.handlePostHideDialog( step );
	}

	public ListCellRenderer<EventWithTime> getCellRenderer() {
		return owner.getCellRenderer();
	}
}
