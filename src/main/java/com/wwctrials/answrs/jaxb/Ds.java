package com.wwctrials.answrs.jaxb;

import javax.xml.bind.annotation.*;

/**
 * @brief A class to represent a nodes.xml data source 
 * @details 
 */
public abstract class Ds
{
	@XmlElement
	private String cty;
	@XmlElement
	private String ste;
	@XmlElement
	private String tme;
	@XmlElement
	private String dbn;

	public Ds()
	{
	}

	public Ds(String cty, String ste, String tme, String dbn)
	{
		this.cty = cty;
		this.ste = ste;
		this.tme = tme;
		this.dbn = dbn;
	}

        public void setSite(String site)
	{
		this.ste = site;
	}

}

