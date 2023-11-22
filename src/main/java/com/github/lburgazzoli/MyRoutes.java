package com.github.lburgazzoli;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.camel.Route;

import java.util.Map;

import org.apache.camel.CamelException;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.impl.engine.DefaultRouteError;
import org.apache.camel.spi.RouteError;
import org.apache.camel.support.DefaultComponent;
import org.apache.camel.support.DefaultConsumer;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.camel.support.DefaultProducer;
import org.apache.camel.support.LoggingExceptionHandler;
import org.apache.camel.support.RoutePolicySupport;

@ApplicationScoped
public class MyRoutes extends EndpointRouteBuilder {
    @Override
    public void configure() throws Exception {       
        getContext().addComponent("foo", new MyComponent());

        from("foo:bar")
            .to("log:info?showAll=true");
    }

    static class MyComponent extends DefaultComponent {

        @Override
        protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters)
                throws Exception {
            return new MyEndpoint(uri, this);
        }
    }

    static class MyEndpoint extends DefaultEndpoint {

        public MyEndpoint(String uri, Component component) {
            super(uri, component);
        }

        @Override
        public Producer createProducer() throws Exception {
            return new DefaultProducer(this) {
                @Override
                public void doStart() throws Exception {        
                    throw new CamelException("failure");            
                }
                @Override
                public void process(Exchange exchange) throws Exception {
                }
            };
        }

        @Override
        public Consumer createConsumer(Processor processor) throws Exception {
            return new DefaultConsumer(this, processor) {
                @Override
                public void doStart() throws Exception {        
                    throw new CamelException("failure");            
                }
            };
        }
        
    }
}