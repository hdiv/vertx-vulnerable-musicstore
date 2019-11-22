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

package io.vertx.demo.musicstore;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import io.reactivex.Single;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.sql.SQLConnection;
import io.vertx.reactivex.ext.sql.SQLRowStream;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

/**
 * @author Thomas Segismont
 */
public class AlbumHandler implements Handler<RoutingContext> {
	private final JDBCClient dbClient;

	private final String findAlbumById;

	private final String findTracksByAlbum;

	private final FreeMarkerTemplateEngine templateEngine;

	public AlbumHandler(final JDBCClient dbClient, final Properties sqlQueries, final FreeMarkerTemplateEngine templateEngine) {
		this.dbClient = dbClient;
		findAlbumById = sqlQueries.getProperty("findAlbumById");
		findTracksByAlbum = sqlQueries.getProperty("findTracksByAlbum");
		this.templateEngine = templateEngine;
	}

	@Override
	public void handle(final RoutingContext rc) {
		Long albumId = PathUtil.parseLongParam(rc.pathParam("albumId"));
		if (albumId == null) {
			rc.next();
			return;
		}

		dbClient.rxGetConnection().flatMap(sqlConnection -> {
			Single<JsonObject> as = findAlbum(sqlConnection, albumId);
			Single<JsonArray> ts = findTracks(sqlConnection, albumId);

			Single<Map<String, Object>> templateData = Single.zip(as, ts, (album, tracks) -> {
				Map<String, Object> data = new HashMap<>(2);
				data.put("album", album);
				data.put("tracks", tracks);
				return data;
			});
			return templateData.doAfterTerminate(sqlConnection::close);

		}).flatMap(data -> {
			data.forEach(rc::put);
			return templateEngine.rxRender(rc.data(), "templates/album");
		}).subscribe(rc.response()::end, rc::fail);
	}

	private Single<JsonObject> findAlbum(final SQLConnection sqlConnection, final Long albumId) {
		return sqlConnection.rxQueryStreamWithParams(findAlbumById, new JsonArray().add(albumId)).flatMapPublisher(SQLRowStream::toFlowable)
				.map(row -> new JsonObject().put("id", albumId).put("title", row.getString(0))).singleOrError();
	}

	private Single<JsonArray> findTracks(final SQLConnection sqlConnection, final Long albumId) {
		return sqlConnection.rxQueryStreamWithParams(findTracksByAlbum, new JsonArray().add(albumId))
				.flatMapObservable(SQLRowStream::toObservable).map(row -> {
					return new JsonObject().put("id", row.getLong(0)).put("track_number", row.getInteger(1)).put("title", row.getString(2))
							.put("mb_track_id", row.getString(3))
							.put("artist", new JsonObject().put("id", row.getLong(4)).put("name", row.getString(5)));
				}).collect(JsonArray::new, JsonArray::add);
	}
}
