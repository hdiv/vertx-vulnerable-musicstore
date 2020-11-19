package io.vertx.demo.musicstore.csv;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.core.streams.Pump;
import io.vertx.reactivex.core.streams.ReadStream;
import io.vertx.reactivex.ext.web.RoutingContext;

public class CSVDownloadHandler implements Handler<RoutingContext> {
	private static final String CONSUMER_ID = "4f127476-41c8-4d55-97fa-6e0c98f27e74";

	private final Vertx vertx;

	public CSVDownloadHandler(final Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public void handle(final RoutingContext event) {
		DeliveryOptions options = new DeliveryOptions();
		HttpServerResponse response = event.response();
		response.putHeader(HttpHeaders.CONTENT_TYPE, "application/csv")
				.putHeader("Content-Disposition", "attachment; filename=user_details.csv")
				.putHeader(HttpHeaders.TRANSFER_ENCODING, "chunked").setChunked(true);

		String artistId = event.request().getParam("artistId");

		ReadStream<Buffer> consumer = vertx.eventBus().<Buffer> consumer(CONSUMER_ID).bodyStream();
		consumer.handler(result -> {
			JsonArray array = result.toJsonArray();
			response.write(toString(array), "UTF-8");
		});

		Pump pump = Pump.pump(consumer, response);
		vertx.eventBus().send(DatabaseHandler.ADDRESS, artistId, options, result -> {
			if (result.succeeded()) {
				response.end();
				pump.stop();
			}
			else {
				// TODO handle error ????
				response.setStatusCode(500);
				response.end();
				pump.stop();
			}
		});
		pump.start();
	}

	public String toString(final JsonArray data) {
		return String.join(",", "" + data.getValue(0), "" + data.getValue(1), "" + data.getValue(2), "" + data.getValue(3),
				"" + data.getValue(4), "" + data.getValue(5), "\r\n");
	}
}