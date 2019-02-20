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
package org.lgna.croquet.codecs;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import org.lgna.croquet.ItemCodec;

import javax.swing.JComponent;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Dennis Cosgrove
 */
public class EnumCodec<T extends Enum<T>> implements ItemCodec<T> {
	public static interface LocalizationCustomizer<T extends Enum<T>> {
		public String customize( String localization, T value );
	}

	private static InitializingIfAbsentMap<Class, EnumCodec> map = Maps.newInitializingIfAbsentHashMap();

	public static synchronized <T extends Enum<T>> EnumCodec<T> createInstance( Class<T> valueCls, final LocalizationCustomizer<T> localizationCustomizer ) {
		return new EnumCodec<T>( valueCls, localizationCustomizer );
	}

	public static synchronized <T extends Enum<T>> EnumCodec<T> getInstance( Class<T> valueCls ) {
		EnumCodec<T> rv = map.getInitializingIfAbsent( (Class)valueCls, new InitializingIfAbsentMap.Initializer<Class, EnumCodec>() {
			@Override
			public EnumCodec initialize( Class valueCls ) {
				return new EnumCodec( valueCls, null );
			}
		} );
		return rv;
	}

	private final Class<T> valueCls;
	private final LocalizationCustomizer<T> localizationCustomizer;
	private Map<T, String> mapValueToLocalization;

	private EnumCodec( Class<T> valueCls, LocalizationCustomizer<T> localizationCustomizer ) {
		this.valueCls = valueCls;
		this.localizationCustomizer = localizationCustomizer;
	}

	@Override
	public Class<T> getValueClass() {
		return this.valueCls;
	}

	public LocalizationCustomizer<T> getLocalizationCustomizer() {
		return this.localizationCustomizer;
	}

	@Override
	public final T decodeValue( BinaryDecoder binaryDecoder ) {
		return binaryDecoder.decodeEnum();
	}

	@Override
	public final void encodeValue( BinaryEncoder binaryEncoder, T t ) {
		binaryEncoder.encode( t );
	}

	@Override
	public final void appendRepresentation( StringBuilder sb, T value ) {
		if( value != null ) {
			if( this.mapValueToLocalization != null ) {
				//pass
			} else {
				this.mapValueToLocalization = Maps.newHashMap();
				String bundleName = this.valueCls.getPackage().getName() + ".croquet";
				try {
					ResourceBundle resourceBundle = ResourceBundleUtilities.getUtf8Bundle( bundleName, JComponent.getDefaultLocale() );
					String clsName = this.valueCls.getSimpleName();
					for( T enumConstant : this.valueCls.getEnumConstants() ) {
						String localizationKey = clsName + "." + enumConstant.name();
						try {
							String localizationValue = resourceBundle.getString( localizationKey );
							if( this.localizationCustomizer != null ) {
								localizationValue = this.localizationCustomizer.customize( localizationValue, enumConstant );
							}
							this.mapValueToLocalization.put( enumConstant, localizationValue );
						} catch( MissingResourceException mre ) {
							//pass
						}
					}
				} catch( MissingResourceException mre ) {
					//pass
				}
			}
			String text = this.mapValueToLocalization.get( value );
			if( text != null ) {
				//pass
			} else {
				text = value.toString();
			}
			sb.append( text );
		} else {
			sb.append( value );
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getName() );
		sb.append( "[" );
		sb.append( this.valueCls.getName() );
		sb.append( "]" );
		return sb.toString();
	}
}
