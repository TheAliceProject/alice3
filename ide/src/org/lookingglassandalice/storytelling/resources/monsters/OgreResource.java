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

package org.lookingglassandalice.storytelling.resources.monsters;

/**
 * @author Dennis Cosgrove
 */
public class OgreResource implements org.lookingglassandalice.storytelling.resources.AdultPersonResource {
    private static java.io.File s_galleryRootDirectory;
    private static final String CHILD_NAME = "assets";
    private static final String GRANDCHILD_NAME = "org.alice.apis.stage";
    static {
        s_galleryRootDirectory = org.alice.apis.moveandturn.gallery.GalleryRootUtilities.calculateGalleryRootDirectory( org.alice.apis.moveandturn.gallery.GalleryModel.class, "/Alice3Beta/gallery", "gallery", "assets", "org.alice.apis.stage", "Alice Move & Turn Gallery", "Alice" );
    }
    public static java.io.File getGalleryRootDirectory() {
        return s_galleryRootDirectory;
    }

    private final edu.cmu.cs.dennisc.resource.SkeletonModelResource skeletonModelResource;
    public OgreResource() {
        java.io.File directory = new java.io.File( new java.io.File( s_galleryRootDirectory, CHILD_NAME ), GRANDCHILD_NAME );
        java.io.File file = new java.io.File( directory, this.getResourceString()+".alice" );
        assert file.exists();
        try {
            java.io.InputStream is = new java.io.FileInputStream(file);
            edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( is );
            this.skeletonModelResource = decoder.decodeReferenceableBinaryEncodableAndDecodable( new java.util.HashMap< Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable >() );
        } catch( java.io.FileNotFoundException fnfe ) {
        	throw new RuntimeException( fnfe );
        }
	}
	protected String getResourceString()
	{
		return "ogre";
	}

	
	public org.lookingglassandalice.storytelling.implementation.PersonImplementation createPersonImplementation( org.lookingglassandalice.storytelling.Person abstraction ) {
		edu.cmu.cs.dennisc.resource.SkeletonModelResource sgCopy = this.skeletonModelResource;
		return new org.lookingglassandalice.storytelling.implementation.monsters.MonsterImplementation( sgCopy, abstraction, this );
	}
}
