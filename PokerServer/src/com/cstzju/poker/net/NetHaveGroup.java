package com.cstzju.poker.net;

import com.cstzju.poker.utils.Common;

public class NetHaveGroup extends NetMessage {

	public NetHaveGroup() {
		time = Common.getTime();
		cmd = HAVE_GROUP;
		len = 0;
	}

}
