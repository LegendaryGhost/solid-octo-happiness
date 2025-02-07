CREATE TABLE profil
(
    id          SERIAL,
    email       VARCHAR(250),
    fond_actuel NUMERIC(15, 2),
    photo       VARCHAR(255),
    token       VARCHAR(255),
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

CREATE TABLE historique_crypto
(
    id                 SERIAL,
    date_action        TIMESTAMP,
    cours              NUMERIC(15, 2),
    quantite           NUMERIC(15, 2),
    taux_commission    NUMERIC(5, 2)  NOT NULL DEFAULT 0,
    montant_commission NUMERIC(15, 2) NOT NULL,
    id_profil          INTEGER        NOT NULL,
    id_cryptomonnaie   INTEGER        NOT NULL,
    id_type_action     INTEGER        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_profil) REFERENCES profil (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id),
    FOREIGN KEY (id_type_action) REFERENCES type_action (id)
);

CREATE TABLE etat_fond
(
    id          SERIAL,
    designation VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE taux_commission
(
    id           SERIAL,
    valeur_achat NUMERIC(5, 2) NOT NULL,
    valeur_vente NUMERIC(5, 2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE historique_fond
(
    id                  SERIAL,
    date_transaction    TIMESTAMP,
    num_carte_bancaire  VARCHAR(50) NOT NULL,
    montant             NUMERIC(15, 2),
    id_profil           INTEGER     NOT NULL,
    id_type_transaction INTEGER     NOT NULL,
    id_etat             INTEGER     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_profil) REFERENCES profil (id),
    FOREIGN KEY (id_type_transaction) REFERENCES type_transaction (id),
    FOREIGN KEY (id_etat) REFERENCES etat_fond (id)
);
