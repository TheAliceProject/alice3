package com.dddviewr.log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Log {

	public interface LogHandler {
		public void log(String msg);
		public int setStatus(String msg);
		public void clearStatus(int id);
	}
	
	private static final int MAX_MSGS = 400;
	private static List<String> msgs = new ArrayList<String>();
	private static LogHandler handler;
	
	public static void log(String msg) {
		msgs.add(msg);
		System.out.println(msg);
		if(handler != null)
			handler.log(msg);
		while(msgs.size() > MAX_MSGS) {
			msgs.remove(0);
		}
	}
	
	public static void exception(Throwable t) {
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer, true));
		String msg = writer.toString();
		for(String line: msg.split("\n"))
			log(line);
	}

	public static LogHandler getHandler() {
		return handler;
	}

	public static void setHandler(LogHandler handler) {
		Log.handler = handler;
	}

	public static List<String> getMsgs() {
		return msgs;
	}
	
	public static int setStatus(String msg) {
		if(handler != null)
			return handler.setStatus(msg);
		return 0;
	}
	
	public static void clearStatus(int id) {
		if(handler != null)
			handler.clearStatus(id);
	}
}
