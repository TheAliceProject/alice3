package com.dddviewr.collada.states;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.visualscene.InstanceMaterial;
import org.xml.sax.Attributes;

import java.lang.reflect.Method;

public class instance_material extends State {
  protected InstanceMaterial instanceMaterial;

  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);
    this.instanceMaterial = new InstanceMaterial(attrs.getValue("symbol"), attrs.getValue("target"));
    State state = getParent().getParent().getParent();
    try {
      Method method = state.getClass().getMethod("addInstanceMaterial", new Class[] {InstanceMaterial.class});
      method.invoke(state, new Object[] {this.instanceMaterial});
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
