
package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.core.service.BdcDataSynServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 */
@WebServiceClient(name = "BDCDataSynServerClass", targetNamespace = "http://tempuri.org/", wsdlLocation = "")
public class BdcDataSynServerImpl extends Service {

    private static URL BDCDATASYNSERVERCLASS_WSDL_LOCATION = null;
    protected static final Logger logger = LoggerFactory.getLogger(BdcDataSynServerImpl.class);

    public static void setUrl(String urlStr) {
        if (BDCDATASYNSERVERCLASS_WSDL_LOCATION == null) {
            URL url = null;
            try {
                url = new URL(urlStr);
            } catch (MalformedURLException e) {
                logger.error("BdcDataSynServerImpl.setUrl",e);
            }
            BDCDATASYNSERVERCLASS_WSDL_LOCATION = url;
        }

    }


    public BdcDataSynServerImpl(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BdcDataSynServerImpl() {
        super(BDCDATASYNSERVERCLASS_WSDL_LOCATION, new QName("http://tempuri.org/", "BDCDataSynServerClass"));
    }

    /**
     * @return returns IBDCDataSynServer
     */
    @WebEndpoint(name = "BasicHttpBinding_IBDCDataSynServer")
    public BdcDataSynServerService getBasicHttpBindingIBDCDataSynServer() {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_IBDCDataSynServer"), BdcDataSynServerService.class);
    }

    /**
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns IBDCDataSynServer
     */
    @WebEndpoint(name = "BasicHttpBinding_IBDCDataSynServer")
    public BdcDataSynServerService getBasicHttpBindingIBDCDataSynServer(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_IBDCDataSynServer"), BdcDataSynServerService.class, features);
    }

}