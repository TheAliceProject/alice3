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
public class ByteArrayDataSource extends javax.media.protocol.PullDataSource {
	private byte[] data;
	private String contentType;

	public ByteArrayDataSource( byte[] data, String contentType ) {
		this.data = data;
		this.contentType = contentType;
	}
	public byte[] getData() {
		return this.data;
	}
	@Override
	public String getContentType() {
		return this.contentType;
	}
	@Override
	public javax.media.Time getDuration() {
		return javax.media.Duration.DURATION_UNKNOWN;
	}
	@Override
	public void connect() throws java.io.IOException {
	}
	@Override
	public void start() throws java.io.IOException {
	}
	@Override
	public void disconnect() {
	}
	@Override
	public Object getControl( String parm1 ) {
		return null;
	}
	@Override
	public javax.media.protocol.PullSourceStream[] getStreams() {
		return new javax.media.protocol.PullSourceStream[] { new ByteArraySeekablePullSourceStream( this.data ) };
	}
	@Override
	public void stop() throws java.io.IOException {
	}
	@Override
	public Object[] getControls() {
		return null;
	}
	
	@Override
	public java.lang.String toString() {
		return ByteArrayDataSource.class.getName() + "[contentType=" + this.contentType + "]";
	}
}
