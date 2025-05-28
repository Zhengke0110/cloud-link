# Cloud-Link 短链接系统

Cloud-Link是一个分布式短链接系统，采用微服务架构设计，支持高并发、大数据量的短链接生成和管理。系统基于Spring Cloud微服务技术栈构建，集成了消息队列、实时数据处理、分布式缓存等技术，提供完整的短链接解决方案。

## 系统架构

### 整体架构图

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                      用户端                                      │
├─────────────────────────────────────────────────────────────────────────────────┤
│                           Web应用(cloud-app)                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                         API网关(cloud-gateway)                                  │
│                        ▶ 路由转发 ▶ 负载均衡 ▶ 限流熔断                           │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                  微服务层                                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │
│  │cloud-account│  │cloud-link   │  │cloud-shop   │  │cloud-data   │          │
│  │ 账户服务    │  │ 短链服务    │  │ 商品服务    │  │ 数据服务    │          │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘          │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                  中间件层                                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │
│  │    Nacos    │  │  RabbitMQ   │  │    Redis    │  │    Kafka    │          │
│  │ 服务发现    │  │  消息队列   │  │  分布式缓存 │  │  消息流处理 │          │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘          │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                   数据层                                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │
│  │    MySQL    │  │ ClickHouse  │  │   XXL-Job   │  │    Flink    │          │
│  │ 业务数据库  │  │  分析数据库 │  │  任务调度   │  │  流式计算   │          │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘          │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 核心技术栈

#### 基础框架
- **Spring Boot 2.x**：微服务基础框架
- **Spring Cloud 2021**：微服务组件套件
- **Spring Cloud Gateway**：API网关
- **Spring Cloud OpenFeign**：服务间调用
- **MyBatis-Plus**：ORM框架

#### 服务治理
- **Nacos**：服务注册发现和配置管理
- **Sentinel**：流量控制和熔断降级
- **Ribbon**：客户端负载均衡

#### 数据存储
- **MySQL 8.0**：主数据库
- **ShardingSphere**：分库分表中间件
- **Redis 6.x**：分布式缓存
- **ClickHouse**：实时数据分析

#### 消息队列
- **RabbitMQ**：异步消息处理
- **Apache Kafka**：大数据流处理

#### 实时计算
- **Apache Flink**：流式数据处理
- **Kafka Connect**：数据连接器

#### 任务调度
- **XXL-Job**：分布式任务调度

#### 监控运维
- **Docker**：容器化部署
- **Nginx**：负载均衡和静态资源
- **Prometheus + Grafana**：监控告警

### 数据流架构

```
短链访问 → Kafka → Flink → ClickHouse → 数据分析
    ↓
 访问日志 → ELK → 日志分析
    ↓
 业务数据 → MySQL → 数据备份
    ↓
 缓存数据 → Redis → 性能优化
```

#### 实时数据处理管道

1. **数据采集层**
   - 短链访问事件通过Kafka Topic收集
   - 用户行为数据实时采集
   - 设备和地理位置信息补全

2. **数据处理层**
   - Flink流处理应用（DwmShortLinWideApp）进行数据宽表补齐
   - 设备信息映射和地理位置解析
   - 独立访客统计（DwmUniqueVisitorApp）

3. **数据存储层**
   - 实时数据写入ClickHouse进行OLAP分析
   - 统计结果存储到MySQL
   - 热点数据缓存到Redis

4. **数据应用层**
   - 实时访问统计展示
   - 多维度数据分析报表
   - 业务监控告警

## 模块介绍

### cloud-account 模块

账户服务模块，负责系统中用户账户管理和流量包管理相关的功能。

#### 核心功能

1. **账户管理**
   - 用户注册：支持手机号注册，使用验证码校验
   - 用户登录：支持手机号和密码登录，登录成功返回JWT令牌
   - 账户详情：获取当前登录用户的账户信息
   - 文件上传：用户头像上传至阿里云OSS存储

