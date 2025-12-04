package com.dddviewr.collada.materials;

import com.dddviewr.collada.Base;

import java.io.PrintStream;

public class InstanceEffect extends Base {
  protected String url;

  public InstanceEffect(String url) {
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
    out.println(prefix + "InstanceEffect (url: " + this.url + ")");
  }
}
