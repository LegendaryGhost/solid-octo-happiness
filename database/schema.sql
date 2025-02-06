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

CREATE TABLE historique_transaction
(
    id                    SERIAL,
    quantite              REAL           NOT NULL,
    cours                 NUMERIC(15, 2) NOT NULL,
    date_heure            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type_transaction      VARCHAR(5)     NOT NULL, -- Achat / Vente
    taux_commission_achat NUMERIC(5, 2)  NOT NULL DEFAULT 0,
    taux_commission_vente NUMERIC(5, 2)  NOT NULL DEFAULT 0,
    id_cryptomonnaie      INTEGER        NOT NULL,
    id_utilisateur        INTEGER        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_cryptomonnaie) REFERENCES cryptomonnaie (id),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id)
);

CREATE TABLE historique_fonds
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
    valeur_vente NUMERIC(5, 2) NOT NULL
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
