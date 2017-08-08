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
 *     +- /fcswitches
 *       +- /fcports
 *         +- /enddeviceports
 * 
 * @author 112146
 *
 */
public class EndDevicePort
{
	
	private String 	key 			= "";		// "key": "50:05:07:68:01:70:A1:BB"
	private int 	domainId 		= 0;		// "domainId": 40
	private String 	wwn 			= "";		// "wwn": "50:05:07:68:01:70:A1:BB"
	private String 	deviceNodeWwn 	= "";		// "deviceNodeWwn": "50:05:07:68:01:00:A1:BB"
	private String 	switchPortWwn 	= "";		// "switchPortWwn": "20:1D:00:05:1E:D3:E2:03"
	private int 	number 			= 0;		// "number": 29
	private String 	portId 			= "";		// "portId": "28f880"
	private String 	type 			= "";		// "type": "N"
	private String 	symbolicName 	= "";		// "symbolicName": ""
	private String 	fc4Type 		= "";		// "fc4Type": "FCP"
	private int 	cos 			= 0;		// "cos": "3"
	private String 	hardwareAddress = ""; 		// "hardwareAddress": "000000"
	private boolean npvPhysical 	= false;	// "npvPhysical": false
	
	//////////////////////////////////////////////////////////////////////
	
	public EndDevicePort()
	{
		
	}

	//////////////////////////////////////////////////////////////////////
	
	public static ArrayList<EndDevicePort> getBnaObjects(String bna_server,
		  	 HashMap<String,String> http_query_headers,
		  	 String resource_group_key,
		  	 String fc_fabric_key,
		  	 String fc_switch_key,
		  	 String fc_port_key)
	{
		ArrayList<EndDevicePort> bna_objects = new ArrayList<EndDevicePort>();
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_query_string = "";
		
		try
		{
			rest_query_string = "http://" + bna_server + 
				"/rest/resourcegroups/" + resource_group_key +
				"/fcfabrics/" + fc_fabric_key +
				"/fcswitches/" + fc_switch_key + 
				"/fcports/" + fc_port_key +
				"/enddeviceports";
			
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
					//System.out.println(json_response.getBody().toString());
					
					Gson gson = new Gson();
					bna_objects.add(gson.fromJson(node.toString(), EndDevicePort.class));
				}
				//System.out.println("fibre_channel_switches SIZE = " + fibre_channel_switches.size());
			
			}
		}
		catch (UnirestException e)
		{
//			e.printStackTrace();
			System.out.println("***** ERROR *****");
		}
		
		return bna_objects;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public static ArrayList<EndDevicePort> getBnaObjects(String bna_server,
													  	 HashMap<String,String> http_query_headers,
													  	 String resource_group_key,
													  	 String fc_port_key)
	{
		ArrayList<EndDevicePort> bna_objects = new ArrayList<EndDevicePort>();
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_query_string = "";
		
		try
		{
			rest_query_string = "http://" + bna_server + 
				"/rest/resourcegroups/" + resource_group_key + 
				"/fcports/" + fc_port_key +
				"/enddeviceports";
			
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
		
				for(Object node: json_response.getBody().getObject().getJSONArray("endDevicePorts"))
				{
//					System.out.println(json_response.getBody().toString());
		
					Gson gson = new Gson();
					bna_objects.add(gson.fromJson(node.toString(), EndDevicePort.class));
				}
				//System.out.println("fibre_channel_switches SIZE = " + fibre_channel_switches.size());
		
			}
		}
		catch (UnirestException e)
		{
//			e.printStackTrace();
			System.out.println("***** ERROR *****");
		}

		return bna_objects;
	}
	
	//////////////////////////////////////////////////////////////////////
		
	public String cypherMergeCommand()
	{
		String cypher = "";
		
		cypher = "MERGE (edp_"	+ this.key.replace(":", "") + ":EndDevicePort {" +
			"key:'"				+ this.key					+ "', " + 
			"domainId:"			+ this.domainId				+ ", " + 
			"wwn:'"				+ this.wwn					+ "', " + 
			"deviceNodeWwn:'"	+ this.deviceNodeWwn		+ "', " +
			"switchPortWwn:'"	+ this.switchPortWwn		+ "', " +
			"number:"			+ this.number				+ ", " +
			"portId:'"			+ this.portId				+ "', " +
			"type:'"			+ this.type					+ "', " +
			"symbolicName:'"	+ this.symbolicName			+ "', " +
			"fc4Type:'"			+ this.fc4Type				+ "', " +
			"cos:'"				+ this.cos					+ "', " +
			"hardwareAddress:'"	+ this.hardwareAddress		+ "', " +
			"npvPhysical:'"		+ this.npvPhysical			+ "' " +
			"})"; 
		
		return cypher;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public String getKey()
	{
		return this.key;
	}

	//////////////////////////////////////////////////////////////////////

	public int getDomainId()
	{
		return this.domainId;
	}

	//////////////////////////////////////////////////////////////////////

	public String getWwn()
	{
		return this.wwn;
	}

	//////////////////////////////////////////////////////////////////////

	public String getDeviceNodeWwn()
	{
		return this.deviceNodeWwn;
	}

	//////////////////////////////////////////////////////////////////////

	public String getSwitchPortWwn()
	{
		return this.switchPortWwn;
	}

	//////////////////////////////////////////////////////////////////////

	public int getNumber()
	{
		return this.number;
	}

	//////////////////////////////////////////////////////////////////////

	public String getPortId()
	{
		return this.portId;
	}

	//////////////////////////////////////////////////////////////////////

	public String getType()
	{
		return this.type;
	}

	//////////////////////////////////////////////////////////////////////

	public String getSymbolicName()
	{
		return this.symbolicName;
	}

	//////////////////////////////////////////////////////////////////////

	public String getFc4Type()
	{
		return this.fc4Type;
	}

	//////////////////////////////////////////////////////////////////////

	public int getCos()
	{
		return this.cos;
	}

	//////////////////////////////////////////////////////////////////////

	public String getHardwareAddress()
	{
		return this.hardwareAddress;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isNpvPhysical()
	{
		return this.npvPhysical;
	}
	
	//////////////////////////////////////////////////////////////////////

}
