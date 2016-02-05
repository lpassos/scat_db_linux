#!/bin/bash

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

function print_message {
  printf "%-39s" "$1"
}

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

function print_ok {
  tput setaf 2
  printf "%-10s\n" "[ ok ]" 
  tput sgr0
}

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

function print_fail {
  tput setaf 1
  printf "%-10s\n" "[ failed ]"
  tput sgr0
}

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

function checkout_and_clean {
   git checkout -f "$1"
   git clean -f -d
   make mrproper
}

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .


T_ERROR=$(cat symbols.h | \
       egrep "#define[[:space:]]T_ERROR[[:space:]][0-9]+" | \
       cut -d ' ' -f3)              
       
failed_log="failed.log"       
klex="$PWD/klex"

> "$failed_log"

cd $LINUX_SRC

current_branch=$(git branch)

if [[ "$current_branch" == "(no branch)" ]] ; then
    branch="master"
fi

passed="0"
failed="0"

for release in $(git tag | git tag | egrep -v "(.*-rc[0-9]+|.*\.11|.*\.11-tree)") ; do

   # Makes sure no spurious file from a different release is mistakenly
   # kept for analysis

   checkout_and_clean "$release"
      
   for kconfig in $(find -type f -name '*Kconfig*') ; do
   
      print_message "Testing $kconfig"
   
      error=$("$klex" "$kconfig" | egrep "^$T_ERROR;[0-9]*;$")
      
      if [[ "x$error" != "x" ]] ; then
         print_fail  
         failed=$(( failed + 1 ))         
         echo "$release:$kconfig" >> "$failed.log"
         
      else
         print_ok
         passed=$(( passed + 1 ))         
      fi            
   done  
done

git checkout -f "$branch"

echo "=================================="
echo -e "Summary\n\n"
echo "Passed: $passed"
echo "Failed: $failed"
echo "Total: $(( passed + failed ))"
echo "=================================="
