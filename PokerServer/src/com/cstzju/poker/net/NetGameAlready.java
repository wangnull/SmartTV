package com.cstzju.poker.net;

import com.cstzju.poker.utils.Common;

public class NetGameAlready extends NetMessage {

	/**
	 * 游戏人数大于2人，可以启动游戏，允许庄家发牌。
	 */
	public NetGameAlready() {
		time = Common.getTime();
		cmd = GAME_ALREADY;
		len = 0;
	}

}
