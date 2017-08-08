package org.childrenscolorado.util;

import java.util.HashMap;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.request.HttpRequest;

public class RestApi
{

	private HttpRequest request = null;
	private HttpResponse<JsonNode> json_response = null;
	private HashMap<String,String> http_login_headers = null;
	private HashMap<String,String> http_query_headers = null;
	
	//////////////////////////////////////////////////////////////////////
	
	public RestApi()
	{
		
	}
	
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @return - the HTTP response code.  Successful execution will return
	 * 			 a value between 200-299
	 */
	
	public int login()
	{
		return 0;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @return - the HTTP response code.  Successful execution will return
	 * 			 a value between 200-299
	 */
	
	public int logout()
	{
		return 0;
	}
	
	//////////////////////////////////////////////////////////////////////
}
