SUBARCH_CORE := arch/um/sys-i386/ arch/i386/crypto/

TOP_ADDR := $(CONFIG_TOP_ADDR)

ifeq ($(CONFIG_MODE_SKAS),y)
  ifneq ($(CONFIG_MODE_TT),y)
     START := 0x8048000
  endif
endif

CFLAGS += -U__$(SUBARCH)__ -U$(SUBARCH)
ARCH_USER_CFLAGS :=

ifneq ($(CONFIG_GPROF),y)
ARCH_CFLAGS += -DUM_FASTCALL
endif

ELF_ARCH := $(SUBARCH)
ELF_FORMAT := elf32-$(SUBARCH)

OBJCOPYFLAGS  := -O binary -R .note -R .comment -S

SYS_UTIL_DIR	:= $(ARCH_DIR)/sys-i386/util

SYS_HEADERS := $(SYS_DIR)/sc.h $(SYS_DIR)/thread.h

prepare: $(SYS_HEADERS)

$(SYS_DIR)/sc.h: $(SYS_UTIL_DIR)/mk_sc
	$(call filechk,gen_header)

$(SYS_DIR)/thread.h: $(SYS_UTIL_DIR)/mk_thread 
	$(call filechk,gen_header)

$(SYS_UTIL_DIR)/mk_sc: scripts_basic $(ARCH_DIR)/user-offsets.h FORCE
	$(Q)$(MAKE) $(build)=$(SYS_UTIL_DIR) $@

$(SYS_UTIL_DIR)/mk_thread: scripts_basic $(ARCH_DIR)/kernel-offsets.h FORCE
	$(Q)$(MAKE) $(build)=$(SYS_UTIL_DIR) $@

$(SYS_UTIL_DIR): scripts_basic include/asm FORCE
	$(Q)$(MAKE) $(build)=$(SYS_UTIL_DIR)

CLEAN_FILES += $(SYS_HEADERS)