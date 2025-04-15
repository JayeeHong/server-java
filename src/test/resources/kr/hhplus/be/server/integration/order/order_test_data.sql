-- 사용자
INSERT INTO users (id, name) VALUES (1, '테스트유저');

-- 잔액 (충분한 금액 보유)
INSERT INTO balances (user_id, amount, transaction_type, created_at)
VALUES (1, 50000, 'CHARGE', NOW());

-- 상품
INSERT INTO product (id, name, price, stock)
VALUES (101, '테스트 상품', 10000, 100);
