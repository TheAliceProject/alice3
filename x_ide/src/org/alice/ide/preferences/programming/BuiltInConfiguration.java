package org.alice.ide.preferences.programming;

public abstract class BuiltInConfiguration implements Configuration {
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " (built-in)";
	}
}
