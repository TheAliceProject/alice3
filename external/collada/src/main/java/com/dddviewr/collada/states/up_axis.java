package com.dddviewr.collada.states;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import org.xml.sax.Attributes;

public class up_axis extends State {

  @Override
  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);
    setContentNeeded(true);
  }

  @Override
  public void endElement(String name) {
    ((COLLADA) getParent().getParent()).getCollada().setUpAxis(content.toString());
    super.endElement(name);
  }

}
