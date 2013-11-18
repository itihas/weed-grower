package com.gde.growth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

public class BoardView extends View {
	// TODO:gridlines, array, bitmaps

	private ShapeDrawable board, tile;

	int width = getWidth();
	int height = getHeight();
	int size;

	public int x, y;
	public int d;

	private Paint paint, one, two, note;

	protected boolean running = false;

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);

		board = new ShapeDrawable(new RectShape());
		board.getPaint().setColor(0xff444444);

		tile = new ShapeDrawable(new RectShape());

		paint = new Paint();
		paint.setColor(0xff888888);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setStrokeWidth(3);

		setOne(new Paint());
		getOne().setColor(0x7700BBAA);
		getOne().setStyle(Style.FILL);

		two = new Paint();
		two.setColor(0x77AA00BB);
		two.setStyle(Style.FILL);

		note = new Paint();
		note.setColor(0xAAFF0000);
		note.setStyle(Style.FILL_AND_STROKE);

		size = MainActivity.game.size;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		width = getMeasuredWidth();
		height = getMeasuredHeight();

		board.setBounds(0, 0, width, width);

	}

	protected Drawable getTile() {
		tile.setBounds(0, 0, width / size, width / size);
		tile.getPaint().set(getOne());
		return tile;
	}

	public void setRunning(boolean b) {
		running = b;
	}

	public void ghostTile(Canvas canvas, int i, int j, int pat) {
		tile.getPaint().set(note);
		Point[] pattern = MainActivity.game.patterns.get(pat);
		for (int k = 0; k < pattern.length; k++) {
			if ((i + pattern[k].x >= 0) && (i + pattern[k].x < size)
					&& (j + pattern[k].y >= 0) && (j + pattern[k].y < size))
				if (MainActivity.game.tiles[i + pattern[k].x][j + pattern[k].y] == 0
						&& MainActivity.game.interference == 0) {

					tile.setBounds(((i + pattern[k].x) * width) / size,
							((j + pattern[k].y) * width) / size, ((i
									+ pattern[k].x + 1) * width)
									/ size, ((j + pattern[k].y + 1) * width)
									/ size);

					tile.draw(canvas);
				}
		}
	}

	protected void drawTiles(Canvas canvas) {
		int t[][] = MainActivity.game.tiles;

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {

				if (t[j][i] == 1) {

					tile.getPaint().set(getOne());

					tile.setBounds((i * width) / size, (j * width) / size,
							((i + 1) * width) / size, ((j + 1) * width) / size);

					tile.draw(canvas);

				}

				if (t[j][i] == 2) {

					tile.getPaint().set(two);

					tile.setBounds((i * width) / size, (j * width) / size,
							((i + 1) * width) / size, ((j + 1) * width) / size);

					tile.draw(canvas);

				}
				// canvas.drawText(Integer.toString(t[j][i]), i * width / 10, j
				// * width / 10, note);

			}
		}

	}

	protected void onDraw(Canvas canvas) {

		board.draw(canvas);

		for (int i = 0; i <= size; i++) {
			canvas.drawLine((width * i) / size, 0, (width * i) / size, width,
					paint);
			canvas.drawLine(0, (width * i) / size, width, (width * i) / size,
					paint);
		}

		canvas.drawText(Integer.toString(x) + " " + Integer.toString(y) + " "
				+ Integer.toString(width), width / 2, width / 2, note);

		drawTiles(canvas);
		if (d != 0)
			ghostTile(canvas, x * size / width, y * size / width, d - 1);

	}

	public Paint getOne() {
		return one;
	}

	public void setOne(Paint one) {
		this.one = one;
	}

}
