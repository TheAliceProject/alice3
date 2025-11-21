package com.dddviewr.collada.states;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.materials.LibraryMaterials;
import org.xml.sax.Attributes;

public class library_materials extends State {
  protected LibraryMaterials library = new LibraryMaterials();

  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);
    Collada collada = ((COLLADA) getParent()).getCollada();
    collada.setLibraryMaterials(this.library);
  }

  public LibraryMaterials getLibrary() {
    return this.library;
  }
}
