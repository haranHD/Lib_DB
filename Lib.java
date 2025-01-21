import java.util.Scanner;
import java.sql.*;


public class Lib {
	static Scanner sc = new Scanner(System.in);
	public static void display(){
	    System.out.println("MENU:\n 1 . Add_Book\n 2 . Search\n 3 . View_Book\n 4 . Borrow\n 5 . Return_Book\n 6 . EXIT");
	}
	
	public void Add(Connection cn) {
		try {
			System.out.println("Enter the Book_ID :");
			int data_ID = sc.nextInt();
			sc.nextLine();
			System.out.println("Enter the Book :");
			String book_name = sc.nextLine();
			System.out.println("Enter the Author :");
			String author = sc.nextLine();
			
			String query = "INSERT INTO books (Book_ID , Book_name , Author , Borrow) VALUES (?,?,?,false)";
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, data_ID);
			ps.setString(2, book_name);
			ps.setString(3,author);
			
			int rows = ps.executeUpdate();
			if(rows >0) {
				System.out.println("Book added Successfully!..");
			}
		}
		catch(Exception e) {
			System.out.println("Error in Adding Book :" + e.getMessage());
		}

	}
	public void search(Connection cn) {
		try {
			sc.nextLine();
			System.out.println("Enter the Book Name :");
			String find = sc.nextLine();
			String q  = "SELECT * FROM books WHERE Book_name = ?";
			PreparedStatement ps = cn.prepareStatement(q);
			
			ps.setString(1,find);
			ResultSet rs = ps.executeQuery();
			System.out.println("ID\tBook\t\t\tAuthor\t\tBorrow");
			System.out.println("--------------------------------------------------------------");
			
			boolean found = false;
			while(rs.next()) {
				found = true;
				int Data_ID = rs.getInt("Book_ID");
				String book_name = rs.getString("Book_name");
				String author =rs.getString("Author");
				Boolean borrow = rs.getBoolean("Borrow");
				System.out.printf("%s\t%s\t\t%s\t\t%s\n",Data_ID,book_name,author,borrow ?"Yes":"No");
			}
			if(!found) {
				System.out.println("No book found with the Book Name :"+find);
			}
			rs.close();
			ps.close();
		}catch(Exception e) {
			System.out.println("Error in Searching.!."+e.getMessage());
		}
	}
	public void Show(Connection cn) {
		try {
			String query = "SELECT * FROM books";
			PreparedStatement ps = cn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			System.out.println("ID\tBook\t\t\tAuthor\t\tBorrow");
			System.out.println("-------------------------------------------------------------");
			while(rs.next()) {
				int Data_ID = rs.getInt("Book_ID");
				String book_name = rs.getString("Book_name");
				String author =rs.getString("Author");
				Boolean borrow = rs.getBoolean("Borrow");
				
				System.out.printf("%s\t%s\t\t%s\t\t%s\n",Data_ID,book_name,author,borrow ?"Yes":"No");
			}
			rs.close();
			ps.close();
			
			
		}catch(Exception e) {
			System.out.println("Error in Displaying..!" + e.getMessage());
		}
	}
	
	public void borrow(Connection cn) {
		try {
			sc.nextLine();
			System.out.println("Enter the Book Name :");
			String book_b = sc.nextLine();
			
			String q = "SELECT Borrow FROM books WHERE Book_name = ?";
			PreparedStatement ps = cn.prepareStatement(q);
			ps.setString(1, book_b);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				boolean isBorrowed = rs.getBoolean("Borrow");
				if(isBorrowed) {
					System.out.println("The book is already borrowed!.");
				}
				else {
					String update_q = "UPDATE books SET Borrow = true WHERE Book_name = ?";
					PreparedStatement up = cn.prepareStatement(update_q);
					up.setString(1, book_b);
					int rows = up.executeUpdate();
					if(rows>0) {
						System.out.println("Book Borrowed Successfully.");
					}else 
						System.out.println("Failed , try again!");
					up.close();
				}
			}else
				System.out.println("Book not found");
		
			rs.close();
			ps.close();
		}
		
		catch(Exception e) {
			System.out.println("Error in Borrowing Book!." + e.getMessage());
		}
	}
	public void Return(Connection cn) {
		try {
			sc.nextLine();
			System.out.println("Enter the Book name You are Borrowed : ");
			String R_book = sc.nextLine();
			String q = "SELECT Borrow FROM books WHERE Book_name = ?";
			PreparedStatement ps = cn.prepareStatement(q);
			ps.setString(1, R_book);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				boolean isBorrowed = rs.getBoolean("Borrow");
				if(!isBorrowed) {
					System.out.println("The book is not Borrowed!, So you cannot return it");
				}else {
					String u_query = "UPDATE books SET Borrow = false WHERE Book_name = ?";
					PreparedStatement up = cn.prepareStatement(u_query);
					up.setString(1, R_book);
					int rows = up.executeUpdate();
					if(rows>0) {
						System.out.println("Book returned successfully.");
					}
					else 
						System.out.println("Failed to return the Book!");
					up.close();
				}
			}
			else
				System.out.println("Book not found");
			ps.close();
			rs.close();
			
		}catch(Exception e) {
			System.out.println("Error in returning book !" + e.getMessage());
		}
	}
	
	public static void main(String[] a) {

		try (Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library", "root", "haran8933");)
		{

		System.out.println("Connected Successfully\n");
		
		Lib library = new Lib();
		while(true) {
			display();
			System.out.println("Enter the Function Number :");
			int choice = sc.nextInt();
			switch(choice) {
			case 1:
				library.Add(cn);
				break;
			case 2:
				library.search(cn);
				break;
			case 3:
				library.Show(cn);
				break;
			case 4:
				library.borrow(cn);
				break;
			case 5:
				library.Return(cn);
				break;
			case 6:
				System.out.println("Exiting..");
				return;
			default:
				System.out.println("Invalid Input Function.!");
			}
		}
		}
				catch(Exception e) {
					System.out.println("Error\n"+ e);
				}
	}
}
		
	

