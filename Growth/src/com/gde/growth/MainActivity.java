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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private Handler boardHandler, sproutHandler;
	private Runnable b, s;
	private Thread boardThread, sproutThread;

	protected static GameLogic game;

	int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		game = new GameLogic(2, 20);

		setContentView(R.layout.activity_main);

		boardHandler = new Handler();
		sproutHandler = new Handler();

		count = 0;

		setB(new Runnable() {
			@SuppressLint("NewApi")
			public void run() {
				count++;
				game.boardUpdate();
				findViewById(R.id.boardview).postInvalidate();
				boardHandler.postDelayed(this, 1100);

			}
		});

		setS(new Runnable() {
			public void run() {
				sproutHandler.postDelayed(this, 5000);
				View view = findViewById(R.id.myimage1);
				ImageView view2 = new ImageView(view.getContext());
				view2.setOnTouchListener(new MyTouchListener());
				view2.setImageResource(R.drawable.petal);
				((RelativeLayout) view.getParent()).addView(view2);
			}
		});

		boardThread = new Thread(b);
		sproutThread = new Thread(s);

		findViewById(R.id.boardview).setOnDragListener(new BoardDragListener());
		findViewById(R.id.testlist).setOnDragListener(new SproutDragListener());

		startGame();
	}

	public void startGame() {
		game.setTile(3, 3, 1);
		game.setTile(4, 4, 1);
		game.setTile(4, 6, 1);
		game.setTile(5, 5, 1);
		game.setTile(6, 4, 1);
		game.setTile(6, 6, 1);
		game.setTile(7, 7, 1);

		game.setTile(13, 13, 2);
		game.setTile(14, 14, 2);
		game.setTile(14, 16, 2);
		game.setTile(15, 15, 2);
		game.setTile(16, 14, 2);
		game.setTile(16, 16, 2);
		game.setTile(17, 17, 2);

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

	private class MyTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						arg0);
				arg0.startDrag(data, shadowBuilder, arg0, 0);
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

			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				container.d = true;
				x = event.getX();
				y = event.getY();
				container.x = (int) x;
				container.y = (int) y;
				container.postInvalidate();

				break;
			case DragEvent.ACTION_DRAG_EXITED:
				container.d = false;
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				x = event.getX();
				y = event.getY();
				container.x = (int) x;
				container.y = (int) y;
				container.postInvalidate();
				break;
			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
				container.d = false;
				View view = (View) event.getLocalState();
				ViewGroup owner = (ViewGroup) view.getParent();
				owner.removeView(view);
				x = event.getX();
				y = event.getY();
				int i = (int) (x * game.size / container.width);
				int j = (int) (y * game.size / container.width);
				game.setTile(j, i, 1);
				container.postInvalidate();
				break;
			case DragEvent.ACTION_DRAG_ENDED:
			default:
				break;
			}
			return true;
		}
	}

	class SproutDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {

			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
				View view = (View) event.getLocalState();
				ViewGroup owner = (ViewGroup) view.getParent();
				owner.removeView(view);
				RelativeLayout container = (RelativeLayout) v;
				container.addView(view);
				view.setVisibility(View.VISIBLE);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
			default:
				break;
			}
			return true;
		}
	}
}
