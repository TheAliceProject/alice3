package com.dddviewr.collada.images;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;

public class LibraryImages extends Base {
	List<Image> images = new ArrayList<Image>();

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "LibraryImages");

		for (Image img : this.images)
			img.dump(out, indent + 1);
	}

	public List<Image> getImages() {
		return this.images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public void addImage(Image image) {
		this.images.add(image);
	}
}