2. **流量包管理**
   - 新用户免费流量包：用户注册成功后，自动发放初始流量包
   - 流量包查询：分页查询用户可用流量包
   - 流量包使用：支持短链接生成时消耗流量
   - 流量包恢复：支持通过RabbitMQ消息队列实现延迟恢复流量
   - 流量包过期处理：定时任务清理过期流量包

#### 技术亮点

1. **数据分片**：使用ShardingSphere实现流量表的水平分片，提高系统性能
2. **异步消息**：使用RabbitMQ实现流量包发放和恢复的异步处理
3. **分布式任务调度**：使用XXL-Job实现流量包过期清理等定时任务
4. **安全认证**：基于JWT的用户认证机制
5. **分布式服务**：基于Nacos的服务注册与发现
6. **缓存应用**：使用Redis实现验证码存储和业务数据缓存

#### 接口说明

1. **账户接口**
   - 注册: `/api/account/v1/register`
   - 登录: `/api/account/v1/login`
   - 账户详情: `/api/account/v1/detail`
   - 头像上传: `/api/account/v1/upload`

2. **流量包接口**
   - 流量包分页查询: `/api/traffic/v1/page`
   - 流量包详情: `/api/traffic/v1/detail/{trafficId}`
   - 流量包扣减: `/api/traffic/v1/reduce`

3. **通知接口**
   - 图形验证码: `/api/notify/v1/captcha`
   - 发送短信验证码: `/api/notify/v1/send_code`

#### 数据模型

1. **账户表(account)**：存储用户基本信息、认证信息和密码等
2. **流量表(traffic)**：存储用户流量包信息，包括日限制、已使用量和过期时间等

### cloud-link-service 模块

短链接服务模块，负责系统中短链接的生成、管理和相关域名处理功能。

#### 核心功能

1. **短链接管理**
   - 短链接生成：根据长URL生成短链接
   - 短链接查询：支持各种条件查询短链接
   - 短链接更新：修改短链接相关属性
   - 短链接删除：支持批量删除短链接

2. **域名管理**
   - 官方域名：系统提供的默认域名
   - 自定义域名：用户可以绑定自己的域名
   - 域名列表查询：获取当前用户可用的所有域名列表

3. **分组管理**
   - 创建分组：用户可以创建自定义分组
   - 分组查询：获取当前用户的分组信息
   - 分组内链接管理：对分组内的短链接进行统一管理

4. **短链接访问统计**
   - 访问量统计：记录短链接的访问次数
   - 访问来源分析：统计访问的设备、地区等信息
   - 访问趋势分析：按时间维度统计访问量变化

#### 技术亮点

1. **分库分表**：使用ShardingSphere实现短链接表的分库分表，支持海量数据
2. **高性能路由**：基于Redis的短链接到长链接的快速路由
3. **防刷机制**：通过Redis Lua脚本实现分布式锁和限流
4. **数据一致性**：基于分布式事务保证数据一致性
5. **域名解析**：支持自定义域名的DNS解析和验证

#### 接口说明

1. **短链接接口**
   - 短链接生成: `/api/link/v1/create`
   - 短链接查询: `/api/link/v1/page`
   - 短链接更新: `/api/link/v1/update`
   - 短链接删除: `/api/link/v1/delete`

2. **域名接口**
   - 域名列表: `/api/domain/v1/list`
   - 域名添加: `/api/domain/v1/add`
   - 域名验证: `/api/domain/v1/verify`

3. **分组接口**
   - 创建分组: `/api/group/v1/create`
   - 分组列表: `/api/group/v1/list`
   - 修改分组: `/api/group/v1/update`

#### 数据模型

1. **短链接表(short_link)**：存储短链接与长链接的映射关系
2. **域名表(domain)**：存储系统官方域名和用户自定义域名信息
3. **链接分组表(link_group)**：存储用户创建的短链接分组
4. **分组码映射表(group_code_mapping)**：存储分组与短链接的关联关系

### cloud-shop 模块

商品服务模块，负责系统中流量包商品的管理、订单处理以及支付相关功能。

