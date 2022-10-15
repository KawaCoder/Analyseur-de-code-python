# Analyseur-de-code-python
Un détécteur d'erreurs fréquentes en python écrit en Java.

Cet outil est capable de détecter avec une fiabilité relative les erreurs suivantes:

- Signe égal seul dans une condition (au lieu de "==")
- Variables mal orthographiées (via l'algorithme de [Jaro-Winkler](https://fr.wikipedia.org/wiki/Distance_de_Jaro-Winkler)
- Erreurs de syntaxe des mots-clefs les plus fréquents (Aussi grâce à Jaro-Winkler)
- Parenthèses en trop/manquantes
- Deux points manquants

## Screenshots:

![image](https://user-images.githubusercontent.com/67145585/195997655-6b7326fa-048c-45e3-a99c-333b9e8d6ad2.png)


(Le programme admet qu'une erreur de ce type vaut 10€, référence à une "private joke". Ne soyez pas choqué si il vous demande une somme d'argent relative au nombre d'erreurs. Vous ne devez en aucun cas verser cette somme au développeur. (sauf si vous voulez))
