
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
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import org.alice.ide.localize.review.core.Item;
import org.alice.ide.localize.review.core.LocalizeUtils;
import org.alice.ide.story.AliceIde;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Dennis Cosgrove
 */
public class CheckTranslations {

	private static class Mistranslation {
		public Mistranslation( Item item, List<String> englishTags, String localizedValue, List<String> localizedTags ) {
			this.item = item;
			this.englishTags = englishTags;
			this.localizedValue = localizedValue;
			this.localizedTags = localizedTags;
		}

		private final Item item;
		private final List<String> englishTags;
		private final String localizedValue;
		private final List<String> localizedTags;
	}

	public static void main( String[] args ) throws Exception {

		List<Locale> ALL_LOCALES = Lists.newArrayList( new Locale( "pt" ), new Locale( "pt", "BR" ), new Locale( "es" ), new Locale( "fr" ), new Locale( "fr", "BE" ), new Locale( "it" ), new Locale( "nl" ), new Locale( "de" ), new Locale( "el" ), new Locale( "ro" ), new Locale( "cs" ), new Locale( "sl" ), new Locale( "lt" ), new Locale( "ru" ), new Locale( "uk" ), new Locale( "tr" ), new Locale( "ar" ), new Locale( "iw" ), new Locale( "in" ), new Locale( "zh", "CN" ), new Locale( "zh", "TW" ), new Locale( "ko" ) );

		final List<Locale> locales;
		if( args.length == 0 ) {
			locales = ALL_LOCALES;
		} else {
			locales = Lists.newArrayListWithInitialCapacity( args.length );
			for( String arg : args ) {
				String[] languageCountryVariant = arg.split( "," );
				Locale locale;
				switch( languageCountryVariant.length ) {
				case 1:
					locale = new Locale( languageCountryVariant[ 0 ] );
					break;
				case 2:
					locale = new Locale( languageCountryVariant[ 0 ], languageCountryVariant[ 1 ] );
					break;
				case 3:
					locale = new Locale( languageCountryVariant[ 0 ], languageCountryVariant[ 1 ], languageCountryVariant[ 2 ] );
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
		for( Locale l : locales ) {
			System.out.println( "  " + l.getDisplayName() );
		}

		InitializingIfAbsentListHashMap<Locale, Mistranslation> mapLocaleToMistranslations = Maps.newInitializingIfAbsentListHashMap();

		List<Item> items = LocalizeUtils.getItems( AliceIde.class, "alice-ide" );
		for( Item item : items ) {
			String defaultValue = item.getDefaultValue();
			List<String> englishTags = LocalizeUtils.getTags( defaultValue );
			if( englishTags.size() > 0 ) {
				//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( item.getKey(), item.getDefaultValue() );

				for( Locale locale : locales ) {

					ResourceBundle resourceBundleB = ResourceBundleUtilities.getUtf8Bundle( item.getBundleName(), locale );
					String localizedValue = resourceBundleB.getString( item.getKey() );

					if( localizedValue.equals( defaultValue ) ) {
						//pass
					} else {
						List<Mistranslation> mistranslations = mapLocaleToMistranslations.getInitializingIfAbsentToLinkedList( locale );
						List<String> localizedTags = LocalizeUtils.getTags( localizedValue );

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
		for( Locale locale : locales ) {
			sb.append( "<h2>" );
			sb.append( locale.getDisplayName() );
			sb.append( "</h2>" );
			List<Mistranslation> mistranslations = mapLocaleToMistranslations.getInitializingIfAbsentToLinkedList( locale );
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
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

		sb.append( "<hr>" );
		sb.append( "<em>generated: " );
		sb.append( simpleDateFormat.format( new Date() ) );
		sb.append( "</em>" );

		sb.append( "</html>" );

		File outputFile = new File( FileUtilities.getDefaultDirectory(), "latestTranslationsValidityReport.html" );
		TextFileUtilities.write( outputFile, sb.toString() );

		System.out.println( "Translation check finished." );
		System.out.println( "Results:" );
		for( Locale locale : locales ) {
			List<Mistranslation> mistranslations = mapLocaleToMistranslations.getInitializingIfAbsentToLinkedList( locale );
			System.out.println( "  " + locale.getDisplayName() + " : " + mistranslations.size() + " errors" );
		}
		System.out.println( "Results written to:\n" + outputFile.getCanonicalPath() );
	}
}
