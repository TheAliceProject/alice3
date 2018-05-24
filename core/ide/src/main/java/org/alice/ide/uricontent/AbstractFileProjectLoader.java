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
package org.alice.ide.uricontent;

import edu.cmu.cs.dennisc.javax.swing.option.MessageType;
import edu.cmu.cs.dennisc.javax.swing.option.OkDialog;
import org.alice.ide.ProjectApplication;
import org.lgna.project.Project;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.io.IoUtilities;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.zip.ZipFile;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractFileProjectLoader extends UriProjectLoader {
	public AbstractFileProjectLoader( File file ) {
		this.file = file;
	}

	protected File getFile() {
		return this.file;
	}

	@Override
	protected Project load() {
		if( file.exists() ) {
			final Locale locale = Locale.ENGLISH;
			String lcFilename = file.getName().toLowerCase( locale );
			if( lcFilename.endsWith( ".a2w" ) ) {
				new OkDialog.Builder( "Alice3 does not load Alice2 worlds" )
						.title( "Cannot read file" )
						.messageType( MessageType.ERROR )
						.buildAndShow();
			} else if( lcFilename.endsWith( IoUtilities.TYPE_EXTENSION.toLowerCase( locale ) ) ) {
				new OkDialog.Builder( file.getAbsolutePath() + " appears to be a class file and not a project file.\n\nLook for files with an " + IoUtilities.PROJECT_EXTENSION + " extension." )
						.title( "Incorrect File Type" )
						.messageType( MessageType.ERROR )
						.buildAndShow();
			} else {
				boolean isWorthyOfException = lcFilename.endsWith( IoUtilities.PROJECT_EXTENSION.toLowerCase( locale ) );
				ZipFile zipFile;
				try {
					zipFile = new ZipFile( file );
				} catch( IOException ioe ) {
					if( isWorthyOfException ) {
						throw new RuntimeException( file.getAbsolutePath(), ioe );
					} else {
						ProjectApplication.getActiveInstance().showUnableToOpenProjectMessageDialog( file, false );
						zipFile = null;
					}
				}
				if( zipFile != null ) {
					try {
						return IoUtilities.readProject( zipFile );
					} catch( VersionNotSupportedException vnse ) {
						ProjectApplication.getActiveInstance().handleVersionNotSupported( file, vnse );
					} catch( IOException ioe ) {
						if( isWorthyOfException ) {
							throw new RuntimeException( file.getAbsolutePath(), ioe );
						} else {
							ProjectApplication.getActiveInstance().showUnableToOpenProjectMessageDialog( file, true );
						}
					}
				} else {
					//actionContext.cancel();
				}
			}
		} else {
			ProjectApplication.getActiveInstance().showUnableToOpenFileDialog( file, "It does not exist." );
		}
		return null;
	}

	private final File file;
}
