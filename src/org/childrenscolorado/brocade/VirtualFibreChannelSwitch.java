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
 *         
 * @author 112146
 *
 */
public class VirtualFibreChannelSwitch
{
	private String  key 						= "";		// "key": "10:00:00:05:1E:D3:E2:03"
	private int     type 						= 0;		// "type": 62
	private String  name 						= "";		// "name": "FITZ_DCX1SAN-vswitch"
	private String  wwn 						= "";		// "wwn": "10:00:00:05:1E:D3:E2:03"
	private int     virtualFabricId 			= 0;		// "virtualFabricId": 40
	private int     domainId 					= 0;		// "domainId": 40
	private boolean baseSwitch 					= false;	// "baseSwitch": false
	private String  role 						= "";		// "role": "PRINCIPAL"
	private String  fcsRole 					= "";		// "fcsRole": "None"
	private boolean adCapable 					= false;	// "adCapable": true
	private String  operationalStatus 			= "";		// "operationalStatus": "HEALTHY"
	private String  state 						= "";		// "state": "ONLINE"
	private String  statusReason 				= "";		// "statusReason": "Switch Status is HEALTHY. Contributors:"
	private boolean lfEnabled 					= false;	// "lfEnabled": false
	private boolean defaultLogicalSwitch 		= false;	// "defaultLogicalSwitch": false
	private boolean fmsMode 					= false;	// "fmsMode": false
	private boolean dynamicLoadSharingCapable	= false;	// "dynamicLoadSharingCapable": true
	private boolean portBasedRoutingPresent		= false;	// "portBasedRoutingPresent": false
	private boolean inOrderDeliveryCapable		= false;	// "inOrderDeliveryCapable": false
	private boolean persistentDidEnabled		= false;	// "persistentDidEnabled": false
	private boolean autoSnmpEnabled				= false;	// "autoSnmpEnabled": false
	
	private ArrayList<PhysicalFibreChannelSwitch> physical_fibre_channel_switches = new ArrayList<PhysicalFibreChannelSwitch>();
	private ArrayList<FibreChannelPort> fibre_channel_ports = new ArrayList<FibreChannelPort>();
	
	//////////////////////////////////////////////////////////////////////
	
