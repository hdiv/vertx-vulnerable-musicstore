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

import java.io.PrintWriter;
import java.io.StringWriter;

import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.demo.musicstore.model.Article;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * Example error stack in json object. Http status 200 It could be used as test of stack trace leak
 */
public class CustomErrorHandler implements Handler<RoutingContext> {

	public CustomErrorHandler() {

	}

	@Override
	public void handle(final RoutingContext ctx) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		new Exception().printStackTrace(pw);
		Article article = new Article(12, sw.toString());
		ctx.response().putHeader("content-type", "application/json").setStatusCode(200).end(Json.encodePrettily(article));
	}

}
