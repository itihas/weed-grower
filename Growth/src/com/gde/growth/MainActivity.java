package com.gde.growth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	private Handler boardHandler, sproutHandler;
	private Runnable b, s;
	private Thread boardThread, sproutThread;

	protected static GameLogic game;

	int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		game = new GameLogic(1);

		setContentView(R.layout.activity_main);

		boardHandler = new Handler();
		sproutHandler = new Handler();

		count = 0;

		setB(new Runnable() {
			@SuppressLint("NewApi")
			public void run() {
				count++;
				findViewById(R.id.boardview).setTag(R.id.boardview, game.tiles);
				findViewById(R.id.boardview).postInvalidate();
				boardHandler.postDelayed(this, 1100);
				game.boardUpdate();

			}
		});

		setS(new Runnable() {
			public void run() {
				sproutHandler.postDelayed(this, 3000);
				game.sproutUpdate();
			}
		});

		boardThread = new Thread(b);
		sproutThread = new Thread(s);

		findViewById(R.id.myimage1).setOnTouchListener(new MyTouchListener());
		findViewById(R.id.boardview).setOnDragListener(new BoardDragListener());
		findViewById(R.id.testlist).setOnDragListener(new SproutDragListener());

		startGame();
	}

	public void startGame() {
		game.setTile(4, 4, 1);
		game.setTile(4, 5, 1);
		game.setTile(4, 3, 1);

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

			int action = event.getAction();

			BoardView container = (BoardView) v;

			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				x = event.getX();
				y = event.getY();
				container.x = (int) x;
				container.y = (int) y;
				
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				x = event.getX();
				y = event.getY();
				container.x = (int) x;
				container.y = (int) y;
				break;
			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
				View view = (View) event.getLocalState();
				ViewGroup owner = (ViewGroup) view.getParent();
				owner.removeView(view);
				x = event.getX();
				y = event.getY();
				int i = (int) (x * 10 /container.width);
				int j = (int) (y * 10 /container.width);
				game.setTile(10 - i, 10 - j, 1);
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
		      int action = event.getAction();
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
		        LinearLayout container = (LinearLayout) v;
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
