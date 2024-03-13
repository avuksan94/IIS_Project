package av.task05.xmlrpc05.Config;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory.RequestProcessorFactory;

//https://ws.apache.org/xmlrpc/apidocs/org/apache/xmlrpc/server/RequestProcessorFactoryFactory
@Component
public class SpringRequestProcessorFactoryFactory implements RequestProcessorFactoryFactory {
    private final ApplicationContext applicationContext;

    public SpringRequestProcessorFactoryFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public RequestProcessorFactory getRequestProcessorFactory(Class aClass) throws XmlRpcException {
        return request -> applicationContext.getBean(aClass);
    }
}
