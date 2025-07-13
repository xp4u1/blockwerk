locals {
  app_label = "paper"
}

resource "kubernetes_deployment" "paper" {
  metadata {
    namespace = var.namespace
    name      = var.name
    labels = {
      app = local.app_label
    }
  }

  spec {
    replicas = var.replicas

    selector {
      match_labels = {
        app = local.app_label
      }
    }

    template {
      metadata {
        namespace = var.namespace
        labels = {
          app               = local.app_label
          "blockwerk-group" = var.name
        }
      }

      spec {
        container {
          name              = var.name
          image             = var.image
          image_pull_policy = var.image_pull_policy

          port {
            container_port = 25565
            protocol       = "TCP"
          }

          env {
            name = "PAPER_VELOCITY_SECRET"
            value_from {
              secret_key_ref {
                name = var.secret_key_ref
                key  = "PAPER_VELOCITY_SECRET"
              }
            }
          }

          resources {
            requests = var.resources_requests
            limits   = var.resources_limits
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "paper" {
  metadata {
    namespace = var.namespace
    name      = var.name
    labels = {
      app = local.app_label
    }
  }

  spec {
    selector = {
      app = local.app_label
    }

    port {
      name        = "minecraft"
      protocol    = "TCP"
      port        = 25565
      target_port = 25565
    }
  }
}
