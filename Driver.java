import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class Driver {

    public static Item createItem(String item_code, String description, double price, int inventory_amount) throws SQLException {
        Connection connection = null;

        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("INSERT INTO items (item_code, description, price, inventory_amount) VALUES ('%s', '%s', %s, %s);",
                item_code,
                description,
                price,
                inventory_amount);
        sqlStatement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

        ResultSet resultSet = sqlStatement.getGeneratedKeys();
        resultSet.next();

        int item_id = resultSet.getInt(1);
        connection.close();

        return new Item(item_id, item_code, description, price, inventory_amount);
    }

    public static List<Order> createOrder(String item_code, int quantity) throws SQLException {
        Connection connection = null;


        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("INSERT INTO orders (item_code, quantity) VALUES ('%s' , %s);",
                item_code, quantity);

        sqlStatement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

        ResultSet resultSet1 = sqlStatement.getGeneratedKeys();
        resultSet1.next();

        int order_id = resultSet1.getInt(1);

        String sql2 = String.format("SELECT * FROM orders WHERE order_id = '%s';", order_id);

        ResultSet resultSet2 = sqlStatement.executeQuery(sql2);

        List<Order> orders = new ArrayList<Order>();

        while (resultSet2.next()) {

            Timestamp timestamp = resultSet2.getTimestamp(4);

            Order order = new Order(order_id, item_code, quantity);
            order.setOrder_timestamp(timestamp);
            orders.add(order);
        }

        resultSet1.close();
        resultSet2.close();
        connection.close();
        return orders;
    }

    public static List<Item> updateInventory(String item_code, int inventory_amount) throws SQLException {
        Connection connection = null;

        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("UPDATE items SET inventory_amount = %s WHERE item_code = '%s';",
                inventory_amount, item_code);

        String sql2 = String.format("SELECT * FROM items WHERE item_code = '%s';", item_code);

        sqlStatement.executeUpdate(sql);
        ResultSet resultSet = sqlStatement.executeQuery(sql2);


        List<Item> items = new ArrayList<Item>();

        while (resultSet.next()) {
            int item_id = resultSet.getInt(1);
            String description = resultSet.getString(3);
            double price = resultSet.getDouble(4);

            Item item = new Item(item_id, item_code, description, price, inventory_amount);
            items.add(item);
        }

        resultSet.close();
        connection.close();
        return items;
    }

    public static void deleteItem(String item_code) throws SQLException {
        Connection connection = null;

        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("DELETE FROM items WHERE item_code = '%s';", item_code);
        sqlStatement.executeUpdate(sql);
        connection.close();
    }

    public static List<Item> getItems() throws SQLException {
        Connection connection = null;


        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = "SELECT * FROM items;";
        ResultSet resultSet = sqlStatement.executeQuery(sql);

        List<Item> items = new ArrayList<Item>();

        while (resultSet.next()) {
            int item_id = resultSet.getInt(1);
            String item_code = resultSet.getString(2);
            String description = resultSet.getString(3);
            double price = resultSet.getDouble(4);
            int inventory_amount = resultSet.getInt(5);

            Item item = new Item(item_id, item_code, description, price, inventory_amount);
            items.add(item);
        }
        resultSet.close();
        connection.close();
        return items;


    }

    public static List<Item> getItems(String item_code) throws SQLException {
        Connection connection = null;


        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("SELECT * FROM items WHERE item_code = '%s';", item_code);
        ResultSet resultSet = sqlStatement.executeQuery(sql);

        List<Item> items = new ArrayList<Item>();

        while (resultSet.next()) {
            int item_id = resultSet.getInt(1);
            String description = resultSet.getString(3);
            double price = resultSet.getDouble(4);
            int inventory_amount = resultSet.getInt(5);

            Item item = new Item(item_id, item_code, description, price, inventory_amount);
            items.add(item);
        }
        resultSet.close();
        connection.close();
        return items;
    }

    public static void deleteOrder(String item_code) throws SQLException {
        Connection connection = null;

        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("DELETE FROM orders WHERE item_code = '%s';", item_code);
        sqlStatement.executeUpdate(sql);
        connection.close();
    }

    public static List<Order> getOrders() throws SQLException {
        Connection connection = null;


        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = "SELECT * FROM orders LEFT JOIN items ON orders.item_code = items.item_code;";
        ResultSet resultSet = sqlStatement.executeQuery(sql);

        List<Order> orders = new ArrayList<Order>();

        while (resultSet.next()) {
            int order_id = resultSet.getInt(1);
            String item_code = resultSet.getString(2);
            int quantity = resultSet.getInt(3);
            Timestamp order_timestamp = resultSet.getTimestamp(4);
            double price = resultSet.getDouble(8);

            Order order = new Order(order_id, item_code, quantity);
            order.setOrder_timestamp(order_timestamp);
            order.setTotal_order_amount((price * quantity));
            orders.add(order);
        }
        resultSet.close();
        connection.close();
        return orders;

    }

    public static List<Order> getOrders(String item_code) throws SQLException {
        Connection connection = null;


        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("SELECT * FROM orders LEFT JOIN items ON orders.item_code = items.item_code WHERE orders.item_code = '%s';", item_code);
        ResultSet resultSet = sqlStatement.executeQuery(sql);

        List<Order> orders = new ArrayList<Order>();

        while (resultSet.next()) {
            int order_id = resultSet.getInt(1);
            int quantity = resultSet.getInt(3);
            Timestamp order_timestamp = resultSet.getTimestamp(4);
            double price = resultSet.getDouble(8);

            Order order = new Order(order_id, item_code, quantity);
            order.setOrder_timestamp(order_timestamp);
            order.setTotal_order_amount((price * quantity));
            orders.add(order);
        }
        resultSet.close();
        connection.close();
        return orders;
    }

    public static String getOrderDetails(int order_id) throws SQLException {
        Connection connection = null;


        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("SELECT orders.order_id, orders.item_code, items.description, orders.quantity, items.price, (orders.quantity * items.price) AS order_total_amount FROM orders LEFT JOIN items ON orders.item_code = items.item_code WHERE %s = orders.order_id;", order_id);
        ResultSet resultSet = sqlStatement.executeQuery(sql);

        resultSet.first();
        String item_code = resultSet.getString(2);
        String description = resultSet.getString(3);
        int quantity = resultSet.getInt(4);
        double price = resultSet.getDouble(5);
        double order_total_amount = resultSet.getDouble(6);

        String details = String.format("%s, %s, %s, %s, %s, %s", order_id, item_code, description, quantity, price, order_total_amount);

        resultSet.close();
        connection.close();

        return details;
    }

    public static List<String> getOrderDetails() throws SQLException {
        Connection connection = null;


        connection = SQLDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = "SELECT orders.order_id, orders.item_code, items.description, orders.quantity, items.price, (orders.quantity * items.price) AS order_total_amount FROM orders LEFT JOIN items ON orders.item_code = items.item_code;";
        ResultSet resultSet = sqlStatement.executeQuery(sql);

        List<String> orderDetails = new ArrayList<String>();

        while (resultSet.next()) {
            int order_id = resultSet.getInt(1);
            String item_code = resultSet.getString(2);
            String description = resultSet.getString(3);
            int quantity = resultSet.getInt(4);
            double price = resultSet.getDouble(5);
            double order_total_amount = resultSet.getDouble(6);

            String details = String.format("%s, %s, %s, %s, %s, %s", order_id, item_code, description, quantity, price, order_total_amount);
            orderDetails.add(details);
        }
        resultSet.close();
        connection.close();
        return orderDetails;

    }

    public static void attemptToGetItems() {
        try {
            List<Item> items = getItems();
            for (Item item : items) {
                System.out.println(item.toString());
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get items");
            System.out.println(sqlException.getMessage());
        }
    }
    public static void attemptToGetItems(String item_code) {
        try {
            List<Item> items = getItems(item_code);
            for(Item item : items) {
                System.out.println(item.toString());
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get items");
            System.out.println(sqlException.getMessage());
        }
    }

    public static void attemptToGetOrders() {
        try {
            List<Order> orders = getOrders();
            for (Order order : orders) {
                System.out.println(order.toString());
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get orders");
            System.out.println(sqlException.getMessage());
        }
    }
    public static void attemptToGetOrders(String item_code) {
        try {
            List<Order> orders = getOrders(item_code);
            for (Order order : orders) {
                System.out.println(order.toString());
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get orders");
            System.out.println(sqlException.getMessage());
        }
    }

    public static void attemptToGetOrderDetails() {
        try {
            List<String> orderDetails = getOrderDetails();
            for (String details : orderDetails) {
                System.out.println(details);
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get order details");
            System.out.println(sqlException.getMessage());
        }
    }
    public static void attemptToGetOrderDetails(int order_id) {
        try {
            String details = getOrderDetails(order_id);
            System.out.println(details);
        } catch (SQLException sqlException) {
            System.out.println("Failed to get order details");
            System.out.println(sqlException.getMessage());
        }
    }

    public static void attemptToCreateNewItem(String item_code, String description, double price, int inventory_amount) {
        try {
            Item item = createItem(item_code, description, price, inventory_amount);
            System.out.println(item.toString());
        } catch (SQLException sqlException) {
            System.out.println("Failed to create item");
            System.out.println(sqlException.getMessage());
        }
    }

    public static void attemptToCreateNewOrder(String item_code, int quantity) {
        try {
            List<Order> orders = createOrder(item_code, quantity);
            for (Order order : orders) {
                System.out.println(order.toString());
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to create order");
            System.out.println(sqlException.getMessage());
        }

    }

    public static void attemptToUpdateInventory(String item_code, int inventory_quantity) {
        try {
            List<Item> items = updateInventory(item_code, inventory_quantity);
            for(Item item : items) {
                System.out.println(item.toString());
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to update inventory");
            System.out.println(sqlException.getMessage());
        }

    }

    public static void attemptToDeleteItem(String item_code) {
        try {
            deleteItem(item_code);
            System.out.println("The item has been deleted");
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete item");
            System.out.println(sqlException.getMessage());
        }
    }

    public static void attemptToDeleteOrder(String item_code) {
        try {
            deleteOrder(item_code);
            System.out.println("The order has been deleted");
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete order");
            System.out.println(sqlException.getMessage());
        }
    }

    public static void main(String[] args){

        if (args[0].equals("GetItems")) {
            if(args[1].equals("*")) {
                attemptToGetItems();
            }
            else {
                attemptToGetItems(args[1]);
            }
        } else if (args[0].equals("CreateItem")) {
            String item_code = args[1];
            String description = args[2];
            double price = Double.parseDouble(args[3]);
            int inventory_amount = Integer.parseInt(args[4]);
            attemptToCreateNewItem(item_code, description, price, inventory_amount);
        } else if (args[0].equals("UpdateInventory")) {
            String item_code = args[1];
            int inventory_amount = Integer.parseInt(args[2]);
            attemptToUpdateInventory(item_code, inventory_amount);
        } else if (args[0].equals("CreateOrder")) {
            String item_code = args[1];
            int quantity = Integer.parseInt(args[2]);
            attemptToCreateNewOrder(item_code, quantity);
        } else if (args[0].equals("DeleteItem")) {
            String item_code = args[1];
            attemptToDeleteItem(item_code);
        } else if (args[0].equals("DeleteOrder")) {
            String item_code = args[1];
            attemptToDeleteOrder(item_code);
        } else if (args[0].equals("GetOrders")) {
            if (args[1].equals("*")) {
                attemptToGetOrders();
            }
            else {
                attemptToGetOrders(args[1]);
            }
        } else if (args[0].equals("GetOrderDetails")) {
            if (args[1].equals("*")) {
                attemptToGetOrderDetails();
            }
            else {
                attemptToGetOrderDetails(Integer.parseInt(args[1]));
            }
        }
    }
}