package edu.cmu.cs.dennisc.matt;

import java.util.List;

import org.lgna.story.Model;
import org.lgna.story.event.EventPolicy;

import edu.cmu.cs.dennisc.java.util.Collections;


public class AddMouseButtonListener  {

	public static interface Detail {
	}

	public static EventPolicy getPolicy(Detail[] details) {
		for(Detail detail: details){
			if(detail instanceof MultipleEventPolicy){
				return MultipleEventPolicy.getPolicy((MultipleEventPolicy) detail);
			}
		}
		return EventPolicy.COMBINE;
	}

	public static List<Model> getTargeted(Detail[] details) {
		for(Detail detail: details){
			if(detail instanceof TargetedModels){
				return Collections.newArrayList(TargetedModels.getCollection((TargetedModels) detail));
			}
		}
		return Collections.newArrayList(TargetedModels.getEmptyCollection());
	}


}
