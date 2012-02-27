package org.lgna.story;


public class AddTimeListener {

	@org.lgna.project.annotations.ClassTemplate( keywordFactoryCls=AddTimeListener.class )
	public interface Detail{}

	public static Detail timerFrequency(double d) {
		return new TimerFrequency(d);
	}
	public static MultipleEventPolicy multipleEventPolicy(MultipleEventPolicy multipleEventPolicy) {
		return multipleEventPolicy;
	}
}
