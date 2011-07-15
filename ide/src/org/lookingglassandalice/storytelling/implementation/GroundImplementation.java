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

package org.lookingglassandalice.storytelling.implementation;

/**
 * @author Dennis Cosgrove
 */
public class GroundImplementation extends ModelImplementation {
	private final org.lookingglassandalice.storytelling.Ground abstraction;
	private org.lookingglassandalice.storytelling.Ground.Appearance appearance = null;
	public GroundImplementation( org.lookingglassandalice.storytelling.Ground abstraction ) {
		this.abstraction = abstraction;
		
		edu.cmu.cs.dennisc.scenegraph.QuadArray plane = new edu.cmu.cs.dennisc.scenegraph.QuadArray();
		
		double xzMin = -10.0;
		double xzMax = +10.0;
		double y = -1.0;

		float i = 0.0f;
		float j = 1.0f;
		float k = 0.0f;
		
		float uvMin = -1.0f;
		float uvMax = +1.0f;

		plane.vertices.setValue(
				new edu.cmu.cs.dennisc.scenegraph.Vertex[] {
						edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( xzMin, y, xzMax, i, j, k, uvMin, uvMax ),
						edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( xzMax, y, xzMax, i, j, k, uvMax, uvMax ),
						edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( xzMax, y, xzMin, i, j, k, uvMax, uvMin ),
						edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( xzMin, y, xzMin, i, j, k, uvMin, uvMin )
				}
		);
		
		this.getSgVisuals()[ 0 ].geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { plane } );
	}
	@Override
	public org.lookingglassandalice.storytelling.Ground getAbstraction() {
		return this.abstraction;
	}
	public org.lookingglassandalice.storytelling.Ground.Appearance getAppearance() {
		return this.appearance;
	}
	public void setAppearance( org.lookingglassandalice.storytelling.Ground.Appearance appearance ) {
		if( this.appearance != null ) {
			
		}
		this.appearance = appearance;
		edu.cmu.cs.dennisc.texture.BufferedImageTexture diffuseColorTexture;
		if( this.appearance != null ) {
			//todo
			java.net.URL resource = this.appearance.getResource();
			try {
				java.awt.image.BufferedImage bufferedImage = javax.imageio.ImageIO.read( resource );
				diffuseColorTexture = new edu.cmu.cs.dennisc.texture.BufferedImageTexture();
				diffuseColorTexture.setBufferedImage( bufferedImage );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		} else {
			diffuseColorTexture = null;
		}
		this.setDiffuseColorTexture( diffuseColorTexture );
	}
	
}
