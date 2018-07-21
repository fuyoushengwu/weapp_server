# 停止进程的函数
function stopProcess()
{
    local PID;
    PID=$(pgrep -f $1);
    if [ $? -eq 0 ]; then
        echo stop process $1
        kill -9 $PID
    fi

}