package org.childrenscolorado.brocade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public class AccessGateway
{

	private String key = "";
	
	//////////////////////////////////////////////////////////////////////
	
	public AccessGateway()
	{
		
	}
	
	//////////////////////////////////////////////////////////////////////

	public static ArrayList<AccessGateway> getBnaObjects(String bna_server,
							  	     HashMap<String,String> http_query_headers,
							  	     String resource_group_key)
	{
		ArrayList<AccessGateway> bna_objects = new ArrayList<AccessGateway>();
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_query_string = "";
		
		try
		{
			rest_query_string = "http://" + bna_server + 
				"/rest/resourcegroups/" + resource_group_key + 
				"/accessgateways/";
			
//			System.out.println("rest_query_string = " + rest_query_string);

			// Query for the BNA fibre channel switches(s)
			request = Unirest.get(rest_query_string);
		
			for(Map.Entry<String,String> http_query_header : http_query_headers.entrySet())
			{
				request.header(http_query_header.getKey(), http_query_header.getValue());
			}
		
			json_response = request.asJson();
		
			if(json_response.getStatus() == 200)
			{
		
				for(Object node: json_response.getBody().getObject().getJSONArray("accessGateways"))
				{
//						System.out.println(json_response.getBody().toString());
		
					Gson gson = new Gson();
					bna_objects.add(gson.fromJson(node.toString(), AccessGateway.class));
				}
			}
		}
		catch (UnirestException e)
		{
			e.printStackTrace();
		}
		
		return bna_objects;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String cypherMergeCommand()
	{
		String cypher = "";
		
		cypher = "MERGE (ag_" + this.key.replace(":", "") + ":AccessGateway {" +
			" }) "; 

		return cypher;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getKey()
	{
		return this.key;
	}
	
	//////////////////////////////////////////////////////////////////////
	
}
