package com.dddviewr.collada.animation;

import com.dddviewr.collada.Base;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class LibraryAnimations extends Base {
  protected List<Animation> animations = new ArrayList<Animation>();

  public void dump(PrintStream out, int indent) {
    String prefix = createIndent(indent);
    out.println(prefix + "LibraryAnimations");
    for (Animation a : this.animations) {
      a.dump(out, indent + 1);
    }
  }

  public void addAnimation(Animation anim) {
    this.animations.add(anim);
  }

  public List<Animation> getAnimations() {
    return this.animations;
  }
}
