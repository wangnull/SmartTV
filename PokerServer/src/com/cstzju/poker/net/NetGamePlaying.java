package com.cstzju.poker.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetGamePlaying extends NetMessage {
	private JSONObject jsonObject = new JSONObject();
	private JSONArray cards = new JSONArray();

	// private JSONObject jsonData = data;
	/**
	 * 服务器给各个客户端发送纸牌数据。 例如如果执行以下代码：
	 * {@code NetGamePlaying cards = new NetGamePlaying(); 
	 * cards.setCount(4); cards.addData(1, 2); cards.addData(3, 4);
	 * cards.addData(5, 6); cards.addData(7, 8); cards.EndAddData();} 实际发出的数据是：
	 * {@code "count" :4,"cards":[ {"color":1,"number":2},{"color":3,"number":4},
	 * {"color":5,"number":6},{"color":7,"number":8}]}}
	 */
	public NetGamePlaying() {
		time = Common.getTime();
		cmd = GAME_PLAYING;
	}

	/**
	 * public void EndAddData()数据添加结束。
	 * 
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public void EndAddData() throws JSONException, UnsupportedEncodingException {
		// JSON Data Example:{"count": "2","cards": [{"color": "3","number":
		// "1"},{"color": "3","number": "1"}]}
		jsonObject.put("cards", cards);
		len = jsonObject.toString().length();
		data = new byte[len];
		// Converter.StringToBytes(jsonObject.toString(), data, 0);
		data = jsonObject.toString().getBytes("UTF-8");
	}

	/**
	 * public void setCount(int count) 添加数据Count。
	 * 
	 * @param count
	 * @throws JSONException
	 */
	public void setCount(int count) throws JSONException {
		jsonObject.put("count", count);
	}

	/**
	 * public void addData(int color, int number) 添加牌面数据。
	 * 
	 * @param color
	 *            花色
	 * @param number
	 *            牌面
	 * @throws JSONException
	 */
	public void addData(int color, int number) throws JSONException {
		// cards.put("color", color).put("number", number);
		JSONObject cardDetail = new JSONObject();
		cardDetail.put("color", color).put("number", number);
		cards.put(cardDetail);
	}

	// TODO
	public void addDataList(int card[][]) {

	}

	/**
	 * 返回顔色List
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws JSONException
	 */
	public List<Integer> getDataColor() throws NumberFormatException,
			JSONException {
		List<Integer> colors = new ArrayList<Integer>();
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data, 0,
				len));
		JSONArray jsonArray = new JSONArray(jsonObject.getString("cards"));
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject temp = new JSONObject();
			colors.add(Integer.parseInt(temp.getString("color")));
		}
		return colors;
	}

	/**
	 * 返回牌面List
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws JSONException
	 */
	public List<Integer> getDataNumber() throws NumberFormatException,
			JSONException {
		List<Integer> numbers = new ArrayList<Integer>();
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data, 0,
				len));
		JSONArray jsonArray = new JSONArray(jsonObject.getString("cards"));
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject temp = new JSONObject();
			numbers.add(Integer.parseInt(temp.getString("number")));
		}
		return numbers;
	}

	// TODO
	// public int getDataNumber() throws NumberFormatException, JSONException {
	// JSONObject jsonObject = new JSONObject(Converter.BytesToString(data, 0,
	// len));
	// return Integer.parseInt(jsonObject.getString("result"));
	// }

	// TODO
	// public int[][] getData() throws NumberFormatException, JSONException {
	// JSONObject jsonObject = new JSONObject(Converter.BytesToString(data, 0,
	// len));
	// jsonObject.length();
	// return 0;
	// }

	/**
	 * 返回Count
	 */
	public int getCount() throws JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data, 0,
				len));
		return Integer.parseInt(jsonObject.getString("count"));

	}

	/**
	 * public String getSendJSONData() 返回发送的JSON格式的数据
	 * 
	 * @return
	 * @throws JSONException
	 */
	public String getSendJSONData() throws JSONException {
		return jsonObject.toString();
	}

	// /**
	// * public String getBackJSONData() 返回服务器收取到的JSON格式的数据
	// *
	// * @return
	// * @throws JSONException
	// */
	// public String getBackJSONData() throws JSONException {
	// String result = null;
	// if (len > 0) {
	// JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
	// result = jsonObject.toString();
	// } else
	// result = "0";
	// return result;
	// }
}
