package com.cstzju.poker.net;

import com.cstzju.poker.utils.Common;

public class NetGameStart extends NetMessage {

	/**
	 * 庄家请求发牌。
	 */
	public NetGameStart() {
		time = Common.getTime();
		cmd = GAME_START;
		len = 0;
	}

}
