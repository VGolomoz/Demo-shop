CREATE TABLE IF NOT EXISTS products
(
    id    UUID PRIMARY KEY,
    name  VARCHAR(255)   NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS discount_policies
(
    id             UUID PRIMARY KEY,
    type           VARCHAR(50)    NOT NULL,
    threshold      INT            NOT NULL,
    discount_value DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS product_discount_policy
(
    product_id         UUID NOT NULL,
    discount_policy_id UUID NOT NULL,
    PRIMARY KEY (product_id, discount_policy_id),
    FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    FOREIGN KEY (discount_policy_id) REFERENCES discount_policies (id) ON DELETE CASCADE
);