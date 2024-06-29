import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LibraryDAO {

	String booksFilePath = "C:\\Users\\deepi\\MyWorkspace\\learningapp\\src\\main\\resources\\Books.csv";
	String usersFilePath = "C:\\Users\\deepi\\MyWorkspace\\learningapp\\src\\main\\resources\\Users.csv";

	public Map<Integer, Book> getBooks() {
		Map<Integer, Book> books = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(booksFilePath))) {
			String line;
			// Read each line from the CSV file
			while ((line = br.readLine()) != null) {
				// Split the line by the comma (,) separator
				String[] data = line.split(",");
				// Process the data to create Book objects
				if (!line.contains("bookId")) {
					Integer id = Integer.parseInt(data[0]);
					Book book = new Book(id, data[1], data[2], data[3], Boolean.valueOf(data[4]));
					books.put(id, book);
				}
			}
		} catch (IOException e) {
			System.out.println("The file doesn't Exist!");
		}
		return books;
	}

	public void addBook(Book book) {
		try (FileWriter fileWriter = new FileWriter(booksFilePath, true);
				PrintWriter printWriter = new PrintWriter(fileWriter)) {
			// Add data to the CSV file
			String dataToAdd = book.getBookId() + "," + book.getTitle() + "," + book.getAuthor() + ","
					+ book.getCategory() + "," + "TRUE";
			printWriter.println(dataToAdd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, User> getUsers() {
		Map<Integer, User> users = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(usersFilePath))) {
			String line;
			// Read each line from the CSV file
			while ((line = br.readLine()) != null) {
				// Split the line by the comma (,) separator
				String[] data = line.split(",");
				List<Integer> borrowedBookIds = new ArrayList<>();
				if (!line.contains("username")) {
					if (data.length == 4) {
						String[] split = data[3].split(":");
						for (String id : split) {
							if (!id.equalsIgnoreCase("borrowedBookIds"))
								borrowedBookIds.add(Integer.parseInt(id));
						}
					}
					// Process the data to create User objects
					Integer id = Integer.parseInt(data[0]);
					User user = new User(id, data[1], data[2]);
					user.setBorrowedBookIds(borrowedBookIds);
					users.put(id, user);
				}
			}
		} catch (IOException e) {
			System.out.println("The file doesn't Exist! " + e.getMessage());
		}
		return users;
	}

	public void addUser(User user) {
		try (FileWriter fileWriter = new FileWriter(usersFilePath, true);
				PrintWriter printWriter = new PrintWriter(fileWriter)) {
			String dataToAdd = user.getId() + "," + user.getUsername() + "," + user.getPassword();
			printWriter.println(dataToAdd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void borrowBook(int userId, int borrowBookId) {
		List<String> lines = new ArrayList<>();
		// Read the existing CSV file and store its contents in memory
		try (BufferedReader br = new BufferedReader(new FileReader(usersFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (!data[0].equalsIgnoreCase("id")) {
					Integer id = Integer.parseInt(data[0]);
					// fetch the row to update
					if (id == userId) {
						if (data.length == 3) {
							String dataWithBorrowBookId = String.join(",", data) + "," + String.valueOf(borrowBookId);
							lines.add(String.join(",", dataWithBorrowBookId));
						} else if (data.length == 4) {
							data[3] = data[3].concat(":" + borrowBookId);
							lines.add(String.join(",", data));
						}
					} else
						lines.add(line);
				} else
					lines.add(line);

			}
		} catch (IOException e) {
			System.out.println("The file doesn't Exist!");
		}
		// Write the updated contents back to the CSV file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFilePath))) {
			for (String updatedLine : lines) {
				writer.write(updatedLine);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateIsAvailable(borrowBookId, Boolean.FALSE);
	}

	public void updateIsAvailable(int borrowBookId, boolean available) {
		List<String> lines = new ArrayList<>();
		// Read the existing CSV file and store its contents in memory
		try (BufferedReader br = new BufferedReader(new FileReader(booksFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (!data[0].equalsIgnoreCase("bookId")) {
					Integer id = Integer.parseInt(data[0]);
					// fetch the row to update
					if (id == borrowBookId) {
						data[4] = String.valueOf(available);
						lines.add(String.join(",", data));
					} else
						lines.add(line);
				} else
					lines.add(line);
			}
		} catch (IOException e) {
			System.out.println("The file doesn't Exist!");
		}
		// Write the updated contents back to the CSV file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(booksFilePath))) {
			for (String updatedLine : lines) {
				writer.write(updatedLine);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void returnBook(int userId, int borrowBookId) {
		List<String> lines = new ArrayList<>();
		// Read the existing CSV file and store its contents in memory
		try (BufferedReader br = new BufferedReader(new FileReader(usersFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (!data[0].equalsIgnoreCase("id")) {
					Integer id = Integer.parseInt(data[0]);
					// fetch the row to update
					if (id == userId) {
						if (data[3] != null || !data[3].equals("")) {
							List<String> list = new LinkedList<>(Arrays.asList(data[3].split(":")));
							list.removeIf(a -> a.equalsIgnoreCase(String.valueOf(borrowBookId)));
							data[3] = String.join(":", list);
							lines.add(String.join(",", data));
						}
					} else
						lines.add(line);
				} else
					lines.add(line);
			}
		} catch (IOException e) {
			System.out.println("The file doesn't Exist!");
		}
		// Write the updated contents back to the CSV file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFilePath))) {
			for (String updatedLine : lines) {
				writer.write(updatedLine);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateIsAvailable(borrowBookId, Boolean.TRUE);
	}

	public void editBook(Book bookToEdit) {
		List<String> lines = new ArrayList<>();
		// Read the existing CSV file and store its contents in memory
		try (BufferedReader br = new BufferedReader(new FileReader(booksFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (!data[0].equalsIgnoreCase("bookId")) {
					Integer id = Integer.parseInt(data[0]);
					// fetch the row to update
					if (id == bookToEdit.getBookId()) {
						String dataToAdd = data[0] + "," + bookToEdit.getTitle() + "," + bookToEdit.getAuthor() + ","
								+ bookToEdit.getCategory() + "," + data[4];
						lines.add(dataToAdd);
					} else
						lines.add(line);
				} else
					lines.add(line);
			}

		} catch (IOException e) {
			System.out.println("The file doesn't Exist!");
		}
		// Write the updated contents back to the CSV file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(booksFilePath))) {
			for (String updatedLine : lines) {
				writer.write(updatedLine);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteBook(int bookId) {
		List<String> lines = new ArrayList<>();
		// Read the existing CSV file and store its contents in memory
		try (BufferedReader br = new BufferedReader(new FileReader(booksFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (!data[0].equalsIgnoreCase("bookId")) {
					Integer id = Integer.parseInt(data[0]);
					// fetch the row to delete
					if (id == bookId) {
						continue;
					} else
						lines.add(line);
				} else
					lines.add(line);
			}

		} catch (IOException e) {
			System.out.println("The file doesn't Exist!");
		}
		// Write the updated contents back to the CSV file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(booksFilePath))) {
			for (String updatedLine : lines) {
				writer.write(updatedLine);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
