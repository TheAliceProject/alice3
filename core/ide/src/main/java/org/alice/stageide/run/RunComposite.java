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
package org.alice.stageide.run;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import org.alice.ide.IDE;
import org.alice.stageide.program.RunProgramContext;
import org.alice.stageide.run.views.RunView;
import org.alice.stageide.run.views.icons.RunIcon;
import org.lgna.common.ComponentExecutor;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.SimpleModalFrameComposite;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.croquet.views.AwtAdapter;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.FixedAspectRatioPanel;
import org.lgna.croquet.views.Frame;
import org.lgna.story.implementation.ProgramImp;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class RunComposite extends SimpleModalFrameComposite<RunView> {
	private static class SingletonHolder {
		private static RunComposite instance = new RunComposite();
	}

	public static RunComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final PlainStringValue restartLabel = this.createStringValue( "restart" );
	private final PlainStringValue speedFormat= this.createStringValue( "speed" );

	private RunComposite() {
		super( UUID.fromString( "985b3795-e1c7-4114-9819-fae4dcfe5676" ), IDE.RUN_GROUP );
		//todo: move to localize
		this.getLaunchOperation().setButtonIcon( new RunIcon() );
	}

	private transient RunProgramContext programContext;
	public static final double WIDTH_TO_HEIGHT_RATIO = 16.0 / 9.0;
	private static final int DEFAULT_WIDTH = 640;
	private static final int DEFAULT_HEIGHT = (int)( DEFAULT_WIDTH / WIDTH_TO_HEIGHT_RATIO );
	private Point location = null;
	private Dimension size = null;

	@Override
	protected GoldenRatioPolicy getGoldenRatioPolicy() {
		return null;
	}

	@Override
	protected Point getDesiredWindowLocation() {
		return this.location;
	}

	@Override
	protected Dimension calculateWindowSize( AbstractWindow<?> window ) {
		if( this.size != null ) {
			return this.size;
		} else {
			return super.calculateWindowSize( window );
		}
	}

	private class ProgramRunnable implements Runnable {
		public ProgramRunnable( ProgramImp.AwtContainerInitializer awtContainerInitializer ) {
			RunComposite.this.programContext = new RunProgramContext();
			RunComposite.this.programContext.getProgramImp().setRestartAction( RunComposite.this.restartAction );
			RunComposite.this.programContext.getProgramImp().setSpeedFormat( RunComposite.this.speedFormat.getText());
			RunComposite.this.programContext.initializeInContainer( awtContainerInitializer );
		}

		@Override
		public void run() {
			RunComposite.this.programContext.setActiveScene();
		}
	}

	private final class RunAwtContainerInitializer implements ProgramImp.AwtContainerInitializer {
		@Override
		public void addComponents( OnscreenRenderTarget<?> onscreenRenderTarget, JPanel controlPanel ) {
			RunView runView = RunComposite.this.getView();
			runView.forgetAndRemoveAllComponents();

			AwtComponentView<?> lookingGlassContainer = new AwtAdapter( onscreenRenderTarget.getAwtComponent() );
			FixedAspectRatioPanel fixedAspectRatioPanel = new FixedAspectRatioPanel( lookingGlassContainer, WIDTH_TO_HEIGHT_RATIO );
			fixedAspectRatioPanel.setBackgroundColor( Color.BLACK );
			if( controlPanel != null ) {
				runView.getAwtComponent().add( controlPanel, BorderLayout.PAGE_START );
			}
			runView.addCenterComponent( fixedAspectRatioPanel );
			runView.revalidateAndRepaint();
		}
	}

	private final RunAwtContainerInitializer runAwtContainerInitializer = new RunAwtContainerInitializer();

	private void startProgram() {
		new ComponentExecutor( new ProgramRunnable( runAwtContainerInitializer ), RunComposite.this.getLaunchOperation().getImp().getName() ).start();
		if( this.fastForwardToStatementOperation != null ) {
			this.fastForwardToStatementOperation.pre( this.programContext );
		}
	}

	private void stopProgram() {
		if( this.programContext != null ) {
			this.programContext.cleanUpProgram();
			this.programContext = null;
		} else {
			Logger.warning( this );
		}
	}

	private class RestartAction extends AbstractAction {
		@Override
		public void actionPerformed( ActionEvent e ) {
			RunComposite.this.stopProgram();
			RunComposite.this.startProgram();
		}
	};

	private final RestartAction restartAction = new RestartAction();

	@Override
	protected void localize() {
		super.localize();
		this.restartAction.putValue( javax.swing.Action.NAME, this.restartLabel.getText() );
	}

	@Override
	protected void handlePreShowWindow( Frame frame ) {
		super.handlePreShowWindow( frame );
		this.startProgram();
		if( this.size != null ) {
			frame.setSize( this.size );
		} else {
			this.programContext.getOnscreenRenderTarget().getAwtComponent().setPreferredSize( new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ) );
			frame.pack();
		}
		if( this.location != null ) {
			frame.setLocation( this.location );
		} else {
			Frame documentFrame = IDE.getActiveInstance().getDocumentFrame().getFrame();
			if( documentFrame != null ) {
				frame.setLocationRelativeTo( documentFrame );
			} else {
				frame.setLocationByPlatform( true );
			}
		}
	}

	@Override
	protected void handlePostHideWindow( Frame frame ) {
		final RunProgramContext context = programContext;
		if (frame != null && context!= null) {
			final ProgramImp imp = context.getProgramImp();
			if (imp != null) {
				Rectangle bounds = imp.getNormalDialogBounds(frame.getAwtComponent());
				this.location = bounds.getLocation();
				this.size = bounds.getSize();
			}
		}
		super.handlePostHideWindow( frame );
	}

	@Override
	protected void handleFinally() {
		if( this.fastForwardToStatementOperation != null ) {
			this.fastForwardToStatementOperation.post();
			this.fastForwardToStatementOperation = null;
		}
		this.stopProgram();
		super.handleFinally();
	}

	@Override
	protected RunView createView() {
		return new RunView( this );
	}

	public void setFastForwardToStatementOperation( FastForwardToStatementOperation fastForwardToStatementOperation ) {
		this.fastForwardToStatementOperation = fastForwardToStatementOperation;
	}

	private FastForwardToStatementOperation fastForwardToStatementOperation;

}
