CREATE TABLE utilisateur
(
    id                  SERIAL,
    email               VARCHAR(75) UNIQUE NOT NULL,
    fonds_actuel        NUMERIC(15, 2)     NOT NULL DEFAULT 0,
    token               VARCHAR(255),
    date_heure_maj      TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_heure_creation TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE cryptomonnaie
(
    id          SERIAL,
    designation VARCHAR(150),
    PRIMARY KEY (id)
);

CREATE TABLE type_action
(
    id          SERIAL,
    designation VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE type_transaction
(
    id          SERIAL,
    designation VARCHAR(50),
    PRIMARY KEY (id)
);

CREATE TABLE cours_crypto
(
    id               SERIAL,
    cours_actuel     NUMERIC(15, 2),
    date_cours       TIMESTAMP,
    id_cryptomonnaie INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id)
);

CREATE TABLE historique_transaction_crypto
(
    id                  SERIAL,
    date_action         TIMESTAMP,
    cours               NUMERIC(15, 2),
    quantite            NUMERIC(15, 2),
    id_profil           INTEGER NOT NULL,
    id_cryptomonnaie    INTEGER NOT NULL,
    id_type_transaction INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_profil) REFERENCES profil (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id),
    FOREIGN KEY (id_type_transaction) REFERENCES type_transaction (id)
);

CREATE TABLE historique_fond
(
    id                 SERIAL,
    date_transaction   TIMESTAMP,
    num_carte_bancaire VARCHAR(50) NOT NULL,
    montant            NUMERIC(15, 2),
    id_profil          INTEGER     NOT NULL,
    id_type_action     INTEGER     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_profil) REFERENCES profil (id),
    FOREIGN KEY (id_type_action) REFERENCES type_action (id)
);

CREATE TABLE crypto_favoris
(
    id               SERIAL,
    id_profil        INTEGER NOT NULL,
    id_cryptomonnaie INTEGER NOT NULL,
    UNIQUE (id_profil, id_cryptomonnaie),
    PRIMARY KEY (id),
    FOREIGN KEY (id_profil) REFERENCES profil (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id)
);
