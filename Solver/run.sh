var=$#
string=""

while [ $# -gt 0 ]
do
  string="$string $1"
  shift
done

java -cp Solver.jar Solver $string 
