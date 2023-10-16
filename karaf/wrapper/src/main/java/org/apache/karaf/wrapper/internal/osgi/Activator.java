/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.wrapper.internal.osgi;

import org.apache.karaf.util.tracker.BaseActivator;
import org.apache.karaf.util.tracker.annotation.ProvideService;
import org.apache.karaf.util.tracker.annotation.Services;
import org.apache.karaf.wrapper.WrapperService;
import org.apache.karaf.wrapper.internal.WrapperServiceImpl;
import org.apache.karaf.wrapper.management.internal.WrapperMBeanImpl;

@Services(provides = @ProvideService(WrapperService.class))
public class Activator extends BaseActivator {

    @Override
    protected void doStart() throws Exception {
        WrapperService wrapperService = new WrapperServiceImpl();
        register(WrapperService.class, wrapperService);

        WrapperMBeanImpl wrapperMBean = new WrapperMBeanImpl();
        wrapperMBean.setWrapperService(wrapperService);
        registerMBean(wrapperMBean, "type=wrapper");
    }

}
