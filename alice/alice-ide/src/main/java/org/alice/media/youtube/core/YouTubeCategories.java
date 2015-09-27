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
package org.alice.media.youtube.core;

/**
 * @author Dennis Cosgrove
 */
public class YouTubeCategories {
	private YouTubeCategories() {
		throw new AssertionError();
	}

	/**
	 * borrowed from dave: YouTubeMeddiaGroupEditorPanel and then from matt
	 */
	private static final String CATEGORY_URL = "http://gdata.youtube.com/schemas/2007/categories.cat";
	private static final String TERM_STRING = "term='";
	private static final String DEPRECATED_STRING = "deprecated";
	private static final String LABEL_STRING = "label='";
	//	private static final String TERM_PATTERN = "term='[^']*'";
	//	private static final String LABEL_PATTERN = "label='[^']*'";
	//	private static final String[] DEFAULT_TAGS = { "alice", "alice3" };
	//	private static final String DEFAULT_CATEGORY = "tech";
	private static java.util.List<String> categoryStrings;
	private static java.util.List<String> termStrings;

	private static void initializeCategoriesAndTerms() throws java.io.IOException {
		java.net.URL categoryURL = new java.net.URL( CATEGORY_URL );
		java.io.InputStream is = categoryURL.openStream();
		StringBuilder sb = new StringBuilder();
		int readValue;
		while( ( readValue = is.read() ) != -1 ) {
			char charVal = (char)readValue;
			sb.append( charVal );
		}
		String categoryData = sb.toString();
		java.util.List<String> labels = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		java.util.List<String> terms = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		int TERM_LENGTH = TERM_STRING.length();
		int LABEL_LENGTH = LABEL_STRING.length();
		int DEPRECATED_LENGTH = DEPRECATED_STRING.length();
		int categoryLength = categoryData.length();
		String searchTerm = TERM_STRING;
		int searchLength = TERM_LENGTH;
		for( int i = 0; i < ( categoryLength - DEPRECATED_LENGTH ); ) {
			if( categoryData.subSequence( i, i + searchLength ).equals( searchTerm ) ) {
				int endIndex = categoryData.indexOf( "'", i + searchLength );
				String foundString = categoryData.substring( i + searchLength, endIndex );
				foundString = foundString.replace( "&amp;", "&" );
				if( searchTerm == TERM_STRING ) {
					searchTerm = LABEL_STRING;
					searchLength = LABEL_LENGTH;
					terms.add( foundString );
				} else {
					searchTerm = TERM_STRING;
					searchLength = TERM_LENGTH;
					labels.add( foundString );
				}
				i = endIndex;
			} else if( categoryData.subSequence( i, i + DEPRECATED_LENGTH ).equals( DEPRECATED_STRING ) ) {
				if( terms.size() == labels.size() ) {
					terms.remove( terms.size() - 1 );
					labels.remove( labels.size() - 1 );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( terms.size(), labels.size() );
				}
				i += DEPRECATED_LENGTH;
			} else {
				i++;
			}
		}
		categoryStrings = labels;
		termStrings = terms;
	}

	private static void initializeCategoriesAndTermsIfNecessary() {
		if( categoryStrings != null ) {
			//pass
		} else {
			try {
				initializeCategoriesAndTerms();
			} catch( Throwable t ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( t );
				categoryStrings = java.util.Collections.emptyList();
				termStrings = java.util.Collections.emptyList();
			}
		}

	}

	public static String[] getCategories() {
		initializeCategoriesAndTermsIfNecessary();
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( categoryStrings, String.class );
	}
}
