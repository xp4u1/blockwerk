resource "kubernetes_secret" "paper_velocity_secret" {
  metadata {
    name      = "paper-velocity-secret"
    namespace = var.namespace
  }

  data = {
    "PAPER_VELOCITY_SECRET" = var.paper_velocity_secret
  }

  type = "Opaque"
}
