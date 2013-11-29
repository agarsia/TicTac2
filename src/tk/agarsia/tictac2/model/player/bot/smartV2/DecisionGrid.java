package tk.agarsia.tictac2.model.player.bot.smartV2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tk.agarsia.tictac2.model.Game;
import tk.agarsia.tictac2.model.board.Board;

public class DecisionGrid {
	private static final int[][][][] PREGRIDS = initGrids();
	private static final int[][][][][] PREWINS = initWins();
	private static Random rand = new Random();

	public static void refactor(int[][] grid, Board board, int mpt, int wLen) {
		ArrayList<int[]> h = board.getHistory();
		
		int[][][] wins = PREWINS[grid.length-3][wLen-3];
		
		//use rules for other player history
		for(int i = h.size()-mpt; i < h.size(); i++) {
			int mark = h.get(i)[0];
			int x = h.get(i)[1];
			int y = h.get(i)[2];
			
			System.out.println(Arrays.toString(h.get(i)));
			
			//rule b. (set other players fields to 0)
			grid[x][y] = 0;
			
			//rule c. (increment all affected fields)
			
			
			
			
			
		}
	}

	
	public static int[] decide(int[][] grid, Board board) {
		int[][] out = new int[board.getFreeFieldCount()][3];
		int sameCount = 0;
		
		for(int x = 0; x < grid.length; x++) {
			for(int y = 0; y < grid[x].length; y++) {
				if(board.getField(x, y).isFree())
					if(out[0][0]<grid[x][y]) {
						out[0][0] = grid[x][y];
						out[0][1] = x;
						out[0][2] = y;
						sameCount = 0;
					} else if(out[0][0]==grid[x][y]) {
						if(rand.nextInt(1)==0) {
							out[++sameCount][0] = grid[x][y];
							out[sameCount][1] = x;
							out[sameCount][2] = y;
						}
					}
			}
		}
		
		if(sameCount==0)
			return out[0];
		return out[rand.nextInt(sameCount)];
	}
	
	public static int[][] get(Game game) {
		return PREGRIDS[game.getBoardDim()-3][game.getWinLength()-3];
	}
	
	public static void print(int[][] grid) {
		for(int[] row : grid) {
			for(int field : row) {
				System.out.printf(" %04d ",field);
			}
			System.out.println();
		}
	}
	
	public static int[][][][] initGrids() {
		int[][][][] grids = new int[4][][][];
		
		//3x3
		grids[0] = new int[1][][];
		grids[0][0] = new int[][] {{3,2,3},{2,4,2},{3,2,3}};
		
		//4x4s
		grids[1] = new int[2][][];
		grids[1][0] = new int[][] {{3,4,4,3},{4,7,7,4},{4,7,7,4},{3,4,4,3}};
		grids[1][1] = new int[][] {{3,2,2,3},{2,3,3,2},{2,3,3,2},{3,2,2,3}};
		
		//5x5s
		grids[2] = new int[3][][];
		grids[2][0] = new int[][] {{3,4,6,4,3},{4,7,9,7,4},{6,9,12,9,6},{4,7,9,7,4},{3,4,6,4,3}};
		grids[2][1] = new int[][] {{3,4,3,4,3},{4,6,6,6,4},{3,6,8,6,3},{4,6,6,6,4},{3,4,3,4,3}};
		grids[2][2] = new int[][] {{3,2,2,2,3},{2,3,2,3,2},{2,2,4,2,2},{2,3,2,3,2},{3,2,2,2,3}};
		
		//6x6s
		grids[3] = new int[4][][];
		grids[3][0] = new int[][] {{3,4,6,6,4,3},{4,7,8,8,7,4},{6,8,12,12,8,6},{6,8,12,12,8,6},{4,7,8,8,7,4},{3,4,6,6,4,3}};
		grids[3][1] = new int[][] {{3,4,5,5,4,3},{4,6,7,7,6,4},{5,7,9,9,7,5},{5,7,9,9,7,5},{4,6,7,7,6,4},{3,4,5,5,4,3}};
		grids[3][2] = new int[][] {{3,4,3,3,4,3},{4,6,5,5,6,4},{3,5,7,7,5,3},{3,5,7,7,5,3},{4,6,5,5,6,4},{3,4,3,3,4,3}};
		grids[3][3] = new int[][] {{3,2,2,2,2,3},{2,3,2,2,3,2},{2,2,3,3,2,2},{2,2,3,3,2,2},{2,3,2,2,3,2},{3,2,2,2,2,3}};

		return grids;
	}
	
	public static int[][][][][] initWins() {
		int[][][][][] wins = new int[4][][][][];
		
		//3x3
		wins[0] = new int[1][][][];
		wins[0][0] = new int[8][3][2];
		wins[0][0][0] = new int[][] {{0,0},{0,1},{0,2}};
		wins[0][0][1] = new int[][] {{1,0},{1,1},{1,2}};
		wins[0][0][2] = new int[][] {{2,0},{2,1},{2,2}};
		wins[0][0][3] = new int[][] {{0,0},{1,0},{2,0}};
		wins[0][0][4] = new int[][] {{0,1},{1,1},{2,1}};
		wins[0][0][5] = new int[][] {{0,2},{1,2},{2,2}};
		wins[0][0][6] = new int[][] {{0,0},{1,1},{2,2}};
		wins[0][0][7] = new int[][] {{0,2},{1,1},{2,0}};

		return wins;
	}
}