### GPG 秘钥操作
```
秘钥列表
gpg --list-keys
/Users/zhangguangyong/.gnupg/pubring.kbx
----------------------------------------
pub   rsa2048 2019-04-16 [SC] [expires: 2021-04-15]
      6D227BD0684D0478818AF3652620D716A712D4F5
uid           [ unknown] lzm320a99981e <lzm320a99981e@gmail.com>
sub   rsa2048 2019-04-16 [E] [expires: 2021-04-15]

生成秘钥
gpg --generate-key

上传公钥
gpg --keyserver hkp://pool.sks-keyservers.net --send-keys 6D227BD0684D0478818AF3652620D716A712D4F5
上传公钥后校验
gpg --keyserver hkp://pool.sks-keyservers.net --recv-keys 6D227BD0684D0478818AF3652620D716A712D4F5
搜索已上传的公钥
gpg --keyserver hkp://pool.sks-keyservers.net --search-keys lzm320a99981e

导出公钥
gpg --output zodiac_pub.gpg --armor --export 6D227BD0684D0478818AF3652620D716A712D4F5
导出私钥
gpg --output zodiac_sec.gpg --armor --export-secret-key 6D227BD0684D0478818AF3652620D716A712D4F5
导入公钥
gpg --import zodiac_pub.gpg
导入私钥
gpg --allow-secret-key-import --import zodiac_sec.gpg

zodiac-component-parent
```

### 修改版本 & 发布
```
mvn versions:set -DnewVersion=1.2
mvn versions:commit
mvn clean deploy -e -U -DskipTests -Prelease
```