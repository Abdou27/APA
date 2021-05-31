# APA

**Membres du groupe :**
- Abderrahim BENMELOUKA
- Gabriel DAGORNE
- Hakim BEGHAMI
- Othmane GOUGILI


Pour la première execution, s'il n'existe pas d'utilisateurs, une méthode helper va remplir la BDD avec des données arbitraires pour pouvoir comprendre le flux de l'application plus facilement.

**Utilisateurs dans le démo :**
- Docteurs :
  - amabella.quinn@gmail.com
  - ranger.laberge@gmail.com
- Intervenants :
  - aubrey.rocher@gmail.com
  - archard.duffet@gmail.com
- Patients :
  - nicolas.berthelette@gmail.com
  - cendrillon.legault@gmail.com

Mot de passe pour tous les utilisateurs du démo : **123456**

L'application commence par la MainActivity qui :
- Redirige vers l'écran de connexion si aucun utilisateur n'est connecté (et l'écran de connexion peut rediriger vers l'écran de registration) 
-	Si un utilisateur est connecté, elle redirige vers une autre activité en fonction du type d'utilisateur.

Le médecin sélectionne un patient et lui crée et attribue un parcours qui contient des activités génériques (une activité est considérée générique si elle est pas rattachée à un intervenant). Après la création, il peut aussi recliquer sur le parcours/l'activité pour la modifier.

L'intervenant sélectionnes un parcours qui contient une activité générique et modifie cette activité (en détaillant la description par exemple) et en ajoutant des sessions. Lors de cette modification, l'activité est automatiquement rattachée à l'intervenant qui l'a modifié. Il peut aussi modifier les sessions qu'il a crée.

Le patient dispose d'une vue du calendrier qui lui permet de voir les sessions qui lui sont attribuées par différents intervenants. Il peut cliquer sur la session pour pouvoir la replanifier en proposant une autre date et heure et il peut aussi cliquer sur des session passée pour donner le taux de completion et son retour sur la session.

Tous les utilisateurs peuvent modifier leurs profiles en cliquant sur le bouton dans toolbar.

Tous les utilisateurs ont accès a une activité de statistiques qui leurs montrent des données différents selon leurs types (patients, docteurs, etc..).

**Démo video :**

[![Démo video](http://img.youtube.com/vi/XUffhUCbcww/0.jpg)](https://youtu.be/XUffhUCbcww "Démo")
