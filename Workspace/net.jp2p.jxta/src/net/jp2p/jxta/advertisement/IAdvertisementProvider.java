/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.advertisement;

import net.jxta.document.Advertisement;

public interface IAdvertisementProvider {
	
	/**
	 * An advertisement provider provides for a list of advertisements
	 * @return
	 */
	public Advertisement[] getAdvertisements();
}
