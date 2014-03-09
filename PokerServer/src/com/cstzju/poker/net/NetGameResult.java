package com.cstzju.poker.net;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetGameResult extends NetMessage {

	/**
	 * 游戏结果。
	 */
	public NetGameResult() {
		time = Common.getTime();
		cmd = GAME_RESULT;
	}

	/**
	 * public void setData(String winnerName)
	 * 
	 * @param winnerName
	 * @throws JSONException
	 * @throws UnsupportedEncodingException 
	 */
	public void setData(String winnerName) throws JSONException, UnsupportedEncodingException {
		// JSON Data Example: {"winner": "192.168.0.31"}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("winner", winnerName);
		len = jsonObject.toString().length();
		data = new byte[len];
		data = jsonObject.toString().getBytes("UTF-8");
	}

	/**
	 * public String getWinnerData()
	 * 
	 * @return 赢家的IPV4地址
	 * @throws NumberFormatException
	 * @throws JSONException
	 */
	public String getWinnerData() throws NumberFormatException, JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
		return jsonObject.getString("winner");
	}
}
