/*
 * Copyright (c) 2006-2014, Carnegie Mellon University. All rights reserved.
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
public class AudioTest {
	public static void main( String[] args ) throws Exception {
		java.io.File root = org.lgna.story.implementation.StoryApiDirectoryUtilities.getSoundGalleryDirectory();
		//root = new java.io.File( root, "Sound Effects" );
		java.io.File[] files = edu.cmu.cs.dennisc.java.io.FileUtilities.listDescendants( root, (java.io.FileFilter)null );
		edu.cmu.cs.dennisc.media.jmf.MediaFactory mediaFactory = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton();
		for( java.io.File file : files ) {
			if( file.isDirectory() ) {
				//pass
			} else {
				String contentType = org.lgna.common.resources.AudioResource.getContentType( file );
				if( contentType != null ) {
					//org.lgna.common.resources.AudioResource audioResource = new org.lgna.common.resources.AudioResource( file, contentType );
					//edu.cmu.cs.dennisc.media.Player player = mediaFactory.createPlayer( audioResource );
					//player.prefetch();
					org.lgna.common.resources.AudioResource audioResource = mediaFactory.createAudioResource( file );
					javax.media.protocol.DataSource dataSource = new edu.cmu.cs.dennisc.javax.media.protocol.ByteArrayDataSource( audioResource.getData(), audioResource.getContentType() );
					javax.media.Player jmfPlayer = javax.media.Manager.createPlayer( dataSource );
					jmfPlayer.addControllerListener( new javax.media.ControllerListener() {
						@Override
						public void controllerUpdate( javax.media.ControllerEvent e ) {
							edu.cmu.cs.dennisc.java.util.logging.Logger.outln( e );
						}
					} );
					jmfPlayer.realize();
					jmfPlayer.start();
					edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 2000 );
					jmfPlayer.stop();
					jmfPlayer.deallocate();
					edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 1000 );
					System.gc();
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( Runtime.getRuntime().freeMemory() );
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( file );
				}
			}
		}
	}
}
