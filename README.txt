Afin de s'éxécuter correctement, ce programme nécéssite une instance MySQL opérationnelle.

# Etape 1 : Création de la base de données

- Créer une base de données nommé 'gabbler'
- Créer un utilisateur nommé 'gabbler' avec pour l'environnement de test le mot de passe "PPdVzydKdBL3tMYp" avec pour client cible 'localhost'.
- Affecter tout les droits sur la base 'gabbler' pour l'utilisateur 'gabbler'

Note : Ces trois étapes peuvent être réunis en une depuis phpMyAdmin
- Aller à l'onglet utilisateur
- Ajouter un utilisateur
- Entrer le nom d'utlisateur, le mot de passe, le client et cocher la case "Créer une base portant son nom et donner à cet utilisateur tous les privilèges sur cette base".



# Etape 2 : Configurer le projet

Ce projet utilise un wrapper Gradle, il n'est donc pas nécéssaire de disposer de gradle sur son environnement afin d'éxécuter le programme.

- Ouvrir un terminal et se placer à la racine du projet
- Entrer la commande suivante "gradlew"



# Etape 3 / Démarrer le programme

- Ouvrir un terminal et se placer à la racine du projet
- Entrer la commande 'gradlew :api:clean tomcatRunWar --stacktrace --info'

L'api est accéssible à l'URL 'http://127.0.0.1:8082/gabbler/'
Une documentation est généré à l'URL 'http://127.0.0.1:8082/gabbler/sdoc.jsp'


# Etape 4 : Insérer le jeu de données

Le programme doit être démarré au moins une fois avant l'insertion du jeu de données afin qu'il crée automatiquement les tables.

 - Executer le script d'insertion du jeu données
   Emplacement du script : api/insert_dev_data_mysql.sql