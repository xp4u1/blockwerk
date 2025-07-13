## Quickstart

```bash
# Create cluster
k3d cluster create --config k3d-cluster.yaml

# Import your custom images
k3d image import velocity --cluster blockwerk
k3d image import lobby --cluster blockwerk
...
```

## Optional Dashboard

```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
kubectl apply -f dashboard-user.yaml
kubectl -n kubernetes-dashboard create token dashboard-user
kubectl port-forward -n kubernetes-dashboard service/kubernetes-dashboard 8443:443
```
