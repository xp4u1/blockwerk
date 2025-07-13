locals {
  name = "velocity"
}

resource "kubernetes_deployment" "velocity" {
  metadata {
    namespace = var.namespace
    name      = local.name
    labels = {
      app = local.name
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = local.name
      }
    }

    template {
      metadata {
        namespace = var.namespace
        labels = {
          app = local.name
        }
      }

      spec {
        service_account_name = kubernetes_service_account.velocity_reader.metadata[0].name

        container {
          name              = local.name
          image             = var.velocity_image
          image_pull_policy = "IfNotPresent"

          port {
            container_port = 25565
          }

          env {
            name = "VELOCITY_FORWARDING_SECRET"

            value_from {
              secret_key_ref {
                name = kubernetes_secret.paper_velocity_secret.metadata[0].name
                key  = "PAPER_VELOCITY_SECRET"
              }
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "velocity" {
  metadata {
    namespace = var.namespace
    name      = local.name
    labels = {
      app = local.name
    }
  }

  spec {
    selector = {
      app = local.name
    }

    type = "NodePort"

    port {
      name        = "minecraft"
      protocol    = "TCP"
      port        = 25565
      target_port = 25565
      node_port   = 30001
    }
  }
}

resource "kubernetes_service_account" "velocity_reader" {
  metadata {
    namespace = var.namespace
    name      = "velocity-reader"
  }
}

resource "kubernetes_role" "pod_reader" {
  metadata {
    namespace = var.namespace
    name      = "pod-reader"
  }

  rule {
    api_groups = [""]
    resources  = ["pods"]
    verbs      = ["get", "list", "watch"]
  }
}

resource "kubernetes_role_binding" "velocity_reader_binding" {
  metadata {
    namespace = var.namespace
    name      = "velocity-reader-binding"
  }

  subject {
    kind      = "ServiceAccount"
    name      = kubernetes_service_account.velocity_reader.metadata[0].name
    namespace = var.namespace
  }

  role_ref {
    kind      = "Role"
    name      = kubernetes_role.pod_reader.metadata[0].name
    api_group = "rbac.authorization.k8s.io"
  }
}
