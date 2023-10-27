import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
public class bankDatabase {
    public static final String CREATEQUERY = "DROP TABLE Bank IF EXISTS; CREATE TABLE Bank(accNo INTEGER PRIMARY KEY AUTO_INCREMENT, accHolderName VARCHAR2(50), bankName VARCHAR2(50), balance NUMBER, ifcs VARCHAR(20))";
     public static final String INSERTQUERY ="INSERT INTO Bank (accHolderName,bankName,balance,ifcs)VALUES (?,?,?,?)";
    public static final String UPDATE_NAME ="UPDATE Bank SET accHolderName=? WHERE accNo = ?";
    public static final String UPDATE_BANK ="UPDATE Bank SET bankName=? WHERE accNo = ?";
    public static final String UPDATE_BALANCE ="UPDATE Bank SET balance=? WHERE accNo = ?";
    public static final String UPDATE_IFCS ="UPDATE Bank SET ifcs=? WHERE accNo = ?";
    public static final String SELECTQUERY="SELECT * FROM Bank";
    public static final String DELETEQUERY="DELETE FROM Bank WHERE accNo=?";


    public static void main(String[] args) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./db", "sa", "");
            if (conn != null) {
                System.out.println("Connected to the h2 database");
            }
            Statement stmt = conn.createStatement();
            stmt.execute(CREATEQUERY);
            System.out.println("Table created");
            Scanner sc = new Scanner(System.in);
            int opt;
            while(true){
                System.out.println("-----------------------------------------");
                System.out.println("WELCOME TO BANK DATABASE ADMINISTRATION");
                System.out.println("-----------------------------------------");
                System.out.println("Select one option from following");
                System.out.println("1.Display Table");
                System.out.println("2.Insert a row");
                System.out.println("3.Update a row");
                System.out.println("4.Delete a row");
                System.out.println("5.Exit");

                opt = sc.nextInt();
                switch (opt){
                    case 1:
                        selectStatement(conn);
                        break;
                    case 2:
                        insertStatement(conn);
                        break;
                    case 3:
                        updateStatement(conn);
                        break;
                    case 4:
                        deleteStatement(conn);
                        break;
                    case 5:
                        System.out.println("Exiting the program");
                        return;
                    default:
                        System.out.println("Invalid option.Please select option between 1-5");
                }
            }
        }
        catch (SQLException ex) {
            System.out.println("SQLException handled"+ex.getMessage());
        }
    }
    private static void selectStatement(Connection conn) throws SQLException{
        PreparedStatement ps = conn.prepareStatement(SELECTQUERY);
        ResultSet rs = ps.executeQuery();
        System.out.println("AccNo\tAccHolderName\tBankName\tBalance\t\tIFCS");
        while(rs.next()){
            System.out.println(rs.getInt(1)+"\t\t"+rs.getString(2)+"\t\t\t"+rs.getString(3)+"\t\t\t"+rs.getFloat(4)+"\t\t"+rs.getString(5));
        }
    }
    private static void insertStatement(Connection conn) throws SQLException{
        try{
            int rows=0;
            Scanner sc = new Scanner(System.in);
            PreparedStatement ps= conn.prepareStatement(INSERTQUERY);
            System.out.println("Enter Account Holder Name : ");
            String name = sc.next();
            System.out.println("Enter Bank Name : ");
            String bank = sc.next();
            System.out.println("Enter Balance : ");
            float balance = sc.nextFloat();
            System.out.println("Enter IFCS code : ");
            String ifcs = sc.next();
            ps.setString(1,name);
            ps.setString(2,bank);
            ps.setFloat(3,balance);
            ps.setString(4,ifcs);
            if(balance<0)
                System.out.println("Invalid data:Balance Must be non-negative");
            rows = ps.executeUpdate();
            System.out.println("Data inserted successfully");
            System.out.println(rows + " rows updated");
        }catch (InputMismatchException e){
            System.out.println("Invalid data type. Please enter valid data");
        }
    }
    private static void updateStatement(Connection conn) throws SQLException{
        try{
            int rows=0;
            System.out.println("Enter the Account number of the user to be updated : ");
            Scanner sc = new Scanner(System.in);
            int accNo = sc.nextInt();
            System.out.println("Select the value to be updated : ");
            System.out.println("1.Account Holder Name\n2.Bank Name\n3.Balance\n4.IFCS");
            int updateCol = sc.nextInt();

            switch (updateCol){
                case 1 :
                    PreparedStatement ps1 = conn.prepareStatement(UPDATE_NAME);
                    ps1.setInt(2,accNo);
                    System.out.println("Enter updated Account holder name");
                    String name = sc.next();
                    ps1.setString(1,name);
                    rows = ps1.executeUpdate();
                    break;

                case 2 :
                    PreparedStatement ps2 = conn.prepareStatement(UPDATE_BANK);
                    ps2.setInt(2,accNo);
                    System.out.println("Enter updated Bank name:");
                    String bank= sc.next();
                    ps2.setString(1, bank);
                    rows = ps2.executeUpdate();
                    break;

                case 3 :
                    PreparedStatement ps3 = conn.prepareStatement(UPDATE_BALANCE);
                    ps3.setInt(2,accNo);
                    System.out.println("Enter the updated balance:");
                    float balance = sc.nextFloat();
                    ps3.setFloat(1, balance);
                    rows = ps3.executeUpdate();
                    if(balance<0){
                        System.out.println("Invalid data:Balance Must be non-negative");
                    }
                    break;

                case 4:
                    PreparedStatement ps4 = conn.prepareStatement(UPDATE_IFCS);
                    ps4.setInt(2,accNo);
                    System.out.println("Enter the updated IFCS code:");
                    String ifcs = sc.next();
                    ps4.setString(1, ifcs);
                    rows = ps4.executeUpdate();
                    break;

                default :
                    System.out.println("Invalid option");
                    break;
            }
            if(rows>0)
            System.out.println(rows + " rows updated successfully");
            else
                System.out.println("Account not found. No data updated");
        }catch (InputMismatchException e){
            System.out.println("Invalid data type. Please enter valid data");
        }
    }
    private static void deleteStatement(Connection conn) throws SQLException{
        int rows=0;
        Scanner sc = new Scanner(System.in);
        PreparedStatement ps = conn.prepareStatement(DELETEQUERY);
        System.out.println("Enter Account number of the user to be deleted:");
        int accNo = sc.nextInt();
        ps.setInt(1,accNo);
        rows = ps.executeUpdate();
        if(rows>0)
            System.out.println(rows + " rows deleted successfully");
        else
            System.out.println("Account not found. No data deleted");
    }
}