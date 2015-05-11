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
package net.jp2p.chaupal.sequencer;

public interface IJp2pBundleSequencer<T extends Object> {

	public abstract String getBundleId();

	public void addListener( IJp2pBundleSequencer<T> listener );

	public void removeListener( IJp2pBundleSequencer<T> listener );
	
	/**
	 * When the client registers an even, the other sequencers are notified
	 * @param event
	 */
	public abstract void registerEvent(SequencerEvent<T> event);

	public abstract void notifyBundleActive(SequencerEvent<T> event);

}