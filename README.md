# JCoinche_Serveur

Serveur gérant un jeu de coinche simplifié.
Projet créée dans le cadre du module B5-Java.


# Règles

- Se joue à 4 joueurs (2 équipes de 2 joueurs)
- La première équipe à atteindre 1000 points à gagné

- Au début de chaque tour le joueur ayant gagné la manché précédente commence et décide de la couleur à jouer durant ce tour.
- Chaque joueur doit jouer une carte de la couleur annoncé par le premier joueur, si ce n'est pas possible il doit jouer de la couleur de l'atout (qui change alléatoirement à chaque tour) si ce n'est pas possible il peut jouer n'importe quelle carte restante
- Le joueur ayant jouée la carte* la plus haute gagne la manche (*carte de la couleur annoncé par le premier joueur, ou de l'atout si un atout à été posé)
- A la fin de la manche les points sont ajouté au gagnant celon la règle suivante :
        
| Cartes | A l'atout | Normal |
| :---:  |  :---:    |  :---: |
| As     | 11        | 11     |
| Roi    | 4         | 4      |
| Dame   | 3         | 3      |
| Valet  | 20        | 20     |
| Dix    | 10        | 10     |
| Neuf   | 14        | 0      |
| Huit   | 0         | 0      |
| Sept   | 0         | 0      |

# RFC

- Serveur

| Message | Commentaire |
| :---:  |  :---:    |
| INFO:Name-Team     | Envoyé au début de la partie. Correspond aux info du joueurs : son nom et sa team (1 ou 2)   |
| CARDS: nb-carte-carte-...    | Envoyé à chaque tour. Correspond à la main d'un joueur. Nb : nombre de cartes du joueurs suivi par ses cartes au format RANG_COULEUR |
| PLAY:Value-Atout   | Envoyé au joueur à qui c'est le tour. Correspond aux couleurs qu'il doit jouer (Value est Null si c'est le premier de la manche)         |
| ERROR  | Si la carte jouée est inexistante ou si le joueur n'avait pas le droit de la jouer        |
| PLAYED:Name-Team-Card    | Envoyé à tous les clients quand une carte est jouée. Nom du joueur, sa team et la carte posée        |
| SCORE:TeamA - TeamB   | Envoyé à la fin de chaque manche. Correspond aux score des deux équipes        |
| QUIT   | Envoyé lorsqu'un joueur quitte la partie. Arrête le jeu et ferme les sessions des clients         |

- Client 

| Message | Commentaire |
| :---:  |  :---:    |
| RANG_COLOR     | Envoie au serveur une carte à jouer   |
| QUIT| Envoie au serveur que le client à quitté sa session |

# Usage

`java -jar jcoinche-server.jar`

- Choisir le port (entre 1024 et 65535)
- Choisir le nombre de joueurs réels (entre 0 et 4) -> correspond aux nombre de clients et d'IA

Pour tester avec un client 

`telnet IP PORT`

# Features

- Choix du port
- IA 



