package com.gde.growth;

public class GameLogic {

	protected int size;
	protected int tiles[][];
	protected int players;

	public GameLogic(int p, int s) {

		size = s;
		tiles = new int[s][s];
		players = p;

	}

	public void setTile(int i, int j, int val) {

		tiles[i][j] = val;
	}

	public int[][] getTiles() {
		return tiles;
	}


	public void placeSprout(int i, int j, int val) {
		tiles[i][j] = 1;
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
