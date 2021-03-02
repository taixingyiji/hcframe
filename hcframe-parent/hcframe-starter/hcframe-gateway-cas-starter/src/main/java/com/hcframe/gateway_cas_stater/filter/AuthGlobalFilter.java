package com.hcframe.gateway_cas_stater.filter;

import com.hcframe.gateway_cas_stater.config.CasGatewayClientConfig;
import com.hcframe.gateway_cas_stater.data.DataStorage;
import com.hcframe.gateway_cas_stater.util.TokenProccessor;
import com.hcframe.gateway_cas_stater.util.Utils;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.authentication.ContainsPatternUrlPatternMatcherStrategy;
import org.jasig.cas.client.authentication.ExactUrlPatternMatcherStrategy;
import org.jasig.cas.client.authentication.RegexUrlPatternMatcherStrategy;
import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;
import org.jasig.cas.client.util.ReflectUtils;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author lhc
 * @date 2021-03-02
 */
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private Protocol protocol;

    private UrlPatternMatcherStrategy whiteListUrl;

    private static final Map<String, Class<? extends UrlPatternMatcherStrategy>> WHITE_LIST = new HashMap<>();

    private CasGatewayClientConfig casGatewayClientConfig;

    private DataStorage dataStorage;

    private final TokenProccessor tokenProccessor = TokenProccessor.getInstance();

    public static final String CAS_ASSERTION_KEY = "_const_cas_assertion_";

    static {
        WHITE_LIST.put("CONTAINS", ContainsPatternUrlPatternMatcherStrategy.class);
        WHITE_LIST.put("REGEX", RegexUrlPatternMatcherStrategy.class);
        WHITE_LIST.put("EXACT", ExactUrlPatternMatcherStrategy.class);
    }

    public AuthGlobalFilter(CasGatewayClientConfig casGatewayClientConfig, DataStorage dataStorage) {
        this.casGatewayClientConfig = casGatewayClientConfig;
        this.protocol = Protocol.CAS2;
        Class<?> ignoreUrlPatternClass = WHITE_LIST.get(casGatewayClientConfig.getIgnoreUrlPatternType());
        this.whiteListUrl = ReflectUtils.newInstance(ignoreUrlPatternClass.getName(), new Object[0]);
        if (this.whiteListUrl != null) {
            this.whiteListUrl.setPattern(casGatewayClientConfig.whiteUrl);
        }
        this.dataStorage = dataStorage;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (isWhiteList(request)) {
            return chain.filter(exchange);
        }
        Object authId = request.getCookies().get(casGatewayClientConfig.authKey);
        if (StringUtils.isEmpty(authId)) {
            authId = this.tokenProccessor.makeToken();
            response.addCookie(ResponseCookie.from(casGatewayClientConfig.authKey, authId.toString()).build());
        }
        Assertion assertion = (Assertion) dataStorage.getValue(authId.toString(), CAS_ASSERTION_KEY);
        if (!StringUtils.isEmpty(assertion)) {
            return chain.filter(exchange);
        } else {
            String serviceUrl = Utils.encodingUrl(request, true, false);
            String urlToRedirectTo = Utils.makeRedirectUrl(casGatewayClientConfig.casServiceUrl + casGatewayClientConfig.casContextPath + casGatewayClientConfig.loginUrl, this.protocol.getServiceParameterName(), serviceUrl);
            return Utils.redirect(exchange, urlToRedirectTo);
        }
    }

    @Override
    public int getOrder() {
        return -1000;
    }

    private boolean isWhiteList(ServerHttpRequest request) {
        if (this.whiteListUrl == null) {
            return false;
        } else {
            StringBuilder urlBuffer = new StringBuilder(request.getURI().toString());
            if (request.getURI().getQuery() != null) {
                urlBuffer.append("?").append(request.getURI().getQuery());
            }
            String requestUri = urlBuffer.toString();
            return this.whiteListUrl.matches(requestUri);
        }
    }

}
