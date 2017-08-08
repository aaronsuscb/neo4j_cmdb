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
 * 
 * @author 112146
 *
 */
public class FibreChannelPorts
{
	private String query = "UNWIND {objects} as object CREATE (fcp:FibreChannelPort) SET fcp = object";
	private HashMap<String,ArrayList<FibreChannelPort>> params = new HashMap<String,ArrayList<FibreChannelPort>>();

	//////////////////////////////////////////////////////////////////////
	
	
	
	//////////////////////////////////////////////////////////////////////
	
	public FibreChannelPorts()
	{
		
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public void getBnaObjects(String bna_server,
							  HashMap<String,String> http_query_headers,
							  String resource_group_key,
							  String fibre_channel_fabric_key,
							  String virtual_fibre_channel_switch_key)
	{
		ArrayList<FibreChannelPort> objects = new ArrayList<FibreChannelPort>();
		
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_query_string = "";
		Gson gson = null;
		
		try
		{
			rest_query_string = "http://" + bna_server + 
				"/rest/resourcegroups/" + resource_group_key + 
				"/fcfabrics/" + fibre_channel_fabric_key + 
				"/fcswitches/" + virtual_fibre_channel_switch_key + 
				"/fcports";
		
			// Query for the BNA resource group(s)
			request = Unirest.get(rest_query_string);
	
			for(Map.Entry<String,String> http_query_header : http_query_headers.entrySet())
			{
				request.header(http_query_header.getKey(), http_query_header.getValue());
			}
	
			json_response = request.asJson();

			if(json_response.getStatus() == 200)
			{
				for(Object node: json_response.getBody().getObject().getJSONArray("fcPorts"))
				{
					gson = new Gson();
					FibreChannelPort fcp = gson.fromJson(node.toString(), FibreChannelPort.class);
					fcp.setVirtualFibreChannelSwitchKey(virtual_fibre_channel_switch_key);
					objects.add(fcp);
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

//	public void insertBnaJson(String target_server,
//							  HashMap<String,String> http_insert_headers,
//						 	  String json)
//	{
//		
//		HttpRequest request = null;
//		HttpResponse<JsonNode> json_response = null;
//		String rest_insert_string = "";
//		
//		try
//		{
//			rest_insert_string = "http://" + target_server + 
//				"/db/data/cypher";
//		
//			// Query for the BNA resource group(s)
//			request = Unirest.post(rest_insert_string);
//	
//			for(Map.Entry<String,String> http_insert_header : http_insert_headers.entrySet())
//			{
//				request.header(http_insert_header.getKey(), http_insert_header.getValue());
//			}
//	
//			json_response = request.asJson();
//
////			if(json_response.getStatus() == 200)
////			{
////				for(Object node: json_response.getBody().getObject().getJSONArray("resourceGroups"))
////				{
////					Gson gson = new Gson();
////					
////					/*
////					 * Do not save the resoure_group "ALL"
////					 */
////					if(!node.toString().equalsIgnoreCase("{\"name\":\"All\",\"type\":\"ALL\",\"key\":\"All\"}"))
////					{
////						objects.add(gson.fromJson(node.toString(), ResourceGroup.class));
////					}
////				}
////			}	
//		}
//		catch (UnirestException e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	//////////////////////////////////////////////////////////////////////
	
	public HashMap<String, ArrayList<FibreChannelPort>> getParams()
	{
		return this.params;
	}

	//////////////////////////////////////////////////////////////////////

}