#### 核心功能

1. **商品管理**
   - 流量包商品创建：创建不同规格的流量包商品
   - 商品列表查询：分页查询商品列表
   - 商品详情查询：获取商品详细信息
   - 商品上下架管理：管理商品的销售状态

2. **订单管理**
   - 订单创建：用户购买流量包商品创建订单
   - 订单支付：接入第三方支付系统完成支付
   - 订单状态更新：根据支付结果更新订单状态
   - 订单查询：支持多维度查询订单信息

3. **支付处理**
   - 支付通知处理：接收并处理支付结果通知
   - 支付状态查询：查询订单的支付状态
   - 退款处理：支持订单退款功能

#### 技术亮点

1. **分布式事务**：确保订单创建、支付和流量包发放的数据一致性
2. **异步通信**：通过RabbitMQ实现订单状态变更的异步通知
3. **幂等设计**：支付回调的幂等处理，避免重复处理
4. **缓存应用**：商品信息缓存，提高查询性能
5. **服务治理**：接入Nacos实现服务注册发现和配置管理

#### 接口说明

1. **商品接口**
   - 商品列表: `/api/product/v1/list`
   - 商品详情: `/api/product/v1/detail/{productId}`
   - 商品创建: `/api/product/v1/create`
   - 商品更新: `/api/product/v1/update`

2. **订单接口**
   - 创建订单: `/api/order/v1/create`
   - 订单详情: `/api/order/v1/detail/{orderNo}`
   - 订单列表: `/api/order/v1/list`
   - 取消订单: `/api/order/v1/cancel`

3. **支付接口**
   - 支付下单: `/api/pay/v1/create`
   - 支付通知: `/api/pay/v1/callback`
   - 支付查询: `/api/pay/v1/query/{orderNo}`

#### 数据模型

1. **商品表(product)**：存储流量包商品信息，包括价格、流量额度、有效期等
2. **订单表(product_order)**：存储用户购买商品的订单信息，包括订单状态、支付状态等

### cloud-gateway 模块

网关服务模块，作为整个系统的流量入口，负责请求的路由转发、身份认证、限流熔断等功能。

#### 核心功能

1. **请求路由**
   - 服务路由：根据请求路径将请求转发到对应的微服务
   - 负载均衡：集成Ribbon实现服务实例间的负载均衡
   - 路径重写：支持URL路径前缀去除等路径重写功能

2. **安全控制**
   - 身份认证：验证请求的JWT令牌，确保用户身份
   - 权限控制：基于角色的访问控制
   - 黑白名单：IP黑白名单控制

3. **流量控制**
   - 限流：基于Redis的分布式限流
   - 熔断：服务不可用时进行熔断保护
   - 降级：高负载情况下服务降级处理

4. **请求增强**
   - 请求日志：记录请求的详细信息
   - 请求头修改：添加或修改请求头
   - 跨域处理：处理跨域请求问题

#### 技术亮点

1. **基于Spring Cloud Gateway**：使用响应式编程模型，非阻塞IO
2. **动态路由**：支持从Nacos动态加载路由配置
3. **集成Sentinel**：实现流控、熔断和系统保护
4. **全局异常处理**：统一处理网关层面的异常
5. **高可用设计**：支持网关服务的集群部署

#### 配置说明

1. **路由配置**
   - cloud-link-api-service：短链接服务路由配置
   - cloud-account-service：账户服务路由配置
   - cloud-data-service：数据统计服务路由配置
   - cloud-shop-service：商品服务路由配置

2. **过滤器配置**
   - StripPrefix：路径前缀去除
   - RequestRateLimiter：请求速率限制
   - JwtAuthenticationFilter：JWT认证过滤器

### cloud-data 模块

数据统计服务模块，负责短链接访问数据的收集、统计和分析。

#### 核心功能

1. **数据采集**
   - 短链接点击事件采集
   - 用户行为数据采集
   - 设备信息采集
   - 位置信息采集

