package io.vertx.demo.musicstore.serialize;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.demo.musicstore.model.FileUploadStatus;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.FileUpload;
import io.vertx.reactivex.ext.web.RoutingContext;

public class DeserializeUploadAsyncHandler implements Handler<RoutingContext> {

	private static final String FILE_UPLOAD_LOCATION = System.getProperty("java.io.tmpdir");

	public DeserializeUploadAsyncHandler() {

	}

	@Override
	public void handle(final RoutingContext ctx) {
		List<Future> list = new ArrayList<>();
		for (FileUpload f : ctx.fileUploads()) {
			Buffer uploadedFile = ctx.vertx().fileSystem().readFileBlocking(f.uploadedFileName());
			try (FileOutputStream fos = new FileOutputStream(FILE_UPLOAD_LOCATION + f.fileName())) {
				fos.write(uploadedFile.getBytes());
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			list.add(deserialize(ctx, FILE_UPLOAD_LOCATION + f.fileName()));

		}
		CompositeFuture.all(list).setHandler((r) -> {
			if (r.succeeded()) {
				// https://github.com/cescoffier/vertx-microservices-examples/blob/master/aggregation-http/A/src/main/java/io/vertx/microservices/A.java
				ctx.response().putHeader("content-type", "application/json").setStatusCode(200).end(Json.encodePrettily(r.result().list()));
			}
		});

	}

	// Return a future, then fulfill it after some time
	private static Future<FileUploadStatus> deserialize(final RoutingContext ctx, final String fileName) {

		Promise<FileUploadStatus> promise = Promise.promise();
		ctx.vertx().setTimer(1000, id -> {
			XStreamTest test = new XStreamTest();

			System.out.println("Deserialization XStream...");
			try {
				test.doSerializationXStream(fileName);
				promise.complete(new FileUploadStatus(fileName, "OK"));
			}
			catch (com.thoughtworks.xstream.io.StreamException e) {
				promise.complete(new FileUploadStatus(fileName, "ERROR"));
			}
			System.out.println("Deserialization ...finish");
		});
		return promise.future();

	}

}
