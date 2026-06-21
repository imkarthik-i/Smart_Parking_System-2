# Smart Parking Management System — Jenkins CI/CD Pipeline Setup

## Table of Contents
1. [Prerequisites](#1-prerequisites)
2. [Required Jenkins Plugins](#2-required-jenkins-plugins)
3. [Global Tool Configuration](#3-global-tool-configuration)
4. [Credentials Setup](#4-credentials-setup)
5. [Pipeline Creation](#5-pipeline-creation)
6. [Pipeline Parameters](#6-pipeline-parameters)
7. [Execution Steps](#7-execution-steps)
8. [Troubleshooting](#8-troubleshooting)

---

## 1. Prerequisites

| Requirement | Version | Notes |
|---|---|---|
| Jenkins | 2.440+ | LTS recommended |
| Java | JDK 21 | For backend compilation |
| Maven | 3.9.x | For Spring Boot build |
| Node.js | 22.x | For React frontend build |
| Docker | 24.0+ | For image building |
| Minikube | 1.32+ | Local Kubernetes cluster |
| kubectl | 1.28+ | Kubernetes CLI |
| Git | 2.40+ | Source control |

### Minikube Setup (if not already running)

```bash
minikube start --cpus=4 --memory=8192 --driver=docker
minikube addons enable ingress
```

Verify cluster:
```bash
kubectl get nodes
kubectl config current-context
```

---

## 2. Required Jenkins Plugins

Install these from **Manage Jenkins → Plugins → Available Plugins**:

| Plugin | ID | Purpose |
|---|---|---|
| Pipeline | `pipeline-model-definition` | Declarative Pipeline support |
| Git | `git` | Git SCM integration |
| GitHub | `github` | GitHub integration |
| Maven Integration | `maven-plugin` | Maven build steps |
| JUnit | `junit` | Test report publishing |
| Docker Pipeline | `docker-workflow` | Docker build/push from pipeline |
| Kubernetes CLI | `kubernetes-cli` | kubectl commands in pipeline |
| Credentials Binding | `credentials-binding` | Secure credential injection |
| Workspace Cleanup | `ws-cleanup` | Post-build workspace cleanup |
| Pipeline Utility Steps | `pipeline-utility-steps` | `readJSON`, `findFiles` steps |
| Blue Ocean | `blueocean` | (Optional) Visual pipeline UI |

### Install via Jenkins CLI

```bash
java -jar jenkins-cli.jar -s http://localhost:8080/ install-plugin \
  pipeline-model-definition git github maven-plugin junit \
  docker-workflow kubernetes-cli credentials-binding ws-cleanup \
  pipeline-utility-steps blueocean
```

Restart Jenkins after installation:
```bash
java -jar jenkins-cli.jar -s http://localhost:8080/ safe-restart
```

---

## 3. Global Tool Configuration

Navigate to **Manage Jenkins → Tools**.

### 3.1 JDK — JDK-21

| Field | Value |
|---|---|
| Name | `JDK-21` |
| Install automatically | ✓ |
| Install from adoptium.net | JDK 21 (latest) |

### 3.2 Maven — Maven-3.9

| Field | Value |
|---|---|
| Name | `Maven-3.9` |
| Install automatically | ✓ |
| Version | 3.9.9 |

### 3.3 NodeJS — Node-22

| Field | Value |
|---|---|
| Name | `Node-22` |
| Install automatically | ✓ |
| Version | 22.x |

### 3.4 Docker

Ensure Docker is installed on the Jenkins agent/controller:

```bash
# Linux agent
curl -fsSL https://get.docker.com | sh
sudo usermod -aG docker jenkins
newgrp docker
# Verify
docker --version
```

### 3.5 kubectl

Ensure kubectl is installed and configured for Minikube:

```bash
# Install kubectl
curl -LO "https://dl.k8s.io/release/$(curl -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x kubectl
sudo mv kubectl /usr/local/bin/

# Verify connectivity
kubectl cluster-info
kubectl config view
```

---

## 4. Credentials Setup

Navigate to **Manage Jenkins → Credentials → System → Global credentials (unrestricted) → Add Credentials**.

### 4.1 Docker Hub Credentials (Optional)

Used by Stage 6 (Push Docker Images). Skip if not pushing to Docker Hub.

| Field | Value |
|---|---|
| Kind | Username with password |
| Scope | Global |
| Username | Your Docker Hub username |
| Password | Docker Hub password or access token |
| ID | `docker-hub-credentials` |
| Description | Docker Hub credentials for image push |

> **Security Note:** Use a Docker Hub access token (not your account password). Generate one at: Docker Hub → Account Settings → Security → New Access Token.

### 4.2 Git Repository Credentials (if private repo)

| Field | Value |
|---|---|
| Kind | Username with password (or SSH Key) |
| Username | GitHub username |
| Password | GitHub personal access token |
| ID | `github-credentials` |

---

## 5. Pipeline Creation

### Option A: Pipeline from SCM (Recommended)

1. In Jenkins Dashboard → **New Item**
2. Enter name: `Smart-Parking-System-CI-CD`
3. Select **Pipeline** → **OK**
4. Scroll to **Pipeline** section:
   - **Definition**: `Pipeline script from SCM`
   - **SCM**: `Git`
   - **Repository URL**: `https://github.com/YOUR_USERNAME/Smart_Parking_System.git`
   - **Credentials**: (your git credentials, or leave blank for public repo)
   - **Branches to build**: `*/main`
   - **Script Path**: `Jenkinsfile`
5. Save

### Option B: Pipeline Script (for testing)

1. Same as above until pipeline section
2. **Definition**: `Pipeline script`
3. **Script**: Paste the Jenkinsfile content
4. Save

---

## 6. Pipeline Parameters

When triggering a build, you can configure:

| Parameter | Default | Description |
|---|---|---|
| `PUSH_TO_DOCKER_HUB` | `false` | Set to `true` to push Docker images to Docker Hub |
| `DOCKER_CREDENTIALS_ID` | `docker-hub-credentials` | Jenkins credential ID for Docker Hub auth |
| `USE_MINIKUBE` | `true` | Rebuild images inside Minikube Docker daemon so images are available locally |

---

## 7. Execution Steps

### 7.1 First Time Setup (One-Time)

```bash
# 1. Start Minikube
minikube start --cpus=4 --memory=8192 --driver=docker

# 2. Enable ingress addon
minikube addons enable ingress

# 3. Clone repository
git clone https://github.com/YOUR_USERNAME/Smart_Parking_System.git
cd Smart_Parking_System

# 4. Test Docker build locally
docker build -t smart-parking-app:latest .
cd frontend && docker build -t smart-parking-frontend:latest . && cd ..

# 5. Test kubectl access
kubectl get nodes
```

### 7.2 Trigger Pipeline

**Manual trigger:**
- Jenkins Dashboard → `Smart-Parking-System-CI-CD` → **Build with Parameters**
- Configure parameters → **Build**

**Webhook trigger (optional):**
- GitHub repo → Settings → Webhooks → Add webhook
- Payload URL: `http://JENKINS_URL:8080/github-webhook/`
- Content type: `application/json`
- Events: `Push events`

### 7.3 Monitor Pipeline

- **Classic UI**: Dashboard → Pipeline name → Stage View
- **Blue Ocean**: Dashboard → Pipeline name → Open Blue Ocean
- **Console Output**: Click build number → Console Output

### 7.4 Post-Build Verification

After successful pipeline run:

```bash
# Check pods
kubectl get pods -n parking-system

# Access application
minikube service frontend-service -n parking-system

# Get Minikube IP
minikube ip

# Access Swagger
curl http://<MINIKUBE_IP>:30080/swagger-ui/index.html

# Check health
curl http://<MINIKUBE_IP>:30080/actuator/health
```

---

## 8. Troubleshooting

### 8.1 Build Failures

| Symptom | Cause | Solution |
|---|---|---|
| `mvn: command not found` | Maven not configured | Configure Maven in Global Tool Configuration |
| `npm: command not found` | NodeJS not configured | Configure NodeJS in Global Tool Configuration |
| `docker: command not found` | Docker not on agent | Install Docker on Jenkins agent |
| `kubectl: command not found` | kubectl not installed | Install kubectl CLI on Jenkins agent |
| `Permission denied` for Docker | Jenkins not in docker group | `sudo usermod -aG docker jenkins` + restart |

### 8.2 Kubernetes Deployment Issues

| Symptom | Cause | Solution |
|---|---|---|
| `ImagePullBackOff` | Image not in registry or Minikube Docker | Use `USE_MINIKUBE=true` parameter |
| `CrashLoopBackOff` | Application startup failure | Check pod logs: `kubectl logs <pod> -n parking-system` |
| `Pending` pods | Insufficient resources | `minikube stop && minikube start --cpus=4 --memory=8192` |
| MySQL connection refused | MySQL not ready | Check init container logs: `kubectl logs <backend-pod> -c wait-for-mysql -n parking-system` |

### 8.3 Credential Issues

| Symptom | Cause | Solution |
|---|---|---|
| Docker push fails | Wrong credentials | Verify `docker-hub-credentials` ID matches |
| `credentials not found` | Credential ID mismatch | Check spelling in Jenkins parameters |

### 8.4 Minikube-Specific

```bash
# Point kubectl to Minikube
kubectl config use-context minikube

# Configure Docker to use Minikube daemon
eval $(minikube -p minikube docker-env)

# Check Minikube status
minikube status

# Reset Minikube
minikube delete && minikube start --cpus=4 --memory=8192
```

### 8.5 Debugging Pipeline

```bash
# Replay pipeline (Jenkins UI)
# Open build → Replay → Edit and run

# Check Jenkins system log
# Manage Jenkins → System Log → All Jenkins Logs

# Verify Jenkinsfile syntax
curl -X POST -u USER:API_TOKEN \
  http://JENKINS_URL:8080/pipeline-model-converter/validate \
  --data-urlencode "jenkinsfile@Jenkinsfile"
```

---

## Pipeline Stages Summary

```
┌─────────────────────────────────────────┐
│ 1. Checkout Source Code                 │
│    → Fetch from Git                     │
│    → Display branch & commit            │
├─────────────────────────────────────────┤
│ 2. Build Backend                        │
│    → mvn clean package -DskipTests      │
│    → Verify JAR in target/              │
│    → Archive artifact                   │
├─────────────────────────────────────────┤
│ 3. Run Unit Tests                       │
│    → mvn test                           │
│    → Publish JUnit reports              │
│    → Fail if tests fail                 │
├─────────────────────────────────────────┤
│ 4. Build Frontend                       │
│    → npm install                        │
│    → npm run build                      │
│    → Verify build/index.html            │
│    → Archive artifact                   │
├─────────────────────────────────────────┤
│ 5. Build Docker Images                  │
│    → Backend: smart-parking-app:latest  │
│    → Frontend: smart-parking-frontend   │
│    → Tag with build number              │
│    → Display image info                 │
├─────────────────────────────────────────┤
│ 6. Push Docker Images (Optional)        │
│    → Push both images to Docker Hub     │
│    → Uses Jenkins credentials           │
│    → Skipped if PUSH_TO_DOCKER_HUB=false│
├─────────────────────────────────────────┤
│ 7. Deploy to Kubernetes                 │
│    → kubectl apply -f k8s/              │
│    → Namespace, ConfigMaps, Secrets     │
│    → PVC, MySQL, Backend, Frontend      │
│    → Services & Ingress                 │
├─────────────────────────────────────────┤
│ 8. Verify Deployment                    │
│    → kubectl get pods/svc/deployments   │
│    → Check each pod status              │
│    → Fail if any pod unhealthy          │
├─────────────────────────────────────────┤
│ 9. Application Health Check             │
│    → Backend Actuator health            │
│    → Swagger UI accessibility           │
│    → Frontend accessibility             │
│    → Rollout status verification        │
├─────────────────────────────────────────┤
│ POST Actions                            │
│    → Clean workspace                    │
│    → Display SUCCESS/FAILURE summary    │
└─────────────────────────────────────────┘
```

---

## Verification Checklist

| Check | Expected Result |
|---|---|
| `kubectl get pods -n parking-system` | All pods `Running` |
| `kubectl get svc -n parking-system` | Services `backend-service`, `frontend-service`, `mysql-service` listed |
| `kubectl get deployments -n parking-system` | Deployments available (ready replicas > 0) |
| Backend health | `curl backend-service:8080/actuator/health` → `{"status":"UP"}` |
| Frontend | `curl frontend-service` → HTTP 200 |
| Swagger UI | `curl backend-service:8080/swagger-ui/index.html` → HTTP 200 |

---

## Architecture Diagram

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│  GitHub  │────▶│ Jenkins  │────▶│ Minikube │
│   Repo   │     │ Pipeline │     │ Cluster  │
└──────────┘     └──────────┘     └──────────┘
                      │                  │
                      ▼                  ▼
               ┌──────────┐      ┌──────────────┐
               │  Docker  │      │  parking-system│
               │ Registry │      │  Namespace    │
               │ (Optional)│     │              │
               └──────────┘      │  ┌─────────┐ │
                                 │  │ MySQL    │ │
       Pipeline Stages:          │  ├─────────┤ │
       1. Checkout ──────────────│──│ Backend  │ │
       2. Build Backend          │  ├─────────┤ │
       3. Unit Tests             │──│ Frontend │ │
       4. Build Frontend         │  └─────────┘ │
       5. Docker Build ──────────┤              │
       6. Docker Push (opt)      └──────────────┘
       7. K8s Deploy ───────────┘
       8. Verify Deployment
       9. Health Check
```
