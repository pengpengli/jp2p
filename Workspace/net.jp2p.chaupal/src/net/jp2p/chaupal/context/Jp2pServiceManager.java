package net.jp2p.chaupal.context;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.chaupal.activator.Jp2pBundleActivator;
import net.jp2p.container.builder.IFactoryBuilder;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.context.Jp2pLoaderEvent;
import net.jp2p.container.context.IContextLoaderListener;
import net.jp2p.container.context.Jp2pServiceDescriptor;
import net.jp2p.container.context.Jp2pServiceLoader;
import net.jp2p.container.factory.IPropertySourceFactory;

public class Jp2pServiceManager{

	public static final String S_CONTEXT_FOUND = "The following context was found and registered: ";
	public static final String S_INFO_BUILDING = "All the required services have been found. Start to build the container ";
	
	private Jp2pServiceLoader loader;

	private Collection<Jp2pServiceDescriptor> descriptors;
	private Collection<IServiceManagerListener> listeners;
	private Jp2pBundleActivator activator;
	private Collection<ContextServiceParser> parsers;

	private boolean completed;

	private IContextLoaderListener listener = new IContextLoaderListener() {
		
		@Override
		public void notifyRegisterContext(Jp2pLoaderEvent event) {
			logger.info( "Builder registered: " + event.getBuilder().getName() );
			switch( event.getType() ){

			case REGISTERED:
				if( completed )
					break;
				updateServiceDescriptors( event.getBuilder() );
				if( !completed )
					break;
				logger.info(S_INFO_BUILDING);
				for( IServiceManagerListener listener: listeners )
					listener.notifyContainerBuilt( new ServiceManagerEvent( this ));
				break;
			case UNREGISTERED:
				updateServiceDescriptors( event.getBuilder() );
				break;
			}
		}
		
		@Override
		public void notifyUnregisterContext(Jp2pLoaderEvent event) {
			for( Jp2pServiceDescriptor info: descriptors ){
				if( event.getBuilder().hasFactory(info))
					if(( info.getContext() != null ) || ( event.getBuilder().getName().equals( info.getContext() )))
						info.setFound( false );
			}
		}
	};
	
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );

	public Jp2pServiceManager( Jp2pBundleActivator activator, Jp2pServiceLoader contextLoader ) {
		this.loader = contextLoader;
		this.activator = activator;
		this.loader.addContextLoaderListener(listener);
		descriptors = new TreeSet<Jp2pServiceDescriptor>();
		listeners = new ArrayList<IServiceManagerListener>();
		parsers = new ArrayList<ContextServiceParser>();
		this.completed = false;		
	}

	/**
	 * Returns true if the manager supports a factory with the given context and name
	 * @param componentName
	 * @return
	 */
	public boolean hasFactory( String context, String componentName  ){
		Jp2pServiceDescriptor descriptor = new Jp2pServiceDescriptor( componentName, context );
		return this.loader.hasFactory( descriptor );
	}

	/**
	 * Returns true if the manager supports a factory with the given context and name
	 * @param componentName
	 * @return
	 */
	public IPropertySourceFactory getFactory( String context, String componentName  ){
		return this.loader.getFactory(context, componentName);
	}

	public void addListener( IServiceManagerListener listener ){
		this.listeners.add( listener );
	}

	public void removeListener( IServiceManagerListener listener ){
		this.listeners.remove( listener );
	}

	/**
	 * First we load the service descriptors
	 * by checking the available services
	 * @param builder
	 */
	protected void loadServiceDescriptors() {
		//We parse the jp2p xml file to see which services we need, and then include the contexts we find
		try {
			extendParsers( activator.getClass() );
			extendParsers( Jp2pServiceManager.class );
		} catch (IOException e) {
			e.printStackTrace();
		}

		//first we parse the xml files to determine which services we need 
		for(ContextServiceParser parser: parsers ){
			descriptors.addAll( parser.parse() );
		}
	}

	/**
	 * Update the service descriptor objects that are needed to build the JP2P container,
	 * by checking the available services
	 * @param builder
	 */
	protected void updateServiceDescriptors( IJp2pServiceBuilder builder ) {
		
		for( Jp2pServiceDescriptor info: descriptors ){
			if( builder.hasFactory( info ) ){
				String context = builder.getName();
				if(( info.getContext() == null ) || ( context.equals( info.getContext() ))){
					info.setContext( builder.getName());
					info.setFound( true );
					this.isCompleted();
				}
			}
		}
	}

	/**
	 * Sets and returns true if the registered builders are able to build all the factories from the
	 * list of descriptors
	 */
	private boolean isCompleted() {
		Logger log = Logger.getLogger( this.getClass().getName() );
		for( Jp2pServiceDescriptor info: descriptors ){
			if( info.isOptional())
				continue;
			if( !info.isFound()){
				log.log( Level.WARNING, "waiting for: " + info.getName());
				this.completed = false;
				return completed;
			}
		}
		log.log( Level.INFO, "Building completed: " + activator.getBundleId() );
		this.completed = true;
		return this.completed;
	}

	/**
	 * Open the manager
	 * @return 
	 */
	public boolean open(){
		this.loadServiceDescriptors();
		loader.addContextLoaderListener(listener);
		return true;
	}

	public void close(){
		loader.removeContextLoaderListener(listener);
		listener = null;
	}

	/**
	 * Allow additional builders to extend the primary builder, by looking at resources with the
	 * similar name and location, for instance provided by fragments
	 * @param clss
	 * @param containerBuilder
	 * @throws IOException
	 */
	private void extendParsers( Class<?> clss ) throws IOException{
		Enumeration<URL> enm = clss.getClassLoader().getResources( IFactoryBuilder.S_DEFAULT_LOCATION );
		while( enm.hasMoreElements()){
			URL url = enm.nextElement();
			parsers.add( new ContextServiceParser( loader, url, clss ));
		}
	}
}