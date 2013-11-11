package com.gde.growth;

public class GameLogic {

	protected int tiles[][];
	protected int players;
	protected int sprouts;

	public GameLogic(int p) {

		tiles = new int[10][10];
		players = p;
		sprouts = 2;

	}

	public void setTile(int i, int j, int val) {

		tiles[i][j] = val;
	}

	public int[][] getTiles() {
		return tiles;
	}

	public void sproutUpdate(){
		sprouts++;
	}

	public void placeSprout(int i, int j){
		sprouts--;
		tiles[i][j] = 1;
	}

	public void boardUpdate() {

		int newTiles[][] = new int[10][10];
		int life;

		for (int i = 0; i < 10; i++) {

			for (int j = 0; j < 10; j++) {

				life = 0;

				for(int k = -1; k <= 1; k++){
					for(int l = -1; l <= 1; l++){
						if((i + k >= 0) && (j + l >= 0) && (i + k < 10) && (j + l < 10) && !(k==0 && l==0))
							life += tiles[i + k][j + l];
					}
				}
/*				
				if (i > 0 && j > 0)
					life += tiles[i - 1][j - 1];
				if (i > 0) {
					life += tiles[i - 1][j];
					if (j < 9)
						life += tiles[i - 1][j + 1];
				}
				if (j > 0) {
					life += tiles[i][j - 1];
					if (i < 9)
						life += tiles[i + 1][j - 1];
				}
				if (j < 9)
					life += tiles[i][j + 1];
				if (i < 9)
					life += tiles[i + 1][j];
				if (i < 9 && j < 9)
					life += tiles[i + 1][j + 1];
*/
				
				
				
				
				if (life < 1 | life > 3)
					newTiles[i][j] = 0;
				else if (life == 3)
					newTiles[i][j] = 1;
				else if(tiles[i][j]==1 && (life==2 | life==3))
					newTiles[i][j] = 1;
			}
		}

		tiles = newTiles;

	}
}
