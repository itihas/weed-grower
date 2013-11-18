package com.gde.growth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Handler boardHandler, sproutHandler;
	private Runnable b, s, m;
	private Thread boardThread, sproutThread;

	protected static GameLogic game;

	int count;
	static int check;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		game = new GameLogic(2, 20, 0);

		setContentView(R.layout.activity_main);

		boardHandler = new Handler();
		sproutHandler = new Handler();

		count = 0;

		setB(new Runnable() {
			@SuppressLint("NewApi")
			public void run() {
				game.boardUpdate();
				findViewById(R.id.boardview).postInvalidate();
				boardHandler.postDelayed(this, 1100);

			}
		});

		setS(new Runnable() {
			public void run() {
				sproutHandler.postDelayed(this, 100);
				count++;
				((TextView) findViewById(R.id.score_view)).setText(Integer
						.toString(count));
				if (count >= 10)
					findViewById(R.id.myimage1).setVisibility(View.VISIBLE);
				else
					findViewById(R.id.myimage1).setVisibility(View.INVISIBLE);
				if (count >= 20)
					findViewById(R.id.myimage2).setVisibility(View.VISIBLE);
				else
					findViewById(R.id.myimage2).setVisibility(View.INVISIBLE);
				if (count >= 15)
					findViewById(R.id.myimage3).setVisibility(View.VISIBLE);
				else
					findViewById(R.id.myimage3).setVisibility(View.INVISIBLE);
			}
		});

		boardThread = new Thread(b);
		sproutThread = new Thread(s);

		findViewById(R.id.myimage1).setOnTouchListener(new MyTouchListener());
		findViewById(R.id.myimage2).setOnTouchListener(new MyTouchListener());
		findViewById(R.id.myimage3).setOnTouchListener(new MyTouchListener());

		findViewById(R.id.boardview).setOnDragListener(new BoardDragListener());

		startGame();
	}

	public void startGame() {
		game.setTile(3, 3, 1, 0);
		game.setTile(4, 4, 1, 0);
		game.setTile(4, 6, 1, 0);
		game.setTile(5, 5, 1, 0);
		game.setTile(6, 4, 1, 0);
		game.setTile(6, 6, 1, 0);
		game.setTile(7, 7, 1, 0);

		game.setTile(13, 13, 2, 0);
		game.setTile(14, 14, 2, 0);
		game.setTile(14, 16, 2, 0);
		game.setTile(15, 15, 2, 0);
		game.setTile(16, 14, 2, 0);
		game.setTile(16, 16, 2, 0);
		game.setTile(17, 17, 2, 0);

		boardThread.start();

		sproutThread.start();
	}

	public Runnable getB() {
		return b;
	}

	public void setB(Runnable r) {
		this.b = r;
	}

	public Runnable getS() {
		return s;
	}

	public void setS(Runnable r) {
		this.s = r;
	}

	public Runnable getM() {
		return m;
	}

	public void setM(Runnable m) {
		this.m = m;
	}

	protected class MyTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {

			switch (arg0.getId()) {
			case R.id.myimage1:
				check = 1;
				break;
			case R.id.myimage2:
				check = 2;
				break;
			case R.id.myimage3:
				check = 3;
				break;
			default:
				check = 0;
				break;
			}
			if (arg1.getAction() == MotionEvent.ACTION_DOWN && count > 0) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						arg0);
				arg0.startDrag(data, shadowBuilder, arg0, 0);
				count -= check * 10;
				((TextView) findViewById(R.id.score_view)).setText(Integer
						.toString(count));
				if (count < check * 10)
					arg0.setVisibility(View.INVISIBLE);
				return true;
			} else
				return false;
		}

	}

	private class BoardDragListener implements OnDragListener {

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
				game.setTile(j, i, 1, check - 1);
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

}
