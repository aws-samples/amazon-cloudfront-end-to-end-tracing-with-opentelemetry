## Groundwork

### Prerequisites

Install CLI tools and settings.

- [terraform](https://learn.hashicorp.com/tutorials/terraform/install-cli) ([check version release](https://github.com/hashicorp/terraform/releases))
- [kubectl](https://kubernetes.io/docs/tasks/tools/#kubectl) ([check version release](https://kubernetes.io/releases/))
- [eksctl](https://eksctl.io/introduction/#installation) ([check version release](https://github.com/weaveworks/eksctl/releases))
- [helm](https://helm.sh/docs/intro/install/) ([check version release](https://github.com/helm/helm/releases))
- [jq](https://stedolan.github.io/jq/download/)
- [awscli v2](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-version.html)
- [Setting AWS Profile](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html) ([with minimum IAM policies](https://eksctl.io/usage/minimum-iam-policies/)):
  `aws sts get-caller-identity`
- Prepare git repository: 
  `https://gitlab.aws.dev/wooyounj/iac-e2e-tracing-cloudfront-opentelemetry`

`TODO: Refine repository`

### Provisioning infrastructure

```bash
# Move to working directory
cd groundwork/provisioning-infrastructure

# Initialize Terraform providers
terraform init

# verify Terraform plan
terraform plan

# Provision infrastructure
terraform apply
```
