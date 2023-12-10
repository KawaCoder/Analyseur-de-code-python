# Analyseur-de-code-python ![Sans_titre-removebg-preview(1)](https://user-images.githubusercontent.com/67145585/196044343-5ed36533-1f72-4eb7-9d91-872e005c7b79.png)
Un détécteur d'erreurs fréquentes en python écrit en Java.
note: ce programme est à voir comme un concept, plus qu'un outil. Les algorithmes de détéction sont assez médiocres.

Cet outil est capable de détecter (avec une fiabilité à revoir) les erreurs suivantes:

- Signe égal seul dans une condition (au lieu de "==")
- Variables mal orthographiées (via l'algorithme de [Jaro-Winkler](https://fr.wikipedia.org/wiki/Distance_de_Jaro-Winkler)
- Erreurs de syntaxe des mots-clefs les plus fréquents (Aussi grâce à Jaro-Winkler)
- Parenthèses en trop/manquantes
- Deux points manquants
- Guillemets manquants

## Téléchargements
[![hey](https://img.shields.io/badge/Download%20.exe-181717?style=for-the-badge&color=blue&logo=windows)](https://github.com/DR34M-M4K3R/Analyseur-de-code-python/releases/download/1.0.0/ERREUR-DETECTOR-3000.exe)

[![hey](https://img.shields.io/badge/Download%20.jar-181717?style=for-the-badge&color=red&logo=java)](https://github.com/DR34M-M4K3R/Analyseur-de-code-python/releases/download/1.0.0/ERREUR-DETECTOR-3000.jar)

## Screenshots:


![image](https://user-images.githubusercontent.com/67145585/196042480-3213b439-3ce2-44ca-b387-d06e412161a6.png)![image](https://user-images.githubusercontent.com/67145585/196042509-1100fdf0-f870-4bdd-a4a2-6e779a376da8.png)![image](https://user-images.githubusercontent.com/67145585/196042521-f646a671-8491-45df-9902-027a55aa7e39.png)

(Le programme admet qu'une erreur de ce type vaut 10€, référence à une "private joke". Ne soyez pas choqué si il vous demande une somme d'argent relative au nombre d'erreurs. Vous ne devez en aucun cas verser cette somme au développeur. (sauf si vous voulez))
