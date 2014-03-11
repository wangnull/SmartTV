package com.cstzju.poker.net;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetGameResult extends NetMessage {
	public NetGameResult() {
		time = Common.getTime();
		cmd = GAME_RESULT;
	}

	/**
	 * 
	 * @param result
	 *            值为1则是赢家，否则为输家。
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public void setData(int result) {
		// JSON Data Example: {"result":1}
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		len = jsonObject.toString().length();
		data = new byte[len];
		try {
			data = jsonObject.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public int getIntResult() throws NumberFormatException, JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
		return Integer.parseInt(jsonObject.getString("result"));
	}

}