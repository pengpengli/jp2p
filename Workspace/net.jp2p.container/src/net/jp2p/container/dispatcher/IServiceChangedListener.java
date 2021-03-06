/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.dispatcher;

public interface IServiceChangedListener {
	
	/**
	 * The name of the listener
	 */
	public String getName();
	
	public void notifyServiceChanged( ServiceChangedEvent event );
}
