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
package org.alice.ide.system.croquet;

/**
 * @author Dennis Cosgrove
 */
public class WindowsSystemAssessmentToolComposite extends org.lgna.croquet.SimpleOperationUnadornedDialogCoreComposite<org.alice.ide.system.croquet.views.WindowsSystemAssessmentToolPane> {
	private static class SingletonHolder {
		private static WindowsSystemAssessmentToolComposite instance = new WindowsSystemAssessmentToolComposite();
	}

	public static WindowsSystemAssessmentToolComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final org.lgna.croquet.PlainStringValue header = this.createStringValue( "header" );
	private final org.lgna.croquet.StringState stardardOutAndStandardErrorState = this.createStringState( "stardardOutAndStandardErrorState" );
	private final org.lgna.croquet.Operation executeWinsatOperation = this.createActionOperation( "executeWinsatOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			boolean isAbleToRunWinsatDirectly;
			try {
				StringBuilder sb = new StringBuilder();
				int result = edu.cmu.cs.dennisc.java.lang.ProcessUtilities.startAndDrainStandardOutAndStandardError( new ProcessBuilder( "winsat", "-?" ), sb );
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "result:", result );
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( sb.toString() );
				isAbleToRunWinsatDirectly = result == 0;
			} catch( edu.cmu.cs.dennisc.java.lang.ProcessStartException pse ) {
				//User Account Control?
				pse.printStackTrace();
				isAbleToRunWinsatDirectly = false;
			} catch( java.io.IOException ioe ) {
				ioe.printStackTrace();
				isAbleToRunWinsatDirectly = false;
			}

			//isAbleToRunWinsatDirectly = false;
			ProcessBuilder[] processBuilders;
			if( isAbleToRunWinsatDirectly ) {
				processBuilders = new ProcessBuilder[] {
						new ProcessBuilder( "winsat", "dwm" ),
						new ProcessBuilder( "winsat", "d3d" )
				};
			} else {
				//				try {
				//					java.io.File tempFile = java.io.File.createTempFile( "fixGraphics", ".bat" );
				//					tempFile.deleteOnExit();
				//					StringBuilder sb = new StringBuilder();
				//					sb.append( "winsat dwm\n" );
				//					sb.append( "winsat d3d\n" );
				//					edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( tempFile, sb.toString() );
				//					processBuilders = new ProcessBuilder[] {
				//						new ProcessBuilder( "cmd", "/C", "start", tempFile.getAbsolutePath() )
				//					};
				//				} catch( java.io.IOException ioe ) {
				//					throw new RuntimeException( "cannot create temp file", ioe );
				//				}
				processBuilders = new ProcessBuilder[] {
						new ProcessBuilder( "cmd", "/c", "winsat.exe dwm & winsat.exe d3d" )
				};
			}
			final javax.swing.text.AttributeSet attributeSet = null;
			edu.cmu.cs.dennisc.worker.process.ProcessWorker processWorker = new edu.cmu.cs.dennisc.worker.process.ProcessWorker( processBuilders ) {
				private void appendText( String s ) {
					javax.swing.text.Document document = stardardOutAndStandardErrorState.getSwingModel().getDocument();
					try {
						document.insertString( document.getLength(), s, attributeSet );
					} catch( javax.swing.text.BadLocationException ble ) {
						ble.printStackTrace();
					}
				}

				@Override
				protected void handleStart_onEventDispatchThread() {
					//this.appendText( "start\n" );
				}

				@Override
				protected void handleStartProcess_onEventDispatchThread( int i ) {
					this.appendText( "start process " + this.getProcessBuilders()[ i ].command() + "\n" );
				}

				@Override
				protected void handleProcessStandardOutAndStandardError_onEventDispatchThread( String s ) {
					this.appendText( s );
				}

				@Override
				protected void handleDone_onEventDispatchThread( Integer value ) {
					this.appendText( "done." );
				}
			};
			processWorker.execute();
			return null;
		}
	} );

	private WindowsSystemAssessmentToolComposite() {
		super( java.util.UUID.fromString( "3c659189-6425-4741-9e30-4f4b3bde2b23" ), org.lgna.croquet.Application.APPLICATION_UI_GROUP );
	}

	public org.lgna.croquet.PlainStringValue getHeader() {
		return this.header;
	}

	public org.lgna.croquet.StringState getStardardOutAndStandardErrorState() {
		return this.stardardOutAndStandardErrorState;
	}

	public org.lgna.croquet.Operation getExecuteWinsatOperation() {
		return this.executeWinsatOperation;
	}

	@Override
	protected org.alice.ide.system.croquet.views.WindowsSystemAssessmentToolPane createView() {
		return new org.alice.ide.system.croquet.views.WindowsSystemAssessmentToolPane( this );
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		WindowsSystemAssessmentToolComposite.getInstance().getLaunchOperation().fire();
		System.exit( 0 );
	}
}
