# Install Sample Application

Using Helm charts, deploy sample microservices to the EKS cluster.


## Install Workload application (Helm chart)

### Set kubectl context

### Set environment variables
```bash
export ORDER_SERVICE_REPO_NAME="e2e-sample-order"
export DELIVERY_SERVICE_REPO_NAME="e2e-sample-delivery"

# check your AWS CLI configuration before you proceed

export AWS_REGION="YOUR_REGION"   # ex. ap-northeast-2
export ACCOUNT_ID=$(aws sts get-caller-identity --output json | jq ".Account" | tr -d '"')
export ECR_URL="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
```

### Prepare `values.yaml` file
```bash
# Move to 'install-sample-microservices' directory from root of repository
cd groundwork/install-sample-microservices

# Create 'values.yaml' file from each template
cat e2e-sample-delivery-chart/values.template | envsubst > e2e-sample-delivery-chart/values.yaml
cat e2e-sample-order-chart/values.template | envsubst > e2e-sample-order-chart/values.yaml

# Check image tag
yq e '.image' -I2 e2e-sample-delivery-chart/values.yaml
yq e '.image' -I2 e2e-sample-order-chart/values.yaml
```

### Install workload chart

```bash
# In the 'install-sample-microservices' directory
export DELIVERY_SERVICE_CHART_NAME="e2e-sample-delivery-chart"
export ORDER_SERVICE_CHART_NAME="e2e-sample-order-chart"

# Install 'e2e-sample-delivery-chart' in the default namespace
helm package ${DELIVERY_SERVICE_CHART_NAME}
helm upgrade --install ${DELIVERY_SERVICE_CHART_NAME} "${DELIVERY_SERVICE_CHART_NAME}-0.1.0.tgz"

# Install 'e2e-sample-order-chart' in the default namespace
helm package ${ORDER_SERVICE_CHART_NAME}
helm upgrade --install ${ORDER_SERVICE_CHART_NAME} "${ORDER_SERVICE_CHART_NAME}-0.1.0.tgz"

# Check
helm list | egrep "NAME |${DELIVERY_SERVICE_CHART_NAME}"
helm list | egrep "NAME |${ORDER_SERVICE_CHART_NAME}"

# Get Pods
kubectl get pods -L "load-type=on-cpu"

# Describe Deployment: check 'Pod Template' and 'Events'
kubectl describe deployment ${DELIVERY_SERVICE_CHART_NAME}
kubectl describe deployment ${ORDER_SERVICE_CHART_NAME}

# Copy the Ingress Address to your clipboard
kubectl get ingress ${DELIVERY_SERVICE_CHART_NAME} -o jsonpath='{.status.loadBalancer.ingress[0].hostname}' | pbcopy
```

### Check the API responses

After ALB has been provisioned and passed health check, you can call microservice APIs.
Refer to the [Postman export file](../prepare-application-docker-images/docs/blog-e2e.postman_collection.json) for API specification.


## How to Uninstall

```bash
# Uninstall the charts
helm delete ${ORDER_SERVICE_CHART_NAME}
helm delete ${DELIVERY_SERVICE_CHART_NAME}

# Delete ECR repositories
aws ecr delete-repository --repository-name "${ORDER_SERVICE_REPO_NAME}"
aws ecr delete-repository --repository-name "${DELIVERY_SERVICE_REPO_NAME}"
```
