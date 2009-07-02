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

package edu.cmu.cs.dennisc.jira;

/**
 * @author Dennis Cosgrove
 */
public class JIRAReport {
	public enum Type {
		BUG, NEW_FEAURE, IMPROVEMENT
	}
	private String projectKey;
	private Type type;
	private String summary;
	private String description;
	private String steps;
	private String environment;
	private String exception;
	private String[] affectsVersions = new String[] {};
	private java.util.List< edu.cmu.cs.dennisc.issue.Attachment > attachments = new java.util.LinkedList< edu.cmu.cs.dennisc.issue.Attachment >();
	public String getProjectKey() {
		return this.projectKey;
	}
	public void setProjectKey( String projectKey ) {
		this.projectKey = projectKey;
	}

	public Type getType() {
		return this.type;
	}
	public void setType( Type type ) {
		this.type = type;
	}
	public String getSummary() {
		return this.summary;
	}
	public void setSummary( String summary ) {
		this.summary = summary;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription( String description ) {
		this.description = description;
	}
	public String getSteps() {
		return this.steps;
	}
	public void setSteps( String steps ) {
		this.steps = steps;
	}
	public String getEnvironment() {
		return this.environment;
	}
	public void setEnvironment( String environment ) {
		this.environment = environment;
	}
	public String[] getAffectsVersions() {
		return this.affectsVersions;
	}
	public void setAffectsVersions( String... affectsVersions ) {
		this.affectsVersions = affectsVersions;
	}
	public String getException() {
		return this.exception;
	}
	public void setException( String exception ) {
		this.exception = exception;
	}

	public String getAffectsVersionText() {
		String rv;
		if( this.affectsVersions != null && this.affectsVersions.length > 0 ) {
			rv = this.affectsVersions[ 0 ];
		} else {
			rv = null;
		}
		if( rv != null ) {
			//pass
		} else {
			rv = "";
		}
		return rv;
	}
	
	public void addAttachment( edu.cmu.cs.dennisc.issue.Attachment attachment ) {
		this.attachments.add( attachment );
	}
	public void removeAttachment( edu.cmu.cs.dennisc.issue.Attachment attachment ) {
		this.attachments.remove( attachment );
	}
	public java.util.List< edu.cmu.cs.dennisc.issue.Attachment > getAttachments() {
		return this.attachments;
	}
}
