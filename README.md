
# Hashminia - Market Trading System

## Table of Contents

1. [Introduction](#introduction)
2. [System Description](#system-description)
3. [System Features](#system-features)
4. [Correctness Constraints](#correctness-constraints)
5. [Configuration files](#configuration-files)
    - [Properties file](#properties-file)
    - [Initialize data](#initialize-data)
6. [Service Level Requirements](#service-level-requirements)
7. [Glossary](#glossary)

## Introduction

This document outlines the requirements and constraints for a Market Trading System. The system facilitates commerce between sellers and buyers, consisting of multiple stores with various roles and functionalities for users.

## System Description

The Market Trading System (Market) allows for trading infrastructure between sellers and buyers. The system consists of multiple stores, each containing identifiable details and an inventory of products with different characteristics. Users of the system can be assigned various roles, and the system supports a wide range of operations to manage stores, items, and user interactions.

## System Features

1. **User Management**:
   - **Visitors**: Users can visit the marketplace as guests or registered members (subscribers).
   - **Subscribers**: Users can register and log in to become subscribers. Subscribers can assume roles such as buyers, sellers, and store managers.

2. **Store Management**:
   - **Store Creation**: Subscribers can create stores and become store owners.
   - **Inventory Management**: Store owners can manage their store's inventory, including adding, removing, and updating products.
   - **Policy Management**: Store owners can set and modify purchase and discount policies.
   - **Role Assignment**: Store owners can appoint other subscribers as co-owners or store managers with specific permissions.

3. **Trade Operations**:
   - **Product Search**: Users can search for products by various criteria such as name, category, keywords, price range, and ratings.
   - **Shopping Cart**: Each buyer has a unique shopping cart for each store.
   - **Purchases**: Users can purchase items in their cart, adhering to the store's purchase and discount policies.

4. **Notifications**:
   - **Real-Time Alerts**: The system provides real-time notifications to users for important events such as purchase confirmations, store status changes, and messages.
   - **Delayed Notifications**: Notifications for subscribers who are not online are stored and presented upon their next login.

5. **External Services**:
   - **Payment Services**: Integration with external payment services to process transactions.
   - **Delivery Services**: Integration with external delivery services to handle product shipments.

## Correctness Constraints

The system ensures the following constraints for correct operation:

- Unique identifiers for stores and products
- Consistent state management for user roles and permissions
- Proper handling of transactions and inventory updates
- Secure authentication and authorization mechanisms

## Configuration files

### Properties file


### Initialize data

The `init-commands.txt` file contains a sequence of commands that are used to initialize the system with predefined data. Each command represents an action that is executed by the system during startup. The commands follow a specific format and are executed in the order they appear in the file.

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

## Service Level Requirements

- **Availability**: The system should be available 99.9% of the time
- **Performance**: The system should handle up to 1000 concurrent users
- **Scalability**: The system should be able to scale to accommodate increased load
- **Security**: The system must comply with industry-standard security practices

## Glossary

- **Market**: The trading system that facilitates commerce between users
- **Store**: A virtual entity within the Market where items are listed
- **Item**: An item listed for sale in a store
- **User**: An individual who interacts with the Market, either as a buyer, seller, or admin
- **Transaction**: An exchange of goods or services for payment between a buyer and seller
