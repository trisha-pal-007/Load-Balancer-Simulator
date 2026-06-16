 Load Balancer Simulator

A Java-based **Load Balancer Simulator** that distributes incoming requests across multiple servers using different routing strategies. This project demonstrates core backend engineering concepts, data structures, and system design patterns.

Overview

This project simulates how real-world load balancers operate by routing requests across a pool of servers using multiple strategies such as:

* Round Robin
* Least Connections
* Weighted Routing
* Hash-Based Routing (optional)

It also exposes REST APIs for managing servers, sending requests, switching strategies, and monitoring performance.

Key Concepts Demonstrated

* Object-Oriented Design (OOP)
* Strategy Design Pattern
* Data Structures (Queues, Hashing, Circular Linked List)
* REST API Development
* Basic Concurrency Simulation
* System Design Principles

System Architecture

The project is divided into four main layers:

1. **Request Layer** – Represents incoming traffic
2. **Load Balancing Layer** – Decides which server to route to
3. **Server Simulation Layer** – Handles request processing and queues
4. **REST API Layer** – Allows external interaction

Features

 Add / Remove servers dynamically  
 Simulate incoming requests  
 Switch load balancing strategies at runtime  
 Track server metrics and performance  
 Queue-based request processing  
 Simulated processing delays

Load Balancing Strategies

Round Robin

* Cycles through servers sequentially
* Implemented using a circular linked list

Least Connections

* Routes to server with the fewest active connections
* Adapts to dynamic load

Weighted Routing

* Servers receive traffic proportional to their capacity
* Ideal for heterogeneous environments

Project Structure

project-root/
│
├── src/
│   ├── model/              # Request, Server, enums
│   ├── strategy/           # Load balancing strategies
│   ├── service/            # LoadBalancer, Metrics, Simulation
│   ├── controller/         # REST APIs
│   └── util/               # Helpers (hashing, etc.)
│
├── docs/                   # Reference documents (Word files)
│
├── README.md
└── pom.xml / build.gradle

 Example Workflow

1. Add servers to the pool
2. Choose a load balancing strategy
3. Send simulated requests
4. Monitor how traffic is distributed
5. Compare performance across strategies

 Metrics Tracked

* Total requests
* Requests per server
* Active connections
* Queue sizes
* Latency (simulated)
* Failed requests

Tech Stack

* **Language:** Java
* **Framework (optional):** Spring Boot
* **Data Structures:** Queue, HashMap, Circular Linked List
* **Build Tool:** Maven 

Testing Strategy

* Unit testing for each strategy
* Integration testing for request routing
* Load simulations with different traffic patterns
* Edge case handling (no servers, server failure, etc.)

 Future Enhancements

 Server health checks
 Failover & retry logic
 Consistent hashing
 Dashboard for visualization
 Latency-based routing

Why This Project?

This project is designed to be:

 Interview-ready
 Conceptually strong
 Real-world applicable
 Easily extensible

It goes beyond CRUD applications by demonstrating **system design + algorithms + backend engineering** in one project.



Just tell me 👍
