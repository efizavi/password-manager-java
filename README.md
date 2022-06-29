# password-manager-java
My goal was to create a website that manages users’ passwords, and other secret information
The inspiration for this project was sites like BitWarden, LastPass, 1password, etc.
The algorithmic part of this project focuses on encryption and decryption.
The algorithm offers Asymmetric and Symmetric encryption capabilities (with implementation of RSA and AES respectively)
The Asymmetric algorithm is used to encrypt user logins
The Symmetric algorithm is using the logged in user’s password as a secret key to encrypt and decrypt the user’s stored secrets after login
This allows the user to only remember a single password and have many different passwords stored securely until he logs in.
The module is built on the Strategy design pattern, allowing for scalability. The “encrypt” and “decrypt” functions are exposed by all implementations of the “IAlgoEncryption” interface.
**PasswordGeneratorProject** was created which depends on the JAR module.
A scalable DAO was created to save users, and user secrets, after encryption. A JSON implementation was also created. (output: resources/datasource.txt)
A DM was created with classes for logins and secrets.
User secrets are split into data transfer objects inheriting AbstractSecretDTO which hold the secret data as plaintext and AbstractSecretEncrypted which hold the same data as encrypted byte arrays
Since abstract classes are used, to assist in JSON deserialization into the correct inherited classes, an “AbstractSerializable” base class was created which holds the base class name. JSON then uses this information (via JSONAbstractAdapter) to infer the correct inherited class.
All secrets hold some base non-encrypted information (for example, secret name) which is inside the base class
Two services were created, one for the user logins and one for the user secrets
Both services use the same base class which forces them to be constructed with a DAO implementation
The login service is constructed with an asymmetric encryption algorithm implementation
The secret service receives a user password with each API call – since we support concurrent users with different passwords. The password is used to create a user-specific symmetric algorithm to encrypt and decrypt only that user’s secrets.
A server was made to handle client requests asynchronously
The server receives a “Request” object and responds with a “Response” object, both are serialized/deserialized using JSON
The request objects holds key-value information
The response object either returns a Boolean or a list of secrets
Requests are sent to a controller, using a Factory DP, which builds an appropriate response according to the request type
The controllers use the services from part 2 to address the requests
The controllers initialize the DAO for the services
Handling the request is done in a thread-safe manner to avoid concurrency issues when accessing DAO
For the login service, the RSA encryption algorithm is built using private and public keys (loaded from resources/privatekey.der, resources/publickey.der – for demonstration purposes…)
The keys were built using OpenSSL for this purpose
**PasswordGeneratorClient**:
The Client is implemented with MVC architecture
The view was built using Swing
The controller links the UI actions to the relevant model 
There are two models, one for logins and one for secrets
The models create a connection to the server, build and send a request and wait for a response, then close the connection.
UUIDs are used to make sure the response was received by the same client that made the request
The controller forwards the response from the model to the view, to be displayed to the user.
A video demonstration of the UI is included alongside this presentation the project files 
