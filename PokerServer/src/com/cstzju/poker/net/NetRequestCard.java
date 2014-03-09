package com.cstzju.poker.net;

import com.cstzju.poker.utils.Common;

public class NetRequestCard extends NetMessage {

	/**
	 * 请求某客户端出牌
	 */
	public NetRequestCard() {
		time = Common.getTime();
		cmd = REGUEST_CARD;
		len = 0;
	}

}
