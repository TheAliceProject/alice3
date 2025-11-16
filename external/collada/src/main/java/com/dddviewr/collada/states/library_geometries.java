package com.dddviewr.collada.states;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.geometry.LibraryGeometries;
import org.xml.sax.Attributes;

public class library_geometries extends State {
  protected LibraryGeometries library = new LibraryGeometries();

  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);

    Collada collada = ((COLLADA) getParent()).getCollada();
    collada.setLibraryGeometries(this.library);
  }

  public LibraryGeometries getLibrary() {
    return this.library;
  }
}
