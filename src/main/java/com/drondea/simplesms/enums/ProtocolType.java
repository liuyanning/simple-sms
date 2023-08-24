package com.drondea.simplesms.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 通讯协议
 * 
 * @author volcano
 * @date 2019年9月12日下午11:16:40
 * @version V1.0
 */
public enum ProtocolType {
	HTTP,

	CMPP2,

	CMPP3,

	SMPP,

	HTTP_XML,

	HTTP_JSON,

	SYSTEM,

	CMPP,

	SGIP,

	SMGP,

	WEB;
	private String value;

	ProtocolType() {
		this.value = this.name().toLowerCase();
	}

	@Override
	public String toString() {
		return value;
	}

	public boolean equals(String value) {
		if (StringUtils.isEmpty(value)) {
			return false;
		}
		return this.value.equalsIgnoreCase(value);
	}
}
