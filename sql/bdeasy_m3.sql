DROP TABLE `Archive`;
DROP TABLE `Formateur`;
DROP TABLE `Eleve`;
DROP TABLE `Utilisateur`;
DROP TABLE `AutoEcole`;
DROP TABLE `Planning`;
DROP TABLE `Compte`;
CREATE TABLE `Archive` ( 
  `arIdArchive` INT AUTO_INCREMENT NOT NULL,
  `arDateNaissance` DATE NOT NULL,
  `arRue` VARCHAR(255) NOT NULL,
  `arCodePostale` VARCHAR(10) NOT NULL,
  `arVille` VARCHAR(100) NOT NULL,
  `arDateInscription` DATE NOT NULL,
  `arNEPH` VARCHAR(20) NOT NULL,
  `arIdEleveArchive` INT NOT NULL,
  `arNom` VARCHAR(100) NULL DEFAULT NULL ,
  `arPrenom` VARCHAR(100) NULL DEFAULT NULL ,
  `arEmail` VARCHAR(255) NULL DEFAULT NULL ,
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
  `elEstArchive` TINYINT NOT NULL DEFAULT 0 ,
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
ALTER TABLE `Archive` ADD CONSTRAINT `fk-arIdEleveArchive` FOREIGN KEY (`arIdEleveArchive`) REFERENCES `Eleve` (`elIdUtilisateur`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Formateur` ADD CONSTRAINT `fk-foIdUtilisateur` FOREIGN KEY (`foIdUtilisateur`) REFERENCES `Utilisateur` (`utId`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Eleve` ADD CONSTRAINT `fk-elIdUtilisateur` FOREIGN KEY (`elIdUtilisateur`) REFERENCES `Utilisateur` (`utId`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Eleve` ADD CONSTRAINT `fk-elIdFormaEleve` FOREIGN KEY (`elIdFormaEleve`) REFERENCES `Formateur` (`foIdUtilisateur`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Utilisateur` ADD CONSTRAINT `fk-utIdAutoEcole` FOREIGN KEY (`utIdAutoEcole`) REFERENCES `AutoEcole` (`auId`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Planning` ADD CONSTRAINT `fk-plIdFormateur` FOREIGN KEY (`plIdFormateur`) REFERENCES `Formateur` (`foIdUtilisateur`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Planning` ADD CONSTRAINT `fk-plIdEleve` FOREIGN KEY (`plIdEleve`) REFERENCES `Eleve` (`elIdUtilisateur`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `Compte` ADD CONSTRAINT `fk-coId` FOREIGN KEY (`coId`) REFERENCES `Utilisateur` (`utId`) ON DELETE CASCADE ON UPDATE NO ACTION;
