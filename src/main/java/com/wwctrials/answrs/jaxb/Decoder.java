package com.wwctrials.answrs.jaxb;

import java.util.*;
import com.wwctrials.answrs.camel.*;

public class Decoder
{
	static Hashtable userRoles;
	
	public Decoder()
	{
		// user roles
		userRoles = new Hashtable();
		userRoles.put("Principal Investigator", new Integer(1));
		userRoles.put("Sub Investigator", new Integer(2));
		userRoles.put("Principal Investigator", new Integer(1));
		userRoles.put("Study Coordinator", new Integer(3));
		userRoles.put("Pharmacist", new Integer(4));
		userRoles.put("Rater", new Integer(5));
		userRoles.put("Regulatory", new Integer(6));
		userRoles.put("Contracts", new Integer(7));
		userRoles.put("Budgets", new Integer(8));
		userRoles.put("Ethics / IRB", new Integer(9));
		userRoles.put("Administrative", new Integer(10));
		userRoles.put("Other", new Integer(99));
		
	}

	public static String getUserRole(String roleName)
	{
		return (userRoles.get(roleName)).toString();
	}

	public static String getDegree(String degree)
	{
		String retStr = "99";
		if(degree.indexOf("PhD") > 0)
		{
			retStr = "13";
		}
		else if(degree.indexOf("MD") > 0)
		{
			retStr = "9";
		}
		
		return retStr;
	}	
	
}


