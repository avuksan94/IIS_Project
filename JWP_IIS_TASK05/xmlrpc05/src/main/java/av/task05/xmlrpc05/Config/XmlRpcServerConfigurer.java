package av.task05.xmlrpc05.Config;

import av.task05.xmlrpc05.BL.ServiceImpl.RpcServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//https://stackoverflow.com/questions/36800227/setting-up-a-java-xml-rpc-servlet
@Component
public class XmlRpcServerConfigurer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(XmlRpcServerConfigurer.class);
    private RpcServiceImpl rpcServiceImpl;
    private final ApplicationContext applicationContext;

    public XmlRpcServerConfigurer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting XML-RPC Server...");
        int port = 9095;
        WebServer webServer = new WebServer(port);

        XmlRpcServerConfigImpl serverConfig = new XmlRpcServerConfigImpl();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);
        webServer.getXmlRpcServer().setConfig(serverConfig);

        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.setRequestProcessorFactoryFactory(new SpringRequestProcessorFactoryFactory(applicationContext));
        phm.addHandler("RpcService", RpcServiceImpl.class);

        webServer.getXmlRpcServer().setHandlerMapping(phm);

        webServer.start();
        log.info("XML-RPC Server started on port " + port);
    }
}