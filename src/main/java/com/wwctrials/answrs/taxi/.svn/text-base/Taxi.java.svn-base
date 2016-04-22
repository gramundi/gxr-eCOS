// java taxi

// no apologies for the state of this code - it's a prototype

package com.wwctrials.answrs.taxi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

public class Taxi {

	// for testing from my windows laptop
	public static void main(String[] args) {
		try {
			String body = readFileAsString("c:\\jpr\\answrs\\subjectlist.xml");
			//System.out.println(body);
			Taxi.getInstance().run(body);
		} catch (Exception e) {
			System.out.println("kak!");
		}
	}

	private static String readFileAsString(String filePath) throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	//TODO this can be done for us by Spring!
	private static Taxi instance = null;

	protected Taxi() {
		// Exists only to defeat instantiation.
	}

	public static Taxi getInstance() {
		if (instance == null) {
			instance = new Taxi();
		}
		return instance;
	}

	public void run(String xmlStr) {

		java.util.Date date = new java.util.Date();
		System.out.println("START: " + new Timestamp(date.getTime()));

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Statement statement = null;
		boolean bContinue = true;

		try {
			// String url = "jdbc:postgresql://baboon:5433/answrs_1.0.5";
			// for testing from my windows laptop
			String url = "jdbc:odbc:PostgreSQL30";
			String user = "wctdba";
			String password = "answrs1";

			con = DriverManager.getConnection(url, user, password);
			// start a transaction
			con.setAutoCommit(false);

			// parse input xml into DOM
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlStr));
			Document doc = db.parse(is);

			// todo - validate against ANSRSW schema
			boolean schemaValid = true;
			bContinue = schemaValid;
			String sqlText = "";
			statement = con.createStatement();
			String schemaStr = "";
			String incomingStr = "";
			String strError = "";
			int subjId = -1;
			int xFormId = -1;

			// test whether schema is valid first
			if (bContinue) {
				// get the datasource details
				NodeList schemaList = doc.getElementsByTagName("target-schema");
				schemaStr = schemaList.item(0).getTextContent();
				NodeList incomingList = doc.getElementsByTagName("incoming-timestamp");
				incomingStr = incomingList.item(0).getTextContent();

				sqlText = "select count(*) from umgr.study where study_name = '" + schemaStr + "'";
				rs = statement.executeQuery(sqlText);
				if (!rs.next()) {
					bContinue = false;
				}
			}

			// set study 
			if (bContinue) {
				pst = con.prepareStatement("SET search_path='" + schemaStr + "','public';");
				pst.execute();

				rs = statement.executeQuery("select current_schema;");
				if (!rs.next()) {
					bContinue = false;
				}
			}

