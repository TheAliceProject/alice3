package com.dddviewr.collada.states;

import com.dddviewr.collada.NameArray;
import com.dddviewr.collada.Source;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import org.xml.sax.Attributes;

public class Name_array extends State {
  protected NameArray nameArray;

  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);
    this.nameArray = new NameArray(attrs.getValue("id"), Integer.parseInt(attrs.getValue("count")));
    Source src = ((source) getParent()).getSource();
    src.setNameArray(this.nameArray);
    setContentNeeded(true);
  }

  public void endElement(String name) {
    this.nameArray.parse(this.content);
    super.endElement(name);
  }

  public NameArray getIdrefArray() {
    return this.nameArray;
  }
}
