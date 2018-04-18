# 8INF957
Cours de Programmation Objet Avancé
## Projet de Fin de Session : 

# Réalisation d'un logiciel de partage de ressource

L'objectif de ce projet est la réalisation d'un algorithme de répartition de ressource entre plusieurs machines principalement sous Windows, avec un programme répartie qui serait un tri de tableau d'entier.

L'algortihme doit être capable de transférer le code utilisé de manière compilé, les données utilisées, ainsi que le résultat et capable de les assembler dans le cas du tri. Il doit gérer les connexions multiples, ainsi que les déconnexions.
Au niveau technique, le transfert du code source, se fera par un transfert de fichier non compilé du code en java, il sera compilé coté Slave. Les données seront transférer sous forme de json. Le monitoring des ressources, l'interface graphique et les connexions seront réalisés à l'aide de bibliothèques java.

# Pistes d'améliorations
Ce projet pourrait être améliorer en permettant d'utiliser d'autre fonction que le sort, tel que faire des opérations sur un DataSet complet à la manière d'un Hadoop. Le programme a été conçu pour utiliser plusieurs fonctions, qui prennent en entrée un liste d'entier et qui renvois une liste d'entier. 
On pourrait faire en sorte d'utiliser un fichier pour stocker la liste d'entrée de valeurs.
On pourrait gérer les ressources mis à disposition par les Slaves, définir combien de puissance il utiliserait pour faire les calculs. 
On pourrait aussi faire une interface graphique, pour permettre de définir les ressources et afficher ses résultats coté client, et pour permettre de définir quel fichier on veut utiliser pour l'entrée de données.


# Guide d'utilisation
1. Start Serveur avec l'argument "port" (numéro du port choisi) ex: 1234
2. Initialisation de la méthode: "Calc" et "sort" par défaut
3. Start plusieurs Client avec les arguments "ip" ( ip extérieur de la machine) et "port" ex: 127.0.0.1 1234
4. Rentrer "go" sur le serveur
5. Le programme s'exécute et affiche le résultat.


lien vers le diagramme de classe : 
https://drive.google.com/file/d/1r7uu5xwjkwhW10FK8WjthZ8iRImIhpB3/view?usp=sharing
