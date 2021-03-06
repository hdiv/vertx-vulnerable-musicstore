/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.demo.musicstore.xpath;

import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * Simulate login process, where data is in xml file
 */
public class XPathHandler implements Handler<RoutingContext> {

	public XPathHandler() {

	}

	@Override
	public void handle(final RoutingContext routingContext) {

		String user = routingContext.request().getParam("user");
		String pass = routingContext.request().getParam("pass");
		XPathTest test = new XPathTest();

		routingContext.vertx().executeBlocking(future -> {
			future.complete(test.doLogin(user, pass));
		}, res -> {
			routingContext.response().putHeader("content-type", "application/json").setStatusCode(200)
					.end(Json.encodePrettily(res.result()));
		});

	}

}
