package com.hcframe.gateway_cas_stater.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


public class SimpleCORSFilter implements GlobalFilter, Ordered {


	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		HttpHeaders requestHeaders =request.getHeaders();
		String myOrigin = requestHeaders.getOrigin();
		HttpHeaders responseHeaders = response.getHeaders();
		responseHeaders.setAccessControlAllowOrigin(myOrigin);
		List<HttpMethod> methods = new ArrayList<>();
		methods.add(HttpMethod.DELETE);
		methods.add(HttpMethod.GET);
		methods.add(HttpMethod.PUT);
		methods.add(HttpMethod.OPTIONS);
		methods.add(HttpMethod.POST);
		responseHeaders.setAccessControlAllowMethods(methods);
		responseHeaders.setAccessControlMaxAge(3600L);
		List<String> headers = new ArrayList<>();
		headers.add("x-requested-with");
		headers.add("X-Access-Token");
		headers.add("datasource-Key");
		responseHeaders.setAccessControlAllowHeaders(headers);
		responseHeaders.setAccessControlAllowCredentials(true);
		// 是否支持cookie跨域
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return -1002;
	}
}
