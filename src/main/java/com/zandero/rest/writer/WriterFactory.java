package com.zandero.rest.writer;

import com.zandero.rest.data.ClassFactory;
import com.zandero.rest.data.RouteDefinition;
import com.zandero.rest.exception.ClassFactoryException;
import com.zandero.utils.Assert;
import io.vertx.core.http.HttpServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Provides definition and caching of response writer implementations
 */
public class WriterFactory extends ClassFactory<HttpResponseWriter> {

	private final static Logger log = LoggerFactory.getLogger(WriterFactory.class);

	public WriterFactory() {

		super();
	}

	@Override
	protected void init() {

		classTypes.put(Response.class, JaxResponseWriter.class);
		classTypes.put(HttpServerResponse.class, VertxResponseWriter.class);

		mediaTypes.put(MediaType.APPLICATION_JSON, JsonResponseWriter.class);
		mediaTypes.put(MediaType.TEXT_PLAIN, GenericResponseWriter.class);
	}

	/**
	 * Finds assigned response writer or tries to assign a writer according to produces annotation and result type
	 *
	 * @param returnType type of result
	 * @param definition method definition
	 * @return writer to be used to produce response, or {@link GenericResponseWriter} in case no suitable writer could be found
	 */
	public HttpResponseWriter getResponseWriter(Class<?> returnType, RouteDefinition definition) {

		try {
			HttpResponseWriter writer = get(returnType, definition.getWriter(), definition.getProduces());
			return writer != null ? writer : new GenericResponseWriter();
		} catch (ClassFactoryException e) {
			log.error("Failed to provide response writer: " + returnType + ", for: " + definition + ", falling back to GenericResponseWriter() instead!");
			return new GenericResponseWriter();
		}
	}

	public void register(Class<?> aClass, Class<? extends HttpResponseWriter> clazz) {

		Assert.notNull(aClass, "Missing response class!");
		Assert.notNull(clazz, "Missing response writer type class");

		super.register(aClass, clazz);
	}

	public void register(String mediaType, Class<? extends HttpResponseWriter> clazz) {

		super.register(mediaType, clazz);
	}

	public void register(MediaType mediaType, Class<? extends HttpResponseWriter> clazz) {

		super.register(mediaType, clazz);
	}
}
