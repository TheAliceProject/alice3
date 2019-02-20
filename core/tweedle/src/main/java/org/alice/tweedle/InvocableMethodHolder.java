package org.alice.tweedle;

import org.alice.tweedle.run.Frame;
import org.alice.tweedle.run.TweedleObject;

public interface InvocableMethodHolder {
	void invoke( Frame frame, TweedleObject target, TweedleMethod method, TweedleValue[] arguments );

	String getName();
}
