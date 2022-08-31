# Copyright (C) 2020 F&S Elektronik Systeme GmbH
# Released under the GPLv2 license

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel

SUMMARY = "Linux Kernel for F&S i.MX6/7/8-based boards and modules"

DEPENDS += "lzop-native bc-native"

COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"

SRC_URI = "file://linux-5.4.70-fus.tar.bz2"
S = "${WORKDIR}/linux-5.4.70-fus"

# We need to pass it as param since kernel might support more then one
# machine, with different entry points
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

# This is necessary because there will be some debian packages created and
# these packages must be lower case. Basically we set CONFIG_LOCALVERSION to
# "-F+S". This appends the string "-F+S". Due to the string is upper case we
# need to set variable to convert it to lower case.
KERNEL_MODULE_PACKAGE_SUFFIX = "${@legitimize_package_name(d.getVar('KERNEL_VERSION'))}"

FSCONFIG_mx6 = "fsimx6_defconfig"
FSCONFIG_mx6sx = "fsimx6sx_defconfig"
FSCONFIG_mx6ul = "fsimx6ul_defconfig"
FSCONFIG_mx7ulp = "fsimx7ulp_defconfig"
FSCONFIG_mx8mm = "fsimx8mm_defconfig"
FSCONFIG_mx8m = "fsimx8m_defconfig"
FSCONFIG_mx8mn = "fsimx8mn_defconfig"
FSCONFIG_mx8mp = "fsimx8mp_defconfig"
FSCONFIG_mx8x = "fsimx8x_defconfig"

# Prevent the galcore-module from beeing build, because it is already
# included in the F&S-Linux-Kernel as a build-in
RPROVIDES_kernel-image += "kernel-module-imx-gpu-viv"

do_extraunpack () {
	mv ${WORKDIR}/linux-fus/* ${S}/
}


kernel_do_configure_prepend() {
	install -m 0644 ${S}/arch/${ARCH}/configs/${FSCONFIG} ${WORKDIR}/defconfig
}
