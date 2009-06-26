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

package edu.cmu.cs.dennisc.issue;

/**
 * @author Dennis Cosgrove
 */
public abstract class Issue extends edu.cmu.cs.dennisc.lang.AbstractObjectWithRevealingToString {
	public enum Type {
		BUG, NEW_FEAURE, IMPROVEMENT
	}

	private Type type;
	private String summary;
	private String description;
	private String steps;
	private Throwable throwable;
	private String[] affectsVersions = new String[] {};
	private java.util.List< edu.cmu.cs.dennisc.issue.Attachment > attachments = new java.util.LinkedList< edu.cmu.cs.dennisc.issue.Attachment >();

	public Type getType() {
		return type;
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
//	public String getEnvironment() {
//		return this.environment;
//	}
//	public void setEnvironment( String environment ) {
//		this.environment = environment;
//	}
	public Throwable getThrowable() {
		return this.throwable;
	}
	public void setThrowable( Throwable throwable ) {
		this.throwable = throwable;
	}
	
	public String[] getAffectsVersions() {
		return this.affectsVersions;
	}
	public void setAffectsVersions( String... affectsVersions ) {
		this.affectsVersions = affectsVersions;
	}
	
	
	
	public String getExceptionText() {
		if( this.throwable != null ) {
			return edu.cmu.cs.dennisc.lang.ThrowableUtilities.getStackTraceAsString( this.throwable );
		} else {
			return "";
		}
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

	public abstract String getMailSubject();
	public abstract String getMailBody();
	public abstract String getJIRASummary();

	@Override
	protected StringBuffer updateRepr( StringBuffer rv ) {
		rv.append( "type=" );
		rv.append( this.getType() );
		rv.append( ";summary=" );
		rv.append( this.getSummary() );
		rv.append( ";description=" );
		rv.append( this.getDescription() );
		rv.append( ";steps=" );
		rv.append( this.getSteps() );
		rv.append( ";throwable=" );
		rv.append( this.getThrowable() );
		
		//todo: attachments
		
		return rv;
	}
}
