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
package org.lgna.project.migration;

/**
 * @author Dennis Cosgrove
 */
public class TextMigration extends AbstractMigration {
	private static final boolean IS_SANITY_CHECKING_DESIRED = edu.cmu.cs.dennisc.java.lang.SystemUtilities.getBooleanProperty( "org.lgna.project.migration.TextMigration.isSanityCheckingDesired", false );

	private static class Pair {
		private final java.util.regex.Pattern pattern;
		private final String replacement;

		public Pair( String regex, String replacement ) {
			this.pattern = java.util.regex.Pattern.compile( regex );
			this.replacement = replacement;
		}

		public String migrate( String source ) {
			java.util.regex.Matcher matcher = this.pattern.matcher( source );
			if( matcher.find() ) {
				//todo?
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "replace all", this.pattern, this.replacement );
				matcher.reset();
				String rv = matcher.replaceAll( this.replacement );
				//				java.util.regex.Matcher postMatcher = this.pattern.matcher( rv );
				//				assert postMatcher.find() == false : rv;
				//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( rv );
				return rv;
			} else {
				return source;
			}
		}

		public boolean isPatternEqual( Pair other ) {
			return this.pattern.toString().equals( other.pattern.toString() );
		}

		public boolean isReplacementEqual( Pair other ) {
			return edu.cmu.cs.dennisc.java.util.Objects.equals( this.replacement, other.replacement );
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append( "Pair[pattern=" );
			sb.append( this.pattern );
			sb.append( ";replacement=" );
			sb.append( this.replacement );
			sb.append( "]" );
			return sb.toString();
		}
	}

	private final Pair[] pairs;

	public TextMigration( org.lgna.project.Version minimumVersion, org.lgna.project.Version resultVersion, String... values ) {
		super( minimumVersion, resultVersion );
		assert ( values.length % 2 ) == 0 : values.length;
		this.pairs = new Pair[ values.length / 2 ];
		for( int i = 0; i < this.pairs.length; i++ ) {
			this.pairs[ i ] = new Pair( values[ i * 2 ], values[ ( i * 2 ) + 1 ] );
		}
		if( IS_SANITY_CHECKING_DESIRED ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "sanity checking " + this.pairs.length );
			for( int i = 0; i < this.pairs.length; i++ ) {
				for( int j = i + 1; j < this.pairs.length; j++ ) {
					if( this.pairs[ i ].isPatternEqual( this.pairs[ j ] ) ) {
						if( this.pairs[ i ].isReplacementEqual( this.pairs[ j ] ) ) {
							edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "duplicate", i, j, this.pairs[ i ] );
						} else {
							edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "severe problem", i, j, this.pairs[ i ], this.pairs[ j ] );
						}
					}
				}
			}
		}
	}

	public String migrate( String source ) {
		for( Pair pair : this.pairs ) {
			source = pair.migrate( source );
		}
		return source;
	}
}
