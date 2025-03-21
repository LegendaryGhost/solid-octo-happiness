INSERT INTO utilisateur (id, nom, prenom, date_naissance, email, fonds_actuel, pdp, date_heure_creation)
VALUES
    ('U1', 'Alice', 'Smith', '1990-05-10', 'alice@example.com', 1500.00, 'alice.jpg', CURRENT_TIMESTAMP),
    ('U2', 'Bob', 'Johnson', '1985-03-15', 'bob@example.com', 2500.00, 'bob.jpg', CURRENT_TIMESTAMP),
    ('U3', 'Charlie', 'Brown', '1992-07-22', 'charlie@example.com', 1000.00, 'charlie.jpg', CURRENT_TIMESTAMP),
    ('U4', 'Diana', 'Evans', '1995-09-12', 'diana@example.com', 1800.00, 'diana.jpg', CURRENT_TIMESTAMP),
    ('U5', 'Edward', 'Wilson', '1988-11-05', 'edward@example.com', 500.00, 'edward.jpg', CURRENT_TIMESTAMP);


INSERT INTO admin(email, mot_de_passe)
VALUES ('phoenix-coin.admin@gmail.com', 'admin1234');

INSERT INTO cryptomonnaie (designation, symbole)
VALUES ('Bitcoin', 'BTC'),
       ('Ethereum', 'ETH'),
       ('Binance Coin', 'BNB'),
       ('Cardano', 'ADA'),
       ('Solana', 'SOL'),
       ('Ripple', 'XRP'),
       ('Polkadot', 'DOT'),
       ('Dogecoin', 'DOGE'),
       ('Shiba Inu', 'SHIB'),
       ('Litecoin', 'LTC');

INSERT INTO cours_crypto (cours, date_heure, id_cryptomonnaie)
VALUES (45000.00, '2025-01-01 12:00:00', 1),
       (3000.00, '2025-01-01 12:00:00', 2),
       (250.00, '2025-01-01 12:00:00', 3),
       (1.30, '2025-01-01 12:00:00', 4),
       (20.00, '2025-01-01 12:00:00', 5),
       (0.90, '2025-01-01 12:00:00', 6),
       (4.50, '2025-01-01 12:00:00', 7),
       (0.07, '2025-01-01 12:00:00', 8),
       (0.00002, '2025-01-01 12:00:00', 9),
       (65.00, '2025-01-01 12:00:00', 10),
       (46000.00, '2025-01-02 12:00:00', 1),
       (3050.00, '2025-01-02 12:00:00', 2),
       (255.00, '2025-01-02 12:00:00', 3),
       (1.40, '2025-01-02 12:00:00', 4),
       (21.00, '2025-01-02 12:00:00', 5),
       (0.95, '2025-01-02 12:00:00', 6),
       (4.70, '2025-01-02 12:00:00', 7),
       (0.08, '2025-01-02 12:00:00', 8),
       (0.00003, '2025-01-02 12:00:00', 9),
       (66.00, '2025-01-02 12:00:00', 10);

-- Transactions du premier jour (2025-01-01)
INSERT INTO _transaction (quantite, cours, date_heure, type_transaction,
                          taux_commission, montant_commission, id_cryptomonnaie, id_utilisateur)
