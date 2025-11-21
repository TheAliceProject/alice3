package com.dddviewr.collada.states;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import org.xml.sax.Attributes;

public class format extends State {
  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);
    setContentNeeded(true);
  }

  public void endElement(String name) {
    ((surface) getParent()).getSurface().setFormat(this.content.toString());
    super.endElement(name);
  }
}
