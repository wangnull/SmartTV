package com.cstzju.poker;

public class Player {
	// private int[] pokes = null;
	public int[] pokers_pushed_id;
	// public int[] pokers_id;
	public String name = null;
	public int cardnumber = 0;
	private String IP = null;

	public Player(String name, String IP) {
		this.name = name;
		this.IP = IP;
		this.cardnumber = 17;
	}

	// public void setPokers(ArrayList<Card> pokers) {
	// this.cardnumber = pokers.size();
	// pokers_id = new int[cardnumber];
	// for (int i = 0; i < cardnumber; i++) {
	// this.pokers_id[i] = pokers.get(i).picID;
	// }
	// }

	public void pushPoker(int[] pokers) {
		pokers_pushed_id = new int[pokers.length];
		cardnumber -= pokers.length;
		this.pokers_pushed_id = pokers;
	}

	public void giveUpPushPoker() {
		// TODO - 放弃出牌
	}

	public String getStringCardNumber() {
		String str = String.valueOf(cardnumber);
		// Log.i("PokerServer", str);
		return str;
	}

	public void setDZ() {
		this.cardnumber += 3;
	}
}
