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
public class OdsNodesData extends NodesData
{
        /**
         * @brief This places the trial name attribute on the "ipt" tag
         */
	@XmlAttribute
	private String trial;
        /**
         * @brief The <ods> tag
         */
	@XmlElement(name = "ods")
	private Ods ods;
        /**
         * @brief Multiple <ety> tags
         */
	@XmlElement(name = "ety")
	List<Ety> etyList;

	// default constructor required by JAXB
	public OdsNodesData()
	{
	}

	public OdsNodesData (String trialName, String databaseType)
	{
                // country code is included for TAXI compatibility
                String countryCode = "ZZ";
                
                // initialise <ety> list
		this.etyList = new ArrayList<Ety>();
                // set trial name
                this.trial = trialName;

                // populate the <ods> tag
			this.ods = new Ods(countryCode, "n/a", getDate(), databaseType, "n/a");
	}

	public void populate(String colHeaders, String line, int pageNo, String formId)
	{
            String pageId = Integer.toString(pageNo);
            String itemId;
            String itemVal;
            // split out the column data - TAB delimited
            String elems[] = line.split("\t");
            String cols[] = colHeaders.split("\t");
            // for this data input, the site number is in column [0]
            String siteNumber = elems[0];
            // for this data input, the case/drug number is in column [1]
            String subjNumber = elems[1];

            this.ods.setSite(siteNumber);
            this.ods.setSubject(subjNumber);

            // populate multiple <ety> tags
            for(int i=0; i<elems.length; i++)
            {
                itemId = cols[i];
                itemVal = elems[i];
                this.etyList.add(new Ety(formId, pageId, itemId, itemVal));
            }

	}
	

}


