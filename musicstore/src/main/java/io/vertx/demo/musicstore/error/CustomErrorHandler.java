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

package io.vertx.demo.musicstore.error;

import io.vertx.core.Handler;
import io.vertx.demo.musicstore.Article;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * @author Thomas Segismont
 */
public class CustomErrorHandler implements Handler<RoutingContext> {

	public CustomErrorHandler() {

	}

	@Override
	public void handle(final RoutingContext routingContext) {
		Article article = new Article(12, "This is an intro to vertx");
		throw new RuntimeException("toma un error");

		// routingContext.response().putHeader("content-type", "application/json").setStatusCode(200).end(Json.encodePrettily(article));

	}

}