2. **数据统计**
   - 访问量统计：PV、UV等核心指标统计
   - 地域分布：访问的地理位置分布
   - 设备分析：访问设备类型、系统分布
   - 时间趋势：按时、天、周、月统计访问趋势

3. **实时监控**
   - 实时访问监控：监控当前系统访问情况
   - 异常监控：监控异常访问行为
   - 性能监控：监控系统性能指标

#### 技术亮点

1. **实时计算**：使用消息队列和流处理实现数据实时处理
2. **分布式处理**：支持大规模数据的分布式处理
3. **数据压缩**：访问日志数据压缩存储
4. **数据聚合**：多维度数据聚合统计
5. **定时任务**：使用XXL-Job实现统计任务的定时调度

#### 接口说明

1. **数据查询接口**
   - 访问量趋势: `/api/stats/v1/trend`
   - 地域分布: `/api/stats/v1/region`
   - 设备分析: `/api/stats/v1/device`
   - 自定义报表: `/api/stats/v1/custom`

2. **监控接口**
   - 实时监控: `/api/monitor/v1/realtime`
   - 系统指标: `/api/monitor/v1/metrics`

#### 数据模型

1. **访问统计表(visit_stats)**：存储短链接访问的统计数据
2. **访问明细表(visit_detail)**：存储短链接访问的详细记录

### cloud-common 模块

公共服务模块，提供整个系统中共享的工具类、配置、常量、模型等基础组件。

#### 核心功能

1. **通用工具类**
   - JsonUtil：JSON序列化和反序列化工具
   - CommonUtil：通用工具方法，如随机字符串生成、MD5加密等
   - TimeUtil：日期时间处理工具
   - IDUtil：唯一ID生成工具
   - JWTUtil：JWT令牌生成和验证工具

2. **通用模型**
   - JsonData：统一响应数据封装
   - LoginUser：登录用户信息
   - EventMessage：事件消息模型
   - PageVO：分页数据模型

3. **全局配置**
   - 拦截器配置：登录拦截器、权限拦截器等
   - 异常处理：全局异常处理
   - 跨域配置：CORS跨域配置
   - 序列化配置：JSON序列化配置

4. **常量定义**
   - BizCodeEnum：业务状态码枚举
   - RedisKey：Redis键名常量
   - CacheKeyManager：缓存键管理

#### 技术亮点

1. **代码复用**：抽取公共组件，提高代码复用率
2. **统一规范**：定义系统统一的响应格式、异常处理等规范
3. **减少冗余**：避免重复实现相同功能
4. **易于维护**：集中管理公共组件，便于统一维护和升级

### cloud-app 模块

应用前端服务模块，提供面向用户的Web界面和API接口，统一前端资源管理。

#### 核心功能

1. **页面服务**
   - 用户登录注册页面
   - 短链接管理页面
   - 数据统计分析页面
   - 流量包购买页面

2. **接口聚合**
   - 聚合后端微服务API
   - 提供统一的前端接口
   - 处理复合业务逻辑

3. **静态资源**
   - JavaScript脚本
   - CSS样式表
   - 图片等媒体资源
   - HTML模板

#### 技术亮点

1. **前后端分离**：采用前后端分离架构
2. **响应式设计**：适配不同设备的界面设计
3. **API网关集成**：与API网关无缝集成
4. **单点登录**：支持统一的用户认证

#### 部署说明

1. **Nginx配置**：静态资源由Nginx提供服务
2. **Docker部署**：使用Docker容器化部署
3. **CDN加速**：配合CDN提供更快的访问速度

## 环境要求

### 开发环境
- **JDK 1.8+**：Java开发环境
- **Maven 3.6+**：项目构建工具
- **Node.js 14+**：前端开发环境
- **Git**：版本控制工具

### 运行环境
- **MySQL 8.0+**：主数据库
- **Redis 6.0+**：缓存数据库
- **RabbitMQ 3.8+**：消息队列
- **Nacos 2.0+**：服务注册中心
- **ClickHouse 21.0+**：数据分析库
- **Apache Kafka 2.8+**：消息流处理
- **Apache Flink 1.13+**：流式计算
- **XXL-Job 2.3+**：任务调度中心

