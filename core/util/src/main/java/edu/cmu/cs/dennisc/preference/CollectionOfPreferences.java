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
package edu.cmu.cs.dennisc.preference;

/**
 * @author Dennis Cosgrove
 */
public abstract class CollectionOfPreferences {
	private java.util.prefs.Preferences utilPrefs;
	private edu.cmu.cs.dennisc.preference.Preference<?>[] preferences;

	public void initialize() {
		assert this.preferences == null;
		assert this.utilPrefs == null;
		this.utilPrefs = java.util.prefs.Preferences.userNodeForPackage( this.getClass() );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.clearAllPreferences" ) ) {
			try {
				this.utilPrefs.clear();
			} catch( java.util.prefs.BackingStoreException bse ) {
				throw new RuntimeException( bse );
			}
		}
		java.util.List<java.lang.reflect.Field> fields = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getPublicFinalFields( this.getClass(), Preference.class );
		this.preferences = new edu.cmu.cs.dennisc.preference.Preference<?>[ fields.size() ];
		int i = 0;
		for( java.lang.reflect.Field field : fields ) {
			this.preferences[ i ] = (Preference)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( field, this );
			this.preferences[ i ].initialize( this, field.getName(), java.lang.reflect.Modifier.isTransient( field.getModifiers() ) );
			i++;
		}
		this.setOrder( this.preferences );
	}

	public java.util.prefs.Preferences getUtilPrefs() {
		return this.utilPrefs;
	}

	protected edu.cmu.cs.dennisc.preference.Preference<?>[] setOrder( edu.cmu.cs.dennisc.preference.Preference<?>[] rv ) {
		return rv;
	}

	public final edu.cmu.cs.dennisc.preference.Preference<?>[] getPreferences() {
		assert this.preferences != null;
		return this.preferences;
	}
}
