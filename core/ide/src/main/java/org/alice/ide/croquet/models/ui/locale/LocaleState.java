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
package org.alice.ide.croquet.models.ui.locale;

import org.alice.ide.croquet.codecs.LocaleCodec;
import org.alice.ide.croquet.models.information.RestartRequiredOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.preferences.PreferenceMutableDataSingleSelectListState;

import java.util.Locale;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class LocaleState extends PreferenceMutableDataSingleSelectListState<Locale> {
	private static class SingletonHolder {
		private static LocaleState instance = new LocaleState();
	}

	public static LocaleState getInstance() {
		return SingletonHolder.instance;
	}

	private LocaleState() {
		super(
				Application.APPLICATION_UI_GROUP, UUID.fromString( "b9ed4d66-2eef-4d7d-b816-55451b437721" ),
				0,
				LocaleCodec.SINGLETON,
				new Locale( "en", "US" ),
				//				new java.util.Locale( "pt" ),
				new Locale( "pt", "BR" ),
				new Locale( "es" ),
				//				new java.util.Locale( "fr" ),
				//				new java.util.Locale( "fr", "BE" ),
				//				new java.util.Locale( "it" ),
				//				new java.util.Locale( "nl" ),
				//				new java.util.Locale( "de" ),
				new Locale( "el" ),
				new Locale( "ro" ),
				//				new java.util.Locale( "cs" ),
				new Locale( "sl" ),
				//				new java.util.Locale( "lt" ),
				new Locale( "ru" ),
				new Locale( "uk" ),
				new Locale( "tr" ),
				new Locale( "ar" ),
				//				new java.util.Locale( "iw" ),
				//				new java.util.Locale( "in" ),
				new Locale( "zh", "CN" ),
				new Locale( "ja" ),
				new Locale( "bg" )
		//				new java.util.Locale( "zh", "TW" ),
		//				new java.util.Locale( "ko" ) 
		);
		this.addNewSchoolValueListener( new ValueListener<Locale>() {
			@Override
			public void valueChanged( ValueEvent<Locale> e ) {
				RestartRequiredOperation.getInstance().fire();
			}
		} );
	}
}
