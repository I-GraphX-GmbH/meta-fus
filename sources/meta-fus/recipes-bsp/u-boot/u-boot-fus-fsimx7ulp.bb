# Copyright (C) 2014 F&S Elektronik Systeme GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-bsp/u-boot/u-boot.inc

DESCRIPTION = "bootloader for F&S boards and modules based on Freescale i.MX7ULP"

PROVIDES += "u-boot"
DEPENDS_append = " python dtc-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRC_URI = "file://u-boot-fus-fsimx7ulp.tar.bz2"
# Set the u-boot environment variable "mode" to rw if it is not a read-only-rootfs
SRC_URI += '${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs", "", "file://0001-Set-file-system-RW-ulp.patch",d)}'
S = "${WORKDIR}/u-boot-fsimx7ulp"

UBOOT_MAKE_TARGET = "all"
COMPATIBLE_MACHINE = "(mx7ulp)"
UBOOT_BINARY = "u-boot-dtb.${UBOOT_SUFFIX}"


# FIXME: Allow linking of 'tools' binaries with native libraries
#        used for generating the boot logo and other tools used
#        during the build process.
EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CPPFLAGS}" \
                 HOSTLDFLAGS="${BUILD_LDFLAGS}" \
                 HOSTSTRIP=true'

PACKAGE_ARCH = "${MACHINE_ARCH}"
