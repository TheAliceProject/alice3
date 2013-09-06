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

import java.io.File;

import org.lgna.common.RandomUtilities;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.codecs.DefaultItemCodec;

import edu.cmu.cs.dennisc.matt.eventscript.EventScript;
import edu.cmu.cs.dennisc.matt.eventscript.events.EventScriptEvent;

/**
 * @author Dennis Cosgrove
 */
public class ExportToYouTubeWizardDialogComposite extends org.lgna.croquet.OperationWizardDialogCoreComposite {
	private final EventRecordComposite eventRecordComposite = new EventRecordComposite( this );
	private final ImageRecordComposite imageRecordComposite = new ImageRecordComposite( this );
	private final UploadComposite uploadComposite = new UploadComposite( this );
	private final StringValue mouseEventName = createStringValue( createKey( "mouseEvent" ) );
	private final StringValue keyBoardEventName = createStringValue( createKey( "keyboardEvent" ) );
	private final ListSelectionState<EventScriptEvent> eventList = createListSelectionState( createKey( "eventList" ), EventScriptEvent.class, new DefaultItemCodec<EventScriptEvent>( EventScriptEvent.class ), -1 );

	private org.lgna.project.Project project;
	private EventScript eventScript;
	private File tempRecordedVideoFile;
	private long randomSeed;

	public ExportToYouTubeWizardDialogComposite() {
		super( java.util.UUID.fromString( "c3542871-3346-4228-a872-1c5641c14e9d" ), org.alice.ide.IDE.EXPORT_GROUP );
		this.addPage( this.eventRecordComposite );
		this.addPage( this.imageRecordComposite );
		this.addPage( this.uploadComposite );
	}

	public org.lgna.project.Project getProject() {
		return this.project;
	}

	public void setProject( org.lgna.project.Project project ) {
		this.project = project;
	}

	public EventScript getEventScript() {
		return this.eventScript;
	}

	public void setEventScript( EventScript script ) {
		this.eventScript = script;
	}

	public File getTempRecordedVideoFile() {
		return this.tempRecordedVideoFile;
	}

	public void setTempRecordedVideoFile( File file ) {
		this.tempRecordedVideoFile = file;
		if( this.tempRecordedVideoFile != null ) {
			edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( this.tempRecordedVideoFile );
		}
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		return null;
	}

	public void setRandomSeed( long currentTimeMillis ) {
		this.randomSeed = currentTimeMillis;
		RandomUtilities.setSeed( currentTimeMillis );
	}

	public long getRandomSeed() {
		return this.randomSeed;
	}

	public StringValue getMouseEventName() {
		return this.mouseEventName;
	}

	public StringValue getKeyBoardEventName() {
		return this.keyBoardEventName;
	}

	@Override
	protected boolean isClearedForCommit() {
		return super.isClearedForCommit() && uploadComposite.tryToUpload();
	}

	//	public EventWithTimeListCellRenderer getCellRenderer() {
	//		return renderer;
	//	}
	//
	//	private class EventWithTimeListCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer<EventScriptEvent> {
	//
	//		@Override
	//		protected JLabel getListCellRendererComponent( JLabel rv, JList list, EventScriptEvent value, int index, boolean isSelected, boolean cellHasFocus ) {
	//			String eventType = "";
	//			if( value.getEvent() instanceof MouseEventWrapper ) {
	//				eventType = getMouseEventName().getText();
	//			} else if( value.getEvent() instanceof KeyEvent ) {
	//				eventType = getKeyBoardEventName().getText();
	//			} else {
	//				eventType = "UNKNOWN EVENT TYPE: " + value.getEvent().getClass().getSimpleName();
	//			}
	//			rv.setText( value.getReportForEventType( eventType ) );
	//			return rv;
	//		}
	//
	//	}

	public static void main( final String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}

		final boolean IS_SIMPLE_APPLICATION_SUFFICIENT = true;
		final org.lgna.project.Project project;
		if( IS_SIMPLE_APPLICATION_SUFFICIENT ) {
			new org.lgna.croquet.simple.SimpleApplication();
			File projectFile = new File( args[ 0 ] );
			project = org.lgna.project.io.IoUtilities.readProject( projectFile );
			org.alice.ide.ProjectStack.pushProject( project );
		} else {
			org.alice.ide.LaunchUtilities.launchAndWait( org.alice.stageide.StageIDE.class, null, args, false );
			project = org.alice.stageide.StageIDE.getActiveInstance().getProject();
		}

		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				ExportToYouTubeWizardDialogComposite composite = new ExportToYouTubeWizardDialogComposite();
				composite.setProject( project );
				composite.getOperation().fire();
				System.exit( 0 );
			}
		} );
	}

	public ListSelectionState<EventScriptEvent> getEventList() {
		return eventList;
	}
}
