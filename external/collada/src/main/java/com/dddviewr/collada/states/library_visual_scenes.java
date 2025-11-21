package com.dddviewr.collada.states;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.visualscene.LibraryVisualScenes;
import org.xml.sax.Attributes;

public class library_visual_scenes extends State {
  protected LibraryVisualScenes library = new LibraryVisualScenes();

  public void init(String name, Attributes attrs, StateManager mngr) {
    super.init(name, attrs, mngr);

    Collada collada = ((COLLADA) getParent()).getCollada();
    collada.setLibraryVisualScenes(this.library);
  }

  public LibraryVisualScenes getLibrary() {
    return this.library;
  }
}
