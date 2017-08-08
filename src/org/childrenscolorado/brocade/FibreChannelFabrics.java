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

/**
 * bna_server
 * +- /resourcegroups
 *   +- /fcfabrics
 * 
 * @author 112146
 *
 */
public class FibreChannelFabrics
{

	private String query = "UNWIND {objects} as object CREATE (fcf:FibreChannelFabric) SET fcf = object";
	private HashMap<String,ArrayList<FibreChannelFabric>> params = new HashMap<String,ArrayList<FibreChannelFabric>>();
	
	//////////////////////////////////////////////////////////////////////
	
	public FibreChannelFabrics()
	{
		
	}

	//////////////////////////////////////////////////////////////////////
	
	public void getBnaObjects(String bna_server,
						  	  HashMap<String,String> http_query_headers,
						  	  String resource_group_key)
	{
		ArrayList<FibreChannelFabric> objects = new ArrayList<FibreChannelFabric>();
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_query_string = "";
		Gson gson = null;
		
		try
		{
			rest_query_string = "http://" + bna_server + 
				"/rest/resourcegroups/" + resource_group_key + 
				"/fcfabrics";
			
//			System.out.println(rest_query_string);
			
			// Query for the BNA fibre channel switches(s)
			request = Unirest.get(rest_query_string);
	
			for(Map.Entry<String,String> http_query_header : http_query_headers.entrySet())
			{
				request.header(http_query_header.getKey(), http_query_header.getValue());
			}
	
			json_response = request.asJson();

			if(json_response.getStatus() == 200)
			{
	
				for(Object node: json_response.getBody().getObject().getJSONArray("fcFabrics"))
				{
					gson = new Gson();
					objects.add(gson.fromJson(node.toString(), FibreChannelFabric.class));
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
	
	public HashMap<String, ArrayList<FibreChannelFabric>> getParams()
	{
		return this.params;
	}
	
	//////////////////////////////////////////////////////////////////////
	
//	public String cypherMergeCommand()
//	{
//		String cypher = "";
//		
//		cypher = "MERGE (fcf_"		+ this.key.replace(":", "") + ":FibreChannelFabric {" +
//			"key:'" 				+ this.key 					+ "', " +
//			"seedSwitchWwn:'"		+ this.seedSwitchWwn		+ "', " + 
//			"name:'"				+ this.name 				+ "', " + 
//			"secure:"				+ this.secure 				+ ", " +
//			"adEnvironment:"		+ this.adEnvironment 		+ ", " +
//			"contact:'"				+ this.contact 				+ "', " +
//			"location:'"			+ this.location 			+ "', " +
//			"description:'"			+ this.description 			+ "', " +
//			"principalSwitchWwn:'"	+ this.principalSwitchWwn 	+ "', " +
//			"fabricName:'"			+ this.fabricName 			+ "', " +
//			"virtualFabricId:"		+ this.virtualFabricId 		+ ", " +
//			"seedSwitchIpAddress:'"	+ this.seedSwitchIpAddress 	+ "' " +
//			"})";
//
//		return cypher;
//	}

	//////////////////////////////////////////////////////////////////////

}
