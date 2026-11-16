# Easy2Drive - Mission 3, groupe 4

On a repris en Java les écrans du projet PHP Easy2Drive. L’application utilise Swing pour les fenêtres et JDBC pour accéder à MariaDB.

## Préparer la base avec DbGate

1. Démarrer le serveur MariaDB et s’y connecter dans DbGate.
2. Créer une base vide nommée `easyToDriveM3`.
3. Ouvrir une requête sur cette base, coller le contenu de [bdeasy_m3.sql](sql/bdeasy_m3.sql) et exécuter le script entier avec **F5**. Le script crée les tables et ajoute les comptes d’essai.

## Lancer dans VS Code

Ouvrir le dossier du projet dans VS Code avec Java 21. Copier `config/db.properties.example` vers `config/db.properties`, puis renseigner les paramètres du serveur MariaDB.

```properties
db.host=ADRESSE_DU_SERVEUR
db.port=3306
db.name=easyToDriveM3
db.user=COMPTE_SQL
db.password=MOT_DE_PASSE_SQL
db.sslMode=PREFERRED
```

`db.properties` reste sur le poste et est ignoré par Git.
Copy-Item -LiteralPath 'C:\Users\Dino\Documents\Codex\2026-11-16\est-ce-que-tu-vois-mes\outputs\README.md' -Destination 'C:\Users\Dino\Documents\Projet Piscine Java\README.md' -Force
Après un clone du dépôt, compiler avec Maven pour créer le JAR si besoin.

```powershell
mvn clean package
```

Depuis le terminal PowerShell à la racine du projet, lancer le JAR. Voici la commande utilisée sur le poste de test.

```powershell
& 'C:\Program Files\Java\jdk-21.0.12.1\bin\java.exe' -jar .\target\easy2drive-gestion-1.0.0.jar
```

Se connecter avec `eleve` comme identifiant et mot de passe. Les comptes d’essai `formateur` et `directeur` ont aussi leur identifiant comme mot de passe initial.

## Fonctionnalités

- Se connecter, voir l’accueil et se déconnecter
- Consulter son profil et la fiche de son auto-école
- Modifier son e-mail et, pour un élève, son adresse
- Modifier l’adresse de son auto-école avec un compte directeur
- Changer son mot de passe. Il est enregistré sous forme de hash PBKDF2.

Le code est dans `src/main/java`. Les fenêtres sont dans `vue`, les actions dans `controleur`, les classes dans `modele` et les requêtes SQL dans `dao`.
