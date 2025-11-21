package com.dddviewr.collada.scene;

import com.dddviewr.collada.Base;

import java.io.PrintStream;

public class Scene extends Base {
  protected InstanceVisualScene instanceVisualScene;

  public InstanceVisualScene getInstanceVisualScene() {
    return this.instanceVisualScene;
  }

  public void setInstanceVisualScene(InstanceVisualScene instanceVisualScene) {
    this.instanceVisualScene = instanceVisualScene;
  }

  public void dump(PrintStream out, int indent) {
    String prefix = createIndent(indent);
    out.println(prefix + "Scene");
    if (this.instanceVisualScene != null) {
      this.instanceVisualScene.dump(out, indent + 1);
    }
  }
}
