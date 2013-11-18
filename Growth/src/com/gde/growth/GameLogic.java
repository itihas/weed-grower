package com.gde.growth;

import java.util.ArrayList;

import android.graphics.Point;

public class GameLogic {

	protected int size;
	protected int tiles[][];
	protected int players;
	protected int interference;

	protected ArrayList<Point[]> patterns = new ArrayList<Point[]>();

	public GameLogic(int p, int s, int a) {

		size = s;
		tiles = new int[s][s];
		players = p;
		interference = a;

		Point[] init = { new Point(0, 0) };

		patterns.add(init);

		Point[] init2 = { new Point(0, 0), new Point(1, -1), new Point(-1, -1),
				new Point(-1, 1), new Point(1, 1) };
		patterns.add(init2);

		Point[] init3 = { new Point(0, 0), new Point(-1, -1), new Point(1, 1) };
		patterns.add(init3);

	}

	public void setTile(int i, int j, int val, int pat) {

		Point[] pattern = patterns.get(pat);
		for (int k = 0; k < pattern.length; k++) {
			if ((i + pattern[k].x >= 0) && (i + pattern[k].x < size)
					&& (j + pattern[k].y >= 0) && (j + pattern[k].y < size))
				if (tiles[i + pattern[k].x][j + pattern[k].y] == 0 && interference==0)
					tiles[i + pattern[k].x][j + pattern[k].y] = val;

		}
	}

	public int[][] getTiles() {
		return tiles;
	}

	public void boardUpdate() {

		int newTiles[][] = new int[size][size];
		int life[];

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {

				life = new int[players + 1];

				for (int k = -1; k <= 1; k++) {
					for (int l = -1; l <= 1; l++) {
						if ((i + k >= 0) && (j + l >= 0) && (i + k < size)
								&& (j + l < size) && !(k == 0 && l == 0))
							life[tiles[i + k][j + l]]++;
					}
				}

				if (tiles[i][j] > 0) {
					newTiles[i][j] = tiles[i][j];

					if (life[tiles[i][j]] < 1 | life[tiles[i][j]] > 4)
						newTiles[i][j] = 0;

				} else {
					for (int k = 1; k <= players; k++) {
						if (interference == 0)
							if (life[k] == 3)
								if (newTiles[i][j] > 0)
									newTiles[i][j] = 0;
								else
									newTiles[i][j] = k;
					}
				}
			}
		}

		tiles = newTiles;

	}
}
