import java.util.Timer;

import java.util.TimerTask;
public class Opponent {
	private boolean thinking;
	
	public int playTurn(Board currentBoard, int boardNumber) {
		thinking = true;
		System.out.print("Your opponent is thinking..");
		// create a timer task to print dots while opponent is thinking
		Timer t = new Timer();
		TimerTask tt = new TimerTask() {
			public void run() {
				if (thinking) {
					System.out.print(".");
				}
				else {
					t.cancel();
					t.purge();
				}
			}
		};
		t.scheduleAtFixedRate(tt, 0, 1000);
		
		// if board has been filled, then deselect it
		if (boardNumber < 9 && currentBoard.getSquare(boardNumber).isFilled()) {
			boardNumber = Game.NO_BOARD;
		}
		// if no board is selected, pick a board and end the timertask
		// (not starting it again bc then the dots would print on a newline instead of next to the "thinking..."
		if (boardNumber > 9) {
			int boardChoice = chooseBoard(currentBoard);
			boardNumber = boardChoice;
			thinking = false;
			System.out.println();
			System.out.print("Your opponent chose to play in board " + (boardNumber + 1) + ".");
		}
		// pick square and end the timertask
		int squareChoice = chooseSquare(currentBoard, boardNumber);
		currentBoard.setValue(boardNumber, squareChoice, 'O');
		thinking = false;
		System.out.println();
		System.out.println("Your opponent chose to play in square " + (squareChoice + 1) + ".");
		// return the square (because it will have to become currentboard)
		return squareChoice;
	}
	
	private int simulatePlaythrough (Board currentBoard, int boardNumber, char nextToPlay) {
		// copy board (to avoid messing up pointer stuff)
		Board newBoard = new Board(currentBoard, true);
		// simulate a randum turn
		int[] turn = simulateTurn(newBoard, boardNumber);
		newBoard.setValue(turn[0], turn[1], nextToPlay);
		// check sub-square wins, if any then set currentBoard 
		for (int i = 0; i < 9; i++) {
			try {
				if (newBoard.threeInARow(i, 'X')) {
					newBoard.getSquare(i).setValue('X');
					boardNumber = Game.NO_BOARD;
				}
				else if (newBoard.threeInARow(i, 'O')) {
					newBoard.getSquare(i).setValue('O');
					boardNumber = Game.NO_BOARD;
				}
				else if (newBoard.getValidMoves(i).length == 0) {
					newBoard.getSquare(i).setValue('Z');
				}
			}
			catch (NullPointerException e) {
				// if the square has already been won, then checks on its board will return null
				continue;
			}
		}
		
		// check overall wins (base case for recursion)
		// (one of these MUST eventually happen (either someone wins or the whole board is filled up)
		if (newBoard.threeInARow('X')) {
			// O lost so move score should decrease
			return -1;
		}
		else if (newBoard.threeInARow('O')) {
			// O won so move score should increase
			return 1;
		}
		else if (newBoard.getValidMoves().length == 0) {
			// tie so move score is unchanged
			return 0;
		}
		else {
			if (nextToPlay == 'X') {
				// keep simulating
				return simulatePlaythrough(newBoard, turn[1], 'O');
			}
			else {
				// keep simulating
				return simulatePlaythrough(newBoard, turn[1], 'X');
			}
		}
	}
	
	private int[] simulateTurn (Board currentBoard, int boardNumber) {
		// if board is filled, deselect it
		if (boardNumber < 9 && currentBoard.getSquare(boardNumber).isFilled()) {
			boardNumber = Game.NO_BOARD;
		}
		// if no board selected, get a random board
		if (boardNumber > 9) {
			int[] boardChoices = currentBoard.getValidMoves();
			int boardChoice = getRandomMove(boardChoices);
			boardNumber = boardChoice;
		}
		// get a random move
		int[] squareChoices = currentBoard.getValidMoves(boardNumber);
		int squareChoice = getRandomMove(squareChoices);
		// return full move
		return new int[]{boardNumber, squareChoice};
	}
	
	private int chooseSquare(Board currentBoard, int boardNumber) {
		int[] possibleMoves = currentBoard.getValidMoves(boardNumber);
		int bestMove = 0;
		// starting at the lowest possible score
		int highestScore = -1500;
		// for each possible move
		for (int i = 0; i < possibleMoves.length; i++) {
			// try playing that move
			Board newBoard = new Board(currentBoard, true);
			int move = possibleMoves[i];
			newBoard.setValue(boardNumber, move, 'O');
			// get score by simulating 1000 games and seeing how many are won
			int moveScore = getMoveScore(1500, newBoard, move, 'X');
			if (moveScore > highestScore) {
				bestMove = move;
				highestScore = moveScore;
			}
		}
		return bestMove;
	}
	
	// simulate a bunch of games and add/subtract the result (-1, 1, or 0)
	private int getMoveScore(int numRuns, Board board, int boardNumber, char nextToPlay) {
		int score = 0;
		for (int j = 0; j < numRuns; j++) {
			score += simulatePlaythrough(board, boardNumber, nextToPlay);
		}
		return score;
	}
	
	private int chooseBoard(Board currentBoard) {
		int[] possibleBoards = currentBoard.getValidMoves();
		int bestBoard = 0;
		// start at lowest possible score (running less sims. for board because it already takes so long)
		int highestScore = -1000;
		// for each possible board 
		for (int i = 0; i < possibleBoards.length; i++) {
			int boardChoice = possibleBoards[i];
			// find the winningest move in that board
			int bestMove = chooseSquare(currentBoard, boardChoice);
			// try playing that move
			Board newBoard = new Board(currentBoard, true);
			newBoard.setValue(boardChoice, bestMove, 'O');
			// find score for that move and compare
			int boardScore = getMoveScore(1000, newBoard, bestMove, 'X');
			if (boardScore > highestScore) {
				bestBoard = boardChoice;
				highestScore = boardScore;
			}
		}
		return bestBoard;
	}

	private int getRandomMove(int[] possibleMoves) {
		int numMoves = possibleMoves.length;
		int move = (int)(Math.random() * numMoves);
		return possibleMoves[move];
	}
}
