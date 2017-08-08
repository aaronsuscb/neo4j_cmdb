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
 * 
 * @author 112146
 *
 */
public class FibreChannelPort
{
	private String 	key 					= "";		// "key": "20:1D:00:05:1E:D3:E2:03"
	private String 	wwn 					= "";		// "wwn": "20:1D:00:05:1E:D3:E2:03"
	private String 	name 					= "";		// "name": "SVCFITZ01_NODE12_DH8_07-fport"
	private String 	slotNumber 				= "";		// "slotNumber": 2
	private int 	portNumber 				= 0;		// "portNumber": 13
	private int 	userPortNumber 			= 0;		// "userPortNumber": 29
	private String 	portId 					= "";		// "portId": "28f880"
	private int 	portIndex 				= 0;		// "portIndex": 29
	private int 	areaId 					= 0;		// "areaId": 248
	private String 	type 					= "";		// "type": "F_PORT"
	private String 	status 					= "";		// "status": "ONLINE"
	private String 	statusMessage 			= "";		// "statusMessage": ""
	private String 	lockedPortType 			= "";		// "lockedPortType": "U_PORT"
	private String 	speed 					= "";		// "speed": "8"
	private String 	speedsSupported 		= "";		// "speedsSupported": "1,2,4,8"
	private int 	maxPortSpeed 			= 0;		// "maxPortSpeed": 8
	private int 	desiredCredits 			= 0;		// "desiredCredits": 0
	private int 	bufferAllocated 		= 0;		// "bufferAllocated": 8
	private int 	estimatedDistance 		= 0;		// "estimatedDistance": 0
	private int 	actualDistance 			= 0;		// "actualDistance": 0
	private int 	longDistanceSetting 	= 0;		// "longDistanceSetting": 0
	private String 	remoteNodeWwn 			= "";		// "remoteNodeWwn": "50:05:07:68:01:00:A1:BB"
	private String 	remotePortWwn 			= "";		// "remotePortWwn": "50:05:07:68:01:70:A1:BB"
	private boolean licensed 				= false;	// "licensed": true
	private boolean swapped 				= false;	// "swapped": false
	private boolean trunked 				= false;	// "trunked": false
	private boolean trunkMaster 			= false;	// "trunkMaster": false
	private boolean persistentlyDisabled 	= false;	// "persistentlyDisabled": false
	private boolean ficonSupported 			= false;	// "ficonSupported": true
	private boolean blocked 				= false;	// "blocked": false
	private String 	prohibitPortNumbers 	= "";		// "prohibitPortNumbers": null
	private int 	prohibitPortCount 		= 0;		// "prohibitPortCount": 0
	private boolean npivCapable 			= false;	// "npivCapable": true
	private boolean npivEnabled 			= false;	// "npivEnabled": true
	private boolean fcFastWriteEnabled 		= false;	// "fcFastWriteEnabled": false
	private boolean islRrdyEnabled 			= false;	// "islRrdyEnabled": false
	private boolean rateLimitCapable 		= false;	// "rateLimitCapable": false
	private boolean rateLimited 			= false;	// "rateLimited": false
	private boolean qosCapable 				= false;	// "qosCapable": false
	private boolean qosEnabled 				= false;	// "qosEnabled": false
	private int 	fcrFabricId 			= 0;		// "fcrFabricId": 0
	private String 	state					= "";		// "state": "ONLINE"
	private boolean occupied 				= false;	// "occupied": true
	private int 	masterPortNumber 		= 0;		// "masterPortNumber": -1
	private String	virtual_fibre_channel_switch_key = "";	// This field is not provided by the BNA fcPort rest endpoint

	private ArrayList<EndDevicePort> end_device_ports = null;
	private ArrayList<ConnectedSwitchPort> connected_switch_ports = null;
	
	//////////////////////////////////////////////////////////////////////
		
	public FibreChannelPort()
	{
	
	}
	
