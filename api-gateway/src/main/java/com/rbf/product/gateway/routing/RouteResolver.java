package com.rbf.product.gateway.routing;

import com.rbf.product.gateway.config.GatewayRoutesProperties;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Optional;

@Component
public class RouteResolver {

    private final GatewayRoutesProperties properties;

    public RouteResolver(GatewayRoutesProperties properties) {
        this.properties = properties;
    }

    public Optional<ResolvedRoute> resolve(String requestPath) {
        return properties.getRoutes().values().stream()
                .filter(route -> matches(route, requestPath))
                .max(Comparator.comparingInt(route -> route.getPathPrefix().length()))
                .map(route -> new ResolvedRoute(route, stripPrefix(requestPath, route.getPathPrefix())));
    }

    private boolean matches(GatewayRoutesProperties.Route route, String requestPath) {
        String prefix = route.getPathPrefix();
        return requestPath.equals(prefix) || requestPath.startsWith(prefix + "/");
    }

    private String stripPrefix(String requestPath, String prefix) {
        String downstreamPath = requestPath.substring(prefix.length());
        return downstreamPath.isBlank() ? "/" : downstreamPath;
    }
}
