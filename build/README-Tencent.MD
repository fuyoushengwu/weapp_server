本文用于指导腾讯云上搭建Kubernets环境
#### 目标
|Hostname|IP Address|Internal Address|Role|CPU|Memory|
|:---|:---|:---|:---|:---|:---|:---|
|centos-1|49.234.14.235|172.17.0.16|master,work|2|4G|
|centos-2|49.234.11.229|172.17.0.14|worker|2|4G|

#### 一. 安装 Docker 
```
yum update -y 
yum install docker -y 
systemctl enable docker & systemctl start docker
systemctl status docker 

# 测试 Docker安装完成
docker pull nginx
docker container run -d -p 127.0.0.1:8080:80 --rm --name mynginx nginx
curl localhost:8080
```

#### 二.配置网络

+ 2.1 配置/etc/hosts
```
hostname centos-1
hostname centos-2
# 配置'/etc/hosts'
cat  << EOF >>/etc/hosts
172.17.0.16 centos-1
172.17.0.14 centos-2
EOF
```

+ 2.2 配置上网代理
+ + 安装Shadowsocks Client(centos-1)
```
# 安装 pip
curl "https://bootstrap.pypa.io/get-pip.py" -o "get-pip.py"
python get-pip.py

# 安装 Shadowsocks Client
pip install --upgrade pip
pip install shadowsocks

# 配置shadowsocks.json
cat << EOF >/etc/shadowsocks.json
{
  "server":"104.225.239.142",
  "server_port":8888,
  "local_address": "127.0.0.1",
  "local_port":1080,
  "password":"g0Ekw&1BAJw&tHQy",
  "timeout":300,
  "method":"aes-256-cfb",
  "workers": 1
}
EOF

# 启动
nohup sslocal -c /etc/shadowsocks.json /dev/null 2>&1 &
echo " nohup sslocal -c /etc/shadowsocks.json /dev/null 2>&1 &" /etc/rc.local   #设置自启动
# 验证
curl --socks5 127.0.0.1:1080 http://httpbin.org/ip
```

+ + 安装Privoxy(centos-1)
```
# 下载 Privoxy
wget https://ayera.dl.sourceforge.net/project/ijbswa/Sources/3.0.26%20%28stable%29/privoxy-3.0.26-stable-src.tar.gz
tar -zxvf privoxy-3.0.26-stable-src.tar.gz
# 编译 Privoxy
cd privoxy-3.0.26-stable
yum groupinstall "Development Tools" -y
autoheader && autoconf
./configure
make
# 建立账户
sudo useradd privoxy -r -s /usr/sbin/nologin
# 安装
sudo make install
# 更改侦听地址
sed -i 's/^listen-address  127.0.0.1:8118$/listen-address  172.17.0.16:8118/' /usr/local/etc/privoxy/config
cat << EOF >>/usr/local/etc/privoxy/config
forward-socks5 / 127.0.0.1:1080 .
forward 10.*.*.*/ .
forward 192.168.*.*/ .
forward 127.*.*.*/ .
forward 172.17.*.*/ .
forward 172.10.*.*/ .
forward localhost/ .
EOF

# 启动Privoxy
privoxy --user privoxy /usr/local/etc/privoxy/config
```

+ + 配置上网代理

```
cat << EOF >>/etc/profile
http_proxy="http://172.17.0.16:8118"
https_proxy="http://172.17.0.16:8118"
no_proxy="127.0.0.1,172.17.0.16,172.17.0.14,centos-1,centos-2,centos-3,centos-4,localhost,github.com,mirrors.aliyun.com,.mirror.aliyuncs.com,registry.cn-hangzhou.aliyuncs.com,.tencentyun.com"

export http_proxy
export https_proxy
export no_proxy
EOF
source /etc/profile
```

#### 三.配置Kubernetes执行环境

