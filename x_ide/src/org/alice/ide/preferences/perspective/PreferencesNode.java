package org.alice.ide.preferences.perspective;

public abstract class PreferencesNode extends org.alice.ide.preferences.PreferencesNode {
	protected PreferencesNode( boolean isDefaultFieldNameGenerationDesiredByDefault ) {
		super(
				new edu.cmu.cs.dennisc.preference.StringPreference( "selectedPerspective", "exposure" )
		);
	}
}
