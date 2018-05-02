//package controller;
//
//import model.Mentor;
//
//import java.sql.*;
//import java.util.LinkedList;
//import java.util.List;
//
//public class SQLiteJDBC {
//
//        private static final String DRIVER = "org.sqlite.JDBC";
//    private static final String DB_URL = "jdbc:sqlite:recent.db";
//
//    private Connection conn;
//    private Statement stat;
//
//    public SQLiteJDBC() {
//        try {
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//            System.err.println("Driver is not found");
//            e.printStackTrace();
//        }
//
//        try {
//            this.conn = DriverManager.getConnection(DB_URL);
//            this.stat = conn.createStatement();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public boolean createTable() {
//        String createMentors = "create table if not exists mentors(id integer primary key unique, first text, last text, nick text, phone text, email text, city text, num int)";
//        String createApplicants = "create table if not exists applicants(id integer primary key unique, first text, last text, phone text, email text, code int)";
//
//        try {
//            stat.execute(createMentors);
//            stat.execute(createApplicants);
//        } catch (SQLException e) {
//            System.err.println("Invalid table creator");
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public boolean insertRecord(String values, String tableName) {
//        String[] listValues = values.split(",");
//        int numberOfValues = listValues.length;
//        String signs = "";
//        while (numberOfValues-- > 0) {
//            signs+=(",?");
//        }
//        numberOfValues = listValues.length;
//
//        try {
//            PreparedStatement preparedStatement = conn.prepareStatement("insert into " + tableName + " values(NULL" + signs + ")");
//            while (--numberOfValues > -1) {
//                if (listValues[numberOfValues].matches("[0-9]")) {
//                    preparedStatement.setInt(numberOfValues+1, Integer.parseInt(listValues[numberOfValues]));
//                } else {
//                    preparedStatement.setString(numberOfValues+1, listValues[numberOfValues]);
//                }
//            }
//            preparedStatement.execute();
//
//        } catch (SQLException e) {
//            System.err.println("Invalid new record data");
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public List<Mentor> selectMentors() {
//        List<Mentor> records = new LinkedList<>();
//
//        try {
//            ResultSet result = stat.executeQuery("SELECT * FROM mentors");
//            String first, last, nick, phone, email, city;
//            int id,fav;
//            while (result.next()) {
//                id = result.getInt("id");
//                first = result.getString("first");
//                last = result.getString("last");
//                nick = result.getString("nick");
//                phone = result.getString("phone");
//                email = result.getString("email");
//                city = result.getString("city");
//                fav = result.getInt("num");
//                records.add(new Mentor(first, last, nick, phone, email, city, fav));
//            }
//        } catch (SQLException e) {
//            System.err.println("Invalid database");
//            e.printStackTrace();
//        }
//        return records;
//    }
//
//    public void close() {
//        try {
//            conn.close();
//        } catch (SQLException e) {
//            System.err.println("Invalid close");
//            e.printStackTrace();
//        }
//    }
//
//
//
//    public static void main(String[] args) {
//        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
//        try {
//            sqLiteJDBC.stat.execute("drop table mentors");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        sqLiteJDBC.createTable();
//        sqLiteJDBC.insertRecord("nika,andrzejewska,nicka,555-555,micka@,leszno,35", "mentors");
//        sqLiteJDBC.insertRecord("ziemo,andrzejewski,555,ziemo@,4", "applicants");
//        sqLiteJDBC.selectMentors().forEach(System.out::println);
//    }
//}
