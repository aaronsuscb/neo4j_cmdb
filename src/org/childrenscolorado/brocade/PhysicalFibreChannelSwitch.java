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

public class PhysicalFibreChannelSwitch
{

	private String 	key 					= "";		// "key": "10:00:00:05:1E:D3:E2:FF"
	private String 	wwn 					= "";		// "wwn": "10:00:00:05:1E:D3:E2:FF"
	private String 	ipAddress 				= "";		// "ipAddress": "10.200.22.120"
	private String 	userId 					= "";		// "userId": "admin"
	private String 	firmwareVersion			= "";		// "firmwareVersion": "v7.2.1b"
	private String 	vendor 					= "";		// "vendor": "Brocade Communications, Inc."
	private String 	supplierSerialNumber 	= "";		// "supplierSerialNumber": "BRCAFX0615F01V"
	private String 	partNumber 				= "";		// "partNumber": "60-1000491-05"
	private String 	modelNumber 			= "";		// "modelNumber": "EMC Connectrix ED-DCX-B"
	private String 	manufacturer 			= "";		// "manufacturer": "EMC"
	private String 	switchSerialNumber 		= "";		// "switchSerialNumber": "AFX0615F01V"
	private String 	vendorVersion 			= "";		// "vendorVersion": "none"
	private String 	vendorPartNumber 		= "";		// "vendorPartNumber": "CONTRX0000DCX"
	private boolean snmpInformsEnabled 		= false;	// "snmpInformsEnabled": true
	private String 	contact 				= "";		// "contact": "ISD Data Storage Team (720-777-7867)"
	private String 	location 				= "";		// "location": "Fitzsimmons Data Center, 13123 E. 16th Ave, Aurora, CO 80045"
	private String 	description 			= "";		// "description": "Left DCX"
	private String  fibre_channel_fabric_key = "";		// This is the linkage between the fibre channel fabric and the physical fibre channel switch
	
	//////////////////////////////////////////////////////////////////////
	
	public PhysicalFibreChannelSwitch()
	{
		
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public static ArrayList<PhysicalFibreChannelSwitch> getBnaObjects(String bna_server,
		  	  HashMap<String,String> http_query_headers,
		  	  String resource_group_key,
		  	  String fc_switch_key)
	{
		ArrayList<PhysicalFibreChannelSwitch> bna_objects = new ArrayList<PhysicalFibreChannelSwitch>();
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_query_string = "";
		
		try
		{
			rest_query_string = "http://" + bna_server + 
				"/rest/resourcegroups/" + resource_group_key + 
				"/fcswitches/" + fc_switch_key + 
				"/physicalswitches";
			
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
		
				for(Object node: json_response.getBody().getObject().getJSONArray("physicalSwitches"))
				{
//						System.out.println(json_response.getBody().toString());
		
					Gson gson = new Gson();
					bna_objects.add(gson.fromJson(node.toString(), PhysicalFibreChannelSwitch.class));
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
	
	public static ArrayList<PhysicalFibreChannelSwitch> getBnaObjects(String bna_server,
													  	  HashMap<String,String> http_query_headers,
													  	  String resource_group_key,
													  	  String fc_fabric_key,
													  	  String fc_switch_key)
	{
		ArrayList<PhysicalFibreChannelSwitch> bna_objects = new ArrayList<PhysicalFibreChannelSwitch>();
		HttpRequest request = null;
		HttpResponse<JsonNode> json_response = null;
		String rest_query_string = "";
		
		try
		{
			rest_query_string = "http://" + bna_server + 
				"/rest/resourcegroups/" + resource_group_key + 
				"/fcfabrics/" + fc_fabric_key + 
				"/fcswitches/" + fc_switch_key + 
				"/physicalswitches";
			
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
		
				for(Object node: json_response.getBody().getObject().getJSONArray("physicalSwitches"))
				{
//						System.out.println(json_response.getBody().toString());
		
					Gson gson = new Gson();
					bna_objects.add(gson.fromJson(node.toString(), PhysicalFibreChannelSwitch.class));
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
		
		cypher = "MERGE (ps_"			+ this.key.replace(":", "")	+ ":FibreChannelChassis {" +
			"key:'" 					+ this.key 					+ "', " +
			"wwn:'" 					+ this.wwn 					+ "', " +
			"ipAddress:'" 				+ this.ipAddress 			+ "', " +
			"userId:'" 					+ this.userId 				+ "', " +
			"firmwareVersion:'" 		+ this.firmwareVersion 		+ "', " +
			"vendor:'" 					+ this.vendor 				+ "', " +
			"supplierSerialNumber:'" 	+ this.supplierSerialNumber + "', " +
			"partNumber:'" 				+ this.partNumber 			+ "', " +
			"modelNumber:'" 			+ this.modelNumber 			+ "', " +
			"manufacturer:'" 			+ this.manufacturer 		+ "', " +
			"switchSerialNumber:'"		+ this.switchSerialNumber 	+ "', " +
			"vendorVersion:'" 			+ this.vendorVersion 		+ "', " +
			"vendorPartNumber:'" 		+ this.vendorPartNumber 	+ "', " +
			"snmpInformsEnabled:"		+ this.snmpInformsEnabled 	+ ", " +
			"contact:'" 				+ this.contact 				+ "', " +
			"location:'" 				+ this.location 			+ "', " +
			"description:'" 			+ this.description 			+ "' " + 
			"})"; 

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

	public String getIpAddress()
	{
		return this.ipAddress;
	}

	//////////////////////////////////////////////////////////////////////

	public String getUserId()
	{
		return this.userId;
	}

	//////////////////////////////////////////////////////////////////////

	public String getFirmwareVersion()
	{
		return this.firmwareVersion;
	}

	//////////////////////////////////////////////////////////////////////

	public String getVendor()
	{
		return this.vendor;
	}

	//////////////////////////////////////////////////////////////////////

	public String getSupplierSerialNumber()
	{
		return this.supplierSerialNumber;
	}

	//////////////////////////////////////////////////////////////////////

	public String getPartNumber()
	{
		return this.partNumber;
	}

	//////////////////////////////////////////////////////////////////////

	public String getModelNumber()
	{
		return this.modelNumber;
	}

	//////////////////////////////////////////////////////////////////////

	public String getManufacturer()
	{
		return this.manufacturer;
	}

	//////////////////////////////////////////////////////////////////////

	public String getSwitchSerialNumber()
	{
		return this.switchSerialNumber;
	}

	//////////////////////////////////////////////////////////////////////

	public String getVendorVersion()
	{
		return this.vendorVersion;
	}

	//////////////////////////////////////////////////////////////////////

	public String getVendorPartNumber()
	{
		return this.vendorPartNumber;
	}

	//////////////////////////////////////////////////////////////////////

	public boolean isSnmpInformsEnabled()
	{
		return this.snmpInformsEnabled;
	}

	//////////////////////////////////////////////////////////////////////

	public String getContact()
	{
		return this.contact;
	}

	//////////////////////////////////////////////////////////////////////

	public String getLocation()
	{
		return this.location;
	}

	//////////////////////////////////////////////////////////////////////

	public String getDescription()
	{
		return this.description;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getFibre_channel_fabric_key()
	{
		return fibre_channel_fabric_key;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setFibre_channel_fabric_key(String fibre_channel_fabric_key)
	{
		this.fibre_channel_fabric_key = fibre_channel_fabric_key;
	}
	
	//////////////////////////////////////////////////////////////////////
	
}