			if (bContinue) {
				NodeList countryList = doc.getElementsByTagName("country");
				// loop through all countries
				for (int i = 0; i < countryList.getLength(); i++) {
					Node countryNode = countryList.item(i);
					Element ctryElement = (Element) countryList.item(i);

					if (countryNode.hasChildNodes()) {
						// first node is ISO
						String strCountry = ctryElement.getElementsByTagName("iso").item(0).getTextContent();
						strCountry.trim();

						// get country id from database
						int countryId = this.getCountryIdCode(con, strCountry);
						System.out.println("ID = " + countryId);
						if (countryId == -1) {
							// add new country if needed
							sqlText = "insert into country (country_name, country_code_" + strCountry.length() + ") values ('" + strCountry + "','"
									+ strCountry + "')";
							System.out.println(sqlText);
							int updateCount = statement.executeUpdate(sqlText);
							if (updateCount <= 0) {
								bContinue = false;
							}
							System.out.println("count = " + updateCount);
							countryId = this.getCountryIdCode(con, strCountry);
						}

						if (bContinue) {
							// get the parent tags to process
							NodeList parentList = ctryElement.getElementsByTagName("parent");
							// loop through each parent element
							for (int j = 0; j < parentList.getLength(); j++) {
								Element prntElement = (Element) parentList.item(j);
								String pageId = prntElement.getElementsByTagName("id").item(0).getTextContent();
								String domainStr = prntElement.getElementsByTagName("domain").item(0).getTextContent();
								System.out.println("ID = [" + pageId + "] DOMAIN = [" + domainStr + "]");

								// get domain details 
								sqlText = "select domain_id, is_parent_country from data_domain where domain_name = '" + domainStr + "'";
								rs = statement.executeQuery(sqlText);
								boolean bIsParentCountry = false;
								int iDomainId = -1;
								if (rs.next()) {
									iDomainId = rs.getInt(1);
									bIsParentCountry = rs.getBoolean(2);
									System.out.println("Domain id = " + iDomainId + " is parent country = " + bIsParentCountry);
								} else {
									bContinue = false;
									strError = "Domain " + domainStr + " does not exist in " + schemaStr + ".data_domain";
								}

								if (bContinue) {
									// get child nodes
									if (prntElement.hasChildNodes()) {
										// todo - update or insert site/drug or subject with data entered or modified
										// does site/drug/subject exist
										sqlText = "select count(*) from " + domainStr + " where " + domainStr + "_name = '" + pageId + "'";
										System.out.println("SQL : " + sqlText);
										rs = statement.executeQuery(sqlText);
										if (rs.next()) {
											int count = rs.getInt(1);
											if (count > 0) {
												// already exists so update 'modified'
												String sql = "UPDATE " + domainStr + " SET " + domainStr + "_modified='" + incomingStr + "' WHERE "
														+ domainStr + "_name = '" + pageId + "'";
												System.out.println("SQL : " + sql);

												// Execute the update statement
												int updateCount = statement.executeUpdate(sql);
												if (updateCount <= 0) {
													System.out.println("update error!!");
													bContinue = false;
												}
											} else {
												// need to add domain parent
												if (bIsParentCountry) {
													// add site
													sqlText = "insert into " + domainStr + " (site_name, country_id, site_entered) values ('"
															+ pageId + "'," + countryId + ",'" + incomingStr + "')";
													System.out.println(sqlText);
													int updateCount = statement.executeUpdate(sqlText);
													if (updateCount <= 0) {
														bContinue = false;
													}
													System.out.println("count = " + updateCount);
												} else {
													// add subject/drug
													sqlText = "insert into " + domainStr + " (" + domainStr + "_name, " + domainStr
															+ "_entered) values ('" + pageId + "','" + incomingStr + "')";
													System.out.println(sqlText);
													int updateCount = statement.executeUpdate(sqlText);
													if (updateCount <= 0) {
														bContinue = false;
													}
													System.out.println("count = " + updateCount);

												}
											}

										}

										if (bContinue) {
											/// why?
											int iSiteId = -1;
											if (domainStr.equals("site")) {
												rs = statement.executeQuery("select site_id from site where site_name='" + pageId + "';");
												while (rs.next()) {
													iSiteId = rs.getInt(1);
													System.out.println("Database site_id = " + iSiteId);
												}
											}
											/// why?

											// parent domain data
											NodeList parentDbList = prntElement.getElementsByTagName("parentDb");
											Element parentDbElement = null;
											if ((parentDbList != null) && (parentDbList.getLength() > 0)) {
												parentDbElement = (Element) parentDbList.item(0);
											}
											// child domain data
											NodeList childDbList = prntElement.getElementsByTagName("childDb");
											Element childDbElement = null;
											if ((childDbList != null) && (childDbList.getLength() > 0)) {
												childDbElement = (Element) childDbList.item(0);
											}

											// parent domain processing
											String strChildPageId = "";
											if ((parentDbList != null) && (parentDbList.getLength() > 0)) {
												Node parentDbNode = parentDbList.item(0);
												if (parentDbNode.hasChildNodes()) {
													strChildPageId = pageId;
													System.out.println("PARENT ID = " + strChildPageId);
												}
											}

											// child domain processing
											String strChildDomain = "";
											int iChildDomainId = -1;
											if ((childDbList != null) && (childDbList.getLength() > 0)) {
												Node childDbNode = childDbList.item(0);
												//System.out.println(childDbNode.getTextContent());
												if (childDbNode.hasChildNodes()) {
													// there are 2 'id' tags - get the last one
													NodeList idNodes = childDbElement.getElementsByTagName("id");
													strChildPageId = idNodes.item(idNodes.getLength() - 1).getTextContent();
													System.out.println("CHILD ID = " + strChildPageId);

													// get the child domain name
													strChildDomain = childDbElement.getElementsByTagName("domain").item(0).getTextContent();
													System.out.println("CHILD DOMAIN = " + strChildDomain);

													// todo - check that child domain exists
													sqlText = "select domain_id from data_domain where domain_name = '" + strChildDomain + "'";
													rs = statement.executeQuery(sqlText);
													if (rs.next()) {
														iChildDomainId = rs.getInt(1);
														System.out.println("Child domain " + strChildDomain + " exists, ID = " + iChildDomainId);
													} else {
														bContinue = false;
														strError = "Child domain " + strChildDomain + " does not exist.";
													}

													if (bContinue) {

														// add child domain row if not found
														sqlText = "select count(*) from " + strChildDomain + " where " + strChildDomain + "_name = '"
																+ strChildPageId + "'";
														System.out.println(sqlText);
														rs = statement.executeQuery(sqlText);
														if (rs.next()) {
															int count = rs.getInt(1);
															if (count > 0) {

																// child domain found so just set modified flag
																String sql = "UPDATE " + strChildDomain + " SET " + strChildDomain + "_modified='"
																		+ incomingStr + "' WHERE " + strChildDomain + "_name = '" + strChildPageId
																		+ "'";
																System.out.println("SQL : " + sql);

																// Execute the update statement
																int updateCount = statement.executeUpdate(sql);
																if (updateCount <= 0) {
																	System.out.println("update error!!");
																	bContinue = false;
																}
															} else {
																// add subject/drug
																sqlText = "insert into " + strChildDomain + " (" + strChildDomain + "_name, "
																		+ strChildDomain + "_entered, site_id) values ('" + strChildPageId + "','"
																		+ incomingStr + "','" + iSiteId + "')";
																System.out.println(sqlText);
																int updateCount = statement.executeUpdate(sqlText);
																if (updateCount <= 0) {
																	bContinue = false;
																}
																System.out.println("count = " + updateCount);

															}
														}
														sqlText = "select " + strChildDomain + "_id from " + strChildDomain + " where "
																+ strChildDomain + "_name = '" + strChildPageId + "'";
														System.out.println(sqlText);
														rs = statement.executeQuery(sqlText);
														if (rs.next()) {
															subjId = rs.getInt(1);
															System.out.println("CHILD _ID = " + subjId);
														}

													}
												}

												if (bContinue) {
													// get the forms for either the Child or Parent
													NodeList formList = null;
													if ((childDbList != null) && (childDbList.getLength() >= 1)) {
														domainStr = strChildDomain;
														System.out.println("CHILD FORM");
														formList = childDbElement.getElementsByTagName("form");
													} else {
														System.out.println("PARENT FORM");
														formList = parentDbElement.getElementsByTagName("form");
													}

													int iFirstDomain = iDomainId;

													System.out.println("num of forms = " + formList.getLength());
													for (int frm = 0; frm < formList.getLength(); frm++) {
														Node frmNode = formList.item(frm);
														Element frmElement = (Element) formList.item(frm);

														if (frmNode.hasChildNodes()) {
															String strFormName = frmElement.getElementsByTagName("id").item(0).getTextContent();
															System.out.println("FORM NAME = " + strFormName);

															int iDom = iChildDomainId; // todo - 2=subject, set properly

															// todo - check whether form exsits
															// select * from domain form where form_name = [domainStr + "_" + strFormName]

															// get form id
															int iFormId = this.getFormId(con, strFormName, iDom);
															System.out.println("FORM ID = " + iFormId);

															if (iFormId < 0) {
																// add form
																sqlText = "insert into domain_form (form_name, domain_id) values ('" + strFormName
																		+ "','" + iDom + "')";
																System.out.println(sqlText);
																int updateCount = statement.executeUpdate(sqlText);
																if (updateCount <= 0) {
																	bContinue = false;
																} else {
																	iFormId = this.getFormId(con, strFormName, iDom);
																	System.out.println("INSERTED FORM ID = " + iFormId);
																}
															}

															if (bContinue) {
																// get iterators
																NodeList iteratorList = frmElement.getElementsByTagName("iterator");
																int iFirstFormId = iFormId;

																for (int iter = 0; iter < iteratorList.getLength(); iter++) {
																	Element iterElement = (Element) iteratorList.item(iter);
																	int iPageId = Integer.parseInt(iterElement.getElementsByTagName("id").item(0)
																			.getTextContent());

																	int iFrm = iFirstFormId;

																	// todo - check if any forms exist for this [domain]_form and page
																	sqlText = "select count(*) from " + strChildDomain + "_form where "
																			+ strChildDomain + "_id = '" + subjId + "' and form_id = " + iFormId
																			+ " and page_id = " + strChildPageId;
																	System.out.println(sqlText);
																	rs = statement.executeQuery(sqlText);
																	if (rs.next()) {
																		int count = rs.getInt(1);
																		if (count > 0) {
																			System.out.println("page exists");
																			// update form_modified
																			String sql = "UPDATE " + strChildDomain + "_form SET form_modified='"
																					+ incomingStr + "' WHERE " + strChildDomain + "_id = " + subjId
																					+ " AND form_id = " + iFrm + " AND page_id = " + strChildPageId;
																			System.out.println("SQL : " + sql);

																			// Execute the insert statement
																			int updateCount = statement.executeUpdate(sql);
																			if (updateCount <= 0) {
																				System.out.println("update error!!");
																			}
																		} else {
																			// add page
																			sqlText = "insert into " + strChildDomain + "_form (" + strChildDomain
																					+ "_id, form_id, page_id, form_entered)";
																			sqlText += " values (" + subjId + "," + iFormId + "," + strChildPageId
																					+ ",'" + incomingStr + "')";
																			System.out.println(sqlText);
																			int updateCount = statement.executeUpdate(sqlText);
																			if (updateCount <= 0) {
																				bContinue = false;
																			}

																		}
																	}

																	sqlText = "select x_form_id from " + strChildDomain + "_form where "
																			+ strChildDomain + "_id = " + subjId + " and form_id = " + iFormId
																			+ " and page_id = " + strChildPageId;
																	System.out.println(sqlText);
																	rs = statement.executeQuery(sqlText);
																	if (rs.next()) {
																		xFormId = rs.getInt(1);
																		System.out.println("X FORM = " + xFormId);
																	}

																	// select * from site_form

																	// if no row - insert site_form

																	// otherwise - update [domain]_form

																	// todo insert row into [domain]_form entry if not already

																	// otherwise update [domain]_form entry

																	NodeList updateList = iterElement.getElementsByTagName("update");

																	for (int upd = 0; upd < updateList.getLength(); upd++) {
																		Element updElement = (Element) updateList.item(upd);

																		if (updElement.hasChildNodes()) {
																			String idStr = updElement.getElementsByTagName("id").item(0)
																					.getTextContent();
																			String valStr = updElement.getElementsByTagName("value").item(0)
																					.getTextContent();
																			//                                                                            if(valStr.length()==0)
																			//                                                                            {
																			//                                                                                valStr = " ";
																			//                                                                            }

																			//System.out.println("ID [" + idStr + "]   VAL [" + valStr + "]");

																			// get form item id from form_item
																			sqlText = "select item_id from form_item where form_id =" + iFrm
																					+ " AND item_name = '" + idStr + "'";
																			System.out.println(sqlText);

																			rs = statement.executeQuery(sqlText);

																			int itemId = 0;
																			if (rs.next()) {
																				itemId = rs.getInt("item_id");
																				System.out.println("item id = " + itemId);

																				// does [domain]_form_item exist
																				sqlText = "select count(*) from " + strChildDomain
																						+ "_form_item where ";
																				sqlText += " x_form_id = " + xFormId + " and form_id = " + iFormId
																						+ " and page_id = " + strChildPageId + " and item_id = "
																						+ itemId;
																				System.out.println(sqlText);
																				rs = statement.executeQuery(sqlText);
																				if (rs.next()) {
																					int count = rs.getInt(1);
																					if (count > 0) {
																						// update
																						// attempt to upadte the [domain]_form_item
																						String sql = "UPDATE " + domainStr
																								+ "_form_item SET item_value='" + valStr
																								+ "' WHERE form_id = " + iFrm + " AND page_id = "
																								+ strChildPageId + " AND item_id = " + itemId;
																						System.out.println("SQL : " + sql);
																						int updateCount = statement.executeUpdate(sql);
																						if (updateCount <= 0) {
																							bContinue = false;
																							System.out.println("FAIL");
																						}
																						sql = "UPDATE " + domainStr
																								+ "_form_item SET item_modified='" + incomingStr
																								+ "' WHERE form_id = " + iFrm + " AND page_id = "
																								+ strChildPageId + " AND item_id = " + itemId;
																						System.out.println("SQL : " + sql);
																						updateCount = statement.executeUpdate(sql);
																						if (updateCount <= 0) {
																							bContinue = false;
																							System.out.println("FAIL");
																						}
																					} else {
																						// insert
																						// insert into [domain]_form_item
																						String sql = "insert into "
																								+ domainStr
																								+ "_form_item (x_form_id, form_id, page_id, item_id, item_value, item_entered)";
																						sql += " values (" + xFormId + "," + iFrm + ","
																								+ strChildPageId + "," + itemId + ",'" + valStr
																								+ "','" + incomingStr + "')";
																						System.out.println("SQL : " + sql);
																						int updateCount = statement.executeUpdate(sql);
																						if (updateCount <= 0) {
																							System.out.println("FAIL");
																							bContinue = false;
																						}
																					}
																				}

																			} else {
																				System.out.println("NO ITEM");
																				// add new item
																				sqlText = "insert into form_item (form_id, item_name, item_long_name,item_type)";
																				sqlText += " values (" + iFrm + ",'" + idStr + "','" + idStr
																						+ "','text')";
																				System.out.println(sqlText);
																				int updateCount = statement.executeUpdate(sqlText);
																				if (updateCount <= 0) {
																					bContinue = false;
																				}

																				rs = statement
																						.executeQuery("select item_id from form_item where form_id ="
																								+ iFrm + " AND item_name = '" + idStr + "'");

																				if (rs.next()) {
																					itemId = rs.getInt("item_id");
																					System.out.println("item id = " + itemId);

																				}

																				// insert into [domain]_form_item
																				String sql = "insert into "
																						+ domainStr
																						+ "_form_item (x_form_id, form_id, page_id, item_id, item_value, item_entered)";
																				sql += " values (" + xFormId + "," + iFrm + "," + strChildPageId
																						+ "," + itemId + ",'" + valStr + "','" + incomingStr + "')";
																				System.out.println("SQL : " + sql);
																				updateCount = statement.executeUpdate(sql);
																				if (updateCount <= 0) {
																					System.out.println("FAIL");
																					bContinue = false;
																				}

																			}
																		}

																	}

																}
															}

														}
													}
												}
											}
										}
									}
								}
							}

						}
					}
				} // for
			}

