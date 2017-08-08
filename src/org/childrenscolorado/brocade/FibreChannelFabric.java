package org.childrenscolorado.brocade;

public class FibreChannelFabric
{
	private String 	key					= "";		// "key": "10:00:00:05:1E:E5:F7:01"
	private String 	seedSwitchWwn		= "";		// "seedSwitchWwn": "10:00:00:05:1E:E5:F7:01"
	private String 	name				= "";		// "name": "FITZ_DCX2_FAN-fabric"
	private boolean secure				= false;	// "secure": false
	private boolean adEnvironment		= false;	// "adEnvironment": false
	private String 	contact				= "";		// "contact": null
	private String 	location			= "";		// "location": null
	private String 	description			= "";		// "description": null
	private String 	principalSwitchWwn	= "";		// "principalSwitchWwn": "10:00:00:05:1E:E5:F7:01"
	private String 	fabricName			= "";		// "fabricName": ""
	private int 	virtualFabricId		= 0;		// "virtualFabricId": 31
	private String 	seedSwitchIpAddress	= "";		// "seedSwitchIpAddress": "10.200.22.121"
	
	//////////////////////////////////////////////////////////////////////
	
	public String getKey()
	{
		return key;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setKey(String key)
	{
		this.key = key;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getSeedSwitchWwn()
	{
		return seedSwitchWwn;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setSeedSwitchWwn(String seedSwitchWwn)
	{
		this.seedSwitchWwn = seedSwitchWwn;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getName()
	{
		return name;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setName(String name)
	{
		this.name = name;
	}

	//////////////////////////////////////////////////////////////////////
	
	public boolean isSecure()
	{
		return secure;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setSecure(boolean secure)
	{
		this.secure = secure;
	}

	//////////////////////////////////////////////////////////////////////
	
	public boolean isAdEnvironment()
	{
		return adEnvironment;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setAdEnvironment(boolean adEnvironment)
	{
		this.adEnvironment = adEnvironment;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getContact()
	{
		return contact;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setContact(String contact)
	{
		this.contact = contact;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getLocation()
	{
		return location;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setLocation(String location)
	{
		this.location = location;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getDescription()
	{
		return description;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setDescription(String description)
	{
		this.description = description;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getPrincipalSwitchWwn()
	{
		return principalSwitchWwn;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setPrincipalSwitchWwn(String principalSwitchWwn)
	{
		this.principalSwitchWwn = principalSwitchWwn;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getFabricName()
	{
		return fabricName;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setFabricName(String fabricName)
	{
		this.fabricName = fabricName;
	}

	//////////////////////////////////////////////////////////////////////
	
	public int getVirtualFabricId()
	{
		return virtualFabricId;
	}

	//////////////////////////////////////////////////////////////////////
	
	public void setVirtualFabricId(int virtualFabricId)
	{
		this.virtualFabricId = virtualFabricId;
	}

	//////////////////////////////////////////////////////////////////////
	
	public String getSeedSwitchIpAddress()
	{
		return seedSwitchIpAddress;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	public void setSeedSwitchIpAddress(String seedSwitchIpAddress)
	{
		this.seedSwitchIpAddress = seedSwitchIpAddress;
	}

	//////////////////////////////////////////////////////////////////////
	
}
