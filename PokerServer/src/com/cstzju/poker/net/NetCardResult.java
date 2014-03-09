package com.cstzju.poker.net;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetCardResult extends NetMessage {

	/**
	 * 玩家出牌的结果。比如牌面有沒有太小，出牌有没有超时。
	 */
	public NetCardResult() {
		time = Common.getTime();
		cmd = CARD_RESULT;
	}

	/**
	 * public void setData(int result) 设置牌面结果
	 * 
	 * @param result
	 *            0表示出牌合格，1表示牌面太小，2表示出牌超时
	 * @throws JSONException
	 * @throws UnsupportedEncodingException 
	 */
	public void setData(int result) throws JSONException, UnsupportedEncodingException {
		// JSON Data Example: {"result": "0"}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", result);
		len = jsonObject.toString().length();
		data = new byte[len];
		data = jsonObject.toString().getBytes("UTF-8");
	}

	/**
	 * public int getIntData() 获取有效数值
	 * 
	 * @return Data
	 * @throws NumberFormatException
	 * @throws JSONException
	 */
	public int getIntData() throws NumberFormatException, JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
		return Integer.parseInt(jsonObject.getString("result"));

	}

}
