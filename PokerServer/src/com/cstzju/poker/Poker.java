package com.cstzju.poker;

import com.cstzju.poker.controller.CardDealing;
import com.cstzju.poker.controller.CardUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Poker extends Activity {
	// Service类 Communication 的引用
	private Communication communicationREF;
	// BroadcastReceive类的引用
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

		initID();
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
		bindService(serverIntent, serviceConnection, Context.BIND_AUTO_CREATE);

		// alertdialog("" + getWindowManager().getDefaultDisplay().getWidth());
		// alertdialog("" + getWindowManager().getDefaultDisplay().getHeight());

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
		unregisterReceiver(receiver);
		unbindService(serviceConnection);
		stopService(serverIntent);
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

	private void initID() {
		threeTop_left = (ImageView) findViewById(R.id.three_border_first);
		threeTop_middle = (ImageView) findViewById(R.id.three_border_second);
		threeTop_right = (ImageView) findViewById(R.id.three_border_third);
		left_player_name = (TextView) findViewById(R.id.left_player_name);
		middle_player_name = (TextView) findViewById(R.id.middle_player_name);
		right_player_name = (TextView) findViewById(R.id.right_player_name);
		left_player_poker_number = (TextView) findViewById(R.id.left_player_number);
		middle_player_poker_number = (TextView) findViewById(R.id.middle_player_number);
		right_player_poker_number = (TextView) findViewById(R.id.right_player_number);
		left_player_poker = (PokerView) findViewById(R.id.left_player_poker);
		left_player_poker_pushed = (PokerView) findViewById(R.id.left_player_pokerpushed);
		middle_player_poker = (PokerView) findViewById(R.id.middle_player_poker);
		middle_player_poker_pushed = (PokerView) findViewById(R.id.middle_player_pokerpushed);
		right_player_poker = (PokerView) findViewById(R.id.right_player_poker);
		right_player_poker_pushed = (PokerView) findViewById(R.id.right_player_pokerpushed);
	}

	private void initGameView() {
		threeTop_left.setImageResource(R.drawable.back);
		threeTop_middle.setImageResource(R.drawable.back);
		threeTop_right.setImageResource(R.drawable.back);
	}

	private void reStartGameView() {
		initGameView();
		left_player_name.setText("第一个玩家");
		left_player_poker_number.setText("0");
		middle_player_name.setText("第二个玩家");
		middle_player_poker_number.setText("0");
		right_player_name.setText("第三个玩家");
		right_player_poker_number.setText("0");
	}

	private void reDrawTopThreePokers(int left_poker_id, int middle_poker_id,
			int right_poker_id) {// 洗牌后，更新地主的三张牌
		threeTop_left.setImageResource(left_poker_id);
		threeTop_middle.setImageResource(middle_poker_id);
		threeTop_right.setImageResource(right_poker_id);

		threeTop_left = null;
		threeTop_middle = null;
		threeTop_right = null;
	}

	private void reDrawPlayerViewDuringPushPoker(int player) {// 某个玩家出牌后更新
		if (player == LEFT_PLAYER) {
			left_player_poker_pushed
					.setPokerAndPokerCount(left_player.pokers_pushed_id);
			left_player_poker.setPokerBackCount(left_player.cardnumber);
			left_player_poker_number.setText(left_player.getStringCardNumber());
			left_player_poker_pushed.invalidate();
			left_player_poker.invalidate();
		} else if (player == MIDDLE_PLAYER) {
			middle_player_poker_pushed
					.setPokerAndPokerCount(middle_player.pokers_pushed_id);
			middle_player_poker.setPokerBackCount(middle_player.cardnumber);
			middle_player_poker_number.setText(middle_player
					.getStringCardNumber());
			middle_player_poker_pushed.invalidate();
			middle_player_poker.invalidate();
		} else if (player == RIGHT_PLAYER) {
			right_player_poker_pushed
					.setPokerAndPokerCount(right_player.pokers_pushed_id);
			right_player_poker.setPokerBackCount(right_player.cardnumber);
			right_player_poker_number.setText(right_player
					.getStringCardNumber());
			right_player_poker_pushed.invalidate();
			right_player_poker.invalidate();
		}
	}

	private void reDrawPlayerViewDuringInitGame() {// 洗牌后，三个玩家的牌更新
		// if (player == LEFT_PLAYER) {
		left_player_poker.setPokerBackCount(left_player.cardnumber);
		left_player_poker_number.setText(left_player.getStringCardNumber());
		left_player_poker.invalidate();
		// } else if (player == MIDDLE_PLAYER) {
		middle_player_poker.setPokerBackCount(middle_player.cardnumber);
		middle_player_poker_number.setText(middle_player.getStringCardNumber());
		middle_player_poker.invalidate();
		// } else if (player == RIGHT_PLAYER) {
		right_player_poker.setPokerBackCount(right_player.cardnumber);
		right_player_poker_number.setText(right_player.getStringCardNumber());
		right_player_poker.invalidate();
		// }
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
	ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			communicationREF = ((Communication.CommunicationBinder) service)
					.getService();
		}
	};

	// 利用broadcast实现通信，onReceive 10s内没有执行完毕会导致anr。
	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int boss = 0;// 地主
			int player_push_caard_now = 0;// 当前该哪个玩家出牌
			Bundle bundle = intent.getExtras();
			int step = bundle.getInt("step");
			if (step == 1) { // 新游戏新玩家
				String[] player = bundle.getStringArray("ip");
				// 3个客户端的连接
				left_player = new Player("玩家1", player[0]);
				middle_player = new Player("玩家2", player[1]);
				right_player = new Player("玩家3", player[2]);
				left_player_name.setText(left_player.name);
				middle_player_name.setText(middle_player.name);
				right_player_name.setText(right_player.name);
				// 开始游戏，洗牌，确定地主，更新地主的牌，发送消息给各个客户端。
				CardDealing cardDealing = new CardDealing();
				cardDealing.init();
				boss = cardDealing.getBoss();
				player_push_caard_now = boss;
				// 更新三个玩家牌的数量
				left_player.setPokerCount(cardDealing.getCardP1().size());
				middle_player.setPokerCount(cardDealing.getCardP2().size());
				right_player.setPokerCount(cardDealing.getCardP3().size());
				reDrawPlayerViewDuringInitGame();
				reDrawTopThreePokers(cardDealing.getCardTop3().get(0).picID,
						cardDealing.getCardTop3().get(1).picID, cardDealing
								.getCardTop3().get(2).picID);
				// 发送消息给客户端
				if (communicationREF != null) {// onServiceConnected是异步
					communicationREF.sendCardToClient(left_player.IP,
							CardUtil.Objects2Id(cardDealing.getCardP1()));
					communicationREF.sendCardToClient(middle_player.IP,
							CardUtil.Objects2Id(cardDealing.getCardP2()));
					communicationREF.sendCardToClient(right_player.IP,
							CardUtil.Objects2Id(cardDealing.getCardP3()));
					communicationREF.startTcpToReceiveCards();
				} else {
					Log.i("PokerServer", "onReceive():communicationREF is null");
				}
			} else if (step == 3) {// 玩家的出牌
				int[] pokers = bundle.getIntArray("cards");
				if (player_push_caard_now == LEFT_PLAYER) {
					left_player.pushPoker(pokers);
					reDrawPlayerViewDuringPushPoker(LEFT_PLAYER);
					if (left_player.cardnumber == 0) {
						setGameWinner(LEFT_PLAYER);
						reStartGameView();
					} else {
						player_push_caard_now = MIDDLE_PLAYER;
						sendPokerToNextPlayer(player_push_caard_now, pokers);
					}
				} else if (player_push_caard_now == MIDDLE_PLAYER) {
					middle_player.pushPoker(pokers);
					reDrawPlayerViewDuringPushPoker(MIDDLE_PLAYER);
					if (middle_player.cardnumber == 0) {
						setGameWinner(MIDDLE_PLAYER);
						reStartGameView();
					} else {
						player_push_caard_now = RIGHT_PLAYER;
						sendPokerToNextPlayer(player_push_caard_now, pokers);
					}
				} else if (player_push_caard_now == RIGHT_PLAYER) {
					right_player.pushPoker(pokers);
					reDrawPlayerViewDuringPushPoker(RIGHT_PLAYER);
					if (right_player.cardnumber == 0) {
						setGameWinner(RIGHT_PLAYER);
						reStartGameView();
					} else {
						player_push_caard_now = LEFT_PLAYER;
						sendPokerToNextPlayer(player_push_caard_now, pokers);
					}
				}
				pokers = null;
			}
		}
	}

	private void sendPokerToNextPlayer(int nextPlayer, int[] cardsNowPlayer) {// 把当前的牌发给下一个玩家
		if (communicationREF != null) {
			if (nextPlayer == LEFT_PLAYER) {// 下一个是玩家1出牌
				communicationREF.sendCardToClient(left_player.IP,
						cardsNowPlayer);
			} else if (nextPlayer == MIDDLE_PLAYER) {
				communicationREF.sendCardToClient(middle_player.IP,
						cardsNowPlayer);
			} else if (nextPlayer == RIGHT_PLAYER) {
				communicationREF.sendCardToClient(right_player.IP,
						cardsNowPlayer);
			}
		} else {
			Log.i("PokerServer",
					"sendPokerToNextPlayer():communicationREF is null");
		}
	}

	private void setGameWinner(int winner) {
		int WIN = 1;
		int LOSE = 2;
		// 发送消息给客户端
		if (communicationREF != null) {// onServiceConnected是异步
			if (winner == LEFT_PLAYER) {
				communicationREF.sendGameResultToClinet(left_player.IP, WIN);
				communicationREF.sendGameResultToClinet(middle_player.IP, LOSE);
				communicationREF.sendGameResultToClinet(right_player.IP, LOSE);
			} else if (winner == MIDDLE_PLAYER) {
				communicationREF.sendGameResultToClinet(left_player.IP, LOSE);
				communicationREF.sendGameResultToClinet(middle_player.IP, WIN);
				communicationREF.sendGameResultToClinet(right_player.IP, LOSE);
			} else if (winner == RIGHT_PLAYER) {
				communicationREF.sendGameResultToClinet(left_player.IP, LOSE);
				communicationREF.sendGameResultToClinet(middle_player.IP, LOSE);
				communicationREF.sendGameResultToClinet(right_player.IP, WIN);
			}
		} else {
			Log.i("PokerServer", "setGameWinner():communicationREF is null");
		}
	}
}
