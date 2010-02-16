/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
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
	public int read( byte[] buffer, int offset, int length ) throws java.io.IOException {
		long bytesLeft = (this.data.length - this.location);
		if( bytesLeft == 0 ) {
			return -1;
		}
		int intBytesLeft = (int)bytesLeft;
		int toRead = length;
		if( intBytesLeft < length )
			toRead = intBytesLeft;
		System.arraycopy( this.data, (int)this.location, buffer, offset, toRead );
		this.location = this.location + toRead;
		return toRead;
	}
	public Object getControl( String controlType ) {
		return null;
	}
	public Object[] getControls() {
		return null;
	}
	public javax.media.protocol.ContentDescriptor getContentDescriptor() {
		return RAW_CONTENT_DISCRIPTOR;
	}
	public boolean endOfStream() {
		return (this.location == this.data.length);
	}
	public long getContentLength() {
		return this.data.length;
	}
	public boolean willReadBlock() {
		return endOfStream();
	}
	public boolean isRandomAccess() {
		return true;
	}
	public long seek( long where ) {
		if( where > this.data.length ) {
			this.location = this.data.length;
		} else {
			this.location = where;
		}
		return this.location;
	}
	public long tell() {
		return this.location;
	}
}
