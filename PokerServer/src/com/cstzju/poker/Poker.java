package com.cstzju.poker;

import com.cstzju.poker.controller.CardDealing;
import com.cstzju.poker.controller.CardUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Poker extends Activity {
	// Service类 Communication 的引用
	// private Communication communicationREF;
	private MyReceiver receiver;

	final int LEFT_PLAYER = 1;
	final int MIDDLE_PLAYER = 2;
	final int RIGHT_PLAYER = 3;

	Intent serverIntent;
	ImageView threeTop_left;
	ImageView threeTop_middle;
	ImageView threeTop_right;

	TextView left_player_name;
	TextView left_player_poker_number;
	PokerView left_player_poker;
	PokerView left_player_poker_pushed;

	TextView middle_player_name;
	TextView middle_player_poker_number;
	PokerView middle_player_poker;
	PokerView middle_player_poker_pushed;

	TextView right_player_name;
	TextView right_player_poker_number;
	PokerView right_player_poker;
	PokerView right_player_poker_pushed;

	Player left_player;
	Player middle_player;
	Player right_player;

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
		initGameView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 注册广播
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MY_RECEIVER");
		registerReceiver(receiver, filter);
		// 启动服务
		serverIntent = new Intent(this, Communication.class);
		startService(serverIntent);
		// bindService(serverIntent, serviceConnection,
		// Context.BIND_AUTO_CREATE);

		left_player_poker = (PokerView) findViewById(R.id.left_player_poker);
		left_player_poker_pushed = (PokerView) findViewById(R.id.left_player_pokerpushed);
		middle_player_poker = (PokerView) findViewById(R.id.middle_player_poker);
		middle_player_poker_pushed = (PokerView) findViewById(R.id.middle_player_pokerpushed);
		right_player_poker = (PokerView) findViewById(R.id.right_player_poker);
		right_player_poker_pushed = (PokerView) findViewById(R.id.right_player_pokerpushed);

		// 假设收到3个客户端的连接
		left_player = new Player("玩家1", "192.168.0.23");
		middle_player = new Player("玩家2", "192.168.0.24");
		right_player = new Player("玩家3", "192.168.0.25");
		left_player_name.setText(left_player.name);
		middle_player_name.setText(left_player.name);
		right_player_name.setText(left_player.name);

		// 洗牌
		CardDealing cardDealing = new CardDealing();
		cardDealing.init();
		left_player_poker.setPokerBackCount(left_player.cardnumber);
		left_player_poker_number.setText(left_player.getStringCardNumber());
		middle_player_poker.setPokerBackCount(middle_player.cardnumber);
		middle_player_poker_number.setText(middle_player.getStringCardNumber());
		right_player_poker.setPokerBackCount(right_player.cardnumber);
		right_player_poker_number.setText(right_player.getStringCardNumber());
		updateTopThreePokers(cardDealing.getCardTop3().get(0).picID,
				cardDealing.getCardTop3().get(1).picID, cardDealing
						.getCardTop3().get(2).picID);

		// 把全部牌都出掉
		left_player.pushPoker(CardUtil.Objects2Id(cardDealing.getCardP1()));
		reDrawPlayerViewDuringPushPoker(LEFT_PLAYER);
		middle_player.pushPoker(CardUtil.Objects2Id(cardDealing.getCardP2()));
		reDrawPlayerViewDuringPushPoker(MIDDLE_PLAYER);
		right_player.pushPoker(CardUtil.Objects2Id(cardDealing.getCardP3()));
		reDrawPlayerViewDuringPushPoker(RIGHT_PLAYER);

		// alertdialog("" + getWindowManager().getDefaultDisplay().getWidth());
		// alertdialog("" + getWindowManager().getDefaultDisplay().getHeight());

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Log.i("PokerServer", String.valueOf(communicationREF.test()));
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
		// unbindService(serviceConnection);
		unregisterReceiver(receiver);
	}

	// private void alertdialog(CharSequence message) {
	// AlertDialog.Builder ad = new AlertDialog.Builder(this);
	// ad.setTitle("通知");
	// ad.setMessage(message);
	// ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// }
	// });
	// ad.show();
	// }

	private void initGameView() {
		threeTop_left = (ImageView) findViewById(R.id.three_border_first);
		threeTop_middle = (ImageView) findViewById(R.id.three_border_second);
		threeTop_right = (ImageView) findViewById(R.id.three_border_third);
		left_player_name = (TextView) findViewById(R.id.left_player_name);
		middle_player_name = (TextView) findViewById(R.id.middle_player_name);
		right_player_name = (TextView) findViewById(R.id.right_player_name);
		left_player_poker_number = (TextView) findViewById(R.id.left_player_number);
		middle_player_poker_number = (TextView) findViewById(R.id.middle_player_number);
		right_player_poker_number = (TextView) findViewById(R.id.right_player_number);

		threeTop_left.setImageResource(R.drawable.back);
		threeTop_middle.setImageResource(R.drawable.back);
		threeTop_right.setImageResource(R.drawable.back);

		// left_player_name.setText("test");
		// left_player_poker_number.setText("17");
	}

	private void updateTopThreePokers(int left_poker_id, int middle_poker_id,
			int right_poker_id) {
		threeTop_left.setImageResource(left_poker_id);
		threeTop_middle.setImageResource(middle_poker_id);
		threeTop_right.setImageResource(right_poker_id);

		threeTop_left = null;
		threeTop_middle = null;
		threeTop_right = null;
	}

	private void reDrawPlayerViewDuringPushPoker(int player) {
		if (player == LEFT_PLAYER) {
			left_player_poker_pushed
					.setPokerAndPokerCount(left_player.pokers_pushed_id);
			left_player_poker_number.setText(left_player.getStringCardNumber());
			left_player_poker.setPokerBackCount(left_player.cardnumber);
			left_player_poker_pushed.invalidate();
			left_player_poker.invalidate();
		} else if (player == MIDDLE_PLAYER) {
			middle_player_poker_pushed
					.setPokerAndPokerCount(middle_player.pokers_pushed_id);
			middle_player_poker_number.setText(middle_player
					.getStringCardNumber());
			middle_player_poker.setPokerBackCount(middle_player.cardnumber);
			middle_player_poker_pushed.invalidate();
			middle_player_poker.invalidate();
		} else if (player == RIGHT_PLAYER) {
			right_player_poker_pushed
					.setPokerAndPokerCount(right_player.pokers_pushed_id);
			right_player_poker_number.setText(right_player
					.getStringCardNumber());
			right_player_poker.setPokerBackCount(right_player.cardnumber);
			right_player_poker_pushed.invalidate();
			right_player_poker.invalidate();
		}
	}

	// private void reDrawPlayerViewDuringCalledDZ(int player) {
	// if (player == LEFT_PLAYER) {
	//
	// } else if (player == MIDDLE_PLAYER) {
	//
	// } else if (player == RIGHT_PLAYER) {
	//
	// }
	// }

	// 利用IBinder获取Service实例
	// ServiceConnection serviceConnection = new ServiceConnection() {
	// @Override
	// public void onServiceDisconnected(ComponentName name) {
	// }
	//
	// @Override
	// public void onServiceConnected(ComponentName name, IBinder service) {
	// communicationREF = ((Communication.CommunicationBinder) service)
	// .getService();
	//
	// }
	// };

	// 利用broadcast实现通信，onReceive 10s内没有执行完毕会导致anr。
	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			int test = bundle.getInt("test");
			Log.i("PokerServer", test + "aaaaaaaaaaaaaaaaaaaaaa");
		}
	}
}
