/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.localize.review.core;

/**
 * @author Dennis Cosgrove
 */
public final class Item {
	private static final String ZANATA_PROJECT_NAME = "alice";
	private static final String ZANATA_DOC_NAME = "org.alice";

	public Item( String projectName, String bundleName, String key, String defaultValue ) {
		this.projectName = projectName;
		this.bundleName = bundleName;
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public java.net.URI createUri( String localeTag ) {
		StringBuilder sb = new StringBuilder();

		sb.append( "https://translate.zanata.org/zanata/webtrans/Application.seam?project=" );
		sb.append( ZANATA_PROJECT_NAME );
		sb.append( "&iteration=master" );

		sb.append( "&localeId=" );
		sb.append( localeTag );

		sb.append( "#view:doc;doc:" );
		sb.append( ZANATA_DOC_NAME );
		sb.append( "/" );
		sb.append( this.projectName );
		sb.append( "/java/" );
		sb.append( bundleName );

		try {
			return new java.net.URI( sb.toString() );
		} catch( java.net.URISyntaxException urise ) {
			throw new RuntimeException( sb.toString(), urise );
		}
	}

	public String getProjectName() {
		return this.projectName;
	}

	public String getBundleName() {
		return this.bundleName;
	}

	public String getKey() {
		return this.key;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public String getLocalizedValue() {
		return this.localizedValue;
	}

	public void setLocalizedValue( String localizedValue ) {
		this.localizedValue = localizedValue;
	}

	public java.util.List<String> getDefaultValueTags() {
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile( "</.*/>" );
		java.util.regex.Matcher matcher = pattern.matcher( this.defaultValue );

		java.util.List<String> tags = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		while( matcher.find() ) {
			tags.add( matcher.group( 0 ) );
		}
		return java.util.Collections.unmodifiableList( tags );
	}

	private final String projectName;
	private final String bundleName;
	private final String key;
	private final String defaultValue;
	private String localizedValue;
}
