package org.alice.ide.preferences.perspective.preparation;

public class PreferencesNode extends org.alice.ide.preferences.perspective.PreferencesNode {
	private static PreferencesNode singleton;
	public static PreferencesNode getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new PreferencesNode();
		}
		return singleton;
	}
	private PreferencesNode() {
	}
	@Override
	protected boolean isDefaultFieldNameGenerationDesiredByDefault() {
		return true;
	}
}