	//////////////////////////////////////////////////////////////////////

//	public static ArrayList<FibreChannelPort> getBnaObjects(String bna_server,
//													  	 	HashMap<String,String> http_query_headers,
//													  	 	String resource_group_key,
//													  	 	String fc_switch_key)
//	{
//		
//		ArrayList<FibreChannelPort> bna_objects = new ArrayList<FibreChannelPort>();
//		HttpRequest request = null;
//		HttpResponse<JsonNode> json_response = null;
//		String rest_query_string = "";
//		
//		try
//		{
//			rest_query_string = "http://" + bna_server + 
//				"/rest/resourcegroups/" + resource_group_key + 
//				"/fcswitches/" + fc_switch_key + 
//				"/fcports";
//			
//			// Query for the BNA fibre channel switches(s)
//			request = Unirest.get(rest_query_string);
//		
//			for(Map.Entry<String,String> http_query_header : http_query_headers.entrySet())
//			{
//				request.header(http_query_header.getKey(), http_query_header.getValue());
//			}
//		
//			json_response = request.asJson();
//		
//			if(json_response.getStatus() == 200)
//			{
//		
//				for(Object node: json_response.getBody().getObject().getJSONArray("fcPorts"))
//				{
////					System.out.println(json_response.getBody().toString());
//		
//					Gson gson = new Gson();
//					bna_objects.add(gson.fromJson(node.toString(), FibreChannelPort.class));
//				}
//				
////				System.out.println("fibre_channel_switches SIZE = " + fibre_channel_switches.size());		
//			}
//		}
//		catch (UnirestException e)
//		{
//			e.printStackTrace();
//		}
//		
//		return bna_objects;
//	}
//	
//	//////////////////////////////////////////////////////////////////////
//	
//	public static ArrayList<FibreChannelPort> getBnaObjects(String bna_server,
//													  	 	HashMap<String,String> http_query_headers,
//													  	 	String resource_group_key,
//													  	 	String fc_fabric_key,
//													  	 	String fc_switch_key)
//	{
//		ArrayList<FibreChannelPort> bna_objects = new ArrayList<FibreChannelPort>();
//		HttpRequest request = null;
//		HttpResponse<JsonNode> json_response = null;
//		String rest_query_string = "";
//	
//		try
//		{
//		rest_query_string = "http://" + bna_server + 
//			"/rest/resourcegroups/" + resource_group_key +
//			"/fcfabrics/" + fc_fabric_key + 
//			"/fcswitches/" + fc_switch_key + 
//			"/fcports";
//	
//			// Query for the BNA fibre channel switches(s)
//			request = Unirest.get(rest_query_string);
//			
//			for(Map.Entry<String,String> http_query_header : http_query_headers.entrySet())
//			{
//				request.header(http_query_header.getKey(), http_query_header.getValue());
//			}
//			
//			json_response = request.asJson();
//			
//			if(json_response.getStatus() == 200)
//			{
//			
//				for(Object node: json_response.getBody().getObject().getJSONArray("fcPorts"))
//				{
//					//System.out.println(json_response.getBody().toString());
//				
//					Gson gson = new Gson();
//					bna_objects.add(gson.fromJson(node.toString(), FibreChannelPort.class));
//				}
//				
//				//System.out.println("fibre_channel_switches SIZE = " + fibre_channel_switches.size());		
//			}
//		}
//		catch (UnirestException e)
//		{
//			e.printStackTrace();
//		}
//		
//		return bna_objects;
//	}
//	
//	//////////////////////////////////////////////////////////////////////
//	
//	public static ArrayList<FibreChannelPort> getBnaObjects(String bna_server,
//															HashMap<String,String> http_query_headers,
//															String resource_group_key)
//	{
//		ArrayList<FibreChannelPort> bna_objects = new ArrayList<FibreChannelPort>();
//		HttpRequest request = null;
//		HttpResponse<JsonNode> json_response = null;
//		String rest_query_string = "";
//	
//		try
//		{
//		rest_query_string = "http://" + bna_server + 
//			"/rest/resourcegroups/" + resource_group_key +
//			"/fcports";
//	
//			// Query for the BNA fibre channel switches(s)
//			request = Unirest.get(rest_query_string);
//			
//			for(Map.Entry<String,String> http_query_header : http_query_headers.entrySet())
//			{
//				request.header(http_query_header.getKey(), http_query_header.getValue());
//			}
//			
//			json_response = request.asJson();
//			
//			if(json_response.getStatus() == 200)
//			{
//			
//				for(Object node: json_response.getBody().getObject().getJSONArray("fcPorts"))
//				{
//					//System.out.println(json_response.getBody().toString());
//				
//					Gson gson = new Gson();
//					bna_objects.add(gson.fromJson(node.toString(), FibreChannelPort.class));
//				}
//				
//				//System.out.println("fibre_channel_switches SIZE = " + fibre_channel_switches.size());		
//			}
//		}
//		catch (UnirestException e)
//		{
//			e.printStackTrace();
//		}
//		
//		return bna_objects;
//	}
	
