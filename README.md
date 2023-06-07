# SpringStarterProject

Contents:

- [Setup Cassandra DB](https://github.com/shinde-shantanu/SpringStarterProject#cassandra-db-setup)
- [Setup Solace Queue](https://github.com/shinde-shantanu/SpringStarterProject#setup-solace-for-jms-log-message-queuing)
- [Deployment on Minikube](https://github.com/shinde-shantanu/SpringStarterProject#deployment-on-minikube)
- [Accessing Swagger-UI](https://github.com/shinde-shantanu/SpringStarterProject#access-swagger-ui)




##
## Cassandra db setup:

Following are the steps I used to install cassandra db on my macbook.

### Step 1: Install Homebrew

```
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### Step 2: Install OpenJDK 11

```
brew install openjdk@11
```

### Step 3: Set up Java environment variable

Determine which shell profile file you are using. Common shell profile files are **~/.bash_profile** for Bash, **~/.zshrc** for Zsh, and **~/.bashrc** for Bash (on some systems). In my case it was **~/.zshrc**.
Run the following command to open the shell profile file using a text editor (replace **~/.zshrc** with the appropriate file if you're using a different shell):
```
open -a TextEdit ~/.zshrc
```
Add the following lines at the end of the file:
```
export JAVA_HOME="/usr/local/opt/openjdk@11"
export PATH="$JAVA_HOME/bin:$PATH"
```
These lines set the JAVA_HOME environment variable to the OpenJDK 11 installation path and add it to the PATH variable.
Save the file and close the text editor.
To apply the changes, run the following command in the Terminal:
```
source ~/.zshrc
```
Check installation using the following command:
```
cqlsh
```
This should open up the CQL shell.

### Step 4: Create the keyspace

Launch the CQL shell using:
```
cqlsh
```
Create the **customerDB** keyspace:
```
CREATE KEYSPACE customerDB WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
```




##
## Setup Solace for JMS log message queuing


Follwing steps are for setting up Solace for JMS messaging to a loggingQueue

### Step 1: Install prerequisites

You need have Docker installed. Installation can be found [here](https://docs.docker.com/engine/install/).
Install [Minikube](https://minikube.sigs.k8s.io/docs/start/).

### Step 2: Download the Docker Compose Template

Clone the GitHub repository containing the Docker Compose template.
```
git clone https://github.com/SolaceLabs/solace-single-docker-compose.git
cd solace-single-docker-compose/template
```

### Step 3 (Optional): Change the ports before composing

This step is optional and is mostly required in development as the default port for spring boot apllication is 8080. The JMS solace application also used the 8080 port for it's SEMP/PubSub+ Manager. In order to do this, open the **template/PubSubStandard_singleNode.yml** file in the cloned repository and change the line:
```
...
#SEMP / PubSub+ Manager
- '8080:8080'
...
```
to:
```
...
#SEMP / PubSub+ Manager
- '8081:8080'
...
```

### Step 4: Create a PubSub+ Software Event Broker

Run the following command to create a PubSub+ software event broker using the Compose template:
```
docker-compose -f PubSubStandard_singleNode.yml up -d
```

### Step 5: Manage the PubSub+ Software Event Broker

To start issuing configuration or monitoring commands on the event broker, you can access Broker Manager or the Solace CLI. To access PubSub+ Broker Manager:
1. Open a browser and enter http://localhost:8080.
2. Log in as user admin with password admin.

### Step 6: Setup a new Message VPN:

In ``` Messaging -> Message VPNs ``` select ``` + Message VPN ``` and fill out the following:


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/700671c9-30e5-4bb7-9eaf-ad64133ab6ae)


The select ``` Set up Default User -> ``` and set **admin** as username and password then press ``` Create ```.

### Step 7: Setup the queue:

Open the **CustomerApplicationMessages**. Got to ``` System -> Uset Mgmt -> Users ``` and add a user with **admin** as password and user name with all accesses granted if not already present.

Go to ``` Messaging -> Queues ``` and Select ``` + Queue ```. Give it a name **loggingQueue** and add **admin** as owner.




##
## Deployment on minikube:

Follwing steps can be used to deploy a Spring Boot application on Minikube capable of querying a locally hosted cassandra db and a Solace queue.

### Step 1: Install prerequisites

You need have Docker installed. Installation can be found [here](https://docs.docker.com/engine/install/).
Install [Minikube](https://minikube.sigs.k8s.io/docs/start/).

### Step 2: Start the Minikube Cluster

Start your cluster using:
```
minikube start --driver=docker
```
Here's how the output looks:


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/8ae0df7b-9ec6-44a4-b028-86b28580c4f4)


Check the status using:
```
minikube status
```
It should display something close to the following:


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/c579800f-aac8-47c3-bb8d-2a41c230ba99)


### Step 3: Configure demoCustomer and generate jar file

Run the following command to retreive the ip address of the Docker host machine:
```
minikube ssh -- cat /etc/hosts | grep host.minikube.internal | awk '{print $1}'
```

Use the ip address return in the above command to configure the Spring Boot Application. In the src/main/resources/application.properties file apdate the following properties:
```
# Cassandra Connection Configuration
cassandra.contactPoints=<enter docker host ip>
...

#Solace Connection Configuration
solace.host=<enter docker host ip>
...

```

Run ``` mvn clean install ``` in the spring project to generate the jar files.
In case ``` mvn clean install ``` does not work, you need to skip tests using the command:
```
mvn clean install -DskipTests
```

### Step 4: Use Docker daemon inside Minikube

Run the following command to list the Docker images present in your local Docker environment:
```
docker images
```
In my case I get the following output:


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/e052ae31-51cb-4ba5-8e81-eeff34e35192)


Minikube comes with a docker daemon preinstalled on it. So the Minikube cluster is running on a Docker virtual machine and a Docker version is installed on this minikube cluster. In order to deploy the Spring boot application docker we need to perform the following steps:
```
minikube docker-env
```
This will give an output similar to the following:


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/f1689396-2c3b-4d6d-a7fd-7e4a190a51a8)


Run the command in the last line of this output to activate the docker commands on the minikube cluster. For eg; in this case:
```
eval $(minikube -p minikube docker-env)
```

Now if run ``` docker images``` again, we will see the docker images present in the minikube environment. For eg;


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/09f62913-0b8a-4ef2-980a-0d69a96bfcc6)


A change in output here represents that the previous commands worked.


### Step 5: Deploy Spring boot image pn minikube

``` cd ``` into the demoCustomer folder which has the Dockerfile and run the following command to build the docker image on minikube:
```
docker build -t springstarterproject:1.0 .
```

Here's how the output looks:


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/54074f70-3c92-4633-9fe1-146aab7df815)


Now if we run ``` docker images ``` we see that springstarterproject is added with tag **1.0**:


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/b42674ac-208f-4827-8e86-7a8e75b602dd)


Next we need to create a deployment on kubernetes using the ``` kubectl ``` commands.
```
kubectl create deployment springstarterproject-dep --image=springstarterproject:1.0 --port=8080
```
Now if we run:
```
kubectl get all
```
we see that a pod and a deployment is created with springstarterproject in their names:


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/33663609-2985-4c3e-b569-261a0781d491)


Inorder to check if the pod is running properly, run:
```
kubectl logs <pod_name>
```
In my case it was:
```
kubectl logs springstarterproject-dep-6f897d5f46-qlf6c
```
The output should look like the following with **Started DemoCustomerApplication** displayed:


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/ed898df4-b3e1-465e-a4bb-5c4de3c2e6ce)


Now we need to create a service for the deployment using:
```
kubectl expose deployment springstarterproject-dep --port=8080 --type=NodePort --name=springstarterproject-service
```
Now if we run ``` kubectl get all ``` we see that **springstarterproject-service** is added in services:


![image](https://github.com/shinde-shantanu/SpringStarterProject/assets/48611375/ecb90430-5578-40ea-b82b-f8e7f1addf46)


Now that we have created a service, we can retrive the url for the service using:
```
minikube service springstarterproject-service --url
```
The displayed url is the host and port that we can query using postman or a web browser to run our microservices.




##
## Access Swagger UI

Inorder to access Swagger-UI after deploying on Minikube use the url returned by the baove command followed by **swagger-ui.html**.
Use the follwowing format:
```
http://localhost:<port>/swagger-ui.html
```
