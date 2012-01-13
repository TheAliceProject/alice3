package edu.cmu.cs.dennisc.matt;

import org.lgna.story.event.EventPolicy;

public class AddKeyPressedListener {

	public static interface Detail{}
	
	public static EventPolicy getPolicy(Detail[] details){
		for(Detail detail: details){
			if(detail instanceof MultipleEventPolicy){
				return MultipleEventPolicy.getPolicy((MultipleEventPolicy) detail);
			}
		}
		return EventPolicy.COMBINE;
	}
}
