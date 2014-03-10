package com.cstzju.poker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import org.json.JSONException;

import com.cstzju.poker.controller.Card;
import com.cstzju.poker.controller.CardUtil;
import com.cstzju.poker.net.NetPushCard;
import com.cstzju.poker.utils.CharacterUtil;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class Communication extends Service {
	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new CommunicationBinder();

	private final String TAG = "PokerServer";
	DatagramSocket socket;
	DatagramPacket receivePacket;
	DatagramPacket sendPacket;
	byte[] dataReceive = new byte[1024];
	byte[] dataSend;
	final String tcpPort = "" + CharacterUtil.tcpServerPort;
	Thread udp;
	Thread tcp;
	Thread sendTcp;

	String[] player = new String[3];
	int[] port = new int[3];

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
		// startTCPConnection();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// Log.i("PokerServer", "Server的onDestroy()方法；");
		udp.interrupt();
		tcp.interrupt();
		sendTcp.interrupt();
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
								"已经收到客户端的UDP广播，客户端IP地址是："
										+ receivePacket.getAddress());

						player[3 - i] = receivePacket.getAddress().toString();
						port[3 - i] = receivePacket.getPort();
						switch (3 - i) {
						case 0:
							i--;
							Log.i(TAG, "已加入第" + (3 - i) + "个玩家");
							break;
						case 1:
							if (!player[1].equals(player[0])) {
								i--;
								Log.i(TAG, "已加入第" + (3 - i) + "个玩家");
							}
							break;
						case 2:
							if ((!player[1].equals(player[0]))
									&& (!player[2].equals(player[1]))) {
								i--;
								Log.i(TAG, "已加入第" + (3 - i) + "个玩家");
							}
							break;
						default:
							break;
						}
						dataSend = tcpPort.getBytes();
						sendPacket = new DatagramPacket(dataSend,
								dataSend.length,
								receivePacket.getSocketAddress());
						socket.send(sendPacket);
						Log.i(TAG, "成功发送UDP给客户端。");
					}
					Log.i(TAG, "玩家已满3人，开始初始化游戏");
					initThreePlayer(player, port);
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

	// private void startTCPConnection() {
	// tcp = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// // Log.i("PokerServer", "服务器建立TCP。");
	// ServerSocket server;
	// NetPushCard card = new NetPushCard();
	// try {
	// server = new ServerSocket(CharacterUtil.tcpServerPort);
	// while (true) {
	// Socket client = server.accept();
	// card.fromStream(client.getInputStream());
	// // Log.i("PokerServer", "客户端IP:"
	// // + client.getInetAddress().toString());
	// // Log.i("PokerServer",
	// // "传送的数据的長度:" + card.getCardNumberArray().length);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// tcp.start();
	// }

	private void initThreePlayer(String[] IP, int[] port) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MY_RECEIVER");
		intent.putExtra("step", 1);
		intent.putExtra("ip", IP);
		intent.putExtra("port", port);
		sendBroadcast(intent);
		intent = null;
	}

	public void sendCardToClientByUsingTCP(String IP, int port, int[] cards) {
		// for (int i = 0; i < cards.length; i++) {
		// Log.i("PokerServer", "发送的牌为：" + cards[i] + "");
		// }
		final String IP_final = IP;
		final int[] cards_final = cards;
		final int Port_final = CharacterUtil.tcpClientPort;
		Log.i(TAG, "發送給" + IP_final + ":" + Port_final);
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
					Socket socket = new Socket(IP_final, Port_final);
					OutputStream os = socket.getOutputStream();
					card.toStream(os);
					Log.i(TAG, "数据被编码成字节流发送给" + IP_final + ":" + Port_final);
					os.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {

				}
			}
		});
		sendTcp.start();
	}
}