## 快速开始

### 1. 环境准备

#### 基础环境安装
```bash
# 安装Docker
curl -fsSL https://get.docker.com | bash -s docker

# 启动基础服务
docker-compose -f docker/docker-compose-base.yml up -d
```

#### 中间件配置
```bash
# Nacos配置
# 1. 访问 http://localhost:8848/nacos
# 2. 用户名/密码: nacos/nacos
# 3. 创建命名空间: cloud-link

# RabbitMQ配置
# 1. 访问 http://localhost:15672
# 2. 用户名/密码: admin/admin
# 3. 创建虚拟主机: dev

# Redis配置（无密码）
redis-cli ping
```

### 2. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE cloud_link DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入数据库脚本
source sql/cloud_link.sql;

-- 创建分表
source sql/sharding_tables.sql;
```

### 3. 配置文件修改

各服务的`application.yml`配置文件需要根据实际环境修改：

```yaml
# Nacos配置
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.0.130:8848
        namespace: 327d6e78-c121-4c52-b83f-1c5c0da33f61

# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cloud_link
    username: root
    password: your_password

# Redis配置
spring:
  redis:
    host: localhost
    port: 6379

# RabbitMQ配置
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin
    virtual-host: dev
```

### 4. 服务启动

#### 启动顺序
1. **基础服务**：MySQL、Redis、RabbitMQ、Nacos
2. **网关服务**：cloud-gateway (端口8000)
3. **业务服务**：
   - cloud-account (端口8001)
   - cloud-data (端口8002)  
   - cloud-link-service (端口8003)
   - cloud-shop (端口8004)
4. **前端应用**：cloud-app (端口8005)

#### 启动命令
```bash
# 方式一：IDE启动
# 直接在IDE中运行各服务的主类

# 方式二：命令行启动
cd cloud-gateway && mvn spring-boot:run
cd cloud-account && mvn spring-boot:run
cd cloud-link-service && mvn spring-boot:run
cd cloud-shop && mvn spring-boot:run
cd cloud-data && mvn spring-boot:run

# 方式三：Docker启动
docker-compose -f docker/docker-compose.yml up -d
```

### 5. 验证服务

```bash
# 检查服务注册情况
curl http://localhost:8848/nacos/v1/ns/instance/list?serviceName=cloud-gateway

# 检查网关路由
curl http://localhost:8000/actuator/gateway/routes

# 测试短链生成
curl -X POST http://localhost:8000/api/link/v1/create \
  -H "Content-Type: application/json" \
  -d '{"originalUrl":"https://www.example.com"}'
```

## API接口文档

### 统一请求格式

所有API请求都遵循统一的响应格式：

```json
{
  "code": 0,
  "msg": "success",
  "data": {},
  "timestamp": 1640995200000
}
```

### 认证机制

系统采用JWT Token认证：

```bash
# 1. 用户登录获取Token
POST /api/account/v1/login
{
  "phone": "13800138000",
  "pwd": "password"
}

# 2. 请求头携带Token
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### 核心接口列表

#### 账户服务 (cloud-account:8001)
- `POST /api/account/v1/register` - 用户注册
- `POST /api/account/v1/login` - 用户登录
- `GET /api/account/v1/detail` - 获取账户信息
- `GET /api/traffic/v1/page` - 流量包列表

#### 短链服务 (cloud-link-service:8003)
- `POST /api/link/v1/create` - 创建短链
- `GET /api/link/v1/page` - 短链列表
- `PUT /api/link/v1/update` - 更新短链
- `DELETE /api/link/v1/delete` - 删除短链
- `GET /api/domain/v1/list` - 域名列表

#### 商品服务 (cloud-shop:8004)
- `GET /api/product/v1/list` - 商品列表
- `POST /api/order/v1/create` - 创建订单
- `GET /api/order/v1/page` - 订单列表
- `POST /api/pay/v1/create` - 发起支付

