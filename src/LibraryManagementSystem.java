import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
	private Library library;
	private Scanner scanner;

	public LibraryManagementSystem() {
		library = new Library();
		scanner = new Scanner(System.in);
	}

	public void run() {
		while (true) {
			System.out.println("Library Management System");
			System.out.println("1. User Login");
			System.out.println("2. Admin Login");
			System.out.println("3. Exit");
			System.out.print("Select an option: ");

			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				userLogin();
				break;
			case 2:
				adminLogin();
				break;
			case 3:
				System.out.println("Exiting the Library Management System.");
				scanner.close();
				System.exit(0);
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private void userLogin() {
		System.out.print("\nEnter User ID: ");
		int userId = scanner.nextInt();
		System.out.print("Enter Password: ");
		String password = scanner.next();

		User user = library.getUserById(userId);

		if (user != null && user.getPassword().equals(password)) {
			System.out.println("\nWelcome, " + user.getUsername() + "!");
			userOptions(user);
		} else {
			System.out.println("\nInvalid User ID or Password. Please try again.\n");
		}
	}

	private void userOptions(User user) {
		while (true) {
			System.out.println("User Options:");
			System.out.println("1. Borrow a Book");
			System.out.println("2. Return a Book");
			System.out.println("3. View Borrowed Books");
			System.out.println("4. View Available Books");
			System.out.println("5. Logout");
			System.out.print("Select an option: ");

			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				borrowBook(user);
				break;
			case 2:
				returnBook(user);
				break;
			case 3:
				viewBorrowedBooks(user);
				break;
			case 4:
				List<Book> availableBooks = library.getAvailableBooks();
				System.out.println("\nList of Available Books:");
				System.out.println("---------------------------");
				for (Book book : availableBooks) {
					System.out.println(book.toString());
					System.out.println("-------------------");
				}
				break;
			case 5:
				System.out.println("\nLogging out...\n");
				return;
			default:
				System.out.println("\nInvalid option. Please try again.\n");
			}
		}
	}

	private void borrowBook(User user) {
		System.out.print("\nEnter the Book ID to borrow: ");
		int bookIdToBorrow = scanner.nextInt();

		if (library.borrowBook(user.getId(), bookIdToBorrow)) {
			System.out.println("\nBook borrowed successfully!!\n");
		} else {
			System.out.println(
					"\nFailed to borrow the book. You can not borrow more than 3 books. Check the book ID and availability.\n");
		}
	}

	private void returnBook(User user) {
		System.out.print("\nEnter the Book ID to return: ");
		int bookIdToReturn = scanner.nextInt();

		if (user.getBorrowedBookIds().contains(bookIdToReturn) && library.returnBook(user.getId(), bookIdToReturn)) {
			System.out.println("\nBook returned successfully.\n");
		} else {
			System.out.println("\nFailed to return the book. Check the book ID and your borrowed books.\n");
		}
	}

	private void viewBorrowedBooks(User user) {
		List<Integer> borrowedBookIds = user.getBorrowedBookIds();
		if (borrowedBookIds.isEmpty()) {
			System.out.println("\nYou haven't borrowed any books yet.\n");
		} else {
			System.out.println("\nYour Borrowed Books:");
			System.out.println("-------------------");
			for (int bookId : borrowedBookIds) {
				Book borrowedBook = library.getBookById(bookId);
				if (borrowedBook != null) {
					System.out.println(borrowedBook.toString());
					System.out.println("-------------------");
				}
			}
			System.out.println();
		}
	}

	private void adminLogin() {
		System.out.print("\nEnter Admin ID: ");
		int adminId = scanner.nextInt();
		System.out.print("Enter Password: ");
		String password = scanner.next();

		if (isAdminAuthenticated(adminId, password)) {
			System.out.println("\nAdmin Login Successful.\n");
			adminOptions();
		} else {
			System.out.println("\nInvalid Admin ID or Password. Please try again.\n");
		}
	}

	private boolean isAdminAuthenticated(int adminId, String password) {
		return (adminId == 420840 && password.equals("admin123"));
	}

	private void adminOptions() {
		while (true) {
			System.out.println("Admin Options:");
			System.out.println("1. Add Book");
			System.out.println("2. Add User");
			System.out.println("3. Manage Books");
			System.out.println("4. View Reports");
			System.out.println("5. Logout");
			System.out.print("Select an option: ");

			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				addBook();
				break;
			case 2:
				addUser();
				break;
			case 3:
				manageBooks();
				break;
			case 4:
				viewReports();
				break;
			case 5:
				System.out.println("\nLogging out...\n");
				return;
			default:
				System.out.println("\nInvalid option. Please try again.\n");
			}
		}
	}

	private void addBook() {
		scanner.nextLine(); // Consume the newline character
		System.out.print("\nEnter the title of the new book: ");
		String title = scanner.nextLine();

		System.out.print("Enter the author of the new book: ");
		String author = scanner.nextLine();

		System.out.print("Enter the category of the new book: ");
		String category = scanner.nextLine();

		if (!title.isEmpty() && !author.isEmpty() && !category.isEmpty()) {
			Book newBook = new Book(generateUniqueBookId(), title, author, category, Boolean.TRUE);
			library.addBook(newBook);
			System.out.println("\nNew book added successfully.\n");
		} else {
			System.out.println("\nInvalid Details Provided. Returning to admin menu.\n");
		}
	}

	private void addUser() {
		System.out.print("\nEnter User ID: ");
		int userId = scanner.nextInt();
		System.out.print("Enter User Name: ");
		String name = scanner.next();
		System.out.print("Enter new Password: ");
		String password = scanner.next();
		if (userId > 0 && !name.isEmpty() && !password.isEmpty()) {
			library.addUser(new User(userId, name, password));
			System.out.println("\nNew User Added Successfully.\n");
		} else {
			System.out.println("\nInvalid User Details Provided. Returning to admin menu.\n");
		}
	}

	private void manageBooks() {
		System.out.println("Manage Books:");
		System.out.println("1. Edit Book Details");
		System.out.println("2. Delete Book");
		System.out.print("Select an option: ");
		int manageChoice = scanner.nextInt();

		switch (manageChoice) {
		case 1:
			editBookDetails();
			break;
		case 2:
			deleteBook();
			break;
		default:
			System.out.println("\nInvalid option. Returning to admin menu.\n");
		}
	}

	private void deleteBook() {
		System.out.print("\nEnter the Book ID to delete: ");
		int bookIdToDelete = scanner.nextInt();

		if (bookIdToDelete > 0) {
			boolean isDeleted = library.deleteBook(bookIdToDelete);
			if (isDeleted) {
				System.out.println("\nBook deleted successfully.\n");
			} else {
				System.out.println("\nBook is either issued or not found. Returning to admin menu.\n");
			}
		}
	}

	private int generateUniqueBookId() {
		return (int) (Math.random() * 1000);
	}

	private void editBookDetails() {
		boolean isChanged = Boolean.FALSE;
		System.out.print("\nEnter the Book ID to edit: ");
		int bookIdToEdit = scanner.nextInt();

		Book bookToEdit = library.getBookById(bookIdToEdit);

		if (bookToEdit != null) {
			System.out.println("\nCurrent Book Details:");
			System.out.println("-------------------");
			System.out.println(bookToEdit.toString());

			scanner.nextLine(); // Consume the newline character

			System.out.print("\nEnter the new title (or press Enter to keep current): ");
			String newTitle = scanner.nextLine().trim();
			if (!newTitle.isEmpty()) {
				isChanged = Boolean.TRUE;
				bookToEdit.setTitle(newTitle);
			}

			System.out.print("Enter the new author (or press Enter to keep current): ");
			String newAuthor = scanner.nextLine().trim();
			if (!newAuthor.isEmpty()) {
				isChanged = Boolean.TRUE;
				bookToEdit.setAuthor(newAuthor);
			}

			System.out.print("Enter the new category (or press Enter to keep current): ");
			String newCategory = scanner.nextLine().trim();
			if (!newCategory.isEmpty()) {
				isChanged = Boolean.TRUE;
				bookToEdit.setCategory(newCategory);
			}
			if (isChanged) {
				library.editBook(bookToEdit);
				System.out.println("\nBook details updated successfully.\n");
			} else {
				System.out.println("\nBook details were not updated.\n");
			}
		} else {
			System.out.println("\nBook not found. Returning to admin menu.\n");
		}
	}

	private void viewReports() {
		System.out.println("View Reports:");
		System.out.println("1. Number of Books");
		System.out.println("2. Number of Issued Books");
		System.out.println("3. List of Available Books");
		System.out.print("Select a report option: ");
		int reportChoice = scanner.nextInt();

		switch (reportChoice) {
		case 1:
			int totalBooks = library.getTotalBooks();
			System.out.println("\nTotal number of books in the library: " + totalBooks + "\n");
			break;
		case 2:
			int issuedBooks = library.getTotalIssuedBooks();
			System.out.println("\nTotal number of issued books: " + issuedBooks + "\n");
			break;
		case 3:
			List<Book> availableBooks = library.getAvailableBooks();
			System.out.println("\nList of Available Books:");
			System.out.println("---------------------------");
			for (Book book : availableBooks) {
				System.out.println(book.toString());
				System.out.println("-------------------");
			}
			break;
		default:
			System.out.println("\nInvalid report option. Returning to admin menu.\n");
		}
	}

	public static void main(String[] args) {
		LibraryManagementSystem librarySystem = new LibraryManagementSystem();
		librarySystem.run();
	}
}
