
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
import org.alice.ide.localize.review.core.Item;

/**
 * @author Dennis Cosgrove
 */
public class CheckTranslations {

	private static class Mistranslation {
		public Mistranslation( Item item, java.util.List<String> englishTags, String localizedValue, java.util.List<String> localizedTags ) {
			this.item = item;
			this.englishTags = englishTags;
			this.localizedValue = localizedValue;
			this.localizedTags = localizedTags;
		}

		private final Item item;
		private final java.util.List<String> englishTags;
		private final String localizedValue;
		private final java.util.List<String> localizedTags;
	}

	public static void main( String[] args ) throws Exception {

		java.util.List<java.util.Locale> ALL_LOCALES = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( new java.util.Locale( "pt" ), new java.util.Locale( "pt", "BR" ), new java.util.Locale( "es" ), new java.util.Locale( "fr" ), new java.util.Locale( "fr", "BE" ), new java.util.Locale( "it" ), new java.util.Locale( "nl" ), new java.util.Locale( "de" ), new java.util.Locale( "el" ), new java.util.Locale( "ro" ), new java.util.Locale( "cs" ), new java.util.Locale( "sl" ), new java.util.Locale( "lt" ), new java.util.Locale( "ru" ), new java.util.Locale( "uk" ), new java.util.Locale( "tr" ), new java.util.Locale( "ar" ), new java.util.Locale( "iw" ), new java.util.Locale( "in" ), new java.util.Locale( "zh", "CN" ), new java.util.Locale( "zh", "TW" ), new java.util.Locale( "ko" ) );

		final java.util.List<java.util.Locale> locales;
		if( args.length == 0 ) {
			locales = ALL_LOCALES;
		} else {
			locales = edu.cmu.cs.dennisc.java.util.Lists.newArrayListWithInitialCapacity( args.length );
			for( String arg : args ) {
				String[] languageCountryVariant = arg.split( "," );
				java.util.Locale locale;
				switch( languageCountryVariant.length ) {
				case 1:
					locale = new java.util.Locale( languageCountryVariant[ 0 ] );
					break;
				case 2:
					locale = new java.util.Locale( languageCountryVariant[ 0 ], languageCountryVariant[ 1 ] );
					break;
				case 3:
					locale = new java.util.Locale( languageCountryVariant[ 0 ], languageCountryVariant[ 1 ], languageCountryVariant[ 2 ] );
					break;
				default:
					throw new Exception( arg );
				}
				if( ALL_LOCALES.contains( locale ) ) {
					locales.add( locale );
				} else {
					throw new Exception( locale.getDisplayName() );
				}
			}
		}

		System.out.println( "Checking locales: " );
		for( java.util.Locale l : locales ) {
			System.out.println( "  " + l.getDisplayName() );
		}

		edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<java.util.Locale, Mistranslation> mapLocaleToMistranslations = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentListHashMap();

		java.util.List<Item> items = org.alice.ide.localize.review.core.LocalizeUtils.getItems( org.alice.ide.story.AliceIde.class, "alice-ide" );
		for( Item item : items ) {
			String defaultValue = item.getDefaultValue();
			java.util.List<String> englishTags = org.alice.ide.localize.review.core.LocalizeUtils.getTags( defaultValue );
			if( englishTags.size() > 0 ) {
				//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( item.getKey(), item.getDefaultValue() );

				for( java.util.Locale locale : locales ) {

					java.util.ResourceBundle resourceBundleB = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getUtf8Bundle( item.getBundleName(), locale );
					String localizedValue = resourceBundleB.getString( item.getKey() );

					if( localizedValue.equals( defaultValue ) ) {
						//pass
					} else {
						java.util.List<Mistranslation> mistranslations = mapLocaleToMistranslations.getInitializingIfAbsentToLinkedList( locale );
						java.util.List<String> localizedTags = org.alice.ide.localize.review.core.LocalizeUtils.getTags( localizedValue );

						//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( localizedValue );
						for( String englishTag : englishTags ) {
							if( localizedTags.contains( englishTag ) ) {
								//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "***", locale, englishTag );
							} else {
								mistranslations.add( new Mistranslation( item, englishTags, localizedValue, localizedTags ) );
								break;
							}
						}
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append( "<html>" );
		sb.append( "<title>" );
		sb.append( "Alice Translations Validity Report" );
		sb.append( "</title>" );
		sb.append( "<h1>Alice Translations Validity Report</h1>" );
		for( java.util.Locale locale : locales ) {
			sb.append( "<h2>" );
			sb.append( locale.getDisplayName() );
			sb.append( "</h2>" );
			java.util.List<Mistranslation> mistranslations = mapLocaleToMistranslations.getInitializingIfAbsentToLinkedList( locale );
			for( Mistranslation mistranslation : mistranslations ) {
				sb.append( "incorrect tag(s): " );
				sb.append( "<a href=\"" );
				sb.append( mistranslation.item.createUri( "el" ) );
				sb.append( "\">" );
				sb.append( mistranslation.item.getKey() );
				sb.append( "</a><br>" );
			}
			sb.append( "<p>" );
		}

		//java.util.Date date = new java.util.Date();
		java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat();

		sb.append( "<hr>" );
		sb.append( "<em>generated: " );
		sb.append( simpleDateFormat.format( new java.util.Date() ) );
		sb.append( "</em>" );

		sb.append( "</html>" );

		java.io.File outputFile = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "latestTranslationsValidityReport.html" );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( outputFile, sb.toString() );

		System.out.println( "Translation check finished." );
		System.out.println( "Results:" );
		for( java.util.Locale locale : locales ) {
			java.util.List<Mistranslation> mistranslations = mapLocaleToMistranslations.getInitializingIfAbsentToLinkedList( locale );
			System.out.println( "  " + locale.getDisplayName() + " : " + mistranslations.size() + " errors" );
		}
		System.out.println( "Results written to:\n" + outputFile.getCanonicalPath() );
	}
}
