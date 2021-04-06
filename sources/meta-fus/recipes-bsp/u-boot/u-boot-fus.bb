# Copyright (C) 2020 F&S Elektronik Systeme GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "bootloader for F&S boards and modules"
require recipes-bsp/u-boot/u-boot.inc
inherit pythonnative

PROVIDES += "u-boot"
DEPENDS_append = " python dtc-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRC_URI = "file://u-boot-2018.03-fus.tar.bz2"
# Set the u-boot environment variable "mode" to rw if it is not a read-only-rootfs
SRC_URI += '${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs", "", "file://0001-Set-file-system-RW.patch",d)}'
S = "${WORKDIR}/u-boot-2018.03-fus"

UBOOT_MAKE_TARGET = "all"
COMPATIBLE_MACHINE = "(mx6|vf60|mx8)"

# Necessary ???
# FIXME: Allow linking of 'tools' binaries with native libraries
#        used for generating the boot logo and other tools used
#        during the build process.
#EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CPPFLAGS}" \
#                 HOSTLDFLAGS="${BUILD_LDFLAGS}" \
#                 HOSTSTRIP=true'

do_deploy_mx8m() {
	install -m 644 ${B}/${UBOOT_WIC_BINARY} ${DEPLOY_DIR_IMAGE}/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
