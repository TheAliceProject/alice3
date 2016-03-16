import org.alice.ide.localize.review.core.Item;

/**
 * Copyright (c) 2006-2015, Carnegie Mellon University. All rights reserved.
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

/**
 * @author Dennis Cosgrove
 */
public class CheckTranslations {
	public static void main( String[] args ) throws Exception {

		java.util.Locale[] locales = { new java.util.Locale( "pt" ), new java.util.Locale( "pt", "BR" ), new java.util.Locale( "es" ), new java.util.Locale( "fr" ), new java.util.Locale( "fr", "BE" ), new java.util.Locale( "it" ), new java.util.Locale( "nl" ), new java.util.Locale( "de" ), new java.util.Locale( "el" ), new java.util.Locale( "ro" ), new java.util.Locale( "cs" ), new java.util.Locale( "sl" ), new java.util.Locale( "lt" ), new java.util.Locale( "ru" ), new java.util.Locale( "uk" ), new java.util.Locale( "tr" ), new java.util.Locale( "ar" ), new java.util.Locale( "iw" ), new java.util.Locale( "in" ), new java.util.Locale( "zh", "CN" ), new java.util.Locale( "zh", "TW" ), new java.util.Locale( "ko" )
		};
		java.util.List<Item> items = org.alice.ide.localize.review.core.LocalizeUtils.getItems( org.alice.ide.story.AliceIde.class, "alice-ide" );
		for( Item item : items ) {
			String defaultValue = item.getDefaultValue();
			java.util.List<String> tags = org.alice.ide.localize.review.core.LocalizeUtils.getTags( defaultValue );
			if( tags.size() > 0 ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( item.getKey(), item.getDefaultValue() );

				for( java.util.Locale locale : locales ) {
					java.util.ResourceBundle resourceBundleB = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getUtf8Bundle( item.getBundleName(), locale );
					String localizedValue = resourceBundleB.getString( item.getKey() );

					if( localizedValue.equals( defaultValue ) ) {
						//pass
					} else {
						java.util.List<String> localizedTags = org.alice.ide.localize.review.core.LocalizeUtils.getTags( localizedValue );

						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( localizedValue );
						for( String tag : tags ) {
							if( localizedTags.contains( tag ) ) {
								edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "***", locale, tag );
							} else {
								throw new RuntimeException( locale + " " + item.getKey() + " " + defaultValue + " " + localizedValue );
							}
						}
					}
				}
			}
		}
	}
}
