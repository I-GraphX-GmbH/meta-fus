# Common settings for all F&S boards

UBOOT_ENTRYPOINT = "0x80800000"
UBOOT_ENTRYPOINT_mx6 = "0x10800000"
UBOOT_ENTRYPOINT_mx6sx = "0x80800000"
UBOOT_ENTRYPOINT_mx6ul = "0x80800000"
UBOOT_ENTRYPOINT_mx7ulp = "0x60800000"
UBOOT_ENTRYPOINT_mx8mm = "0x40008000"
UBOOT_ENTRYPOINT_mx8m = "0x40008000"
UBOOT_ENTRYPOINT_mx8mn = "0x40008000"
UBOOT_ENTRYPOINT_mx8mp = "0x40008000"

PREFERRED_PROVIDER_u-boot_mx6 = "u-boot-fus"
PREFERRED_PROVIDER_u-boot_mx6ul = "u-boot-fus"
PREFERRED_PROVIDER_u-boot_mx6sx = "u-boot-fus"
PREFERRED_PROVIDER_u-boot_mx7ulp = "u-boot-fsimx7ulp"
PREFERRED_PROVIDER_u-boot_mx8mm = "u-boot-fus"
PREFERRED_PROVIDER_u-boot_mx8m = "u-boot-fus"
PREFERRED_PROVIDER_u-boot_mx8mn = "u-boot-fus"
PREFERRED_PROVIDER_u-boot_mx8mp = "u-boot-fsimx8mp"

PREFERRED_PROVIDER_virtual/bootloader_mx6 = "u-boot-fus"
PREFERRED_PROVIDER_virtual/bootloader_mx6ul = "u-boot-fus"
PREFERRED_PROVIDER_virtual/bootloader_mx6sx = "u-boot-fus"
PREFERRED_PROVIDER_virtual/bootloader_mx7ulp = "u-boot-fsimx7ulp"
PREFERRED_PROVIDER_virtual/bootloader_mx8mm = "u-boot-fus"
PREFERRED_PROVIDER_virtual/bootloader_mx8m = "u-boot-fus"
PREFERRED_PROVIDER_virtual/bootloader_mx8mn = "u-boot-fus"
PREFERRED_PROVIDER_virtual/bootloader_mx8mp = "u-boot-fsimx8mp"

PREFERRED_PROVIDER_virtual/kernel_mx6 = "linux-fus"
PREFERRED_PROVIDER_virtual/kernel_mx6ul = "linux-fus"
PREFERRED_PROVIDER_virtual/kernel_mx6sx = "linux-fus"
PREFERRED_PROVIDER_virtual/kernel_mx7ulp = "linux-fus"
PREFERRED_PROVIDER_virtual/kernel_mx8mm = "linux-fus"
PREFERRED_PROVIDER_virtual/kernel_mx8m = "linux-fus"
PREFERRED_PROVIDER_virtual/kernel_mx8mn = "linux-fus"
PREFERRED_PROVIDER_virtual/kernel_mx8mp = "linux-fus"

IMAGE_FSTYPES_mx7ulp  = "sdcard"

MKUBIFS_ARGS ??= "-m 2048 -e 126976 -c 2048"

# Remove image_types_fsl because there is a awk command which search after a
# point(.) in the devicetrees. But we have 2 points in the devicetree
# picocom1.2.dtb so the awk command find the first point, so the new name is
# picocom1 instead of picocom1.2. And thats why you get an error at compile
# time. Thats why we copy the image_types_fsl and rename it as image_types_fus
# and changed the line in the original file from
# "DTS_BASE_NAME=`basename ${DTS_FILE} | awk -F "." '{print $1}'`" to
# "DTS_BASE_NAME=`basename ${DTS_FILE} | awk -F ".dtb" '{print $1}'`"
# The Original file will be find in sources/meta-fsl-arm/classes/
# image_types_fsl.bbclass
IMAGE_CLASSES_remove = "image_types_fsl"
IMAGE_CLASSES += "image_types_fus"

# necessary, will be set in fsl-imx-preferred-env.inc
UBOOT_SUFFIX = "nb0"
UBOOT_BINARY = "uboot.${UBOOT_SUFFIX}"
UBOOT_WIC_BINARY = "u-boot-dtb.img"
# need hosttools crc32 for U-Boot addfsheader.sh
HOSTTOOLS_NONFATAL_append_mx8m = " crc32"

DISTRO_NAME = "F&S i.MX Release Distro"
SANITY_TESTED_DISTROS += "fedora-27"
