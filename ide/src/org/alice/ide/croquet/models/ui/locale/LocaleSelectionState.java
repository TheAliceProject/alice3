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
package org.alice.ide.croquet.models.ui.locale;

/**
 * @author Dennis Cosgrove
 */
public class LocaleSelectionState extends edu.cmu.cs.dennisc.croquet.DefaultListSelectionState< java.util.Locale > {
	private static class SingletonHolder {
		private static LocaleSelectionState instance = new LocaleSelectionState();
	}
	public static LocaleSelectionState getInstance() {
		return SingletonHolder.instance;
	}
	private LocaleSelectionState() {
		super( 
				edu.cmu.cs.dennisc.croquet.Application.UI_STATE_GROUP, java.util.UUID.fromString( "b9ed4d66-2eef-4d7d-b816-55451b437721" ), 
				org.alice.ide.croquet.codecs.LocaleCodec.SINGLETON,
				0,
				new java.util.Locale( "en", "US" ),
				new java.util.Locale( "pt" ),
				new java.util.Locale( "pt", "BR" ),
				new java.util.Locale( "es" ),
				new java.util.Locale( "fr" ),
				new java.util.Locale( "fr", "BE" ),
				new java.util.Locale( "it" ),
				new java.util.Locale( "nl" ),
				new java.util.Locale( "de" ),
				new java.util.Locale( "el" ),
				new java.util.Locale( "ro" ),
				new java.util.Locale( "cs" ),
				new java.util.Locale( "sl" ),
				new java.util.Locale( "lt" ),
				new java.util.Locale( "ru" ),
				new java.util.Locale( "uk" ),
				new java.util.Locale( "tr" ),
				new java.util.Locale( "ar" ),
				new java.util.Locale( "iw" ),
				new java.util.Locale( "in" ),
				new java.util.Locale( "zh" ),
				new java.util.Locale( "ko" )
		);
		org.alice.ide.PreferenceManager.registerAndInitializePreference( this );
		this.addValueObserver( new ValueObserver< java.util.Locale >() {
			public void changing( edu.cmu.cs.dennisc.croquet.State< java.util.Locale > state, java.util.Locale prevValue, java.util.Locale nextValue, boolean isAdjusting ) {
			}
			public void changed( edu.cmu.cs.dennisc.croquet.State< java.util.Locale > state, java.util.Locale prevValue, java.util.Locale nextValue, boolean isAdjusting ) {
				org.alice.ide.croquet.models.information.RestartRequiredOperation.getInstance().fire();
			}
		} );
	}
	@Override
	protected String getMenuText(java.util.Locale item) {
		if( item != null ) {
			return item.getDisplayName( item ) + " / " + item.getDisplayName();
		} else {
			return super.getMenuText(item);
		}
	}
}
