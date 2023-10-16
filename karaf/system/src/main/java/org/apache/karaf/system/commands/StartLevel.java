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
package org.apache.karaf.system.commands;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.system.SystemService;

/**
 * Get/set the system start level.
 */
@Command(scope = "system", name = "start-level", description = "Gets or sets the system start level.")
@Service
public class StartLevel implements Action {

    @Argument(index = 0, name = "level", description = "The new system start level to set", required = false, multiValued = false)
    Integer level;

    @Reference
    SystemService systemService;

    @Override
    public Object execute() throws Exception {
        if (level == null) {
            System.out.println("Level " + systemService.getStartLevel());
        } else {
            systemService.setStartLevel(level);
        }
        return null;
    }

}
