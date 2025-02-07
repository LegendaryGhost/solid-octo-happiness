CREATE TABLE utilisateur
(
    id                  SERIAL,
    email               VARCHAR(75)    NOT NULL,
    pdp                 VARCHAR(255),
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
    cours            NUMERIC(15, 2) NOT NULL,
    date_heure       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_cryptomonnaie INTEGER        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id)
);

CREATE TABLE _transaction
(
    id                 SERIAL,
    quantite           REAL           NOT NULL,
    cours              NUMERIC(15, 2) NOT NULL,
    date_heure         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type_transaction   VARCHAR(5)     NOT NULL, -- Achat / Vente
    taux_commission    NUMERIC(5, 2)  NOT NULL DEFAULT 0,
    montant_commission NUMERIC(15, 2) NOT NULL,
    id_cryptomonnaie   INTEGER        NOT NULL,
    id_utilisateur     INTEGER        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id)
);

CREATE TABLE operation
(
    id                 SERIAL,
    num_carte_bancaire VARCHAR(50)    NOT NULL,
    montant            NUMERIC(15, 2) NOT NULL,
    date_heure         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type_operation     VARCHAR(7)     NOT NULL, -- Dépôt / Retrait
    id_utilisateur     INTEGER        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id)
);

CREATE TABLE portefeuille
(
    id               SERIAL,
    quantite         REAL    NOT NULL DEFAULT 0,
    id_utilisateur   INTEGER NOT NULL,
    id_cryptomonnaie INTEGER NOT NULL,
    UNIQUE (id_utilisateur, id_cryptomonnaie),
    PRIMARY KEY (id),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id)
);

CREATE TABLE taux_commission
(
    id           SERIAL,
    valeur_achat NUMERIC(5, 2) NOT NULL,
    valeur_vente NUMERIC(5, 2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE statut_operation
(
    id           SERIAL,
    statut       VARCHAR(10) NOT NULL,
    date_heure   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_operation INTEGER     NOT NULL,
    UNIQUE (id_operation, statut),
    PRIMARY KEY (id),
    FOREIGN KEY (id_operation) REFERENCES operation (id)
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
