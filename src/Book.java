public class Book {
	private int bookId;
	private String title;
	private String author;
	private String category;
	private boolean isAvailable;

	public Book(int bookId, String title, String author, String category, boolean isAvailable) {
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.category = category;
		this.isAvailable = isAvailable; // New books are available by default
	}

	public int getBookId() {
		return bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	// Method to borrow the book
	public void borrow() {
		if (isAvailable()) {
			isAvailable = false;
		} else {
			System.out.println("The book is already borrowed.");
		}
	}

	// Method to return the book
	public void returnBook() {
		isAvailable = true;
	}

	@Override
	public String toString() {
		return "Book ID: " + bookId + "\nTitle: " + title + "\nAuthor: " + author + "\nCategory: " + category;
	}
}
