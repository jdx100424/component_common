package com.maoshen.component.base.dto;

public class RequestHeaderDtoHolder {
	private static ThreadLocal<RequestHeaderDto> holder = new ThreadLocal<>();

	public static void set(RequestHeaderDto dto) {
		holder.set(dto);
	}

	public static void clear() {
		holder.remove();
	}

	public static RequestHeaderDto get() {
		RequestHeaderDto header = holder.get();
		if (header == null) {
			header = new RequestHeaderDto();
			holder.set(header);
		}
		return header;
	}
}
