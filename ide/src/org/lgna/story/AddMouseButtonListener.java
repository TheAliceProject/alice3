package org.lgna.story;


public class AddMouseButtonListener  {

	@org.lgna.project.annotations.ClassTemplate( keywordFactoryCls=AddMouseButtonListener.class )
	public static interface Detail {
	}
	public static MultipleEventPolicy multipleEventPolicy(MultipleEventPolicy multipleEventPolicy) {
		return multipleEventPolicy;
	}
	
	public static SetOfVisuals setOfVisuals( Visual[] visuals ) {
		return new SetOfVisuals( visuals );
	}
	
//
//	
//	public static EventPolicy getPolicy(Detail[] details) {
//		for(Detail detail: details){
//			if(detail instanceof MultipleEventPolicy){
//				return MultipleEventPolicy.getPolicy((MultipleEventPolicy) detail);
//			}
//		}
//		return EventPolicy.COMBINE;
//	}
//	public static List<Model> getTargeted(Detail[] details) {
//		for(Detail detail: details){
//			if(detail instanceof TargetedModels){
//				return Collections.newArrayList(TargetedModels.getCollection((TargetedModels) detail));
//			}
//		}
//		return Collections.newArrayList(TargetedModels.getEmptyCollection());
//	}
}
