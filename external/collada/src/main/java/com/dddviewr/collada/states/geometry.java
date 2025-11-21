package com.dddviewr.collada.states;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.geometry.Geometry;
import com.dddviewr.collada.geometry.LibraryGeometries;
import org.xml.sax.Attributes;

public class geometry extends State {
  protected Geometry geo;

  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);
    String id = attrs.getValue("id");
    String geoName = attrs.getValue("name");

    this.geo = new Geometry(id, geoName);
    LibraryGeometries lib = ((library_geometries) getParent()).getLibrary();
    lib.addGeometry(this.geo);
  }

  public Geometry getGeometry() {
    return this.geo;
  }
}
