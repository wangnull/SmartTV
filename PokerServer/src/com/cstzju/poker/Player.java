package com.cstzju.poker;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import com.cstzju.poker.controller.Card;

public class Player {
	// private int[] pokes = null;
	public int[] pokers_id;
	private String name = null;
	public int cardnumber = 0;
	private String IP = null;

	public Player(String name, String IP) {
		this.name = name;
		this.IP = IP;
	}

	public void setPokers(ArrayList<Card> pokers) {
		this.cardnumber = pokers.size();
		pokers_id = new int[cardnumber];
		for (int i = 0; i < cardnumber; i++) {
			this.pokers_id[i] = pokers.get(i).picID;
		}
	}

	public void pushPoker() {
		// TODO - 出牌
	}

	public void giveUpPushPoker() {
		// TODO - 放弃出牌
	}

}
