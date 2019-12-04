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

package io.vertx.demo.musicstore.pathtraversal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.RoutingContext;

public class PathTraversalHandler implements Handler<RoutingContext> {

	public PathTraversalHandler() {

	}

	@Override
	public void handle(final RoutingContext routingContext) {
		String fileName = routingContext.request().getParam("filename");

		routingContext.vertx().executeBlocking(future -> {
			File file = new File(fileName);
			if (file.exists()) {
				try {
					future.complete(new String(Files.readAllBytes(Paths.get(fileName))));
				}
				catch (IOException e) {
					future.complete(fileName + ":Error read ");
				}
			}
			else {
				future.complete(fileName + " not found");
			}

		}, res -> {
			routingContext.response().putHeader("content-type", "application/json").setStatusCode(200)
					.end(Json.encodePrettily(res.result()));
		});

	}

}
