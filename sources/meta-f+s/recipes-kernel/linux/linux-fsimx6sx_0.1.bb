# Copyright (C) 2015 F&S Elektronik Systeme GmbH
# Released under the GPLv2 license

# ###
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

SUMMARY = "Linux Kernel for F&S i.MX6-based boards and modules"

require recipes-kernel/linux/linux-dtb.inc

DEPENDS += "lzop-native bc-native"

COMPATIBLE_MACHINE = "(mx6)"

# ### For now we have the regular IMX kernel with an F&S patch
#SRCBRANCH = "imx_3.10.17_1.0.1_ga"
#SRCREV = "dac46dcf913585956a0e7a838e6f4b7465f00f57"
#LOCALVERSION = "-1.0.1_ga"
#SCMVERSION ?= "y"
#SRC_URI = "git://git.freescale.com/imx/linux-2.6-imx.git;branch=${SRCBRANCH} \
#	file://linux-3.10.17-fsimx6-Y0.1.patch"
#S = "${WORKDIR}/git"

SRC_URI = "file://linux-3.14.28-fsimx6sx-Y0.1.tar.bz2"
S = "${WORKDIR}/linux-3.14.28-fsimx6sx"

# We need to pass it as param since kernel might support more then one
# machine, with different entry points
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

FSCONFIG = "fsimx6sx_defconfig"

kernel_do_configure_prepend() {
	install -m 0644 ${S}/arch/${ARCH}/configs/${FSCONFIG} ${WORKDIR}/defconfig
}
