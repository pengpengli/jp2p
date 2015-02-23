/*******************************************************************************
 * Copyright (c) 2013 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.jxta.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.Platform;

import net.jp2p.container.AbstractJp2pContainer;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.builder.IJp2pContainerBuilder;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jxse.osgi.compat.CompatibilityEvent;
import net.jxse.osgi.compat.ICompatibilityListener;
import net.jxse.osgi.compat.IJP2PCompatibility;
import net.jxse.osgi.platform.activator.PlatformJxseBuilder;

public class Jp2pCompatBuilder<T extends Object> extends PlatformJxseBuilder<T> implements IJp2pContainerBuilder<T>{

	private String bundle_id;
	private IJP2PCompatibility<T> compat;
	private  IJp2pContainer<T> container;
	private Collection<IContainerBuilderListener<T>> listeners;
				
	public Jp2pCompatBuilder(String bundle_id, IJP2PCompatibility<T> compat ) {
		super( bundle_id, compat );
		this.bundle_id = bundle_id;
		this.compat = compat;
		listeners = new ArrayList<IContainerBuilderListener<T>>();
	}
	
	@Override
	public IJp2pContainer<T> getContainer() {
		return this.container;
	}

	private final void notifyListeners( ContainerBuilderEvent<T> event ){
		for( IContainerBuilderListener<T> listener: listeners ){
			listener.notifyContainerBuilt(event);
		}
	}

	@Override
	public void addContainerBuilderListener( IContainerBuilderListener<T> listener ){
		listeners.add( listener );
	}

	@Override
	public void removeContainerBuilderListener( IContainerBuilderListener<T> listener ){
		listeners.remove( listener );
	}

	/**
	 * Create the container;
	 */
	@Override
	public boolean build(){
		container = new JxtaContainer<T>( this.bundle_id, compat );
		return true;
	}

	@Override
	protected void onRunJxse() {
		ICompatibilityListener cl = new ICompatibilityListener(){

			@Override
			public void notifyNodeChanged(CompatibilityEvent event) {
				notifyListeners( new ContainerBuilderEvent<T>(this, container));
			}	
		};
		
		this.compat.addListener(cl);
		build();
		super.onRunJxse();
		this.compat.removeListener( cl);	
	}


	private static class JxtaContainer<T extends Object> extends AbstractJp2pContainer<T> {

		JxtaContainer( String bundle_id, IJP2PCompatibility<T> compat ) {
			super( new PropertySource( bundle_id, compat.getIdentifier() ));
		}
		
		/**
		 * Create a default property source
		 * @author Kees
		 *
		 */
		private static class PropertySource extends AbstractJp2pWritePropertySource{

			PropertySource( String bundleName, String identifier) {
				super( bundleName, identifier );
				setDirective( IJp2pDirectives.Directives.NAME, identifier );
			}
			
		}
	}
}