package com.rbf.product.gateway.routing;

import com.rbf.product.gateway.config.GatewayRoutesProperties;

public class ResolvedRoute {

    private final GatewayRoutesProperties.Route route;
    private final String downstreamPath;

    public ResolvedRoute(GatewayRoutesProperties.Route route, String downstreamPath) {
        this.route = route;
        this.downstreamPath = downstreamPath;
    }

    public GatewayRoutesProperties.Route getRoute() {
        return route;
    }

    public String getDownstreamPath() {
        return downstreamPath;
    }
}
