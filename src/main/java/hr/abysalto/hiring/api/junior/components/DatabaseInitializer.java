package hr.abysalto.hiring.api.junior.components;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private boolean dataInitialized = false;

    public boolean isDataInitialized() {
        return this.dataInitialized;
    }

    /**
     * InitDataController only runs on POST /init-data/; this runs once when the app starts.
     */
    @PostConstruct
    void createSchemaOnStartup() {
        initialize();
    }

    public void initialize() {
        if (this.dataInitialized) {
            return;
        }
        initTables();
        initData();
        this.dataInitialized = true;
    }

    private void initTables() {
        this.jdbcTemplate.execute("""
                 CREATE TABLE buyer (
                	 buyer_id INT auto_increment PRIMARY KEY,
                	 first_name varchar(100) NOT NULL,
                	 last_name varchar(100) NOT NULL,
                	 title varchar(100) NULL
                 );
                """);

        this.jdbcTemplate.execute("""
                 CREATE TABLE buyer_address (
                	 buyer_address_id INT auto_increment PRIMARY KEY,
                	 city varchar(100) NOT NULL,
                	 street varchar(100) NOT NULL,
                	 home_number varchar(100) NULL
                 );
                """);

        this.jdbcTemplate.execute("""
                    CREATE TABLE "order" (
                       order_nr INT auto_increment PRIMARY KEY,
                       buyer_id int NOT NULL,
                       order_status varchar(32) NOT NULL,
                       order_time datetime NOT NULL,
                       payment_option varchar(32) NOT NULL, -- Added this
                       delivery_address_id INT NOT NULL,
                       contact_number varchar(100) NULL,
                       note varchar(255) NULL,              -- Added this (Napomena)
                       currency varchar(50) NULL,
                       total_price decimal,
                       CONSTRAINT FK_order_to_buyer FOREIGN KEY (buyer_id) REFERENCES buyer (buyer_id),
                       CONSTRAINT FK_order_to_delivery_address FOREIGN KEY (delivery_address_id) REFERENCES buyer_address (buyer_address_id)
                    );
                """);

        this.jdbcTemplate.execute("""
                    CREATE TABLE order_item (
                       order_item_id INT auto_increment PRIMARY KEY,
                       order_id int NOT NULL,
                       order_key int,
                       name varchar(100) NOT NULL,
                       quantity smallint NOT NULL,
                       price decimal,
                       CONSTRAINT FK_order_item_to_order FOREIGN KEY (order_id) REFERENCES "order" (order_nr)
                    );
                """);
    }

    private void initData() {
        this.jdbcTemplate.execute("INSERT INTO buyer (first_name, last_name, title) VALUES ('Jabba', 'Hutt', 'the')");
        this.jdbcTemplate.execute("INSERT INTO buyer (first_name, last_name, title) VALUES ('Anakin', 'Skywalker', NULL)");
        this.jdbcTemplate.execute("INSERT INTO buyer (first_name, last_name, title) VALUES ('Jar Jar', 'Binks', NULL)");
        this.jdbcTemplate.execute("INSERT INTO buyer (first_name, last_name, title) VALUES ('Han', 'Solo', NULL)");
        this.jdbcTemplate.execute("INSERT INTO buyer (first_name, last_name, title) VALUES ('Leia', 'Organa', 'Princess')");

        this.jdbcTemplate.execute("INSERT INTO buyer_address (city, street, home_number) VALUES ('Tatooine', 'Dune Sea', '1')");
        this.jdbcTemplate.execute("INSERT INTO buyer_address (city, street, home_number) VALUES ('Coruscant', 'Galactic City', '500')");
        this.jdbcTemplate.execute("INSERT INTO buyer_address (city, street, home_number) VALUES ('Naboo', 'Otoh Gunga', 'Binks-2')");
        this.jdbcTemplate.execute("INSERT INTO buyer_address (city, street, home_number) VALUES ('Alderaan', 'Royal Palace', 'A1')");

        this.jdbcTemplate.execute("""
                    INSERT INTO "order" (buyer_id, order_status, order_time, payment_option, delivery_address_id, contact_number, note, currency, total_price) 
                    VALUES (5, 'PREPARING', NOW(), 'CASH', 4, '555-LEIA', 'Deliver to back gate', 'EUR', 150.00)
                """);

        this.jdbcTemplate.execute("""
                    INSERT INTO "order" (buyer_id, order_status, order_time, payment_option, delivery_address_id, contact_number, note, currency, total_price) 
                    VALUES (1, 'PREPARING', NOW(), 'CARD_UPFRONT', 1, '555-HUTT', 'Fragile!', 'EUR', 3000.50)
                """);

        this.jdbcTemplate.execute("INSERT INTO order_item (order_id, name, quantity, price) VALUES (1, 'Thermal Detonator', 2, 75.00)");
        this.jdbcTemplate.execute("INSERT INTO order_item (order_id, name, quantity, price) VALUES (2, 'Han Solo in Carbonite', 1, 3000.50)");
    }
}
