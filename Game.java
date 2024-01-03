public class Game {
	private final String instructions =
			  "╒══════════════════════════════════════════════════════════╕\n"
			+ "│   _   _           _                  _             ___   │\n"
			+ "│  | |_(_) ___     | |_ __ _  ___     | |_ ___   ___|_  )  │\n"
			+ "│  | __| |/ __|____| __/ _` |/ __|____| __/ _ \\ / _ \\/ /   │\n"
			+ "│  | |_| | (_|_____| || (_| | (_|_____| || (_) |  __/___|  │\n"
			+ "│   \\__|_|\\___|     \\__\\__,_|\\___|     \\__\\___/ \\___|      │\n"
			+ "╘══════════════════════════════════════════════════════════╛\n"
			+ "\n"
			+ "Welcome to ULTIMATE tic-tac-toe! In this game, the board is\n"
			+ "made up of a 3x3 grid, each square of which is its own 3x3\n"
			+ "grid. As the human player, you get to go first. You first\n"
			+ "select which board (larger square) to play in by selecting\n"
			+ "a number 1 through 9:\n"
			+ "╔═══╤═══╤═══╗\n"
			+ "║ 1 │ 2 │ 3 ║\n"
			+ "╟───┼───┼───╢\n"
			+ "║ 4 │ 5 │ 6 ║\n"
			+ "╟───┼───┼───╢\n"
			+ "║ 7 │ 8 │ 9 ║\n"
			+ "╚═══╧═══╧═══╝\n"
			+ "Then, you select the smaller square within that board.\n"
			+ "The square you pick determines the board where the opponent\n"
			+ "plays. For example, if you picked Board 5, Square 9, then the\n"
			+ "AI would have to play their next move in Board 9. If they\n"
			+ "picked Square 7 in that board, then you would choose a\n"
			+ "square in Board 7 for your move. (A simple demo video is\n"
			+ "attached which explains this visually).\n\n"
			+ "When someone gets 3 in a row of any board, they have “won”\n"
			+ "that board. This means that the whole board is replaced\n"
			+ "with a giant X or O, and their opponent gets to pick any\n"
			+ "board to play in for their next move. If you get sent to a\n"
			+ "“won” board, you get to choose which board to play in for\n"
			+ "your next move. If you get sent to a board with no open\n"
			+ "spaces left, you also get to pick any board to play in.\n"
			+ "If you “win” three boards in a row, you win the game.\n\n"
			+ "If possible, change your console settings to display up and \n"
			+ "down -- the game displays a LOT of text vertically!\n\n"
			+ "Type “help” at any point to see this explanation again.\n"
			+ "Or type “map” for a reminder of which number corresponds\n"
			+ "to which square.\n";

	private final String map =
			"╔═══╤═══╤═══╗\n"
			+ "║ 1 │ 2 │ 3 ║\n"
			+ "╟───┼───┼───╢\n"
			+ "║ 4 │ 5 │ 6 ║\n"
			+ "╟───┼───┼───╢\n"
			+ "║ 7 │ 8 │ 9 ║\n"
			+ "╚═══╧═══╧═══╝\n";


	private Board board = new Board(true);
	private int boardNumber = 10;
	private boolean gameWon = false;
	private int gamesPlayed = 0;
	private int gamesWon = 0;
	private Opponent o = new Opponent();
	public static final int NO_BOARD = 10;

	public void startGame() {
		System.out.println(this.instructions);
		System.out.println("Press enter to begin.");
		TextIO.getln();
		while (!gameWon) {
			playTurn();
		}
		System.out.println(board);
		System.out.println("You have won " + gamesWon + " out of the " + gamesPlayed + " games you have played.");
		System.out.print("Would you like to play again? (y/n) > ");
		boolean playAgain = TextIO.getlnBoolean();
		// initialize and start new game
		if (playAgain) {
			this.board = new Board(true);
			this.boardNumber = NO_BOARD;
			this.gameWon = false;
			this.startGame();
		}
	}

	private void playTurn() {
		System.out.println("------------------------------------------------------------");
		System.out.println(board);
		// if the current board has been won, set the board to invalid
		if (boardNumber < 9 && board.getSquare(boardNumber).isFilled()) {
			boardNumber = NO_BOARD;
		}
		// get a valid current board
		while (boardNumber > 9) {
			System.out.println("Which board would you like to play in?");
			boardNumber = getInput("Select a board from 1-9. > ");
			if (boardNumber < 9 && board.getSquare(boardNumber).isFilled()) {
				boardNumber = NO_BOARD;
			}
		}
		// get a valid current square
		try {
			System.out.println("Current board: " + (boardNumber + 1));
			System.out.println("Which square would you like to play in?");
			int squareNumber = getInput("Select a square from 1-9. > ");
			board.setValue(boardNumber, squareNumber, 'X');
			boardNumber = squareNumber;
		} catch (IllegalArgumentException e) {
			System.out.println(e.getLocalizedMessage());
			return;
		}
		// check if there are updates to game state
		this.checkWins();
		if (this.gameWon) {
			return;
		}
		// opponent's turn
		boardNumber = o.playTurn(board, boardNumber);
		this.checkWins();
	}
	
	private int getInput(String instruction) {
		// get input or help or map
		System.out.print(instruction);
		String input = TextIO.getlnString();
		if (input.equals("help")) {
			System.out.println(this.instructions);
			System.out.println(this.board);
			return getInput(instruction);
		}
		else if (input.equals("map")) {
			System.out.println(this.map);
			System.out.println(this.board);
			return getInput(instruction);
		}
		else if (input.equals("")) {
			return getInput(instruction);
		}
		int resultAsInt = Integer.parseInt(input) - 1;		
		return resultAsInt;
	}

	private void checkWins() {
		// check each smaller board for three in a rows/draws
		for (int i = 0; i < 9; i++) {
			try {
				if (board.threeInARow(i, 'X')) {
					System.out.println("You got a three in a row in board " + (i + 1) + "!");
					board.getSquare(i).setValue('X');
					// let the opponent pick a new board after you win a board
					this.boardNumber = NO_BOARD;
				}
				else if (board.threeInARow(i, 'O')) {
					System.out.println("Your opponent got a three in a row in board " + (i + 1) + ".");
					board.getSquare(i).setValue('O');
					this.boardNumber = NO_BOARD;
				}
				else if (board.getValidMoves(i).length == 0) {
					System.out.println("The board " + (i + 1) + " has been drawn.");
					board.getSquare(i).setValue('Z');
					// don't want to allow opponent to pick a new board after you draw a board
				}
			}
			catch (NullPointerException e) {
				continue;
			}
		}
		if (board.threeInARow('X')) {
			System.out.println("You won the game!");
			this.gameWon = true;
			// setting gamewon to true will lead to end of game loop and end-game state
			this.gamesPlayed++;
			this.gamesWon++;
		}
		else if (board.threeInARow('O')) {
			System.out.println("Your opponent won the game.");
			this.gameWon = true;
			// setting gamewon to true will lead to end of game loop and end-game state
			this.gamesPlayed++;
		}
		else if (board.getValidMoves().length == 0) {
			System.out.println("The game ended in a draw.");
			this.gameWon = true;
			// setting gamewon to true will lead to end of game loop and end-game state
			this.gamesPlayed++;
		}
	}
}
