# Deploy shell script
## set_evn.sh
```bash
# 수행 변수 설정
jar_runtime='c://Program Files/java/jdk1.7.0_79/bin/jar.exe'
war_path_base='c://workspace/project.lab/data/deploy/build/'
war_file='web.war'
...
# WAS 재기동 여부를 결정하기 위해 리소스명을 정규식으로 매칭여부 확인
resource_exp_inc='^(WEB\-INF/classes/[a-zA-Z0-9\_\$/\-]+.class|WEB\-INF/classes/resources/[a-zA-Z0-9\_/\-]+.xml|WEB\-INF/web.xml/lib/[a-zA-Z0-9\_\./\-]+.jar)$'
...
```

## deploy_files.sh
```bash
#!/bin/sh -e
    
show_info() {
    echo "jar_runtime : $jar_runtime"
    ...
check_workspace_path() {
    if [ ! -d "$workspace_path" ]; then
    echo "[ERROR] workspace path is not exist. [$workspace_path]"
    return 1
    fi
}
...
check_jar_file() {
    if [! -f "$jar_runtime" ]; then
    echo "[ERROR] jar is not exist. [$jar_runtime]"
    return 1
    fi
}
...
get_backup_path() {
    if [ ! -d "$backup_path_base" ]; then
    echo "[ERROR] backup base path is not exist. [$backup_path_base]"
    return 1
    fi
    for i in $(seq -f "%03g" 1 100); do
    current_path=$backup_path_base$("date" +%Y%m%d$i)/
    if [ ! -d "$current_path" ]; then
        mkdir --parents $current_path
        eval "$1='$current_path'"
        return 0
    fi
    done
    echo "[ERROR] get backup path file."
    return 1
}
...
check_changes_file() {
    if [ ! -f "$workspace_path$changes_file"]; then
    echo "[ERROR] changes.txt is not exist. [$workspace_path$changes_file]"
    return 1
    fi
    changed_file_unix="$workspace_path$changes_file".unix
    sed -e "s/\r//" $workspace_path$changes_fiile > $changed_file_unix
    
    while read -r p || [ -n "$p" ]; do
    [ "$p" == "" ] && continue;
    
    if [ ! -f $workspace_path$p ]; then
        echo "[ERROR] changes.txt has invalid file name. [$workspace_path$p]"
        return 1
    fi
    if [[ $p =~ $resource_exp_inc ]]; then
        if [[ ! $p =~ $resource_exp_exc ]]; then
        echo "[INFO] WAS reboot required. [$p]"
        eval "$1=restart"
        fi
    fi
    done <$changed_file_unix
}
...
. ./set_env.sh
    
if ! check_jar_file; then
    exit -1
fi
restart=''
if ! check_changes_file restart; then
    exit -1
fi             
```