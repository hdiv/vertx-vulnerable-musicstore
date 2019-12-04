package io.vertx.demo.musicstore.csv;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.EncodeException;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.MessageConsumer;

public class DatabaseHandler {
	private static final String CONSUMER_ID = "4f127476-41c8-4d55-97fa-6e0c98f27e74";

	private final io.vertx.reactivex.ext.jdbc.JDBCClient sqlClient;

	private final MessageConsumer<String> consumer;

	private final Vertx vertx;

	private static final String SELECT_DETAILS = "SELECT * from tracks where artist_id=";

	public static final String ADDRESS = "db_queue";

	public DatabaseHandler(final io.vertx.reactivex.ext.jdbc.JDBCClient client, final MessageConsumer<String> consumer, final Vertx vertx) {
		sqlClient = client;
		this.consumer = consumer;
		this.vertx = vertx;
	}

	public void execute() {
		consumer.handler(m -> {
			try {
				String condition = m.body();
				String sqlWithParam = SELECT_DETAILS + condition;

				sqlClient.getConnection(conn -> {
					if (conn.failed()) {
						conn.cause().printStackTrace();
						return;
					}
					else {
						io.vertx.reactivex.ext.sql.SQLConnection connection = conn.result();
						connection.queryStream(sqlWithParam, stream -> {
							if (stream.succeeded()) {
								io.vertx.reactivex.ext.sql.SQLRowStream sqlRowStream = stream.result();
								sqlRowStream.resultSetClosedHandler(v -> {
									sqlRowStream.moreResults();
								}).handler(row -> {
									// array.add(row);
									vertx.eventBus().send(CONSUMER_ID, Buffer.buffer(row.encode()));
								}).endHandler(v -> {
									sqlRowStream.close();
									connection.close();
									m.reply("done");
								});
							}
							else {
								stream.cause().printStackTrace();
								m.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "error");

							}
						});
					}
				});
			}
			catch (EncodeException ex) {
				ex.printStackTrace();
				m.fail(HttpResponseStatus.BAD_REQUEST.code(), "Failed to encode data.");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				m.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), ex.getMessage());
			}
		}).exceptionHandler(ex -> {
			ex.printStackTrace();
			throw new RuntimeException("Error while fetching records");
		});
	}
}