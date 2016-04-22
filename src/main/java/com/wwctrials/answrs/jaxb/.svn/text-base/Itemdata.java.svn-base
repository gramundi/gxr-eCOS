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
public class Itemdata
{
        /**
         * @brief This places the trial name attribute on the "ipt" tag
         */
	@XmlAttribute
	private String trial;
        /**
         * @brief The <sds> tag
         */
	@XmlElement(name = "sds")
	private Sds sds;
        /**
         * @brief Multiple <ety> tags
         */
	@XmlElement(name = "ety")
	List<Ety> etyList;

	public Itemdata()
	{
	}

	public void populate(String studyName, String headerLine, String line, int pageNo)
	{
                String dbn = "site";
                // country code is included for TAXI compatibility
                String countryCode = "ZZ";
                String formId = "S1_md";
                String pageId = Integer.toString(pageNo);
                String itemId;
                String itemVal;
                
                // create a date string
    		final SimpleDateFormat sdfD = new SimpleDateFormat("yyyy-MM-dd");
    		final SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss");
	    	sdfD.setTimeZone(TimeZone.getTimeZone("UTC"));
	    	sdfT.setTimeZone(TimeZone.getTimeZone("UTC"));
    		final String utcDate = sdfD.format(new Date());
    		final String utcTime = sdfT.format(new Date());
		final String utc = utcDate + "T" + utcTime + "Z";
                // initialise <ety> list
		this.etyList = new ArrayList<Ety>();
                // set trial name
                setTrial(studyName);
                // split out the column header - TAB delimited
		String cols[] = headerLine.split("\t");
                // split out the column data - TAB delimited
		String elems[] = line.split("\t");
                // for this data input, the site number is in column [1]
                String siteNumber = elems[1];

                // populate the <sds> tag
		this.sds = new Sds(countryCode, siteNumber, utc, dbn);

                // populate multiple <ety> tags
		for(int i=0; i<elems.length; i++)
		{
                        itemId = cols[i];
                        itemVal = elems[i];
			addEty(new Ety(formId, pageId, itemId, itemVal));
		}

	}
	
	public void setTrial(String trialName)
	{
		this.trial = trialName;
	}
		
	public void addEty(Ety ety)
	{
		this.etyList.add(ety);
	}

}


