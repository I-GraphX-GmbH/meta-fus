# Copyright (C) 2022 F&S Elektronik Systeme GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "bootloader for F&S boards and modules"
require recipes-bsp/u-boot/u-boot.inc
inherit pythonnative

PROVIDES += "u-boot"
DEPENDS_append = " python dtc-native bison-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"

SRC_URI = "file://u-boot-2020.04-fus.tar.bz2"
S = "${WORKDIR}/u-boot-2020.04-fus"

UBOOT_MAKE_TARGET = "all"
COMPATIBLE_MACHINE = "(mx6|vf60|mx7ulp|mx8)"

do_deploy_append_mx8x() {
	install -m 644 ${B}/${UBOOT_WIC_BINARY} ${DEPLOY_DIR_IMAGE}/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
