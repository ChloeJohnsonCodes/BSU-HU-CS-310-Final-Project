
CREATE TABLE items(
	item_id INT AUTO_INCREMENT,
    item_code VARCHAR(10) NOT NULL,
    description VARCHAR(50),
    price DECIMAL(13,2) DEFAULT 0,
    inventory_amount INT DEFAULT 0,
    PRIMARY KEY(item_id),
    UNIQUE(item_code)
);

CREATE TABLE orders(
	order_id INT AUTO_INCREMENT,
    item_code VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    order_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(order_id)
);