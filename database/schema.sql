CREATE TABLE utilisateur
(
    id                  SERIAL,
    email               VARCHAR(75)    NOT NULL,
    fonds_actuel        NUMERIC(15, 2) NOT NULL DEFAULT 0,
    token               VARCHAR(255),
    date_heure_maj      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_heure_creation TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (email),
    PRIMARY KEY (id)
);

CREATE TABLE cryptomonnaie
(
    id          SERIAL,
    designation VARCHAR(150) NOT NULL,
    UNIQUE (designation),
    PRIMARY KEY (id)
);

CREATE TABLE cours_crypto
(
    id               SERIAL,
    cours_actuel     NUMERIC(15, 2) NOT NULL,
    date_heure       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_cryptomonnaie INTEGER        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id)
);

-- Achat / Vente
CREATE TABLE type_transaction
(
    id          SERIAL,
    designation VARCHAR(50) NOT NULL,
    UNIQUE (designation),
    PRIMARY KEY (id)
);

CREATE TABLE historique_transaction
(
    id                  SERIAL,
    cours               NUMERIC(15, 2) NOT NULL,
    quantite            REAL           NOT NULL,
    date_heure          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_utilisateur      INTEGER        NOT NULL,
    id_cryptomonnaie    INTEGER        NOT NULL,
    id_type_transaction INTEGER        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id),
    FOREIGN KEY (id_type_transaction) REFERENCES type_transaction (id)
);

-- Dépôt / Retrait
CREATE TABLE type_operation
(
    id          SERIAL,
    designation VARCHAR(50) NOT NULL,
    UNIQUE (designation),
    PRIMARY KEY (id)
);

CREATE TABLE historique_fonds
(
    id                 SERIAL,
    num_carte_bancaire VARCHAR(50)    NOT NULL,
    montant            NUMERIC(15, 2) NOT NULL,
    date_heure         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_utilisateur     INTEGER        NOT NULL,
    id_type_operation  INTEGER        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id),
    FOREIGN KEY (id_type_operation) REFERENCES type_operation (id)
);

CREATE TABLE crypto_favoris
(
    id               SERIAL,
    id_utilisateur   INTEGER NOT NULL,
    id_cryptomonnaie INTEGER NOT NULL,
    UNIQUE (id_utilisateur, id_cryptomonnaie),
    PRIMARY KEY (id),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id)
);
