# Copyright (C) 2014 F&S Elektronik Systeme GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-bsp/u-boot/u-boot.inc

DESCRIPTION = "bootloader for F&S boards and modules based on Freescale i.MX6"

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRC_URI = "file://u-boot-2014.07-fus-Y1.0.tar.bz2"
S = "${WORKDIR}/u-boot-2014.07-fus-Y1.0"
PV = "1.0"

UBOOT_MAKE_TARGET = "all"
COMPATIBLE_MACHINE = "(mx6|vf60)"
UBOOT_BINARY = "uboot.${UBOOT_SUFFIX}"


# FIXME: Allow linking of 'tools' binaries with native libraries
#        used for generating the boot logo and other tools used
#        during the build process.
EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CPPFLAGS}" \
                 HOSTLDFLAGS="${BUILD_LDFLAGS}" \
                 HOSTSTRIP=true'

PACKAGE_ARCH = "${MACHINE_ARCH}"
