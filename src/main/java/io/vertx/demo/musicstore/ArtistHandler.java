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
import java.util.List;
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
public class ArtistHandler implements Handler<RoutingContext> {
	private final JDBCClient dbClient;

	private final String findArtistById;

	private final String findAlbumsByArtist;

	private final FreeMarkerTemplateEngine templateEngine;

	public ArtistHandler(final JDBCClient dbClient, final Properties sqlQueries, final FreeMarkerTemplateEngine templateEngine) {
		this.dbClient = dbClient;
		findArtistById = sqlQueries.getProperty("findArtistById");
		findAlbumsByArtist = sqlQueries.getProperty("findAlbumsByArtist");
		this.templateEngine = templateEngine;
	}

	@Override
	public void handle(final RoutingContext rc) {
		Long artistId = PathUtil.parseLongParam(rc.pathParam("artistId"));

		if (artistId == null) {
			rc.next();
			return;
		}
		String strArtistId = null;
		List<String> artists = rc.queryParam("hack");
		if (artists == null || artists.isEmpty()) {
			strArtistId = artistId.toString();
		}
		else {
			strArtistId = artists.get(0);
		}
		final String searchArtist = strArtistId;

		dbClient.rxGetConnection().flatMap(sqlConnection -> {
			Single<JsonObject> ars = findArtist(sqlConnection, searchArtist);
			Single<JsonArray> als = findAlbums(sqlConnection, artistId);

			return Single.zip(ars, als, (artist, albums) -> {
				Map<String, Object> data = new HashMap<>(2);
				data.put("artist", artist);
				data.put("albums", albums);
				return data;
			}).doAfterTerminate(sqlConnection::close);

		}).flatMap(data -> {
			data.forEach(rc::put);
			return templateEngine.rxRender(rc.data(), "templates/artist");
		}).subscribe(rc.response()::end, rc::fail);
	}

	private Single<JsonObject> findArtist(final SQLConnection sqlConnection, final String artistId) {

		return sqlConnection.rxQueryStream("SELECT a.name FROM artists a WHERE a.id = " + artistId)
				.flatMapObservable(SQLRowStream::toObservable).map(row -> new JsonObject().put("id", 1l).put("name", row.getString(0)))
				.singleOrError();

	}

	private Single<JsonArray> findAlbums(final SQLConnection sqlConnection, final Long artistId) {
		return sqlConnection.rxQueryStreamWithParams(findAlbumsByArtist, new JsonArray().add(artistId))
				.flatMapObservable(SQLRowStream::toObservable)
				.map(row -> new JsonObject().put("id", row.getLong(0)).put("title", row.getString(1)))
				.collect(JsonArray::new, JsonArray::add);
	}
}
