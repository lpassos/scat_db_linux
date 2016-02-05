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
# Date: March, 2013
#
#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
# Categories

export CORE_CATEGORY="core"
export FS_CATEGORY="fs"
export DRIVER_CATEGORY="driver"
export NET_CATEGORY="net"
export ARCH_CATEGORY="arch"
export FIRMWARE_CATEGORY="firmware"
export MISC_CATEGORY="misc"

# Feature patterns

export FP_KCONFIG="^[[:space:]]*(config|menuconfig|choice)[[:space:]]+[a-zA-Z0-9_]+[[:space:]]*\$"
export FP_CODE="[[:space:]]*#[[:space:]]*(if|ifdef|ifndef|elif)[[:space:]]+(CONFIG_.*|.*[^a-zA-Z0-9_]+CONFIG_.*)"

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
# Description: given a line of the form string1:str2 ... str-n
#              returns a string1:str-n
# Status: tested

function map_entry {
  
  local entry_file=$(   echo "$1"  | awk -F '[: ]' '{ print $1 }')
  local entry_feature=$(echo "$1" | awk -F '[: ]' '{ print $NF }')

  echo "$(entry_type $entry_file):$entry_feature"
}

export -f map_entry

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
# Description: given a directory or file entry in the linux kernel
#              source code tree, returns the location category of that
#              file, i.e., either one of core, driver, net, firmware, 
#              arch or misc
# Status: tested

