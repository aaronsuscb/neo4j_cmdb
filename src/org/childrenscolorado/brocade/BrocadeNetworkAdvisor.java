package org.childrenscolorado.brocade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public class BrocadeNetworkAdvisor
{
	private HttpRequest request = null;
	private HttpResponse<JsonNode> json_response = null;
	private HashMap<String,String> http_login_headers = null;
	private HashMap<String,String> http_query_headers = null;
	
	private String query = "UNWIND {objects} as object CREATE (bna:BnaServer) SET bna = object";
	private HashMap<String,ArrayList<EndDevicePort>> params = new HashMap<String,ArrayList<EndDevicePort>>();
	
	//////////////////////////////////////////////////////////////////////
	
	public HashMap<String,String> BnaLogin(String bna_protocol,
										   String bna_server, 
										   String bna_username, 
										   String bna_password)
	{

		this.http_login_headers = new HashMap<String,String>();
		this.http_query_headers = new HashMap<String,String>();
		
		this.http_login_headers.put("Accept", "application/vnd.brocade.networkadvisor+json;version=v1");
		this.http_login_headers.put("WSUsername", bna_username);
		this.http_login_headers.put("WSPassword", bna_password);
		
		this.http_query_headers.put("Accept", "application/vnd.brocade.networkadvisor+json;version=v1");
		this.http_query_headers.put("WSToken", "");
		
		try
		{
			// Login to BNA
			this.request = Unirest.post(bna_protocol + "://" + bna_server + "/rest/login");
	
			for(Map.Entry<String,String> http_login_header : this.http_login_headers.entrySet())
			{
				this.request.header(http_login_header.getKey(), http_login_header.getValue());
			}
	
			this.json_response = this.request.asJson();
	
			if(this.json_response.getStatus() == 200)
			{
				this.http_query_headers.put("WSToken", this.json_response.getHeaders().getFirst("WStoken"));
				System.out.println("* WSToken = " + this.http_query_headers.get("WSToken"));
			}
			else
			{
				System.out.println(bna_server + " login failed");
				System.out.println("\tHTTP Response: " + this.json_response.getStatus());
				System.out.println("\t" + this.json_response.getStatusText());
	
				try
				{
					Unirest.shutdown();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (UnirestException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return this.http_query_headers;
	}

	//////////////////////////////////////////////////////////////////////
	
	public int BnaLogOut(String bna_server)
	{
		try
		{
			// Logout of BNA
			this.request = Unirest.post("http://" + bna_server + "/rest/logout");
			this.request.header("WSToken", this.http_login_headers.get("WSToken"));
			
			/*
			 * TODO: Test for response code 204
			 */
			try
			{
				Unirest.shutdown();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return this.json_response.getStatus();
	}
	
	//////////////////////////////////////////////////////////////////////
	
}
