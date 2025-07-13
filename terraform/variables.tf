variable "namespace" {
  description = "Namespace that will be created for Blockwerk"
  type        = string
  default     = "blockwerk"
}

variable "paper_velocity_secret" {
  description = "Shared forwarding secret (Velocity <-> Paper)"
  type        = string
  sensitive   = true
}

variable "velocity_image" {
  description = "Custom Docker image for the Velocity proxy"
  type        = string
}

variable "paper_groups" {
  description = "List of Paper server groups to create"
  type = list(object({
    name     = string
    image    = string
    replicas = number

    resources_requests = optional(object({
      memory = string
      cpu    = string
    }))
    resources_limits = optional(object({
      memory = string
      cpu    = string
    }))
  }))
}
