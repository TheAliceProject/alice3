package edu.cmu.cs.dennisc.animation;

public interface MediaPlayerObserver extends AnimationObserver{
	public void playerStarted(edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation playerAnimation, double playTime);

}
