package com.maoshen.component.zipkin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.DefaultManagedAwareThreadFactory;

import com.github.kristofa.brave.AbstractSpanCollector;
import com.github.kristofa.brave.EmptySpanCollectorMetricsHandler;
import com.github.kristofa.brave.SpanCollectorMetricsHandler;
import com.google.auto.value.AutoValue;
import com.twitter.zipkin.gen.SpanCodec;

public final class BaseHttpSpanCollector extends AbstractSpanCollector {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseHttpSpanCollector.class);
	
	protected ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 50, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(), //
			new DefaultManagedAwareThreadFactory(),new MyRejectedExecutionHandler()) {
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
		}
	};;
	@AutoValue
	public static abstract class Config {
		public static Builder builder() {
			return new BaseAutoValue_HttpSpanCollector_Config.Builder().connectTimeout(10 * 1000).readTimeout(60 * 1000)
					.compressionEnabled(false).flushInterval(1);
		}

		abstract int connectTimeout();

		abstract int readTimeout();

		abstract int flushInterval();

		abstract boolean compressionEnabled();

		@AutoValue.Builder
		public interface Builder {
			/** Default 10 * 1000 milliseconds. 0 implies no timeout. */
			Builder connectTimeout(int connectTimeout);

			/** Default 60 * 1000 milliseconds. 0 implies no timeout. */
			Builder readTimeout(int readTimeout);

			/**
			 * Default 1 second. 0 implies spans are {@link #flush() flushed}
			 * externally.
			 */
			Builder flushInterval(int flushInterval);

			/**
			 * Default false. true implies that spans will be gzipped before
			 * transport.
			 *
			 * <p>
			 * Note: This feature requires zipkin-scala 1.34+ or zipkin-java
			 * 0.6+
			 */
			Builder compressionEnabled(boolean compressSpans);

			Config build();
		}
	}

	private final String url;
	private final Config config;

	/**
	 * Create a new instance with default configuration.
	 *
	 * @param baseUrl
	 *            URL of the zipkin query server instance. Like:
	 *            http://localhost:9411/
	 * @param metrics
	 *            Gets notified when spans are accepted or dropped. If you are
	 *            not interested in these events you can use
	 *            {@linkplain EmptySpanCollectorMetricsHandler}
	 */
	public static BaseHttpSpanCollector create(String baseUrl, SpanCollectorMetricsHandler metrics) {
		return new BaseHttpSpanCollector(baseUrl, Config.builder().build(), metrics);
	}

	/**
	 * @param baseUrl
	 *            URL of the zipkin query server instance. Like:
	 *            http://localhost:9411/
	 * @param config
	 *            includes flush interval and timeouts
	 * @param metrics
	 *            Gets notified when spans are accepted or dropped. If you are
	 *            not interested in these events you can use
	 *            {@linkplain EmptySpanCollectorMetricsHandler}
	 */
	public static BaseHttpSpanCollector create(String baseUrl, Config config, SpanCollectorMetricsHandler metrics) {
		return new BaseHttpSpanCollector(baseUrl, config, metrics);
	}

	// Visible for testing. Ex when tests need to explicitly control flushing,
	// set interval to 0.
	BaseHttpSpanCollector(String baseUrl, Config config, SpanCollectorMetricsHandler metrics) {
		super(SpanCodec.JSON, metrics, config.flushInterval());
		this.url = baseUrl + (baseUrl.endsWith("/") ? "" : "/") + "api/v1/spans";
		this.config = config;
	}

	@Override
	protected void sendSpans(byte[] json) throws IOException {
		executor.submit(new asyncTask(json));
	}
	
	protected class MyRejectedExecutionHandler implements RejectedExecutionHandler {
	    @Override
	    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
            if (!executor.isShutdown()) {
            	task.run();
            }
	    }
	}
	

	private class asyncTask implements Runnable {
		private byte[] json;
		public asyncTask(byte[] json) {
			this.json = json;
		}
		
		@Override
		public void run() {
			try{
				HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
				connection.setConnectTimeout(config.connectTimeout());
				connection.setReadTimeout(config.readTimeout());
				connection.setRequestMethod("POST");
				connection.addRequestProperty("Content-Type", "application/json");
				if (config.compressionEnabled()) {
					connection.addRequestProperty("Content-Encoding", "gzip");
					ByteArrayOutputStream gzipped = new ByteArrayOutputStream();
					try (GZIPOutputStream compressor = new GZIPOutputStream(gzipped)) {
						compressor.write(json);
					}
					json = gzipped.toByteArray();
				}
				connection.setDoOutput(true);
				connection.setFixedLengthStreamingMode(json.length);
				connection.getOutputStream().write(json);

				try (InputStream in = connection.getInputStream()) {
					while (in.read() != -1)
						; // skip
				} catch (IOException e) {
					try (InputStream err = connection.getErrorStream()) {
						if (err != null) { // possible, if the connection was dropped
							while (err.read() != -1)
								; // skip
						}
					}
					throw e;
				}
			}catch(Exception e){
				LOGGER.error(e.getMessage(),e);
			}
		}
	}
}
