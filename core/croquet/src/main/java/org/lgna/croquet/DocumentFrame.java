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

import edu.cmu.cs.dennisc.java.awt.FileDialogUtilities;
import edu.cmu.cs.dennisc.java.util.DStack;
import edu.cmu.cs.dennisc.java.util.Stacks;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.triggers.WindowEventTrigger;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.croquet.views.Frame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class DocumentFrame {
	public DocumentFrame() {
		this.frame.setDefaultCloseOperation( Frame.DefaultCloseOperation.DO_NOTHING );
		this.frame.addWindowListener( this.windowListener );
		this.stack = Stacks.newStack( this.getFrame() );
	}

	public abstract Document getDocument();

	public Frame getFrame() {
		return this.frame;
	}

	public void pushWindow( AbstractWindow<?> window ) {
		this.stack.push( window );
	}

	public AbstractWindow<?> popWindow() {
		AbstractWindow<?> rv = this.stack.peek();
		this.stack.pop();
		return rv;
	}

	public AbstractWindow<?> peekWindow() {
		if( this.stack.size() > 0 ) {
			return this.stack.peek();
		} else {
			Logger.severe( "window stack is empty" );
			return null;
		}
	}

	@Deprecated
	public File showOpenFileDialog( File directory, String filename, String extension, boolean isSharingDesired ) {
		return FileDialogUtilities.showOpenFileDialog( this.frame.getAwtComponent(), directory, filename, extension, isSharingDesired );
	}

	@Deprecated
	public File showSaveFileDialog( File directory, String filename, String extension, boolean isSharingDesired ) {
		return FileDialogUtilities.showSaveFileDialog( this.frame.getAwtComponent(), directory, filename, extension, isSharingDesired );
	}

	public File showOpenFileDialog( UUID sharingId, String dialogTitle, File initialDirectory, String initialFilename, FilenameFilter filenameFilter ) {
		return FileDialogUtilities.showOpenFileDialog( sharingId, this.peekWindow().getAwtComponent(), dialogTitle, initialDirectory, initialFilename, filenameFilter );
	}

	private final Frame frame = Frame.getApplicationRootFrame();
	//private final edu.cmu.cs.dennisc.java.util.DStack<org.lgna.croquet.views.AbstractWindow<?>> stack = edu.cmu.cs.dennisc.java.util.Stacks.newStack( this.frame );
	private final WindowListener windowListener = new WindowListener() {
		@Override
		public void windowOpened( WindowEvent e ) {
			Application.getActiveInstance().handleWindowOpened( e );
		}

		@Override
		public void windowClosing( WindowEvent e ) {
			Application.getActiveInstance().handleQuit( WindowEventTrigger.createUserActivity( e ) );
		}

		@Override
		public void windowClosed( WindowEvent e ) {
		}

		@Override
		public void windowActivated( WindowEvent e ) {
		}

		@Override
		public void windowDeactivated( WindowEvent e ) {
		}

		@Override
		public void windowIconified( WindowEvent e ) {
		}

		@Override
		public void windowDeiconified( WindowEvent e ) {
		}
	};
	private final DStack<AbstractWindow<?>> stack;
}
