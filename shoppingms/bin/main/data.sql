INSERT INTO invoice (id, number, description, customer, created_at, status) VALUES(1, '0001', 'invoice office items', 1, NOW(),'CREATED');

INSERT INTO invoice_item ( invoice, product, quantity, price ) VALUES(1, 1 , 1, 178.89);
INSERT INTO invoice_item ( invoice, product, quantity, price)  VALUES(1, 2 , 2, 12.5);
INSERT INTO invoice_item ( invoice, product, quantity, price)  VALUES(1, 3 , 1, 40.06);