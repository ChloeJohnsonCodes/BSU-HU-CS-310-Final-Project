# BSU-HU-CS-310-Final-Project

## To Compile

`make`

## To Run

`java Driver <command> <arguments>`

## List of Commands

* GetItems <item_code or * for all>
  * Prints out items
* CreateItem <item_code> <description> <price> <inventory_amount>
  * Creates an item and prints it
* UpdateInventory <item_code> <inventory_amount>
  * Changes the inventory amount for the specified item
* CreateOrder <item_code> <quantity>
  * Creates an order and prints it
* DeleteItem <item_code>
  * Deletes the specified item
* GetOrders <item_code or * for all>
  * Prints out orders
* GetOrderDetails <order_id or * for all>
  * Prints out specific order information
