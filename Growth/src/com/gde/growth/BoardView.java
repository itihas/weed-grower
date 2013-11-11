package com.gde.growth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

public class BoardView extends View {
	// TODO:gridlines, array, bitmaps

	private ShapeDrawable board, tile;

	int width = getWidth();
	int height = getHeight();
	
	public int x, y;

	private Paint paint, one, note;

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

		one = new Paint();
		one.setColor(0x7799BBAA);
		one.setStyle(Style.FILL);

		note = new Paint();
		note.setColor(0xffFF0000);
		note.setStyle(Style.FILL_AND_STROKE);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		width = getMeasuredWidth();
		height = getMeasuredHeight();

		board.setBounds(0, 0, width, width);

	}

	public void setRunning(boolean b) {
		running = b;
	}

	protected void drawTiles(Canvas canvas) {
		int t[][] = (int[][]) this.getTag(R.id.boardview);

		tile.getPaint().set(one);

		for (int i = 0; i < 10; i++) {

			for (int j = 0; j < 10; j++) {

				if (t[j][i] == 1) {

					tile.setBounds((i * width) / 10, (j * width) / 10,
							((i + 1) * width) / 10, ((j + 1) * width) / 10);

					tile.draw(canvas);

				}
//				canvas.drawText(Integer.toString(t[j][i]), i * width	/ 10, j * width / 10, note);

			}
		}

	}

	protected void onDraw(Canvas canvas) {

		board.draw(canvas);

		for (int i = 0; i <= 10; i++) {
			canvas.drawLine((width * i) / 10, 0, (width * i) / 10, width, paint);
			canvas.drawLine(0, (width * i) / 10, width, (width * i) / 10, paint);
		}
		
		canvas.drawText(Integer.toString(x)+" "+Integer.toString(y)+" "+Integer.toString(width), width/2, width/2, note);
		
		drawTiles(canvas);

	}

}
