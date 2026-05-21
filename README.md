# TP 10 - LAB Développement Mobile - NumberBook

## Description

NumberBook est une application Android développée en Java permettant :

- de lire les contacts du téléphone ;
- d’afficher les contacts dans une liste ;
- de synchroniser les contacts vers une base MySQL ;
- de rechercher des contacts à distance.

Le projet utilise Android Studio, Retrofit, PHP et MySQL.

---

# Technologies utilisées

## Mobile
- Java
- Android Studio
- RecyclerView
- Retrofit
- Gson

## Backend
- PHP
- API REST
- PDO

## Base de données
- MySQL
- phpMyAdmin
- XAMPP

---

# Architecture générale

```text
Android App
     │
Retrofit + JSON
     │
     ▼
PHP REST API
     │
     ▼
MySQL Database
````

---

# Base de données

Nom de la base :

```sql
numberbook
```

Table principale :

```sql
CREATE TABLE contact (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    source VARCHAR(50) DEFAULT 'mobile',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

# Structure du backend

```text
numberbook-api/
│
├── config/
├── model/
├── service/
└── api/
```

---

# APIs utilisées

## insertContact.php

Permet d’insérer un contact dans MySQL.

## getAllContacts.php

Retourne tous les contacts.

## searchContact.php

Recherche un contact par nom ou numéro.

---

# Structure Android

```text
app/
│
├── MainActivity.java
├── Contact.java
├── ContactApi.java
├── RetrofitClient.java
├── ContactAdapter.java
└── activity_main.xml
```

---

# Permissions Android

```xml
<uses-permission android:name="android.permission.READ_CONTACTS"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

---

# Fonctionnement de l’application

1. L’utilisateur ouvre l’application.
2. L’application demande la permission des contacts.
3. Les contacts sont lus via ContentResolver.
4. Les contacts sont affichés dans RecyclerView.
5. L’utilisateur clique sur LOAD CONTACTS.
6. Les contacts sont chargés.
7. L’utilisateur clique sur SYNC CONTACTS.
8. Les contacts sont envoyés via Retrofit.
9. Le backend PHP reçoit les données.
10. Les données sont enregistrées dans MySQL.
11. L’utilisateur peut effectuer une recherche distante.
12. Les résultats sont affichés dans l’application.

---

# Retrofit

Retrofit permet :

* d’envoyer des requêtes HTTP ;
* de convertir JSON ↔ objets Java ;
* de communiquer facilement avec l’API REST.

---

# RecyclerView

RecyclerView permet :

* d’afficher efficacement les contacts ;
* d’actualiser les résultats ;
* d’améliorer les performances.

---

# Gestion des doublons

Une protection contre les doublons a été ajoutée :

```sql
ALTER TABLE contact
ADD UNIQUE(phone);
```

Cela empêche l’insertion multiple du même numéro.

---

# Tests réalisés

## Chargement des contacts

* permission accordée ;
* contacts affichés.

## Synchronisation

* insertion réussie dans MySQL.

## Recherche distante

* affichage des résultats corrects.

---
# Vidéo Démonstrative


https://github.com/user-attachments/assets/acd754f7-3212-42b0-a736-c54fc452f588



---

# Résultat final

Le projet permet :

* la lecture des contacts Android ;
* la communication client/serveur ;
* l’utilisation d’une API REST ;
* la persistance des données dans MySQL ;
* la recherche distante de contacts.

---

# Auteur

ASSEKNOUR SANA

ENSA Marrakech

```
```
