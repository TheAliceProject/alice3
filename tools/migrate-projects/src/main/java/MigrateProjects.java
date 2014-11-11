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

	private void outputProblematicFiles() {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "problematic files:", this.problematicFiles.size() );
		for( java.io.File problematicFile : this.problematicFiles ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( problematicFile );
		}

		edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "problematic java fields:", this.problematicJavaFields.size() );
		for( org.lgna.project.ast.JavaField javaField : this.problematicJavaFields ) {
			org.lgna.project.ast.FieldReflectionProxy fieldReflectionProxy = javaField.getFieldReflectionProxy();
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( fieldReflectionProxy.getDeclaringClassReflectionProxy().getName() + " " + fieldReflectionProxy.getName() );
		}
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln();
	}

	@Override
	protected void handle( java.io.File inFile, java.io.File outFile ) {
		if( edu.cmu.cs.dennisc.java.util.logging.Logger.getLevel().intValue() < java.util.logging.Level.SEVERE.intValue() ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( inFile );
		}
		try {
			org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( inFile );

			final boolean IS_DISPLAY_DESIRED = false;
			if( IS_DISPLAY_DESIRED ) {
				final javax.swing.JFrame frame = new javax.swing.JFrame();
				frame.setLocation( x, y );
				frame.setSize( WIDTH, HEIGHT );
				x += WIDTH;
				if( x > 1600 ) {
					x = 0;
					y += HEIGHT;
				}
				frame.setVisible( true );

				org.alice.stageide.program.RunProgramContext runProgramContext = new org.alice.stageide.program.RunProgramContext( project.getProgramType() );
				runProgramContext.initializeInContainer( new org.lgna.story.implementation.ProgramImp.AwtContainerInitializer() {
					@Override
					public void addComponents( edu.cmu.cs.dennisc.render.OnscreenRenderTarget<?> onscreenRenderTarget, javax.swing.JPanel controlPanel ) {
						frame.getContentPane().add( onscreenRenderTarget.getAwtComponent() );
					}
				} );
				runProgramContext.setActiveScene();
				runProgramContext.cleanUpProgram();
				frame.dispose();
			}

			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.FieldAccess> crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.FieldAccess.class );
			project.getProgramType().crawl( crawler, org.lgna.project.ast.CrawlPolicy.COMPLETE );
			for( org.lgna.project.ast.FieldAccess node : crawler.getList() ) {
				if( node.isValid() ) {
					//pass
				} else {
					org.lgna.project.ast.AbstractField field = node.field.getValue();
					if( field instanceof org.lgna.project.ast.JavaField ) {
						org.lgna.project.ast.JavaField javaField = (org.lgna.project.ast.JavaField)field;
						this.problematicJavaFields.add( javaField );
					} else {
						assert false : node;
					}
				}
			}

			final boolean IS_WRITE_DESIRED = true;
			if( IS_WRITE_DESIRED ) {
				java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile( inFile );
				java.util.zip.ZipEntry zipEntry = zipFile.getEntry( "thumbnail.png" );
				edu.cmu.cs.dennisc.java.util.zip.DataSource[] dataSources;
				if( zipEntry != null ) {
					java.io.InputStream is = zipFile.getInputStream( zipEntry );
					java.awt.Image image = edu.cmu.cs.dennisc.image.ImageUtilities.read( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, is );
					final byte[] data = edu.cmu.cs.dennisc.image.ImageUtilities.writeToByteArray( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, image );
					dataSources = new edu.cmu.cs.dennisc.java.util.zip.DataSource[] { new edu.cmu.cs.dennisc.java.util.zip.DataSource() {
						@Override
						public String getName() {
							return "thumbnail.png";
						}

						@Override
						public void write( java.io.OutputStream os ) throws java.io.IOException {
							os.write( data );
						}
					} };
				} else {
					dataSources = new edu.cmu.cs.dennisc.java.util.zip.DataSource[] {};
				}
				org.lgna.project.io.IoUtilities.writeProject( outFile, project, dataSources );
			}

			if( edu.cmu.cs.dennisc.java.util.logging.Logger.getLevel().intValue() < java.util.logging.Level.SEVERE.intValue() ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "success", inFile );
			}
		} catch( org.lgna.project.VersionNotSupportedException vnse ) {
			throw new RuntimeException( inFile.toString(), vnse );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( inFile.toString(), ioe );
		} catch( Throwable t ) {
			problematicFiles.add( inFile );
			this.outputProblematicFiles();
			t.printStackTrace();
		}
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
	}

	@Override
	protected boolean isSkipExistingOutFilesDesirable() {
		return false;
	}

	private int x = 0;
	private int y = 0;

	private final java.util.List<java.io.File> problematicFiles = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private final java.util.List<org.lgna.project.ast.JavaField> problematicJavaFields = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	public static void main( String[] args ) {
		String inRootPath;
		String outRootPath;
		switch( args.length ) {
		case 0:
			org.lgna.project.Version prevVersion = new org.lgna.project.Version( "3.1.92.0.0" );
			inRootPath = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory() + "/GalleryTest/" + prevVersion;
			outRootPath = inRootPath + "_FixedTo_" + org.lgna.project.ProjectVersion.getCurrentVersionText();
			break;
		case 1:
			inRootPath = args[ 0 ];
			outRootPath = inRootPath + "_migrated";
			break;
		case 2:
			inRootPath = args[ 0 ];
			outRootPath = args[ 1 ];
			break;
		default:
			throw new RuntimeException( java.util.Arrays.toString( args ) );
		}
		String ext = "a3p";
		MigrateProjects migrateProjects = new MigrateProjects();
		migrateProjects.process( inRootPath, outRootPath, ext, ext );
		migrateProjects.outputProblematicFiles();
		System.exit( 0 );
	}
}
