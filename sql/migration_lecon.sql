-- =====================================================================
-- Migration : Ajout de la table Lecon pour le planning (Mission 3)
-- A transmettre a l'equipe SISR pour execution sur le serveur MySQL
-- =====================================================================

CREATE TABLE IF NOT EXISTS `Lecon` (
    `id` INT AUTO_INCREMENT NOT NULL,
    `idEleve` INT NOT NULL,
    `idFormateur` INT NOT NULL,
    `dateLecon` DATE NOT NULL,
    `heureDebut` TIME NOT NULL,
    `dureeMinutes` INT NOT NULL DEFAULT 60,
    `etat` VARCHAR(20) NOT NULL DEFAULT 'Planifiée', -- 'Planifiée', 'Effectuée', 'Annulée'
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_lecon_eleve` FOREIGN KEY (`idEleve`) REFERENCES `Eleve` (`idUtilisateur`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_lecon_formateur` FOREIGN KEY (`idFormateur`) REFERENCES `Formateur` (`idUtilisateur`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- Donnees de test initiales
-- Formateurs existants : 2, 7
-- Eleves existants : 3, 5
INSERT INTO `Lecon` (`idEleve`, `idFormateur`, `dateLecon`, `heureDebut`, `dureeMinutes`, `etat`) VALUES
(3, 2, CURDATE(), '09:00:00', 60, 'Planifiée'),
(5, 2, CURDATE(), '11:00:00', 60, 'Planifiée'),
(3, 7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', 120, 'Planifiée');
