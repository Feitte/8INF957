# 8INF957
Cours de Programmation Objet Avancé

# Projet de Fin de Session : 
# Réalisation d'un logiciel de partage de ressource

L'objectif de ce projet est la réalisation d'un algorithme de répartition de ressource entre plusieurs machines principalement sous Windows ,avec un programme répartie qui pourrait être changeable qui peut être : 
- un tri
- un MapReduce
L'algortihme doit être capable de transférer le code utilisé de manière compilé, les données utilisé, ainsi que le résultat et capable de les assembler dans le cas du tri. Il doit prendre en compte les ressources disponibles, et mise a disposition de chaque machine. Et enfin un interface utilisateur graphique sur la machine master, pour l'affichage et l'entrée de donnée ainsi que la répartition voulu des calculs, et une sur les machines slaves pour choisir les ressources mise à disposition.
Au niveau technique, le transfert du code source, se fera par un transfert de fichier compilé du code en java. Les données seront transférer sous forme de json. Le monitoring des ressources, l'interface graphique et les connexions seront réalisés à l'aide de bibliothèques java.