	public VirtualFibreChannelSwitch()
	{
		
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public static ArrayList<VirtualFibreChannelSwitch> getBnaObjects(String bna_server,
		  	 HashMap<String,String> http_query_headers,
		  	 String resource_group_key)
	{
	
		ArrayList<VirtualFibreChannelSwitch> fibre_channel_switches = new ArrayList<VirtualFibreChannelSwitch>();
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_query_string = "";
		
		try
		{
			rest_query_string = "http://" + bna_server + 
				"/rest/resourcegroups/" + resource_group_key + 
				"/fcswitches";
		
			//System.out.println(rest_query_string);
		
			// Query for the BNA fibre channel switches(s)
			request = Unirest.get(rest_query_string);
		
			for(Map.Entry<String,String> http_query_header : http_query_headers.entrySet())
			{
				request.header(http_query_header.getKey(), http_query_header.getValue());
			}
		
			json_response = request.asJson();
		
			if(json_response.getStatus() == 200)
			{
			
				for(Object node: json_response.getBody().getObject().getJSONArray("fcSwitches"))
				{
					//System.out.println(json_response.getBody().toString());
			
					Gson gson = new Gson();
					fibre_channel_switches.add(gson.fromJson(node.toString(), VirtualFibreChannelSwitch.class));
				}
		
				//System.out.println("fibre_channel_switches SIZE = " + fibre_channel_switches.size());
			}	
		}
		catch (UnirestException e)
		{
			e.printStackTrace();
		}
		
		return fibre_channel_switches;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public static ArrayList<VirtualFibreChannelSwitch> getBnaObjects(String bna_server,
																  	 HashMap<String,String> http_query_headers,
																  	 String resource_group_key,
																  	 String fibre_channel_fabric_key)
	{
		ArrayList<VirtualFibreChannelSwitch> fibre_channel_switches = new ArrayList<VirtualFibreChannelSwitch>();
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_query_string = "";
		
		try
		{
			rest_query_string = "http://" + bna_server + 
				"/rest/resourcegroups/" + resource_group_key + 
				"/fcfabrics/" + fibre_channel_fabric_key + 
				"/fcswitches";
			
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
	
				for(Object node: json_response.getBody().getObject().getJSONArray("fcSwitches"))
				{
//					System.out.println(json_response.getBody().toString());

					Gson gson = new Gson();
					fibre_channel_switches.add(gson.fromJson(node.toString(), VirtualFibreChannelSwitch.class));
				}

//				System.out.println("fibre_channel_switches SIZE = " + fibre_channel_switches.size());
			}	
		}
		catch (UnirestException e)
		{
			e.printStackTrace();
		}
	
		return fibre_channel_switches;
	}
	
	//////////////////////////////////////////////////////////////////////

	public String cypherMergeCommand()
	{
		String cypher = "";
		
		cypher = "MERGE (vfcs_"				+ this.key.replace(":", "") 		+ ":VirtualFibreChannelSwitch {" +
			"key:'" 						+ this.key 							+ "', " + 
			"type:'" 						+ this.type 						+ "', " + 
			"name:'" 						+ this.name 						+ "', " +  
			"wwn:'" 						+ this.wwn 							+ "', " + 
			"virtualFabricId:" 				+ this.virtualFabricId 				+ ", " + 
			"domainId:" 					+ this.domainId 					+ ", " + 
			"baseSwitch:" 					+ this.baseSwitch 					+ ", " + 
			"role:'" 						+ this.role 						+ "', " +  
			"fcsRole:'" 					+ this.fcsRole 						+ "', " +  
			"adCapable:" 					+ this.adCapable 					+ ", " + 
			"operationalStatus:'" 			+ this.operationalStatus 			+ "', " +   
			"state:'" 						+ this.state 						+ "', " + 
			"statusReason:'" 				+ this.statusReason 				+ "', " +  
			"lfEnabled:" 					+ this.lfEnabled 					+ ", " + 
			"defaultLogicalSwitch:" 		+ this.defaultLogicalSwitch 		+ ", " +  
			"fmsMode:" 						+ this.fmsMode 						+ ", " + 
			"dynamicLoadSharingCapable:"	+ this.dynamicLoadSharingCapable	+ ", " +  
			"portBasedRoutingPresent:"		+ this.portBasedRoutingPresent 		+ ", " +
			"inOrderDeliveryCapable:"		+ this.inOrderDeliveryCapable 		+ ", " +
			"persistentDidEnabled:"			+ this.persistentDidEnabled 		+ ", " +
			"autoSnmpEnabled:"				+ this.autoSnmpEnabled 				+ " " + 
			"})"; 

		return cypher;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public ArrayList<PhysicalFibreChannelSwitch> getPhysicalFibreChannelSwitches()
	{
		return this.physical_fibre_channel_switches;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public void setPhysicalFibreChannelSwitches(ArrayList<PhysicalFibreChannelSwitch> physical_fibre_channel_switches)
	{
		this.physical_fibre_channel_switches = physical_fibre_channel_switches;
	}

	//////////////////////////////////////////////////////////////////////
		
	public void setFibreChannelPorts(ArrayList<FibreChannelPort> fibre_channel_ports)
	{
		this.fibre_channel_ports = fibre_channel_ports;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public ArrayList<FibreChannelPort> getFibreChannelPorts()
	{
		return this.fibre_channel_ports;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getKey()
	{
		return key;
	}
	
	//////////////////////////////////////////////////////////////////////

	public int getType()
	{
		return type;
	}
	
	//////////////////////////////////////////////////////////////////////

	public String getName()
	{
		return name;
	}
	
	//////////////////////////////////////////////////////////////////////

	public String getWwn()
	{
		return wwn;
	}
	
	//////////////////////////////////////////////////////////////////////

	public int getVirtualFabricId()
	{
		return virtualFabricId;
	}
	
	//////////////////////////////////////////////////////////////////////

	public int getDomainId()
	{
		return domainId;
	}
	
	//////////////////////////////////////////////////////////////////////

	public boolean isBaseSwitch()
	{
		return baseSwitch;
	}
	
	//////////////////////////////////////////////////////////////////////

	public String getRole()
	{
		return role;
	}
	
	//////////////////////////////////////////////////////////////////////

	public String getFcsRole()
	{
		return fcsRole;
	}
	
	//////////////////////////////////////////////////////////////////////

	public boolean isAdCapable()
	{
		return adCapable;
	}
	
	//////////////////////////////////////////////////////////////////////

	public String getOperationalStatus()
	{
		return operationalStatus;
	}
	
	//////////////////////////////////////////////////////////////////////

	public String getState()
	{
		return state;
	}
	
	//////////////////////////////////////////////////////////////////////

	public String getStatusReason()
	{
		return statusReason;
	}
	
	//////////////////////////////////////////////////////////////////////

	public boolean isLfEnabled()
	{
		return lfEnabled;
	}
	
	//////////////////////////////////////////////////////////////////////

	public boolean isDefaultLogicalSwitch()
	{
		return defaultLogicalSwitch;
	}
	
	//////////////////////////////////////////////////////////////////////

	public boolean isFmsMode()
	{
		return fmsMode;
	}
	
	//////////////////////////////////////////////////////////////////////

	public boolean isDynamicLoadSharingCapable()
	{
		return dynamicLoadSharingCapable;
	}
	
	//////////////////////////////////////////////////////////////////////

	public boolean isPortBasedRoutingPresent()
	{
		return portBasedRoutingPresent;
	}
	
	//////////////////////////////////////////////////////////////////////

	public boolean isInOrderDeliveryCapable()
	{
		return inOrderDeliveryCapable;
	}
	
	//////////////////////////////////////////////////////////////////////

	public boolean isPersistentDidEnabled()
	{
		return persistentDidEnabled;
	}
	
	//////////////////////////////////////////////////////////////////////

	public boolean isAutoSnmpEnabled()
	{
		return autoSnmpEnabled;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	
}