#### 数据服务 (cloud-data:8002)
- `GET /api/stats/v1/trend` - 访问趋势
- `GET /api/stats/v1/region` - 地域分布
- `GET /api/stats/v1/device` - 设备统计

## 监控运维

### 健康检查

所有服务都提供健康检查端点：

```bash
# 服务健康状态
GET /actuator/health

# 服务信息
GET /actuator/info

# 指标监控
GET /actuator/metrics
```

### 日志管理

系统采用分层日志设计：

```yaml
# logback-spring.xml
logging:
  level:
    fun.timu.cloud.net: INFO
    org.springframework.cloud: WARN
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### 性能调优

#### JVM参数优化
```bash
java -Xms512m -Xmx1g -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=100 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -jar app.jar
```

#### 数据库优化
```sql
-- 短链表索引优化
CREATE INDEX idx_short_link_code ON short_link(short_link_code);
CREATE INDEX idx_account_no ON short_link(account_no);
CREATE INDEX idx_create_time ON short_link(create_time);

-- 流量表分区
ALTER TABLE traffic PARTITION BY RANGE(account_no);
```

#### 缓存策略
```java
// Redis缓存配置
@Cacheable(value = "shortLink", key = "#code", unless = "#result == null")
public ShortLink getByCode(String code) {
    return shortLinkMapper.selectByCode(code);
}
```

## 故障排查

### 常见问题

1. **服务注册失败**
   - 检查Nacos连接配置
   - 确认网络可达性
   - 验证命名空间配置

2. **数据库连接失败**
   - 检查数据库连接参数
   - 确认数据库服务状态
   - 验证用户权限

3. **消息队列异常**
   - 检查RabbitMQ服务状态
   - 确认虚拟主机配置
   - 验证队列和交换机设置

4. **短链访问404**
   - 检查短链码是否存在
   - 确认域名解析配置
   - 验证网关路由规则

### 日志分析

```bash
# 查看服务日志
docker logs -f cloud-gateway
docker logs -f cloud-account

# 搜索错误日志
grep "ERROR" logs/application.log

# 分析访问日志
tail -f logs/access.log | grep "POST /api/link/v1/create"
```

## 项目结构

```
cloud-link/
├── cloud-account/          # 账户服务
├── cloud-app/             # 实时数据处理应用
├── cloud-common/          # 公共组件
├── cloud-data/            # 数据统计服务
├── cloud-gateway/         # API网关
├── cloud-link-service/    # 短链服务
├── cloud-shop/           # 商品服务
├── docker/               # Docker配置
├── sql/                  # 数据库脚本
└── docs/                # 项目文档
```

## 开发规范

### 代码规范
- 遵循阿里巴巴Java开发手册
- 统一使用UTF-8编码
- 接口返回统一JsonData格式
- 异常处理统一使用全局异常处理器

### 数据库规范
- 表名使用小写字母和下划线
- 主键统一使用`id`字段
- 创建时间字段统一命名为`create_time`
- 更新时间字段统一命名为`update_time`

### API设计规范
- RESTful API设计风格
- 版本号通过URL路径指定（如/v1/）
- 状态码遵循HTTP标准
- 请求参数驼峰命名

## 贡献指南

### 开发流程
1. Fork项目到个人仓库
2. 创建feature分支进行开发
3. 提交代码并创建Pull Request
4. 代码审查通过后合并到主分支

### 提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档修改
style: 代码格式调整
refactor: 代码重构
test: 测试用例
chore: 构建工具或辅助工具的变动
```

## 许可证

本项目采用 MIT 许可证，详情请查看 [LICENSE](LICENSE) 文件。

## 联系方式

- 项目主页：https://github.com/username/cloud-link
- 问题反馈：https://github.com/username/cloud-link/issues
- 作者邮箱：author@example.com

---

**Cloud-Link短链接系统** - 高性能、高可用的分布式短链接解决方案