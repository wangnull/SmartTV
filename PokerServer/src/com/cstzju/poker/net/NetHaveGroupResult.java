package com.cstzju.poker.net;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetHaveGroupResult extends NetMessage {

	public NetHaveGroupResult() {
		time = Common.getTime();
		cmd = HAVE_GROUP_RESULT;
	}

	/**
	 * public void setData(int status) 设置游戏状态
	 * 
	 * @param status
	 *            0表示等待组队，1表示无游戏，可以发起组队，2表示游戏进行中
	 * @throws JSONException
	 * @throws UnsupportedEncodingException 
	 */
	public void setData(int status) throws JSONException, UnsupportedEncodingException {
		// JSON Data Example: {"status": "2"}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", status);
		len = jsonObject.toString().length();
		data = new byte[len];
		data = jsonObject.toString().getBytes("UTF-8");
	}

	/**
	 * public int getIntData()
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
