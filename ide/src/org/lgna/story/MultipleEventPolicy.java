package org.lgna.story;

public enum MultipleEventPolicy implements AddMouseButtonListener.Detail, AddKeyPressListener.Detail, AddTimerEventListener.Detail {
	ENQUEUE,
	//todo
	//ENQUEUE_PER_SUBJECT,
	IGNORE,
	COMBINE;
	//todo
	//private static final EventPolicy DEFAULT_VALUE = EventPolicy.ENQUEUE_PER_SUBJECT;
	private static final MultipleEventPolicy DEFAULT_VALUE = MultipleEventPolicy.ENQUEUE;
	
	private static MultipleEventPolicy getValue( Object[] details, MultipleEventPolicy defaultValue ) {
		for( Object detail : details ) {
			if( detail instanceof MultipleEventPolicy ) {
				MultipleEventPolicy eventPolicy = (MultipleEventPolicy)detail;
				return eventPolicy;
			}
		}
		return defaultValue;
	}
	/*package-private*/ static MultipleEventPolicy getValue( Object[] details ) {
		return getValue( details, DEFAULT_VALUE );
	}
}
