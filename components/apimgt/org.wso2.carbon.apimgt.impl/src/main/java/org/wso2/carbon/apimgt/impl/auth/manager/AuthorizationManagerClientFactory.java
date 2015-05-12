/**
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.apimgt.impl.auth.manager;

import org.wso2.carbon.apimgt.api.APIManagementException;

public abstract class AuthorizationManagerClientFactory {

    public enum ClientType {
        REMOTE, STANDALONE
    }

    public static AuthorizationManagerClientFactory getAuthorizationManagerClientFactory(ClientType clientType) {
        switch (clientType) {
            case REMOTE:
                return new RemoteAuthorizationClientFactory();
            case STANDALONE:
                return new StandaloneAuthorizationClientFactory();
            default:
                throw new IllegalArgumentException("Invalid Authorization Manager Client Type found");
        }
    }

    public abstract AuthorizationManagerClient getAuthorizationManagerClient() throws APIManagementException;

    public abstract void releaseAuthorizationManagerClient(
            AuthorizationManagerClient client) throws APIManagementException;

}