1) Setup and run instructions
Need Java 17 run time environment
Need RabbitMQ -  i attached docker file (docker-compose.yml) for rabbitmq  and run cmd 'docker-compose up -d'
Optional - Need intellij / Eclipse IDEA


2) API usage examples
Data store in-memory.

Login :

POST  http://localhost:8080/api/auth/login
Request JSON -
user - {"username": "user",
   "password": "user123"}

or

admin - {"username": "admin",
"password": "admin123"}


Order:

POST http://localhost:8080/api/orders
Authorization - Bearer Toke Required
Request JSON -
{
"customerId": "CUST-001",
"product": "Headphone",
"amount": 500.50
}

GET http://localhost:8080/api/orders?customerId
Authorization - Bearer Toke Required
** Only Admin role can access this api **

GET http://localhost:8080/api/orders/{orderId}
Authorization - Bearer Toke Required
** ADMIN can view all orders, USER can only view their own orders **


3) Explanation of Camel routes


Complete End-to-End Flow:

Route 1: Order → File

API receives order request
Order is saved in memory
Camel route is triggered (direct:write-order-file)
Order object is converted to JSON
File is created at: input/orders/order-<orderId>.json


Route 2: File → RabbitMQ

Camel polls the input/orders folder
Reads JSON file
Converts JSON → Order object
Validates: orderId is not null , customerId is not null , amount > 0 , Logs file name and order ID
Order is sent to RabbitMQ queue: ORDER.CREATED.QUEUE
File is moved to:  input/processed
Failure Path : input/error/orders



Route 3: RabbitMQ → Consumer

Camel listens to ORDER.CREATED.QUEUE
Message is received
JSON is converted to Order
Order details are logged: Order ID | Customer ID | Amount
Message is acknowledged automatically

Sample Log : Order processed | OrderId=xxx | CustomerId=CUST1001 | Amount=75000


Architecture Diagram: 

REST API
↓
Order created in memory
↓
Camel Route 1 → JSON file (input/orders)
↓
Camel Route 2 → Validate → RabbitMQ
↓
Camel Route 3 → Consume → Log



*Important -  RabbitMQ docker-compose file*

version: '3.8'

services:
  rabbitmq:
    image: rabbitmq:3.12-management
    container_name: rabbitmq
    ports:
      - "5672:5672"      # RabbitMQ messaging port
      - "15672:15672"    # Management UI
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq

volumes:
  rabbitmq_data:

  
