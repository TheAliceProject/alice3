/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

package edu.cmu.cs.dennisc.jira;

/**
 * @author Dennis Cosgrove
 */
public class JIRAReport extends edu.cmu.cs.dennisc.issue.AbstractReport {
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
}
