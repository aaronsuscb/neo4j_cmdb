package org.childrenscolorado.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.childrenscolorado.brocade.FibreChannelPort;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public class Neo4j
{
	
	HashMap<String,HashMap<String,String>> neo4j_servers = null;
	HashMap<String,String> http_login_headers = null;
	HashMap<String,String> http_query_headers = null;
	
	//////////////////////////////////////////////////////////////////////
	
	public Neo4j()
	{
		
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public void neo4jLogin(HashMap<String,String> http_neo4j_server_properties)
	{
//		local_neo4j_server_properties.put("protocol", prop.getProperty("neo4j.protocol"));
//    	local_neo4j_server_properties.put("server",   prop.getProperty("neo4j.server"));
//    	local_neo4j_server_properties.put("port",     prop.getProperty("neo4j.port"));
//    	local_neo4j_server_properties.put("userid",   prop.getProperty("neo4j.userid"));
//    	local_neo4j_server_properties.put("password", prop.getProperty("neo4j.password"));
		
		String neo4j_base64_authorization = "";
		
		/*
		 * The Neo4j community edition accepts username:password in a base64 encoding
		 */
		try
		{
			String username_password = http_neo4j_server_properties.get("userid") + ":" + http_neo4j_server_properties.get("password"); 
			neo4j_base64_authorization = Base64.getEncoder().encodeToString(username_password.getBytes("utf-8"));
		}
		catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		
		neo4j_servers = new HashMap<String,HashMap<String,String>>();
		
		http_login_headers = new HashMap<String,String>();
		http_query_headers = new HashMap<String,String>();
		
		http_login_headers.put("Accept", "application/json; charset=UTF-8");
		http_login_headers.put("Content-Type", "application/json");
		http_login_headers.put("Authorization", "Basic " + neo4j_base64_authorization);
		
		neo4j_servers.put(http_neo4j_server_properties.get("server")+":"+http_neo4j_server_properties.get("port"), http_login_headers);

//		for(Map.Entry<String,HashMap<String,String>> neo4j_server : neo4j_servers.entrySet())
//		{
////			System.out.println("Key: \"" + neo4j_server.getKey() + "\" --- Value: \"" + neo4j_server.getValue() + "\"");
//			
//			for(Map.Entry<String,String> http_login_header : neo4j_server.getValue().entrySet())
//			{
//				System.out.println("HTTP Header Key: \"" + http_login_header.getKey() + "\" --- HTTP Header Value: \"" + http_login_header.getValue() + "\"");
//			}
//		}
//		
//		return new HashMap<String,String>();
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public void httpPostRequest(String json)
	{
//		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_post_url = "";
//		Gson gson = null;
		
		try
		{
			/*
			 * FIXME: pass neo4j server in
			 */
			rest_post_url = "http://d112146:7474/db/data/cypher"; 
			
			// Send a json array to Neo4J
			json_response = Unirest.post(rest_post_url)
					.header("accept", http_login_headers.get("Accept"))
					.header("Authorization", http_login_headers.get("Authorization"))
					.header("Content-Type", http_login_headers.get("Content-Type"))
					.body(json)
					.asJson();

			if(json_response.getStatus() == 200)
			{
				System.out.println("POST == " + json_response.getStatus());
			}
			else
			{
				System.out.println("POST FAILED == " + json_response.getStatus());
			}
		}
		catch (UnirestException e)
		{
			e.printStackTrace();
		}
	}
	
	//////////////////////////////////////////////////////////////////////
}
