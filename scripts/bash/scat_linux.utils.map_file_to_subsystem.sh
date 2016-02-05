#!/bin/bash

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
# Author: Leonardo Passos (lpassos@gsd.uwaterloo.ca)
# Date: January, 2014
#
#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 

SCRIPTSDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

source "$SCRIPTSDIR/utils.sh"

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 

if [[ "$#" != "1" ]] ; then
  echo "Incorrect usage: map_file_to_subsystem <file_path>" >> /dev/stderr
  exit 1
fi

echo $(entry_type "$1")
