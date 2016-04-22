package com.wwctrials.answrs.jaxb;

import javax.xml.bind.annotation.*;
import java.util.*;
import java.text.*;
import com.wwctrials.answrs.camel.*;

@XmlRootElement(name = "ipt")
@XmlAccessorType(XmlAccessType.FIELD)	
public class Medidata
{

	@XmlAttribute
	private String trial;

	@XmlElement(name = "sds")
	private Sds sds;
	@XmlElement(name = "ety")
	List<Ety> etyList;
	
	public Medidata()
	{
	}
	
	public void populate(String line,int pageNo)
	{
		String elems[] = line.split("\t");

    		final SimpleDateFormat sdfD = new SimpleDateFormat("yyyy-MM-dd");
    		final SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss");
	    	sdfD.setTimeZone(TimeZone.getTimeZone("UTC"));
	    	sdfT.setTimeZone(TimeZone.getTimeZone("UTC"));
    		final String utcDate = sdfD.format(new Date());
    		final String utcTime = sdfT.format(new Date());
		final String utc = utcDate + "T" + utcTime + "Z";
		System.out.println(utc);

		//this.sds = new Sds("ZZ", elems[1], utc, "site");
		this.sds = new Sds("ZZ", elems[1], utc, "site");
		this.etyList = new ArrayList<Ety>();

		setTrial(elems[0]);

		addEty(new Ety("S1", Integer.toString(pageNo), "Code", elems[2])); // identifier
		addEty(new Ety("S1", Integer.toString(pageNo), "Deg", Decoder.getDegree(elems[3]))); // degree
		addEty(new Ety("S1", Integer.toString(pageNo), "FN", elems[4])); // first name
		addEty(new Ety("S1", Integer.toString(pageNo), "Init", elems[5])); // middle name
		addEty(new Ety("S1", Integer.toString(pageNo), "Surname", elems[6])); // last name
		addEty(new Ety("S1", Integer.toString(pageNo), "Title", elems[7])); // title
		// elems[8] = job title - not in S1
		// elems[9] = company name - not in S1
		addEty(new Ety("S1", Integer.toString(pageNo), "UR", Decoder.getUserRole(elems[10]))); // site role
		addEty(new Ety("S1", Integer.toString(pageNo), "EA", elems[11])); // email address
		addEty(new Ety("S1", Integer.toString(pageNo), "Tel", elems[12])); // tel
		addEty(new Ety("S1", Integer.toString(pageNo), "Mob", elems[13])); // mob
		addEty(new Ety("S1", Integer.toString(pageNo), "Alt", elems[14])); // pager
		addEty(new Ety("S1", Integer.toString(pageNo), "Fax", elems[15])); // fax
		// elems[16] = active - not in S1
		// elems[17] = comments - not in S1
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


