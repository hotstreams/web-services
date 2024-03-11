CREATE TABLE PRODUCT (
    id bigserial PRIMARY KEY,
    code bigint NOT NULL,
    name varchar(255) NOT NULL,
    category varchar(255) NOT NULL,
    quantity bigint NOT NULL,
    cost bigint NOT NULL
);

INSERT INTO PRODUCT (code, name, category, quantity, cost) VALUES (
    100,
    'Stove',
    'Equipment',
    11,
    4300
);
INSERT INTO PRODUCT (code, name, category, quantity, cost) VALUES (
    101,
    'Fridge',
    'Equipment',
    6,
    5600
);
INSERT INTO PRODUCT (code, name, category, quantity, cost) VALUES (
    205,
    'Chicken breast',
    'Food',
    30,
    500
);
INSERT INTO PRODUCT (code, name, category, quantity, cost) VALUES (
    206,
    'Canned meat',
    'Food',
    55,
    300
);
INSERT INTO PRODUCT (code, name, category, quantity, cost) VALUES (
    210,
    'Vegetable box',
    'Food',
    60,
    640
);
INSERT INTO PRODUCT (code, name, category, quantity, cost) VALUES (
    315,
    'Grease washer',
    'Chemicals',
    27,
    150
);
INSERT INTO PRODUCT (code, name, category, quantity, cost) VALUES (
    316,
    'Household soap',
    'Chemicals',
    30,
    450
);
INSERT INTO PRODUCT (code, name, category, quantity, cost) VALUES (
    519,
    'Stapler',
    'Stationery',
    22,
    1300
);
