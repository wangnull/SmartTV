package com.cstzju.poker;

import com.cstzju.poker.controller.CardDealing;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Poker extends Activity {
	int left_player_time = 1;
	int middle_player_time = 2;
	int right_player_time = 3;
	Intent serverIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		// onStart()中如果存档不正确就启动新游戏
		super.onStart();
		// 假设收到3个客户端的连接
		Player left_player = new Player("玩家1", "192.168.0.23");
		Player middle_player = new Player("玩家2", "192.168.0.24");
		Player right_player = new Player("玩家3", "192.168.0.25");

		// 三个玩家的界面更改，第一个玩家
		PokerView left_poker = (PokerView) findViewById(R.id.left_player_poker);
		left_poker.setPokerBackCount(17);
		PokerView left_poker_pushed = (PokerView) findViewById(R.id.left_player_pokerpushed);
		// 第二个玩家
		PokerView middle_poker = (PokerView) findViewById(R.id.middle_player_poker);
		middle_poker.setPokerBackCount(17);
		PokerView middle_poker_pushed = (PokerView) findViewById(R.id.middle_player_pokerpushed);
		// 第三个玩家
		PokerView right_poker = (PokerView) findViewById(R.id.right_player_poker);
		right_poker.setPokerBackCount(17);
		PokerView right_poker_pushed = (PokerView) findViewById(R.id.right_player_pokerpushed);

		// 洗牌
		CardDealing cardDealing = new CardDealing();
		cardDealing.init();

		left_player.setPokers(cardDealing.getCardP1());
		middle_player.setPokers(cardDealing.getCardP2());
		right_player.setPokers(cardDealing.getCardP3());

		left_poker_pushed.setPokerAndPokerCount(left_player.pokers_id, 3);
		left_poker_pushed.invalidate();
		middle_poker_pushed.setPokerAndPokerCount(middle_player.pokers_id, 3);
		middle_poker_pushed.postInvalidate();
		right_poker_pushed.setPokerAndPokerCount(right_player.pokers_id, 3);
		middle_poker_pushed.postInvalidate();
		// alertdialog("" + getWindowManager().getDefaultDisplay().getWidth());
		// alertdialog("" + getWindowManager().getDefaultDisplay().getHeight());

		serverIntent = new Intent(this, Communication.class);
		startService(serverIntent);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onPause() {
		// onPause(),onReSume(),OnStop()。数据储存，及游戏计时器的开关
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		stopService(serverIntent);
	}

	private void alertdialog(CharSequence message) {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("通知");
		ad.setMessage(message);
		ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		ad.show();
	}

}
