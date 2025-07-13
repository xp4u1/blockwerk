resource "kubernetes_namespace" "blockwerk_namespace" {
  metadata {
    annotations = {
      name = var.namespace
    }

    name = var.namespace
  }
}
