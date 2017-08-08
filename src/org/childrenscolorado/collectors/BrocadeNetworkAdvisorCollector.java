package org.childrenscolorado.collectors;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.childrenscolorado.brocade.AccessGateway;
import org.childrenscolorado.brocade.BrocadeNetworkAdvisor;
import org.childrenscolorado.brocade.ConnectedSwitchPort;
import org.childrenscolorado.brocade.EndDevicePort;
import org.childrenscolorado.brocade.EndDevicePorts;
import org.childrenscolorado.brocade.FibreChannelFabric;
import org.childrenscolorado.brocade.FibreChannelFabrics;
import org.childrenscolorado.brocade.FibreChannelPort;
import org.childrenscolorado.brocade.FibreChannelPorts;
import org.childrenscolorado.brocade.PhysicalFibreChannelSwitch;
import org.childrenscolorado.brocade.PhysicalFibreChannelSwitches;
import org.childrenscolorado.brocade.ResourceGroup;
import org.childrenscolorado.brocade.VirtualFibreChannelSwitch;
import org.childrenscolorado.brocade.ResourceGroups;
import org.childrenscolorado.brocade.VirtualFibreChannelSwitches;
import org.childrenscolorado.util.Neo4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


/**
 * 
 * The topology for the BNA rest api as of v12.3 is located at 
 * http://www1.brocade.com/downloads/documents/html_product_manuals/NA_1230_RESTAPI/images/BNA_API_na_topology.png
 * or the full documentation is at
 * http://www1.brocade.com/downloads/documents/html_product_manuals/NA_1230_RESTAPI/wwhelp/wwhimpl/js/html/wwhelp.htm
 * 
 */

public class BrocadeNetworkAdvisorCollector
{

	//////////////////////////////////////////////////////////////////////
	
	public BrocadeNetworkAdvisorCollector()
	{
		
	}

