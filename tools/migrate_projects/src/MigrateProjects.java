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

/**
 * @author Dennis Cosgrove
 */
public class MigrateProjects extends Batch {
	private static final int WIDTH = 200;
	private static final int HEIGHT = 150 + 40;
	private int x = 0;
	private int y = 0;
	
	private final java.util.List<Error> errors = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	
	@Override
	protected void handle( java.io.File inFile, java.io.File outFile ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( inFile );
		try {
			org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( inFile );

			final boolean IS_DISPLAY_DESIRED = true;
			if( IS_DISPLAY_DESIRED ) {
				javax.swing.JFrame frame = new javax.swing.JFrame();
				frame.setLocation( x, y );
				frame.setSize( WIDTH, HEIGHT );
				x += WIDTH;
				if( x > 1600 ) {
					x = 0;
					y += HEIGHT;
				}
				frame.setVisible( true );
				
				org.alice.stageide.program.RunProgramContext runProgramContext = new org.alice.stageide.program.RunProgramContext( project.getProgramType() );
				runProgramContext.initializeInContainer( frame.getContentPane() );
				runProgramContext.setActiveScene();
				runProgramContext.cleanUpProgram();
				frame.dispose();
			}
			
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( project );
		} catch( org.lgna.project.VersionNotSupportedException vnse ) {
			throw new RuntimeException( inFile.toString(), vnse );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( inFile.toString(), ioe );
		} catch( Error e ) {
			errors.add( e );
			e.printStackTrace();
		}
	}
	@Override
	protected boolean isSkipExistingOutFilesDesirable() {
		return false;
	}
	public static void main( String[] args ) {
		MigrateProjects migrateProjects = new MigrateProjects();
		String inRootPath = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory() + "/GalleryTest/3.1.33.1.0";
		String outRootPath = inRootPath + "_FixedTo_" + org.lgna.project.Version.getCurrentVersionText();
		String ext = "a3p";
		migrateProjects.process( inRootPath, outRootPath, ext, ext );
		for( Error error : migrateProjects.errors ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( error );
		}
	}
}
