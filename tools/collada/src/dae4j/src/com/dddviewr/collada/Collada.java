package com.dddviewr.collada;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.dddviewr.collada.animation.LibraryAnimations;
import com.dddviewr.collada.controller.Controller;
import com.dddviewr.collada.controller.LibraryControllers;
import com.dddviewr.collada.controller.Skin;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.LibraryEffects;
import com.dddviewr.collada.geometry.Geometry;
import com.dddviewr.collada.geometry.LibraryGeometries;
import com.dddviewr.collada.geometry.Mesh;
import com.dddviewr.collada.images.Image;
import com.dddviewr.collada.images.LibraryImages;
import com.dddviewr.collada.materials.LibraryMaterials;
import com.dddviewr.collada.materials.Material;
import com.dddviewr.collada.nodes.LibraryNodes;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.scene.Scene;
import com.dddviewr.collada.visualscene.LibraryVisualScenes;
import com.dddviewr.log.Log;


public class Collada extends Base {
	protected LibraryGeometries libraryGeometries;
	protected LibraryVisualScenes libraryVisualScenes;
	protected LibraryControllers libraryControllers;
	protected LibraryAnimations libraryAnimations;
	protected LibraryImages libraryImages;
	protected LibraryMaterials libraryMaterials;
	protected LibraryEffects libraryEffects;
	protected LibraryNodes libraryNodes;
	protected Scene scene;
	
	protected String authoringTool = "";
	protected String upAxis = "Z_UP";
	protected Unit unit;
	
	public static XMLReader reader;

	public LibraryGeometries getLibraryGeometries() {
		return libraryGeometries;
	}

	public void setLibraryGeometries(LibraryGeometries libraryGeometries) {
		this.libraryGeometries = libraryGeometries;
	}

	public LibraryVisualScenes getLibraryVisualScenes() {
		return libraryVisualScenes;
	}

	public void setLibraryVisualScenes(LibraryVisualScenes libraryVisualScenes) {
		this.libraryVisualScenes = libraryVisualScenes;
	}

	public LibraryControllers getLibraryControllers() {
		return libraryControllers;
	}

	public void setLibraryControllers(LibraryControllers libraryControllers) {
		this.libraryControllers = libraryControllers;
	}

	public LibraryAnimations getLibraryAnimations() {
		return libraryAnimations;
	}

	public void setLibraryAnimations(LibraryAnimations libraryAnimations) {
		this.libraryAnimations = libraryAnimations;
	}

	public LibraryImages getLibraryImages() {
		return libraryImages;
	}

	public void setLibraryImages(LibraryImages library) {
		this.libraryImages = library;
	}

	public LibraryMaterials getLibraryMaterials() {
		return libraryMaterials;
	}

	public void setLibraryMaterials(LibraryMaterials libraryMaterials) {
		this.libraryMaterials = libraryMaterials;
	}

	public LibraryEffects getLibraryEffects() {
		return libraryEffects;
	}

	public void setLibraryEffects(LibraryEffects libraryEffects) {
		this.libraryEffects = libraryEffects;
	}

	public LibraryNodes getLibraryNodes() {
		return libraryNodes;
	}

	public void setLibraryNodes(LibraryNodes libraryNodes) {
		this.libraryNodes = libraryNodes;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public String getUpAxis() {
		return upAxis;
	}

	public void setUpAxis(String upAxis) {
		this.upAxis = upAxis;
	}
	
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getAuthoringTool() {
		return authoringTool;
	}

	public void setAuthoringTool(String authoringTool) {
		this.authoringTool = authoringTool;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "COLLADA");
		out.println(prefix + " (" + upAxis + ")");
		if (this.unit != null)
			this.unit.dump(out, indent + 1);
		if (this.libraryImages != null)
			this.libraryImages.dump(out, indent + 1);
		if (this.libraryMaterials != null)
			this.libraryMaterials.dump(out, indent + 1);
		if (this.libraryEffects != null)
			this.libraryEffects.dump(out, indent + 1);
		if (this.libraryGeometries != null)
			this.libraryGeometries.dump(out, indent + 1);
		if (this.libraryNodes != null)
			this.libraryNodes.dump(out, indent + 1);
		if (this.libraryControllers != null)
			this.libraryControllers.dump(out, indent + 1);
		if (this.libraryAnimations != null)
			this.libraryAnimations.dump(out, indent + 1);
		if (this.libraryVisualScenes != null)
			this.libraryVisualScenes.dump(out, indent + 1);
		if (this.scene != null)
			this.scene.dump(out, indent + 1);
	}

