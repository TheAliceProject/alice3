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

package org.lgna.croquet;

import org.lgna.croquet.components.AbstractWindow;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.Frame;

/**
 * @author Dennis Cosgrove
 */
public abstract class Application {
	public static final Group UI_STATE_GROUP = Group.getInstance( java.util.UUID.fromString( "d92c1a48-a6ae-473b-9b9f-94734e1606c1" ), "UI_STATE_GROUP" );
	public static final Group INFORMATION_GROUP = Group.getInstance( java.util.UUID.fromString( "c883259e-3346-49d0-a63f-52eeb3d9d805" ), "INFORMATION_GROUP" );
	public static final Group INHERIT_GROUP = Group.getInstance( java.util.UUID.fromString( "488f8cf9-30cd-49fc-ab72-7fd6a3e13c3f" ), "INHERIT_GROUP" );
	private static Application singleton;

	public static Application getSingleton() {
		return singleton;
	}

	public Application() {
		assert Application.singleton == null;
		Application.singleton = this;
		org.lgna.croquet.history.TransactionManager.startListeningToMenuSelection();
	}

	protected abstract Component< ? > createContentPane();

	private Frame frame = new Frame();
	private java.util.Stack< AbstractWindow< ? > > stack = edu.cmu.cs.dennisc.java.util.Collections.newStack( new AbstractWindow< ? >[] { frame } );
	
	public void pushWindow( AbstractWindow< ? > window ) {
		this.stack.push( window );
	}
	public AbstractWindow< ? > popWindow() {
		AbstractWindow< ? > rv = this.stack.peek();
		this.stack.pop();
		return rv;
	}
	
	public AbstractWindow< ? > getOwnerWindow() {
		return this.stack.peek();
	}
	
	public Frame getFrame() {
		return this.frame;
	}
	
