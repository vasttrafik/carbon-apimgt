/*
 *  Copyright WSO2 Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.apimgt.gateway.handlers.security.keys;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.Header;
import org.wso2.carbon.apimgt.api.model.URITemplate;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityException;
import org.wso2.carbon.apimgt.gateway.internal.ServiceReferenceHolder;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.impl.dto.APIKeyValidationInfoDTO;
import org.wso2.carbon.apimgt.keymgt.stub.validator.APIKeyValidationServiceStub;
import org.wso2.carbon.utils.CarbonUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class APIKeyValidatorClient {

	private static final int TIMEOUT_IN_MILLIS = 15 * 60 * 1000;

	private static APIKeyValidationServiceStub keyValidationServiceStub;
	private static Options options;

	private static String serviceURL;
	private static String username;
	private static String password;
	
	private static LinkedHashMap<String, Long> cookieCache = new LinkedHashMap<String, Long>(){

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            // Oldest entry of the cache will be removed when hitting max cache size of 5.
            return size() > 5;
        }

    };

	public APIKeyValidatorClient() throws APISecurityException {

		if (serviceURL == null || username == null || password == null) {

			APIManagerConfiguration config = ServiceReferenceHolder.getInstance().getAPIManagerConfiguration();
			String serviceURL = config.getFirstProperty(APIConstants.API_KEY_VALIDATOR_URL);
			username = config.getFirstProperty(APIConstants.API_KEY_VALIDATOR_USERNAME);
			password = config.getFirstProperty(APIConstants.API_KEY_VALIDATOR_PASSWORD);
			if (serviceURL == null || username == null || password == null) {
				throw new APISecurityException(APISecurityConstants.API_AUTH_GENERAL_ERROR,
						"Required connection details for the key management server not provided");
			}

		}

		if (keyValidationServiceStub == null || options == null) {

			try {
				ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null,
						null);
				keyValidationServiceStub = new APIKeyValidationServiceStub(ctx, serviceURL + "APIKeyValidationService");
				ServiceClient client = keyValidationServiceStub._getServiceClient();
				options = client.getOptions();
				options.setTimeOutInMilliSeconds(TIMEOUT_IN_MILLIS);
				options.setProperty(HTTPConstants.SO_TIMEOUT, TIMEOUT_IN_MILLIS);
				options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, TIMEOUT_IN_MILLIS);
				options.setCallTransportCleanup(true);
				options.setManageSession(true);

			} catch (AxisFault axisFault) {
				throw new APISecurityException(APISecurityConstants.API_AUTH_GENERAL_ERROR,
						"Error while initializing the API key validation stub", axisFault);
			}

		}
	}

	public APIKeyValidationInfoDTO getAPIKeyData(String context, String apiVersion, String apiKey,
			String requiredAuthenticationLevel, String clientDomain, String matchingResource, String httpVerb)
					throws APISecurityException {

		CarbonUtils.setBasicAccessSecurityHeaders(username, password, true,
				keyValidationServiceStub._getServiceClient());
		if (getCookies(cookieCache) != null) {
			keyValidationServiceStub._getServiceClient().getOptions().setProperty(HTTPConstants.COOKIE_STRING,
					getCookies(cookieCache));
		}
		try {
			List headerList = (List) keyValidationServiceStub._getServiceClient().getOptions()
					.getProperty(org.apache.axis2.transport.http.HTTPConstants.HTTP_HEADERS);
			Map headers = (Map) MessageContext.getCurrentMessageContext()
					.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
			if (headers != null) {
				headerList.add(new Header(APIConstants.ACTIVITY_ID, (String) headers.get(APIConstants.ACTIVITY_ID)));
			}
			keyValidationServiceStub._getServiceClient().getOptions()
					.setProperty(org.apache.axis2.transport.http.HTTPConstants.HTTP_HEADERS, headerList);
			/**/

			org.wso2.carbon.apimgt.impl.dto.xsd.APIKeyValidationInfoDTO dto = keyValidationServiceStub.validateKey(
					context, apiVersion, apiKey, requiredAuthenticationLevel, clientDomain, matchingResource, httpVerb);

			ServiceContext serviceContext = keyValidationServiceStub._getServiceClient().getLastOperationContext()
					.getServiceContext();
			addCookie((String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING));
			return toDTO(dto);
		} catch (Exception e) {
			throw new APISecurityException(APISecurityConstants.API_AUTH_GENERAL_ERROR,
					"Error while accessing backend services for API key validation", e);
		}
	}

	private APIKeyValidationInfoDTO toDTO(org.wso2.carbon.apimgt.impl.dto.xsd.APIKeyValidationInfoDTO generatedDto) {
		APIKeyValidationInfoDTO dto = new APIKeyValidationInfoDTO();
		dto.setSubscriber(generatedDto.getSubscriber());
		dto.setAuthorized(generatedDto.getAuthorized());
		dto.setTier(generatedDto.getTier());
		dto.setType(generatedDto.getType());
		dto.setEndUserToken(generatedDto.getEndUserToken());
		dto.setEndUserName(generatedDto.getEndUserName());
		dto.setApplicationName(generatedDto.getApplicationName());
		dto.setEndUserName(generatedDto.getEndUserName());
		dto.setConsumerKey(generatedDto.getConsumerKey());
		dto.setAuthorizedDomains(Arrays.asList(generatedDto.getAuthorizedDomains()));
		dto.setValidationStatus(generatedDto.getValidationStatus());
		dto.setApplicationId(generatedDto.getApplicationId());
		dto.setApplicationTier(generatedDto.getApplicationTier());
		dto.setApiPublisher(generatedDto.getApiPublisher());
		dto.setApiName(generatedDto.getApiName());
		dto.setValidityPeriod(generatedDto.getValidityPeriod());
		dto.setIssuedTime(generatedDto.getIssuedTime());
		dto.setScopes(
				generatedDto.getScopes() == null ? null : new HashSet<String>(Arrays.asList(generatedDto.getScopes())));
		return dto;
	}

	public ArrayList<URITemplate> getAllURITemplates(String context, String apiVersion) throws APISecurityException {

		CarbonUtils.setBasicAccessSecurityHeaders(username, password, true,
				keyValidationServiceStub._getServiceClient());
		if (getCookies(cookieCache) != null) {
			keyValidationServiceStub._getServiceClient().getOptions().setProperty(HTTPConstants.COOKIE_STRING,
					getCookies(cookieCache));
		}
		try {
			org.wso2.carbon.apimgt.api.model.xsd.URITemplate[] dto = keyValidationServiceStub
					.getAllURITemplates(context, apiVersion);
			ServiceContext serviceContext = keyValidationServiceStub._getServiceClient().getLastOperationContext()
					.getServiceContext();
			addCookie((String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING));
			ArrayList<URITemplate> templates = new ArrayList<URITemplate>();
			for (org.wso2.carbon.apimgt.api.model.xsd.URITemplate aDto : dto) {
				URITemplate temp = toTemplates(aDto);
				templates.add(temp);
			}
			return templates;
		} catch (Exception e) {
			throw new APISecurityException(APISecurityConstants.API_AUTH_GENERAL_ERROR,
					"Error while accessing backend services for API key validation", e);
		}
	}

	private URITemplate toTemplates(org.wso2.carbon.apimgt.api.model.xsd.URITemplate dto) {
		URITemplate template = new URITemplate();
		template.setAuthType(dto.getAuthType());
		template.setHTTPVerb(dto.getHTTPVerb());
		template.setResourceSandboxURI(dto.getResourceSandboxURI());
		template.setUriTemplate(dto.getUriTemplate());
		template.setThrottlingTier(dto.getThrottlingTier());
		return template;
	}
	
	/**
    *
    * @param cookieCache
    * @return
    */
   public static String getCookies(LinkedHashMap<String, Long> cookieCache){

       String cookieString = null;

       if(cookieCache != null){
           for(Map.Entry<String, Long> entry : cookieCache.entrySet()){
               String key = entry.getKey();
               if(key != null){
                   if(cookieString != null){
                       cookieString = cookieString + "," +  key;
                   } else {
                       cookieString = key;
                   }
               }
               
           }
       }
       return cookieString;
   }

   /**
   *
   * @param cookie
   * @return
   */
   public static void addCookie(String cookie) {
	   
	   if(cookie != null && !cookie.equals("") && !cookieCache.containsKey(cookie))
		   cookieCache.put(cookie, System.currentTimeMillis());
	   
   }
}
