本文用于指导,使用可执行文件,在VBOX虚拟机上搭建4个节点的Kubernetes环境
#### 目标
|Hostname|IP Address|Role|MAC|CPU|Memory|Component|
|:---|:---|:---|:---|:---|:---|:---|:---|
|centos-1|192.168.0.102|master,work|080027D50C2C|4|4G|etcd,kube-api,kube-scheduler,kube-controller,kubectl,kubelet,kube-proxy,flannel|
|centos-2|192.168.0.103|worker|080027119D05|4|4G|kubelet,kube-proxy,flannel|
|centos-3|192.168.0.104|worker|080027015A2E|4|4G|kubelet,kube-proxy,flannel|
|centos-4|192.168.0.105|worker|080027BD3257|4|4G|kubelet,kube-proxy,flannel|
|centos-5|192.168.0.106|worker|080027CBA7C5|4|4G|kubelet,kube-proxy,flannel|
|centos-6|192.168.0.107|worker|0800272A9ABB|4|4G|kubelet,kube-proxy,flannel|

#### 一.准备
+ 虚拟软件: [VirtualBox-5.2.18-124319-Win.exe](https://download.virtualbox.org/virtualbox/5.2.18/VirtualBox-5.2.18-124319-Win.exe)
+ 系统镜像: [CentOS-7-x86_64-DVD-1708.iso](https://mirrors.oit.uci.edu/centos/7.4.1708/isos/x86_64/CentOS-7-x86_64-DVD-1708.iso)

#### 二.安装配置CentOS虚拟机
+ 1.设置路由器IP-MAC绑定规则

|主机|MAC地址|IP地址|
|:---|:---|:---|
|centos-1|08-00-27-D5-0C-2C|192.168.0.102|
|centos-2|08-00-27-11-9D-05|192.168.0.103|
|centos-3|08-00-27-01-5A-2E|192.168.0.104|
|centos-4|08-00-27-BD-32-57|192.168.0.105|
|centos-5|08-00-27-CB-A7-C5|192.168.0.106|
|centos-6|08-00-27-2A-9A-BB|192.168.0.107|

+ 2.准备虚拟机

|名称|类型|版本|内存|硬盘|处理器|系统|网络|MAC|
|:---|:---|:---|:---|:---|:---|:---|:---|:---|
|centos-1|Linux|Red Hat(64-bit)|4096 MB|50G|4|CentOS-7|桥接网卡|080027D50C2C|
|centos-2|Linux|Red Hat(64-bit)|4096 MB|50G|4|CentOS-7|桥接网卡|080027119D05|
|centos-3|Linux|Red Hat(64-bit)|4096 MB|50G|4|CentOS-7|桥接网卡|080027015A2E|
|centos-4|Linux|Red Hat(64-bit)|4096 MB|50G|4|CentOS-7|桥接网卡|080027BD3257|
|centos-5|Linux|Red Hat(64-bit)|4096 MB|50G|4|CentOS-7|桥接网卡|080027CBA7C5|
|centos-6|Linux|Red Hat(64-bit)|4096 MB|50G|4|CentOS-7|桥接网卡|0800272A9ABB|
+ + 2.1.配置/etc/hosts和上网代理

```
# 配置'/etc/hosts'
cat  << EOF >>/etc/hosts
192.168.0.102 centos-1
192.168.0.103 centos-2
192.168.0.104 centos-3
192.168.0.105 centos-4
192.168.0.106 centos-5
192.168.0.107 centos-6
EOF

# 配置上网代理
cat << EOF >>/etc/profile
http_proxy="http://192.168.0.203:1080"
https_proxy="http://192.168.0.203:1080"
no_proxy="127.0.0.1,192.168.0.102,192.168.0.103,192.168.0.104,192.168.0.105,192.168.0.106,192.168.0.107,192.168.0.201,192.168.0.202,192.168.0.203,192.168.0.204,192.168.0.1,192.168.1.1,centos-1,centos-2,centos-3,centos-4,centos-5,centos-6,localhost,github.com,.aliyun.com,.aliyuncs.com"

export http_proxy
export https_proxy
export no_proxy
EOF
source /etc/profile
```
+ + 2.2.开启命令行界面
```
systemctl set-default multi-user.target
```

+ + 2.3.关闭防火墙
```
systemctl stop firewalld & systemctl disable firewalld
cat <<EOF >  /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
```

+ + 2.4.关闭SELinux
```
sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config
sed -i 's/SELINUXTYPE=targeted/#&/' /etc/selinux/config
setenforce 0
```

+ + 2.5.关闭Swap
```
swapoff -a&& sysctl -w vm.swappiness=0
sed -i '/ swap / s/^/#/' /etc/fstab
```

#### 三.准备Docker+Kubernetes

+ 1.配置yum源
```
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo 
yum makecache
```

+ 2.安装Docker
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
Environment="HTTP_PROXY=http://192.168.0.203:1080" "HTTPS_PROXY=http://192.168.0.203:1080" "NO_PROXY=127.0.0.1,192.168.0.102,192.168.0.103,192.168.0.104,192.168.0.105,192.168.0.106,192.168.0.107,192.168.0.201,192.168.0.202,192.168.0.203,192.168.0.204,192.168.0.1,192.168.1.1,centos-1,centos-2,centos-3,centos-4,centos-5,centos-6,localhost,.aliyun.com,.aliyuncs.com"
EOF

systemctl daemon-reload
systemctl start docker & systemctl enable docker
systemctl status docker

# 验证docker安装完成
docker run hello-world
```

+ 3.安装Kubernetes组件
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
yum install -y kubelet kubeadm kubectl git
git clone https://github.com/fuyoushengwu/weapp_server.git
cd weapp_server
cd build && rm -rf base_image/kubectl
cp -rf /usr/bin/kubectl base_image/ 

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

# Worker初始化(centos-2,centos-3,centos-4,centos-5,centos-6),根据kubeadm init执行结果,执行
kubeadm join 192.168.0.102:6443 --token [token] --discovery-token-ca-cert-hash [hash]
# # 注意:token 24小時过期后查看token和cert-hash
# token
kubeadm token create
# cert-hash
openssl x509 -pubkey -in /etc/kubernetes/pki/ca.crt  | openssl rsa -pubin -outform der 2>/dev/null | openssl dgst -sha256 -hex | sed 's/^.* /sha256:/'

# 安装Flannel网络(centos-1)
kubectl apply -f kube-flannel/kube-flannel.yml
```

+ 4.将Master作为工作节点(centos-1)
```
kubectl taint nodes --all node-role.kubernetes.io/master-
```

+ 5.安装etcd工具(centos-1)
```
rm -rf /root/{etcd-v3.3.10-linux-amd64.tar.gz,etcd-v3.3.10-linux-amd64}
cp etcd/etcd-v3.3.10-linux-amd64.tar.gz /root/etcd-v3.3.10-linux-amd64.tar.gz 
tar zxvf /root/etcd-v3.3.10-linux-amd64.tar.gz -C /root
rm -rf /usr/bin/etcdctl
cp -p /root/etcd-v3.3.10-linux-amd64/etcdctl /usr/bin/

# 查看etcd 集群情况(centos-1)
etcdctl --key-file /etc/kubernetes/pki/etcd/server.key --cert-file /etc/kubernetes/pki/etcd/server.crt --ca-file /etc/kubernetes/pki/etcd/ca.crt --endpoints https://192.168.0.102:2379  member list
etcdctl --key-file /etc/kubernetes/pki/etcd/server.key --cert-file /etc/kubernetes/pki/etcd/server.crt --ca-file /etc/kubernetes/pki/etcd/ca.crt --endpoints https://192.168.0.102:2379  cluster-health
```



#### 四. 准备WeApp运行环境(kubectl命令在centos-1上执行)
+ 0.解决service-node-port-range只能是30000-32767的情况
```
在“/etc/kubernetes/manifests/kube-apiserver.yaml中添加启动参数”–-service-node-port-range=80-32767″
```

+ 1.测试CoreDNS
```
kubectl apply -f demo/busybox.yaml
# 在生成的container中执行
kubectl exec -it busybox nslookup kubernetes
```

+ 2.部署 NFS Storage Class
+ + 2.1.在centos-1上提供NFS Service
```
mkdir -p /var/nfs
cat << EOF >/etc/exports
/var/nfs     192.168.0.0/24(rw,sync,no_root_squash,no_all_squash)
EOF

yum -y install nfs-utils
systemctl enable rpcbind &&systemctl start rpcbind
systemctl enable nfs &&systemctl start nfs
```

+ + 2.2.在kubernetes上部署StorageClass
```
# 因为镜像下载很慢,所用手动导入kube_nfs.yaml中使用到的镜像(在所有worker上执行)
# 安装StorageClass(centos-1)
kubectl apply -f kube_nfs/kube_nfs.yaml
```

+ + 2.3 测试NFS Storage Class
```
kubectl apply -f demo/nfs_test.yaml
```

+ 3.部署rabbitmq(centos-1)
```
kubectl apply -f rabbitmq/rabbitmq.yml
```

+ 4.部署MySQL5.5数据库(centos-1)
+ + 4.1.检查并卸载centos-1上已安装的Mariadb,MySQL
```
rpm -e --nodeps `rpm -qa | grep mariadb`
rpm -e --nodeps `rpm -qa | grep mysql`
rm -rf `find / -name mysql`
```

+ + 4.2.安装MySQL的YUM Repository
```
wget http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm
rpm -ivh mysql-community-release-el7-5.noarch.rpm
# 修改　/etc/yum.repos.d/mysql-community.repo 里把　5.5开启　把5.6禁用
git checkout mysql/mysql-community.repo
rm -rf /etc/yum.repos.d/mysql-community.repo && cp -p mysql/mysql-community.repo /etc/yum.repos.d/
```

+ + 4.3.安装MySQL
```
yum install -y mysql-community-server
```

+ + 4.4.配置MySQL
```
systemctl enable mysqld && systemctl start mysqld
mysqladmin -u root password 'PASSWORD'

# 登录mysql
mysql -u root -p
# 在mysql中执行
use mysql;
update user set host = '%' where user = 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
flush privileges;
```

# 五.准备WeApp编译环境
+ 1.部署Java-Maven编译环境
+ + 1.1.安装Java
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

+ + 1.2.安装Maven
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

+ 2.SCP 免密码传输
+ + 2.1.centos-1生成公钥
```
ssh-keygen -t rsa
```

+ + 2.2.centos-2,centos-3,centos-4,centos-5,centos-6信任公钥
```
scp root@192.168.0.102:/root/.ssh/id_rsa.pub /root/id_rsa.pub
cat /root/id_rsa.pub >>/root/.ssh/authorized_keys
```

+ 3.安装Jenkins
+ + 3.1.部署Jenkins
```
# 部署Ingress
kubectl apply -f ingress/

# 创建Ingress-Secret
kubectl create namespace jenkins-namespace
kubectl create secret tls jenkins-secret --key cert/jenkins/ingress.key --cert cert/jenkins/ingress.crt -n jenkins-namespace
# 部署Jenkins
kubectl apply -f jenkins/
mkdir -p /var/nfs/jenkins_slave/
cp -rf /root/.kube /var/nfs/jenkins_slave/
```
+ +  3.2.配置Jenkinss
+ + + 1.安装插件Jenkins
+ + + 2.系统配置

Kubernetes
|key|value|
|:---|:---|
|名称|kubernetes|
|Kubernetes地址|https://kubernetes.default.svc.cluster.local|
|Kubernetes命名空间|jenkins-namespace|
|Jenkins地址|http://jenkins.jenkins-namespace.svc.cluster.local:8080|

Pod
|key|value|
|:---|:---|
|名称|jnlp|
|命名空间|jenkins-namespace|
|标签列表|haimaxy-jnlp|
|用法|尽可能的使用这个节点|
|Service Account|jenkins|

容器:
|key|value|
|:---|:---|
|名称|jnlp|
|Docker镜像|cnych/jenkins:jnlp|
|工作目录|/home/jenkins|

卷(Host Path Volume)
|key|value|
|:---|:---|
|主机路径|/var/run/docker.sock|
|挂载路径|/var/run/docker.sock|

+ 4.制作podTemplate中使用的基础镜像
```
docker build -t registry.cn-hangzhou.aliyuncs.com/podtemplate/base-image:latest base_image/
```
+ 5.部署Weapp
```
# 创建Weapp-Secret
kubectl create namespace weapp-namespace
kubectl create namespace weapptest-namespace
kubectl create secret tls weapp-secret --key cert/www/ingress.key --cert cert/www/ingress.crt -n weapp-namespace
kubectl create secret tls weapp-secret --key cert/www/ingress.key --cert cert/www/ingress.crt -n weapptest-namespace
# 部署Weapp
kubectl apply -f weapp/
```