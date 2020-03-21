#DNS解析服务器

早期的DNSServer项目，主要木块包括：DNSServer，Console，Dubbo服务
主要思想是尽可能的提高DNSServer的解析性能，防止与数据库和文件直接发生作用，由于DNS记录非常适合使用Redis存储，所以缓存使用
Redis
##一、需要支持的解析类型
A,AAAA,MX,PTR,SOA,CNAME,NS,ANY
##二、需要用到的组件与中间件
DnsJava 主要负责记录更新，解析与应答
Mina 主要负责NETIO支持
Redis 负责对解析数据进行缓存
ActiveMq 负责把需要更新数据库数据进行缓冲，不需要接收处理结果，定期推送统计数据并将结果发送到指定邮箱
Mybatis 配置和数据

##三、数据存储结构 数据存储结构要综合考虑几种不同记录，确定主键与Context


A          url ->ip
AAAA	   url ->ipv6
MX 		   url ->url
PTR		   ip ->url
SOA		   url ->SOARecord
CNAME	   url ->url
NS		   url ->NSRecord
ANY		   url ->any

主要方向是url-> 将A与PTR一并存储

id		key（url)		type		value()


Redis存储方式

key（type+key） value

Redis与数据库存储方式并不完全一致，每个键值的超时时间设置为1Day

策略如下：
更新：正常情况下应该先写数据库后写缓存，但对于DNS服务器来说最重要的是性能，允许一定的差错故

先更新Redis，后将结果发给Mq，消费者执行写数据库操作

查询：

先查询缓存，缓存未命中，直接更新，更新成功写Mq，更新失败读取数据库返回


系统初始化：

将数据库中的记录全部加载缓存，并设置超时1Day