function entry_type {

  # Checks whether the file is in the include directory
  # If so, apply a mapping specific to includes
 
   if [[ $(echo "$1" | egrep "^(\./)?include/*") == "$1" ]] ; then
      entry_include_type "$1"    

   else

      # File is not an include header. Proceed to normal
      # mapping

      case "$1" in

        # . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
        # Core
        init|init/*|./init|./init/*)                                        echo "$CORE_CATEGORY" ;;
        block|block/*|./block|./block/*)                                    echo "$CORE_CATEGORY" ;;
        ipc|ipc/*|./ipc|./ipc/*)                                            echo "$CORE_CATEGORY" ;;
        kernel|kernel/*|./kernel|./kernel/*)                                echo "$CORE_CATEGORY" ;;
        lib|lib/*|./lib|./lib/*)                                            echo "$CORE_CATEGORY" ;;
        mm|mm/*|./mm|./mm/*)                                                echo "$CORE_CATEGORY" ;;
        virt|virt/*|./virt|./virt/*)                                        echo "$CORE_CATEGORY" ;;

        # . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
        # FS
        fs|fs/*|./fs|./fs/*)                                                echo "$FS_CATEGORY" ;;

        # . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    
        # Driver
        crypto|crypto/*|./crypto|./crypto/*)                                echo "$DRIVER_CATEGORY" ;; 
        drivers|drivers/*|./drivers|./drivers/*)                            echo "$DRIVER_CATEGORY" ;;
        sound|sound/*|./sound|./sound/*)                                    echo "$DRIVER_CATEGORY" ;;
        security|security/*|./security|./security/*)                        echo "$DRIVER_CATEGORY" ;;
#        arch/*/drivers|arch/*/drivers/*|./arch/*/drivers|arch/*/drivers/*)  echo "$DRIVER_CATEGORY" ;;

        # . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
        # Net
        net|net/*|./net|./net/*)                                            echo "$NET_CATEGORY" ;;

        # . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
        # Firmware
        firmware|firmware/*|./firmware|./firmware/*)                        echo "$FIRMWARE_CATEGORY" ;;

        # . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .   
        # Arch
        arch|arch/*|\./arch|\./arch/*)                                      echo "$ARCH_CATEGORY" ;;
      
        # . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
        # Misc
        *)                                                                  echo "$MISC_CATEGORY" ;;  

      esac  
  fi
}

export -f entry_type

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
# Description: given an include directory or a header file inside the 'include'
#              folder of the kernel source code tree, returns the location 
#              category, i.e., either one of core, driver, net, firmware, arch
#              or misc
# Status: tested

function entry_include_type {

  case "$1" in 
    
    include/linux|include/linux/*|./include/linux|./include/linux/*)             echo "$CORE_CATEGORY" ;;
    include/keys|include/keys/*|./include/keys|./include/keys/*)                 echo "$CORE_CATEGORY" ;;
    include/trace|include/trace/*|./include/trace|./include/trace/*)             echo "$CORE_CATEGORY" ;;
    include/uapi|include/uapi/*|./include/uapi|./include/uapi/*)                 echo "$CORE_CATEGORY" ;;

    include/acpi|include/acpi/*|./include/acpi|./include/acpi/*)                 echo "$DRIVER_CATEGORY" ;;
    include/drm|include/drm/*|./include/drm|./include/drm/*)                     echo "$DRIVER_CATEGORY" ;;
    include/crypto|include/crypto/*|./include/crypto|./include/crypto/*)         echo "$DRIVER_CATEGORY" ;;
    include/media|include/media/*|./include/media|./include/media/*)             echo "$DRIVER_CATEGORY" ;;
    include/mtd|include/mtd/*|./include/mtd|./include/mtd/*)                     echo "$DRIVER_CATEGORY" ;;
    include/pcmcia|include/pcmcia/*|./include/pcmcia|./include/pcmcia/*)         echo "$DRIVER_CATEGORY" ;;
    include/target|include/target/*|./include/target|./include/target/*)         echo "$DRIVER_CATEGORY" ;;
    include/rdma|include/rdma/*|./include/rdma|./include/rdma/*)                 echo "$DRIVER_CATEGORY" ;;
    include/rxrpc|include/rxrpc/*|./include/rxrpc|./include/rxrpc/*)             echo "$DRIVER_CATEGORY" ;;
    include/scsi|include/scsi/*|./include/scsi|./include/scsi/*)                 echo "$DRIVER_CATEGORY" ;;
    include/sound|include/sound/*|./include/sound|./include/sound/*)             echo "$DRIVER_CATEGORY" ;;
    include/video|include/video/*|./include/video|./include/video/*)             echo "$DRIVER_CATEGORY" ;;
  
    include/net|include/net/*|.include/net|.include/net/*)                       echo "$NET_CATEGORY" ;;

    include/xen|include/xen/*|./include/xen|./include/xen/*)                     echo "$ARCH_CATEGORY" ;;
    include/math-emu|include/math-emu/*|./include/math-emu|./include/math-emu/*) echo "$ARCH_CATEGORY" ;;    

    *)
        if [[ $(echo "$1" | egrep "^(\./)?include/asm-*") == "$1" ]] ; then 
          echo "$ARCH_CATEGORY"  
        else
          echo "$MISC_CATEGORY"
        fi

  esac
}

export -f entry_include_type

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
# Description: gets the releases of kernel repository
# Status: tested

function get_releases {
  
  # Start from 2.6.12 (strip out v2.6.11 and v2.6.11-tree)

  git tag | egrep -v "*-rc[0-9]+$" | sed '1d;2d'
}

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
# Description: Returns the full path of a given filename, relative to a
#              working directory
# Status: tested

function get_file_full_name {

  local filename="$1"
  local scriptsdir="$2"

  case "$filename" in

    ./*)
      echo "$scriptsdir$(echo $filename | cut -d '.' -f2 )"
    ;;

    /*)
      echo "$1"
    ;;

    *)
      echo "$scriptsdir/$filename"
    ;;

  esac

}

export -f get_file_full_name

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 
# Description: Gets the current branch of the kernel repository
# Status: tested

function get_current_branch {

  local branch=$(git branch | grep "*" | sed "s/* //")

  if [[ "$branch" == "(no branch)" ]] ; then
    branch="master"
  fi

  echo "$branch"
}

export -f get_current_branch

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

function get_all_features_with_location_type {

  # Returns all unique  feature names, preceeded by their location type

  find "$1" -type f -name "*Kconfig*" -print | \
  xargs egrep -o "$FP_KCONFIG" | sed 's/\s\+$//' | xargs -I{} bash -c 'map_entry "$@"' _  {} \| sort -u
}

export -f get_all_features_with_location_type

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
# Description: Gets all source files (C/C++, Assembly and Header files)
# Status: tested

function get_all_src {

  find "$1" -type f \( -name '*\.agh'     -o  \
                       -name '*\.asm'     -o  \
                       -name '*\.asp'     -o  \
                       -name '*\.awk'     -o  \
                       -name '*\.ax'      -o  \
                       -name '*\.bc'      -o  \
                       -name '*\.C'       -o  \
                       -name '*\.c'       -o  \
                       -name '*\.cc'      -o  \
                       -name '*\.cpp'     -o  \
                       -name '*\.cxx'     -o  \
                       -name '*\.cocci'   -o  \
                       -name '*\.ec'      -o  \
                       -name '*\.fuc'     -o  \
                       -name '*\.glade'   -o  \
                       -name '*\.gperf'   -o  \
                       -name '*\.H'       -o  \
                       -name '*\.h'       -o  \
                       -name '*\.hh'      -o  \
                       -name '*\.hpp'     -o  \
                       -name '*\.inc'     -o  \
                       -name '*\.ini'     -o  \
                       -name '*\.inl'     -o  \
                       -name '*\.l'       -o  \
                       -name '*\.pgc'     -o  \
                       -name '*\.pl'      -o  \
                       -name '*\.pm'      -o  \
                       -name '*\.py'      -o  \
                       -name '*\.S'       -o  \
                       -name '*\.s'       -o  \
                       -name '*\.sed'     -o  \
                       -name '*\.seq'     -o  \
                       -name '*\.sh'      -o  \
                       -name '*\.uc'      -o  \
                       -name '*\.ucode'   -o  \
                       -name '*\.xs'      -o  \
                       -name '*\.y'           \
                   \) -and \( -not -name  'Kbuild.asm' -and -not -name  'KBuild.asm' \) \
                      -print                 
}

export -f get_all_src

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

function get_all_c_src {

  find "$1" -type f \( -name '*\.agh'     -o \
                       -name '*\.asp'     -o \
                       -name '*\.c'       -o \
                       -name '*\.ec'      -o \
                       -name '*\.fuc'     -o \
                       -name '*\.H'       -o \
                       -name '*\.h'       -o \
                       -name '*\.hh'      -o \
                       -name '*\.hpp'     -o \
                       -name '*\.inc'     -o \
                       -name '*\.ini'     -o \
                       -name '*\.inl'     -o \
                       -name '*\.pgc'     -o \
                       -name '*\.uc'      -o \
                       -name '*\.ucode'      \) -print                 
}

export -f get_all_c_src

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

function get_vps_from_file_list {

  # Forcing to find macro names prefixed with CONFIG_ 
  # This will decrease the number of comparison in later moments in the script's 

  cat "$1" | xargs egrep "$FP_CODE"
}

export -f get_vps_from_file_list

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

function format_file_list_in_repo {

  echo "Formating files..." 

  local file_list="$1"
  local i="1"

  for file in $(cat "$file_list") ; do

    echo "$i: Formating $file" 

    stripcomments "$file" > "$file.decomment"
    sline "$file.decomment" > "$file"
    rm -f "$file.decomment"        

    i=$(( i + 1 ))

  done

}

export -f format_file_list_in_repo

#. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
