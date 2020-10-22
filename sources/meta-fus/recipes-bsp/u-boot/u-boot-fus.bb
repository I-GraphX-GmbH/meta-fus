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
UBOOT_BINARY_mx6 = "uboot.${UBOOT_SUFFIX}"
UBOOT_NAME_mx8 = "u-boot-${MACHINE}.bin-${UBOOT_CONFIG}"

BOOT_TOOLS = "imx-boot-tools"

# Necessary ???
# FIXME: Allow linking of 'tools' binaries with native libraries
#        used for generating the boot logo and other tools used
#        during the build process.
EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CPPFLAGS}" \
                 HOSTLDFLAGS="${BUILD_LDFLAGS}" \
                 HOSTSTRIP=true'


# FIXME: Allow setting boardconfig by environment variables.
#        The environment variables have to be added to the BB_ENV_EXTRAWHITE e.g.:
#        export BB_ENV_EXTRAWHITE=" $BB_ENV_EXTRAWHITE CONFIG_FUS_BOARDTYPE CONFIG_FUS_BOARDREV CONFIG_FUS_FEATURES2"
do_compile_prepend () {
	export CONFIG_FUS_BOARDTYPE=${CONFIG_FUS_BOARDTYPE};
	export CONFIG_FUS_BOARDREV=${CONFIG_FUS_BOARDREV};
	export CONFIG_FUS_FEATURES2=${CONFIG_FUS_FEATURES2};
}

do_deploy_append_mx8m () {
    # Deploy the mkimage, u-boot-nodtb.bin and fsl-imx8mq-XX.dtb for mkimage to generate boot binary
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
                    install -m 0777 ${B}/${config}/arch/arm/dts/${UBOOT_DTB_NAME}  ${DEPLOYDIR}/${BOOT_TOOLS}
                    install -m 0777 ${B}/${config}/u-boot-nodtb.bin  ${DEPLOYDIR}/${BOOT_TOOLS}/u-boot-nodtb.bin-${MACHINE}-${UBOOT_CONFIG}
                fi
            done
            unset  j
        done
        unset  i
    fi

}

PACKAGE_ARCH = "${MACHINE_ARCH}"
