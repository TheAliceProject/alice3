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
public class MonsterUtilities {
	private MonsterUtilities() {
	}
	
	@Deprecated
	public static org.lookingglassandalice.storytelling.resources.PersonResource getInstance( String path ) {
		return PersonResource.getInstance( path );
	}
	
	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual decode( String path ) {
    	java.io.InputStream is = MonsterUtilities.class.getResourceAsStream( path );
    	edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( is );
    	return decoder.decodeReferenceableBinaryEncodableAndDecodable( new java.util.HashMap< Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable >() );
	}
	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual createCopy( edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgOriginal ) {
	    edu.cmu.cs.dennisc.scenegraph.Geometry[] sgGeometries = sgOriginal.geometries.getValue();
	    edu.cmu.cs.dennisc.scenegraph.WeightedMesh[] sgWeightedMeshes = sgOriginal.weightedMeshes.getValue();
		edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRoot = sgOriginal.skeleton.getValue();

	    edu.cmu.cs.dennisc.scenegraph.SkeletonVisual rv = new edu.cmu.cs.dennisc.scenegraph.SkeletonVisual();
		edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRootCopy = (edu.cmu.cs.dennisc.scenegraph.Joint)sgSkeletonRoot.newCopy();

    	rv.skeleton.setValue( sgSkeletonRootCopy );
		rv.geometries.setValue( sgGeometries );
		rv.weightedMeshes.setValue( sgWeightedMeshes );
		return rv;
	}
	public static edu.cmu.cs.dennisc.texture.Texture getTexture( edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgOriginal ) {
		edu.cmu.cs.dennisc.scenegraph.SingleAppearance sgAppearance = (edu.cmu.cs.dennisc.scenegraph.SingleAppearance)sgOriginal.frontFacingAppearance.getValue();
	    return sgAppearance.diffuseColorTexture.getValue();
	}
}