	public void deindexMeshes() {
		if(libraryGeometries == null) {
			return;
		}
		List<Geometry> geos = libraryGeometries.getGeometries();
		for (Geometry geo : geos) {
			Mesh mesh = geo.getMesh();
			if (mesh != null)
				mesh.deindex(this);
		}
	}

	public static Collada readFile(String fname) throws SAXException,
			FileNotFoundException, IOException {
		StateManager stateManager = new StateManager();
		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setContentHandler(stateManager);
		reader.parse(new InputSource(new FileReader(fname)));
		Collada collada = stateManager.getCollada();
		return collada;
	}

	public static Collada readFile(URL file) throws SAXException,
			FileNotFoundException, IOException {
		StateManager stateManager = new StateManager();
		if(reader == null) {
			try {
				SAXParserFactory factory;
				factory = (SAXParserFactory) Class.forName(
						"com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl").newInstance();
				//factory = SAXParserFactory.newInstance();
				Log.log("SAXParserFactory: " + factory);
				//factory.setNamespaceAware(true);
				//factory.setValidating(false);
				//factory.setXIncludeAware(false);
				reader = factory.newSAXParser().getXMLReader();
				
			} catch (Throwable t) {
				Log.exception(t);
			}
		}			
		Log.log("XMLReader: " + reader);
		reader.setContentHandler(stateManager);
		try {
			Log.log("Start parsing");
			reader.parse(new InputSource(file.openStream()));
			Log.log("Parsing done");
		} catch ( Throwable t) {
			Log.exception(t);
		}
		Collada collada = stateManager.getCollada();
		return collada;
	}
	
	public List<Skin> findSkins(String source) {
		if (this.libraryControllers == null)
			return new ArrayList<Skin>();
		return this.libraryControllers.findSkins(source);
	}

	public Controller findController(String id) {
		if (this.libraryControllers == null)
			return null;
		String search = id;
		if (id.indexOf(35) == 0)
			search = id.substring(1);
		for (Controller ctrl : this.libraryControllers.getControllers()) {
			if (search.equals(ctrl.getId()))
				return ctrl;
		}
		return null;
	}

	public Effect findEffect(String id) {
		if (this.libraryEffects == null)
			return null;
		String search = id;
		if (id.indexOf(35) == 0)
			search = id.substring(1);
		for (Effect effect : this.libraryEffects.getEffects()) {
			if (search.equals(effect.getId()))
				return effect;
		}
		return null;
	}

	public Image findImage(String id) {
		if (this.libraryImages == null)
			return null;
		String search = id;
		if (id.indexOf(35) == 0)
			search = id.substring(1);
		for (Image img : this.libraryImages.getImages()) {
			if (search.equals(img.getId()))
				return img;
		}
		return null;
	}

	public Material findMaterial(String id) {
		if (this.libraryMaterials == null)
			return null;
		String search = id;
		if (id.indexOf(35) == 0)
			search = id.substring(1);
		for (Material mat : this.libraryMaterials.getMaterials()) {
			if (search.equals(mat.getId()))
				return mat;
		}
		return null;
	}

	public Geometry findGeometry(String id) {
		if (this.libraryGeometries == null)
			return null;
		String search = id;
		if (id.indexOf(35) == 0)
			search = id.substring(1);
		for (Geometry geo : this.libraryGeometries.getGeometries()) {
			if (search.equals(geo.getId()))
				return geo;
		}
		return null;
	}

	public Node findNode(String id) {
		if (this.libraryNodes == null)
			return null;
		String search = id;
		if (id.indexOf(35) == 0)
			search = id.substring(1);
		for (Node n : this.libraryNodes.getNodes()) {
			if (search.equals(n.getId()))
				return n;
		}
		return null;
	}
}