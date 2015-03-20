package net.jp2p.chaupal.jxta.platform.configurator;

import java.util.Iterator;

import net.jp2p.chaupal.jxta.platform.NetworkManagerPropertySource;
import net.jp2p.chaupal.jxta.platform.NetworkManagerPropertySource.NetworkManagerProperties;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jxta.platform.NetworkManager.ConfigMode;

public class NetworkConfigurationPropertySource extends AbstractJp2pWritePropertySource
	implements IJp2pWritePropertySource<IJp2pProperties>

{
	public enum NetworkConfiguratorProperties implements IJp2pProperties{
		DESCRIPTION,
		HOME,
		CONFIG_MODE,
		INFRASTRUCTURE_8NAME,
		INFRASTRUCTURE_8DESCRIPTION,
		INFRASTRUCTURE_8ID,
		MULTICAST_8ENABLED,
		MULTICAST_8ADDRESS,
		MULTICAST_8INTERFACE,
		MULTICAST_8POOL_SIZE,
		MULTICAST_8PORT,
		MULTICAST_8SIZE,
		MULTICAST_8STATUS,
		MULTICAST_8USE_ONLY_RELAY_SEEDS,
		MULTICAST_8USE_ONLY_RENDEZVOUS_SEEDS,
		NAME,
		PEER_ID,
		SECURITY_8AUTHENTICATION_TYPE,
		SECURITY_8CERTFICATE,
		SECURITY_8CERTIFICATE_CHAIN,
		SECURITY_8KEY_STORE_LOCATION,
		SECURITY_8PASSWORD,
		SECURITY_8PRINCIPAL,
		SECURITY_8PRIVATE_KEY,
		RELAY_8MAX_CLIENTS,
		RELAY_8SEEDING_URIS,
		RELAY_8SEED_URIS,
		RENDEZVOUS_8MAX_CLIENTS,
		RENDEZVOUS_8SEEDING_URIS,
		RENDEZVOUS_8SEED_URIS,
		STORE_HOME,
		USE_ONLY_RELAY_SEEDS,
		USE_ONLY_RENDEZVOUS_SEEDS;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		/**
		 * Returns true if the given property is valid for this enumeration
		 * @param property
		 * @return
		 */
		public static boolean isValidProperty( IJp2pProperties property ){
			if( property == null )
				return false;
			for( NetworkConfiguratorProperties prop: values() ){
				if( prop.equals( property ))
					return true;
			}
			return false;
		}

		public static NetworkConfiguratorProperties convertTo( String str ){
			String enumStr = StringStyler.styleToEnum( str );
			return valueOf( enumStr );
		}
	}

	public NetworkConfigurationPropertySource( NetworkManagerPropertySource nmps ) {
		super( JxtaPlatformComponents.NETWORK_CONFIGURATOR.toString(), nmps );
		this.fill();
	}

	private void fill(){
		NetworkManagerPropertySource source = (NetworkManagerPropertySource) super.getParent();
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		NetworkConfiguratorProperties nmp = null;
		while( iterator.hasNext() ){
			IJp2pProperties cp = iterator.next();
			nmp = convertTo( cp );
			if( nmp != null ){
				Object value = source.getProperty( cp );
				super.setProperty(nmp, value, true);
			}
		}
		if( Utils.isNull( (String) super.getProperty( NetworkConfiguratorProperties.SECURITY_8PRINCIPAL )))
			super.setProperty( NetworkConfiguratorProperties.SECURITY_8PRINCIPAL, getBundleId(source) );
		//super.setProperty( NetworkConfiguratorProperties.TCP_8ENABLED, source.isEnabled() );
		//super.setProperty( NetworkConfiguratorProperties.HTTP_8ENABLED, true );
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return false;
	}

	/**
	 * convert the given context property to a networkManagerProperty, or null if there is
	 * no relation between them
	 * @param context
	 * @return
	 */
	public static NetworkManagerProperties convertFrom( NetworkConfiguratorProperties context ){
		switch( context ){
		case NAME:
			return NetworkManagerProperties.INSTANCE_NAME;
		case HOME:
			return NetworkManagerProperties.INSTANCE_HOME;
		case PEER_ID:
			return NetworkManagerProperties.PEER_ID;
		default:
			break;
		}
		return null;
	}

	/**
	 * convert the given context property to a networkManagerProperty, or null if there is
	 * no relation between them
	 * @param context
	 * @return
	 */
	public static NetworkConfiguratorProperties convertTo( IJp2pProperties props ){
		if(!( props instanceof NetworkManagerProperties ))
			return null;
		NetworkManagerProperties id = (NetworkManagerProperties) props;
		switch( id ){
		case INSTANCE_NAME:
			return NetworkConfiguratorProperties.NAME;			
		case INSTANCE_HOME:
			return NetworkConfiguratorProperties.STORE_HOME;
		case PEER_ID:
			return NetworkConfiguratorProperties.PEER_ID;
		default:
			break;
		}
		return null;
	}

	/**
	 * Get the config modes as string
	 * @return
	 */
	public static final String[] getConfigModes(){
		ConfigMode[] modes = ConfigMode.values();
		String[] results = new String[ modes.length];
		for( int i=0; i<modes.length; i++ ){
			ConfigMode mode = modes[i];
			results[i] = mode.toString();
		}
		return results;
	}

	private static class Convertor extends SimplePropertyConvertor{

		public Convertor(IJp2pPropertySource<IJp2pProperties> source) {
			super(source);
		}

		@Override
		public NetworkConfiguratorProperties getIdFromString(String key) {
			return NetworkConfiguratorProperties.valueOf( key );
		}
	}
}