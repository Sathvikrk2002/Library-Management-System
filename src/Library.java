import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Library {
	private Map<Integer, Book> books;
	private Map<Integer, User> users;
	private LibraryDAO libraryDao;

	public Library() {
		libraryDao = new LibraryDAO();
		books = libraryDao.getBooks();
		users = libraryDao.getUsers();
	}

	public void addBook(Book book) {
		books.put(book.getBookId(), book);
		libraryDao.addBook(book);
	}

	public boolean deleteBook(int bookId) {
		Book bookToRemove = books.get(bookId);
		if (bookToRemove != null && bookToRemove.isAvailable()) {
			books.remove(bookId);
			libraryDao.deleteBook(bookId);
			return true;
		}
		return false;
	}

	public List<Book> getAvailableBooks() {
		List<Book> availableBooks = new ArrayList<>();
		for (Book book : books.values()) {
			if (book.isAvailable()) {
				availableBooks.add(book);
			}
		}
		return availableBooks;
	}

	public int getTotalBooks() {
		return books.size();
	}

	public int getTotalIssuedBooks() {
		int issuedBooks = 0;
		for (Book book : books.values()) {
			if (!book.isAvailable()) {
				issuedBooks++;
			}
		}
		return issuedBooks;
	}

	// Add a user to the library
	public void addUser(User user) {
		users.put(user.getId(), user);
		libraryDao.addUser(user);
	}

	// Get a user by their ID
	public User getUserById(int userId) {
		return users.get(userId);
	}

	// Borrow a book for a user
	public boolean borrowBook(int userId, int bookId) {
		User user = getUserById(userId);
		Book book = getBookById(bookId);

		if (user != null && book != null && book.isAvailable() && user.getBorrowedBookIds().size() < 3) {
			user.borrowBook(bookId);
			book.borrow();
			libraryDao.borrowBook(userId, bookId);
			return true;
		}
		return false;
	}

	public Book getBookById(int bookId) {
		return books.get(bookId);
	}

	// Return a book for a user
	public boolean returnBook(int userId, int bookId) {
		User user = getUserById(userId);
		Book book = getBookById(bookId);

		if (user != null && book != null && !book.isAvailable()) {
			user.returnBook(bookId);
			book.returnBook();
			libraryDao.returnBook(userId, bookId);
			return true;
		}

		return false;
	}

	public void editBook(Book bookToEdit) {
		libraryDao.editBook(bookToEdit);
	}
}
