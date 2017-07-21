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
package edu.wustl.lookingglass.media;

/**
 * @author Dennis Cosgrove
 */
public class FFmpegCodec {
	private final boolean isDecodingSupported;
	private final boolean isEncodingSupported;
	private final boolean isVideo;
	private final boolean isAudio;
	private final boolean isSubtitle;
	private final boolean isIntraFrameOnly;
	private final boolean isLossy;
	private final boolean isLossless;
	private final String name;
	private final String description;

	public FFmpegCodec( String line ) {
		String[] split = line.split( " ", 3 );
		String mask = split[ 0 ];
		this.isDecodingSupported = mask.charAt( 0 ) == 'D';
		this.isEncodingSupported = mask.charAt( 1 ) == 'E';
		this.isVideo = mask.charAt( 2 ) == 'V';
		this.isAudio = mask.charAt( 2 ) == 'A';
		this.isSubtitle = mask.charAt( 2 ) == 'S';
		this.isIntraFrameOnly = mask.charAt( 3 ) == 'I';
		this.isLossy = mask.charAt( 4 ) == 'L';
		this.isLossless = mask.charAt( 5 ) == 'S';
		this.name = split[ 1 ];
		if( split.length == 3 ) {
			this.description = split[ 2 ].trim();
		} else {
			this.description = null;
		}
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public boolean isDecodingSupported() {
		return this.isDecodingSupported;
	}

	public boolean isEncodingSupported() {
		return this.isEncodingSupported;
	}

	public boolean isAudio() {
		return this.isAudio;
	}

	public boolean isVideo() {
		return this.isVideo;
	}

	public boolean isSubtitle() {
		return this.isSubtitle;
	}

	public boolean isIntraFrameOnly() {
		return this.isIntraFrameOnly;
	}

	public boolean isLossy() {
		return this.isLossy;
	}

	public boolean isLossless() {
		return this.isLossless;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		sb.append( this.name );
		sb.append( "," );
		sb.append( this.description );

		if( this.isDecodingSupported ) {
			sb.append( ", decode" );
		}
		if( this.isEncodingSupported ) {
			sb.append( ", encode" );
		}
		if( this.isVideo ) {
			sb.append( ", video" );
		}
		if( this.isAudio ) {
			sb.append( ", audio" );
		}
		if( this.isSubtitle ) {
			sb.append( ", subtitle" );
		}

		if( this.isIntraFrameOnly ) {
			sb.append( ", intraFrameOnly" );
		}
		if( this.isLossy ) {
			sb.append( ", lossy" );
		}
		if( this.isLossless ) {
			sb.append( ", lossless" );
		}

		sb.append( "]" );
		return sb.toString();
	}
}
