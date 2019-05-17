### */1 * * * * source $HOME/.bash_profile && cd /opt/apps/gsps-parent && ./deploy.sh > $HOME/logs/gsps.log 2>&1
#! /bin/sh
# 当前文件所在目录
current_dir=$(cd `dirname $0`; pwd)
echo "current dir: $current_dir"

# 1.更新项目 ++++++++++++++++++++++++++++++++++
echo "git pull origin master"
v_update_result=`git pull origin master`
echo "git pull result -> $v_update_result"

if [[ $(echo ${v_update_result} | grep "Already up-to-date") != "" ]]
then
    echo "项目没有更新"
    exit 2
fi

# 2.获取需要升级的项目 ++++++++++++++++++++++++++++++++++
declare -A v_module_map=()
v_module_map["ZODIAC-CLOUD-REGISTRATION-RELEASE-TEST.VERSION"]="zodiac-cloud-parent/zodiac-cloud-registration/target/zodiac-cloud-registration-1.0.jar"
v_module_map["ZODIAC-CLOUD-SHARED-RELEASE-TEST.VERSION"]="zodiac-cloud-parent/zodiac-cloud-shared/target/zodiac-cloud-shared-1.0.jar"
v_index=0
for key in ${!v_module_map[@]}
do
    if [[ $(echo ${v_update_result} | grep "$key") != "" ]]
    then
        v_modules[$v_index]=${v_module_map[$key]}
        v_index=$[v_index+1]
    fi
done

# 3.停止项目
for module in ${v_modules[@]}
do
    v_file_path="${current_dir}/${module}"
    v_pid=`ps -ef | grep ${v_file_path} | grep -v grep | grep -v kill | awk '{print $2}'`
    if [[ ${v_pid} ]]; then
        echo "kill -9 $v_pid -> ($v_file_path)"
        kill -9 ${v_pid}
    fi
done


# 4.打包项目 ++++++++++++++++++++++++++++++++++
echo "mvn clean package -e -U -DskipTests"
mvn clean package -e -U -DskipTests -Ptest

# 5.启动项目 ++++++++++++++++++++++++++++++++++
for module in ${v_modules[@]}
do
    v_file_path="${current_dir}/${module}"
    v_config="--spring.profiles.active=test"
    echo "nohup java -jar -Xms384m -Xmx384m ${v_file_path} > /dev/null 2>&1 &"
    nohup java -jar -Xms384m -Xmx384m ${v_file_path} ${v_config} > /dev/null 2>&1 &
done