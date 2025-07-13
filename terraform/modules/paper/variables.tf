variable "namespace" {
  description = "Namespace of the Blockwerk namespace"
  type        = string
  default     = "blockwerk"
}

variable "secret_key_ref" {
  description = "Name of the Kubernetes secret for the Velocity-Paper forwarding"
  type        = string
}

variable "name" {
  description = "Name of the Kubernetes deployment"
  type        = string
}

variable "image" {
  description = "Preconfigured Docker image"
  type        = string
}

variable "image_pull_policy" {
  description = "Image pull policy"
  type        = string
  default     = "IfNotPresent"
}

variable "replicas" {
  description = "Replicas of this resource"
  type        = number
  default     = 1
}

variable "resources_requests" {
  description = "Container resource requests"
  type = object({
    memory = string
    cpu    = string
  })
  default = {
    memory = "2Gi"
    cpu    = "1"
  }
}

variable "resources_limits" {
  description = "Container resource limits"
  type = object({
    memory = string
    cpu    = string
  })
  default = {
    memory = "4Gi"
    cpu    = "2"
  }
}
