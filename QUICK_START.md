# Quick Start Guide

Cloud-Link 短链接系统快速开始指南，帮助您在5分钟内启动完整的短链接服务。

## 🚀 一键启动

### 前置条件

- Docker 20.0+
- Docker Compose 1.29+
- 8GB+ 可用内存
- 20GB+ 可用磁盘空间

### 快速启动

```bash
# 1. 克隆项目
git clone https://github.com/Zhengke0110/cloud-link.git
cd cloud-link

# 2. 一键启动所有服务
./quick-start.sh

# 3. 等待服务启动完成（约3-5分钟）
# 访问 http://localhost 开始使用
```

## 📋 服务列表

启动完成后，您可以访问以下服务：

| 服务 | 地址 | 用户名/密码 | 说明 |
|------|------|-------------|------|
| 前端应用 | http://localhost | - | 短链接管理界面 |
| API网关 | http://localhost:8000 | - | API入口 |
| Nacos | http://localhost:8848/nacos | nacos/nacos | 服务注册中心 |
| RabbitMQ | http://localhost:15672 | admin/admin | 消息队列管理 |
| XXL-Job | http://localhost:8080/xxl-job-admin | admin/123456 | 任务调度中心 |

## 🔧 快速测试

### 1. 用户注册

```bash
curl -X POST http://localhost:8000/api/account/v1/register \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "pwd": "123456",
    "captcha": "1234"
  }'
```

### 2. 用户登录

```bash
curl -X POST http://localhost:8000/api/account/v1/login \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "pwd": "123456"
  }'
```

### 3. 创建短链

```bash
# 使用登录返回的token
curl -X POST http://localhost:8000/api/link/v1/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "originalUrl": "https://www.example.com",
    "title": "测试短链",
    "domain": "timu.fun"
  }'
```

### 4. 访问短链

```bash
# 使用返回的短链进行访问
curl -L http://localhost:8000/ABC123
```

## 📊 监控检查

### 服务状态检查

```bash
# 检查所有容器状态
docker-compose ps

# 检查服务注册情况
curl http://localhost:8848/nacos/v1/ns/instance/list?serviceName=cloud-gateway

# 检查数据库连接
docker exec -it cloud-link-mysql mysql -u root -p123456 -e "SHOW DATABASES;"
```

### 性能监控

```bash
# 查看容器资源使用
docker stats

# 查看日志
docker-compose logs -f cloud-gateway
```

## 🛠 开发模式

如果您想进行开发调试，可以按以下步骤启动：

### 1. 启动基础服务

```bash
# 只启动中间件服务
docker-compose up -d mysql redis rabbitmq nacos clickhouse kafka xxl-job-admin
```

### 2. 修改配置

更新各服务的 `application.yaml` 配置文件：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
  datasource:
    url: jdbc:mysql://localhost:3306/cloud_link
  redis:
    host: localhost
  rabbitmq:
    host: localhost
```

### 3. IDE启动服务

在IDE中依次启动：
1. `GatewayApplication`
2. `AccountApplication`
3. `LinkApplication`
4. `ShopApplication`
5. `DataApplication`

### 4. 前端开发

```bash
cd cloud-fronted
pnpm install
pnpm dev
# 访问 http://localhost:5173
```

## 🔍 故障排除

### 常见问题

1. **端口冲突**
   ```bash
   # 查看端口占用
   lsof -i :8000
   # 或者修改docker-compose.yml中的端口映射
   ```

2. **内存不足**
   ```bash
   # 释放内存
   docker system prune -a
   # 或者增加虚拟内存
   ```

3. **服务启动失败**
   ```bash
   # 查看具体错误
   docker-compose logs service-name
   ```

### 重置环境

```bash
# 停止所有服务
docker-compose down

# 清理数据（谨慎操作）
docker-compose down -v
docker system prune -a

# 重新启动
./quick-start.sh
```

## 📚 下一步

- 阅读 [完整文档](README.md) 了解系统架构
- 查看 [API文档](docs/api.md) 了解接口详情
- 参考 [部署指南](Deployment.md) 进行生产部署
- 访问 [开发指南](docs/development.md) 开始二次开发

## 💬 获取帮助

- 🐛 [提交Bug](https://github.com/username/cloud-link/issues)
- 💡 [功能建议](https://github.com/username/cloud-link/discussions)
- 📧 [联系作者](mailto:author@example.com)

---

🎉 恭喜！您已成功启动Cloud-Link短链接系统！
