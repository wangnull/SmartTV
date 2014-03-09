package com.cstzju.poker.net;

import com.cstzju.poker.utils.Common;

public class NetKeepAlive extends NetMessage {

	/**
	 * 心跳包
	 */
	public NetKeepAlive() {
		time = Common.getTime();
		cmd = GAME_PLAYING;
		len = 0;
	}

}
