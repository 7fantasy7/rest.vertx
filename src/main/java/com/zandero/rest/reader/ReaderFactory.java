package com.zandero.rest.reader;

import com.zandero.rest.data.ClassFactory;
import com.zandero.rest.data.MethodParameter;
import com.zandero.rest.data.RouteDefinition;
import com.zandero.rest.exception.ClassFactoryException;
import com.zandero.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

/**
 * Provides definition and caching of request body reader implementations
 */
public class ReaderFactory extends ClassFactory<HttpRequestBodyReader> {

	private final static Logger log = LoggerFactory.getLogger(ReaderFactory.class);

	public ReaderFactory() {

		super();
	}

	@Override
	protected void init() {

		classTypes.put(String.class, GenericBodyReader.class);

		mediaTypes.put(MediaType.APPLICATION_JSON, JsonBodyReader.class);
		mediaTypes.put(MediaType.TEXT_PLAIN, GenericBodyReader.class);
	}

	/**
	 * Provides request body converter
	 *
	 * @param definition route definition
	 * @return reader to convert request body
	 */
	public HttpRequestBodyReader getRequestBodyReader(RouteDefinition definition) {

		Class<?> readerType = null;

		try {
			// find body argument
			MethodParameter parameter = definition.getBodyParameter();
			Assert.notNull(parameter, "No body reader present for " + definition + ", register body reader via @RequestReader or RestRouter.getReaders().register()");

			readerType = parameter.getDataType();

			HttpRequestBodyReader reader = get(readerType, definition.getReader(), definition.getConsumes());
			return reader != null ? reader : new GenericBodyReader();
		} catch (ClassFactoryException e) {
			log.error("Failed to provide request body reader: " + readerType + ", for: " + definition + ", falling back to GenericBodyReader() instead!");
			return new GenericBodyReader();
		}
	}

	public void register(Class<?> aClass, Class<? extends HttpRequestBodyReader> clazz) {

		Assert.notNull(aClass, "Missing request body class!");
		Assert.notNull(clazz, "Missing request reader type class");

		super.register(aClass, clazz);
	}

	public void register(String mediaType, Class<? extends HttpRequestBodyReader> clazz) {

		super.register(mediaType, clazz);
	}

	public void register(MediaType mediaType, Class<? extends HttpRequestBodyReader> clazz) {

		super.register(mediaType, clazz);
	}
}
