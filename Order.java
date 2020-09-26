import java.util.Date;
import java.sql.Timestamp;

public class Order {
    private int order_id;
    private String item_code;
    private int quantity;
    private Timestamp order_timestamp;
    private double total_order_amount;

    public Order(int order_id, String item_code, int quantity){
        this.order_id = order_id;
        this.item_code = item_code;
        this.quantity = quantity;
        total_order_amount = 0;
        order_timestamp = null;
    }

    public int getOrder_id() {
        return order_id;
    }

    public Date getOrder_timestamp() {
        return order_timestamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setOrder_timestamp(Timestamp order_timestamp) {
        this.order_timestamp = order_timestamp;
    }

    public void setTotal_order_amount(double total_order_amount) {
        this.total_order_amount = total_order_amount;
    }

    public double getTotal_order_amount() {
        return total_order_amount;
    }

    public String toString() {
        return String.format("%s, %s, %s, %s, %s", order_id, item_code, quantity, order_timestamp, total_order_amount);
    }
}
