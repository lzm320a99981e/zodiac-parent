### */1 * * * * source $HOME/.bash_profile && cd /opt/apps/gsps-parent && ./deploy.sh > $HOME/logs/gsps.log 2>&1
#! /bin/sh
# 当前文件所在目录
current_dir=$(cd `dirname $0`; pwd)
echo "current dir: $current_dir"

# 更新项目 ++++++++++++++++++++++++++++++++++
echo "git pull origin master"
v_update_result=`git pull origin master`
echo "git pull result -> $v_update_result"

# 判断是否需要重启 ++++++++++++++++++++++++++++++++++
# 1.项目是否更新 ++++++++++++++++++++++++++++++++++
v_not_update="Already up to date"
v_update_flag=$(echo ${v_update_result} | grep "${v_not_update}")
if [[ ${v_update_flag} != "" ]]
then
    echo "项目没有更新"
    exit 2
fi

# 2.获取需要升级的模块 ++++++++++++++++++++++++++++++++++
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

for module in ${v_modules}
do
    echo ${module}
done