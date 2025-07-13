variable "namespace" {
  description = "Namespace that will be created for blockwerk"
  type        = string
  default     = "blockwerk"
}

variable "paper_velocity_secret" {
  description = "Shared forwarding secret (Velocity <-> Paper)"
  type        = string
  sensitive   = true
}
