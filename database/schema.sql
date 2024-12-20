CREATE TABLE profil(
   id SERIAL,
   email VARCHAR(250) ,
   fond_actuel NUMERIC(15,2)  ,
   PRIMARY KEY(id)
);

CREATE TABLE cryptomonnaie(
   id SERIAL,
   designation VARCHAR(150) ,
   PRIMARY KEY(id)
);

CREATE TABLE type_action(
   id SERIAL,
   designation VARCHAR(100) ,
   PRIMARY KEY(id)
);

CREATE TABLE type_transaction(
   id SERIAL,
   designation VARCHAR(50) ,
   PRIMARY KEY(id)
);

CREATE TABLE cours_crypto(
   id SERIAL,
   cours_actuel NUMERIC(15,2)  ,
   date_cours TIMESTAMP,
   id_cryptomonnaie INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_cryptomonnaie) REFERENCES cryptomonnaie(id)
);

CREATE TABLE historique_crypto(
   id SERIAL,
   date_action TIMESTAMP,
   cours NUMERIC(15,2)  ,
   montant NUMERIC(15,2)  ,
   id_profil INTEGER NOT NULL,
   id_cryptomonnaie INTEGER NOT NULL,
   id_type_action INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_profil) REFERENCES profil(id),
   FOREIGN KEY(id_cryptomonnaie) REFERENCES cryptomonnaie(id),
   FOREIGN KEY(id_type_action) REFERENCES type_action(id)
);

CREATE TABLE historique_fond(
   id SERIAL,
   date_transaction TIMESTAMP,
   montant NUMERIC(15,2)  ,
   id_profil INTEGER NOT NULL,
   id_type_transaction INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_profil) REFERENCES profil(id),
   FOREIGN KEY(id_type_transaction) REFERENCES type_transaction(id)
);
