<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0">
	<!-- define how this shall be outputted -->
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		media-type="application/xml" />

	<!-- allow a parameter REMOTE_ADDR to be passed into the style sheet (sender 
		IP). -->
	<xsl:param name="REMOTE_ADDR">
		<!-- the default value -->
		<xsl:text>127.0.0.1</xsl:text>
	</xsl:param>

	<!-- need to define a country code to stuff all the data into -->
	<xsl:variable name="COUNTRY_CODE" select="'ZZ'" />

	<!-- need to define a global site for site data without a site code -->
	<xsl:variable name="GLOBAL_SITE" select="-1" />

	<!-- match the root of the document -->
	<xsl:template match="/">

		<!-- create the root ANSWRS element -->
		<xsl:element name="answrs">

			<!-- create the datasource element (about the message) -->
			<xsl:element name="datasource">
				<!-- when was the message generated -->
				<xsl:element name="incoming-timestamp">
					<xsl:value-of select="current-dateTime()" />
				</xsl:element>
				<!-- when is the input being processed? -->
				<xsl:element name="processed-timestamp">
					<xsl:value-of select="current-dateTime()" />
				</xsl:element>
				<!-- where did the message come from? -->
				<xsl:element name="remote-addr">
					<!-- passed into XSLT processor -->
					<xsl:value-of select="$REMOTE_ADDR" />
				</xsl:element>
				<!-- what is the target schema -->
				<xsl:element name="target-schema">
					<xsl:value-of select="/data/@studyid" />
				</xsl:element>
				<!-- use the XML schema version to identify myEDC type data -->
				<xsl:element name="schema-version">
					<xsl:value-of select="'myEDC'" />
				</xsl:element>
			</xsl:element>

			<xsl:choose>
				<!-- site data -->
				<!-- data_sitelist -->
				<xsl:when test="/data/sites">
					<xsl:call-template name="site_data">
						<xsl:with-param name="RootPath" select="/data/sites/site" />
					</xsl:call-template>
				</xsl:when>
				<!-- data_userlist -->
				<xsl:when test="/data/users">
					<xsl:call-template name="site_data">
						<xsl:with-param name="RootPath" select="/data/users/user" />
					</xsl:call-template>
				</xsl:when>
				<!-- data_userlogins -->
				<xsl:when test="/data/userlogins">
					<xsl:call-template name="site_data">
						<xsl:with-param name="RootPath" select="/data/userlogins/userlogin" />
					</xsl:call-template>
				</xsl:when>
				<!-- data_userhistory -->
				<xsl:when test="/data/userhistory">
					<xsl:call-template name="site_data">
						<xsl:with-param name="RootPath"
							select="/data/userhistory/userrevision" />
					</xsl:call-template>
				</xsl:when>
				<!-- data_rand -->
				<xsl:when test="/data/randlist">
					<xsl:call-template name="site_data">
						<xsl:with-param name="RootPath" select="/data/randlist/rand" />
					</xsl:call-template>
				</xsl:when>
				<!-- data_dispensesitesettings -->
				<xsl:when test="/data/dispensesitesettings">
					<xsl:call-template name="site_data">
						<xsl:with-param name="RootPath"
							select="/data/dispensesitesettings/site" />
					</xsl:call-template>
				</xsl:when>
				<!-- tricky ones with embedded rows -->
				<xsl:when test="/data/queries">
					<xsl:call-template name="data_querylist" />
				</xsl:when>
				<xsl:when test="/data/dispenseshipments">
					<xsl:call-template name="data_dispenseshipments" />
				</xsl:when>

				<!-- subject data -->
				<!-- data_pagestatus -->
				<xsl:when test="/data/pagestatuses">
					<xsl:call-template name="subject_data">
						<xsl:with-param name="RootPath"
							select="/data/pagestatuses/pagestatus" />
					</xsl:call-template>
				</xsl:when>
				<!-- data_epro -->
				<xsl:when test="/data/epros">
					<xsl:call-template name="subject_data">
						<xsl:with-param name="RootPath" select="/data/epros/epro" />
					</xsl:call-template>
				</xsl:when>
				<!-- data_pagelinksattachments -->
				<xsl:when test="/data/pagelinksattach">
					<xsl:call-template name="subject_data">
						<xsl:with-param name="RootPath"
							select="/data/pagelinksattach/linkattach" />
					</xsl:call-template>
				</xsl:when>
				<!-- data_subjectlist -->
				<xsl:when test="/data/subjects">
					<xsl:call-template name="subject_data">
						<xsl:with-param name="RootPath" select="/data/subjects/subject" />
					</xsl:call-template>
				</xsl:when>

				<!-- drug data -->
				<!-- data_dispenseitems -->
				<xsl:when test="/data/dispenseitems">
					<xsl:call-template name="drug_data">
						<xsl:with-param name="RootPath" select="/data/dispenseitems/item" />
					</xsl:call-template>
				</xsl:when>
				<!-- data_dispensebatches -->
				<xsl:when test="/data/dispensebatches">
					<xsl:call-template name="drug_data">
						<xsl:with-param name="RootPath" select="/data/dispensebatches/batch" />
					</xsl:call-template>
				</xsl:when>

				<!-- design data -->
				<!-- all tables are imported - possibly need to specifically define some 
					more -->
				<!-- but don't know which ones yet so leaving it as is -->
				<xsl:when test="contains(/data/@type,'design_')">
					<xsl:choose>
						<xsl:when test="contains(/data/@type,'design_subjectstatus')">
							<xsl:call-template name="site_data">
								<xsl:with-param name="RootPath" select="/data/*/*/*" />
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="contains(/data/@type,'design_pagestatus')">
							<xsl:call-template name="site_data">
								<xsl:with-param name="RootPath" select="/data/*/*/*" />
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="contains(/data/@type,'design_querytypes')">
							<xsl:call-template name="site_data">
								<xsl:with-param name="RootPath" select="/data/*/*/*" />
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="contains(/data/@type,'design_visitschedules')">
							<xsl:call-template name="design_visitschedules" />
						</xsl:when>
						<!-- default -->
						<xsl:otherwise>
							<xsl:call-template name="site_data">
								<xsl:with-param name="RootPath" select="/data/*/*" />
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>

				</xsl:when>

			</xsl:choose>
		</xsl:element>
	</xsl:template>


	<!-- site data -->
	<!-- =============================================================== -->
	<xsl:template name="site_data">
		<xsl:param name="RootPath" />
		<xsl:for-each select="$RootPath">
			<xsl:element name="country">
				<xsl:element name="iso">
					<xsl:value-of select="$COUNTRY_CODE" />
				</xsl:element>

				<!-- create a site element -->
				<xsl:element name="parent">
					<xsl:element name="id">
						<xsl:choose>
							<!-- if there is a site number - use it -->
							<xsl:when test="sitenum[.!='']">
								<xsl:value-of select="sitenum" />
							</xsl:when>
							<!-- otherwise - use the global site -->
							<xsl:otherwise>
								<xsl:value-of select="$GLOBAL_SITE" />
							</xsl:otherwise>
						</xsl:choose>
					</xsl:element>

					<!-- get all data -->
					<xsl:element name="domain">
						<xsl:value-of select="'site'" />
					</xsl:element>

					<xsl:element name="parentDb">
						<xsl:element name="form">
							<xsl:element name="id">
								<xsl:value-of select="/data/@type" />
							</xsl:element>

							<!-- page id -->
							<xsl:element name="iterator">
								<xsl:element name="id">
									<xsl:choose>
										<!-- data_sitelist -->
										<xsl:when test="/data/sites">
											<xsl:value-of select="siteid" />
										</xsl:when>
										<!-- data_userlist -->
										<xsl:when test="/data/users">
											<xsl:value-of select="internalid" />
										</xsl:when>
										<!-- data_userlogins -->
										<xsl:when test="/data/userlogins">
											<xsl:value-of select="position()" />
										</xsl:when>
										<xsl:when test="/data/userhistory">
											<xsl:value-of select="position()" />
										</xsl:when>
										<!-- data_dispensesitesettings -->
										<xsl:when test="/data/dispensesitesettings">
											<xsl:value-of select="siteid" />
										</xsl:when>
										<!-- data_rand -->
										<xsl:when test="/data/randlist">
											<xsl:value-of select="randnum" />
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="position()" />
										</xsl:otherwise>
									</xsl:choose>
								</xsl:element>

								<!-- loop through each one -->
								<xsl:call-template name="item_values" />
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<!-- design_visitschedules -->
	<xsl:template name="design_visitschedules">
		<xsl:element name="country">
			<xsl:element name="iso">
				<xsl:value-of select="$COUNTRY_CODE" />
			</xsl:element>

			<!-- create a site element -->
			<xsl:element name="parent">
				<xsl:element name="id">
					<xsl:value-of select="'-1'" />
				</xsl:element>

				<xsl:element name="domain">
					<xsl:value-of select="'site'" />
				</xsl:element>

				<xsl:element name="parentDb">
					<xsl:for-each select="/data/visitschedules/revision/visit/pages/page">
						<xsl:element name="form">
							<xsl:element name="id">
								<xsl:value-of select="/data/@type" />
							</xsl:element>

							<xsl:element name="iterator">
								<xsl:element name="id">
									<!--xsl:value-of select="../../id" / -->
									<xsl:value-of select="position()" />
								</xsl:element>

								<!-- loop through query header details -->
								<xsl:for-each select="../../*[not(self::pages)]">

									<xsl:element name="update">
										<xsl:element name="id">
											<xsl:value-of select="local-name()" />
										</xsl:element>
										<xsl:element name="value">
											<xsl:value-of select="." />
										</xsl:element>
									</xsl:element>
								</xsl:for-each>

								<!-- loop through query details -->
								<xsl:for-each select="*">
									<xsl:element name="update">
										<xsl:element name="id">
											<xsl:value-of select="'p_'" />
											<xsl:value-of select="local-name()" />
										</xsl:element>
										<xsl:element name="value">
											<xsl:value-of select="." />
										</xsl:element>
									</xsl:element>
								</xsl:for-each>

							</xsl:element>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>


	<!-- data_querylist -->
	<xsl:template name="data_querylist">
		<xsl:element name="country">
			<xsl:element name="iso">
				<xsl:value-of select="$COUNTRY_CODE" />
			</xsl:element>

			<!-- create a site element -->
			<xsl:element name="parent">
				<xsl:element name="id">
					<xsl:value-of select="'-1'" />
				</xsl:element>

				<xsl:element name="domain">
					<xsl:value-of select="'site'" />
				</xsl:element>

				<xsl:element name="parentDb">
					<xsl:for-each select="/data/queries/query/querycomments/querycomment">
						<xsl:element name="form">
							<xsl:element name="id">
								<xsl:value-of select="/data/@type" />
							</xsl:element>

							<xsl:element name="iterator">
								<xsl:element name="id">
									<!--xsl:value-of select="../../id" / -->
									<xsl:value-of select="position()" />
								</xsl:element>


								<!-- loop through query header details -->
								<xsl:for-each select="../../*[not(self::querycomments)]">

									<xsl:element name="update">
										<xsl:element name="id">
											<xsl:value-of select="local-name()" />
										</xsl:element>
										<xsl:element name="value">
											<xsl:value-of select="." />
										</xsl:element>
									</xsl:element>
								</xsl:for-each>

								<!-- loop through query details -->
								<xsl:for-each select="*">
									<xsl:element name="update">
										<xsl:element name="id">
											<xsl:value-of select="'qry_'" />
											<xsl:value-of select="local-name()" />
										</xsl:element>
										<xsl:element name="value">
											<xsl:value-of select="." />
										</xsl:element>
									</xsl:element>
								</xsl:for-each>

							</xsl:element>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<!-- data_dispenseshipments -->
	<xsl:template name="data_dispenseshipments">
		<xsl:element name="country">
			<xsl:element name="iso">
				<xsl:value-of select="$COUNTRY_CODE" />
			</xsl:element>

			<!-- create a site element -->
			<xsl:element name="parent">
				<xsl:element name="id">
					<xsl:value-of select="'-1'" />
				</xsl:element>

				<xsl:element name="domain">
					<xsl:value-of select="'site'" />
				</xsl:element>

				<xsl:element name="parentDb">
					<xsl:for-each select="/data/dispenseshipments/shipment/items/item">
						<xsl:element name="form">
							<xsl:element name="id">
								<xsl:value-of select="/data/@type" />
							</xsl:element>

							<xsl:element name="iterator">
								<xsl:element name="id">
									<xsl:value-of select="position()" />
									<!--xsl:value-of select="../../id" / -->
								</xsl:element>


								<!-- loop through query header details -->
								<xsl:for-each select="../../*[not(self::items)]">

									<xsl:element name="update">
										<xsl:element name="id">
											<xsl:value-of select="local-name()" />
										</xsl:element>
										<xsl:element name="value">
											<xsl:value-of select="." />
										</xsl:element>
									</xsl:element>
								</xsl:for-each>

								<!-- loop through query details -->
								<xsl:for-each select="*">
									<xsl:element name="update">
										<xsl:element name="id">
											<xsl:value-of select="'repeat_'" />
											<xsl:value-of select="local-name()" />
										</xsl:element>
										<xsl:element name="value">
											<xsl:value-of select="." />
										</xsl:element>
									</xsl:element>
								</xsl:for-each>

							</xsl:element>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<!-- drug_data -->
	<!-- =============================================================== -->
	<xsl:template name="drug_data">
		<xsl:param name="RootPath" />
		<xsl:for-each select="$RootPath">
			<xsl:element name="country">
				<xsl:element name="iso">
					<xsl:value-of select="$COUNTRY_CODE" />
				</xsl:element>

				<!-- create a site element -->
				<xsl:element name="parent">
					<xsl:element name="id">
						<xsl:value-of select="'-1'" />
					</xsl:element>

					<xsl:element name="domain">
						<xsl:value-of select="'site'" />
					</xsl:element>

					<xsl:element name="childDb">
						<xsl:element name="form">
							<xsl:element name="id">
								<xsl:value-of select="/data/@type" />
							</xsl:element>

							<xsl:element name="iterator">
								<xsl:element name="id">
									<xsl:choose>
										<xsl:when test="/data/dispensebatches">
											<xsl:value-of select="id" />
										</xsl:when>
										<xsl:when test="/data/dispenseitems">
											<xsl:value-of select="identifier" />
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="'-1'" />
										</xsl:otherwise>
									</xsl:choose>
								</xsl:element>

								<!-- loop through each one -->
								<xsl:call-template name="item_values" />

							</xsl:element>

						</xsl:element>

						<!-- Add the domain tag for the sub domain -->
						<xsl:element name="domain">
							<xsl:value-of select="'drug'" />
						</xsl:element>

						<!-- add it's identifier -->
						<xsl:element name="id">
							<xsl:choose>
								<xsl:when test="/data/dispensebatches">
									<xsl:value-of select="id" />
								</xsl:when>
								<xsl:when test="/data/dispenseitems">
									<xsl:value-of select="identifier" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="'-1'" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:element>

					</xsl:element>

				</xsl:element>
			</xsl:element>
		</xsl:for-each>

	</xsl:template>

	<!-- template subject_data -->
	<!-- =============================================================== -->
	<xsl:template name="subject_data">
		<xsl:param name="RootPath" />
		<xsl:for-each select="$RootPath">
			<xsl:element name="country">
				<xsl:element name="iso">
					<xsl:value-of select="$COUNTRY_CODE" />
				</xsl:element>

				<!-- create a site element -->
				<xsl:element name="parent">
					<xsl:element name="id">
						<xsl:choose>
							<!-- data_subjectlist -->
							<xsl:when test="/data/subjects">
								<xsl:value-of select="sitenum" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$GLOBAL_SITE" />
							</xsl:otherwise>
						</xsl:choose>
					</xsl:element>

					<xsl:element name="domain">
						<xsl:value-of select="'site'" />
					</xsl:element>

					<xsl:element name="childDb">
						<xsl:element name="form">
							<xsl:element name="id">
								<xsl:value-of select="/data/@type" />
							</xsl:element>

							<xsl:element name="iterator">
								<xsl:element name="id">
									<xsl:choose>
										<!-- data_subjectlist -->
										<xsl:when test="/data/subjects">
											<xsl:value-of select="subid" />
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="position()" />
										</xsl:otherwise>
									</xsl:choose>
								</xsl:element>

								<!-- loop through each one -->
								<xsl:call-template name="item_values" />

							</xsl:element>

						</xsl:element>

						<!-- Add the domain tag for the sub domain -->
						<xsl:element name="domain">
							<xsl:value-of select="'subject'" />
						</xsl:element>

						<!-- add it's identifier -->
						<xsl:element name="id">
							<xsl:value-of select="subnum" />
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:element>
		</xsl:for-each>

	</xsl:template>

	<!-- item values -->
	<!-- =============================================================== -->
	<xsl:template name="item_values">
		<xsl:for-each select="*">
			<xsl:element name="update">
				<xsl:element name="id">
					<xsl:value-of select="local-name()" />
				</xsl:element>
				<xsl:element name="value">
					<xsl:value-of select="." />
				</xsl:element>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>

