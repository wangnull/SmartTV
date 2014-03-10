package com.cstzju.poker;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;

import com.cstzju.poker.net.NetPushCard;
import com.cstzju.poker.utils.CharacterUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Communication extends Service {
	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	// private final IBinder mBinder = new CommunicationBinder();

	DatagramSocket socket;
	DatagramPacket receivePacket;
	DatagramPacket sendPacket;
	InetAddress clientAddress;
	byte[] dataReceive = new byte[1024];
	byte[] dataSend;
	final String tcpPort = "" + CharacterUtil.tcpServerPort;
	String message;
	Thread udp;
	Thread tcp;

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	// public class CommunicationBinder extends Binder {
	// Communication getService() {
	// return Communication.this;
	// }
	// }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
		// return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// Log.i("PokerServer", "Server的onCreate()方法；");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Log.i("PokerServer", "Server的onStartCommand()方法；");
		teset();
		// startUDPConnection();
		// startTCPConnection();
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		// Log.i("PokerServer", "Server的onDestroy()方法；");
		udp.interrupt();
		tcp.interrupt();
		super.onDestroy();

	}

	public void startUDPConnection() {
		udp = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.i("PokerServer", "服务器建立UDP。");
					socket = new DatagramSocket(CharacterUtil.udpServerPort);
					while (true) {
						receivePacket = new DatagramPacket(dataReceive,
								dataReceive.length); // 建立UDP连接，将接收到的数据放入数组中
						socket.receive(receivePacket);
						Log.i("PokerServer", "已经收到客户端的UDP广播，客户端IP地址是："
								+ receivePacket.getAddress());
						dataSend = tcpPort.getBytes();
						sendPacket = new DatagramPacket(dataSend,
								dataSend.length,
								receivePacket.getSocketAddress());
						socket.send(sendPacket);
						Log.i("PokerServer", "成功发送UDP给客户端。");
					}
				} catch (SocketException e) {
					Log.e("Socket Exception", e.toString());
				} catch (IOException e) {
					Log.e("IO Exception", e.toString());
				}

			}
		});
		udp.start();
	}

	public void startTCPConnection() {
		tcp = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("PokerServer", "服务器建立TCP。");
				ServerSocket server;
				NetPushCard card = new NetPushCard();
				try {
					server = new ServerSocket(CharacterUtil.tcpServerPort);
					while (true) {
						Socket client = server.accept();
						card.fromStream(client.getInputStream());
						Log.i("PokerServer", "客户端IP:"
								+ client.getInetAddress().toString());
						Log.i("PokerServer",
								"传送的数据的長度:" + card.getCardNumberArray().length);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		tcp.start();
	}

	public void teset() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MY_RECEIVER");
		intent.putExtra("test", 1234);
		sendBroadcast(intent);
	}

}
