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

SRC_URI = "file://linux-3.14.52-fsimx6sx-Y0.2.tar.bz2"
S = "${WORKDIR}/linux-3.14.52-fsimx6sx-Y0.2"
PV="0.2"

# We need to pass it as param since kernel might support more then one
# machine, with different entry points
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

FSCONFIG = "fsimx6sx_defconfig"

kernel_do_configure_prepend() {
	install -m 0644 ${S}/arch/${ARCH}/configs/${FSCONFIG} ${WORKDIR}/defconfig
}
