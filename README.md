# store-service

### docker 创建mysql
` docker run --name local-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql --default-authentication-plugin=mysql_native_password`

### 生成证书命令
`keytool -genkey -alias tomcat -keyalg RSA -keystore /home/yousuf-zhang/keys/tomcat.keystore`

### ubuntu 端口转发
```aidl
gedit /etc/sysctl.conf
将net.ipv4.ip_forward=0更改为net.ipv4.ip_forward=1
sysctl -p（这条命令是使数据转发功能生效）
iptables -t nat -A PREROUTING -d 192.168.31.110 -p tcp --dport 443 -j DNAT --to-destination 192.168.31.110:8000
iptables -t nat --list检查nat列表信息

Nat列表信息删除：
iptables -t nat -D PREROUTING 1    //序号从1 开始，后边以此+1.

```