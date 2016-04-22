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
public class SdsNodesData extends NodesData
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

	// default constructor required by JAXB
	public SdsNodesData()
	{
	}

	public SdsNodesData (String trialName, String databaseType)
	{
                // country code is included for TAXI compatibility
                String countryCode = "ZZ";
                
                // initialise <ety> list
		this.etyList = new ArrayList<Ety>();
                // set trial name
                this.trial = trialName;

                // populate the <sds> tag
		this.sds = new Sds(countryCode, "n/a", getDate(), databaseType);
	}

	public void populate(String colHeaders, String line, int pageNo, String formId)
	{
            String pageId = Integer.toString(pageNo);
            String itemId;
            String itemVal;

            // split out the column data - TAB delimited
            // use regex to test for comma's within comma seperated fields
            String elems[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            String cols[] = colHeaders.split(",");
            // for this data input, the site number is in column [0]
            String siteNumber = elems[0];

            this.sds.setSite(siteNumber);

            // populate multiple <ety> tags
            for(int i=0; i<elems.length; i++)
            {
                itemId = cols[i];
                itemVal = elems[i];
                this.etyList.add(new Ety(formId, pageId, itemId, itemVal));
            }
	}
}


