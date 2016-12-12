/*
 * VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

*/
package com.vmware.samltoolkit;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.eucenablement.saml.api.IdpLogoutMetadata;
import com.vmware.eucenablement.saml.api.IdpMainMetadata;
import com.vmware.eucenablement.saml.api.IdpSsoMetadata;
import com.vmware.eucenablement.saml.service.IdpDiscoveryService;
import com.vmware.eucenablement.saml.service.SAMLSsoService;

public class SsoServiceFacade {

	private SAMLSsoService _service;

	public String getVIDMURL() {
		return this.config.getIdpURL();
	}

	public String getConsumerURL() {
		return this.config.getConsumerURL();
	}

	public SAMLToolkitConf getSAMLToolkitConf() {
		return this.config;
	}

	private SAMLToolkitConf config;

	private IdpDiscoveryService idpService;

	/**
	 *
	 * @param conf
	 * @throws Exception
	 */
	public SsoServiceFacade(SAMLToolkitConf conf) throws Exception {
		this.config = conf;
		if (config == null || !config.isReady()) {
			throw new Exception("Config not correct!!!");
		}

		if (config.shouldRequestVIDMMetaData()) {
			idpService = new IdpDiscoveryService(config.getIdpURL(), config.isByPassSSLCertValidation());

			IdpMainMetadata metadata = idpService.getMainMetadata();
			List<IdpSsoMetadata> ssodataList = metadata.getSsoMetadata();
			for (IdpSsoMetadata d : ssodataList) {
				config.setLoginBinding(d.getBinding(), d.getLocation());
			}

			List<IdpLogoutMetadata> logoutList = metadata.getLogoutMetadata();
			for (IdpLogoutMetadata d : logoutList) {
				config.setLogoutBinding(d.getBinding(), d.getLocation());
			}

			config.setCertificate(metadata.getSigningKey());

		}

		this._service = new SAMLSsoService(config);

	}

	/**
	 *
	 * @param relayState
	 * @return the URL for SSO with vIDM
	 * @throws Exception
	 */
	public String getSSOURLRedirect(String relayState) throws Exception {
		return _service.getSAMLRequestURLRedirect(relayState);
	}

	/**
	 *
	 * @param relayState
	 * @return the html content for SSO with vIDM
	 * @throws Exception
	 */
	public String getSSOHtmlPost(String relayState) throws Exception {
		return _service.getSSOHtmlPost(relayState);
	}

	/**
	 *
	 * @param response
	 *            the object of SamlSsoResponse,
	 * @see decodeSSOURLRedirect
	 * @return String the URL for logout
	 * @throws Exception
	 */
	public String getLogoutURLRedirect() throws Exception {
		return _service.getSAMLLogoutURLRedirect();
	}

	private static Logger log = LoggerFactory.getLogger(SsoServiceFacade.class);

	/**
	 *
	 * @param encodedSAMLResponse,
	 *            you can get this parameter from
	 *            request.getParameter("SAMLResponse");
	 * @return SAMLSsoResponse with username if sso is successful
	 * @throws Exception
	 */
	public SAMLSsoResponse decodeSSOResponse(String encodedSAMLResponse) throws Exception {
		log.info("Receive SAML Response from request");
		// String s = request.getParameter("SAMLResponse");
		return _service.decodeSAMLResponse(encodedSAMLResponse);
	}

}