import sys

num_records = sys.argv[1]
record_size = sys.argv[2]
append_flag = int(sys.argv[3])
fetch_flag = int(sys.argv[4])


basedir = "/Users/Tapan/kafka/data/"
appending_file = basedir + "appending_time.txt"
fetch_file = basedir + "fetch_time.txt"
total_file = basedir + "total_runtime.txt"
outfile = basedir + "final_data.csv"

pieces = []

if append_flag:
    with open(appending_file, "r") as f:
        line = f.readline().strip()
        pieces.append(line) 
else:
    pieces.append("-1")

if fetch_flag:
    with open(fetch_file, "r") as f:
        line = f.readline().strip()
        pieces.append(line)
else:
    pieces.append("-1")

with open(total_file, "r") as f:
    line = f.readline().strip()
    pieces.append(line)

pieces.append(num_records)
pieces.append(record_size)

with open(outfile, "a") as f:
    f.write(",".join(pieces) + "\n")

#with open(appending_file, "r") as f1:
#    with open(total_file, "r") as f2:
#        with open(outfile, "a") as of:
#            line1 = f1.readline().strip()
#            line2 = f2.readline().strip()
#            pieces = [line1, line2, num_records, record_size]
#            of.write(",".join(pieces) + "\n")
#
