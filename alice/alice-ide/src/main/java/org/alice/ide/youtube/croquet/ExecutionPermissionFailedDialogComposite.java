/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.youtube.croquet;

import java.awt.Container;
import java.io.File;

import javax.swing.JDialog;

import org.alice.ide.youtube.croquet.views.ExecutionPermissionFailedDialogView;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.MessageDialogComposite;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;

import edu.cmu.cs.dennisc.java.lang.RuntimeUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.wustl.lookingglass.media.FFmpegProcess;

/**
 * @author Matt May
 */
public class ExecutionPermissionFailedDialogComposite extends MessageDialogComposite<ExecutionPermissionFailedDialogView> {

	private final StringValue explanation = createStringValue( "explanation" );
	private boolean isFixed = false;
	private final org.lgna.croquet.Operation browserOperation = new org.alice.ide.browser.ImmutableBrowserOperation( java.util.UUID.fromString( "06d89886-9433-4b52-85b6-10615412eb0c" ), org.alice.ide.help.HelpBrowserOperation.HELP_URL_SPEC + "w/page/68664600/FFmpeg_execute_permission" );
	private final File ffmpegFile;

	public ExecutionPermissionFailedDialogComposite( File f ) {
		super( java.util.UUID.fromString( "d60cddc2-ec53-40bd-949b-7a445b92b43b" ), edu.cmu.cs.dennisc.javax.swing.option.MessageType.ERROR );
		this.ffmpegFile = f;
	}

	private final ActionOperation troubleShootAction = createActionOperation( "troubleShoot", new Action() {

		@Override
		public Edit perform( CompletionStep<?> step, InternalActionOperation source ) throws CancelException {
			if( SystemUtilities.isMac() ) {
				RuntimeUtilities.exec( "chmod a+x " + ffmpegFile.getAbsolutePath() );
				isFixed = ( ffmpegFile != null ) && ffmpegFile.canExecute();
				if( isFixed ) {
					closeDown();
				}
			} else if( SystemUtilities.isWindows() ) {
				// TODO later perhaps? (mmay)
				//				DesktopUtilities.open( ffmpegFile.getParentFile() );
			}
			return null;
		}
	} );

	private void closeDown() {
		Container awtComponent = ExecutionPermissionFailedDialogComposite.this.getView().getAwtComponent();
		while( ( awtComponent != null ) && !( awtComponent instanceof JDialog ) ) {
			awtComponent = awtComponent.getParent();
		}
		assert awtComponent != null;
		( (JDialog)awtComponent ).setVisible( false );
	}

	@Override
	protected org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return null;
	}

	@Override
	protected ExecutionPermissionFailedDialogView createView() {
		return new ExecutionPermissionFailedDialogView( this );
	}

	public boolean getIsFixed() {
		return isFixed;
	}

	public StringValue getExplanation() {
		return this.explanation;
	}

	public org.lgna.croquet.Operation getBrowserOperation() {
		return this.browserOperation;
	}

	public ActionOperation getTroubleShootAction() {
		return SystemUtilities.isMac() ? this.troubleShootAction : null;
	}

	public static void main( String[] args ) {
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		new ExecutionPermissionFailedDialogComposite( new File( FFmpegProcess.getArchitectureSpecificCommand() ) ).getLaunchOperation().fire();
		System.exit( 0 );
	}
}
