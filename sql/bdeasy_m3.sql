CREATE TABLE `Formateur` ( 
  `idUtilisateur` INT NOT NULL,
  `numeroAutorisation` VARCHAR(50) NOT NULL,
   PRIMARY KEY (`idUtilisateur`),
  CONSTRAINT `numeroAutorisation` UNIQUE (`numeroAutorisation`)
)
ENGINE = InnoDB;
CREATE TABLE `Eleve` ( 
  `idUtilisateur` INT NOT NULL,
  `dateNaissance` DATE NOT NULL,
  `rue` VARCHAR(255) NOT NULL,
  `codePostal` VARCHAR(10) NOT NULL,
  `ville` VARCHAR(100) NOT NULL,
  `dateInscription` DATE NULL DEFAULT current_timestamp() ,
  `NEPH` VARCHAR(20) NULL DEFAULT NULL ,
  `idFormaEleve` INT NULL DEFAULT NULL ,
   PRIMARY KEY (`idUtilisateur`),
  CONSTRAINT `NEPH` UNIQUE (`NEPH`)
)
ENGINE = InnoDB;
CREATE TABLE `Utilisateur` ( 
  `id` INT AUTO_INCREMENT NOT NULL,
  `nom` VARCHAR(100) NOT NULL,
  `prenom` VARCHAR(100) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `idAutoEcole` INT NOT NULL,
  `idUser` INT NULL DEFAULT NULL ,
   PRIMARY KEY (`id`),
  CONSTRAINT `email` UNIQUE (`email`)
)
ENGINE = InnoDB;
CREATE TABLE `AutoEcole` ( 
  `id` INT AUTO_INCREMENT NOT NULL,
  `raisonSociale` VARCHAR(255) NOT NULL,
  `rue` VARCHAR(255) NOT NULL,
  `codePostal` VARCHAR(10) NOT NULL,
  `ville` VARCHAR(100) NOT NULL,
   PRIMARY KEY (`id`)
)
ENGINE = InnoDB;
CREATE TABLE `Compte` ( 
  `id` INT AUTO_INCREMENT NOT NULL,
  `login` VARCHAR(25) NOT NULL,
  `mdp_hash` VARCHAR(250) NOT NULL,
  `mdp_sel` VARCHAR(250) NOT NULL,
   PRIMARY KEY (`id`)
)
ENGINE = InnoDB;
CREATE TABLE `Directeur` ( 
  `idUtilisateur` INT NOT NULL,
   PRIMARY KEY (`idUtilisateur`)
)
ENGINE = InnoDB;
SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO `Formateur` (`idUtilisateur`, `numeroAutorisation`) VALUES (2, 'AUTH-123456');
INSERT INTO `Formateur` (`idUtilisateur`, `numeroAutorisation`) VALUES (7, 'AUTH-789456');
INSERT INTO `Eleve` (`idUtilisateur`, `dateNaissance`, `rue`, `codePostal`, `ville`, `dateInscription`, `NEPH`, `idFormaEleve`) VALUES (3, '2008-03-22', '8 rue nodot', '21000', 'Dijon', '2026-03-22', NULL, 2);
INSERT INTO `Eleve` (`idUtilisateur`, `dateNaissance`, `rue`, `codePostal`, `ville`, `dateInscription`, `NEPH`, `idFormaEleve`) VALUES (5, '2007-01-15', '12 rue montparnasse', '21800', 'Quetigny', '2026-03-22', NULL, 2);
INSERT INTO `Utilisateur` (`nom`, `prenom`, `email`, `idAutoEcole`, `idUser`) VALUES ('Brissiaud', 'Fabrice', 'fabbris@bdeasy.com', 1, 8);
INSERT INTO `Utilisateur` (`nom`, `prenom`, `email`, `idAutoEcole`, `idUser`) VALUES ('Thevenot', 'Delphine', 'delthev@bdeasy.com', 1, 9);
INSERT INTO `Utilisateur` (`nom`, `prenom`, `email`, `idAutoEcole`, `idUser`) VALUES ('Bald', 'Nathan', 'natbald@bdeasy.com', 1, 10);
INSERT INTO `Utilisateur` (`nom`, `prenom`, `email`, `idAutoEcole`, `idUser`) VALUES ('Bald', 'Nathan', 'natbald2@bdeasy.com', 1, 11);
INSERT INTO `Utilisateur` (`nom`, `prenom`, `email`, `idAutoEcole`, `idUser`) VALUES ('Brissiaud', 'Fabrice', 'fabbris2@bdeasy.com', 2, 12);
INSERT INTO `Utilisateur` (`nom`, `prenom`, `email`, `idAutoEcole`, `idUser`) VALUES ('Thevenot', 'Delphine', 'delthev2@bdeasy.com', 2, 13);
INSERT INTO `Utilisateur` (`nom`, `prenom`, `email`, `idAutoEcole`, `idUser`) VALUES ('Brisbare', 'Kevin', 'kevbrib@bdeasy.com', 2, 15);
INSERT INTO `AutoEcole` (`raisonSociale`, `rue`, `codePostal`, `ville`) VALUES ('Auto-École Excellence', '10 Rue de la Conduite', '75001', 'Paris');
INSERT INTO `AutoEcole` (`raisonSociale`, `rue`, `codePostal`, `ville`) VALUES ('Paris Auto-Ecole', 'place république', '75013', 'Paris');
INSERT INTO `Compte` (`login`, `mdp_hash`, `mdp_sel`) VALUES ('directeur', '904ba1ddba9279ae6a24a2869b213d8e12a1096ca231dc6dee762f84f4a10a0b', 'Kw0V68XC7VZ/mLsFQL7k3g==');
INSERT INTO `Compte` (`login`, `mdp_hash`, `mdp_sel`) VALUES ('formateur', 'f08b17661162c244abd3d1026b0a2b0dadd9674fc3e40963223876fc50cd07f6', '4ACnnfjVU0297bx+RIZMuA==');
INSERT INTO `Compte` (`login`, `mdp_hash`, `mdp_sel`) VALUES ('eleve', '74b178c0d002e98cac7b6625ad96c6f804a9bd127addf0eb868b61a209e94946', 'nBdGterjuvTdt9DSsY8bqA==');
INSERT INTO `Compte` (`login`, `mdp_hash`, `mdp_sel`) VALUES ('nbald', '6ed7c7da3fea5270296e417f769d544501088472f633a057d526a1e0e04bce60', 'cHeb2ZNfDikraR62Wfv2bA==');
INSERT INTO `Compte` (`login`, `mdp_hash`, `mdp_sel`) VALUES ('fbrissiaud', 'fb896621fee2aa25f3b2657fd5473ad45c99e48ece777f934f0a96cf1c557e88', 'Yhpk8dQXLX8bvM7ouDrVRg==');
INSERT INTO `Compte` (`login`, `mdp_hash`, `mdp_sel`) VALUES ('dthevenot', '87fa3a53924e8c52fafa9053d1227de32185a7e4fbaf7f816bb69381c88e1475', 'gO25nC5VYAhiF/Mcw8aklQ==');
INSERT INTO `Compte` (`login`, `mdp_hash`, `mdp_sel`) VALUES ('kbrisbare', 'bb5526df27ce8c7afba572d463e6b89f4c5cc39cbf771806b64769ed20e670b0', 'wiYOUPKIiwF7DDEudrdXmg==');
INSERT INTO `Directeur` (`idUtilisateur`) VALUES (1);
INSERT INTO `Directeur` (`idUtilisateur`) VALUES (6);
SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE `Formateur` ADD CONSTRAINT `Formateur_ibfk_1` FOREIGN KEY (`idUtilisateur`) REFERENCES `Utilisateur` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE `Eleve` ADD CONSTRAINT `formaEleve` FOREIGN KEY (`idFormaEleve`) REFERENCES `Formateur` (`idUtilisateur`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `Eleve` ADD CONSTRAINT `Eleve_ibfk_1` FOREIGN KEY (`idUtilisateur`) REFERENCES `Utilisateur` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE `Utilisateur` ADD CONSTRAINT `FK_idUser` FOREIGN KEY (`idUser`) REFERENCES `Compte` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `Utilisateur` ADD CONSTRAINT `Utilisateur_ibfk_1` FOREIGN KEY (`idAutoEcole`) REFERENCES `AutoEcole` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE `Directeur` ADD CONSTRAINT `Directeur_ibfk_1` FOREIGN KEY (`idUtilisateur`) REFERENCES `Utilisateur` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT;
