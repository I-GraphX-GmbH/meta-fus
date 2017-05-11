# Common settings for all F&S boards

UBOOT_ENTRYPOINT = "0x80800000"
UBOOT_ENTRYPOINT_mx6 = "0x10800000"
UBOOT_ENTRYPOINT_mx6sx = "0x80800000"
UBOOT_ENTRYPOINT_mx6ul = "0x80800000"

PREFERRED_PROVIDER_u-boot_mx6 = "u-boot-fus"
PREFERRED_PROVIDER_u-boot_mx6ul = "u-boot-fus"

PREFERRED_PROVIDER_virtual/bootloader_mx6 = "u-boot-fus"
PREFERRED_PROVIDER_virtual/bootloader_mx6ul = "u-boot-fus"

PREFERRED_PROVIDER_virtual/kernel_mx6 = "linux-fus"
PREFERRED_PROVIDER_virtual/kernel_mx6ul = "linux-fus"
