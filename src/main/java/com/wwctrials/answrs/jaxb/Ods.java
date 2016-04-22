package com.wwctrials.answrs.jaxb;

import javax.xml.bind.annotation.*;


/**
 * @brief A class to represent a nodes.xml object data source 
 * tag <ods>
 * @details 
 */
public class Ods extends Ds
{
	@XmlElement
	private String sid;

	public Ods()
	{
	}

	public Ods(String cty, String ste, String tme, String dbn, String sid)
	{
            super(cty, ste, tme, dbn);
            this.sid = sid;
	}


	public void setSubject(String subject)
	{
            this.sid = subject;
	}

}

