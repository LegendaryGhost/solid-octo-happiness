INSERT INTO utilisateur (email, fonds_actuel, pdp,  date_heure_maj, date_heure_creation)
VALUES
  ('alice@example.com', 1500.00, 'alice.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('bob@example.com', 2500.00, 'bob.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('charlie@example.com', 1000.00, 'charlie.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('diana@example.com', 1800.00, 'diana.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('edward@example.com', 500.00, 'edward.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO cryptomonnaie (designation, symbole)
VALUES
    ('Bitcoin', 'BTC'),
    ('Ethereum', 'ETH'),
    ('Binance Coin', 'BNB'),
    ('Cardano', 'ADA'),
    ('Solana', 'SOL'),
    ('Ripple', 'XRP'),
    ('Polkadot', 'DOT'),
    ('Dogecoin', 'DOGE'),
    ('Shiba Inu', 'SHIB'),
    ('Litecoin', 'LTC');

INSERT INTO type_action (designation)
VALUES
  ('Achat'),
  ('Vente');

INSERT INTO type_transaction (designation)
VALUES
  ('Dépôt'),
  ('Retrait');

INSERT INTO cours_crypto (cours_actuel, date_cours, id_cryptomonnaie)
VALUES
  (45000.00, '2025-01-01 12:00:00', 1),
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

INSERT INTO historique_crypto (date_action, cours, quantite, id_profil, id_cryptomonnaie, id_type_action)
VALUES
  ('2025-01-01 13:00:00', 45000.00, 0.1, 1, 1, 1),
  ('2025-01-01 13:30:00', 3000.00, 0.5, 2, 2, 1),
  ('2025-01-01 14:00:00', 250.00, 4, 3, 3, 1),
  ('2025-01-01 15:00:00', 1.30, 1000, 4, 4, 1),
  ('2025-01-01 16:00:00', 20.00, 50, 5, 5, 1),
  ('2025-01-02 13:00:00', 46000.00, 0.2, 1, 1, 1),
  ('2025-01-02 13:30:00', 3050.00, 0.3, 2, 2, 2),
  ('2025-01-02 14:00:00', 255.00, 3, 3, 3, 1),
  ('2025-01-02 15:00:00', 1.40, 2000, 4, 4, 2),
  ('2025-01-02 16:00:00', 21.00, 100, 5, 5, 1);

  INSERT INTO etat_fond (designation) VALUES 
    ('Validée'),
    ('Refusée'),
    ('En attente');

INSERT INTO historique_fond (date_transaction, montant, id_profil, id_type_transaction,num_carte_bancaire, id_etat)
VALUES
  ('2025-01-01 10:00:00', 1000.00, 1, 1,'CRT1',3),
  ('2025-01-01 11:00:00', 500.00, 2, 2,'CRT2',3),
  ('2025-01-01 12:00:00', 2000.00, 3, 1,'CRT3',3),
  ('2025-01-01 13:00:00', 700.00, 4, 2,'CRT4',3),
  ('2025-01-01 14:00:00', 300.00, 5, 1,'CRT5',3),
  ('2025-01-02 10:00:00', 1500.00, 1, 1,'CRT1',3),
  ('2025-01-02 11:00:00', 800.00, 2, 2,'CRT2',3),
  ('2025-01-02 12:00:00', 2500.00, 3, 1,'CRT3',3),
  ('2025-01-02 13:00:00', 900.00, 4, 2,'CRT4',3),
  ('2025-01-02 14:00:00', 400.00, 5, 1,'CRT5',3);

