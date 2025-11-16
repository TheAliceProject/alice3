package com.dddviewr.collada.states;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import org.xml.sax.Attributes;

public class authoring_tool extends State {
  @Override
  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);
    setContentNeeded(true);
  }

  @Override
  public void endElement(String name) {
    ((COLLADA) getParent().getParent().getParent()).getCollada().setAuthoringTool(content.toString());
    super.endElement(name);
  }

}
