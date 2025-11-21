package com.dddviewr.collada.states;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import org.xml.sax.Attributes;

import java.lang.reflect.Method;

public class v extends State {
  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);
    setContentNeeded(true);
  }

  public void endElement(String name) {
    State parent = getParent();
    try {
      Method method = parent.getClass().getMethod("parseData", new Class[] {StringBuilder.class});
      method.invoke(parent, new Object[] {this.content});
    } catch (Exception e) {
      e.printStackTrace();
    }

    super.endElement(name);
  }
}
