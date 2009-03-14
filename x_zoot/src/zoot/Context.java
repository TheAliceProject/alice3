/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package zoot;

/**
 * @author Dennis Cosgrove
 */
public interface Context {
	public java.util.EventObject getEvent();
	public <E extends Object> E get( Object key, Class<E> cls );
	public Object get( Object key );
	public void put( Object key, Object value );
	public boolean isCommitted();
	public boolean isCancelled();
	public void commit();
	public void cancel();
	public boolean isCancelWorthwhile();
	public ActionContext perform( ActionOperation operation, java.util.EventObject o, boolean isCancelWorthwhile );
	public SingleSelectionContext perform( SingleSelectionOperation operation, java.util.EventObject o, boolean isCancelWorthwhile, Object prevSelection, Object nextSelection );
}
