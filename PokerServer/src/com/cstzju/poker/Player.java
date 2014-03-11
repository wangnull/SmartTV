package com.cstzju.poker;

public class Player {
	// private int[] pokes = null;
	public int[] pokers_pushed_id = { 0 };
	public int[] pokers_id;
	public String name = null;
	public int cardnumber = 0;
	public String IP = null;

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

	public void setPokerCount(int count) {
		this.cardnumber = count;
	}

	public void pushPoker(int[] pokers) {
		cardnumber -= pokers.length;
		pokers_pushed_id = new int[pokers.length];
		this.pokers_pushed_id = pokers;
	}

	public String getStringCardNumber() {
		String str = String.valueOf(cardnumber);
		// Log.i("PokerServer", str);
		return str;
	}

	// public void setDZ() {
	// this.cardnumber += 3;
	// }
}
