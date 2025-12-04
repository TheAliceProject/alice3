package com.dddviewr.collada.states;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.visualscene.InstanceController;
import org.xml.sax.Attributes;

public class skeleton extends State {
  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);
    setContentNeeded(true);
  }

  public void endElement(String name) {
    InstanceController controller = ((instance_controller) getParent()).getInstanceController();
    controller.setSkeleton(this.content.toString());
    super.endElement(name);
  }
}
