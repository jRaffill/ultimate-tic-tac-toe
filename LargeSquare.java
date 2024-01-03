
public class LargeSquare extends Square {

	// contains all instance variables of square (filled, value, location) plus a board
	private Board innerBoard;
	
	// initialize board as a reg. square Board
	public LargeSquare(int location) {
		super(location);
		innerBoard = new Board(false);
	}
	
	// copy constructor (for AI)
	public LargeSquare(int location, Board innerBoard) {
		super(location);
		this.innerBoard = new Board(innerBoard, false);
	}

	public Board getBoard() {
		if (super.isFilled()) {
			return null;
		}
		return this.innerBoard;
	}

	@Override
	public String toString() {
		// if the square has been filled, return ascii art based on value
		if (super.isFilled()) {
			if (super.getValue() == 'X') {
				return "             \n"
						+ "   __  __    \n"
						+ "   \\ \\/ /    \n"
						+ "    \\  /     \n"
						+ "    /  \\     \n"
						+ "   /_/\\_\\    \n"
						+ "             ";
			}
			else if (super.getValue() == 'O') {
				return "             \n"
					+ "     ___     \n"
					+ "    / _ \\    \n"
					+ "   | | | |   \n"
					+ "   | |_| |   \n"
					+ "    \\___/    \n"
					+ "             \n";
			}
			// if the board has been drawn, square will be set to filled but we still want to print the board
			return this.innerBoard.toString();
		}
		return this.innerBoard.toString();
	}
}
