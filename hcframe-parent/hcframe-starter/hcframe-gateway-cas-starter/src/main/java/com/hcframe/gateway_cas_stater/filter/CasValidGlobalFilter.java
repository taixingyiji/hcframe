package com.hcframe.gateway_cas_stater.filter;

import com.hcframe.gateway_cas_stater.config.CasGatewayClientConfig;
import com.hcframe.gateway_cas_stater.data.DataStorage;
import com.hcframe.gateway_cas_stater.util.Utils;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.validation.*;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lhc
 */
public class CasValidGlobalFilter implements GlobalFilter, Ordered {

    protected Cas20ServiceTicketValidator defaultServiceTicketValidator;
    protected Cas20ProxyTicketValidator defaultProxyTicketValidator;

    protected TicketValidator ticketValidator;

    protected Protocol protocol;

    protected CasGatewayClientConfig casGatewayClientConfig;

    private final DataStorage dataStorage;

    public CasValidGlobalFilter(CasGatewayClientConfig casGatewayClientConfig, DataStorage dataStorage) {
        this.casGatewayClientConfig = casGatewayClientConfig;
        this.protocol = Protocol.CAS3;
        this.defaultServiceTicketValidator = new Cas30ServiceTicketValidator(casGatewayClientConfig.casServiceUrl);
        this.defaultProxyTicketValidator = new Cas30ProxyTicketValidator(casGatewayClientConfig.casServiceUrl);
        this.dataStorage = dataStorage;
        this.ticketValidator = Utils.initTicketValidator(casGatewayClientConfig);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (Utils.preFilter(request, casGatewayClientConfig.proxyReceptorUrl)) {
            String ticket = Utils.getParameter(request, this.protocol.getArtifactParameterName());
            if (StringUtils.isEmpty(ticket)) {
                return chain.filter(exchange);
            } else {
                Assertion assertion;
                try {
                    assertion = ticketValidator.validate(ticket, Utils.encodingUrl(request, true, true));

                    Object authId = request.getCookies().get(casGatewayClientConfig.authKey);
                    if (authId == null) {
                        return chain.filter(exchange);
                    }
                    dataStorage.setValue(authId.toString(), AuthGlobalFilter.CAS_ASSERTION_KEY, assertion);
                    return Utils.redirect(exchange, Utils.encodingUrl(request, true, true));
                } catch (TicketValidationException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return chain.filter(exchange);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -1001;
    }
}
