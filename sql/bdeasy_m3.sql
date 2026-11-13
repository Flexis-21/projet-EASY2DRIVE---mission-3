CREATE TABLE `Archive` ( 
  `arIdArchive` INT AUTO_INCREMENT NOT NULL,
  `arDateNaissance` DATE NOT NULL,
  `arRue` VARCHAR(255) NOT NULL,
  `arCodePostale` VARCHAR(10) NOT NULL,
  `arVille` VARCHAR(100) NOT NULL,
  `arDateInscription` DATE NOT NULL,
  `arNEPH` VARCHAR(20) NOT NULL,
  `arIdEleveArchive` INT NOT NULL,
   PRIMARY KEY (`arIdArchive`)
)
ENGINE = InnoDB;
CREATE TABLE `Formateur` ( 
  `foIdUtilisateur` INT NOT NULL,
  `foNumeroAutorisation` VARCHAR(50) NOT NULL,
   PRIMARY KEY (`foIdUtilisateur`),
  CONSTRAINT `numeroAutorisation` UNIQUE (`foNumeroAutorisation`)
)
ENGINE = InnoDB;
CREATE TABLE `Eleve` ( 
  `elIdUtilisateur` INT NOT NULL,
  `elDateNaissance` DATE NOT NULL,
  `elRue` VARCHAR(255) NOT NULL,
  `elCodePostal` VARCHAR(10) NOT NULL,
  `elVille` VARCHAR(100) NOT NULL,
  `elDateInscription` DATE NULL DEFAULT current_timestamp() ,
  `elNEPH` VARCHAR(20) NULL DEFAULT NULL ,
  `elIdFormaEleve` INT NULL DEFAULT NULL ,
   PRIMARY KEY (`elIdUtilisateur`)
)
ENGINE = InnoDB;
CREATE TABLE `Utilisateur` ( 
  `utId` INT AUTO_INCREMENT NOT NULL,
  `utNom` VARCHAR(100) NOT NULL,
  `utPrenom` VARCHAR(100) NOT NULL,
  `utEmail` VARCHAR(255) NOT NULL,
  `utIdAutoEcole` INT NOT NULL,
  `utEstDirecteur` TINYINT NOT NULL DEFAULT 0 ,
  `utEstSecretaire` TINYINT NOT NULL DEFAULT 0 ,
   PRIMARY KEY (`utId`)
)
ENGINE = InnoDB;
CREATE TABLE `AutoEcole` ( 
  `auId` INT AUTO_INCREMENT NOT NULL,
  `auRaisonSociale` VARCHAR(255) NOT NULL,
  `auRue` VARCHAR(255) NOT NULL,
  `auCodePostal` VARCHAR(10) NOT NULL,
  `auVille` VARCHAR(100) NOT NULL,
   PRIMARY KEY (`auId`)
)
ENGINE = InnoDB;
CREATE TABLE `Planning` ( 
  `plId` INT AUTO_INCREMENT NOT NULL,
  `plIdEleve` INT NOT NULL,
  `plIdFormateur` INT NOT NULL,
  `plDuree` INT NOT NULL,
  `plDateHeureDebut` DATETIME NOT NULL,
   PRIMARY KEY (`plId`)
)
ENGINE = InnoDB;
CREATE TABLE `Compte` ( 
  `coId` INT AUTO_INCREMENT NOT NULL,
  `coLogin` VARCHAR(25) NOT NULL,
  `coMdp_hash` VARCHAR(250) NOT NULL,
  `coMdp_sel` VARCHAR(250) NOT NULL,
   PRIMARY KEY (`coId`)
)
ENGINE = InnoDB;
SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO `Formateur` (`foIdUtilisateur`, `foNumeroAutorisation`) VALUES (2, 'AUTH-123456');
INSERT INTO `Formateur` (`foIdUtilisateur`, `foNumeroAutorisation`) VALUES (7, 'AUTH-789456');
INSERT INTO `Eleve` (`elIdUtilisateur`, `elDateNaissance`, `elRue`, `elCodePostal`, `elVille`, `elDateInscription`, `elNEPH`, `elIdFormaEleve`) VALUES (3, '2008-03-22', '8 rue nodot', '21000', 'Dijon', '2026-03-22', NULL, 2);
INSERT INTO `Eleve` (`elIdUtilisateur`, `elDateNaissance`, `elRue`, `elCodePostal`, `elVille`, `elDateInscription`, `elNEPH`, `elIdFormaEleve`) VALUES (5, '2007-01-15', '12 rue montparnasse', '21800', 'Quetigny', '2026-03-22', NULL, 2);
INSERT INTO `Utilisateur` (`utId`, `utNom`, `utPrenom`, `utEmail`, `utIdAutoEcole`, `utEstDirecteur`, `utEstSecretaire`) VALUES (1, 'Brissiaud', 'Fabrice', 'fabbris@bdeasy.com', 1, 8, 0);
INSERT INTO `Utilisateur` (`utId`, `utNom`, `utPrenom`, `utEmail`, `utIdAutoEcole`, `utEstDirecteur`, `utEstSecretaire`) VALUES (2, 'Thevenot', 'Delphine', 'delthev@bdeasy.com', 1, 9, 0);
INSERT INTO `Utilisateur` (`utId`, `utNom`, `utPrenom`, `utEmail`, `utIdAutoEcole`, `utEstDirecteur`, `utEstSecretaire`) VALUES (3, 'Bald', 'Nathan', 'natbald@bdeasy.com', 1, 10, 0);
INSERT INTO `Utilisateur` (`utId`, `utNom`, `utPrenom`, `utEmail`, `utIdAutoEcole`, `utEstDirecteur`, `utEstSecretaire`) VALUES (4, 'Bald', 'Nathan', 'natbald2@bdeasy.com', 1, 11, 0);
INSERT INTO `Utilisateur` (`utId`, `utNom`, `utPrenom`, `utEmail`, `utIdAutoEcole`, `utEstDirecteur`, `utEstSecretaire`) VALUES (5, 'Brissiaud', 'Fabrice', 'fabbris2@bdeasy.com', 2, 12, 0);
INSERT INTO `Utilisateur` (`utId`, `utNom`, `utPrenom`, `utEmail`, `utIdAutoEcole`, `utEstDirecteur`, `utEstSecretaire`) VALUES (6, 'Thevenot', 'Delphine', 'delthev2@bdeasy.com', 2, 13, 0);
INSERT INTO `Utilisateur` (`utId`, `utNom`, `utPrenom`, `utEmail`, `utIdAutoEcole`, `utEstDirecteur`, `utEstSecretaire`) VALUES (7, 'Brisbare', 'Kevin', 'kevbrib@bdeasy.com', 2, 15, 0);
INSERT INTO `AutoEcole` (`auId`, `auRaisonSociale`, `auRue`, `auCodePostal`, `auVille`) VALUES (1, 'Auto-École Excellence', '10 Rue de la Conduite', '75001', 'Paris');
INSERT INTO `AutoEcole` (`auId`, `auRaisonSociale`, `auRue`, `auCodePostal`, `auVille`) VALUES (2, 'Paris Auto-Ecole', 'place république', '75013', 'Paris');
INSERT INTO `Compte` (`coId`, `coLogin`, `coMdp_hash`, `coMdp_sel`) VALUES (1, 'directeur', '904ba1ddba9279ae6a24a2869b213d8e12a1096ca231dc6dee762f84f4a10a0b', 'Kw0V68XC7VZ/mLsFQL7k3g==');
INSERT INTO `Compte` (`coId`, `coLogin`, `coMdp_hash`, `coMdp_sel`) VALUES (2, 'formateur', 'f08b17661162c244abd3d1026b0a2b0dadd9674fc3e40963223876fc50cd07f6', '4ACnnfjVU0297bx+RIZMuA==');
SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE `Archive` ADD CONSTRAINT `fk-arIdEleveArchive` FOREIGN KEY (`arIdEleveArchive`) REFERENCES `Eleve` (`elIdUtilisateur`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Formateur` ADD CONSTRAINT `fk-foIdUtilisateur` FOREIGN KEY (`foIdUtilisateur`) REFERENCES `Utilisateur` (`utId`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Eleve` ADD CONSTRAINT `fk-elIdFormaEleve` FOREIGN KEY (`elIdFormaEleve`) REFERENCES `Formateur` (`foIdUtilisateur`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Eleve` ADD CONSTRAINT `fk-elIdUtilisateur` FOREIGN KEY (`elIdUtilisateur`) REFERENCES `Utilisateur` (`utId`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Utilisateur` ADD CONSTRAINT `fk-utIdAutoEcole` FOREIGN KEY (`utIdAutoEcole`) REFERENCES `AutoEcole` (`auId`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Planning` ADD CONSTRAINT `fk-plIdEleve` FOREIGN KEY (`plIdEleve`) REFERENCES `Eleve` (`elIdUtilisateur`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Planning` ADD CONSTRAINT `fk-plIdFormateur` FOREIGN KEY (`plIdFormateur`) REFERENCES `Formateur` (`foIdUtilisateur`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Compte` ADD CONSTRAINT `fk-coId` FOREIGN KEY (`coId`) REFERENCES `Utilisateur` (`utId`) ON DELETE CASCADE ON UPDATE NO ACTION;
