
# Hashminia - Market Trading System

## Table of Contents

1. [Introduction](#introduction)
2. [System Description](#system-description)
3. [System Features](#system-features)
4. [Configuration files](#configuration-files)
    - [Properties file](#properties-file)
        - [Database Configuration](#database-configuration)
        - [Datasource Credentials](#datasource-credentials)
        - [JPA Configuration](#jpa-configuration)
        - [Connection Pooling](#connection-pooling)
        - [Caching](#caching)
        - [Logging Configuration](#logging-configuration)
        - [Profile Management](#profile-management)
        - [Initialization Properties](#initialization-properties)
    - [Initialize data](#initialize-data)
5. [Security](#security)
6. [Glossary](#glossary)

## Introduction

This document outlines the requirements and constraints for a Market Trading System. The system facilitates commerce between sellers and buyers, consisting of multiple stores with various roles and functionalities for users.

## System Description

The Market Trading System (Market) allows for trading infrastructure between sellers and buyers. The system consists of multiple stores, each containing identifiable details and an inventory of products with different characteristics. Users of the system can be assigned various roles, and the system supports a wide range of operations to manage stores, items, and user interactions.

## System Features

1. **User Management**:
   - **Guests**: Users can visit the marketplace as guests or registered members (subscribers).
   - **Subscribers**: Users can register and log in to become subscribers. Subscribers can assume roles such as buyers, sellers, and store managers.

2. **Store Management**:
   - **Store Creation**: Subscribers can create stores and become store owners.
   - **Inventory Management**: Store owners can manage their store's inventory, including adding, removing, and updating items.
   - **Policy Management**: Store owners can set and modify purchase and discount policies.
   - **Role Assignment**: Store owners can appoint other subscribers as co-owners or store managers with specific permissions.

3. **Trade Operations**:
   - **Product Search**: Users can search for items by various criteria such as name, category and keywords.
   - **Shopping Cart**: Each buyer has a unique shopping cart.
   - **Purchases**: Users can purchase items in their cart, adhering to the store's purchase and discount policies.

4. **Notifications**:
   - **Real-Time Alerts**: The system provides real-time notifications to users for important events such as purchase confirmations and messages for store managers.
   - **Delayed Notifications**: Notifications for subscribers who are not online are stored and presented upon their next login.

5. **External Services**:
   - **Payment Services**: Integration with external payment services to process transactions.
   - **Delivery Services**: Integration with external delivery services to handle product shipments.

## Configuration files

### Properties file

This project uses a Spring Boot configuration file named `application.properties`, that can be found in 'Market-Trading-System/src/main/resources', which defines various settings for connecting to a MySQL database, configuring JPA, and managing logging. Below is a breakdown of the key configurations and how to use them.

#### Database Configuration

- **Datasource URL**: 
  ```properties
  spring.datasource.url=jdbc:mysql://34.172.134.83:3306/noam_DB

Specifies the URL to connect to the MySQL database. Replace the IP and database name with your own as needed.

#### Datasource Credentials

```properties
spring.datasource.username=dbManager
spring.datasource.password=dbManager
```

Set the database username and password for authentication.

#### JPA Configuration
```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

- `ddl-auto=update`: Automatically updates the database schema on application startup.
- `show-sql=true`: Enables logging of SQL statements executed by Hibernate.
- `dialect`: Specifies the SQL dialect for MySQL 8

#### Connection Pooling

This configuration uses HikariCP as the connection pool. Key properties include:

- **Connection Timeout**:

  ```properties
  spring.datasource.hikari.connection-timeout=5000

Maximum time to wait for a connection from the pool (in milliseconds).

- **Idle Timeout:**

  ```properties
  spring.datasource.hikari.idle-timeout=60000
  
Time a connection can sit idle before being removed (in milliseconds).

- **Maximum Pool Size:**

  ```properties
  spring.datasource.hikari.maximum-pool-size=10

Maximum number of connections in the pool.

#### Caching

Enable second-level caching with:

```properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.internal.JCacheRegionFactory
```
This improves performance by caching entities and queries.

#### Logging Configuration

Logging settings to manage output levels:

```properties
logging.level.org.springframework=OFF
logging.level.org.hibernate=OFF
```

These lines turn off logging for Spring and Hibernate to reduce console clutter. Adjust levels as needed for debugging.

#### Profile Management

Set the active profile with:

```properties
spring.profiles.active=db
```

This specifies the active configuration profile, which can be used to differentiate settings for various environments (e.g., development, production).

#### Initialization Properties

```properties
data.initialization=true
data.admin.register=true
data.delete.on_shutdown=true
```

These properties control data initialization at startup, whether to register an admin user, and if data should be deleted on shutdown.





### Initialize data

The `init-commands.txt` file, that can be found in 'Market-Trading-System/src/main/resources', contains a sequence of commands that are used to initialize the system with predefined data. Each command represents an action that is executed by the system during startup. The commands follow a specific format and are executed in the order they appear in the file.

##### File Format

Each line in the `init-commands.txt` file represents a command to be executed. The general format of a command is:

commandName(arg1, arg2, ..., argN)

Where:
- `commandName` is the name of the command to be executed.
- `arg1, arg2, ..., argN` are the arguments required by the command.

#### Example File

Here is an example `init-commands.txt` file:

register(u2, u2, 20)
register(u3, u3, 20)
register(u4, u4, 20)
register(u5, u5, 20)
register(u6, u6, 20)
login(u2, u2)
createStore(u2, s1, s1 store description)
addItemToStore(u2, s1, Bamba, snack for kids, 30, 20, [snacks])
assignStoreManager(u2, s1, u3, [UPDATE_ITEM])
assignStoreOwner(u2, s1, u4)
assignStoreOwner(u2, s1, u5)
logout(u2)

#### Example Commands

- `register(username, password, age)` - Registers a new user.
- `login(username, password)` - Logs in a user.
- `createStore(ownerUsername, storeName, storeDescription)` - Creates a new store.
- `addItemToStore(ownerUsername, storeName, itemName, itemDescription, price, quantity, categories)` - Adds an item to a store.
- `assignStoreManager(ownerUsername, storeName, managerUsername, permissions)` - Assigns a manager to a store.
- `assignStoreOwner(ownerUsername, storeName, newOwnerUsername)` - Assigns a new owner to a store.
- `logout(username)` - Logs out a user.

#### Note

Ensure that the commands and their arguments match the expected format and data types required by the system to avoid any execution errors.

## Security

- The system encrypt the users passwords
- Unique identifiers for stores and products
- Consistent state management for user roles and permissions
- Proper handling of transactions and inventory updates
- Secure authentication and authorization mechanisms

## Glossary

- **Market**: The trading system that facilitates commerce between users
- **Store**: A virtual entity within the Market where items are listed
- **Item**: An item listed for sale in a store
- **User**: An individual who interacts with the Market, either as a buyer, seller, or admin
- **Transaction**: An exchange of goods or services for payment between a buyer and seller
