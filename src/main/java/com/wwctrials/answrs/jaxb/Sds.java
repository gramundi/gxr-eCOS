package com.wwctrials.answrs.jaxb;

import javax.xml.bind.annotation.*;


/**
 * @brief A class to represent a nodes.xml site data source tag <sds>
 * @details 
 */
public class Sds extends Ds
{
	public Sds()
	{
	}

	public Sds(String cty, String ste, String tme, String dbn)
	{
            super(cty, ste, tme, dbn);
	}

}

