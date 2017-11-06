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

package org.lgna.croquet;

import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.triggers.AppleApplicationEventTrigger;
import org.lgna.croquet.triggers.Trigger;

/**
 * @author Dennis Cosgrove
 */
public abstract class Application<D extends DocumentFrame> {

	public static final Group APPLICATION_UI_GROUP = Group.getInstance( java.util.UUID.fromString( "0d3393ba-8769-419d-915c-fd5038a5853a" ), "APPLICATION_UI_GROUP" );
	public static final Group DOCUMENT_UI_GROUP = Group.getInstance( java.util.UUID.fromString( "d92c1a48-a6ae-473b-9b9f-94734e1606c1" ), "DOCUMENT_UI_GROUP" );
	public static final Group INFORMATION_GROUP = Group.getInstance( java.util.UUID.fromString( "c883259e-3346-49d0-a63f-52eeb3d9d805" ), "INFORMATION_GROUP" );
	public static final Group INHERIT_GROUP = Group.getInstance( java.util.UUID.fromString( "488f8cf9-30cd-49fc-ab72-7fd6a3e13c3f" ), "INHERIT_GROUP" );
	public static final Group PROJECT_GROUP = Group.getInstance( java.util.UUID.fromString( "a89d2513-6d9a-4378-a08b-4d773618244d" ), "PROJECT_GROUP" );

	private final org.lgna.croquet.history.TransactionHistory transactionHistory;
	private final javax.swing.event.ChangeListener menuSelectionChangeListener = new javax.swing.event.ChangeListener() {
		@Override
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			handleMenuSelectionStateChanged( e );
		}
	};

	private static Application<?> singleton;

	public static Application<?> getActiveInstance() {
		return singleton;
	}

	public Application() {
		assert Application.singleton == null;
		Application.singleton = this;
		this.transactionHistory = new org.lgna.croquet.history.TransactionHistory();
		javax.swing.MenuSelectionManager.defaultManager().addChangeListener( this.menuSelectionChangeListener );
	}

	public abstract D getDocumentFrame();

	public org.lgna.croquet.history.TransactionHistory getTransactionHistory() {
		return this.transactionHistory;
	}

	// TODO: Fix this the right way... if we *ever* support multiple documents... this is a hack for now...
	@Deprecated
	public org.lgna.croquet.history.TransactionHistory getApplicationOrDocumentTransactionHistory() {
		DocumentFrame documentFrame = this.getDocumentFrame();
		if( documentFrame != null ) {
			Document document = documentFrame.getDocument();
			if( document == null ) {
				return this.getTransactionHistory();
			} else {
				return document.getRootTransactionHistory();
			}
		} else {
			return this.getTransactionHistory();
		}
	}

	public void initialize( String[] args ) {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
			com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
			application.setAboutHandler( new com.apple.eawt.AboutHandler() {
				@Override
				public void handleAbout( com.apple.eawt.AppEvent.AboutEvent e ) {
					Operation aboutOperation = Application.this.getAboutOperation();
					if( aboutOperation != null ) {
						aboutOperation.fire( org.lgna.croquet.triggers.AppleApplicationEventTrigger.createUserInstance( e ) );
					}
				}
			} );
			application.setPreferencesHandler( new com.apple.eawt.PreferencesHandler() {
				@Override
				public void handlePreferences( com.apple.eawt.AppEvent.PreferencesEvent e ) {
					Operation preferencesOperation = Application.this.getPreferencesOperation();
					if( preferencesOperation != null ) {
						preferencesOperation.fire( org.lgna.croquet.triggers.AppleApplicationEventTrigger.createUserInstance( e ) );
					}
				}
			} );
			application.setQuitHandler( new com.apple.eawt.QuitHandler() {
				@Override
				public void handleQuitRequestWith( com.apple.eawt.AppEvent.QuitEvent e, com.apple.eawt.QuitResponse quitResponse ) {
					CompletionStep<?> completion = Application.this.handleQuit( AppleApplicationEventTrigger.createUserInstance( e ) );
					if (completion.isCanceled()) {
						quitResponse.cancelQuit();
					}
				}
			} );

			application.setOpenFileHandler( new com.apple.eawt.OpenFilesHandler() {
				@Override
				public void openFiles( com.apple.eawt.AppEvent.OpenFilesEvent e ) {
					Application.this.handleOpenFiles( e.getFiles() );
				}
			} );
		}
		//this.frame.pack();
	}

	public void setLocale( java.util.Locale locale ) {
		if( locale != null ) {
			if( locale.equals( java.util.Locale.getDefault() ) && locale.equals( javax.swing.JComponent.getDefaultLocale() ) ) {
				//pass
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "skipping locale", locale );
			} else {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "setLocale", locale );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "java.util.Locale.getDefault()", java.util.Locale.getDefault() );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "javax.swing.JComponent.getDefaultLocale()", javax.swing.JComponent.getDefaultLocale() );
				java.util.Locale.setDefault( locale );
				javax.swing.JComponent.setDefaultLocale( locale );

				Manager.relocalizeAllElements();

				try {
					javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getLookAndFeel() );
				} catch( javax.swing.UnsupportedLookAndFeelException ulafe ) {
					ulafe.printStackTrace();
				}
				//todo?
				//javax.swing.UIManager.getLookAndFeel().uninitialize();
				//javax.swing.UIManager.getLookAndFeel().initialize();
				for( javax.swing.JComponent component : edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( this.getDocumentFrame().getFrame().getAwtComponent(), javax.swing.JComponent.class ) ) {
					component.setLocale( locale );
					component.setComponentOrientation( java.awt.ComponentOrientation.getOrientation( locale ) );
					component.revalidate();
					component.repaint();
				}
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "locale==null" );
		}
	}

	public static java.util.Locale getLocale() {
		return javax.swing.JComponent.getDefaultLocale();
	}

	protected abstract Operation getAboutOperation();

	protected abstract Operation getPreferencesOperation();

	protected abstract void handleOpenFiles( java.util.List<java.io.File> files );

	protected abstract void handleWindowOpened( java.awt.event.WindowEvent e );

	public abstract CompletionStep<?> handleQuit( Trigger trigger );

	private boolean isDragInProgress = false;

	@Deprecated
	public final synchronized boolean isDragInProgress() {
		return this.isDragInProgress;
	}

	@Deprecated
	public synchronized void setDragInProgress( boolean isDragInProgress ) {
		this.isDragInProgress = isDragInProgress;
	}

	private void handleMenuSelectionStateChanged( javax.swing.event.ChangeEvent e ) {
		org.lgna.croquet.triggers.ChangeEventTrigger trigger = org.lgna.croquet.triggers.ChangeEventTrigger.createUserInstance( e );
		org.lgna.croquet.history.MenuSelection menuSelection = new org.lgna.croquet.history.MenuSelection( trigger );
		if( menuSelection.isValid() ) {
			// TODO: This is probably not the best way to handle this... we should really have a document application and an non-document application.
			// and then override this method.
			this.getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory().acquireActiveTransaction().addMenuSelection( menuSelection );
		}
	}
}
