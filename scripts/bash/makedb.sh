#!/bin/bash

set -e

# . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 

function get_releases {
  cd "$LINUX_SRC"
  releases=$(git tag | egrep -v "(*-rc*|v2.6.11|v2.6.11-tree)")
  cd - &> /dev/stderr
  echo "$releases" 
}

# . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 

function get_branch {
  cd "$LINUX_SRC"
  branch=$(git branch | grep '*' | cut -d  '*' -f2)
  cd - &> /dev/stderr
  echo "$branch" 
}

# . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 

JAR="scat_linux_db.jar" 

# . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 

cd ../../dist

branch=$(get_branch)

for release in $(get_releases) ; do

  echo "==========================="
  echo -e "Inspecting $release\n\n"

  java -cp lib -Dlinux.repo="$LINUX_SRC"   \
       -Dsql.scripts.dir="../scripts/sql/" \
       -Dbash.scripts.dir="../scripts/bash" \
       -jar "$JAR" --makedb -p -nr "$release"
done

git clean -f -d
git checkout -f "$branch"
