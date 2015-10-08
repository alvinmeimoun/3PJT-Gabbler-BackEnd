Afin d'ouvrir le projet avec IntelliJ

- Cliquer sur "Import Project"
- Selectionner le dossier racine du projet
- Selectionner "Import for external model" et choisir Gradle
- Selcetionner "Use default wrapper"

Un fichier .iml sera crée ce qui permettra d'ouvrir le projet à l'avenir et non plus l'importer.
Les fichiers .iml sont spécifiques à l'environnement de travail, ils ne doivent donc pas être versionnés.

BUG IntelliJ présent dans la version 14.1.3 sous OS X :
    Appeler les commandes Gradle depuis IDEA uniquement sur le ROOT project, l'appeler depuis le module api n'utiliserai pas la version 8 de Java.