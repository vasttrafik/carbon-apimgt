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

package org.wso2.carbon.apimgt.impl.dto;

import java.util.Properties;

public class ThrottleProperties {

    private boolean enabled;
    private DataPublisher dataPublisher;
    private GlobalEngineWSConnection globalEngineWSConnection;
    private DataPublisherPool dataPublisherPool;
    private JMSConnectionProperties jmsConnectionProperties;
    private boolean enableUnlimitedTier;
    private String throttleDataSourceName;
    private PolicyDeployer policyDeployer;
    private BlockCondition blockCondition;

    public BlockCondition getBlockCondition() {
        return blockCondition;
    }

    public void setBlockCondition(BlockCondition blockCondition) {
        this.blockCondition = blockCondition;
    }

    public PolicyDeployer getPolicyDeployer() {
        return policyDeployer;
    }

    public void setPolicyDeployer(PolicyDeployer policyDeployer) {
        this.policyDeployer = policyDeployer;
    }

    public boolean isEnableUnlimitedTier() {
        return enableUnlimitedTier;
    }

    public void setEnableUnlimitedTier(boolean enableUnlimitedTier) {
        this.enableUnlimitedTier = enableUnlimitedTier;
    }

    public String getThrottleDataSourceName() {
        return throttleDataSourceName;
    }

    public void setThrottleDataSourceName(String throttleDataSourceName) {
        this.throttleDataSourceName = throttleDataSourceName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public DataPublisher getDataPublisher() {
        return dataPublisher;
    }

    public void setDataPublisher(DataPublisher dataPublisher) {
        this.dataPublisher = dataPublisher;
    }

    public GlobalEngineWSConnection getGlobalEngineWSConnection() {
        return globalEngineWSConnection;
    }

    public void setGlobalEngineWSConnection(GlobalEngineWSConnection globalEngineWSConnection) {
        this.globalEngineWSConnection = globalEngineWSConnection;
    }

    public DataPublisherPool getDataPublisherPool() {
        return dataPublisherPool;
    }

    public void setDataPublisherPool(DataPublisherPool dataPublisherPool) {
        this.dataPublisherPool = dataPublisherPool;
    }

    public JMSConnectionProperties getJmsConnectionProperties() {
        return jmsConnectionProperties;
    }

    public void setJmsConnectionProperties(JMSConnectionProperties jmsConnectionProperties) {
        this.jmsConnectionProperties = jmsConnectionProperties;
    }

    public static class DataPublisher {
        private String type;
        private String receiverUrlGroup;
        private String authUrlGroup;
        private String username;
        private String password;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReceiverUrlGroup() {
            return receiverUrlGroup;
        }

        public void setReceiverUrlGroup(String receiverUrlGroup) {
            this.receiverUrlGroup = receiverUrlGroup;
        }

        public String getAuthUrlGroup() {
            return authUrlGroup;
        }

        public void setAuthUrlGroup(String authUrlGroup) {
            this.authUrlGroup = authUrlGroup;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class DataPublisherPool {
        private int maxIdle;
        private int initIdleCapacity;

        public int getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getInitIdleCapacity() {
            return initIdleCapacity;
        }

        public void setInitIdleCapacity(int initIdleCapacity) {
            this.initIdleCapacity = initIdleCapacity;
        }
    }

    public static class GlobalEngineWSConnection {
        private String dataSource;
        private boolean enabled;
        private String serviceUrl;
        private String username;
        private String password;

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }

        public String getServiceUrl() {
            return serviceUrl;
        }

        public void setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class JMSConnectionProperties {
        private String destination;
        private String serviceUrl;
        private String username;
        private String password;
        private Properties jmsConnectionProperties;
        private JMSTaskManagerProperties jmsTaskManagerProperties;

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getServiceUrl() {
            return serviceUrl;
        }

        public void setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Properties getJmsConnectionProperties() {
            return jmsConnectionProperties;
        }

        public void setJmsConnectionProperties(Properties jmsConnectionProperties) {
            this.jmsConnectionProperties = jmsConnectionProperties;
        }

        public JMSTaskManagerProperties getJmsTaskManagerProperties() {
            return jmsTaskManagerProperties;
        }

        public void setJmsTaskManagerProperties(JMSTaskManagerProperties jmsTaskManagerProperties) {
            this.jmsTaskManagerProperties = jmsTaskManagerProperties;
        }

        public static class JMSTaskManagerProperties {
            private int minThreadPoolSize, maxThreadPoolSize, keepAliveTimeInMillis, jobQueueSize;

            public int getMinThreadPoolSize() {
                return minThreadPoolSize;
            }

            public void setMinThreadPoolSize(int minThreadPoolSize) {
                this.minThreadPoolSize = minThreadPoolSize;
            }

            public int getMaxThreadPoolSize() {
                return maxThreadPoolSize;
            }

            public void setMaxThreadPoolSize(int maxThreadPoolSize) {
                this.maxThreadPoolSize = maxThreadPoolSize;
            }

            public int getKeepAliveTimeInMillis() {
                return keepAliveTimeInMillis;
            }

            public void setKeepAliveTimeInMillis(int keepAliveTimeInMillis) {
                this.keepAliveTimeInMillis = keepAliveTimeInMillis;
            }

            public int getJobQueueSize() {
                return jobQueueSize;
            }

            public void setJobQueueSize(int jobQueueSize) {
                this.jobQueueSize = jobQueueSize;
            }
        }
    }

    public static class PolicyDeployer {
        private String serviceUrl;
        private String username;
        private String password;

        public String getServiceUrl() {
            return serviceUrl;
        }

        public void setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    public static class BlockCondition {
        private String dataSource;
        private String serviceUrl;
        private String username;
        private String password;

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }

        public String getServiceUrl() {
            return serviceUrl;
        }

        public void setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
