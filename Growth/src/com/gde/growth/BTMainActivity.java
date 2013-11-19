package com.gde.growth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.Toast;

@TargetApi(5)
public class BTMainActivity extends MainActivity {

	public boolean isRunning = false;

	private OutputStream mOutputStream;
	private PrintWriter mPrintWriterOUT;
	private BufferedReader mBufferedReader;

	private static BluetoothSocket mSocket;

	private int ID;

	private void initBT() {

		try {

			mBufferedReader = new BufferedReader(new InputStreamReader(
					mSocket.getInputStream()));
			mOutputStream = mSocket.getOutputStream();
			mPrintWriterOUT = new PrintWriter(mOutputStream);
		} catch (UnknownHostException e) {

			Toast.makeText(this, "UNKNOWN SERVER ADDRESS", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {

			Toast.makeText(this, "ERROR CREATING SOCKET", Toast.LENGTH_SHORT).show();
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBT();

		findViewById(R.id.boardview)
				.setOnDragListener(new BoardDragListener1());

		startReceiverThread();
	}

	public String receiveMessage() {

		String receivedMessage;
		// Toast.makeText(this, "RECEIVE MESSAGE CALLED", 100).show();
		try {
			receivedMessage = new String(mBufferedReader.readLine() + "\n");
			receivedMessage.trim();
			return receivedMessage;
		} catch (IOException e) {
			// Debug.d("error reading stream");
			isRunning = false;
			Toast.makeText(this, "NOT ABLE TO RECEIVE MESSAGE", Toast.LENGTH_SHORT).show();
			// endGame(0);
		}

		return null;

	}

	public void sendMessage(String str) {

		mPrintWriterOUT.println(str);
		mPrintWriterOUT.flush();
	}

	@SuppressLint("NewApi")
	private class BoardDragListener1 implements OnDragListener {

		private float x, y;

		@Override
		public boolean onDrag(View v, DragEvent event) {

			BoardView container = (BoardView) v;
			View view = (View) event.getLocalState();

			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				container.d = check;
				x = event.getX();
				y = event.getY();
				container.x = (int) x;
				container.y = (int) y;
				view = (View) event.getLocalState();
				container.postInvalidate();

				break;
			case DragEvent.ACTION_DRAG_EXITED:
				container.d = 0;
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				container.d = check;
				view = (View) event.getLocalState();
				x = event.getX();
				y = event.getY();
				container.x = (int) x;
				container.y = (int) y - 100;
				container.postInvalidate();

				break;
			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
				container.d = 0;
				x = event.getX();
				y = event.getY();
				int i = (int) (x * game.size / container.width);
				int j = (int) ((y - 100) * game.size / container.width);

				sendMessage("set " + Integer.toString(i) + Integer.toString(j)
						+ Integer.toString(ID) + Integer.toString(check));

				game.setTile(j, i, ID, check - 1);
				container.postInvalidate();
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				view = (View) event.getLocalState();
				if (count == 0)
					view.setVisibility(View.INVISIBLE);

			default:
				break;
			}
			return true;
		}
	}

	private void startReceiverThread() {
		isRunning = true;
		// Toast.makeText(this, "RECEIVER THREAD RUNNING", 100).show();
		(new Thread() {

			public void run() {

				while (true) {
					if (isRunning) {
						String[] msgArray = new String[3];
						msgArray = receiveMessage().split(" ", 3);

						// Debug.d("incoming: " + msg);

						handleReceivedMessage(msgArray);

					} else
						break;
				}

			}
		}).start();

	}

	private void handleReceivedMessage(String[] msgArray) {
		if (msgArray[0].contains("set"))
			game.setTile(Integer.parseInt(msgArray[2]),
					Integer.parseInt(msgArray[1]),
					Integer.parseInt(msgArray[3]),
					Integer.parseInt(msgArray[4]));

	}

	public static void setBluetoothSocket(BluetoothSocket _BTSocket) {
		mSocket = _BTSocket;
	}

}
