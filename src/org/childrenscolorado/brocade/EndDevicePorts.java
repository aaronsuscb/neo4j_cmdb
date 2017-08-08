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

public class EndDevicePorts
{

	private String query = "UNWIND {objects} as object CREATE (edp:EndDevicePort) SET edp = object";
	private HashMap<String,ArrayList<EndDevicePort>> params = new HashMap<String,ArrayList<EndDevicePort>>();
	
	//////////////////////////////////////////////////////////////////////
	
	public EndDevicePorts()
	{
		
	}
	
	//////////////////////////////////////////////////////////////////////
	
	
	public void getBnaObjects(String bna_server,
							  HashMap<String,String> http_query_headers,
							  String resource_group_key,
							  String fibre_channel_fabric_key,
							  String virtual_fibre_channel_switch_key,
							  String fibre_channel_port_key)
	{
		ArrayList<EndDevicePort> objects = new ArrayList<EndDevicePort>();
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
					"/fcports/" + fibre_channel_port_key + 
					"/enddeviceports";
			
//			System.out.println("*** " + rest_query_string);
			
			// Query for the BNA fibre channel switches(s)
			request = Unirest.get(rest_query_string);
	
			for(Map.Entry<String,String> http_query_header : http_query_headers.entrySet())
			{
				request.header(http_query_header.getKey(), http_query_header.getValue());
			}
	
			json_response = request.asJson();

			if(json_response.getStatus() == 200)
			{
	
				for(Object node: json_response.getBody().getObject().getJSONArray("endDevicePorts"))
				{
					gson = new Gson();
					objects.add(gson.fromJson(node.toString(), EndDevicePort.class));
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
	
	public HashMap<String, ArrayList<EndDevicePort>> getParams()
	{
		return this.params;
	}
	
	//////////////////////////////////////////////////////////////////////
	
}
