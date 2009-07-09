/**
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
package org.alice.media;

/**
 * @author David Culyba
 */
public class YouTubeEvent {
	
	public enum EventType
	{
		UPLOAD_SUCCESS("Upload Succeeded"),
		UPLOAD_FAILED("Upload Failed"),
		UPLOAD_STARTED("Upload Started"),
		UPLOAD_CANCELLED_SUCCESS("Upload Successfully Cancelled"),
		UPLOAD_CANCELLED_FAILED("Failed to Cancel Upload"),
		LOGIN_STARTED("Login Started"),
		LOGIN_FAILED("Login Failed"),
		LOGIN_SUCCESS("Login Succeeded"),
		LINK_RETRIEVED("Link Retrieved");
		
		private String description;
		private EventType(String description)
		{
			this.description = description;
		}
		
		@Override
		public String toString()
		{
			return this.description;
		}
	}
	
	private EventType type;
	private Object moreInfo;
	
	public YouTubeEvent(EventType type)
	{
		this(type, null);
	}
	
	public YouTubeEvent(EventType type, Object moreInfo)
	{
		this.type = type;
		this.moreInfo = moreInfo;
	}

	public EventType getType()
	{
		return this.type;
	}
	
	public Object getMoreInfo()
	{
		return this.moreInfo;
	}
	
	public boolean hasMoreInfo()
	{
		return this.moreInfo != null;
	}
	
}