VALUES
    -- Alice achète du Bitcoin
    (0.001, 45000.00, '2025-01-01 09:05:00', 'ACHAT', 0.5, 22.50, 1, 'U1'),
    -- Bob vend ses Ethereum
    (0.5, 3000.00, '2025-01-01 09:15:00', 'VENTE', 0.5, 750.00, 2, 'U2'),
    -- Charlie achète du Solana
    (10.0, 20.00, '2025-01-01 09:30:00', 'ACHAT', 0.5, 100.00, 5, 'U3'),
    -- Diana investit dans le Dogecoin
    (1000.0, 0.07, '2025-01-01 09:45:00', 'ACHAT', 0.5, 35.00, 8, 'U4'),
    -- Edward achète du Litecoin
    (1.0, 65.00, '2025-01-01 10:00:00', 'ACHAT', 0.5, 32.50, 10, 'U5'),
    -- Alice vend une partie de son Bitcoin
    (0.0005, 46000.00, '2025-01-02 09:10:00', 'VENTE', 0.5, 115.00, 1, 'U1'),
    -- Bob achète du BNB
    (2.0, 255.00, '2025-01-02 09:20:00', 'ACHAT', 0.5, 255.00, 3, 'U2'),
    -- Charlie vend ses Solana
    (5.0, 21.00, '2025-01-02 09:35:00', 'VENTE', 0.5, 52.50, 5, 'U3'),
    -- Diana achète plus de Dogecoin
    (500.0, 0.08, '2025-01-02 09:50:00', 'ACHAT', 0.5, 20.00, 8, 'U4'),
    -- Edward investit dans le Cardano
    (100.0, 1.40, '2025-01-02 10:05:00', 'ACHAT', 0.5, 70.00, 4, 'U5');

-- Opérations du premier jour (2025-01-01)
INSERT INTO operation (num_carte_bancaire, montant, date_heure, type_operation, id_utilisateur)
VALUES
    -- Dépôt de Alice pour acheter des cryptos
    ('4532015112830366', 1500.00, '2025-01-01 09:03:00', 'DEPOT', 'U1'),
    -- Retrait de Bob après ses ventes
    ('5398228707871528', 750.00, '2025-01-01 09:20:00', 'RETRAIT', 'U2'),
    -- Dépôt de Charlie pour ses investissements
    ('6771549495586802', 1000.00, '2025-01-01 09:28:00', 'DEPOT', 'U3'),
    -- Retrait de Diana après ses gains
    ('4532015112830367', 35.00, '2025-01-01 09:48:00', 'RETRAIT', 'U4'),
    -- Dépôt initial d'Edward
    ('5398228707871529', 500.00, '2025-01-01 10:02:00', 'DEPOT', 'U5'),
    -- Retrait de Alice après ses ventes BTC
    ('4532015112830366', 115.00, '2025-01-02 09:12:00', 'RETRAIT', 'U1'),
    -- Dépôt de Bob pour nouveaux achats
    ('5398228707871528', 255.00, '2025-01-02 09:22:00', 'DEPOT', 'U2'),
    -- Retrait de Charlie après ses ventes SOL
    ('6771549495586802', 52.50, '2025-01-02 09:37:00', 'RETRAIT', 'U3'),
    -- Dépôt de Diana pour investir plus
    ('4532015112830367', 20.00, '2025-01-02 09:52:00', 'DEPOT', 'U4'),
    -- Retrait de Edward après ses gains ADA
    ('5398228707871529', 70.00, '2025-01-02 10:07:00', 'RETRAIT', 'U5');

INSERT INTO taux_commission (valeur_achat, valeur_vente)
VALUES (0.5, 0.5);

-- Ajout des cryptomonnaies favorites pour chaque utilisateur
INSERT INTO crypto_favoris (id_utilisateur, id_cryptomonnaie)
VALUES
    -- Favoris d'Alice (id=U1)
    ('U1', 1),  -- Bitcoin
    ('U1', 2),  -- Ethereum
    ('U1', 5),  -- Solana
    -- Favoris de Bob (id=U2)
    ('U2', 1),  -- Bitcoin
    ('U2', 3),  -- Binance Coin
    ('U2', 10), -- Litecoin
    -- Favoris de Charlie (id=U3)
    ('U3', 4),  -- Cardano
    ('U3', 5),  -- Solana
    ('U3', 7),  -- Polkadot
    -- Favoris de Diana (id=U4)
    ('U4', 8),  -- Dogecoin
    ('U4', 9),  -- Shiba Inu
    ('U4', 6),  -- Ripple
    -- Favoris d'Edward (id=U5)
    ('U5', 2),  -- Ethereum
    ('U5', 4),  -- Cardano
    ('U5', 10); -- Litecoin
