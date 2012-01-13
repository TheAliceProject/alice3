package edu.cmu.cs.dennisc.matt;

import org.lgna.story.event.EventPolicy;

public class MultipleEventPolicy implements 
		AddMouseButtonListener.Detail,
		AddKeyPressedListener.Detail {
	
	private EventPolicy policy;

	public MultipleEventPolicy(EventPolicy policy) {
		this.policy = policy;
	}

	public static EventPolicy getPolicy(MultipleEventPolicy detail) {
		return detail.policy;
	}

}
