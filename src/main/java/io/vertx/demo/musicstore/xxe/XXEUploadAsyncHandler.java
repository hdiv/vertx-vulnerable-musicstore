package io.vertx.demo.musicstore.xxe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.demo.musicstore.model.Foo;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.FileUpload;
import io.vertx.reactivex.ext.web.RoutingContext;

public class XXEUploadAsyncHandler implements Handler<RoutingContext> {

	private static final String FILE_UPLOAD_LOCATION = System.getProperty("java.io.tmpdir");

	private static final String MAPPER_PARAM_NAME = "mapper";

	private static final String MAPPER_PARAM_CUSTOM = "custom";

	private static final String MAPPER_PARAM_JACKSON = "jackson";

	public XXEUploadAsyncHandler() {

	}

	@Override
	public void handle(final RoutingContext ctx) {
		boolean customMapper = true;
		String mapper = ctx.request().getParam(MAPPER_PARAM_NAME);
		if (mapper != null && mapper.equals(MAPPER_PARAM_JACKSON)) {
			customMapper = false;
		}
		List<Future> list = new ArrayList<>();
		for (FileUpload f : ctx.fileUploads()) {
			Buffer uploadedFile = ctx.vertx().fileSystem().readFileBlocking(f.uploadedFileName());
			try (FileOutputStream fos = new FileOutputStream(FILE_UPLOAD_LOCATION + f.fileName())) {
				fos.write(uploadedFile.getBytes());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			list.add(mapper(ctx, FILE_UPLOAD_LOCATION + f.fileName(), customMapper));
		}

		CompositeFuture.all(list).setHandler((r) -> {
			if (r.succeeded()) {
				ctx.response().putHeader("content-type", "application/json").setStatusCode(200).end(Json.encodePrettily(r.result().list()));
			}
		});

	}

	// Return a future, then fulfill it after some time
	private static Future<Foo> mapper(final RoutingContext ctx, final String fileName, final boolean customMapping) {
		System.out.println("Xml mapper using custom mapper? " + customMapping);
		Promise<Foo> promise = Promise.promise();
		ctx.vertx().setTimer(1000, id -> {
			System.out.println("XML...");
			try {
				if (customMapping) {
					promise.complete(MusicCatalogXMLMapper.customMapper(new File(fileName)));
				}
				else {
					promise.complete(MusicCatalogXMLMapper.mapperFooJackson(new File(fileName)));
				}
			}
			catch (Exception e) {
				promise.complete(new Foo("Error " + fileName));
			}
			System.out.println("XML ...finish");
		});
		return promise.future();

	}

}
