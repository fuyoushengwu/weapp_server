本文用于指导,使用可执行文件,在VBOX虚拟机上搭建4个节点的Kubernetes环境
#### 目标
|Hostname|IP Address|Role|MAC|CPU|Memory|Component|
|:---|:---|:---|:---|:---|:---|:---|:---|
|centos-1|192.168.0.102|master,work|080027D50C2C|4|4G|etcd,kube-api,kube-scheduler,kube-controller,kubectl,kubelet,kube-proxy,flannel|
|centos-2|192.168.0.103|worker|080027119D05|4|4G|kubelet,kube-proxy,flannel|
|centos-3|192.168.0.104|worker|080027015A2E|4|4G|kubelet,kube-proxy,flannel|
|centos-4|192.168.0.105|worker|080027B368B8|4|4G|kubelet,kube-proxy,flannel|

#### 一. 准备
+ 虚拟软件: [VirtualBox-5.2.18-124319-Win.exe](https://download.virtualbox.org/virtualbox/5.2.18/VirtualBox-5.2.18-124319-Win.exe)
+ 系统镜像: [CentOS-7-x86_64-DVD-1708.iso](https://mirrors.oit.uci.edu/centos/7.4.1708/isos/x86_64/CentOS-7-x86_64-DVD-1708.iso)

#### 二. 安装CentOS虚拟机
+ 1. 设置路由器IP-MAC绑定规则

|主机|MAC地址|IP地址|
|:---|:---|:---|
|centos-1|08-00-27-D5-0C-2C|192.168.0.102|
|centos-2|08-00-27-11-9D-05|192.168.0.103|
|centos-3|08-00-27-01-5A-2E|192.168.0.104|
|centos-4|08-00-27-B3-68-B8|192.168.0.105|

+ 2. 准备虚拟机

|名称|类型|版本|内存|硬盘|处理器|系统|网络|MAC|
|:---|:---|:---|:---|:---|:---|:---|:---|:---|
|centos-1|Linux|Red Hat(64-bit)|4096 MB|50G|4|CentOS-7|桥接网卡|080027D50C2C|
|centos-2|Linux|Red Hat(64-bit)|4096 MB|50G|4|CentOS-7|桥接网卡|080027119D05|
|centos-3|Linux|Red Hat(64-bit)|4096 MB|50G|4|CentOS-7|桥接网卡|080027015A2E|
|centos-4|Linux|Red Hat(64-bit)|4096 MB|50G|4|CentOS-7|桥接网卡|080027B368B8|
+ + 2.1 配置/etc/hosts和上网代理

```
# 配置'/etc/hosts'
cat  << EOF >>/etc/hosts
192.168.0.102 centos-1
192.168.0.103 centos-2
192.168.0.104 centos-3
192.168.0.105 centos-4
EOF

# 配置上网代理
cat << EOF >>/etc/profile
http_proxy="http://192.168.0.201:1080"
https_proxy="http://192.168.0.201:1080"
no_proxy="127.0.0.1,192.168.0.102,192.168.0.103,192.168.0.104,192.168.0.105,192.168.0.106,192.168.0.201,192.168.0.202,192.168.0.203,192.168.0.1,192.168.1.1,centos-1,centos-2,centos-3,centos-4,localhost,github.com,mirrors.aliyun.com,.mirror.aliyuncs.com,registry.cn-hangzhou.aliyuncs.com"

export http_proxy
export https_proxy
export no_proxy
EOF
source /etc/profile
```
+ + 2.2 开启命令行界面
```
systemctl set-default multi-user.target
```
+ + 2.3 关闭防火墙
```
systemctl stop firewalld & systemctl disable firewalld
cat <<EOF >  /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
```
+ + 2.4 关闭SELinux
```
sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config
sed -i 's/SELINUXTYPE=targeted/#&/' /etc/selinux/config
setenforce 0
```
+ + 2.5 关闭Swap
```
swapoff -a&& sysctl -w vm.swappiness=0
sed -i '/ swap / s/^/#/' /etc/fstab
```
+ + 2.6 配置yum源
```
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo 
yum makecache
```
+ + 2.7 安装Docker
```
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
yum makecache
yum install docker-ce -y

# 为docker添加私有仓库
mkdir -p /etc/docker
cat << EOF >/etc/docker/daemon.json
{
    "registry-mirrors": ["https://sws6h8pa.mirror.aliyuncs.com"]
}
EOF
mkdir -p /etc/systemd/system/docker.service.d/
cat << EOF > /etc/systemd/system/docker.service.d/http-proxy.conf
[Service]
Environment="HTTP_PROXY=http://192.168.0.201:1080" "HTTPS_PROXY=http://192.168.0.201:1080" "NO_PROXY=127.0.0.1,192.168.0.102,192.168.0.103,192.168.0.104,192.168.0.105,192.168.0.106,192.168.0.201,192.168.0.202,192.168.0.203,192.168.0.1,192.168.1.1,centos-1,centos-2,centos-3,centos-4,localhost,mirrors.aliyun.com,.mirror.aliyuncs.com,registry.cn-hangzhou.aliyuncs.com"
EOF

systemctl start docker & systemctl enable docker
systemctl status docker

# 验证docker安装完成
docker run hello-world
```

#### 三.准备Kubernetes
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
kubeadm init --pod-network-cidr=172.10.0.0/16  --apiserver-advertise-address=192.168.0.102

# 配置kube(centos-1)
cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
chown $(id -u):$(id -g) $HOME/.kube/config

# 安装etcd工具(centos-1)
rm -rf /root/{etcd-v3.3.10-linux-amd64.tar.gz,etcd-v3.3.10-linux-amd64}
cp etcd/etcd-v3.3.10-linux-amd64.tar.gz /root/etcd-v3.3.10-linux-amd64.tar.gz 
tar zxvf /root/etcd-v3.3.10-linux-amd64.tar.gz -C /root
rm -rf /usr/bin/etcdctl
cp -p /root/etcd-v3.3.10-linux-amd64/etcdctl /usr/bin/

# 查看etcd 集群情况(centos-1)
etcdctl --key-file /etc/kubernetes/pki/etcd/server.key --cert-file /etc/kubernetes/pki/etcd/server.crt --ca-file /etc/kubernetes/pki/etcd/ca.crt --endpoints https://192.168.0.102:2379  member list
etcdctl --key-file /etc/kubernetes/pki/etcd/server.key --cert-file /etc/kubernetes/pki/etcd/server.crt --ca-file /etc/kubernetes/pki/etcd/ca.crt --endpoints https://192.168.0.102:2379  cluster-health


# Worker初始化(centos-2,centos-2,centos-3),根据kubeadm init执行结果,执行
kubeadm join 192.168.0.102:6443 --token [token] --discovery-token-ca-cert-hash [hash]

# 安装Flannel网络(centos-1)
kubectl apply -f kube-flannel/kube-flannel.yml
```

# 2.将Master作为工作节点
```
kubectl taint nodes --all node-role.kubernetes.io/master-
```

#### 四. 测试Kubernete环境(kubectl命令在centos-1上执行)
+ 1.测试CoreDNS
```
kubectl apply -f demo/busybox.yaml
# 在生成的container中执行
nslookup kubernetes
```

+ 2. 部署 NFS Storage Class
+ + 2.1 在centos-1上提供NFS Service
```
mkdir -p /var/nfs
cat << EOF >/etc/exports
/var/nfs     192.168.0.0/24(rw,sync,no_root_squash,no_all_squash)
EOF

yum -y install nfs-utils
systemctl enable rpcbind &&systemctl start rpcbind
systemctl enable nfs &&systemctl start nfs
```

+ + 2.2 在kubernetes上部署StorageClass
```
# 因为镜像下载很慢,所用手动导入kube_nfs.yaml中使用到的镜像(在所有worker上执行)
# 安装StorageClass(centos-1)
kubectl apply -f kube_nfs/kube_nfs.yaml
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

# 五.部署WeAPP依赖的应用
+ 1. 部署MySQL5.5数据库(centos-1)
+ + 1.1 检查并卸载centos-1上已安装的Mariadb,MySQL
```
rpm -e --nodeps `rpm -qa | grep mariadb`
rpm -e --nodeps `rpm -qa | grep mysql`
rm -rf `find / -name mysql`
```
+ + 1.2 安装MySQL的YUM Repository
```
rpm -ivh mysql/mysql-community-release-el7-5.noarch.rpm
# 修改　/etc/yum.repos.d/mysql-community.repo 里把　5.5或5.7开启　把　5.6禁用
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
scp root@192.168.0.102:/root/.ssh/id_rsa.pub /root/id_rsa.pub
cat /root/id_rsa.pub >>/root/.ssh/authorized_keys
```

+ 4. 部署rabbitmq
+ + 4.1 部署rabbitmq(centos-1)
```
kubectl apply -f rabbitmq/rabbitmq.yml
```

+ 5.解决service-node-port-range只能是30000-32767的情况
在“command字段对应的值中添加”–-service-node-port-range=80-32767″