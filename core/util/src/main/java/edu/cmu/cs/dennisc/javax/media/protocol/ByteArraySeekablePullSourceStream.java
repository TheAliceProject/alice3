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
package edu.cmu.cs.dennisc.javax.media.protocol;

/**
 * @author Dennis Cosgrove
 */
public class ByteArraySeekablePullSourceStream implements javax.media.protocol.PullSourceStream, javax.media.protocol.Seekable {
	private static final javax.media.protocol.ContentDescriptor RAW_CONTENT_DISCRIPTOR = new javax.media.protocol.ContentDescriptor( javax.media.protocol.ContentDescriptor.RAW );
	private byte[] data;
	private long location;

	public ByteArraySeekablePullSourceStream( byte[] data ) {
		this.data = data;
		this.location = 0;
	}

	@Override
	public int read( byte[] buffer, int offset, int length ) throws java.io.IOException {
		long bytesLeft = ( this.data.length - this.location );
		if( bytesLeft == 0 ) {
			return -1;
		}
		int intBytesLeft = (int)bytesLeft;
		int toRead = length;
		if( intBytesLeft < length ) {
			toRead = intBytesLeft;
		}
		System.arraycopy( this.data, (int)this.location, buffer, offset, toRead );
		this.location = this.location + toRead;
		return toRead;
	}

	@Override
	public Object getControl( String controlType ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "ByteArraySeekablePullSourceStream getControl", controlType );
		return null;
	}

	@Override
	public Object[] getControls() {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "ByteArraySeekablePullSourceStream getControls" );
		return null;
	}

	@Override
	public javax.media.protocol.ContentDescriptor getContentDescriptor() {
		return RAW_CONTENT_DISCRIPTOR;
	}

	@Override
	public boolean endOfStream() {
		return ( this.location == this.data.length );
	}

	@Override
	public long getContentLength() {
		return this.data.length;
	}

	@Override
	public boolean willReadBlock() {
		return endOfStream();
	}

	@Override
	public boolean isRandomAccess() {
		return true;
	}

	@Override
	public long seek( long where ) {
		if( where > this.data.length ) {
			this.location = this.data.length;
		} else {
			this.location = where;
		}
		return this.location;
	}

	@Override
	public long tell() {
		return this.location;
	}
}
