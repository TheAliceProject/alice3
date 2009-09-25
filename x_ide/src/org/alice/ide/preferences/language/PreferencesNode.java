package org.alice.ide.preferences.language;

public abstract class PreferencesNode extends org.alice.ide.preferences.PreferencesNode {
	protected PreferencesNode( boolean isDefaultFieldNameGenerationDesiredByDefault ) {
		super(
				new edu.cmu.cs.dennisc.preference.BooleanPreference( "isDefaultFieldNameGenerationDesired", isDefaultFieldNameGenerationDesiredByDefault )
		);
	}
}
