package com.wwctrials.answrs.jaxb;

import javax.xml.bind.annotation.*;
import java.util.*;
import java.text.*;
import com.wwctrials.answrs.camel.*;

/**
 * @brief A class representing a nodes.xml message, specific to site data.
 * @details Contains an <sds> tag and multiple <ety> tags.
 */
@XmlRootElement(name = "ipt")
@XmlAccessorType(XmlAccessType.FIELD)	
public abstract class NodesData
{

	// default constructor required by JAXB
	public NodesData()
	{
	}

	public String getDate()
	{
                
                // create a date string
    		final SimpleDateFormat sdfD = new SimpleDateFormat("yyyy-MM-dd");
    		final SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss");
	    	sdfD.setTimeZone(TimeZone.getTimeZone("UTC"));
	    	sdfT.setTimeZone(TimeZone.getTimeZone("UTC"));
    		final String utcDate = sdfD.format(new Date());
    		final String utcTime = sdfT.format(new Date());
		final String utc = utcDate + "T" + utcTime + "Z";
                
                return utc;
	}
        
}


