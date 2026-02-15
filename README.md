# 📚 Secure Book API

Une API REST robuste développée avec **Spring Boot 3**, sécurisée par **JWT** et **Spring Security**.
Ce projet gère une bibliothèque de livres avec des rôles utilisateurs (ADMIN vs USER) et inclut des tests automatisés.

## 🚀 Technologies

* **Java 21**
* **Spring Boot 3** (Web, Security, Data JPA, Validation)
* **Database :** H2 (In-memory) + Flyway (Migrations)
* **Security :** JSON Web Token (JWT) + BCrypt
* **Testing :** JUnit 5, Mockito, MockMvc

## ✨ Fonctionnalités

### 🔐 Sécurité & Auth
* Inscription & Connexion (`/auth/register`, `/auth/authenticate`).
* Authentification via **JWT Token** (Stateless).
* Gestion des Rôles :
    * **USER** : Peut consulter les livres.
    * **ADMIN** : Peut créer et supprimer des livres.

### 📚 Gestion des Livres
* CRUD complet (Create, Read, Update, Delete).
* Validation des données (ex: prix positif, titre obligatoire).
* Gestion globale des exceptions (Réponses JSON propres en cas d'erreur 400/404/500).