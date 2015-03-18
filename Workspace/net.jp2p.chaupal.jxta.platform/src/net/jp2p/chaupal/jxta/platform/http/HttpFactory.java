/*******************************************************************************
 * Copyright 2014 Chaupal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.http;

import net.jp2p.container.partial.PartialFactory;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;

public class HttpFactory extends PartialFactory {

	public HttpFactory() {
		super(JxtaPlatformComponents.HTTP.toString());
	}
}
