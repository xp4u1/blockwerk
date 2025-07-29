# Blockwerk

**Blockwerk** is a Minecraft server network built for flexibility and
automation. Instead of relying on static, manually managed servers, it treats
server instances as ephemeral resources that come and go as needed. Whether for
minigames, testing, or scaling active player hubs, Blockwerk makes it easy to
manage a dynamic Minecraft ecosystem.

Server instances are provisioned dynamically as Paper servers, registered at
runtime with a Velocity proxy, and orchestrated entirely within Kubernetes.
Deployments are managed through Terraform and backed by custom Docker images,
allowing consistent, reproducible setups across clusters. Blockwerk is built for
modern infrastructure and integrates cleanly into self-hosted or cloud-native
environments.

## Why not a regular cloud system?

Most existing Minecraft cloud systems like CloudNet are Java-based and come with
their own partial implementations of server management. Blockwerk takes a
different approach by building directly on Kubernetes, a mature, battle-tested
platform designed for orchestrating containerized workloads. Instead of
reinventing clustering from scratch, it leverages the full ecosystem of
Kubernetes: autoscaling, observability, rollout strategies, and more. With
native integration into modern tooling, Blockwerk aligns Minecraft server
hosting with industry best practices.

## Example deployment

Deploying an entire Minecraft network takes just a few lines of Terraform when
using Blockwerk.

```terraform
module "blockwerk" {
  source = "github.com/xp4u1/blockwerk//terraform"

  paper_velocity_secret = "bd95bf84aa9ab57a9fecce72fad8984cf89383e94f25458616b1c8b80ca1aba7"
  velocity_image        = "velocity:latest"
  paper_groups = [
    {
      name     = "lobby"
      image    = "lobby:latest"
      replicas = 1
    },
    {
      name     = "bedwars"
      image    = "bedwars:latest"
      replicas = 5
    }
  ]
}
```

## Installation

To get a real-world setup running, follow these steps:

1. **Configure a local Kubernetes cluster:** Use the configs in
   [`./cluster`](./cluster) to spin up a k3d-based Kubernetes cluster tailored
   for Blockwerk.

2. **Build custom Docker images:** Create your Velocity (proxy) and Paper
   (server) images using the Dockerfiles in [`./docker`](./docker).  
   Make sure to include the
   [Blockwerk plugin](https://github.com/xp4u1/blockwerk/releases) in your
   Velocity image since it handles server registration and lobby coordination.

3. **Deploy using Terraform:** Use the example above or explore the full
   configuration options in [`./terraform`](./terraform/) to deploy all
   resources into your cluster.