	//////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args)
	{
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Gson gson = new Gson();
        
    	Properties prop = new Properties();
    	InputStream input = null;
    	String config_file = null;

    	/*
    	 * The 1 required argument to this java application is a FQDN to the 
    	 * properties file.  If this is not provided then the application exits.
    	 */
    	if(args.length == 1)
    	{
    		try
	    	{
	    		input = new FileInputStream(args[0]);
	    		config_file = args[0];
	    		
	    		// load a properties file
	    		prop.load(input);
	    		System.out.println("config_file = " + config_file);
	    	}
	    	catch(FileNotFoundException fnfe)
	    	{
	    		System.out.println("ERROR: The config file \"" + args[0] + "\" not found");
	    		System.exit(1);
	    	}
    		catch (IOException e)
    		{
				e.printStackTrace();
			}
    	}
    	else
    	{
    		System.out.println("ERROR: No configuration file was passed in");
    		System.exit(1);
    	}
    	
    	/*
    	 * Assign the values in the properties file to the variables needed
    	 * to connect to BNA and neo4j
    	 */
    	HashMap<String,String> local_neo4j_server_properties = new HashMap<String,String>();
    	local_neo4j_server_properties.put("protocol", prop.getProperty("neo4j.protocol"));
    	local_neo4j_server_properties.put("server",   prop.getProperty("neo4j.server"));
    	local_neo4j_server_properties.put("port",     prop.getProperty("neo4j.port"));
    	local_neo4j_server_properties.put("userid",   prop.getProperty("neo4j.userid"));
    	local_neo4j_server_properties.put("password", prop.getProperty("neo4j.password"));
    	   	
    	String local_bna_protocol	= prop.getProperty("bna.protocol");
		String local_bna_server		= prop.getProperty("bna.server");
		String local_bna_userid		= prop.getProperty("bna.userid");
		String local_bna_password	= prop.getProperty("bna.password");

//		BrocadeNetworkAdvisorCollector bna_collector = new BrocadeNetworkAdvisorCollector();
		BrocadeNetworkAdvisor bna = new BrocadeNetworkAdvisor();
		
		// this is a base64 encoding of neo4J username:password
//		String neo4j_authorization = args[0];
		
		ArrayList<String> bna_servers = new ArrayList<String>();
		String bna_username = local_bna_userid;
		String bna_password = local_bna_password;
		String bna_wstoken = "";
		
		HashMap<String,String> http_bna_login_headers = new HashMap<String,String>();
		HashMap<String,String> http_bna_query_headers = new HashMap<String,String>();
		HashMap<String,String> http_neo4j_login_headers = new HashMap<String,String>();
		HashMap<String,String> http_neo4j_insert_headers = new HashMap<String,String>();
		
		bna_servers.add(local_bna_server);
		
//		String rest_query_string = "";
		ResourceGroups resource_groups = null;
//		ArrayList<AccessGateway> access_gateways = null; //new ArrayList<AccessGateway>();		
		FibreChannelFabrics fibre_channel_fabrics = null;
		VirtualFibreChannelSwitches virtual_fibre_channel_switches = null;
		FibreChannelPorts fibre_channel_ports = null;
		EndDevicePorts end_device_ports = null;
//		ConnectedSwitchPorts connected_switch_ports = null;
		PhysicalFibreChannelSwitches physical_fibre_channel_switches = null;
		
		/*
		 * Login to Neo4j
		 */
		Neo4j neo4j = new Neo4j();
		neo4j.neo4jLogin(local_neo4j_server_properties);
		
//		neo4j.httpPostNodes(json);
		/*
		 * DEBUG: Send gson output to file 
		 */
//		String json_file_path = "U:\\scripts\\dev\\112146_workspace\\git\\secverctl01.thechildrenshospital.org\\CMDB\\src\\org\\childrenscolorado\\data\\bna.json";
//        FileWriter file_writer = null;
//        BufferedWriter buffer_writer = null;
//        PrintWriter out = null;
//        
//		try
//		{
//			file_writer = new FileWriter(json_file_path, true);
//			buffer_writer = new BufferedWriter(file_writer);
//			out = new PrintWriter(buffer_writer);
//		}
//		catch (IOException e1)
//		{
//			e1.printStackTrace();
//		}
		
		/*
		 * Process all BNA servers
		 */
		for(String bna_server : bna_servers)
		{
			/*
			 * TODO: This is ugly code, put this some where else
			 */
			String insert_bna_server =  
				"{" + 
					"\"query\":\"UNWIND {objects} as object MERGE (bna:BnaServer) SET bna \\u003d object\"," +
					"\"params\":" +
					"{" +
						"\"objects\":" +
						"[" +
							"{\"name\":\"" + bna_server + "\"}" +
						"]" +
					"}" +
				"}";
			
			neo4j.httpPostRequest(insert_bna_server);
			insert_bna_server = "";
			
			/*
			 * Retrieve the WSTokin to login without the need for uid and pwd
			 */
			http_bna_query_headers = bna.BnaLogin(local_bna_protocol,
												  bna_server, 
												  bna_username, 
												  bna_password);

			/*
			 * ...query for resource groups (http://prdbna01/resourcegroups/) 
			 */
			resource_groups = new ResourceGroups();
			resource_groups.getBnaObjects(bna_server, 
										  http_bna_query_headers);

//			System.out.println(gson.toJson(resource_groups));
//			out.println(gson.toJson(resource_groups));
			
			/*
			 * Send the resource_groups to Neo4J for insertion
			 */
			neo4j.httpPostRequest(gson.toJson(resource_groups));
			
			/*
			 * Iterate over the resource groups and ...
			 */
			for(ResourceGroup resource_group : resource_groups.getParams().get("objects"))
			{
				/*
				 * Create the relationship between bna_server and resource_group
				 * TODO: This is ugly code, move it somewhere else
				 */
				String insert_bna_server_to_resource_group =  
						"{" + 
							"\"query\":\"MATCH (bna:BnaServer),(brg:BnaResourceGroup) WHERE bna.name='" + bna_server + "' AND brg.key='" + resource_group.getKey() + "' MERGE (bna)<-[:MEMBER_OF]-(brg)\"," +
							"\"params\":" +
							"{" +
								"\"objects\":" +
								"[" +
//									"{\"name\":\"" + bna_server + "\",\"key\":\"" + resource_group.getKey() + "\"}" +
								"]" +
							"}" +
						"}";
				
				
//				System.out.println(insert_bna_server_to_resource_group);
				neo4j.httpPostRequest(insert_bna_server_to_resource_group);

				/********************************************************/
				
//				/*
//				 * ...query for access gateways (http://prdbna01/resourcegroups/<rgkey>/accessgateways)
//				 */
//				
//				access_gateways = AccessGateway.getBnaObjects(bna_server, 
//															  bna_http_query_headers, 
//															  resource_group.getKey());
//				resource_group.setAccessGateways(access_gateways);
//				resource_group.getAccessGateways().forEach(access_gateway -> System.out.println("  " + access_gateway.cypherMergeCommand()));
				
				/********************************************************/
								
				/*
				 * ...query for fibre channel fabrics (http://prdbna01/resourcegroups/<rgkey>/fcfabrics/)
				 */
				fibre_channel_fabrics = new FibreChannelFabrics();

				fibre_channel_fabrics.getBnaObjects(bna_server, http_bna_query_headers, resource_group.getKey());
						 
//				System.out.println(gson.toJson(fibre_channel_fabrics));
//				out.println(gson.toJson(fibre_channel_fabrics));

				neo4j.httpPostRequest(gson.toJson(fibre_channel_fabrics));
//			
				for(FibreChannelFabric fibre_channel_fabric : fibre_channel_fabrics.getParams().get("objects"))
				{
//					System.out.println("  * " + fibre_channel_fabric.getKey());
					
					/*
					 * Create the relationship between bna_server and resource_group
					 * TODO: This is ugly code, move it somewhere else
					 */
					String insert_resource_group_to_fibre_channel_fabric =  
							"{" + 
								"\"query\":\"MATCH (brg:BnaResourceGroup),(fcf:FibreChannelFabric) WHERE brg.key='" + resource_group.getKey() + "' AND fcf.name='" + fibre_channel_fabric.getName() + "' MERGE (brg)<-[:MEMBER_OF]-(fcf)\"," +
								"\"params\":" +
								"{" +
									"\"objects\":" +
									"[" +
//										"{\"name\":\"" + bna_server + "\",\"key\":\"" + resource_group.getKey() + "\"}" +
									"]" +
								"}" +
							"}";
					
					
//					System.out.println(insert_resource_group_to_fibre_channel_fabric);
					neo4j.httpPostRequest(insert_resource_group_to_fibre_channel_fabric);

					/****************/
					
					/*
					 * ...query for enddevices (http://prdbna01/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/enddevices)
					 */
					
					/****************/
					
					/*
					 * ...query for enddeviceconnections (http://prdbna01/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/enddeviceconnections)
					 */
					
					/****************/
					
					/*
					 * ...query for ifls (http://prdbna01/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/ifls)
					 */
					
					/****************/
					
					/*
					 * ...query for isls (http://prdbna01/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/isls)
					 */

					/****************/
					
					/*
					 * ...query for zonedbs,zones,zonesets,zonealiases 
					 */

					/****************/

					/*
					 * ...query for virtual fcswitches (http://<bna_server>/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/fcswitches)
					 */
					virtual_fibre_channel_switches = new VirtualFibreChannelSwitches();

					virtual_fibre_channel_switches.getBnaObjects(bna_server, 
																 http_bna_query_headers, 
																 resource_group.getKey(), 
																 fibre_channel_fabric.getKey());
//					System.out.println("" + gson.toJson(virtual_fibre_channel_switches));
//					out.println(gson.toJson(virtual_fibre_channel_switches));

					neo4j.httpPostRequest(gson.toJson(virtual_fibre_channel_switches));
					
					for(VirtualFibreChannelSwitch virtual_fibre_channel_switch : virtual_fibre_channel_switches.getParams().get("objects"))
					{
						/*
						 * Connect the FibreChannelFabric to the VirtualFibreChannelSwitch
						 */
						String insert_fibre_channel_fabric_to_virtual_fiber_channel_switch =  
								"{" + 
									"\"query\":\"MATCH (fcf:FibreChannelFabric),(vfcs:VirtualFibreChannelSwitch) WHERE fcf.key='" + fibre_channel_fabric.getKey() + "' AND vfcs.wwn='" + virtual_fibre_channel_switch.getWwn() + "' MERGE (fcf)<-[:MEMBER_OF]-(vfcs)\"," +
									"\"params\":" +
									"{" +
										"\"objects\":" +
										"[" +
//											"{\"name\":\"" + bna_server + "\",\"key\":\"" + resource_group.getKey() + "\"}" +
										"]" +
									"}" +
								"}";
						
						
//						System.out.println(insert_fibre_channel_fabric_to_virtual_fiber_channel_switch);
						neo4j.httpPostRequest(insert_fibre_channel_fabric_to_virtual_fiber_channel_switch);

						/****************/
						
						/*
						 * ...query for gigeports (http://<bna_server>/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/fcswitches/<fcskey>/gigeports)
						 */
						
						/****************/
						
						/*
						 * ...query for fcports (http://<bna_server>/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/fcswitches/<fcskey>/fcports)
						 */
						fibre_channel_ports = new FibreChannelPorts();

						fibre_channel_ports.getBnaObjects(bna_server, 
														  http_bna_query_headers, 
														  resource_group.getKey(),
														  fibre_channel_fabric.getKey(),
														  virtual_fibre_channel_switch.getKey());
//						System.out.println("" + gson.toJson(fibre_channel_ports));
//						out.println(gson.toJson(fibre_channel_ports));
						
						neo4j.httpPostRequest(gson.toJson(fibre_channel_ports));

						for(FibreChannelPort fibre_channel_port : fibre_channel_ports.getParams().get("objects"))
						{
							/*
							 * Connect the VirtualFibreChannelSwitch to the FibreChannelPort
							 */
							String insert_virtual_fiber_channel_switch_to_fibre_channel_port =  
									"{" + 
										"\"query\":\"MATCH (vfcs:VirtualFibreChannelSwitch),(fcp:FibreChannelPort) WHERE vfcs.key='" + virtual_fibre_channel_switch.getKey() + "' AND fcp.virtual_fibre_channel_switch_key='" + fibre_channel_port.getVirtualFibreChannelSwitch() + "' MERGE (vfcs)<-[:MEMBER_OF]-(fcp)\"," +
										"\"params\":" +
										"{" +
											"\"objects\":" +
											"[" +
//												"{\"name\":\"" + bna_server + "\",\"key\":\"" + resource_group.getKey() + "\"}" +
											"]" +
										"}" +
									"}";
							
							
//							System.out.println(insert_virtual_fiber_channel_switch_to_fibre_channel_port);
							neo4j.httpPostRequest(insert_virtual_fiber_channel_switch_to_fibre_channel_port);

							/****************/
							
							end_device_ports = new EndDevicePorts();

							end_device_ports.getBnaObjects(bna_server, 
														   http_bna_query_headers, 
														   resource_group.getKey(),
														   fibre_channel_fabric.getKey(),
														   virtual_fibre_channel_switch.getKey(),
														   fibre_channel_port.getKey());
							neo4j.httpPostRequest(gson.toJson(end_device_ports));

							for(EndDevicePort end_device_port: end_device_ports.getParams().get("objects"))
							{
								String insert_fibre_channel_port_to_end_device_port =  
										"{" + 
											"\"query\":\"MATCH (fcp:FibreChannelPort),(edp:EndDevicePort) WHERE fcp.key='" + fibre_channel_port.getKey() + "' AND edp.switchPortWwn='" + end_device_port.getSwitchPortWwn() + "' MERGE (fcp)<-[:MEMBER_OF]-(edp)\"," +
											"\"params\":" +
											"{" +
												"\"objects\":" +
												"[" +
	//												"{\"name\":\"" + bna_server + "\",\"key\":\"" + resource_group.getKey() + "\"}" +
												"]" +
											"}" +
										"}";
	
								System.out.println(insert_fibre_channel_port_to_end_device_port);
								neo4j.httpPostRequest(insert_fibre_channel_port_to_end_device_port);
								end_device_ports = null;
							}

							/****************/
							
							/*
							* ...query for connected switch ports (http://prdbna01/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/fcswitches/<fcskey>/fcports/<fcpkey>/connectedswitchports)
							*/
////							connected_switch_ports = ConnectedSwitchPort.getBnaObjects(bna_server, 
////																					   bna_http_query_headers, 
////																					   resource_group.getKey(), 
////																					   fibre_channel_fabric.getKey(),
////																					   virtual_fibre_channel_switch.getKey(),
////																					   fibre_channel_port.getKey());
////							
////							fibre_channel_port.setConnectedSwitchPorts(connected_switch_ports);
////							
////							for(ConnectedSwitchPort connected_switch_port : fibre_channel_port.getConnectedSwitchPorts())
////							{
////								System.out.println("        " + connected_switch_port.cypherMergeCommand());
////							
////								/*
////								 * Connect the FibreChannelPort to the ConnectedSwitchPort
////								 */
////								System.out.println("    " + 
////										"MERGE " +
////										"(fcp_" + fibre_channel_port.getKey().replace(":", "").replace("-", "") + ":FibreChannelPort {key:'" + fibre_channel_port.getKey() + "'})" +
////										"<-[:MEMBER_OF]-" +
////										"(csp_" + connected_switch_port.getKey().replace(":", "").replace("-", "") + ":ConnectedSwitchPort {key:'" + connected_switch_port.getKey() + "'})"
////										);
////							}
//							/****************/
//							
//							/*
//							* ...query for end device ports (http://prdbna01/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/fcswitches/<fcskey>/fcports/<fcpkey>/enddeviceports)
//							*/
////							end_device_ports = EndDevicePort.getBnaObjects(bna_server, 
////																		   bna_http_query_headers, 
////																		   resource_group.getKey(),
////																		   fibre_channel_fabric.getKey(),
////																		   virtual_fibre_channel_switch.getKey(),
////																		   fibre_channel_port.getKey());
////							
////							fibre_channel_port.setEndDevicePorts(end_device_ports);
////							
////							for(EndDevicePort end_device_port : fibre_channel_port.getEndDevicePorts())
////							{
////								System.out.println("        " + end_device_port.cypherMergeCommand());
////							
////								/*
////								 * Connect the FibreChannelPort to the EndDevicePort
////								 */
////								System.out.println("    " + 
////										"MERGE " +
////										"(fcp_" + fibre_channel_port.getKey().replace(":", "").replace("-", "") + ":FibreChannelPort {key:'" + fibre_channel_port.getKey() + "'})" +
////										"<-[:MEMBER_OF]-" +
////										"(edp_" + end_device_port.getKey().replace(":", "").replace("-", "") + ":EndDevicePort {key:'" + end_device_port.getKey() + "'})"
////										);
////							}
								
							/****************/
						}  // end fibre channel port
//						
//						/****************/
//						
//						/*
//						 * ...query for trunks (http://<bna_server>/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/fcswitches/<fcskey>/trunks)
//						 */
//						
//						/****************/
//						
//						/*
//						 * ...query for physicalswitches (http://<bna_server>/resourcegroups/<rgkey>/fcfabrics/<fcfkey>/fcswitches/<fcskey>/physicalswitches)
//						 */
//						
//						physical_fibre_channel_switches = new PhysicalFibreChannelSwitches();
//
//						physical_fibre_channel_switches.getBnaObjects(bna_server, 
//																	  http_bna_query_headers, 
//																	  resource_group.getKey(), 
//																	  fibre_channel_fabric.getKey(), 
//																	  virtual_fibre_channel_switch.getKey());
//						
//						System.out.println("" + gson.toJson(physical_fibre_channel_switches));
//						//out.println(gson.toJson(physical_fibre_channel_switches));
//						
//						neo4j.httpPostRequest(gson.toJson(physical_fibre_channel_switches));
//
//						/****************/
					}  // end fibre channel virtual switch
					
					/****************/
					
				}  // end iterate fibre_channel_fabric
				
				/********************************************************/

//				promptEnterKey();
				
			}  // end for resource_groups
			
			bna.BnaLogOut(bna_server);

			/*
			 * DEBUG: Close the file writers
			 */
//			try
//			{
//				out.close();
//				buffer_writer.close();
//				file_writer.close();
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
			
		}  // end for bna_servers

		/**********************************/
		
//		/*
//		 * Output virtual fibre channel switch + fibre channel port
//		 */
//		
//		for(ResourceGroup resource_group : resource_groups)
//		{
////			System.out.println(resource_group.cypherMergeCommand());
//			
//			for(VirtualFibreChannelSwitch virtual_fibre_channel_switch : resource_group.getVirtualFibreChannelSwitches())
//			{
//				for(PhysicalFibreChannelSwitch physical_fibre_channel_switch : virtual_fibre_channel_switch.getPhysicalFibreChannelSwitches())
//				{
//					System.out.println(physical_fibre_channel_switch.cypherMergeCommand());
//				}
//			}  // end for virtual_fibre_channel_switch
//			
//			for(FibreChannelFabric fibre_channel_fabric : resource_group.getFibreChannelFabrics())
//			{
//				System.out.println("    " + fibre_channel_fabric.cypherMergeCommand());
//			}  // end for fibre_channel_fabric
//		}  // end for resource_group
		
		/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
//		for (HashMap.Entry<VirtualFibreChannelSwitch,ArrayList<FibreChannelPort>> virtual_fibre_channel_switch : fibre_channel_ports.entrySet())
//		{
//			System.out.println(virtual_fibre_channel_switch.getKey().cypherMergeCommand());
//			
//			for(FibreChannelPort port : virtual_fibre_channel_switch.getValue())
//			{
//				try
//				{
//					/*
//					 * Define the FibreChannelPort as a Node in the Neo4J
//					 */
//					System.out.println("  " + port.cypherMergeCommand());
//					/*
//					 * Define the relationship between the ViratualFibreChannelSwitch and the FibreChannelPort
//					 * 
//					 *  MERGE (:VirtualFibreChannelSwitch {key:'10:00:00:05:1E:D3:E2:01'})<-[:MEMBER_OF]-(:FibreChannelPort {key:'20:FE:00:05:1E:D3:E2:01'})
//					 *  
//					 *  MATCH (a:Person),(b:Person)
//					 *  WHERE a.name = 'Node A' AND b.name = 'Node B'
//					 *  CREATE (a)-[r:RELTYPE]->(b)
//					 *  
//					 */
//	//				System.out.println("    MERGE " +
//	//						/*
//	//						 * virtual_fibre_channel_switch
//	//						 *   .getKey() = Retrieve the HashMap key which is a VirtualFibreChannelSwitch Object
//	//						 *   .getKey() = Get the VirtualFibreChannelSwitch Object value Key
//	//						 */
//	//						"(vfcs.:VirtualFibreChannelSwitch {key:'" + virtual_fibre_channel_switch.getKey().getKey() + "'})" + 
//	//						"<-[:MEMBER_OF]-" + 
//	//						"(fcp.:FibreChannelPort {key:'" + port.getKey() + "'})"
//	//						);
//					
//	//				System.out.println("    " + 
//	//					"MATCH(wwn" + virtual_fibre_channel_switch.getKey().getKey().replace(":", "") + ":VirtualFibreChannelSwitch),(wwn." + port.getKey().replace(":", "") + ":FibreChannelPort) " + 
//	//					"WHERE " + virtual_fibre_channel_switch.getKey().getKey().replace(":", "") + ".key = '" + virtual_fibre_channel_switch.getKey().getKey() + "' " + 
//	//					"AND " + port.getKey().replace(":", "") + ".key = '" + port.getKey() + "' " + 
//	//					"MERGE (wwn" + virtual_fibre_channel_switch.getKey().getKey().replace(":", "") + ")<-[:MEMBER_OF]-(wwn" + port.getKey().replace(":", "") + ")"
//	//					);
//	
//					// MERGE (vfcs5000533933A78F01:VirtualFibreChannelSwitch)<-[:MEMBER_OF]-(fcpXF5000533933A78A08:FibreChannelPort)
//					
//					
//					System.out.println("    " + 
//						"MERGE (vfcs_" + virtual_fibre_channel_switch.getKey().getKey().replace(":", "") + ":VirtualFibreChannelSwitch {key:'" + virtual_fibre_channel_switch.getKey().getKey() + "'})" + 
//						"<-[:MEMBER_OF]-" + 
//						"(fcp_" + port.getKey().replace(":", "").replace("-", "") + ":FibreChannelPort {key:'" + port.getKey() + "'})"
//						);
//				}
//				catch(Exception e)
//				{
//					/*
//					 * In some BNA Rest calls there are null values reported back.
//					 * This try/catch captures the error when trying to print a null value 
//					 */
//				}
//			}
//		}
		/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/

//		neo4jLogin(neo4j_authorization);

		try
		{
			Unirest.shutdown();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//////////////////////////////////////////////////////////////////////

	@SuppressWarnings("resource")
	public static void promptEnterKey()
	{
	   System.out.println("Press \"ENTER\" to continue...");
	   System.out.println("");
	   System.out.println("*********************************************");
	   System.out.println("");
	   java.util.Scanner scanner = new java.util.Scanner(System.in);
	   scanner.nextLine();
	}
	
	//////////////////////////////////////////////////////////////////////
}
