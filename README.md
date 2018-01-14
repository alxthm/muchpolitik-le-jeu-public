# muchpolitik-le-jeu
 
Ceci est le repo GitHub du jeu où se trouve toutes les données du jeu importantes, et le plus à jour possible. Il est privé.

## Structure
Dans le répertoire ```core/src/``` se trouve **le code du jeu**, il est surtout utile au développeur.

Dans le répertoire ```android/assets/``` se trouvent toutes les **ressources graphiques, audio, et données du jeu (cartes, dialogues)**. On y retrouve :
- Les niveaux dans le répertoire ```android/assets/data/levels/```
- Les dialogues dans le répertoire ```android/assets/data/dialogs/```
- Les graphismes dans le répertoire ```android/assets/data/graphics/```
- Les fichiers audio dans le répertoire ```android/assets/data/ausdio/```

Si vous avez des modifications à faire, **essayez de vous relire avant de poster, et de ne modifier que les fichiers nécessaires** (pour limiter les erreurs).

## Comment travailler sur le jeu en restant à jour
1. Se créer un compte GitHub
2. Télécharger GitHub Desktop (ou équivalent)
3. Aller voir sur google ou suivre les petits guides expliquant comment GitHub marche
4. Abracadabra plus de problèmes de versions différentes chez chacun

## Tester ou admirer le produit fini
#### Pour desktop (Windows, Mac, ou Linux)
- Pour tester le jeu en modifiant des niveaux (réservé aux **level-designers**) : Aller à la page des "Releases" (onglet "code", dans la barre blanche). Télécharger la dernière version exécutable pour les level-designers (ex : desktop-1.0.1-leveldesign.jar). Placer le fichier dans le dossier ```android/assets/data/levels/``` (sur son ordinateur), et le lancer pour tester les changements effectués sur les fichiers .tmx ou .json.
- Pour simplement jouer : La dernière version exécutable du jeu (au format .jar) se trouve également en allant voir les "Releases" (https://github.com/garbadrom/muchpolitik-le-jeu/releases). Normalement il suffit d'avoir java installé. Bientôt disponible sur Steam.

#### Pour Android
Le jeu est officiellement sorti sur le Play Store : https://play.google.com/store/apps/details?id=com.muchpolitik.lejeu.
