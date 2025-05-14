# Deployment

```bash

docker run -d --name cloud-gateway -p 18000:8000 zhengke0110-cloud/cloud-gateway:latest

docker run -d --name cloud-account-1 -p 18001:8001 zhengke0110-cloud/cloud-account:latest

docker run -d --name cloud-data-1 -p 18002:8002 zhengke0110-cloud/cloud-data:latest

docker run -d --name cloud-link-1 -p 18003:8003 zhengke0110-cloud/cloud-link-service:latest

docker run -d --name cloud-shop-1 -p 18004:8004 zhengke0110-cloud/cloud-shop:latest
```