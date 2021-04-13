package com.hcframe.gateway_cas_stater.util;


import com.hcframe.gateway_cas_stater.config.CasGatewayClientConfig;
import org.jasig.cas.client.validation.Cas30ProxyTicketValidator;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

public class Utils {

    private static final String QUESTION_MARK = "?";
    private static final String TICKET = "ticket";
    private static final String EQUAL = "=";
    private static final String AND = "&";
    private static final String UTF8 = "utf-8";

    public static String getParameter(ServerHttpRequest request, String parameter) {
        if (request.getQueryParams().containsKey(parameter)) {
            List<String> list = request.getQueryParams().get(parameter);
            return list.get(list.size()-1);
        }
        return null;
    }

    public static String exceptTicket(ServerHttpRequest request) {
        MultiValueMap<String, String> multiValueMap = request.getQueryParams();
        StringBuilder stringBuffer = new StringBuilder();
        for (MultiValueMap.Entry<String, List<String>> entry : multiValueMap.entrySet()) {
            if (!"ticket".equals(entry.getKey())) {
                stringBuffer.append(entry.getKey()).append("=").append(entry.getValue().get(0)).append("&");
            }
        }
        if (stringBuffer.length() == 0) {
            return "";
        }
        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    public static String encodingUrl(ServerHttpRequest request, boolean isEncode, boolean isTicket) {
        String url = request.getURI().toString();
        url = "http://" + request.getURI().getHost() + ":" + request.getURI().getPort() + request.getURI().getPath();
        String query = exceptTicket(request);
        if (!StringUtils.isEmpty(query)) {
            url = url + QUESTION_MARK + query;
        }
        HttpHeaders headers = request.getHeaders();
        List<String> forwardedUrl = headers.get("x-forwarded-host");
        if (!StringUtils.isEmpty(forwardedUrl)) {
            url = "http://" + forwardedUrl.get(0) + request.getURI().getPath();
            if (!StringUtils.isEmpty(query)) {
                url = url + QUESTION_MARK + query;
            }
        }
        if (isTicket) {
            if (url.contains(QUESTION_MARK)) {
                int i = url.indexOf(QUESTION_MARK);
                String pureUrl = url.substring(0, i + 1);
                StringBuilder stringBuilder = new StringBuilder(pureUrl);
                MultiValueMap<String, String> map = request.getQueryParams();
                Set<String> set = map.keySet();
                int j = 0;
                for (String str : set) {
                    j++;
                    if (!TICKET.equals(str)) {
                        stringBuilder.append(str).append(EQUAL).append(map.getFirst(str)).append(AND);
                    }
                }
                return stringBuilder.substring(0, stringBuilder.length() - 1);
            } else {
                return url;
            }
        }
        try {
            return isEncode ? URLEncoder.encode(url, UTF8) : url;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean preFilter(ServerHttpRequest request, String proxyReceptorUrl) {
        String requestUri = request.getURI().toString();
        return StringUtils.isEmpty(proxyReceptorUrl) || !requestUri.endsWith(proxyReceptorUrl);
    }

    public static Mono<Void> redirect(ServerWebExchange exchange, String redirect) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location", redirect);
        return exchange.getResponse().setComplete();
    }

    public static String makeRedirectUrl(String casServerLoginUrl, String serviceParameterName, String serviceUrl) {
        return casServerLoginUrl + (casServerLoginUrl.contains("?") ? "&" : "?") + serviceParameterName + "=" + serviceUrl;
    }

    public static TicketValidator initTicketValidator(CasGatewayClientConfig casGatewayClientConfig) {
        TicketValidator ticketValidator;
        if (!casGatewayClientConfig.getAcceptAnyProxy()) {
            Cas30ServiceTicketValidator cas30ServiceTicketValidator= new Cas30ServiceTicketValidator(casGatewayClientConfig.casServiceUrl + casGatewayClientConfig.casContextPath);
            cas30ServiceTicketValidator.setEncoding("UTF-8");
            ticketValidator = cas30ServiceTicketValidator;

        } else {
            Cas30ProxyTicketValidator cas30ProxyTicketValidator = new Cas30ProxyTicketValidator(casGatewayClientConfig.casServiceUrl + casGatewayClientConfig.casContextPath);
            cas30ProxyTicketValidator.setEncoding("UTF-8");
            ticketValidator = cas30ProxyTicketValidator;
        }
        return ticketValidator;
    }


}
