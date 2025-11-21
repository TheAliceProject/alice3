package com.dddviewr.collada.states;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.Vcount;
import org.xml.sax.Attributes;

import java.lang.reflect.Method;

public class vcount extends State {
  protected Vcount theVcount;

  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);
    this.theVcount = new Vcount();
    setContentNeeded(true);

    State parent = getParent();
    try {
      Method method = parent.getClass().getMethod("setVcount", new Class[] {Vcount.class});
      method.invoke(parent, new Object[] {this.theVcount});
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void endElement(String name) {
    this.theVcount.parse(content);
    super.endElement(name);
  }

  public Vcount getVcount() {
    return this.theVcount;
  }
}
