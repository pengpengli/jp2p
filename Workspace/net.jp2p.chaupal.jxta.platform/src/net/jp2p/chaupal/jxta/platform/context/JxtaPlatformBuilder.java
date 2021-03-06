/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.context;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.chaupal.jxta.platform.NetworkManagerFactory;
import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationFactory;
import net.jp2p.chaupal.jxta.platform.http.Http2Factory;
import net.jp2p.chaupal.jxta.platform.http.HttpFactory;
import net.jp2p.chaupal.jxta.platform.infra.InfrastructureFactory;
import net.jp2p.chaupal.jxta.platform.multicast.MulticastFactory;
import net.jp2p.chaupal.jxta.platform.security.SecurityFactory;
import net.jp2p.chaupal.jxta.platform.seeds.SeedListFactory;
import net.jp2p.chaupal.jxta.platform.tcp.TcpFactory;
import net.jp2p.container.context.AbstractJp2pServiceBuilder;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.jxta.context.IJxtaBuilder;
import net.jp2p.jxta.netpeergroup.NetPeerGroupFactory;
import net.jxta.peergroup.IModuleDefinitions.DefaultModules;
import net.jxta.peergroup.core.ModuleClassID;

public class JxtaPlatformBuilder extends AbstractJp2pServiceBuilder implements IJxtaBuilder {

	public JxtaPlatformBuilder() {
		super( Contexts.PLATFORM.toString());
	}

	@Override
	protected void prepare() {
		super.addFactory( new NetworkManagerFactory( ));
		super.addFactory( new NetworkConfigurationFactory( ));
		super.addFactory( new NetPeerGroupFactory());
		super.addFactory( new SecurityFactory());
		super.addFactory( new TcpFactory() );
		super.addFactory( new HttpFactory() );
		super.addFactory( new Http2Factory() );
		super.addFactory( new MulticastFactory() );
		super.addFactory( new InfrastructureFactory() );
		super.addFactory( new SeedListFactory());
	}

	@Override
	public ModuleClassID[] getSupportedModuleClassIDs() {
		Collection<ModuleClassID> ids = new ArrayList<ModuleClassID>();
		for( DefaultModules dm: DefaultModules.values()){
			switch( dm ){
			case HTTP:
			case TCP:
				break;
			default:
				ids.add( DefaultModules.getModuleClassID(dm));
				break;
			}
		}		
		return ids.toArray( new ModuleClassID[ ids.size() ]);
	}

	@Override
	public Object createValue( String componentName, IJp2pProperties id ){
		return null;
	}
	
	@Override
	public int compareTo(IJp2pServiceBuilder o) {
		return this.getName().compareTo( o.getName() );
	}
}
