CREATE TABLE utilisateur
(
    id                  VARCHAR,
    email               VARCHAR(75)    NOT NULL,
    nom                 VARCHAR(75)    NOT NULL,
    prenom              VARCHAR(75)    NOT NULL,
    date_naissance      DATE           NOT NULL,
    pdp                 VARCHAR(255),
    fonds_actuel        NUMERIC(15, 2) NOT NULL DEFAULT 0,
    identityflow_token  TEXT,
    expo_push_token     TEXT,
    date_heure_creation TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (email),
    PRIMARY KEY (id)
);

CREATE TABLE admin
(
    id           SERIAL,
    email        VARCHAR(75)  NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    UNIQUE (email),
    PRIMARY KEY (id)
);

CREATE TABLE cryptomonnaie
(
    id          SERIAL,
    designation VARCHAR(150) NOT NULL,
    symbole     VARCHAR(10)  NOT NULL,
    UNIQUE (designation),
    UNIQUE (symbole),
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
    id_utilisateur     VARCHAR        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id)
);

CREATE TABLE operation
(
    id                 SERIAL,
    num_carte_bancaire VARCHAR(50)    NOT NULL,
    montant            NUMERIC(15, 2) NOT NULL,
    date_heure         TIMESTAMP      NOT NULL,
    type_operation     VARCHAR(7)     NOT NULL, -- Dépôt / Retrait
    id_utilisateur     VARCHAR        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id)
);

CREATE TABLE portefeuille
(
    id               SERIAL,
    quantite         REAL    NOT NULL DEFAULT 0,
    id_utilisateur   VARCHAR NOT NULL,
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

CREATE TABLE suivi_operation
(
    id           SERIAL,
    statut       VARCHAR(10) NOT NULL,
    date_heure   TIMESTAMP   NOT NULL,
    id_operation INTEGER     NOT NULL,
    UNIQUE (id_operation, statut),
    PRIMARY KEY (id),
    FOREIGN KEY (id_operation) REFERENCES operation (id)
);

CREATE TABLE crypto_favoris
(
    id               SERIAL,
    id_utilisateur   VARCHAR NOT NULL,
    id_cryptomonnaie INTEGER NOT NULL,
    UNIQUE (id_utilisateur, id_cryptomonnaie),
    PRIMARY KEY (id),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id)
);
