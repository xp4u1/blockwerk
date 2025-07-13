module "paper" {
  for_each = { for group in var.paper_groups : group.name => group }

  source         = "./modules/paper"
  namespace      = var.namespace
  secret_key_ref = kubernetes_secret.paper_velocity_secret.metadata[0].name

  name     = each.value.name
  image    = each.value.image
  replicas = each.value.replicas

  resources_limits   = each.value.resources_limits
  resources_requests = each.value.resources_requests
}
