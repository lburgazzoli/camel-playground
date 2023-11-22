package com.github.lburgazzoli;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.camel.Route;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.spi.RouteError;
import org.apache.camel.support.RoutePolicySupport;

@ApplicationScoped
public class MyRoutes extends EndpointRouteBuilder {
    @Override
    public void configure() throws Exception {       

        from("timer:tick")
            .routePolicy(new RoutePolicySupport() {
                @Override
                public void onInit(Route route) {
                    throw new RuntimeCamelException("fail");    
                }
            })
            .to("log:info?showAll=true");
    }
}