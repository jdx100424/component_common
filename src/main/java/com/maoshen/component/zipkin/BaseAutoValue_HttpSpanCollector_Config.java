package com.maoshen.component.zipkin;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
public class BaseAutoValue_HttpSpanCollector_Config extends BaseHttpSpanCollector.Config {

	private final int connectTimeout;
	private final int readTimeout;
	private final int flushInterval;
	private final boolean compressionEnabled;

	private BaseAutoValue_HttpSpanCollector_Config(
	      int connectTimeout,
	      int readTimeout,
	      int flushInterval,
	      boolean compressionEnabled) {
	    this.connectTimeout = connectTimeout;
	    this.readTimeout = readTimeout;
	    this.flushInterval = flushInterval;
	    this.compressionEnabled = compressionEnabled;
	  }

	@Override
	int connectTimeout() {
		return connectTimeout;
	}

	@Override
	int readTimeout() {
		return readTimeout;
	}

	@Override
	int flushInterval() {
		return flushInterval;
	}

	@Override
	boolean compressionEnabled() {
		return compressionEnabled;
	}

	@Override
	public String toString() {
		return "Config{" + "connectTimeout=" + connectTimeout + ", " + "readTimeout=" + readTimeout + ", "
				+ "flushInterval=" + flushInterval + ", " + "compressionEnabled=" + compressionEnabled + "}";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof BaseHttpSpanCollector.Config) {
			BaseHttpSpanCollector.Config that = (BaseHttpSpanCollector.Config) o;
			return (this.connectTimeout == that.connectTimeout()) && (this.readTimeout == that.readTimeout())
					&& (this.flushInterval == that.flushInterval())
					&& (this.compressionEnabled == that.compressionEnabled());
		}
		return false;
	}

	@Override
	public int hashCode() {
		int h = 1;
		h *= 1000003;
		h ^= connectTimeout;
		h *= 1000003;
		h ^= readTimeout;
		h *= 1000003;
		h ^= flushInterval;
		h *= 1000003;
		h ^= compressionEnabled ? 1231 : 1237;
		return h;
	}

	static final class Builder implements BaseHttpSpanCollector.Config.Builder {
		private Integer connectTimeout;
		private Integer readTimeout;
		private Integer flushInterval;
		private Boolean compressionEnabled;

		Builder() {
		}

		Builder(BaseHttpSpanCollector.Config source) {
			this.connectTimeout = source.connectTimeout();
			this.readTimeout = source.readTimeout();
			this.flushInterval = source.flushInterval();
			this.compressionEnabled = source.compressionEnabled();
		}

		@Override
		public BaseHttpSpanCollector.Config.Builder connectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
			return this;
		}

		@Override
		public BaseHttpSpanCollector.Config.Builder readTimeout(int readTimeout) {
			this.readTimeout = readTimeout;
			return this;
		}

		@Override
		public BaseHttpSpanCollector.Config.Builder flushInterval(int flushInterval) {
			this.flushInterval = flushInterval;
			return this;
		}

		@Override
		public BaseHttpSpanCollector.Config.Builder compressionEnabled(boolean compressionEnabled) {
			this.compressionEnabled = compressionEnabled;
			return this;
		}

		@Override
		public BaseHttpSpanCollector.Config build() {
			String missing = "";
			if (connectTimeout == null) {
				missing += " connectTimeout";
			}
			if (readTimeout == null) {
				missing += " readTimeout";
			}
			if (flushInterval == null) {
				missing += " flushInterval";
			}
			if (compressionEnabled == null) {
				missing += " compressionEnabled";
			}
			if (!missing.isEmpty()) {
				throw new IllegalStateException("Missing required properties:" + missing);
			}
			return new BaseAutoValue_HttpSpanCollector_Config(this.connectTimeout, this.readTimeout, this.flushInterval,
					this.compressionEnabled);
		}
	}
}
