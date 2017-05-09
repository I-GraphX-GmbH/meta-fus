# Copyright (C) 2014 F&S Elektronik Systeme GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-bsp/u-boot/u-boot.inc

DESCRIPTION = "bootloader for F&S boards and modules based on Freescale i.MX6"

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=025bf9f768cbcb1a165dbe1a110babfb"

# ### For now we have the regular IMX U-Boot with an F&S patch
#PV = "v2014.01"
#SRCBRANCH = "patches-2014.01"
#SRC_URI = "git://github.com/Freescale/u-boot-imx.git;branch=${SRCBRANCH} \
#	file://u-boot-2014.01-fsimx6-Y0.2.patch"
#SRCREV = "f5d80303de12e6fefc022426a73136a288f70294"
#S = "${WORKDIR}/git"

SRC_URI = "file://u-boot-2014.01-fsimx6-Y0.2.tar.bz2"
S = "${WORKDIR}/u-boot-2014.01-fsimx6-Y0.2"
PV = "V0.1"

UBOOT_MAKE_TARGET = "all"
COMPATIBLE_MACHINE = "(mxs|mx3|mx5|mx6|vf60)"
UBOOT_BINARY = "uboot.${UBOOT_SUFFIX}"


# FIXME: Allow linking of 'tools' binaries with native libraries
#        used for generating the boot logo and other tools used
#        during the build process.
EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CPPFLAGS}" \
                 HOSTLDFLAGS="${BUILD_LDFLAGS}" \
                 HOSTSTRIP=true'

PACKAGE_ARCH = "${MACHINE_ARCH}"
