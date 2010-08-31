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
package org.alice.ide.preferences;

public class ProgrammingPreferences extends edu.cmu.cs.dennisc.preference.CollectionOfPreferences {
	private static ProgrammingPreferences singleton;
	public static ProgrammingPreferences getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new ProgrammingPreferences();
			singleton.initialize();
		}
		return singleton;
	}

	class CustomConfigurationsPreference extends edu.cmu.cs.dennisc.preference.ListPreference<org.alice.ide.preferences.programming.BinaryEncodableAndDecodableConfiguration> {
		public CustomConfigurationsPreference() {
			super( new java.util.LinkedList<org.alice.ide.preferences.programming.BinaryEncodableAndDecodableConfiguration>() );
		}
		@Override
		protected int getItemVersion() {
			return 1;
		}
		@Override
		protected org.alice.ide.preferences.programming.BinaryEncodableAndDecodableConfiguration decode( int itemVersion, edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
			if( itemVersion == 1 ) {
				return binaryDecoder.decodeBinaryEncodableAndDecodable(/*org.alice.ide.preferences.programming.BinaryEncodableAndDecodableConfiguration.class*/);
			} else {
				return null;
			}
		}
		@Override
		protected void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, org.alice.ide.preferences.programming.BinaryEncodableAndDecodableConfiguration item ) {
			binaryEncoder.encode(item);
		}
	}
	public final CustomConfigurationsPreference listOfCustomProgrammingPreferencesPreference = new CustomConfigurationsPreference();
	public final edu.cmu.cs.dennisc.preference.UUIDPreference activePerspective = new edu.cmu.cs.dennisc.preference.UUIDPreference( org.alice.ide.preferences.programming.exposure.ExposureConfiguration.getSingleton().getUUID() );
	public final transient edu.cmu.cs.dennisc.preference.BooleanPreference isDefaultFieldNameGenerationDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference( true );
	public final transient edu.cmu.cs.dennisc.preference.BooleanPreference isSyntaxNoiseDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference( false );

	@Override
	protected edu.cmu.cs.dennisc.preference.Preference<?>[] setOrder(edu.cmu.cs.dennisc.preference.Preference<?>[] rv) {
		assert rv.length == 4;
		rv[ 0 ] = this.listOfCustomProgrammingPreferencesPreference;
		rv[ 1 ] = this.activePerspective;
		rv[ 2 ] = this.isDefaultFieldNameGenerationDesired;
		rv[ 3 ] = this.isSyntaxNoiseDesired;
		return rv;
	}
	
	private org.alice.ide.preferences.programming.Configuration[] builtInPreferenceNodes = new org.alice.ide.preferences.programming.Configuration[] {
			org.alice.ide.preferences.programming.exposure.ExposureConfiguration.getSingleton(),
			org.alice.ide.preferences.programming.preparation.PreparationConfiguration.getSingleton()	
	};
	public org.alice.ide.preferences.programming.Configuration[] getBuiltInPreferenceNodes() {
		return this.builtInPreferenceNodes;
	}
}
