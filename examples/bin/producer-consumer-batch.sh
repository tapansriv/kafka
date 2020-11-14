#!/bin/bash

# start kafka server
# run demo
# stop kafka server
# run python script that moves data to csv, along with configuration
# repeat
# 

append=0
fetch=0
while test $# -gt 0; do
  case "$1" in
    -h|--help)
      echo "options:"
      echo "-h, --help                show brief help"
      echo "-a, --append              check append time"
      echo "-f, --fetch               check fetch time"
      exit 0
      ;;
    -a|--append)
        append=1
        shift
      ;;
    -f|--fetch)
        fetch=1
        shift
      ;;
    *)
      break
      ;;
  esac
done

echo $append
echo $fetch

basedir=/Users/Tapan/kafka
cd $basedir
for i in "100000000 100" "1000000000 10" "10000000 1000"
#for i in "100000000 100" "100000000 10" "100000000 1000"
do
    set -- $i
    echo $1 and $2

    bin/kafka-server-start.sh config/server.properties > ~/out.txt &
    sleep 20
    examples/bin/java-producer-consumer-demo.sh async $1 $2
    echo done
    bin/kafka-server-stop.sh
    sleep 10
    python3 examples/bin/max.py $1 $2 $append $fetch
    sudo du -sh /tmp/kafka-logs
    rm -r /tmp/kafka-logs
done
