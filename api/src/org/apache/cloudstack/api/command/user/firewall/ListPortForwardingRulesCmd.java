// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package org.apache.cloudstack.api.command.user.firewall;

import java.util.ArrayList;
import java.util.List;

import org.apache.cloudstack.api.response.IPAddressResponse;
import org.apache.log4j.Logger;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListTaggedResourcesCmd;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.response.FirewallRuleResponse;
import org.apache.cloudstack.api.response.ListResponse;
import com.cloud.network.rules.PortForwardingRule;
import com.cloud.utils.Pair;

@Implementation(description="Lists all port forwarding rules for an IP address.", responseObject=FirewallRuleResponse.class)
public class ListPortForwardingRulesCmd extends BaseListTaggedResourcesCmd {
    public static final Logger s_logger = Logger.getLogger(ListPortForwardingRulesCmd.class.getName());

    private static final String s_name = "listportforwardingrulesresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType = FirewallRuleResponse.class,
            description="Lists rule with the specified ID.")
    private Long id;

    @Parameter(name=ApiConstants.IP_ADDRESS_ID, type=CommandType.UUID, entityType = IPAddressResponse.class,
            description="the id of IP address of the port forwarding services")
    private Long ipAddressId;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getIpAddressId() {
        return ipAddressId;
    }

    public Long getId() {
        return id;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public void execute(){
        Pair<List<? extends PortForwardingRule>, Integer> result = _rulesService.listPortForwardingRules(this);
        ListResponse<FirewallRuleResponse> response = new ListResponse<FirewallRuleResponse>();
        List<FirewallRuleResponse> fwResponses = new ArrayList<FirewallRuleResponse>();

        for (PortForwardingRule fwRule : result.first()) {
            FirewallRuleResponse ruleData = _responseGenerator.createPortForwardingRuleResponse(fwRule);
            ruleData.setObjectName("portforwardingrule");
            fwResponses.add(ruleData);
        }
        response.setResponses(fwResponses, result.second());
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }
}
