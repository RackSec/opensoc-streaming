package com.opensoc.dataservices.rest;

import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensoc.dataservices.kafkaclient.KafkaConsumer;

@Path("/")
public class Index 
{
	private static final Logger logger = LoggerFactory.getLogger( Index.class );
	
	@GET
	@Produces("text/html")
	public Response index() throws URISyntaxException 
	{
		String mt = "<html><body><h1>FOOBARBAZ</h1></body></html>";
		return Response.ok(mt).build();
	}

	@GET
	@Path("/hello")
	public Response helloGet() 
	{
		return Response.status(200).entity("HTTP GET method called").build();
	}

	@POST
	@Path("/hello")
	public Response helloPost() 
	{
		return Response.status(200).entity("HTTP POST method called").build();
	}
}