+ 3.1 开启命令行界面
```
systemctl set-default multi-user.target
```
+ 3.2 关闭防火墙
```
systemctl stop firewalld & systemctl disable firewalld
cat <<EOF >  /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
echo 1 > /proc/sys/net/bridge/bridge-nf-call-iptables
echo 1 > /proc/sys/net/bridge/bridge-nf-call-ip6tables
```
+ 3.3 关闭SELinux
```
sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config
sed -i 's/SELINUXTYPE=targeted/#&/' /etc/selinux/config
setenforce 0
```
+ 3.4 关闭Swap
```
swapoff -a&& sysctl -w vm.swappiness=0
sed -i '/ swap / s/^/#/' /etc/fstab
```
+ 3.5 配置Docker
```
mkdir -p /etc/systemd/system/docker.service.d/
cat << EOF > /etc/systemd/system/docker.service.d/http-proxy.conf
[Service]
Environment="HTTP_PROXY=http://172.17.0.16:8118" "HTTPS_PROXY=http://172.17.0.16:8118" "NO_PROXY=127.0.0.1,172.17.0.16,172.17.0.14,centos-1,centos-2,centos-3,centos-4,localhost,mirrors.aliyun.com,.mirror.aliyuncs.com,registry.cn-hangzhou.aliyuncs.com,.tencentyun.com"
EOF

systemctl daemon-reload
systemctl restart docker
systemctl status docker

```

#### 二.准备Kubernetes
+ 1. 安装Kubernetes组件
```
# 安装Kubernetes的yum源
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
        http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF

# 安装 kubelet kubeadm kubectl
yum install -y kubelet kubeadm kubectl

# 启动kubelet
systemctl enable kubelet && systemctl start kubelet


# 安装Kubernetes自动补全(centos-1)
mkdir -p $HOME/.kube
kubeadm completion bash > ~/.kube/kubeadm_completion.bash.inc
printf "\n# Kubeadm shell completion\nsource '$HOME/.kube/kubeadm_completion.bash.inc'\n" >> $HOME/.bash_profile
source $HOME/.bash_profile

# Master初始化(centos-1)
kubeadm init --pod-network-cidr=172.10.0.0/16  --apiserver-advertise-address=172.17.0.16

# 配置kube(centos-1)
cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
chown $(id -u):$(id -g) $HOME/.kube/config

# 安装etcd工具(centos-1)
rm -rf /root/{etcd-v3.3.10-linux-amd64.tar.gz,etcd-v3.3.10-linux-amd64}
cd /root/
wget https://github.com/etcd-io/etcd/releases/download/v3.3.10/etcd-v3.3.10-linux-amd64.tar.gz
tar zxvf /root/etcd-v3.3.10-linux-amd64.tar.gz -C /root
rm -rf /usr/bin/etcdctl
cp -p /root/etcd-v3.3.10-linux-amd64/etcdctl /usr/bin/

# 查看etcd 集群情况(centos-1)
etcdctl --key-file /etc/kubernetes/pki/etcd/server.key --cert-file /etc/kubernetes/pki/etcd/server.crt --ca-file /etc/kubernetes/pki/etcd/ca.crt --endpoints https://172.17.0.16:2379  member list
etcdctl --key-file /etc/kubernetes/pki/etcd/server.key --cert-file /etc/kubernetes/pki/etcd/server.crt --ca-file /etc/kubernetes/pki/etcd/ca.crt --endpoints https://172.17.0.16:2379  cluster-health


# Worker初始化(centos-2,centos-2,centos-3),根据kubeadm init执行结果,执行
kubeadm join 172.17.0.16:6443 --token [token] --discovery-token-ca-cert-hash [hash]

# 安装Flannel网络(centos-1)
kubectl apply -f kube-flannel/kube-flannel.yml
```

# 2.将Master作为工作节点
```
kubectl taint nodes --all node-role.kubernetes.io/master-
```

#### 三. 测试Kubernete环境(kubectl命令在centos-1上执行)
+ 1.测试CoreDNS
```
kubectl apply -f demo/busybox.yaml
# 在生成的container中执行
kubectl exec -it busybox nslookup kubernetes
```

