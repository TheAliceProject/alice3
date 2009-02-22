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
public class Issue extends edu.cmu.cs.dennisc.lang.AbstractObjectWithRevealingToString {
	public enum Type {
		BUG, NEW_FEAURE, IMPROVEMENT
	}

	private Type type;
	private String summary;
	private String description;
	private String steps;
	private String environment;
	private String exception;

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
	public String getEnvironment() {
		return this.environment;
	}
	public void setEnvironment( String environment ) {
		this.environment = environment;
	}
	public String getException() {
		return this.exception;
	}
	public void setException( String exception ) {
		this.exception = exception;
	}

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
		rv.append( ";environment=" );
		rv.append( this.getEnvironment() );
		rv.append( ";exception=" );
		rv.append( this.getException() );
		return rv;
	}
}
