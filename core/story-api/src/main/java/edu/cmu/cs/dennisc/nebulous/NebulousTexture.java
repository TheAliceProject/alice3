/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.nebulous;

import org.lgna.story.resourceutilities.StorytellingResources;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;

/**
 * @author alice
 * 
 */
public class NebulousTexture extends edu.cmu.cs.dennisc.texture.Texture {

	static {
		edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory.register( edu.cmu.cs.dennisc.nebulous.NebulousTexture.class, edu.cmu.cs.dennisc.nebulous.NebulousTextureAdapter.class );
		if( SystemUtilities.getBooleanProperty( "org.alice.ide.disableDefaultNebulousLoading", false ) )
		{
			//Don't load nebulous resources if the default loading is disabled
			//Disabling should only happen under controlled circumstances like running the model batch process
		}
		else
		{
			StorytellingResources.getInstance().loadSimsBundles();
		}
	}

	private final String m_textureKey;
	private boolean m_isMipMappingDesired = true;
	private boolean m_isPotentiallyAlphaBlended = false;

	public native void initializeIfNecessary( Object textureKey );

	public native void setup( javax.media.opengl.GL gl );

	public native void addReference();

	public native void removeReference();

	public NebulousTexture( String textureKey ) {
		m_textureKey = textureKey;
		this.initializeIfNecessary( m_textureKey );
	}

	public NebulousTexture( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
		m_textureKey = binaryDecoder.decodeString();
		this.initializeIfNecessary( m_textureKey );
	}

	public void doSetup( javax.media.opengl.GL gl ) {
		assert m_textureKey != null;
		this.initializeIfNecessary( m_textureKey );
		this.setup( gl );
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		assert m_textureKey != null;
		binaryEncoder.encode( m_textureKey );
	}

	public String getTextureKey() {
		return m_textureKey;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public boolean isMipMappingDesired() {
		return m_isMipMappingDesired;
	}

	public void setMipMappingDesired( boolean isMipMappingDesired ) {
		if( m_isMipMappingDesired != isMipMappingDesired ) {
			m_isMipMappingDesired = isMipMappingDesired;
			fireTextureChanged();
		}
	}

	@Override
	public boolean isPotentiallyAlphaBlended() {
		return m_isPotentiallyAlphaBlended;
	}

	public void setPotentiallyAlphaBlended( boolean isPotentiallyAlphaBlended ) {
		if( m_isPotentiallyAlphaBlended != isPotentiallyAlphaBlended ) {
			m_isPotentiallyAlphaBlended = isPotentiallyAlphaBlended;
			fireTextureChanged();
		}
	}

	@Override
	public int getWidth() {
		throw new RuntimeException( "NOT SUPPORTED" );
	}

	@Override
	public int getHeight() {
		throw new RuntimeException( "NOT SUPPORTED" );
	}

	@Override
	public boolean isAnimated() {
		return false;
	}

	@Override
	public edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy getMipMapGenerationPolicy() {
		return edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy.PAINT_EACH_INDIVIDUAL_LEVEL;
	}

	@Override
	public void paint( java.awt.Graphics2D g, int width, int height ) {
		throw new RuntimeException( "NOT SUPPORTED" );
	}
}
