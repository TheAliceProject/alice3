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
package org.alice.imageeditor.croquet;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.lang.ThreadUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.worker.WorkerWithProgress;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
/* package-private */class FilenameListWorker extends WorkerWithProgress<File[], File> {
	private static final FileFilter PNG_FILE_FILTER = FileUtilities.createFileWithExtensionFilter( ".png" );
	private static final FileFilter DIRECTORY_FILTER = FileUtilities.createDirectoryFilter();

	private final FilenameComboBoxModel filenameComboBoxModel;
	private final File rootDirectory;

	public FilenameListWorker( FilenameComboBoxModel filenameComboBoxModel, File rootDirectory ) {
		this.filenameComboBoxModel = filenameComboBoxModel;
		this.rootDirectory = rootDirectory;
	}

	public File getRootDirectory() {
		return this.rootDirectory;
	}

	private void appendDescendants( List<File> descendants, File dir ) {
		if( this.isCancelled() ) {
			//pass
		} else {
			File[] files = dir.listFiles( PNG_FILE_FILTER );
			if( ( files != null ) && ( files.length > 0 ) ) {
				for( File childFile : files ) {
					descendants.add( childFile );
				}
				this.publish( files );
				boolean IS_YIELD_BEHAVING_THE_WAY_I_EXPECT = false;
				if( IS_YIELD_BEHAVING_THE_WAY_I_EXPECT ) {
					Thread.yield();
				} else {
					ThreadUtilities.sleep( 10 );
				}
			}
			File[] dirs = dir.listFiles( DIRECTORY_FILTER );
			if( dirs != null ) {
				for( File childDir : dirs ) {
					this.appendDescendants( descendants, childDir );
				}
			}
		}
	}

	@Override
	protected File[] do_onBackgroundThread() throws Exception {
		//Thread.currentThread().setPriority( Thread.MIN_PRIORITY );
		if( this.rootDirectory.isDirectory() ) {
			List<File> descendants = Lists.newLinkedList();
			this.appendDescendants( descendants, this.rootDirectory );
			return ArrayUtilities.createArray( descendants, File.class );
		} else {
			return new File[ 0 ];
		}
	}

	@Override
	protected void handleProcess_onEventDispatchThread( List<File> files ) {
		synchronized( this.filenameComboBoxModel ) {
			this.filenameComboBoxModel.addAll( files );
		}
	}

	@Override
	protected void handleDone_onEventDispatchThread( File[] value ) {
		synchronized( this.filenameComboBoxModel ) {
			this.filenameComboBoxModel.done( value );
		}
	}
}
