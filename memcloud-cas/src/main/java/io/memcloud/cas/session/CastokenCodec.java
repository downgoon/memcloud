package io.memcloud.cas.session;

import com.github.downgoon.jresty.commons.security.HexCodec;
import com.github.downgoon.jresty.commons.security.Md5Codec;

public class CastokenCodec {

	public static String encode(String plain) {
		return HexCodec.b2HEX(Md5Codec.encode(plain.toString().getBytes())).substring(16, 24);
	}
}
