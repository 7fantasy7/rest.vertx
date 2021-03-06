package com.zandero.rest.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 */
@Path("/{root}")
public class TestPathRest {

	@Path("/echo/{param}")
	@GET
	public String echo(@PathParam("root") String rootPath, @PathParam("param") String param) {

		return rootPath + param;
	}
}
