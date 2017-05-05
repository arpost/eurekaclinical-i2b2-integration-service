package org.eurekaclinical.i2b2.props;

/*
 * #%L
 * i2b2 Eureka Service
 * %%
 * Copyright (C) 2015 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.inject.Singleton;
import org.eurekaclinical.standardapis.props.CasEurekaClinicalProperties;

/**
 *
 * @author Andrew Post
 */
@Singleton
public class I2b2EurekaServicesProperties extends CasEurekaClinicalProperties {

	public I2b2EurekaServicesProperties() {
		super("/etc/ec-i2b2-integration");
	}
	
	@Override
	public String getProxyCallbackServer() {
		return this.getValue("eurekaclinical.i2b2service.callbackserver", "https://localhost:8443");
	}
	
	public String getEurekaServiceUrl() {
		return this.getValue("eureka.services.url", "https://localhost/eureka-services");
	}
	
	public String getSourceConfigId() {
		return this.getValue("eurekaclinical.i2b2service.sourceConfigId", "i2b2 Eureka Service");
	}
	
	public String getUserAgreementServiceUrl() {
		return this.getValue("eurekaclinical.useragreementservice.url");
	}
	
	public String getCasEmailAttribute() {
		return this.getValue("cas.attribute.email");
	}

	@Override
	public String getUrl() {
		return this.getValue("eurekaclinical.i2b2integrationservice.url", "https://localhost:8443/i2b2-eureka-service");
	}
	
}
