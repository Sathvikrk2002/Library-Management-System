import java.util.ArrayList;
import java.util.List;

public class User {
	private int id;
	private String username;
	private String password;
	private List<Integer> borrowedBookIds;

	public User(int id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.borrowedBookIds = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<Integer> getBorrowedBookIds() {
		return borrowedBookIds;
	}

	public void setBorrowedBookIds(List<Integer> bookIds) {
		this.borrowedBookIds = bookIds;
	}

	public void borrowBook(int bookId) {
		borrowedBookIds.add(bookId);
	}

	public void returnBook(int bookId) {
		borrowedBookIds.remove(Integer.valueOf(bookId));
	}

	@Override
	public String toString() {
		return "User ID: " + id + "\nUsername: " + username + "\nBorrowed Books: " + borrowedBookIds.size();
	}
}
