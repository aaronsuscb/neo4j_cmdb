package org.childrenscolorado.brocade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

/**
 * bna_server
 * +- /resourcegroups
 * 
 * @author 112146
 *
 */
public class ResourceGroups
{
	private String query = "UNWIND {objects} as object CREATE (brg:BnaResourceGroup) SET brg = object";
	private HashMap<String,ArrayList<ResourceGroup>> params = new HashMap<String,ArrayList<ResourceGroup>>();

	//////////////////////////////////////////////////////////////////////
	
	
	
	//////////////////////////////////////////////////////////////////////
	
	public ResourceGroups()
	{
		
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public void getBnaObjects(String local_bna_server,
							  HashMap<String,String> http_query_headers)
	{
		ArrayList<ResourceGroup> objects = new ArrayList<ResourceGroup>();
		
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_query_string = "";
		Gson gson = null;
		
		try
		{
			rest_query_string = "http://" + local_bna_server + 
				"/rest/resourcegroups/";
		
			// Query for the BNA resource group(s)
			request = Unirest.get(rest_query_string);
	
			for(Map.Entry<String,String> http_query_header : http_query_headers.entrySet())
			{
				request.header(http_query_header.getKey(), http_query_header.getValue());
			}
	
			json_response = request.asJson();

			if(json_response.getStatus() == 200)
			{
				for(Object node: json_response.getBody().getObject().getJSONArray("resourceGroups"))
				{
					gson = new Gson();
					
					/*
					 * Do not save the resoure_group "ALL"
					 */
					if(!node.toString().equalsIgnoreCase("{\"name\":\"All\",\"type\":\"ALL\",\"key\":\"All\"}"))
					{
						ResourceGroup brg = gson.fromJson(node.toString(), ResourceGroup.class);
						brg.setBna_server(local_bna_server);
						objects.add(brg);
					}
				}
			}	
		}
		catch (UnirestException e)
		{
			e.printStackTrace();
		}
		
		this.params.put("objects", objects);
	}

	//////////////////////////////////////////////////////////////////////

	public void insertBnaJson(String target_server,
							  HashMap<String,String> http_insert_headers,
						 	  String json)
	{
		
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_insert_string = "";
		Gson gson = null; 
		
		try
		{
			rest_insert_string = "http://" + target_server + 
				"/db/data/cypher";
		
			// Query for the BNA resource group(s)
			request = Unirest.post(rest_insert_string);
	
			for(Map.Entry<String,String> http_insert_header : http_insert_headers.entrySet())
			{
				request.header(http_insert_header.getKey(), http_insert_header.getValue());
			}
	
			json_response = request.asJson();

//			if(json_response.getStatus() == 200)
//			{
//				for(Object node: json_response.getBody().getObject().getJSONArray("resourceGroups"))
//				{
//					gson = new Gson();
//					
//					/*
//					 * Do not save the resoure_group "ALL"
//					 */
//					if(!node.toString().equalsIgnoreCase("{\"name\":\"All\",\"type\":\"ALL\",\"key\":\"All\"}"))
//					{
//						objects.add(gson.fromJson(node.toString(), ResourceGroup.class));
//					}
//				}
//			}	
		}
		catch (UnirestException e)
		{
			e.printStackTrace();
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	
//	public String getQuery()
//	{
//		return query;
//	}
//
//	//////////////////////////////////////////////////////////////////////
//	
//	public void setQuery(String query)
//	{
//		this.query = query;
//	}

	//////////////////////////////////////////////////////////////////////
	
	public HashMap<String, ArrayList<ResourceGroup>> getParams()
	{
		return params;
	}

	//////////////////////////////////////////////////////////////////////
	
//	public String cypherMergeCommand()
//	{
//		String cypher = "";
//		
//		cypher = "MERGE (brg_"	+ this.key.replace("-", "_") + ":BnaResourceGroup {" +
//			"key:'" 			+ this.key 					+ "', " + 
//			"type:'" 			+ this.type 				+ "', " + 
//			"name:'" 			+ this.name 				+ "' " +
////			"bna_server:'"		+ this.bna_server			+ "'" + 
//			"})"; 
//
//		return cypher;
//	}

	//////////////////////////////////////////////////////////////////////
	
}
