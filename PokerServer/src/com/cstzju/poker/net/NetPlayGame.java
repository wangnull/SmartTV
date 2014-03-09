package com.cstzju.poker.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetPlayGame extends NetMessage {

	/**
	 * 玩游戏。发起游戏或者加入现有队伍。
	 */
	public NetPlayGame() {
		time = Common.getTime();
		cmd = HAVE_GROUP_RESULT;
	}

	/**
	 * public void setData(int status)
	 * 
	 * @param status
	 *            0发起游戏，1加入现有游戏队伍
	 * @exception Exception
	 */
	public void setData(int status) throws Exception {
		// JSON Data Example: {"status": "0"}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", status);
		len = jsonObject.toString().length();
		data = new byte[len];
		data = jsonObject.toString().getBytes("UTF-8");

	}

	/**
	 * public int getIntData() 获取有效数据
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws JSONException
	 */
	public int getIntData() throws NumberFormatException, JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
		return Integer.parseInt(jsonObject.getString("status"));
	}

}
