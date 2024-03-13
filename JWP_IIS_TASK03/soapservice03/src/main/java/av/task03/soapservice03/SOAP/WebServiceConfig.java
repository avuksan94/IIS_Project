package av.task03.soapservice03.SOAP;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

//https://medium.com/@extio/developing-soap-web-services-with-spring-boot-a-comprehensive-guide-1d4f89bc3127
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }
    @Bean(name = "workouts")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema workoutsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("WorkoutsPort");
        wsdl11Definition.setLocationUri("/soap-api");
        wsdl11Definition.setTargetNamespace("http://avtest.com/soap-web-service");
        wsdl11Definition.setSchema(workoutsSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema workoutsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schema2.xsd"));
    }
}