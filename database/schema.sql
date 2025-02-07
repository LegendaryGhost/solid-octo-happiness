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
    valeur_vente NUMERIC(5, 2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE statut_historique_fonds
(
    id                  SERIAL,
    statut              VARCHAR(10) NOT NULL,
    date_heure          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_historique_fonds INTEGER     NOT NULL,
    UNIQUE (id_historique_fonds, statut),
    PRIMARY KEY (id),
    FOREIGN KEY (id_historique_fonds) REFERENCES historique_fonds (id)
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
