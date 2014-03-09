package com.cstzju.poker.net;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetPushCard extends NetMessage {

	/**
	 * 出牌
	 */
	public NetPushCard() {
		time = Common.getTime();
		cmd = GAME_PLAYING;
	}

	/**
	 * public void setData(String color, int number)设置牌的大小和花色
	 * 
	 * @param color
	 *            花色
	 * @param number
	 *            大小
	 * @throws JSONException
	 * @throws UnsupportedEncodingException 
	 */
	public void setData(int color, int number) throws JSONException, UnsupportedEncodingException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("color", color);
		jsonObject.put("number", number);
		len = jsonObject.toString().length();
		data = new byte[len];
		data = jsonObject.toString().getBytes("UTF-8");
	}

	/**
	 * public String getDataColorString() 返回牌的花色的String类型
	 * 
	 * @return “SPADE”是黑桃，“HEART”是红桃，“CLUB”是梅花，“DIAMOND”是方块，默认是“NULL”
	 * @throws NumberFormatException
	 * @throws JSONException
	 */
	public String getDataColorString() throws NumberFormatException,
			JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data, 0,
				len));
		String color = "NULL";
		switch (Integer.parseInt(jsonObject.getString("color"))) {
		case 1:
			color = "SPADE";
			break;
		case 2:
			color = "HEART";
			break;
		case 3:
			color = "CLUB";
			break;
		case 4:
			color = "DIAMOND";
			break;
		default:
			color = "NULL";
			break;
		}
		return color;

	}

	/**
	 * public int getIntDataColor() 返回牌的花色的Int类型
	 * 
	 * @return 1是黑桃，2是红桃，3是梅花，4是方块。
	 * @throws NumberFormatException
	 * @throws JSONException
	 */
	public int getIntDataColor() throws NumberFormatException, JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data, 0,
				len));
		return Integer.parseInt(jsonObject.getString("color"));
	}

	/**
	 * public int getIntDataNumber() 返回牌的大小
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws JSONException
	 */
	public int getIntDataNumber() throws NumberFormatException, JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
		return Integer.parseInt(jsonObject.getString("number"));
	}
}
