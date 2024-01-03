
public class Square {
	private boolean filled = false;
	private char value = ' ';
	private int location;

	public Square(int location) {
		this.location = location;
	}

	public void setValue(char c) {
		if (this.filled) {
			throw new IllegalArgumentException("*That square has already been filled. Please select another.*");
			// caught and then re-thrown by board setValue, caught by game input
		}
		
		this.value = c;
		this.filled = true;
	}

	public char getValue() {
		return this.value;
	}

	public int getLocation() {
		return this.location;
	}

	public boolean isFilled() {
		return this.filled;
	}

	public String[] toStringArray() {
		return this.toString().split("\n");
	}

	public String toString() {
		return "" + this.value;
	}
}
