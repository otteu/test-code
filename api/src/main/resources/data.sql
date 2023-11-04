insert into product(product_number, type, selling_status, name, price)
values ('001', 'HANDMADE', 'SELLING', '아메리카노', '4000'),
       ('002', 'HANDMADE', 'HOLD', '카페라떼', '4500'),
       ('003', 'BAKERY', 'STOP_SELLING', '크로아상', '3500');


insert into stock(product_number, quantity)
values ('001', 2),
       ('002', 2);