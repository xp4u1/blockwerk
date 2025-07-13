## Usage

**Dependencies:** `curl`, `jq`

Download the desired server type:

```bash
# Download Paper
$ PROJECT=paper ./download.sh

# Download Velocity
$ PROJECT=velocity ./download.sh
```

Put all your custom configuration, plugins, and any other server-specific files
into the `server/` directory. This folder will be copied into the image during
build.

```bash
# Build the Docker image
$ docker build -t your_name .
```

## Configuration

### Velocity

For Velocity to function properly:

- Modern forwarding must be enabled in `velocity.toml`
- The Blockwerk plugin JAR must be placed in the `plugins/` directory

```toml
# velocity.toml
player-info-forwarding-mode = "modern"
```

### Paper

For Paper servers to work correctly:

- You must accept the EULA (`eula.txt`)
- Set `online-mode=false` in `server.properties`
- Disable BungeeCord in `spigot.yml`
- Enable Velocity support in `config/paper-global.yml`

The Velocity secret is automatically configured by the Kubernetes cluster.

```yaml
# spigot.yml
settings:
  bungeecord: false

# config/paper-global.yml
velocity:
  enabled: true
  online-mode: true
  secret: will_be_overridden
```