			if (bContinue) {
				System.out.println("YAY COMMIT!!");
				con.commit();
			} else {
				System.out.println("OOPSIE ROLLBACK !!");
				con.rollback();
			}

		} catch (RuntimeException e) {
			if (con != null) {
				try {
					// Second try catch as the rollback could fail as well
					con.rollback();
					System.out.println("OOPSIE ROLLBACK !!");
				} catch (Exception e1) {

				}
				// throw again the first exception
				//throw e;
			}
		} catch (SAXParseException e) {
			System.out.println("SAXParseException. " + e.getLocalizedMessage());
		} catch (Exception e) {
			System.out.println("Exception. " + e.getLocalizedMessage());
		}

		date = new java.util.Date();
		System.out.println("END:   " + new Timestamp(date.getTime()));

	}

	private int getFormId(Connection con, String strForm, int iDom) {
		int id = -1;

		ResultSet rs = null;
		try {
			Statement statement = con.createStatement();
			rs = statement.executeQuery("select form_id from domain_form where form_name='" + strForm + "' and domain_id=" + iDom + ";");

			if (rs.next()) {
				id = rs.getInt("form_id");
			}
		} catch (Exception e) {
			System.out.println("Exception. " + e.getLocalizedMessage());
		}
		return id;
	}

	private int getCountryIdCode(Connection con, String strCountry) {
		int id = -1;

		ResultSet rs = null;
		try {
			Statement statement = con.createStatement();
			rs = statement.executeQuery("select country_id from country where country_name='" + strCountry + "';");
			if (rs.next()) {
				id = rs.getInt("country_id");
			}
		} catch (Exception e) {
			System.out.println("Exception. " + e.getLocalizedMessage());
		}
		System.out.println("ID = " + id);
		return id;
	}

}
