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

package org.alice.virtualmachine.event;

/**
 * @author Dennis Cosgrove
 */
public class ResourceContentEvent extends edu.cmu.cs.dennisc.pattern.event.Event< org.alice.virtualmachine.Resource > {
	private String contentType;
	private byte[] data;
	public ResourceContentEvent( org.alice.virtualmachine.Resource source, String contentType, byte[] data ) {
		super( source );
		this.contentType = contentType;
		this.data = data;
	}
	public String getContentType() {
		return this.contentType;
	}
	public byte[] getData() {
		return this.data;
	}
}