	//////////////////////////////////////////////////////////////////////

	public ArrayList<EndDevicePort> getEndDevicePorts()
	{
		return this.end_device_ports;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public void setEndDevicePorts(ArrayList<EndDevicePort> end_device_ports)
	{
		this.end_device_ports = end_device_ports;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public ArrayList<ConnectedSwitchPort> getConnectedSwitchPorts()
	{
		return this.connected_switch_ports;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public void setConnectedSwitchPorts(ArrayList<ConnectedSwitchPort> connected_switch_ports)
	{
		this.connected_switch_ports = connected_switch_ports;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public String cypherMergeCommand()
	{
		String cypher = "";
		
		try
		{
			cypher = "MERGE (fcp_"	+ this.key.replace(":", "").replace("-", "") + ":FibreChannelPort {" + 
				"key:'" 				+ this.key	+ "', " + 
				"wwn:'" 				+ this.wwn 					+ "', " +
				"name:'" 				+ this.getName()			+ "', " +
				"slotNumber:" 			+ this.slotNumber 			+ ", " +
				"portNumber:" 			+ this.portNumber 			+ ", " +
				"userPortNumber:" 		+ this.userPortNumber 		+ ", " +
				"portId:'" 				+ this.portId 				+ "', " +
				"portIndex:" 			+ this.portIndex 			+ ", " +
				"areaId:" 				+ this.areaId 				+ ", " +
				"type:'" 				+ this.type 				+ "', " +
				"status:'" 				+ this.status 				+ "', " +
				"statusMessage:'" 		+ this.statusMessage 		+ "', " +
				"lockedPortType:'" 		+ this.lockedPortType 		+ "', " +
				"speed:'" 				+ this.speed 				+ "', " +
				"speedsSupported:'" 	+ this.speedsSupported 		+ "', " +
				"maxPortSpeed:" 		+ this.maxPortSpeed 		+ ", " +
				"desiredCredits:" 		+ this.desiredCredits 		+ ", " +
				"bufferAllocated:" 		+ this.bufferAllocated 		+ ", " +
				"estimatedDistance:" 	+ this.estimatedDistance 	+ ", " +
				"actualDistance:" 		+ this.actualDistance 		+ ", " +
				"longDistanceSetting:" 	+ this.longDistanceSetting 	+ ", " +
				"remoteNodeWwn:'" 		+ this.remoteNodeWwn 		+ "', " +
				"remotePortWwn:'" 		+ this.remotePortWwn 		+ "', " +
				"licensed:" 			+ this.licensed 			+ ", " +
				"swapped:" 				+ this.swapped 				+ ", " +
				"trunked:" 				+ this.trunked 				+ ", " +
				"trunkMaster:" 			+ this.trunkMaster 			+ ", " +
				"persistentlyDisabled:" + this.persistentlyDisabled + ", " +
				"ficonSupported:" 		+ this.ficonSupported 		+ ", " +
				"blocked:" 				+ this.blocked 				+ ", " +
				"prohibitPortNumbers:'" + this.getProhibitPortNumbers()	+ "', " +
				"prohibitPortCount:" 	+ this.prohibitPortCount 	+ ", " +
				"npivCapable:" 			+ this.npivCapable 			+ ", " +
				"npivEnabled:" 			+ this.npivEnabled 			+ ", " +
				"fcFastWriteEnabled:" 	+ this.fcFastWriteEnabled 	+ ", " +
				"islRrdyEnabled:" 		+ this.islRrdyEnabled 		+ ", " +
				"rateLimitCapable:" 	+ this.rateLimitCapable 	+ ", " +
				"rateLimited:" 			+ this.rateLimited 			+ ", " +
				"qosCapable:" 			+ this.qosCapable 			+ ", " +
				"qosEnabled:" 			+ this.qosEnabled 			+ ", " +
				"fcrFabricId:" 			+ this.fcrFabricId 			+ ", " +
				"state:'" 				+ this.state 				+ "', " +
				"occupied:" 			+ this.occupied 			+ ", " +
				"masterPortNumber:" 	+ this.masterPortNumber 	+ " " +
				"})";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			cypher = "";
		}

		return cypher;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getKey()
	{
		return this.key;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getWwn()
	{
		return this.wwn;
	}

	//////////////////////////////////////////////////////////////////////

	public String getName()
	{
		if(this.name.length() == 0)
		{
			this.name = this.getWwn();
		}
		return this.name;
	}

	//////////////////////////////////////////////////////////////////////

	public String getSlotNumber()
	{
		return this.slotNumber;
	}

	//////////////////////////////////////////////////////////////////////

	public int getPortNumber()
	{
		return this.portNumber;
	}

	//////////////////////////////////////////////////////////////////////

	public int getUserPortNumber()
	{
		return this.userPortNumber;
	}

	//////////////////////////////////////////////////////////////////////

	public String getPortId()
	{
		return this.portId;
	}

	//////////////////////////////////////////////////////////////////////

	public int getPortIndex()
	{
		return this.portIndex;
	}

	//////////////////////////////////////////////////////////////////////

	public int getAreaId()
	{
		return this.areaId;
	}

	//////////////////////////////////////////////////////////////////////

	public String getType()
	{
		return this.type;
	}

	//////////////////////////////////////////////////////////////////////

	public String getStatus()
	{
		return this.status;
	}

	//////////////////////////////////////////////////////////////////////

	public String getStatusMessage()
	{
		return this.statusMessage;
	}

	//////////////////////////////////////////////////////////////////////

	public String getLockedPortType()
	{
		return this.lockedPortType;
	}

	//////////////////////////////////////////////////////////////////////

	public String getSpeed()
	{
		return this.speed;
	}

	//////////////////////////////////////////////////////////////////////

	public String getSpeedsSupported()
	{
		return this.speedsSupported;
	}

	//////////////////////////////////////////////////////////////////////

	public int getMaxPortSpeed()
	{
		return this.maxPortSpeed;
	}

	//////////////////////////////////////////////////////////////////////

	public int getDesiredCredits()
	{
		return this.desiredCredits;
	}

	//////////////////////////////////////////////////////////////////////

	public int getBufferAllocated()
	{
		return this.bufferAllocated;
	}

	//////////////////////////////////////////////////////////////////////

	public int getEstimatedDistance()
	{
		return this.estimatedDistance;
	}

	//////////////////////////////////////////////////////////////////////

	public int getActualDistance()
	{
		return this.actualDistance;
	}

	//////////////////////////////////////////////////////////////////////

	public int getLongDistanceSetting()
	{
		return this.longDistanceSetting;
	}

	//////////////////////////////////////////////////////////////////////

	public String getRemoteNodeWwn()
	{
		return this.remoteNodeWwn;
	}

	//////////////////////////////////////////////////////////////////////

	public String getRemotePortWwn()
	{
		return this.remotePortWwn;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isLicensed()
	{
		return this.licensed;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isSwapped()
	{
		return this.swapped;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isTrunked()
	{
		return this.trunked;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isTrunkMaster()
	{
		return this.trunkMaster;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isPersistentlyDisabled()
	{
		return this.persistentlyDisabled;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isFiconSupported()
	{
		return this.ficonSupported;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isBlocked()
	{
		return this.blocked;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getProhibitPortNumbers()
	{
		if(this.prohibitPortNumbers == "null")
		{
			this.prohibitPortNumbers = "";
		}
		return this.prohibitPortNumbers;
	}

	//////////////////////////////////////////////////////////////////////

	public int getProhibitPortCount()
	{
		return this.prohibitPortCount;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isNpivCapable()
	{
		return this.npivCapable;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isNpivEnabled()
	{
		return this.npivEnabled;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isFcFastWriteEnabled()
	{
		return this.fcFastWriteEnabled;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isIslRrdyEnabled()
	{
		return this.islRrdyEnabled;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isRateLimitCapable()
	{
		return this.rateLimitCapable;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isRateLimited()
	{
		return this.rateLimited;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isQosCapable()
	{
		return this.qosCapable;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isQosEnabled()
	{
		return this.qosEnabled;
	}

	//////////////////////////////////////////////////////////////////////

	public int getFcrFabricId()
	{
		return this.fcrFabricId;
	}

	//////////////////////////////////////////////////////////////////////

	public String getState()
	{
		return this.state;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isOccupied()
	{
		return this.occupied;
	}

	//////////////////////////////////////////////////////////////////////

	public int getMasterPortNumber()
	{
		return this.masterPortNumber;
	}

	//////////////////////////////////////////////////////////////////////

	public void setVirtualFibreChannelSwitchKey(String virtual_fibre_channel_switch_key)
	{
		this.virtual_fibre_channel_switch_key = virtual_fibre_channel_switch_key;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public String getVirtualFibreChannelSwitch()
	{
		return this.virtual_fibre_channel_switch_key;
	}
	
	//////////////////////////////////////////////////////////////////////
}
