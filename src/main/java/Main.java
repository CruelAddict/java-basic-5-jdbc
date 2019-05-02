import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager
                    .getConnection("jdbc:mysql://localhost/jdbc_practice?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false"
                            , "root"
                            , "root") ;
            System.out.println("Creating table in given database...");
            Statement stmt = connection.createStatement();

            String sql="DROP TABLE IF EXISTS ITEM";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE ITEM " +
                    "(ID INTEGER not NULL AUTO_INCREMENT, " +
                    " name VARCHAR(255), " +
                    " price INTEGER, "+
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);

            System.out.println("Created table ITEM in given database...");

            sql="INSERT INTO ITEM (NAME, PRICE) VALUES ('Guitar', 3000), ('Drums', 5000)";
            stmt.executeUpdate(sql);

            System.out.println("Table is filled with Guitar and Drums");


            Scanner scanner = new Scanner(System.in);
            System.out.print("Input product name: ");
            String name = scanner.nextLine();

            System.out.print("Input product price: ");
            int price = scanner.nextInt();

            sql = "INSERT INTO ITEM (NAME, PRICE) Values (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, price);

            int rows = preparedStatement.executeUpdate();

            System.out.printf("%d row added successfully! \nNow the table looks like this:", rows);

            sql = "SELECT id, name, price FROM ITEM";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int itemId  = rs.getInt("id");
                int itemPrice = rs.getInt("price");
                String itemName = rs.getString("name");

                System.out.print("ID: " + itemId);
                System.out.print(", Name: " + itemName);
                System.out.println(", Price: " + itemPrice);
            }
            System.out.print("You may now try to get an item's price by its ID. Enter the ID: ");
            int searchedItemId = scanner.nextInt();
            sql = "SELECT PRICE FROM ITEM WHERE ID=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(searchedItemId));
            rs = preparedStatement.executeQuery();
            rs.next();
            int returnedPrice = rs.getInt("price");
            System.out.println("This item's price is "+returnedPrice);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
