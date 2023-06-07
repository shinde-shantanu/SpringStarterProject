# SpringStarterProject

## Deployment on minikube:

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
