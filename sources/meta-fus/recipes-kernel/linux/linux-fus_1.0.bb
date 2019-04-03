# Copyright (C) 2016 F&S Elektronik Systeme GmbH
# Released under the GPLv2 license

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

SUMMARY = "Linux Kernel for F&S i.MX6-based boards and modules"

DEPENDS += "lzop-native bc-native"

COMPATIBLE_MACHINE = "(mx6|mx7)"

SRC_URI = "file://linux-4.1.15-fus-Y1.0.tar.bz2"
S = "${WORKDIR}/linux-4.1.15-fus-Y1.0"
PV = "1.0"

# We need to pass it as param since kernel might support more then one
# machine, with different entry points
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

FSCONFIG_mx6 = "fsimx6_defconfig"
FSCONFIG_mx6sx = "fsimx6sx_defconfig"
FSCONFIG_mx6ul = "fsimx6ul_defconfig"
FSCONFIG_mx7ulp = "fsimx7ulp_defconfig"

kernel_do_configure_prepend() {
	install -m 0644 ${S}/arch/${ARCH}/configs/${FSCONFIG} ${WORKDIR}/defconfig
}
