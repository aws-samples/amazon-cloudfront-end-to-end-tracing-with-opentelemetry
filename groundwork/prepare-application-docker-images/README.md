# Prepare Demo Application Docker Image

The Demo App is a simple web application, powered by Spring Boot, to demonstrate end-to-end tracing using OpenTelemetry.
The Demo App receives an HTTP request made to an endpoint, `GET '/'`, and serves a text response, `'Hello World'`. 

## How to use

To deploy this application with the IaC code, you should build the docker image of the application 
and host the image in your private repository.

Below, we describe the steps to build a docker image and host the image on Amazon ECR.

i) Create ECR repository
```
# Check your AWS CLI configuration before you proceed

export DEMO_APP_REPO_NAME="demo-app"
aws ecr create-repository --repository-name ${DEMO_APP_REPO_NAME} --output json | jq
```

ii) Build docker image
```
# Refer to the Makefile

make
```

iii) Tag the image
```
export AWS_REGION="YOUR_REGION"   # ex. ap-northeast-2
export ACCOUNT_ID=$(aws sts get-caller-identity --output json | jq ".Account" | tr -d '"')
export ECR_URL="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

docker tag demo-app:latest ${ECR_URL}/${DEMO_APP_REPO_NAME}:latest
```

iv) Push the image
```
aws ecr get-login-password --region ap-northeast-2 | \
docker login --username AWS --password-stdin ${ECR_URL}

docker push ${ECR_URL}/${DEMO_APP_REPO_NAME}:latest
```
