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

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent;
import com.apple.eawt.OpenFilesHandler;
import com.apple.eawt.PreferencesHandler;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;
import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.history.MenuSelection;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.preferences.PreferencesManager;
import org.lgna.croquet.triggers.AppleApplicationEventTrigger;
import org.lgna.croquet.triggers.ChangeEventTrigger;
import org.lgna.croquet.triggers.Trigger;

import javax.swing.JComponent;
import javax.swing.MenuSelectionManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import java.awt.ComponentOrientation;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class Application<D extends DocumentFrame> {

	public static final Group APPLICATION_UI_GROUP = Group.getInstance( UUID.fromString( "0d3393ba-8769-419d-915c-fd5038a5853a" ), "APPLICATION_UI_GROUP" );
	public static final Group DOCUMENT_UI_GROUP = Group.getInstance( UUID.fromString( "d92c1a48-a6ae-473b-9b9f-94734e1606c1" ), "DOCUMENT_UI_GROUP" );
	public static final Group INFORMATION_GROUP = Group.getInstance( UUID.fromString( "c883259e-3346-49d0-a63f-52eeb3d9d805" ), "INFORMATION_GROUP" );
	public static final Group INHERIT_GROUP = Group.getInstance( UUID.fromString( "488f8cf9-30cd-49fc-ab72-7fd6a3e13c3f" ), "INHERIT_GROUP" );
	public static final Group PROJECT_GROUP = Group.getInstance( UUID.fromString( "a89d2513-6d9a-4378-a08b-4d773618244d" ), "PROJECT_GROUP" );

	private final PreferencesManager preferencesManager;
	private final UserActivity systemActivity;

	private static Application<?> singleton;

	public static Application<?> getActiveInstance() {
		return singleton;
	}

	public Application() {
		assert Application.singleton == null;
		Application.singleton = this;
		preferencesManager = new PreferencesManager( this );
		this.systemActivity = new UserActivity();
		MenuSelectionManager.defaultManager().addChangeListener( this::handleMenuSelectionStateChanged );
	}

	public abstract D getDocumentFrame();

	public UserActivity getOverallUserActivity() {
		return systemActivity;
	}

	public PreferencesManager getPreferencesManager() {
		return preferencesManager;
	}

	// Find a user activity to add to. If the latest one is the top level application activity, create a new child in that
	public UserActivity acquireOpenActivity() {
		UserActivity latest = systemActivity.getLatestActivity();
		return latest == systemActivity ? systemActivity.newChildActivity() : latest;
	}

	public UserActivity acquireOpenUnboundedActivity() {
		UserActivity latest = acquireOpenActivity();
		return latest.getCompletionStep() != null ? latest.newChildActivity() : latest;
	}

	public void initialize( String[] args ) {
		if( SystemUtilities.isMac() ) {
			com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
			application.setAboutHandler( new AboutHandler() {
				@Override
				public void handleAbout( AppEvent.AboutEvent e ) {
					Operation aboutOperation = Application.this.getAboutOperation();
					if( aboutOperation != null ) {
						aboutOperation.fire( AppleApplicationEventTrigger.createUserInstance( e ) );
					}
				}
			} );
			application.setPreferencesHandler( new PreferencesHandler() {
				@Override
				public void handlePreferences( AppEvent.PreferencesEvent e ) {
					Operation preferencesOperation = Application.this.getPreferencesOperation();
					if( preferencesOperation != null ) {
						preferencesOperation.fire( AppleApplicationEventTrigger.createUserInstance( e ) );
					}
				}
			} );
			application.setQuitHandler( new QuitHandler() {
				@Override
				public void handleQuitRequestWith( AppEvent.QuitEvent e, QuitResponse quitResponse ) {
					final AppleApplicationEventTrigger trigger = AppleApplicationEventTrigger.createUserInstance( e );
					Application.this.handleQuit( trigger );
					if ( trigger.getUserActivity().isCanceled() ) {
						quitResponse.cancelQuit();
					}
				}
			} );

			application.setOpenFileHandler( new OpenFilesHandler() {
				@Override
				public void openFiles( AppEvent.OpenFilesEvent e ) {
					Application.this.handleOpenFiles( e.getFiles() );
				}
			} );
		}
		//this.frame.pack();
	}

	public void setLocale( Locale locale ) {
		if( locale != null ) {
			if( locale.equals( Locale.getDefault() ) && locale.equals( JComponent.getDefaultLocale() ) ) {
				//pass
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "skipping locale", locale );
			} else {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "setLocale", locale );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "java.util.Locale.getDefault()", java.util.Locale.getDefault() );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "javax.swing.JComponent.getDefaultLocale()", javax.swing.JComponent.getDefaultLocale() );
				Locale.setDefault( locale );
				JComponent.setDefaultLocale( locale );

				Manager.relocalizeAllElements();

				try {
					UIManager.setLookAndFeel( UIManager.getLookAndFeel() );
				} catch( UnsupportedLookAndFeelException ulafe ) {
					ulafe.printStackTrace();
				}
				//todo?
				//javax.swing.UIManager.getLookAndFeel().uninitialize();
				//javax.swing.UIManager.getLookAndFeel().initialize();
				for( JComponent component : ComponentUtilities.findAllMatches( this.getDocumentFrame().getFrame().getAwtComponent(), JComponent.class ) ) {
					component.setLocale( locale );
					component.setComponentOrientation( ComponentOrientation.getOrientation( locale ) );
					component.revalidate();
					component.repaint();
				}
			}
		} else {
			Logger.warning( "locale==null" );
		}
	}

	public static Locale getLocale() {
		return JComponent.getDefaultLocale();
	}

	protected abstract Operation getAboutOperation();

	protected abstract Operation getPreferencesOperation();

	protected abstract void handleOpenFiles( List<File> files );

	protected abstract void handleWindowOpened( WindowEvent e );

	public abstract void handleQuit( Trigger trigger );

	private boolean isDragInProgress = false;

	@Deprecated
	public final synchronized boolean isDragInProgress() {
		return this.isDragInProgress;
	}

	@Deprecated
	public synchronized void setDragInProgress( boolean isDragInProgress ) {
		this.isDragInProgress = isDragInProgress;
	}

	private void handleMenuSelectionStateChanged( ChangeEvent e ) {
		MenuSelection menuSelection = new MenuSelection();
		if( menuSelection.isValid() ) {
			final UserActivity menuActivity = acquireOpenActivity();
			ChangeEventTrigger.createUserInstance( menuActivity, e );
			menuActivity.addMenuSelection( menuSelection );
		}
	}

	public abstract String getApplicationSubPath();
}
