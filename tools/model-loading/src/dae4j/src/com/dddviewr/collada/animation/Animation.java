package com.dddviewr.collada.animation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;
import com.dddviewr.collada.Input;
import com.dddviewr.collada.Source;

public class Animation extends Base {
	protected String id;
	protected List<Source> sources = new ArrayList<Source>();
	protected Sampler sampler;
	protected Channel channel;

	public Animation(String id) {
		this.id = id;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Animation (id: " + this.id + ")");
		for (Source s : this.sources) {
			s.dump(out, indent + 1);
		}
		if (this.sampler != null)
			this.sampler.dump(out, indent + 1);
		if (this.channel != null)
			this.channel.dump(out, indent + 1);
	}

	public void addSource(Source src) {
		this.sources.add(src);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Source> getSources() {
		return this.sources;
	}

	public Sampler getSampler() {
		return this.sampler;
	}

	public void setSampler(Sampler sampler) {
		this.sampler = sampler;
	}

	public Channel getChannel() {
		return this.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Source getSource(String id) {
		String searchId = id;
		if (id.startsWith("#"))
			searchId = id.substring(1);
		for (Source s : this.sources) {
			if (s.getId().equals(searchId))
				return s;
		}
		return null;
	}

	public Source getSourceFromSemantic(String sem) {
		for (Input inp : this.sampler.getInputs()) {
			if (inp.getSemantic().equals(sem)) {
				return getSource(inp.getSource());
			}
		}

		return null;
	}
}