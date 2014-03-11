package com.cstzju.poker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.json.JSONException;

import com.cstzju.poker.net.NetGameResult;
import com.cstzju.poker.net.NetPushCard;
import com.cstzju.poker.utils.CharacterUtil;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class Communication extends Service {
	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new CommunicationBinder();

	private final String TAG = "PokerServer";
	Thread udp = null;
	Thread receiveTcp = null;
	Thread sendTcp = null;
	String[] player = new String[3];
	final int Port_f = CharacterUtil.tcpPort;

	DatagramSocket socket;
	DatagramPacket receivePacket;
	DatagramPacket sendPacket;
	byte[] dataReceive = new byte[1024];
	byte[] dataSend;

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class CommunicationBinder extends Binder {
		Communication getService() {
			return Communication.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// return null;
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// Log.i("PokerServer", "Server的onCreate()方法；");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Log.i("PokerServer", "Server的onStartCommand()方法；");
		startUDPConnection();
		// startTcpToReceiveCards();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// Log.i("PokerServer", "Server的onDestroy()方法；");
		if (udp != null && udp.isAlive()) {
			udp.interrupt();
		}
		if (receiveTcp != null && receiveTcp.isAlive()) {
			receiveTcp.interrupt();
		}
		if (sendTcp != null && sendTcp.isAlive()) {
			sendTcp.interrupt();
		}
		super.onDestroy();
	}

	private void startUDPConnection() {
		udp = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Log.i("PokerServer", "服务器建立UDP。");
					socket = new DatagramSocket(CharacterUtil.udpServerPort);
					int i = 3;
					while (i > 0) {
						receivePacket = new DatagramPacket(dataReceive,
								dataReceive.length); // 建立UDP连接，将接收到的数据放入数组中
						socket.receive(receivePacket);
						Log.i(TAG,
								"收到客户端的UDP广播，客户端IP地址是："
										+ receivePacket.getAddress());

						player[3 - i] = receivePacket.getAddress()
								.getHostAddress();
						switch (3 - i) {
						case 0:
							i--;
							sendUdpResponseToClient(i);
							break;
						case 1:
							if (!player[1].equals(player[0])) {
								i--;
								sendUdpResponseToClient(i);
							}
							break;
						case 2:
							if ((!player[1].equals(player[0]))
									&& (!player[2].equals(player[1]))) {
								i--;
								sendUdpResponseToClient(i);
							}
							break;
						default:
							break;
						}
					}
					Log.i(TAG, "玩家已满3人，开始初始化游戏");
					initThreePlayer(player);
					player = null;
				} catch (SocketException e) {
					Log.e("Socket Exception", e.toString());
				} catch (IOException e) {
					Log.e("IO Exception", e.toString());
				}
			}
		});
		udp.start();
	}

	private void sendUdpResponseToClient(int i) {// 发送UDP反馈给玩家,重复发送的UDP不予响应
		Log.i(TAG, "已加入第" + (3 - i) + "个玩家");
		try {
			socket = new DatagramSocket(CharacterUtil.udpServerPort);
			dataSend = String.valueOf(CharacterUtil.tcpPort).getBytes();
			sendPacket = new DatagramPacket(dataSend, dataSend.length,
					receivePacket.getSocketAddress());
			socket.send(sendPacket);
			Log.i(TAG, "成功发送UDP给客户端。");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void startTcpToReceiveCards() {// 准备接受玩家的牌，该thread需要手动stop
		receiveTcp = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("PokerServer", "服务器建立TCP准备收牌。");
				ServerSocket server;
				NetPushCard card = new NetPushCard();
				try {
					server = new ServerSocket(CharacterUtil.tcpPort);
					while (true) {
						Socket client = server.accept();
						card.fromStream(client.getInputStream());
						playerPushedCard(card.getCardNumberArray());
						Log.i(TAG, "收到数据:"
								+ client.getInetAddress().getHostAddress()
								+ "。牌的数量：" + card.getCardNumberArray().length);
						for (int i = 0; i < card.getCardNumberArray().length; i++) {
							Log.i(TAG,
									String.valueOf(card.getCardNumberArray()[i]));
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		receiveTcp.start();
	}

	private void initThreePlayer(String[] IP) {// 初始化三个玩家
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MY_RECEIVER");
		intent.putExtra("step", 1);
		intent.putExtra("ip", IP);
		sendBroadcast(intent);
		intent = null;
	}

	private void playerPushedCard(int[] pushedcards) {// 玩家出的牌
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MY_RECEIVER");
		intent.putExtra("step", 3);
		intent.putExtra("cards", pushedcards);
		sendBroadcast(intent);
		intent = null;
	}

	public void sendCardToClient(String IP, int[] cards) {
		// final String IP_f = InetAddress.getByName("112.15.173.232")
		// .getHostAddress();
		// final int Port_f = 80;
		final String IP_f = IP;
		final int[] cards_final = cards;

		sendTcp = new Thread(new Runnable() {
			@Override
			public void run() {
				NetPushCard card = new NetPushCard();
				try {
					card.setData(cards_final);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					Socket socket = new Socket(IP_f, Port_f);
					OutputStream os = socket.getOutputStream();
					card.toStream(os);
					Log.i(TAG, "发牌给" + IP_f);
					os.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		sendTcp.start();
	}

	public void sendGameResultToClinet(String IP, int status) {
		final int status_f = status;
		final String ip_f = IP;
		receiveTcp.interrupt();// 停止接收牌
		sendTcp = new Thread(new Runnable() {
			@Override
			public void run() {
				NetGameResult gameresult = new NetGameResult();
				gameresult.setData(status_f);
				try {
					Socket socket = new Socket(ip_f, Port_f);
					OutputStream os = socket.getOutputStream();
					gameresult.toStream(os);
					Log.i(TAG, "发送给" + ip_f + "游戏结果:" + status_f);
					os.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		sendTcp.start();
		startUDPConnection();// 接收UDP广播，开始新一轮游戏
	}
}