+ 2. 部署 NFS Storage Class
+ + 2.1 在centos-1上提供NFS Service
```
mkdir -p /var/nfs
cat << EOF >/etc/exports
/var/nfs     172.17.0.0/24(rw,sync,no_root_squash,no_all_squash)
EOF

yum -y install nfs-utils
systemctl enable rpcbind &&systemctl start rpcbind
systemctl enable nfs &&systemctl start nfs
```

+ + 2.2 在kubernetes上部署StorageClass
```
# 安装StorageClass(centos-1)
kubectl apply -f kube_nfs/kube_nfs_tencent.yaml
```

+ + 2.3 测试NFS Storage Class
```
kubectl apply -f demo/nfs_test.yaml
```

+ 3. 部署Redis
+ + 3.1 部署Redis
```
kubectl apply -f kube_redis/redis.yaml
```

# 四.部署WeAPP依赖的应用
+ 1. 部署MySQL5.5数据库(centos-1)
+ + 1.1 检查并卸载centos-1上已安装的Mariadb,MySQL
```
rpm -e --nodeps `rpm -qa | grep mariadb`
rpm -e --nodeps `rpm -qa | grep mysql`
rm -rf `find / -name mysql`
```
+ + 1.2 安装MySQL的YUM Repository
```
wget http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm
rpm -ivh mysql-community-release-el7-5.noarch.rpm
# 修改　/etc/yum.repos.d/mysql-community.repo 里把　5.5开启　把5.6禁用
rm -rf /etc/yum.repos.d/mysql-community.repo && cp -p mysql/mysql-community.repo /etc/yum.repos.d/
```
+ + 1.3 安装MySQL
```
yum install -y mysql-community-server
``

+ + 1.4 配置MySQL
```
systemctl enable mysqld && systemctl start mysqld
mysqladmin -u root password 'huawei123'

# 登录mysql
mysql -u root -p
# 在mysql中执行
use mysql;
update user set host = '%' where user = 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
flush privileges;
```

+ 2. 部署Java-Maven编译环境
+ + 2.1 安装Java
```
rpm -e --nodeps `rpm -qa | grep java`

tar -zxvf java/jdk-8u201-linux-x64.tar.gz 
mv jdk1.8.0_201 /usr/local

cat << EOF >> /etc/profile
export JAVA_HOME=/usr/local/jdk1.8.0_201
export JRE_HOME=\$JAVA_HOME/jre
export CLASSPATH=.:\$JAVA_HOME/lib/dt.jar:\$JAVA_HOME/lib/tools.jar:\$JRE_HOME/lib
export PATH=\$JAVA_HOME/bin:\$PATH
EOF

source /etc/profile
```

+ + 2.2 安装Maven
```
rpm -e --nodeps `rpm -qa | grep maven`

tar -zxvf maven/apache-maven-3.3.1-bin.tar.gz 
mv apache-maven-3.3.1 /usr/local

cat << EOF >> /etc/profile
export MAVEN_HOME=/usr/local/apache-maven-3.3.1
export PATH=\$MAVEN_HOME/bin:\$PATH
EOF

source /etc/profile
```
+ 3. SCP 免密码传输
+ + 3.1 centos-1生成公钥
```
ssh-keygen -t rsa
```
+ + 3.2 centos-2,centos-3,centos-4信任公钥
```
scp root@172.17.0.16:/root/.ssh/id_rsa.pub /root/id_rsa.pub
cat /root/id_rsa.pub >>/root/.ssh/authorized_keys
```

+ 4. 部署rabbitmq
+ + 4.1 部署rabbitmq(centos-1)
```
kubectl apply -f rabbitmq/rabbitmq.yml
```

+ 5.解决service-node-port-range只能是30000-32767的情况
在“command字段对应的值中添加”–-service-node-port-range=80-32767″