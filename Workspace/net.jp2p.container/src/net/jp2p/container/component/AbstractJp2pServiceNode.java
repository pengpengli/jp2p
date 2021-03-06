/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.component;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.container.Jp2pContainer;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;

public abstract class AbstractJp2pServiceNode<T extends Object>
		extends AbstractJp2pService<T> implements IJp2pComponentNode<T> {

	private Collection<IJp2pComponent<?>> children;

	protected AbstractJp2pServiceNode( IJp2pComponentNode<?> source, IComponentFactory<T> factory  ) {
		super( factory );
		this.children = new ArrayList<IJp2pComponent<?>>();
	}
	
	protected AbstractJp2pServiceNode(
			IJp2pWritePropertySource<IJp2pProperties> source, T module) {
		super(source, module);
		this.children = new ArrayList<IJp2pComponent<?>>();
	}

	protected AbstractJp2pServiceNode(String bundleId, String componentName) {
		super(bundleId, componentName);
		this.children = new ArrayList<IJp2pComponent<?>>();
	}


	@Override
	public boolean isRoot() {
		return (super.getPropertySource().getParent() == null );
	}

	@Override
	public boolean addChild( IJp2pComponent<?> child ){
		this.children.add( child );
		String identifier = AbstractJp2pPropertySource.getBundleId( super.getPropertySource());
		notifyComponentChanged( new ComponentChangedEvent<IJp2pComponent<?>>( this, child, identifier, Jp2pContainer.ServiceChange.CHILD_ADDED ));
		return true;
	}

	@Override
	public void removeChild( IJp2pComponent<?> child ){
		this.children.remove( child );
		String identifier = AbstractJp2pPropertySource.getBundleId( super.getPropertySource());
		notifyComponentChanged( new ComponentChangedEvent<IJp2pComponent<?>>( this, child, identifier, Jp2pContainer.ServiceChange.CHILD_REMOVED ));
	}

	protected void notifyComponentChanged( ComponentChangedEvent<?> event){
		super.getDispatcher().serviceChanged( event );		
	}
	
	@Override
	public IJp2pComponent<?>[] getChildren(){
		return this.children.toArray(new IJp2pComponent<?>[ this.children.size() ]);
	}

	@Override
	public boolean hasChildren() {
		return !this.children.isEmpty();
	}
}