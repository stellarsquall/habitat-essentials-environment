# Habitat package: jenkins

## Description

This Habitat Package deploys a Jenkins instance, extending `core/jenkins`.

### Differences from core/jenkins

- Exposes plugins as configurable value, installed serially at startup. Found under `cfg.jenkins.plugins`
- Exposes authentication as configurable values at `cfg.jenkins.security`
- Exposes credentials as configurable values at `cfg.jenkins.credentials[]`
- Updates plugins on startup
- Sets Default Crumb Issuer
- Disables Usage Statistics by Default
- Disables slave-to-master kill switch
- Disalbes Startup Wizard

## Usage

```
hab svc load <origin>/jenkins
```