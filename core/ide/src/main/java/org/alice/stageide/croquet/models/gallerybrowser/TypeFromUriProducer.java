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
package org.alice.stageide.croquet.models.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class TypeFromUriProducer extends UriCreator<org.lgna.project.ast.NamedUserType> {
	private static class SingletonHolder {
		private static TypeFromUriProducer instance = new TypeFromUriProducer();
	}

	public static TypeFromUriProducer getInstance() {
		return SingletonHolder.instance;
	}

	private TypeFromUriProducer() {
		super( java.util.UUID.fromString( "4ab159a0-7fee-4c0f-8b71-25591fda2b0d" ) );
	}

	@Override
	protected String getExtension() {
		return org.lgna.project.io.IoUtilities.TYPE_EXTENSION;
	}

	private static void showMessageDialog( java.io.File file, boolean isValidZip ) {
		String applicationName = org.alice.ide.IDE.getApplicationName();
		StringBuffer sb = new StringBuffer();
		sb.append( "Unable to create instance from file " );
		sb.append( edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file ) );
		sb.append( ".\n\n" );
		sb.append( applicationName );
		sb.append( " is able to create instances from class files saved by " );
		sb.append( applicationName );
		sb.append( ".\n\nLook for files with an " );
		sb.append( org.lgna.project.io.IoUtilities.TYPE_EXTENSION );
		sb.append( " extension." );
		new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( sb.toString() )
				.title( "Cannot read file" )
				.messageType( edu.cmu.cs.dennisc.javax.swing.option.MessageType.ERROR )
				.buildAndShow();
	}

	@Override
	protected org.lgna.project.ast.NamedUserType internalGetValueFrom( java.io.File file ) {
		final java.util.Locale locale = java.util.Locale.ENGLISH;
		String lcName = file.getName().toLowerCase( locale );
		if( lcName.endsWith( ".a2c" ) ) {
			new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( "Alice3 does not load Alice2 characters" )
					.title( "Incorrect File Type" )
					.messageType( edu.cmu.cs.dennisc.javax.swing.option.MessageType.ERROR )
					.buildAndShow();
		} else if( lcName.endsWith( org.lgna.project.io.IoUtilities.PROJECT_EXTENSION.toLowerCase( locale ) ) ) {
			new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( file.getAbsolutePath() + " appears to be a project file and not a class file.\n\nLook for files with an " + org.lgna.project.io.IoUtilities.TYPE_EXTENSION + " extension." )
					.title( "Incorrect File Type" )
					.messageType( edu.cmu.cs.dennisc.javax.swing.option.MessageType.ERROR )
					.buildAndShow();
		} else {
			boolean isWorthyOfException = lcName.endsWith( org.lgna.project.io.IoUtilities.TYPE_EXTENSION.toLowerCase( locale ) );
			java.util.zip.ZipFile zipFile;
			try {
				zipFile = new java.util.zip.ZipFile( file );
			} catch( java.io.IOException ioe ) {
				if( isWorthyOfException ) {
					throw new RuntimeException( file.getAbsolutePath(), ioe );
				} else {
					showMessageDialog( file, false );
					zipFile = null;
				}
			}
			if( zipFile != null ) {
				org.lgna.project.ast.NamedUserType type;
				try {
					org.lgna.project.io.TypeResourcesPair typeResourcesPair = org.lgna.project.io.IoUtilities.readType( zipFile );
					type = typeResourcesPair.getType();
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO: add in resources" );
				} catch( org.lgna.project.VersionNotSupportedException vnse ) {
					type = null;
					org.alice.ide.ProjectApplication.getActiveInstance().handleVersionNotSupported( file, vnse );
				} catch( java.io.IOException ioe ) {
					if( isWorthyOfException ) {
						throw new RuntimeException( file.getAbsolutePath(), ioe );
					} else {
						showMessageDialog( file, true );
						type = null;
					}
				}
				return type;
			}
		}
		return null;
	}

	@Override
	protected java.io.File getInitialDirectory() {
		return org.alice.ide.croquet.models.ui.preferences.UserTypesDirectoryState.getInstance().getDirectoryEnsuringExistance();
	}
}