	public abstract DropReceptor getDropReceptor( DropSite dropSite );
	public void initialize( String[] args ) {
		this.frame.getContentPanel().addComponent( this.createContentPane(), BorderPanel.Constraint.CENTER );
		this.frame.setDefaultCloseOperation( Frame.DefaultCloseOperation.DO_NOTHING );
		this.frame.addWindowListener( new java.awt.event.WindowListener() {
			public void windowOpened( java.awt.event.WindowEvent e ) {
				Application.this.handleWindowOpened( e );
			}
			public void windowClosing( java.awt.event.WindowEvent e ) {
				Application.this.handleQuit( new org.lgna.croquet.triggers.WindowEventTrigger( e ) );
			}
			public void windowClosed( java.awt.event.WindowEvent e ) {
			}
			public void windowActivated( java.awt.event.WindowEvent e ) {
			}
			public void windowDeactivated( java.awt.event.WindowEvent e ) {
			}
			public void windowIconified( java.awt.event.WindowEvent e ) {
			}
			public void windowDeiconified( java.awt.event.WindowEvent e ) {
			}
		} );
		edu.cmu.cs.dennisc.apple.AppleUtilities.addApplicationListener( new edu.cmu.cs.dennisc.apple.event.ApplicationListener() {
			public void handleAbout( java.util.EventObject e ) {
				Application.this.handleAbout( new org.lgna.croquet.triggers.AppleApplicationEventTrigger( e ) );
			}
			public void handlePreferences( java.util.EventObject e ) {
				Application.this.handlePreferences( new org.lgna.croquet.triggers.AppleApplicationEventTrigger( e ) );
			}
			public void handleQuit( java.util.EventObject e ) {
				Application.this.handleQuit( new org.lgna.croquet.triggers.AppleApplicationEventTrigger( e ) );
			}
			public void handleOpenFile( java.util.EventObject e ) {
				Application.this.handleOpenFile( new org.lgna.croquet.triggers.AppleApplicationEventTrigger( e ) );
			}
		} );
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
				
				Manager.localizeAllModels();
				
				try {
					javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getLookAndFeel() );
				} catch( javax.swing.UnsupportedLookAndFeelException ulafe ) {
					ulafe.printStackTrace();
				}
				//todo?
				//javax.swing.UIManager.getLookAndFeel().uninitialize();
				//javax.swing.UIManager.getLookAndFeel().initialize();
				for( javax.swing.JComponent component : edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( this.frame.getAwtComponent(), javax.swing.JComponent.class ) ) {
					component.setLocale( locale );
					component.setComponentOrientation( java.awt.ComponentOrientation.getOrientation( locale ) );
					component.revalidate();
					component.repaint();
				}
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: locale==null" );
		}
	}

	protected abstract void handleWindowOpened( java.awt.event.WindowEvent e );
	protected abstract void handleAbout( org.lgna.croquet.triggers.Trigger trigger );
	protected abstract void handlePreferences( org.lgna.croquet.triggers.Trigger trigger );
	protected abstract void handleQuit( org.lgna.croquet.triggers.Trigger trigger );
	protected abstract void handleOpenFile( org.lgna.croquet.triggers.Trigger trigger );

	public void showMessageDialog( Object message, String title, MessageType messageType, javax.swing.Icon icon ) {
		if( message instanceof Component<?> ) {
			message = ((Component<?>)message).getAwtComponent();
		}
		javax.swing.JOptionPane.showMessageDialog( this.frame.getAwtComponent(), message, title, messageType.internal, icon );
	}
	public void showMessageDialog( Object message, String title, MessageType messageType ) {
		showMessageDialog( message, title, messageType, null );
	}
	public void showMessageDialog( Object message, String title ) {
		showMessageDialog( message, title, MessageType.QUESTION );
	}
	public void showMessageDialog( Object message ) {
		showMessageDialog( message, null );
	}

	public YesNoCancelOption showYesNoCancelConfirmDialog( Object message, String title, MessageType messageType, javax.swing.Icon icon ) {
		return YesNoCancelOption.getInstance( javax.swing.JOptionPane.showConfirmDialog( this.frame.getAwtComponent(), message, title, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, messageType.internal, icon ) );
	}
	public YesNoCancelOption showYesNoCancelConfirmDialog( Object message, String title, MessageType messageType ) {
		return showYesNoCancelConfirmDialog( message, title, messageType, null );
	}
	public YesNoCancelOption showYesNoCancelConfirmDialog( Object message, String title ) {
		return showYesNoCancelConfirmDialog( message, title, MessageType.QUESTION );
	}
	public YesNoCancelOption showYesNoCancelConfirmDialog( Object message ) {
		return showYesNoCancelConfirmDialog( message, null );
	}
	public YesNoOption showYesNoConfirmDialog( Object message, String title, MessageType messageType, javax.swing.Icon icon ) {
		return YesNoOption.getInstance( javax.swing.JOptionPane.showConfirmDialog( this.frame.getAwtComponent(), message, title, javax.swing.JOptionPane.YES_NO_OPTION, messageType.internal, icon ) );
	}
	public YesNoOption showYesNoConfirmDialog( Object message, String title, MessageType messageType ) {
		return showYesNoConfirmDialog( message, title, messageType, null );
	}
	public YesNoOption showYesNoConfirmDialog( Object message, String title ) {
		return showYesNoConfirmDialog( message, title, MessageType.QUESTION );
	}
	public YesNoOption showYesNoConfirmDialog( Object message ) {
		return showYesNoConfirmDialog( message, null );
	}

	public Object showOptionDialog( String text, String title, MessageType messageType, javax.swing.Icon icon, Object optionA, Object optionB, int initialValueIndex ) {
		Object[] options = { optionA, optionB };
		Object initialValue = initialValueIndex >= 0 ? options[ initialValueIndex ] : null;
		int result = javax.swing.JOptionPane.showOptionDialog( this.frame.getAwtComponent(), text, title, javax.swing.JOptionPane.YES_NO_OPTION, messageType.internal, icon, options, initialValue );
		switch( result ) {
		case javax.swing.JOptionPane.YES_OPTION:
			return options[ 0 ];
		case javax.swing.JOptionPane.NO_OPTION:
			return options[ 1 ];
		default:
			return null;
		}
	}
	public Object showOptionDialog( String text, String title, MessageType messageType, javax.swing.Icon icon, Object optionA, Object optionB, Object optionC, int initialValueIndex ) {
		Object[] options = { optionA, optionB, optionC };
		Object initialValue = initialValueIndex >= 0 ? options[ initialValueIndex ] : null;
		int result = javax.swing.JOptionPane.showOptionDialog( this.frame.getAwtComponent(), text, title, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, messageType.internal, icon, options, initialValue );
		switch( result ) {
		case javax.swing.JOptionPane.YES_OPTION:
			return options[ 0 ];
		case javax.swing.JOptionPane.NO_OPTION:
			return options[ 1 ];
		case javax.swing.JOptionPane.CANCEL_OPTION:
			return options[ 2 ];
		default:
			return null;
		}

	}

	public java.io.File showOpenFileDialog( String directoryPath, String filename, String extension, boolean isSharingDesired ) {
		return edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showOpenFileDialog( this.frame.getAwtComponent(), directoryPath, filename, extension, isSharingDesired );
	}
	public java.io.File showSaveFileDialog( String directoryPath, String filename, String extension, boolean isSharingDesired ) {
		return edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showSaveFileDialog( this.frame.getAwtComponent(), directoryPath, filename, extension, isSharingDesired );
	}
	public java.io.File showOpenFileDialog( java.io.File directory, String filename, String extension, boolean isSharingDesired ) {
		return edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showOpenFileDialog( this.frame.getAwtComponent(), directory, filename, extension, isSharingDesired );
	}
	public java.io.File showSaveFileDialog( java.io.File directory, String filename, String extension, boolean isSharingDesired ) {
		return edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showSaveFileDialog( this.frame.getAwtComponent(), directory, filename, extension, isSharingDesired );
	}

	private boolean isDragInProgress = false;
	@Deprecated
	public final boolean isDragInProgress() {
		return this.isDragInProgress;
	}
	@Deprecated
	public void setDragInProgress( boolean isDragInProgress ) {
		this.isDragInProgress = isDragInProgress;
	}
}
