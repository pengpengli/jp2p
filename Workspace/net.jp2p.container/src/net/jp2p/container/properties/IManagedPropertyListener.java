/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

import net.jp2p.container.utils.StringStyler;

public interface IManagedPropertyListener<T extends Object, U extends Object> {

	public enum PropertyEvents{
		DEFAULT_VALUE_SET,
		VALUE_CHANGED;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	public void notifyValueChanged( ManagedPropertyEvent<T,U> event );
}
