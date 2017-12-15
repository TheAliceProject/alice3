package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Source;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.animation.Animation;
import com.dddviewr.collada.animation.LibraryAnimations;

public class animation extends State {
	protected Animation theAnimation;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theAnimation = new Animation(attrs.getValue("id"));

		LibraryAnimations library = ((library_animations) getParent())
				.getLibrary();
		library.addAnimation(this.theAnimation);
	}

	public void addSource(Source src) {
		this.theAnimation.addSource(src);
	}

	public Animation getAnimation() {
		return this.theAnimation;
	}
}