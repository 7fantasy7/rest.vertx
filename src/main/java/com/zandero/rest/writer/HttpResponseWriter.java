package com.zandero.rest.writer;

import com.zandero.rest.data.RouteDefinition;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import javax.ws.rs.core.MediaType;

/**
 * Response writer interface to implement
 * use RestRouter.getWriters().register(...) to register a global writer
 * or use @ResponseWriter annotation to associate REST with given writer
 */
public interface HttpResponseWriter<T> {

	void write(T result, HttpServerRequest request, HttpServerResponse response);

	default void addResponseHeaders(RouteDefinition definition, HttpServerResponse response) {

		if (!response.ended() &&
			!response.headers().contains(HttpHeaders.CONTENT_TYPE)) {

			if (definition.getProduces() != null) {
				for (MediaType produces : definition.getProduces()) {
					response.putHeader(HttpHeaders.CONTENT_TYPE, produces.toString());
				}
			}
			else {
				response.putHeader(HttpHeaders.CONTENT_TYPE, MediaType.WILDCARD);
			}
		}
	}
}
