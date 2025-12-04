package com.dddviewr.collada.scene;

import com.dddviewr.collada.Base;

import java.io.PrintStream;

public class InstanceVisualScene extends Base {
  protected String url;

  public InstanceVisualScene(String url) {
    this.url = url;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void dump(PrintStream out, int indent) {
    String prefix = createIndent(indent);
    out.println(prefix + "InstanceVisualScene (url: " + this.url + ")");
  }
}
